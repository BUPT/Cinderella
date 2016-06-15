# -*- coding: utf-8 -*-

from __future__ import unicode_literals
from common import get_regs, get_compile_regs
import re

REGS = get_compile_regs(get_regs('startup'))

def get_startup(document):
    """ 抽取项目名
    @document: Document对象 chunk
    @rtype: str 项目名
    """

    global REGS
    startup = ''
    max_length = 20

    # 存储正则表达式的列表，用于去除格式之后的项目名称筛选
    for sentence in document.sentences:
        text = sentence.raw
        # 文件格式名之前的部分为包含项目名称的部分
        searchObj = re.search(
            r'附件[内容]*[：:]?(.*)(\.pp|\.do|\.pdf|\.wps)', text)

        # 如果匹配到的内容不为空
        if searchObj:
            # 取出包含项目名称的部分，并用reh_list中的规则对其进行匹配
            new_text = searchObj.group(1)
            startup = new_text

            for every_re in REGS:
                new_searchObj = re.search(every_re, new_text)

                if new_searchObj and startup == new_text:
                    # 如果关键字前面的字段长度大于2，则为项目名称
                    if len(new_searchObj.group(1)) >= 2:
                        startup = new_searchObj.group(1)
                        break
                    # 否则，关键字后面为项目名称
                    else:
                        startup = new_searchObj.group(2)
                        break

    # 对项目名称中的一些符号进行替换，将其去除
    # 去除BP
    startup = startup.replace('BP', '')

    # 去除开头的-
    matchObj = re.match('^-*(.*)', startup)
    if matchObj:
        startup = matchObj.group(1)

    # 去除开头的_
    matchObj = re.match(u'^_*(.*)', startup)
    if matchObj:
        startup = matchObj.group(1)

    # 去除开头的——
    matchObj = re.match(u'^——*(.*)', startup)
    if matchObj:
        startup = matchObj.group(1)

    # 去除开头的.
    matchObj = re.match(r'^\.*(.*)', startup)
    if matchObj:
        startup = matchObj.group(1)

    # 去除版本号
    matchObj = re.match(r'(.*)v[0~9]*', startup)
    if matchObj:
        startup = matchObj.group(1)

    matchObj = re.match(r'(.*)V[0~9]*', startup)
    if matchObj:
        startup = matchObj.group(1)

    # 去除末尾的-、_、.等符号
    matchObj = re.match(r'(.*)_$', startup)
    while matchObj:
        startup = matchObj.group(1)
        matchObj = re.match(r'(.*)_$', startup)

    matchObj = re.match(r'(.*)-$', startup)
    while matchObj:
        startup = matchObj.group(1)
        matchObj = re.match(r'(.*)-$', startup)

    matchObj = re.match(r'(.*)\.$', startup)
    while matchObj:
        startup = matchObj.group(1)
        matchObj = re.match(r'(.*)\.$', startup)

    matchObj = re.match(r'(.*)―$', startup)
    while matchObj:
        startup = matchObj.group(1)
        matchObj = re.match(r'(.*)―$', startup)

    # 去除结尾的‘PreA轮、B轮’等内容
    startup = re.sub(u'PreA轮.*', '', startup)
    startup = re.sub(u'Pre-A轮.*', '', startup)
    startup = re.sub(u'A轮.*', '', startup)
    startup = re.sub(u'B轮.*', '', startup)
    startup = re.sub(u'C轮.*', '', startup)
    startup = re.sub(u'D轮.*', '', startup)
    startup = re.sub(u'天使轮.*', '', startup)

    # 去除《》
    startup = startup.replace(u'《', '')
    startup = startup.replace(u'》', '')
    # 去除APP
    startup = startup.replace(u'APP', '')
    # 去除结尾的“项目”
    startup = startup.replace(u'项目', '')
    # 去除结尾的“网站”
    startup = startup.replace(u'网站', '')
    startup = startup.replace(r'\s*阅读版', '')

    startup = re.sub(r'\d{4,11}[-_.\d]*', '', startup)

    # 如果包含‘项目名称’的关键字，则取其为项目名称
    searchObj = re.search(u'项目名称：(.{2,})', text)

    if searchObj:
        startup = searchObj.group(1)
    if len(startup) > max_length:
        startup == ''

    return startup
