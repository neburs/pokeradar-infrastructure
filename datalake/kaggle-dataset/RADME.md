## Bulding Gradle project
docker run -ti --rm -v "$PWD":/project -w /project --name gradle gradle:alpine gradle <gradle-task>
