# -*- coding: utf-8 -*-

from __future__ import unicode_literals
from common import get_regs, get_compile_regs
import re
import os,cPickle,pickle,jieba

REGS = get_compile_regs(get_regs('equity'))
DICT_PATH = './dict/'
# 股权标签
equityTag = re.compile(u"(\d+)[%％]+")
DYN_DICT = cPickle.load(open('%smoney_dynamic_dict_CHI' % DICT_PATH))
KNN = pickle.load(open('equity_knn.model','rb'))

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
    利用KNN抽取股权占比
    :param document: chunk 包含 sentence,words,labels
    :return: 字典 {"status":1,"equity":10%} 或者 {"status":0,"equity":20%}
            定位 百分比 的句子,取"百分比"左右的window大小的句段,转化为特征向量
            利用knn识别是否为股权而非其他等(如利润等)
            否则 返回None
    '''
    global equityTag,DYN_DICT,KNN
    result = []
    # 移动窗口
    window = 5
    for sentence in document.sentences:
        # 只处理带百分比的句子
        hasEquity = re.findall(equityTag, sentence.raw)
        if hasEquity:
            # 匹配的终点
            equity_end = 0
            for reg_equity in hasEquity:
                #非常态值,则跳过
                if not is_legal(reg_equity):
                    continue
                new_sentence = sentence.raw[equity_end:]
                equity_start,equity_end = re.search(re.compile(reg_equity),new_sentence).span()
                #边界检测
                window_start = equity_start - window if equity_start > window else 0
                window_end = equity_end + window if equity_end < len(new_sentence) - window else len(new_sentence)
                #转化为向量
                sentence_in_window = new_sentence[window_start : equity_start] + new_sentence[equity_end : window_end]
                vec = sentence2vec(sentence_in_window,DYN_DICT)
                is_equity = KNN.predict([vec])
                weight = 0.95 if is_equity else 0.05

                result.append((reg_equity,weight))

    return result

def is_legal(equity):
    '''
    认为出让股份在[3,40]之间
    :param equity:
    :return:
    '''
    try:
        if int(equity) > 3 and int(equity) <= 40:
            return True
        else:
            return False
    except:
        return False
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
def sentence2vec(sentence, dynamic_dicts):
    """ 使用动态词典将句子转换成特征向量

    每个类别分别生成4个特征:
        - average: 平均强度
        - top_1: 类别强度最高
        - top_2: 类别强度前2之和
        - top_4: 类别强度前4之和
    """
    global STOPWORDS
    if not isinstance(sentence,list):
        sentence = removeStopWords(jieba.cut(sentence))

    vec = []
    weights = [dynamic_dicts.get(word, 0.1) for word in sentence]
    if not weights:
        return [0]*4

    weights = sorted(weights, reverse=True)

    # average
    vec.append(sum(weights) / weights)
    # top_1
    vec.append(weights[0])
    # top_2
    vec.append(sum(weights[:2]))
    # top_4
    vec.append(sum(weights[:4]))

    return vec