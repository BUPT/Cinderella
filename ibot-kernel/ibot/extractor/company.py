# -*- coding: utf-8 -*-

from __future__ import unicode_literals
from common import get_regs
import re


REGS = get_regs('city')

# 绝对排除词性
EXCLUDE_POS = (
    'u',    # 助词
    'x',    # 干扰字符串
    'w',    # 标点符号
)
# 用于定位公司前缀
SPLIT_POS = (
    'w',    # 标点符号
    'u',    # 助词
    'x',    # 干扰字符串
    'v',    # 纯动词
    'd',    # 副词
    'c',    # 连词
    'u',    # 助词
    'e',    # 叹词
    'y',    # 语气词
    'm',    # 数量词
)

# 2016.05.26    地名 + .* + 公司
def get_company(document):
    """ 抽取公司名

    @document: Document对象
    @rtype: list[str] 公司名
    """
    global REGS
    global SPLIT_POS

    max_length = 20     # 长度大于20直接过滤

    # 地名 + .* + 公司
    p = re.compile(u'[（]?(%s)(.*?)[^子分了]公司' % '|'.join(REGS))

    companies = []
    for sentence in document.sentences:
        m = p.search(sentence.raw)
        if m:
            valid = True
            tmp_company = m.group()
            start, end = m.span()
            if len(tmp_company) > max_length:
                continue

            # 捕获词序列
            # 同时过滤绝对排除
            company_words = []
            prefix_words = []
            idx = 0
            for word in sentence:
                if idx < start:
                    prefix_words.append(word)
                elif idx < end - 2:
                    if word.pos in EXCLUDE_POS and word.raw not in [u'（', u'）']:
                        valid = False
                        break
                    company_words.append(word)
                else:
                    break
                idx += len(word.raw)

            if not valid: continue

            # 若存在括号, 补充前缀
            if u'）' in tmp_company:
                # 寻找前缀分割点
                split = 0
                for i, word in enumerate(prefix_words[:-1]):
                    if word.pos in SPLIT_POS:
                        split = i + 1

                prefix = ''.join([w.raw for w in prefix_words[split:]])
            else:
                prefix = ''

            company = prefix + tmp_company
            companies.append(company)

    return companies





