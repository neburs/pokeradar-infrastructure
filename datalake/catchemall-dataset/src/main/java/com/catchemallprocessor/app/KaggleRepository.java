package com.catchemallprocessor.app;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.json.JSONObject;

public class KaggleRepository {
    private int NUM_PART_DATASET = 4;
    private String NAME_OF_DATASET_FILE_COMPRESSED = "data/catchemall_part";
    private String FORMAT_OF_DATASET_FILE_COMPRESSED = ".zip";
    private static String NAME_OF_DATASET_DIR = "./data/kaggledata";
    private List<String> dataFilesCompressed;

    public KaggleRepository() {
        this.dataFilesCompressed = new ArrayList<>();
        for(int i = 1; i <= NUM_PART_DATASET; i++) {
            this.dataFilesCompressed.add(NAME_OF_DATASET_FILE_COMPRESSED + i + FORMAT_OF_DATASET_FILE_COMPRESSED);
        }
    }

    public void processKaggleFile() {
        try {
            deleteDataSetDir();
            prepareData();
            processCSVFile(NAME_OF_DATASET_DIR);
        } catch (IOException e) {
            System.out.println("Can not download dataset from kaggle error => " + e.getMessage());
        }
    }

    private void prepareData() throws IOException {
        UnzipUtility manager = new UnzipUtility();
        for (String file : this.dataFilesCompressed) {
            manager.unzip(file, NAME_OF_DATASET_DIR);
        }
    }

    private void deleteDataSetDir() {
        try {
            File index = new File(NAME_OF_DATASET_DIR);
            if (index.exists()) {
                String[] entries = index.list();
                for (String s : entries) {
                    File currentFile = new File(index.getPath(), s);
                    currentFile.delete();
                }
                index.delete();
            }
        } catch (NullPointerException e) {
            System.out.println("directory " + NAME_OF_DATASET_FILE_COMPRESSED + " not found");
        }
    }

    private String[] getFilesOfDir(String dirName) {
        String[] response = new String[0];
        try {
            File index = new File(dirName);
            if (index.exists()) {
                response = index.list();
            }
        } catch (NullPointerException e) {
            System.out.println("directory " + dirName + " not found");
        }

        return response;
    }

    private void processCSVFile(String dataSetDirectory) {

        BufferedReader br = null;
        String line;
        String[] filesOfDataSet = getFilesOfDir(dataSetDirectory);
        try {
            for (String file : filesOfDataSet) {

                MongoDatabase dbConnection = connectToDataLake();

                br = new BufferedReader(new FileReader(dataSetDirectory + "/" + file));
                Integer counter = 0;
                while ((line = br.readLine()) != null) {

                    JSONObject jsonItem = new JSONObject(line);

                    System.out.println("Processing document " + counter);
                    System.out.println(jsonItem.toString());
                    persistItemToDataLake(dbConnection, jsonItem);
                    counter++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private MongoDatabase connectToDataLake() {

        MongoClient mongoClient = new MongoClient("localhost",27017);

        // Now connect to your databases
        MongoDatabase db = mongoClient.getDatabase( "datalake" );
        System.out.println("Connect to database successfully");

        return db;
    }

    private void persistItemToDataLake(MongoDatabase database, JSONObject item) {
        MongoCollection<Document> collection = database.getCollection("catchemall-datalake");

        Iterator<?> keys = item.keys();
        Document document = new Document();

        while (keys.hasNext()) {
            String key = (String)keys.next();
            String value = item.get(key).toString();
            document.put(key,value);
        }
        collection.insertOne(document);
    }
}
