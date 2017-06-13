FROM python:3.6.1
LABEL maintainer_1="Rubén Vasallo <ruben@vasallo.nom.es>" \
      maintainer_2="David Sola <d.sola.03@gmail.com>"

ENV TERM xterm-256color
ENV LANG en_US.UTF-8

#Configure TimeZone
RUN ln -sf /usr/share/zoneinfo/Europe/Madrid /etc/localtime

# Clone project
RUN git clone https://github.com/pokeradargo/pokeradar-preprocessing.git /data

# Install dependencies
RUN	cd /data/kaggle-model-input && \
	python setup.py install

WORKDIR /data/kaggle-model-input

CMD ["python", "-m", "App.run"]
