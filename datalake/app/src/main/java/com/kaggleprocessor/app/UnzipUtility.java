package com.kaggleprocessor.app;

import java.io.IOException;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.core.ZipFile;


public class UnzipUtility {
    /**
     * Unzip it
     * @param zipFile input zip file
     * @param output zip file output folder
     */
    public void unzip(String zipFilePath, String destDirectory) throws IOException {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            zipFile.extractAll(destDirectory);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }
}
