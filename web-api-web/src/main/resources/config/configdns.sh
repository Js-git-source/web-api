#!/bin/bash
if [ ! "$1" ];then
	exit 1
fi
cat /etc/resolv.conf > /etc/resolv.conf.bak
echo "nameserver $1" > /etc/resolv.conf
if [ "$2" ];then
  echo "nameserver $2" >> /etc/resolv.conf
fi

# $1参数为首选DNS服务器，$2为备选DNS服务器