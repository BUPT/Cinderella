# -*- coding: utf-8 -*-
# 预处理模块
# 包括分词, 分块等预处理操作函数
#
# 对['company', 'city', 'money', 'equity', 'founders']先使用分类器初步筛选后
# 再使用关键词匹配

from __future__ import absolute_import, unicode_literals
from .base import Word, Sentence, Document
from collections import Counter
import xml.etree.ElementTree as ET
import jieba.posseg as pseg
import os
import codecs
import re
import cPickle


def _get_module_path(path):
    real_path = os.path.join(os.getcwd(), os.path.dirname(__file__), path)
    return os.path.normpath(real_path)


def _get_stopwords(path):
    stopwords = set()
    with codecs.open(path, 'rb', 'utf-8') as f:
        for line in f:
            word = line.strip()
            stopwords.add(word)
    return stopwords


STOPWORDS_PATH = _get_module_path('stopwords.dat')
LABELS_PATH = _get_module_path('labels.xml')
STOPWORDS = _get_stopwords(STOPWORDS_PATH)
DICT_PATH = _get_module_path('dict.txt.big')

CORRE_MODEL_PATH = _get_module_path('correlation.model')
CHUNK_MODEL_PATH = _get_module_path('chunk.model')

CORRE_MODEL, CORRE_DICT = cPickle.load(open(CORRE_MODEL_PATH, 'rb'))
CHUNK_MODEL, CHUNK_DICT = cPickle.load(open(CHUNK_MODEL_PATH, 'rb'))

KEYWORDS_ROOT = ET.parse(LABELS_PATH)
INFO = ['company', 'city', 'money', 'equity', 'founders']

def _get_keywords(xpath):
    """ 解析正则配置XML, 返回正则表达式列表"""
    global KEYWORDS_ROOT
    regs = [node.text
            for node in KEYWORDS_ROOT.find(xpath).getchildren()]
    return regs


def _get_chunk_labels_by_reg(chunk_str):
    """ 从关键词预测chunk标签

    @chunk_str: chunk字符串
    @rtype: set[str] 标签集
    """
    global INFO

    labels = set()
    for label in INFO:
        keywords = _get_keywords(label)
        p = re.compile('|'.join(keywords), re.I)
        if p.search(chunk_str):
            labels.add(label)

    return labels


def _is_category_by_reg(chunk_str, label):
    """ 从关键字判断是否属于类别 """
    keywords = _get_keywords(label)
    p = re.compile('|'.join(keywords), re.I)

    return True if p.search(chunk_str) else False


def _is_correlation(word_set):
    """ 通过分类器判断是否是信息相关chunk
    分类器输出1 为不相关, 0 为相关
    """
    global CORRE_MODEL, CORRE_DICT
    vec = _sentence2vec(word_set, CORRE_DICT)
    corre = _predict(CORRE_MODEL, vec)

    return True if corre == 0 else False


def _is_category(word_set, model, ddict):
    """ 通过分类器判断是否是某类别
    分类器输出1 是该类别, 0 不是该类别
    """
    vec = _sentence2vec(word_set, ddict)
    category = _ensemble_predict(model, vec)

    return True if category == 1 else False


def segment(raw_str):
    """ 结巴标注分词 """
    for word, pos in pseg.cut(raw_str):
        word = word.strip()
        if not word:
            continue
        yield word, pos


def get_chunks(raw_str, sep='\n\n\n'):
    """ 从原始字符获得chunks分块

    @raw_str: 原始字符串
    @sep: 分块标记
    @rtype: list[Document]
    """
    global STOPWORDS
    global CHUNK_MODEL, CHUNK_DICT
    global INFO

    for chunk_str in raw_str.split(sep):
        chunk_str = chunk_str.strip()
        if not chunk_str:
            continue

        word_set = set()
        sentences = []
        for sent in chunk_str.split('\n'):
            sent = sent.strip()
            if not sent:
                continue
            words = []
            for word, pos in segment(sent):
                words.append(Word((word, pos)))
                if word not in STOPWORDS:
                    word_set.add(word)

            # 构建Sentence
            sentences.append(Sentence(sent, words))

        # 预测标签集
        labels = set()
        if word_set:
            # 1. 对startup, 只使用关键字匹配
            if _is_category_by_reg(chunk_str, 'startup'):
                labels.add('startup')

            # 2. 使用分类器判断chunk的信息相关性
            correlation = _is_correlation(word_set)

            # 3. 对相关chunk, 在每个类别下进行处理:
            if correlation:
                for category in INFO:
                    # 1) 使用分类器进行第一遍筛选
                    model = CHUNK_MODEL[category]
                    ddict = CHUNK_DICT[category]
                    isCategory = _is_category(word_set, model, ddict)
                    if not isCategory: continue

                    # 2) 使用关键词进行第二遍筛选
                    isCategory = _is_category_by_reg(chunk_str, category)

                    # 3) 确定类别
                    if isCategory:
                        labels.add(category)

        # 构建Document
        yield Document(chunk_str, sentences, word_set, labels)


def _sentence2vec(word_set, dynamic_dicts):
    """ 使用动态词典将词袋转换成特征向量
    @word_set: 句子的词袋集合
    @dynamic_dicts: 动态词典
    """
    vec = []
    word_set = set(word_set)
    for label, dynamic_dict in dynamic_dicts.iteritems():
        weights = [dynamic_dict.get(word, 0.1) for word in word_set]
        weights = sorted(weights, reverse=True)

        # average
        vec.append(sum(weights) / len(weights))
        # top_1
        vec.append(weights[0])
        # top_3
        vec.append(sum(weights[:3]))
        # top_6
        vec.append(sum(weights[:6]))
        # top_10
        vec.append(sum(weights[:10]))
        # top_15
        vec.append(sum(weights[:15]))

    return vec


def _predict(models, vec):
    results = []
    for cls in models:
        result = cls.predict([vec])
        results.append(result[0])
    return Counter(results).most_common(1)[0][0]


def _ensemble_predict(ensemble_model, vec):
    results = []
    for sub_model in ensemble_model:
        result = _predict(sub_model, vec)
        results.append(result)
    return Counter(results).most_common(1)[0][0]