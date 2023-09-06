package br.edu.seffrin.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesManager {
    private static final String PROPERTIES_FILE = "arpag.properties";

    // Method to store a key-value pair in the properties file
    public static void storeProperty(String key, String value) {
        Properties properties = new Properties();

        // Check if the file exists and load its contents
        File file = new File(PROPERTIES_FILE);
        if (file.exists()) {
            try (InputStream input = new FileInputStream(file)) {
                properties.load(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        properties.setProperty(key, value);

        try (OutputStream output = new FileOutputStream(file)) {
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve a value by key from the properties file
    public static String getProperty(String key) {
        Properties properties = new Properties();

        // Check if the file exists and load its contents
        File file = new File(PROPERTIES_FILE);
        if (file.exists()) {
            try (InputStream input = new FileInputStream(file)) {
                properties.load(input);
                return properties.getProperty(key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

//  class PropertiesManager {
//    private static final String PROPERTIES_FILE = "application.properties";
//
//    // Method to store a key-value pair in the properties file
//    public static void storeProperty(String key, String value) {
//        Properties properties = loadProperties();
//        properties.setProperty(key, value);
//        saveProperties(properties);
//    }
//
//    // Method to retrieve a value by key from the properties file
//    public static String getProperty(String key) {
//        Properties properties = loadProperties();
//        return properties.getProperty(key);
//    }
//
//    private static Properties loadProperties() {
//        Properties properties = new Properties();
//        try (InputStream input = PropertiesManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
//            if (input != null) {
//                properties.load(input);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return properties;
//    }
//
//    private static void saveProperties(Properties properties) {
//        try {
//            Path resourcePath = Paths.get(PropertiesManager.class.getClassLoader().getResource(PROPERTIES_FILE).toURI());
//            try (OutputStream output = Files.newOutputStream(resourcePath)) {
//                properties.store(output, null);
//            }
//        } catch (IOException | URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }
//}