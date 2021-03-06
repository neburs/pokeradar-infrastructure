package com.kaggleprocessor.app;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONObject;

import java.io.*;
import java.util.Iterator;

public class KaggleRepository {

    private static String NAME_OF_DATASET_FILE_COMPRESSED = "data/300k.csv.zip";
    private static String NAME_OF_DATASET_FILE = "300k.csv";
    private static String NAME_OF_DATASET_DIR = "./data/kaggledata";

    private String mongoServerIp;
    private int mongoServerPort;

    public KaggleRepository(String mongoServerIp, int mongoServerPort) {
        this.mongoServerIp = mongoServerIp;
        this.mongoServerPort = mongoServerPort;
    }

    public void processKaggleFile() {
        try {
            deleteDataSetDir();
            prepareData();
            processCSVFile(NAME_OF_DATASET_DIR + "/" + NAME_OF_DATASET_FILE);
        } catch (IOException e) {
            System.out.println("Can not download dataset from kaggle error => " + e.getMessage());
        }
    }

    private void prepareData() throws IOException {
        UnzipUtility manager = new UnzipUtility();
        manager.unzip(NAME_OF_DATASET_FILE_COMPRESSED, NAME_OF_DATASET_DIR);
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

    private void processCSVFile(String csvFile) {

        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";
        JSONObject item;

        try {

            MongoDatabase dbConnection = connectToDataLake();

            br = new BufferedReader(new FileReader(csvFile));
            Integer counter = 0;
            String[] header = new String[1];
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(cvsSplitBy);

                if (counter == 0) {
                    header = data;
                    counter++;
                    continue;
                }

                item = new JSONObject();
                for (int iterator = 0; iterator < header.length; iterator++) {
                    item.put(header[iterator], data[iterator]);
                }

                System.out.println("Processing document " + counter);
                System.out.println(item.toString() );
                persistItemToDataLake(dbConnection, item);

                counter++;
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

        MongoClient mongoClient = new MongoClient(this.mongoServerIp,this.mongoServerPort);

        // Now connect to your databases
        MongoDatabase db = mongoClient.getDatabase( "datalake" );
        System.out.println("Connect to database successfully");

        return db;
    }

    private void persistItemToDataLake(MongoDatabase database, JSONObject item) {
        MongoCollection<Document> collection = database.getCollection("kaggle-datalake");

        Iterator<?> keys = item.keys();
        Document document = new Document();

        while (keys.hasNext()) {
            String key = (String)keys.next();
            String value = (String)item.get(key);
            document.put(key,value);
        }
        collection.insertOne(document);
    }
}
