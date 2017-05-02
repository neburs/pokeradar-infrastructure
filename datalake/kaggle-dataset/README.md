# Full datalake with info from kaggle web

This script use the datalake downloaded from kaggle to save it in mongo.

In this project, you can find in data directory, a sample file to see the structure of it.

Please download the dataset file from this [url](https://www.kaggle.com/semioniy/predictemall/downloads/300k_csv.zip) and save it in data directory

## How to run

**Note**: Ensure you're running a instance of mongo in localhost:27017. If you not know how made this, check the Readme file in mongodb directory

```
java -jar kaggle-dataset-1.0.jar
```

## How to build

```
./gradlew clean build
```
Then, move the file generated in build/libs to the root project
