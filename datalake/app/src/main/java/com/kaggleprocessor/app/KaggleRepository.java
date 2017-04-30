package com.kaggleprocessor.app;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;

public class KaggleRepository {

    private static String NAME_OF_DATASET_FILE_COMPRESSED = "300k_csv.zip";
    private static String NAME_OF_DATASET_FILE = "300k.csv";
    private static String NAME_OF_DATASET_DIR = "./kaggledata";

    public void processKaggleFile() {
        try {
//            deleteDataSetFile();
            deleteDataSetDir();
//            downloadDataSet();
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

    private void downloadDataSet() throws IOException {
        URL dataSet = new URL("https://www.kaggle.com/semioniy/predictemall/downloads/300k_csv.zip");
        ReadableByteChannel rbc = Channels.newChannel(dataSet.openStream());
        FileOutputStream fos = new FileOutputStream(NAME_OF_DATASET_FILE_COMPRESSED);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }

    private void deleteDataSetFile() {
        File f = new File(NAME_OF_DATASET_FILE_COMPRESSED);
        if (f.exists() && !f.isDirectory()) {
            f.delete();
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

        MongoClient mongoClient = new MongoClient("localhost",27017);

        // Now connect to your databases
        MongoDatabase db = mongoClient.getDatabase( "kaggle-datalake" );
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
