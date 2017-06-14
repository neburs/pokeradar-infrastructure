## To run

# Creating Elastic Search master
docker container run -d \
    --name pokeradar-elastic-master \
    --network pokeradargo \
    --network-alias pokeradar-elastic-master-dev.sandbox \
    -p 9200:9200 \
    pokeradar-elastic \
    /usr/share/elasticsearch/bin/elasticsearch \
        -Enode.master=true \
        -Ecluster.name=pokeradar-logs \
        -Enode.name="pokeradar master"

# Creating Elastic Search node
docker container run -d \
    --name pokeradar-elastic-node \
    --network pokeradargo \
    --network-alias pokeradar-elastic-node-dev.sandbox \
    pokeradar-elastic
    /usr/share/elasticsearch/bin/elasticsearch \
        -Enode.master=false \
        -Ecluster.name=pokeradar-logs \
        -Ediscovery.zen.ping.unicast.hosts=pokeradar-elastic-master-dev.sandbox
