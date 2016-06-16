# -*- coding: utf-8 -*-
# 人名识别


from __future__ import unicode_literals
import os
import codecs
import pynlpir


def _get_module_path(path):
    real_path = os.path.join(os.getcwd(), os.path.dirname(__file__), path)
    return os.path.normpath(real_path)


def _load_name_dict(dict_path):
    """ 构造名字字典 """
    with codecs.open(dict_path, 'rb', 'utf-8') as f:
        names = [n.strip() for n in f.readlines() if n.strip()]
    return set(names) if names else set()


def _build_fistname_dict(name_dict):
    """ 构建姓氏字典 """
    fist_name_dict = {}
    for name in name_dict:
        if len(name) == 1:  # {'王': [None]}
            fist_name_dict[name] = [None]
        else:
            if name[0] in fist_name_dict:   # {'欧':[0, '阳', '几']
                if not fist_name_dict[name[0]]:
                    fist_name_dict[name[0]] = ['0']
                fist_name_dict[name[0]].append(name[1])
            else:   # {'诸': ['葛']}
                fist_name_dict[name[0]] = [name[1]]

    return fist_name_dict


# 初始化字典
DICT_PATH = './dict/'
FIRST_NAME = _load_name_dict(_get_module_path(DICT_PATH + 'ChsFistName.txt'))
SINGLE_NAME = _load_name_dict(_get_module_path(DICT_PATH + 'ChsSingleName.txt'))
DOUBLE_NAME_1 = _load_name_dict(_get_module_path(DICT_PATH + 'ChsDoubleName1.txt'))
DOUBLE_NAME_2 = _load_name_dict(_get_module_path(DICT_PATH + 'ChsDoubleName2.txt'))
FIRST_NAME = _build_fistname_dict(FIRST_NAME)


def _match_nr(raw_str, start):
    """ 从字符串中起始位置为首匹配所有可能的中文人名
    @raw_str: 待匹配字符串
    @start: 起始点
    @rtype: list[str] 所有可能人名list
    """
    global FIRST_NAME
    global SINGLE_NAME
    global DOUBLE_NAME_1
    global DOUBLE_NAME_2

    max_length = len(raw_str)
    result = []

    cur = start
    if cur > max_length - 2: return result

    fistname_1 = raw_str[cur]
    cur += 1
    fistname_2 = raw_str[cur]

    # 姓氏匹配失败
    if fistname_1 not in FIRST_NAME: return result

    f2_list = FIRST_NAME[fistname_1]

    # 匹配复姓
    if f2_list[0]:
        if fistname_2 in f2_list:
            cur += 1  # 复姓匹配成功
        elif f2_list[0] != '0':
            return result   # 唯一复姓匹配失败

    if cur >= max_length: return result

    # 匹配名字
    name_1 = raw_str[cur]
    if name_1 in SINGLE_NAME:   # 单名
        result.append(''.join(raw_str[start: cur+1]))

    if name_1 in DOUBLE_NAME_1: # 双名
        cur += 1
        if cur >= max_length: return result

        name_2 = raw_str[cur]
        if name_2 in DOUBLE_NAME_2:
            result.append(''.join(raw_str[start: cur+1]))

    return result


def _sub_sequence(sequence, start, end):
    """ 捕获所有子序列 """
    length = end - start + 1
    for sub_len in xrange(1, length + 1):
        for cur in xrange(start, end - sub_len + 2):
            yield sequence[cur: cur + sub_len]


def _get_founders_by_ner(document):
    """ 使用实体识别, 侧重召回率 """
    founders = set()
    for sentence in document.sentences:

        # ----------------------------------------
        # 使用pynlpir分词实体识别结果
        # ----------------------------------------
        nlpir_seg = pynlpir.segment(sentence.raw, pos_names='child')
        s_len = len(nlpir_seg)

        i = 0
        while i < s_len:
            # 1. 定位nr
            if nlpir_seg[i][1] == 'personal name':
                end = i
                while end < s_len and nlpir_seg[end][1] == 'personal name':
                    end += 1

                # 2. 捕捉连续nr组合
                names = _sub_sequence(nlpir_seg, i, end - 1)

                # 3. 过滤不符合条件姓名
                for name in names:
                    name = ''.join([word[0] for word in name])
                    if len(name) < 2 or len(name) > 4: continue
                    if name[0] not in FIRST_NAME: continue
                    founders.add(name)

                i = end - 1
            i += 1

        # ----------------------------------------
        # 使用jieba分词标注结果进行优化
        #  - 多个连续的nr全组合后过滤
        #  - 姓氏过滤
        #  - 排除单字nr
        # ----------------------------------------
        # s_len = len(sentence.words)
        # i = 0
        # while i < s_len:
        #     # 1. 定位nr
        #     if sentence.words[i].pos == 'nr':
        #         end = i
        #         while end < s_len and sentence.words[end].pos == 'nr':
        #             end += 1

        #         # 2. 捕捉连续nr组合
        #         names = _sub_sequence(sentence.words, i, end - 1)

        #         # 3. 过滤不符合条件姓名
        #         for name in names:
        #             name = ''.join([word.raw for word in name])
        #             if len(name) < 2 or len(name) > 4: continue
        #             if name[0] not in FIRST_NAME: continue
        #             founders.add(name)

        #         i = end - 1
        #     i += 1
        # ----------------------------------------

    return founders


# 2016.05.29    待解决歧义问题
def _get_founders_by_dict(document):
    """ 使用词库匹配姓名, 侧重准确率 """
    founders = set()
    for sentence in document.sentences:
        for start in xrange(len(sentence.raw)):
            founders |= set(_match_nr(sentence.raw, start))
    return founders


def get_founders(document, mode=2):
    """ 获取团队成员
    @document: Document对象 Chunk
    @mode: 匹配模式: 1为精准模式, 2为普通模式
    @rtype: set(str) 成员名集合
    """
    if mode == 1:
        return _get_founders_by_ner(document)
    elif mode == 2:
        return _get_founders_by_dict(document)
    else:
        raise ValueError('error mode of "get_founders".')