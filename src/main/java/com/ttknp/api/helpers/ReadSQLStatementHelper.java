package com.ttknp.api.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
public class ReadSQLStatementHelper {

    private final ResourceLoader resourceLoader;

    @Autowired
    public ReadSQLStatementHelper(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
      get sql as statement
    */
    public StringBuilder readFileAsStatement(String fileName) {
        // Getting the resource using the ResourceLoader *** (If your app as microservice this way it's not working)
        Resource resource = this.resourceLoader.getResource("classpath:sql/" + fileName);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            // Opening an InputStream to read the content of the resource
            InputStream inputStream = resource.getInputStream();
            // Creating a BufferedReader to read text from the InputStream efficiently
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            // StringBuilder to accumulate the lines read from the file
            String line;
            // Reading each line from the file and appending it to the StringBuilder
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            // Closing the BufferedReader
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return stringBuilder; // Returning the contents of the file as a string build
    }

}