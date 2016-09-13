# -*- coding: utf-8 -*-

from __future__ import unicode_literals
from common import get_regs, get_compile_regs
import re,cPickle
import os,jieba
from sklearn.externals import joblib

REGS = get_compile_regs(get_regs('money'))
DICT_PATH = './dict/'

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


STOPWORDS = load_stopwords('%sstopwords.txt' % DICT_PATH)
DYN_DICT = cPickle.load(open('%smoney_dynamic_dict_CHI.txt' % DICT_PATH))[0]
KNN = joblib.load('%smoney_knn_model.pkl' % DICT_PATH)

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
    借助knn分类器定位抽取
    :param document:
    :return:
    '''
    global STOPWORDS,DYN_DICT,REGS,KNN

    # 金额标签
    money_reg = ['万','w','W','亿','百万','美元','美金','元','RMB']
    moneyTag = ('|').join(['\d?\.?\d+%s'%x for x in money_reg])
    # 移动窗口
    window = 5
    for sentence in document.sentences:
        # 优先定位 金额的位置
        hasMoney = re.findall(moneyTag, sentence.raw)
        if hasMoney:
            # 匹配的起点和终点
            money_end = 0
            for money in hasMoney:
                new_sentence = sentence.raw[money_end:]
                money_start, money_end = re.search(re.compile(money),new_sentence).span()
                #边界判断
                window_start = money_start - window if money_start > window else 0
                window_end = money_end + window if money_end < len(new_sentence) - window else len(new_sentence)
                #取出窗口内词,转换向量
                sentence_in_window = new_sentence[window_start : money_start] + new_sentence[money_end : window_end]
                vec = sentence2vec(sentence_in_window,DYN_DICT)
                #判断是否为融资额
                is_money = KNN.predict(vec)
                if is_money:
                    return {"status": 1, "money": money}

    return None


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
                temp = removeStopWords(wordList[start:end], STOPWORDS)
                result.append(temp)
        except:
            continue

    return result


def findIndex(inList, value):

    index = [i for i, a in enumerate(inList) if a in value]
    return index


def removeStopWords(inList):
    '''
    去除停用词
    :param inList:
    :return:
    '''
    global STOPWORDS
    wordList = []
    for word in list(inList):
        word = word.strip()
        if word != '' and word not in STOPWORDS:
            wordList.append(word)
    return wordList

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


