FROM ubuntu:16.04
MAINTAINER Ruben "ruben@vasallo.nom.es"

ENV DEBIAN_FRONTEND noninteractive
ENV TERM xterm-256color

# Configure TimeZone
RUN ln -sf /usr/share/zoneinfo/UTC /etc/localtime

# Added mongo remote port
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 0C49F3730359A14518585931BC711F9BA15703C6 && \
  echo "deb [ arch=amd64,arm64 ] http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.4 multiverse" | tee /etc/apt/sources.list.d/mongodb-org-3.4.list && \
  apt-get update

# Install mongodb
RUN apt-get -y install \
  mongodb-org=3.4.* \
  mongodb-org-server=3.4.* \
  mongodb-org-shell=3.4.* \
  mongodb-org-mongos=3.4.* \
  mongodb-org-tools=3.4.*

# Configure MongoDb
RUN sed -i "/bindIp: 127.0.0.1/s/^/#/g" /etc/mongod.conf && \
	mkdir -p /data/db

EXPOSE 27017

CMD [ "mongod" ]
