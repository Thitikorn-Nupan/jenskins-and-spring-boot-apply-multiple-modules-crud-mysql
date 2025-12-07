package com.ttknp.api.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

@Service
public class JdbcReadSQLFileHelper {
    // Ex,
    // Root start on resource , sqlScriptDir = "/sql/";
    // Absolute start wherever sqlScriptDir = "B:/practice-java-one-jetbrains/spring-boot-skills/lab_core_36/sumary-spring-boot-career/abc-parent/abc-properties-service/src/main/resources/sql/";
    private static final Logger log = LoggerFactory.getLogger(JdbcReadSQLFileHelper.class);
    private final JdbcTemplate jdbcTemplate;
    private String sqlScriptDir;

    public JdbcReadSQLFileHelper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setSqlScriptDir(String sqlScriptDir) {
        this.sqlScriptDir = sqlScriptDir;
    }

    /**
      Way to query with script sql ** if you want queries no response don't do the way
      query script (as file) *** query by ResourceDatabasePopulator So , you can have many sql statements in script as reset-products-mysql.sql
    */
    public void loadScriptAbsPath(String fileName) {
        String fullSqlScriptDirOnRoot = sqlScriptDir + fileName;
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(new FileSystemResource(fullSqlScriptDirOnRoot)); //** FileSystemResource class it looks to abs path
        populator.execute(Objects.requireNonNull(this.jdbcTemplate.getDataSource())); // by default it'll log queries result on console
    }

    /** Way to query with script sql ** if you want queries no response don't do the way */
    public void loadScriptRootPath(String fileName) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(new ClassPathResource(fileName));
        populator.execute(Objects.requireNonNull(this.jdbcTemplate.getDataSource()));
    }

    /** (Abs as B:/A/B/...) Query script (as statement) and pass params *** query by JdbcTemplate ** (MYSQL ONLY) you can have only one sql statement in script as demo-products-mysql-params.sql */
    public void loadScriptAbsPath(String fileName, HashMap<String,String> params) throws IOException { // Class aClass,
        String fullSqlScriptDirOnRoot = sqlScriptDir + fileName;
        StringBuilder stringBuilder = readSQLFileAsStatementOnJar(fullSqlScriptDirOnRoot);
        String sql = stringBuilder.toString();
        log.debug("Loaded sql script as statement = {}" , sql);
        int paramCount = params.size();
        String[] keys = new String[paramCount];
        String[] values = new String[paramCount];
        int i = 0;
        for (String key : params.keySet()) {
            keys[i] = key;
            i++;
        }
        i=0;
        for (String value : params.values()) {
            values[i] = value;
            i++;
        }
        for (i = 0; i < paramCount ; i++) {
            sql = sql.replace(keys[i], values[i]); // replace and update
        }
        // In mysql you can't execute many statements by execute(<queries>) , Note If do this way script must not contain the comment in sql script or contain it on the last lines
        jdbcTemplate.execute(sql);
        log.debug("Queried sql script as statement = {}" , sql);
    }

    /** Note, aClass should be a class on main method (Root start on resource/..) Query script (as statement) and pass params *** query by JdbcTemplate ** (MYSQL ONLY) you can have only one sql statement in script as demo-products-mysql-params.sql */
    public void loadScriptRootPath(String fileName, Class<?> aClass ,HashMap<String,String> params) throws IOException {
        StringBuilder stringBuilder = readSQLFileAsStatementOnJar(aClass,fileName);
        String sql = stringBuilder.toString();
        log.debug("Loaded sql script as statement  = {}" , sql);
        int paramCount = params.size();
        String[] keys = new String[paramCount];
        String[] values = new String[paramCount];
        int i = 0;
        for (String key : params.keySet()) {
            keys[i] = key;
            i++;
        }
        i=0;
        for (String value : params.values()) {
            values[i] = value;
            i++;
        }
        for (i = 0; i < paramCount ; i++) {
            sql = sql.replace(keys[i], values[i]);
        }
        jdbcTemplate.execute(sql);
        log.debug("Queried sql script as statement = {}" , sql);
    }


    public StringBuilder readSQLFileOnAbsPath(String filePath) throws IOException {
        return readSQLFileAsStatementOnJar(filePath);
    }

    public StringBuilder readSQLFileOnRootPath(Class<?> aClass,String sqlFileName) throws IOException {
        return readSQLFileAsStatementOnJar(aClass,sqlFileName);
    }

    // get on base app
    private StringBuilder readSQLFileAsStatement(Class<?> aClass,String filePath) throws IOException {
        InputStream inputStream = aClass.getClassLoader().getResourceAsStream(filePath);
        // convert to useful form
        assert inputStream != null;
        Scanner in = new Scanner(inputStream);
        // read contents
        StringBuilder sqlScriptBuilder = new StringBuilder();
        while (in.hasNext()) {
            sqlScriptBuilder.append(in.nextLine());
        }
        // close file
        in.close();
        return sqlScriptBuilder;
    }

    // ** It's not working on Jar file but work good when run
    private StringBuilder readSQLFileAsStatement(String filePath) throws IOException { // Class aClass,
        File file = new File(filePath); // ** InputStream works for root dir but this case i work with absolute dir so i use File instead
        if (file == null) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }
        // convert to useful form
        Scanner in = new Scanner(file);
        // read contents
        StringBuilder sqlScriptBuilder = new StringBuilder();
        while (in.hasNext()) {
            sqlScriptBuilder.append(in.nextLine());
        }
        // close file
        in.close();
        return sqlScriptBuilder;
    }

    // ** It's working on Jar file
    private StringBuilder readSQLFileAsStatementOnJar(String filePath) throws IOException {
        StringBuilder sqlScriptBuilder = new StringBuilder();
        try (InputStream is = Files.newInputStream(Paths.get(filePath));
             Scanner scanner = new Scanner(is)) {
            if (is == null) {
                throw new IllegalArgumentException("File not found: " + filePath);
            }
            while (scanner.hasNextLine()) {
                sqlScriptBuilder.append(scanner.nextLine());
            }
        } catch (Exception e) {
            throw new IOException("Error while reading file " + filePath, e);
        }
        return sqlScriptBuilder;
    }

    // ** It's working on Jar file
    private StringBuilder readSQLFileAsStatementOnJar(Class<?> aClass,String sqlFileName) throws IOException {
        StringBuilder sqlScriptBuilder = new StringBuilder();
        try (InputStream inputStream = aClass.getClassLoader().getResourceAsStream(sqlFileName)) {
            if (inputStream != null) {
                // You can now read from the inputStream // For example, to read the content as a String:
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    sqlScriptBuilder.append(new String(buffer, 0, bytesRead));
                }
            } else {
                log.debug("SQL file {} not found in the classpath root.",sqlFileName);
            }
        } catch (IOException e) {
            throw new IOException("Error reading SQL file: " + e.getMessage());
        }
        return sqlScriptBuilder;
    }


}

