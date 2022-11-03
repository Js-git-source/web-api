#!/bin/bash
if [ ! "$1" ];then
#  echo 'ntpserver is null'
	exit 1
fi
#执行ntpdate
info=$(/usr/sbin/ntpdate $1)
result=$(echo $?)
echo $result
#获取上一个命令的执行结果
if [ $result -eq 1 ];then
  exit 1
fi
echo $(date '+%Y-%m-%d %H:%M:%S')
exit 0;