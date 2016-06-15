# -*- coding: utf-8 -*-

from __future__ import unicode_literals
from common import get_regs, get_compile_regs
import re
import os

REGS = get_compile_regs(get_regs('equity'))
DICT_PATH = './dict/'


def _get_module_path(path):
    real_path = os.path.join(os.getcwd(), os.path.dirname(__file__), path)
    return os.path.normpath(real_path)


def get_equity(document, mode=2):

    if int(mode) == 1:
        return equity_2(document, 1.5)

    if int(mode) == 2:
        return equity_2(document)


def equity_1(document):
    '''
    抽取股权占比
    :param document: chunk 包含 sentence,words,labels
    :return: 字典 {"status":1,"equity":[10%]} 或者 {"status":0,"equity":[20%]} => 1 表示 关键字抽取,0 表示 全文抽取
    '''
    global REGS
    equity = []
    # 股权标签
    equityTag = re.compile(u"(\d+)[%％]+")
    # 移动窗口
    window = 3
    for sentence in document.sentences:
        # 优先定位 百分比的位置
        hasEquity = re.search(equityTag, sentence.raw)
        if hasEquity:
            # 匹配的起点和终点
            start, end = hasEquity.span()
            if 9 < int(hasEquity.group(1)) < 40:

                if start < window:
                    start = window
                if end > len(sentence.raw) - window:
                    end = len(sentence.raw) - window
                # 按优先级 检测 窗口词是否包含股权关键词
                for reg in REGS:
                    if re.search(reg, sentence.raw[start - window: end + window]):
                        return {"status": 1, "equity": hasEquity.group()}

                # 如果检测不到关键词
                equity.append(hasEquity.group())

    return {"status": 0, "equity": equity}


def equity_2(document, baseLine=0.0):
    '''
    抽取股权比
    :param document:
    :return:
    '''
    bagofWords = load_bagWords('equity')
    windowWord = cutWindowWord(document)

    reg = re.compile(u'(\d+%)')
    result = []
    for words in windowWord:

        regCont = re.search(reg, ''.join(words))
        if not regCont:
            continue
        tagTemp = regCont.group()

        if len(tagTemp) == 5:
            tagCont = '%d~%d%s' % (int(tagTemp[:2]), int(tagTemp[2:4]), '%')
        elif len(tagTemp) == 4:
            tagCont = '%d~%d%s' % (int(tagTemp[:1]), int(tagTemp[1:3]), '%')
        else:
            tagCont = tagTemp
        weight = 0.0
        for word in words:
            if word in bagofWords:
                weight += bagofWords[word]
        if weight < baseLine:
            continue
        temp = (tagCont, weight)
        result.append(temp)

    return result


def load_bagWords(tag):
    '''
    加载词袋
    :param tag:
    :return:
    '''
    bagofWords = {}
    with open(_get_module_path('%s%s_bagWords.txt' % (DICT_PATH, tag)), 'rb') as f:
        for line in f.readlines():
            cont = line.decode('utf-8').strip().split()
            try:
                bagofWords[cont[0]] = float(cont[1])
            except:
                continue
    return bagofWords


def cutWindowWord(document):
    '''
    取窗口词
    :param text:
    :return:
    '''
    result = []
    stopWords = load_stopwords(_get_module_path('%sstopwords.txt' % DICT_PATH))
    wordList = []
    for sentence in document.sentences:
        wordList.extend([w.raw for w in sentence.words])
    wordList, index = findIndex(wordList, [u'%'])
    windows = 10
    for i in xrange(len(index)):
        start = index[i] - windows
        end = index[i] + windows

        if start < 0:
            start = 0
        if '%' in wordList[start:index[i] - 1]:
            start = index[i - 1] + 1

        if end > len(wordList):
            end = len(wordList)
        if '%' in wordList[index[i] + 1:end]:
            end = index[i + 1] - 1
        # 去除10%
        try:
            if float(wordList[index[i] - 1]) % 1 == 0 and 9.0 < float(wordList[index[i] - 1]) < 40.0:

                temp = removeStopWords(wordList[start:end], stopWords)
                result.append(temp)
        except:
            continue

    return result


def findIndex(inList, value):

    index = [i for i, a in enumerate(inList) if a in value]
    # 解决10%-20%的问题
    Del = []
    for i in xrange(len(index)):
        if i > 0:
            if index[i] - index[i - 1] < 4:
                Del.append(i - 1)
                inList[index[i - 1]] = '-'
    while Del:
        index.pop(Del[-1])
        Del.remove(Del[-1])
    return inList, index


def removeStopWords(inList, stopWords):
    wordList = []
    for word in list(inList):
        word = word.strip()
        if word != '' and word not in stopWords:
            wordList.append(word)
    return wordList


def load_stopwords(path):
    '''
    载入停用词
    :param path:
    :return:
    '''
    stopwords = set()
    with open(path, 'rb') as f:
        for line in f:
            word = line.strip().decode('utf-8')
            stopwords.add(word)
    return stopwords
