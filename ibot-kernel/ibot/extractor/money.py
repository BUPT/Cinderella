# -*- coding: utf-8 -*-

from __future__ import unicode_literals
from common import get_regs, get_compile_regs
import re
import os

REGS = get_compile_regs(get_regs('money'))
DICT_PATH = './dict/'


def _get_module_path(path):
    real_path = os.path.join(os.getcwd(), os.path.dirname(__file__), path)
    return os.path.normpath(real_path)


def get_money(document, mode=2):
    if int(mode) == 1:
        return money_2(document, 2.5)
    if int(mode) == 2:
        return money_2(document)


def money_1(document):
    '''
    抽取融资额
    :param document: chunk 包含 sentence,words,labels
    :return: 字典 {"status":1,"money":[100万]} 或者 {"status":0.5,"money":[200万]}
            或者 {"status":0,"money":[100万,200万]}=> 1 表示 高置信度抽取,0.5 表示 低置信度抽取 0 表示 全文抽取
    '''
    global REGS

    money = []
    # 金额标签
    moneyTag = re.compile(u"(\d+\.?\d*[亿万美金RMB]+)")
    # 移动窗口
    window = 3
    for sentence in document.sentences:
        # 优先定位 金额的位置
        hasMoney = re.search(moneyTag, sentence.raw)
        if hasMoney:
            # 匹配的起点和终点
            start, end = hasMoney.span()
            if start < window:
                start = window
            if end > len(sentence.raw) - window:
                end = len(sentence.raw) - window
            # 按优先级 检测 窗口词是否包含融资关键词
            count = 0
            for reg in REGS:
                if re.search(reg, sentence.raw[start - window: end + window]):
                    if count < len(REGS) / 2:
                        return {"status": 1, "money": hasMoney.group()}
                    else:
                        return {"status": 0.5, "money": [hasMoney.group()]}
                count += 1
            # 如果检测不到关键词
            money.append(hasMoney.group())

    return {"status": 0.5, "money": money}


def money_2(document, baseLine=0.0):
    '''
    抽取融资额
    :param document:
    :return:
    '''
    bagofWords = load_bagWords('money')
    windowWord = cutWindowWord(document)

    reg = re.compile(u'(\d+\.?\d*)[万亿美金]+')
    result = []
    for words in windowWord:
        regCont = re.search(reg, ''.join(words))
        if not regCont:
            continue
        tagCont = regCont.group()

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
    for sentence in document:
        wordList.extend([w.raw for w in sentence.words])

    # words = jieba.cut(document)
    # wordList=[]
    # for word in words:
    #     wordList.append(word)
    moneyTags = [u'万', u'万元', u'亿', u'亿元', u'美金', u'美元']
    index = findIndex(wordList, moneyTags)
    windows = 6
    for i in xrange(len(index)):
        start = index[i] - windows
        end = index[i] + windows

        if start < 0:
            start = 0
        if end > len(wordList):
            end = len(wordList)

        for tag in moneyTags:
            if tag in wordList[start:index[i] - 1]:
                start = index[i - 1] + 1

            if tag in wordList[index[i] + 1:end]:
                end = index[i + 1] - 1

        # 去除100万
        try:
            if float(wordList[index[i] - 1]):
                temp = removeStopWords(wordList[start:end], stopWords)
                result.append(temp)
        except:
            continue

    return result


def findIndex(inList, value):

    index = [i for i, a in enumerate(inList) if a in value]
    return index


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


# txt = '北京网红科技公司融资300万出让股份10到20%大约400亿用于新一轮利率15%占股的500美金公司扩展业务'
#
# print get_money(txt,2)
