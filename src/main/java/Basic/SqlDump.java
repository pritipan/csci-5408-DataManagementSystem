package Basic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static Basic.FeatureMenu.DATABASE_NAME;
import static Query.MetadataHandle.getPrimaryKey;

public class SqlDump {

    public static void generateDump() {
        List<String> tableNames = new ArrayList<>();
        String metadataName = "src/main/java/FileStorage/Database/METADATA_" + DATABASE_NAME.trim().toUpperCase() + ".txt";
        String dumpName = "src/main/java/FileStorage/Database/" + DATABASE_NAME.trim().toUpperCase() + "_DUMP.txt";
        PrintWriter printWriter;
        try {
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter(dumpName)));
            printWriter.println("DATABASE : " + DATABASE_NAME.trim().toUpperCase());
            printWriter.println();
            printWriter.println("CREATE DATABASE "+DATABASE_NAME.trim().toUpperCase()+";");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(metadataName));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(" \\|\\| ");
                if (getPrimaryKey(parts[0].trim()) == null) {
                    printWriter.println("CREATE TABLE " + parts[0].trim().toUpperCase() + " (" + parts[1].trim().toUpperCase() + ");");
                } else {
                    printWriter.println("CREATE TABLE " + parts[0].trim().toUpperCase() + " (" + parts[1].trim().toUpperCase() + ", PRIMARY KEY(" + parts[2].trim().toUpperCase() + "));");
                }
                tableNames.add(parts[0].trim());
            }
            printWriter.println();
            printWriter.println();

            for (String tableName : tableNames) {
                String tableNameFile = "src/main/java/FileStorage/Database/" + DATABASE_NAME.trim().toUpperCase() + "_" + tableName.trim().toUpperCase() + ".txt";
                printWriter.println("TABLE : " + tableName.trim().toUpperCase());
                Scanner scanner = new Scanner(new FileReader(tableNameFile));
                bufferedReader = new BufferedReader(new FileReader(tableNameFile));
                if (!scanner.hasNext()) {
                    printWriter.println("NO ROWS INSERTED");
                } else {
                    while ((line = bufferedReader.readLine()) != null) {
                        if (!line.isEmpty()) {
                            String[] parts = line.split(" \\|\\| ");
                            String insertQuery = "INSERT INTO " + tableName.trim().toUpperCase();
                            StringBuilder columns = new StringBuilder();
                            StringBuilder values = new StringBuilder();
                            for (String col : parts) {
                                columns.append(col.trim().split(":")[0].trim()).append(", ");
                                values.append(col.trim().split(":")[1].trim()).append(", ");
                            }
                            columns = new StringBuilder(columns.substring(0, columns.length() - 2).toUpperCase());
                            values = new StringBuilder(values.substring(0, values.length() - 2).toUpperCase());
                            insertQuery += " (" + columns + ") VALUES (" + values + ");";
                            printWriter.println(insertQuery);
                        }
                    }
                }

                printWriter.println();
                printWriter.println();
            }
            printWriter.close();
            System.out.println("Dump file successfully created. Please check in "+DATABASE_NAME.trim().toUpperCase() + "_DUMP.txt");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
