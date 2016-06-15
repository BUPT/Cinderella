# -*- coding: utf-8 -*-
# 系统总体逻辑实现

from __future__ import unicode_literals, absolute_import, division
from .preproccess import get_chunks
from .extractor import *
from collections import Counter, defaultdict
from ConfigParser import ConfigParser
import os
import math
import pynlpir

__version__ = '1.0'


def _get_module_path(path):
    real_path = os.path.join(os.getcwd(), os.path.dirname(__file__), path)
    return os.path.normpath(real_path)


def _vote(candidate):
    """ 投票决策
    @cadidate: list
    @rtype: str
    """
    if not candidate:
        return ''
    return Counter(candidate).most_common(1)[0][0]


def _prob(cadidate, most=5):
    if not cadidate:
        return []
    ct = Counter(cadidate)
    total = sum(ct.values())
    return [[v, cnt / total * 0.98] for v, cnt in ct.most_common(most)]


def _weight(cadidate, most=5):
    if not cadidate:
        return []
    merge = defaultdict(float)
    for value, weight in cadidate:
        if merge[value] < weight:
            merge[value] = weight
    return sorted(merge.iteritems(), key=lambda d: d[1], reverse=True)

CONF_PATH = _get_module_path('config.ini')
CONF_PARSER = None


def _get_conf(xpath, default=0):
    global CONF_PARSER
    item, key = xpath.split('/')
    _intValue = lambda d: [d[0], int(d[1])]
    item = dict(map(_intValue, CONF_PARSER.items(item)))
    return item.get(key, default)


def _init(func):
    def wrapper(*args, **kwargs):
        global CONF_PARSER
        if not CONF_PARSER:
            raise RuntimeError('ibot not init.')
        return func(*args, **kwargs)
    return wrapper


def open(conf=CONF_PATH):
    global CONF_PARSER
    if not CONF_PARSER:
        if not os.path.isfile(conf):
            raise IOError('Config file is not exist.')
        CONF_PARSER = ConfigParser()
        CONF_PARSER.read(conf)
    pynlpir.open()


@_init
def get_info(raw_str, sep='\n\n\n', encoding=None):
    """ 从字符串抽取7类信息, 返回json格式结果
    @raw_str: 原始字符串
    @sep: chunk分隔符
    @encoding: 原始字符串编码
    @rtype: json
    """
    global CONF_PARSER

    info = {}

    founders = set()    # 候选成员名
    companies = []  # 候选公司名
    cities = []     # 候选城市名
    equities = []   # 候选股权
    monies = []     # 候选融资额

    startup = ''

    if encoding:
        raw_str = raw_str.decode(encoding)

    # 按chunk分别处理各信息模块
    chunks = get_chunks(raw_str, sep)
    full_word_set = set()   # 整合词袋
    for chunk in chunks:

        # 普通模式全局抽取company, 精准模式按chunk抽取company
        if _get_conf('company/mode', 1) == 2:
            companies.extend(get_company(chunk))
        elif 'company' in chunk.labels:
            companies.extend(get_company(chunk))

        # startup匹配第一个抽取结果
        if not startup and 'startup' in chunk.labels:
            startup = get_startup(chunk)

        # founders按chunk抽取, 内部实现精准模式及普通模式
        if 'founders' in chunk.labels:
            founders |= get_founders(chunk, _get_conf('founders/mode', 1))

        # 从chunk中抽取非公司城市
        if 'city' in chunk.labels:
            cities.extend(get_city(chunk.raw))

        # equity和money按chunk抽取, 内部实现精准模式及普通模式
        if 'equity' in chunk.labels:
            equities.extend(get_equity(chunk, _get_conf('equity/mode', 1)))
        if 'money' in chunk.labels:
            monies.extend(get_money(chunk, _get_conf('money/mode', 1)))

        # 整合词袋用于行业划分
        full_word_set |= chunk.word_set

    # --------------------------------------------------
    # 整合信息
    # --------------------------------------------------

    # 若公司名存在, 优先从公司名提取地名
    # 否则从候选集中投票获得
    cadidate_city = []
    if companies:
        for _company in companies:
            cadidate_city.extend(get_city(_company))
    else:
        cadidate_city = cities

    # equity 备选模式
    equities = _weight(equities)
    if _get_conf('equity/prob', 0) == 1:
        info['equity'] = equities
    else:
        info['equity'] = equities[0][0] if equities else ''

    # money 备选模式
    monies = _weight(monies)
    if _get_conf('money/prob', 0) == 1:
        info['money'] = monies
    else:
        info['money'] = monies[0][0] if monies else ''

    # company 备选模式
    if _get_conf('company/prob', 0) == 1:
        info['company'] = _prob(companies)
    else:
        info['company'] = _vote(companies)

    # city 备选模式
    if _get_conf('city/prob', 0) == 1:
        # city 备选模式做特殊处理
        # 同时考虑公司名及chunk结果
        # 权重 1 / (1 + e ^ (len(comp) - len(chunk)))
        if companies:
            prob_from_company = _prob(cadidate_city)
            prob_from_chunk = _prob(cities)
            prob_all = defaultdict(float)
            weight = 1 / (1 + math.exp(len(cadidate_city) - len(cities)))
            for city, prob in prob_from_company:
                prob_all[city] = prob * weight
            for city, prob in prob_from_chunk:
                prob_all[city] += prob * (1 - weight)
            info['city'] = sorted(prob_all.iteritems(), key=lambda d: d[1], reverse=True)[:5]
        else:
            info['city'] = _prob(cadidate_city)
    else:
        info['city'] = _vote(cadidate_city)

    # 其他信息
    info['startup'] = startup
    info['founders'] = list(founders)
    info['industry'] = get_industry(full_word_set) if full_word_set else ''

    return info
