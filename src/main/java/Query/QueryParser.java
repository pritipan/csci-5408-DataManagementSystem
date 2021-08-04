package Query;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Basic.FeatureMenu.DATABASE_NAME;
import static Basic.LogGenerator.logQueryExecute;
import static Basic.Message.display;
import static Query.MetadataHandle.*;

public class QueryParser {

    public static void main(String[] arg) {
        //CreateParser("create table student3(id int NOT NULL, name varchar(45), name123 varchar(45), PRIMARY KEY(id));", "demo");
        //InsertParser("insert into student(id, name) values (1, \"foram\");", "demo");
    }

    public static void CreateParser(String query) {
        String createRegex = "CREATE TABLE\\s(\\w+)[(]((((\\w+)\\s(varchar[(]\\d+[)]|int)*\\s*(?:NOT\\sNULL)?)(,)*\\s*)+)(?:,\\sPRIMARY KEY[(](\\w+)[)])?[)];";

        Pattern syntaxExp = Pattern.compile(createRegex, Pattern.CASE_INSENSITIVE);
        Matcher queryParts = syntaxExp.matcher(query);
        if (queryParts.find()) {
            String tableName = queryParts.group(1);
            String columns = queryParts.group(2);
            String primaryKey = queryParts.group(8);

            if (tableName != null) {
                if (checkTableExist(tableName)) {
                    display("Table already exist");
                    logQueryExecute(query,"Table already exist");
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
                                display("Column name should not be same.");
                                logQueryExecute(query,"Column name should not be same.");
                                return;
                            }
                        }
                        PrintWriter printWriter = null;
                        try {
                            printWriter = new PrintWriter(new BufferedWriter(new FileWriter("src/main/java/Files/Database/METADATA_" + DATABASE_NAME.trim().toUpperCase() + ".txt", true)));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        String tableMeta = tableName + " || " + columns;
                        if (primaryKey != null) {
                            if (valuesPartTemp.contains(primaryKey.trim().toLowerCase())) {
                                tableMeta += " || " + primaryKey.trim().trim().toLowerCase();
                            } else {
                                display("Primary key must be from one of the columns.");
                                logQueryExecute(query,"Primary key must be from one of the columns.");
                                return;
                            }
                        }
                        File tableFile = new File("src/main/java/Files/Database/" + DATABASE_NAME.trim().toUpperCase() + "_" + tableName.trim().toUpperCase() + ".txt");
                        try {
                            boolean isCreated = tableFile.createNewFile();
                            if (isCreated) {
                                if (printWriter != null) {
                                    printWriter.println(tableMeta);
                                    printWriter.close();
                                }
                                display("Table successfully created.");
                                logQueryExecute(query,"Table successfully created.");
                            }
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    } else {
                        display("Atleast one column is required.");
                        logQueryExecute(query,"Atleast one column is required.");
                    }
                }
            } else {
                display("Table name cannot be null.");
                logQueryExecute(query,"Table name cannot be null.");
            }
        } else {
            display("Invalid query");
            logQueryExecute(query,"Invalid query");
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
            display("Database " + dbName.trim() + " created successfully.");
            logQueryExecute(query,"Database " + dbName.trim() + " created successfully.");
            return true;
        }
        return false;
    }

    static void InsertParser(String query) {
        String insertRegex = "INSERT INTO (\\w+)(\\((?:\\w+)(?:, \\w+)*\\))? VALUES (\\((?:(?:\"(\\w+)\"|\\d+))(?:, (?:\"(\\w+)\"|\\d+))*\\));";
        Pattern syntaxExp = Pattern.compile(insertRegex, Pattern.CASE_INSENSITIVE);
        Matcher queryParts = syntaxExp.matcher(query);
        if (queryParts.find()) {
            String tableName = queryParts.group(1);
            if (tableName != null) {
                if (checkTableExist(tableName)) {
                    String columns = queryParts.group(2);
                    columns = columns.substring(1, queryParts.group(2).length() - 1);
                    String[] columnName = columns.split(",");

                    String values = queryParts.group(3);
                    values = values.substring(1, queryParts.group(3).length() - 1);
                    String[] valueName = values.split(",");

                    if (columnName.length == valueName.length) {
                        Map<String, String> nameValueMap = new HashMap<>();
                        List<String> columnsNameList = getColumnsNameList(queryParts.group(1));
                        String pk = getPrimaryKey(tableName);
                        List<String> primaryKeyValues = getPrimaryKeyValues(tableName);
                        for (int i = 0; i < columnName.length; i++) {
                            if (columnsNameList.contains(columnName[i].trim().toLowerCase())) {
                                if (pk != null) {
                                    if (pk.equals(columnName[i].trim().toLowerCase())) {
                                        if (valueName[i] != null && valueName[i].trim().length() != 0) {
                                            if (!primaryKeyValues.isEmpty() && primaryKeyValues.contains(valueName[i])) {
                                                display("Duplicate value not allowed in primary key column.");
                                                logQueryExecute(query,"Duplicate value not allowed in primary key column.");
                                                return;
                                            }
                                        } else {
                                            display("Primary Key value should not be null or empty.");
                                            logQueryExecute(query,"Primary Key value should not be null or empty.");
                                            return;
                                        }
                                    }
                                }
                                nameValueMap.put(columnName[i].trim().toLowerCase(), valueName[i].trim());
                            } else {
                                display("Column " + columnName[i].trim().toLowerCase() + " not exist in table.");
                                logQueryExecute(query,"Column " + columnName[i].trim().toLowerCase() + " not exist in table.");
                                return;
                            }
                        }
                        if (!nameValueMap.keySet().isEmpty()) {
                            PrintWriter printWriter;
                            try {
                                printWriter = new PrintWriter(new BufferedWriter(new FileWriter("src/main/java/Files/Database/" + DATABASE_NAME.trim().toUpperCase() + "_" + tableName.trim().toUpperCase() + ".txt", true)));
                                StringBuilder insertData = new StringBuilder();
                                for (String colName : nameValueMap.keySet()) {
                                    insertData.append(colName).append(":").append(nameValueMap.get(colName)).append(" || ");
                                }
                                insertData = new StringBuilder(insertData.substring(0, insertData.length() - 4));
                                printWriter.println(insertData);
                                printWriter.close();
                                display("1 Row inserted.");
                                logQueryExecute(query,"1 Row inserted.");
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    } else {
                        display("Number of columns and values are not same.");
                        logQueryExecute(query,"Number of columns and values are not same.");
                    }
                } else {
                    display("Table does not exist.");
                    logQueryExecute(query,"Table does not exist.");
                }
            } else {
                display("Table name cannot be null.");
                logQueryExecute(query,"Table name cannot be null.");
            }
        } else {
            display("Invalid query");
            logQueryExecute(query,"Invalid query");
        }
    }

    public static String UseDatabase(String query) {
        String useRegex = "USE DATABASE\\s(\\w+);";
        String dbName = null;
        Pattern syntaxExp = Pattern.compile(useRegex, Pattern.CASE_INSENSITIVE);
        Matcher queryParts = syntaxExp.matcher(query);
        if (queryParts.find()) {
            dbName = queryParts.group(1);
            UseDb useDb = new UseDb();
            dbName = useDb.checkDb(dbName);
        }
        return dbName;
    }
    public static boolean selectQueryParser(String query) {
        String selectRegex = "^select\\s(?:\\*|\\w+)\\sfrom\\s\\w+;?\\s*$";
        String selectWithWhereRegex = "select\\s.*from\\s.*where\\s.*";
        String dbName = null;
        boolean isQueryValid = false;
        Pattern syntaxExp = Pattern.compile(selectRegex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher queryParts = syntaxExp.matcher(query);
        if (queryParts.find()) {
            isQueryValid = true;
            int index = 1;
            String[] commandTokens = query.split(" ");
            StringBuilder columns = new StringBuilder();
            while (!commandTokens[index].equalsIgnoreCase("from")) {
                columns.append(commandTokens[index++]);
            }
            columns = new StringBuilder(columns.toString().replaceAll("\\s+", ""));

            StringBuilder tables = new StringBuilder();
            index++;
            while (index < commandTokens.length && !commandTokens[index].equalsIgnoreCase("where")) {
                tables.append(commandTokens[index++]);
            }
            tables = new StringBuilder(tables.toString().replaceAll("\\s+", ""));

            String[] tokens = new String[4];
            if (index != commandTokens.length) {
                StringBuilder condition = new StringBuilder();
                for (int i = index + 1; i < commandTokens.length; i++) {
                    condition.append(commandTokens[i]);
                }
                condition = new StringBuilder(condition.toString().replaceAll("\\s+", ""));

                tokens = new String[6];
                tokens[4] = "where";
                tokens[5] = condition.toString();
            }
            tokens[0] = commandTokens[0];
            tokens[1] = columns.toString();
            tokens[2] = "from";
            tokens[3] = tables.toString();
            System.out.println(tokens[1]);


        }
        return isQueryValid;
    }
}
