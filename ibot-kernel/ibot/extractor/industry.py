# -*- coding: utf-8 -*-

from __future__ import unicode_literals
from collections import Counter
import cPickle
import os


_get_module_path = lambda path: os.path.normpath(os.path.join(os.getcwd(),
                                                              os.path.dirname(__file__), path))

MODEL_PATH = _get_module_path('industry.model')
MODEL, DICT = cPickle.load(open(MODEL_PATH, 'rb'))

CATEGORIES = {0: u'其他', 1: u'电子商务', 2: u'社交网络'}

def get_industry(word_set):
    """ 行业分类 """
    global MODEL
    global DICT
    global CATEGORIES

    vec = _sentence2vec(word_set, DICT)
    industry = _predict(MODEL, vec)

    return CATEGORIES[industry]


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