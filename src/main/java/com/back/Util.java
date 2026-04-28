package com.back;

import java.io.IOException;
import java.nio.file.*;

public class Util {

    public static void saveToFile(String path, String content) {
        try {
            Path parentDir = Paths.get(path).getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            Files.writeString(Paths.get(path), content, 
                StandardOpenOption.CREATE, 
                StandardOpenOption.TRUNCATE_EXISTING);
                
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFromFile(String path) {
        try {
            Path filePath = Paths.get(path);

            if (Files.notExists(filePath)) {
                return null;
            }

            return Files.readString(filePath);
            
        } catch (IOException e) {
            return null;
        }
    }

    public static void deleteFile(String path) {
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
