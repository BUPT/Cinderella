# -*- coding: utf-8 -*-
#
# 抽取信息通用函数

from __future__ import unicode_literals
import xml.etree.ElementTree as ET
import os
import re

_get_module_path = lambda path: os.path.normpath(os.path.join(os.getcwd(),
                                                              os.path.dirname(__file__), path))
REG_FILE_PATH = _get_module_path('reg.xml')
REG_ROOT = ET.parse(REG_FILE_PATH)


def get_regs(xpath):
    """ 解析正则配置XML, 返回正则表达式列表"""
    global REG_ROOT
    regs = [node.text
            for node in REG_ROOT.find(xpath).getchildren()]
    return regs


def get_compile_regs(regs):
    """ 返回编译好的正则list """
    return [re.compile(reg) for reg in regs]
