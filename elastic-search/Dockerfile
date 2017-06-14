FROM ubuntu:16.04
LABEL maintainer_1="Rubén Vasallo <ruben@vasallo.nom.es>" \
      maintainer_2="David Sola <d.sola.03@gmail.com>"

ENV DEBIAN_FRONTEND noninteractive
ENV TERM xterm-256color
ENV LANG C.UTF-8
ENV ES_JAVA_OPTS '-Xms1g -Xmx1g'
ENV ES_SKIP_SET_KERNEL_PARAMETERS true

#Configure TimeZone
RUN 	ln -sf /usr/share/zoneinfo/UTC /etc/localtime

# Install dependencies
RUN 	apt-get update && \
	apt-get install -y \
	openjdk-8-jre \
	apt-transport-https \
	wget \
	vim

# Install Elastic Search
RUN	cd && \
	wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.1.2.deb && \
	dpkg -i elasticsearch-5.1.2.deb

# Configure ElasticSearch
RUN 	mkdir -p \
		/usr/share/elasticsearch/data \
		/usr/share/elasticsearch/logs \
		/usr/share/elasticsearch/config \
		/usr/share/elasticsearch/config/scripts && \
	chown -R elasticsearch:elasticsearch \
		/usr/share/elasticsearch/data \
                /usr/share/elasticsearch/logs \
                /usr/share/elasticsearch/config \
		/usr/share/elasticsearch/config/scripts

# Configure ElasticSearch
RUN 	echo "vm.max_map_count=262144" >> /etc/sysctl.conf
ADD 	log4j2.properties /usr/share/elasticsearch/config/log4j2.properties
ADD 	elasticsearch.yml /usr/share/elasticsearch/config/elasticsearch.yml
RUN 	chown -R elasticsearch:elasticsearch /usr/share/elasticsearch/config/

EXPOSE 9200-9300

USER elasticsearch
CMD ["/usr/share/elasticsearch/bin/elasticsearch"]

## NOTE:
# run sudo sysctl -w vm.max_map_count=262144 in server that run this container
# for permanent setting add this in /etc/sysctl.conf
# $ su -c 'echo "vm.max_map_count=262144" >> /etc/sysctl.conf'
# for arch env run:
# $ su -c 'echo "vm.max_map_count=262144" >> /etc/sysctl.d/elastic.conf'
# see: https://www.archlinux.org/news/deprecation-of-etcsysctlconf/
