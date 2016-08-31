#!/usr/bin/env bash
########################################################################################################################
# 功能: 构建项目并运行
# 作者: 应卓
# 日期: 2016-08-31
########################################################################################################################

# 停止容器
docker-compose -f /home/yingzhuo/projects/kuruma/docker-compose.yml down 2> /dev/null

# 删除镜像
docker rmi yingzhuo/kuruma-user:latest 2> /dev/null
docker rmi yingzhuo/kuruma-config-server:latest 2> /dev/null

# 下载源文件
rm -rf /tmp/kuruma 2> /dev/null
git clone https://github.com/yingzhuo/kuruma.git "/tmp/kuruma"

# 构建Jar文件
mvn -f /tmp/kuruma/pom.xml clean package -U -Dmaven.test.skip=true

# 构建Docker镜像
mvn -f /tmp/kuruma/kuruma-config-server/pom.xml docker:build
mvn -f /tmp/kuruma/kuruma-user/pom.xml docker:build

# 复制docker-compose.yml 文件
rm /home/yingzhuo/projects/kuruma/docker-compose.yml
cp /tmp/kuruma/docker-cnf/docker-compose.yml /home/yingzhuo/projects/kuruma/docker-compose.yml

# 清理
mvn -f /tmp/kuruma/pom.xml clean -q

#重启项目
docker-compose -f /home/yingzhuo/projects/kuruma/docker-compose.yml up -d

exit 0