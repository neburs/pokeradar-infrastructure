package com.kaggleprocessor.app;


public class App {

    public static void main(String [] args) {
        System.out.println("Starting to process Kaggle data lake");
        App app = new App();
        app.downloadDataSet();
        System.out.println("Process Kaggle data lake finished");
    }

    private void downloadDataSet() {
        KaggleRepository kaggleRepository = new KaggleRepository();
        kaggleRepository.processKaggleFile();
    }
}
