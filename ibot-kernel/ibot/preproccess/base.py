# -*- coding: utf-8 -*-
# 基本数据结构


class Word(str):
    """ 单词类,  包含单词字符串和pos标注字符串
    属性:
        raw: 单词的原始文本字符串
        pos: 单词的标注字符串
    """

    def __init__(self, (raw, pos)):
        super(Word, self).__init__(raw)
        self._raw = raw
        self._pos = pos

    @property
    def raw(self):
        return self._raw

    @property
    def pos(self):
        return self._pos


class Sentence(list):
    """ 句子类, 每一个句子是由Word对象组成的list对象
    属性:
        raw: 句子的原始文本
        words: 句子包含的词的集合, 每个词是一个Word对象
    """

    def __init__(self, raw, words):
        super(Sentence, self).__init__(words)
        self._raw = raw
        self._words = words

    @property
    def raw(self):
        return self._raw

    @property
    def words(self):
        return self._words


class Document(list):
    """ 篇章类, 每一个篇章包含一个由Sentence对象组成的list对象以及一个标签字符串组成的list对象
    属性:
        raw: 文档原始文本
        sentences: 文档包含的纯净句子集合, 每个句子是一个Sentence对象
        word_set: 不包含停用词的词集
        labels: 文档对应的标签集合

    """

    def __init__(self, raw, sentences, word_set, labels):
        super(Document, self).__init__(sentences)
        self._raw = raw
        self._sentences = sentences
        self._word_set = word_set
        self._labels = labels

    @property
    def raw(self):
        return self._raw

    @property
    def sentences(self):
        return self._sentences

    @property
    def word_set(self):
        return self._word_set

    @property
    def labels(self):
        return self._labels