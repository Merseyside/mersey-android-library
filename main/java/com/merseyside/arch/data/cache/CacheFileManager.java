package com.merseyside.archy.data.cache;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;

@Singleton
class CacheFileManager {

    @Inject
    CacheFileManager() {}

    void writeToFile(File file, String fileContent) {
        if (!file.exists()) {
            try {
                final FileWriter writer = new FileWriter(file);
                writer.write(fileContent);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    String readFileContent(File file) {
        final StringBuilder fileContentBuilder = new StringBuilder();
        if (file.exists()) {
            String stringLine;
            try {
                final FileReader fileReader = new FileReader(file);
                final BufferedReader bufferedReader = new BufferedReader(fileReader);
                while ((stringLine = bufferedReader.readLine()) != null) {
                    fileContentBuilder.append(stringLine).append("\n");
                }
                bufferedReader.close();
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileContentBuilder.toString();
    }

    boolean exists(File file) {
        return file.exists();
    }

    boolean clearDirectory(File directory) {
        boolean result = false;
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                result = file.delete();
            }
        }
        return result;
    }

    void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }
}
