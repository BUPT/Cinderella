#!/bin/sh

pid=`ps aux| grep start.py | grep -v grep | sed -n  '1P' | awk '{print $2}'`
if [ -z $pid ]; then
        echo "begin restart start.py,please waiting..."
        sudo python2 /home/test/test/ibot_server/start.py
        exit 1
fi