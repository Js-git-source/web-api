#!/bin/bash
if [[ ! "$1" || ! "$2" || ! "$3" ]];then
  echo 'ntpserver and cron is null'
	exit 1
fi
#查看当前用户名下的定时任务中，存在ntpdate任务数量
exists=`crontab -l | grep -c ntpdate`
#如果当前没有ntpdate任务则直接添加，如果已存在则需要执行查找、替换的操作
if [ $((exists)) -gt 0 ];then
	echo "Already exists ntptask quantity is $exists"
	#将当前用户下的所有定时任务重定向到临时文件
	echo "`crontab -l`" > crontab_list_tmp.txt
	#删除临时文件中所有的ntpdate任务
	sed -i '/ntpdate/'d crontab_list_tmp.txt
	#如果周期校时开启才会添加定时，否则只删除之前的周期设置
	if [[ "$3" == '1' || "$3" -eq 1 ]];then
	  #将最新的ntpdate定时任务追加到临时文件
	  echo "$1 /usr/sbin/ntpdate $2" >> crontab_list_tmp.txt
    echo 'add cron '
  fi
	#将临时文件的任务重新覆蓋写入定时
	cat crontab_list_tmp.txt > /var/spool/cron/root
	#删除临时文件
	rm -rf crontab_list_tmp.txt
else
  if [[ "$3" == '1' || "$3" -eq 1 ]];then
	  echo "$1 /usr/sbin/ntpdate $2" >> /var/spool/cron/root
	fi
fi
