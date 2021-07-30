package Query;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {

    public static void main(String[] arg) throws IOException {
        CreateParser("create table student(id int NOT NULL, name varchar(45), name varchar(45), PRIMARY KEY(id));", "demo");
    }

    public static void CreateParser(String query, String dbName) {
        //String createRegex = "CREATE TABLE\\s(\\w+)[(]((((\\w+)\\s(varchar[(]\\d+[)]|int))(,)*\\s*)+)[)];";
        String createRegex = "CREATE TABLE\\s(\\w+)[(]((((\\w+)\\s(varchar[(]\\d+[)]|int)*\\s*(?:NOT\\sNULL)?)(,)*\\s*)+)(?:,\\sPRIMARY KEY[(](\\w+)[)])?[)];";

        Pattern syntaxExp = Pattern.compile(createRegex, Pattern.CASE_INSENSITIVE);
        Matcher queryParts = syntaxExp.matcher(query);
        if (queryParts.find()) {
            String tableName = queryParts.group(1);
            String columns = queryParts.group(2);
            String primaryKey = queryParts.group(8);
            List<String> valuesPart = Arrays.asList(columns.split(", "));
            List<String> valuesPartTemp = new ArrayList<>();
            for (String str : valuesPart) {
                String[] temp = str.split(" ");
                valuesPartTemp.add(temp[0]);
            }
            if (valuesPartTemp.contains(primaryKey)) {
                File file = new File("src/main/java/Files/Database/" + dbName.trim().toUpperCase(Locale.ROOT) + "_" + tableName.trim().toUpperCase(Locale.ROOT) + ".txt");
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PrintWriter printWriter = null;
                try {
                    printWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File("src/main/java/Files/Database/METADATA_" + dbName.trim().toUpperCase(Locale.ROOT) + ".txt"), true)));
                    printWriter.println(tableName + " || " + columns + " || " + primaryKey);
                    printWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Enter primary key does not match with the query columns.");
            }
        } else {
            System.out.println("Invalid query");
        }
    }

    public Boolean CreateSchemaParser(String query) throws IOException {
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

    public String UseDatabase(String query) throws IOException {
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
