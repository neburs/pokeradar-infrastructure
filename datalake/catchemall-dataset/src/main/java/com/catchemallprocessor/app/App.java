package com.catchemallprocessor.app;

public class App {
    public static void main(String [] args) {
        if (args.length < 2) {
            System.out.printf("Please indicate the ip and the port of the mongo server\n");
            System.exit(0);
        }
        String mongoServerIp = args[0];
        int mongoServerPort = Integer.parseInt(args[1]);

        System.out.println("Starting to process CatcheMall data lake");
        App app = new App();
        app.downloadDataSet(mongoServerIp, mongoServerPort);
        System.out.println("Process CatcheMall data lake finished");
    }

    private void downloadDataSet(String mongoServerIp, int mongoServerPort) {
        KaggleRepository kaggleRepository = new KaggleRepository(mongoServerIp, mongoServerPort);
        kaggleRepository.processKaggleFile();
    }
}
