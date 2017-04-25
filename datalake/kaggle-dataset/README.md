## Using Gradle
docker run -ti --rm -v "$PWD":/project -w /project --name gradle gradle:alpine gradle <gradle-task>

## Init Gradle java project
docker run -ti --rm -v "$PWD":/project -w /project --name gradle gradle:alpine gradle init --type java-application

## Show tasks avaliables
docker run -ti --rm -v "$PWD":/project -w /project --name gradle gradle:alpine gradle :tasks
