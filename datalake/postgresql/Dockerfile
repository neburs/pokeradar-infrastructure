FROM centos:7
LABEL maintainer_1="Rubén Vasallo <ruben@vasallo.nom.es>" \
      maintainer_2="David Sola <d.sola.03@gmail.com>"

ENV TERM xterm-256color

#Configure TimeZone
RUN ln -sf /usr/share/zoneinfo/Europe/Madrid /etc/localtime

# Add repository Postgress
RUN yum install -y https://download.postgresql.org/pub/repos/yum/9.6/redhat/rhel-7-x86_64/pgdg-centos96-9.6-3.noarch.rpm

# Install PostgresqlServer
RUN yum install -y \
	glibc-common \
	postgresql96-server-9.6.* \
	postgresql96-contrib-9.6.*

# Configure Locale
RUN localedef -v -c -i en_US -f UTF-8 en_US.utf8 || true

# Configure postgresql Server
RUN mkdir -p /datapostgres && \
	chown postgres:postgres /datapostgres && \
	su -c "/usr/pgsql-9.6/bin/initdb -D /datapostgres -E 'UTF-8' --lc-collate='en_US.UTF-8' --lc-ctype='en_US.UTF-8'" - postgres && \
	su -c "/usr/pgsql-9.6/bin/postgres -D /datapostgres &" - postgres && \
	sleep 5 && \
	su -c "/usr/pgsql-9.6/bin/psql --command \"CREATE USER root WITH PASSWORD 'development';\"" - postgres && \
	su -c "/usr/pgsql-9.6/bin/psql --command \"ALTER ROLE root WITH CREATEDB;\"" - postgres && \
	su -c "/usr/pgsql-9.6/bin/psql --command \"CREATE DATABASE pokemon WITH OWNER = root ENCODING = 'UTF8' TABLESPACE = pg_default LC_COLLATE = 'en_US.UTF-8' LC_CTYPE = 'en_US.UTF-8' CONNECTION LIMIT = -1 TEMPLATE template0;\"" - postgres && \
	sed -i "s%#listen_addresses = 'localhost'%listen_addresses = '*'%" /datapostgres/postgresql.conf && \
	echo "host all all 172.17.42.0/24 md5" >> /datapostgres/pg_hba.conf && \
	echo "host all all 172.17.0.0/24 md5" >> /datapostgres/pg_hba.conf


EXPOSE 5432

USER postgres

CMD ["/usr/pgsql-9.4/bin/postgres", "-D", "/datapostgres"]
