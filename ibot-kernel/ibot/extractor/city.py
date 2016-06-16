# -*- coding: utf-8 -*-

from __future__ import unicode_literals
from common import get_regs
import re


REGS = get_regs('city')

def _get_city(document):
    '''
    抽取城市地名
    :param document:chunk 包含 sentence,words,labels
    :return: 字典 {"status":1,"city":[北京]} 或者 {"status":0,"city":[北京,上海]} => 1 表示 从公司名抽取,0 表示 全文抽取
    '''

    global REGS

    p = re.compile(u'(%s)' % '|'.join(REGS))
    companyWords = ["公司", "工作室"]

    for sentence in document.sentences:
        # 优先从公司名中抽取
        for kw in companyWords:
            if kw in sentence:
                cityReg = re.search(p, sentence.raw)
                if cityReg:
                    city = cityReg.group()

        # 否则全文抽取
        cityReg = re.findall(p, document.raw)
        # 存放返回值
        city = cityReg
        if "" in city:
            city.remove("")

        return {"status": 0, "city": city}


# 2016.05.26
# 是否能稍微简化一下流程
# 该模块不考虑逻辑流程, 只是单纯考虑从全局抽取城市
# 从公司名抽取城市放在最外层逻辑业务中,
# 考虑传入参数改为字符串
def get_city(raw_str):
    """ 抽取城市名

    @raw_str: 文本字符串
    @rtype: list[str] 城市名
    """
    global REGS

    p = re.compile(u'(%s)' % '|'.join(REGS))
    return p.findall(raw_str)

