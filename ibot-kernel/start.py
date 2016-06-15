#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Created on  : 2016-05-26 17:24:54
#
# ibot 服务端启动程序
# 使用Flask框架实现REST Webservice
#

import ibot
import os
from flask import Flask, json, abort, request


API = '/ibot/api/GetInfo'
PORT = 8839
DEBUG = True
CONF = 'ibot.conf' if os.path.isfile('ibot.conf') else None
ibot.open(CONF)

app = Flask(__name__)


@app.route('/ibot')
def index():
    return "Welcome to iBot!"


@app.route('/ibot/api/test', methods=['POST'])
def test():
    content = request.data
    c_type = str(type(content))
    return c_type + '\n' + content


@app.route(API, methods=['POST'])
def get_info():

    content = request.data
    if not content:
        abort(400)
    info = ibot.get_info(content, sep='\n\n\n', encoding='utf-8')

    return json.dumps(info, ensure_ascii=False, sort_keys=True, indent=4), 201

    # if request.json:
    #     content = request.json.get('content', None)
    #     encoding = request.json.get('encoding', None)
    #     sep = request.json.get('sep', '\n\n\n')
    # else:
    #     content = request.form.get('content', default=None)
    #     encoding = request.form.get('encoding', default=None)
    #     sep = request.form.get('sep', default='\n\n\n')
    # if not content:
    #     abort(400)

    # info = ibot.get_info(content, sep=sep, encoding=encoding)
    # return json.dumps(info, ensure_ascii=False, sort_keys=True, indent=4), 201


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=PORT, debug=DEBUG)
