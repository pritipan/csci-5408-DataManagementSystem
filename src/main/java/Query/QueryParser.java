package Query;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Query.MetadataHandle.*;

public class QueryParser {

    public static void main(String[] arg) {
        //CreateParser("create table student3(id int NOT NULL, name varchar(45), name123 varchar(45), PRIMARY KEY(id));", "demo");
        InsertParser("insert into student(id, name) values (1, \"foram\");", "demo");
    }

    public static void CreateParser(String query, String dbName) {
        String createRegex = "CREATE TABLE\\s(\\w+)[(]((((\\w+)\\s(varchar[(]\\d+[)]|int)*\\s*(?:NOT\\sNULL)?)(,)*\\s*)+)(?:,\\sPRIMARY KEY[(](\\w+)[)])?[)];";

        Pattern syntaxExp = Pattern.compile(createRegex, Pattern.CASE_INSENSITIVE);
        Matcher queryParts = syntaxExp.matcher(query);
        if (queryParts.find()) {
            String tableName = queryParts.group(1);
            String columns = queryParts.group(2);
            String primaryKey = queryParts.group(8);

            if (tableName != null) {
                if (checkTableExist(tableName, dbName)) {
                    System.out.println("Table already exist");
                } else {
                    columns = columns.toLowerCase();

                    List<String> columnMetaList = Arrays.asList(columns.split(", "));
                    if (!columnMetaList.isEmpty()) {
                        List<String> valuesPartTemp = new ArrayList<>();
                        for (String col : columnMetaList) {
                            String[] temp = col.split(" ");
                            if (!valuesPartTemp.contains(temp[0].trim().toLowerCase())) {
                                valuesPartTemp.add(temp[0].trim().toLowerCase());
                            } else {
                                System.out.println("Column name should not be same.");
                                return;
                            }
                        }
                        PrintWriter printWriter = null;
                        try {
                            printWriter = new PrintWriter(new BufferedWriter(new FileWriter("src/main/java/Files/Database/METADATA_" + dbName.trim().toUpperCase() + ".txt", true)));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        String tableMeta = tableName + " || " + columns;
                        if (primaryKey != null) {
                            if (valuesPartTemp.contains(primaryKey.trim().toLowerCase())) {
                                tableMeta += " || " + primaryKey.trim().trim().toLowerCase();
                            } else {
                                System.out.println("Primary key must be from one of the columns.");
                                return;
                            }
                        }
                        if (printWriter != null) {
                            File tableFile = new File("src/main/java/Files/Database/" + dbName.trim().toUpperCase() + "_" + tableName.trim().toUpperCase() + ".txt");
                            try {
                                boolean isCreated = tableFile.createNewFile();
                                if (isCreated) {
                                    printWriter.println(tableMeta);
                                    printWriter.close();
                                    System.out.println("Table successfully created.");
                                }
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    } else {
                        System.out.println("Atleast one column is required.");
                    }
                }
            } else {
                System.out.println("Table name cannot be null.");
            }
        } else {
            System.out.println("Invalid query");
        }
    }

    public static boolean CreateSchemaParser(String query) {
        String createRegex = "CREATE DATABASE\\s(\\w+);";
        Pattern syntaxExp = Pattern.compile(createRegex, Pattern.CASE_INSENSITIVE);
        Matcher queryParts = syntaxExp.matcher(query);
        if (queryParts.find()) {
            String dbName = queryParts.group(1);
            CreateDb createDb = new CreateDb();
            createDb.create(dbName);
            return true;
        }
        return false;
    }

    static void InsertParser(String query, String dbName) {
        String insertRegex = "INSERT INTO (\\w+)(\\((?:\\w+)(?:, \\w+)*\\))? VALUES (\\((?:(?:\"(\\w+)\"|\\d+))(?:, (?:\"(\\w+)\"|\\d+))*\\));";
        Pattern syntaxExp = Pattern.compile(insertRegex, Pattern.CASE_INSENSITIVE);
        Matcher queryParts = syntaxExp.matcher(query);
        if (queryParts.find()) {
            String tableName = queryParts.group(1);
            if (tableName != null) {
                if (checkTableExist(tableName, dbName)) {
                    String columns = queryParts.group(2);
                    columns = columns.substring(1, queryParts.group(2).length() - 1);
                    String[] columnName = columns.split(",");

                    String values = queryParts.group(3);
                    values = values.substring(1, queryParts.group(3).length() - 1);
                    String[] valueName = values.split(",");

                    if (columnName.length == valueName.length) {
                        Map<String, String> nameValueMap = new HashMap<>();
                        List<String> columnsNameList = getColumnsNameList(queryParts.group(1), dbName);
                        String pk = getPrimaryKey(tableName, dbName);
                        List<String> primaryKeyValues = getPrimaryKeyValues(tableName, dbName);
                        for (int i = 0; i < columnName.length; i++) {
                            if (columnsNameList.contains(columnName[i].trim().toLowerCase())) {
                                if (pk != null) {
                                    if (pk.equals(columnName[i].trim().toLowerCase())) {
                                        if (valueName[i] != null && valueName[i].trim().length() != 0) {
                                            if (!primaryKeyValues.isEmpty() && primaryKeyValues.contains(valueName[i])) {
                                                System.out.println("Duplicate value not allowed in primary key column.");
                                                return;
                                            }
                                        } else {
                                            System.out.println("Primary Key value should not be null or empty.");
                                            return;
                                        }
                                    }
                                }
                                nameValueMap.put(columnName[i].trim().toLowerCase(), valueName[i].trim());
                            } else {
                                System.out.println("Column " + columnName[i].trim().toLowerCase() + " not exist in table.");
                                return;
                            }
                        }
                        if (!nameValueMap.keySet().isEmpty()) {
                            PrintWriter printWriter;
                            try {
                                printWriter = new PrintWriter(new BufferedWriter(new FileWriter("src/main/java/Files/Database/" + dbName.trim().toUpperCase() + "_" + tableName.trim().toUpperCase() + ".txt", true)));
                                StringBuilder insertData = new StringBuilder();
                                for (String colName : nameValueMap.keySet()) {
                                    insertData.append(colName).append(":").append(nameValueMap.get(colName)).append(" || ");
                                }
                                insertData = new StringBuilder(insertData.substring(0, insertData.length() - 4));
                                printWriter.println(insertData);
                                printWriter.close();
                                System.out.println("1 Row inserted.");
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    } else {
                        System.out.println("Number of columns and values are not same.");
                    }
                } else {
                    System.out.println("Table does not exist.");
                }
            } else {
                System.out.println("Table name cannot be null.");
            }
        } else {
            System.out.println("Invalid query");
        }
    }

    public String UseDatabase(String query) {
        String useRegex = "USE DATABASE\\s(\\w+);";
        String dbName = "";
        Pattern syntaxExp = Pattern.compile(useRegex, Pattern.CASE_INSENSITIVE);
        Matcher queryParts = syntaxExp.matcher(query);
        if (queryParts.find()) {
            dbName = queryParts.group(1);
            UseDb useDb = new UseDb();
            dbName = useDb.checkDb(dbName);
            if (dbName == null) {
                dbName = null;
            }
        } else {
            dbName = "Invalid";
        }
        return dbName;
    }
}
