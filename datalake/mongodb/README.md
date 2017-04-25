## For build
docker build -t=poke-mongo --force-rm=true .

## For run
docker run -d -p 27017:27017 poke-mongo
