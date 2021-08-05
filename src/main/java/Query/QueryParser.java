package Query;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Basic.FeatureMenu.DATABASE_NAME;
import static Basic.LogGenerator.logQueryExecute;
import static Basic.Message.display;
import static Query.MetadataHandle.*;

public class QueryParser {

  public static void main(String[] arg) throws IOException {
    //CreateParser("create table student3(id int NOT NULL, name varchar(45), name123 varchar(45), PRIMARY KEY(id));", "demo");
    //InsertParser("insert into student(id, name) values (1, \"foram\");", "demo");
    DATABASE_NAME = "TESTONE";
//    UseDatabase("use database DEMO;");
//    UpateParser("UPDATE qwe SET name = \"three\" WHERE name=\"one\";");
    AlterParser("alter table student2 add foreign key(name) references "
//        "student3" +
//        "(id);");
//      DeleteParser("DELETE FROM qwe WHERE name=\"three\" ; "
          );
  }

  public static void CreateParser(String query) {

    String createRegex = "CREATE TABLE\\s*(?<tableName>\\w+)\\((?<columns>(" +
        "(\\w+\\s" +
        "(varchar\\(\\d+\\)|int)\\s*(?:NOT NULL)?,?\\s*)+))(PRIMARY " +
        "KEY\\((?<PK>\\w+)\\))?\\);";
    Pattern syntaxExp = Pattern.compile(createRegex, Pattern.CASE_INSENSITIVE);
    Matcher queryParts = syntaxExp.matcher(query);
    if (queryParts.find()) {
      String tableName = queryParts.group("tableName");
      String columns = queryParts.group("columns");
      String primaryKey = queryParts.group("PK");

      if (tableName != null) {
        if (checkTableExist(tableName)) {
          display("Table already exist");
          logQueryExecute(query, "Table already exist");
        } else {
          columns = columns.toLowerCase();

          List<String> columnMetaList = Arrays.asList(columns.trim().split(","));
          if (!columnMetaList.isEmpty()) {
            List<String> valuesPartTemp = new ArrayList<>();
            for (String col : columnMetaList) {
              String[] temp = col.trim().split(" ");
              if (!valuesPartTemp.contains(temp[0].trim().toLowerCase())) {
                valuesPartTemp.add(temp[0].trim().toLowerCase());
              } else {
                display("Column name should not be same.");
                logQueryExecute(query, "Column name should not be same.");
                return;
              }
            }
            PrintWriter printWriter = null;
            try {
              printWriter = new PrintWriter(new BufferedWriter(new FileWriter("src/main/java/FileStorage/Database/METADATA_" + DATABASE_NAME.trim().toUpperCase() + ".txt", true)));
            } catch (IOException ioException) {
              ioException.printStackTrace();
            }
            String tableMeta = tableName + " || " + String.join(",",columnMetaList);
            if (primaryKey != null) {
              if (valuesPartTemp.contains(primaryKey.trim().toLowerCase())) {
                tableMeta += " || " + primaryKey.trim().trim().toLowerCase();
              } else {
                display("Primary key must be from one of the columns.");
                logQueryExecute(query, "Primary key must be from one of the columns.");
                return;
              }
            }
            File tableFile = new File("src/main/java/FileStorage/Database/" + DATABASE_NAME.trim().toUpperCase() + "_" + tableName.trim().toUpperCase() + ".txt");
            try {
              boolean isCreated = tableFile.createNewFile();
              if (isCreated) {
                if (printWriter != null) {
                  printWriter.println(tableMeta);
                  printWriter.close();
                }
                display("Table successfully created.");
                logQueryExecute(query, "Table successfully created.");
              }
            } catch (IOException ioException) {
              ioException.printStackTrace();
            }
          } else {
            display("Atleast one column is required.");
            logQueryExecute(query, "Atleast one column is required.");
          }
        }
      } else {
        display("Table name cannot be null.");
        logQueryExecute(query, "Table name cannot be null.");
      }
    } else {
      display("Invalid query");
      logQueryExecute(query, "Invalid query");
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
      logQueryExecute(query, "Database " + dbName.trim() + " created successfully.");
      return true;
    }
    return false;
  }

  static void InsertParser(String query) {

    String insertRegex = "(?!)INSERT INTO (\\w+)(\\((?:\\w+)(?:, \\w+)*\\))? " +
        "VALUES (\\((?:(?:\"(\\w+)\"|\\d+))(?:, (?:\"(\\w+)\"|\\d+))*\\));";
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
                        logQueryExecute(query, "Duplicate value not allowed in primary key column.");
                        return;
                      }
                    } else {
                      display("Primary Key value should not be null or empty.");
                      logQueryExecute(query, "Primary Key value should not be null or empty.");
                      return;
                    }
                  }
                }
                nameValueMap.put(columnName[i].trim().toLowerCase(), valueName[i].trim());
              } else {
                display("Column " + columnName[i].trim().toLowerCase() + " not exist in table.");
                logQueryExecute(query, "Column " + columnName[i].trim().toLowerCase() + " not exist in table.");
                return;
              }
            }
            if (!nameValueMap.keySet().isEmpty()) {
              PrintWriter printWriter;
              try {
                printWriter = new PrintWriter(new BufferedWriter(new FileWriter("src/main/java/FileStorage/Database/" + DATABASE_NAME.trim().toUpperCase() + "_" + tableName.trim().toUpperCase() + ".txt", true)));
                StringBuilder insertData = new StringBuilder();
                for (String colName : nameValueMap.keySet()) {
                  insertData.append(colName).append(":").append(nameValueMap.get(colName)).append(" || ");
                }
                insertData = new StringBuilder(insertData.substring(0, insertData.length() - 4));
                printWriter.println(insertData);
                printWriter.close();
                display("1 Row inserted.");
                logQueryExecute(query, "1 Row inserted.");
              } catch (IOException ioException) {
                ioException.printStackTrace();
              }
            }
          } else {
            display("Number of columns and values are not same.");
            logQueryExecute(query, "Number of columns and values are not same.");
          }
        } else {
          display("Table does not exist.");
          logQueryExecute(query, "Table does not exist.");
        }
      } else {
        display("Table name cannot be null.");
        logQueryExecute(query, "Table name cannot be null.");
      }
    } else {
      display("Invalid query");
      logQueryExecute(query, "Invalid query");
    }
  }

   public static boolean UpdateParser(String query) throws IOException {

     String updateRegex = "UPDATE\\s+(?<tablename>\\w+)\\s+SET\\s+" +
         "(?<values>(\\w+\\s*=\\s*[\"\']?\\w+[\"\']?[,\\s]*)+)\\s+where\\s+" +
         "(?<conditions>" + "(\\w+\\s*=\\s*[\"\']?\\w+[\"\']?[,\\s]*)*);?";
     Pattern syntaxExp = Pattern.compile(updateRegex, Pattern.CASE_INSENSITIVE);
     Matcher queryParts = syntaxExp.matcher(query);
     System.out.println(query);
     String result = "";
     String fileName = "";
     boolean flag = false;
     if (queryParts.find()) {
       String tableName = queryParts.group("tablename");

       if (tableName != null && checkTableExist(tableName)) {
         fileName = "src/main/java/FileStorage/Database/" + DATABASE_NAME.trim().toUpperCase() + "_" + tableName.trim().toUpperCase() + ".txt";

         String[] colName = queryParts.group("values").split(",");
         String[] tempCondition = queryParts.group("conditions").split(",");
         Map<String, String> columnValuePair = convertToMap(Arrays.asList(colName), "=");

         List<String> conditions = Arrays.asList(tempCondition);
         System.out.println(conditions);

         List<String> rows = getAllRows(fileName);
         for (String line : rows) {
           String[] temp = line.split(" \\|\\|");
           Map<String, String> rowData = convertToMap(Arrays.asList(temp), ":");
           if (conditions.size() == 0)
             flag = true;
           else
             flag = false;
           for (String condition : conditions) {
             String conditionKey = condition.split("=")[0].trim();
             String conditionValue = condition.split("=")[1].trim();
             if (rowData.containsKey(conditionKey)) {
               if (rowData.get(conditionKey).equals(conditionValue))
                 flag = true;
             } else {
               System.out.println("Invalid Condition " + conditionKey + "=" + conditionValue + " !");
               return false;
             }
           }
           System.out.println(flag);
           if (flag) {
             for (Map.Entry<String, String> updatePair : columnValuePair.entrySet()) {
               String updateColumnName = updatePair.getKey();
               String updateColumnValue = updatePair.getValue();
               if (rowData.containsKey(updateColumnName)) {
                 rowData.put(updateColumnName, updateColumnValue);
               } else {
                 System.out.println("Invalid Column Name " + updateColumnName + " !");
                 return false;
               }

             }
           }
           result += mapToString(rowData) + "\n";
         }
         System.out.println(result);
         try {
           FileWriter fWriter = new FileWriter(fileName);
           fWriter.write(result);
           fWriter.close();
           System.out.println("File is created successfully with the content.");
         } catch (IOException e) {
           System.out.print(e.getMessage());
         }
       }
     } else {
       System.out.println("Invalid Query !");
     }
     return true;
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

  private static Map<String, String> convertToMap(List<String> arr,
                                            String delimeter) {

    Map<String, String> result = new HashMap<>();

    for (String s : arr) {
      result.put(s.split(delimeter)[0].trim(), s.split(delimeter)[1].trim());
    }
    System.out.println(result);

    return result;
  }

  private static String mapToString(Map<String,String> map)
  {
    List<String> list = new ArrayList<>();

    for(Map.Entry<String,String> temp : map.entrySet())
    {
      list.add(temp.getKey()+":"+temp.getValue());

    }
    return String.join(" || ", list);
  }

  private static List<String> getAllRows(String fileName) throws IOException {

    List<String> result;
    try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
      result = lines.collect(Collectors.toList());
    }
    return result;
  }

  public static boolean DeleteParser(String query) throws IOException {

    String deleteRegex = "DELETE\\s+FROM\\s+(?<tablename>\\w+)\\s*(WHERE\\s" +
        "(?<conditions>(\\w+\\s*(=|>=|<=|!=)\\s*[\"\']?\\w+[\"\']?[,\\s]*)*))" +
        "?;";
    String operators = "(?<operator>(=|>=|<=|!=))";
    Pattern syntaxExp = Pattern.compile(deleteRegex, Pattern.CASE_INSENSITIVE);
    Pattern operatorExp = Pattern.compile(operators,
        Pattern.CASE_INSENSITIVE);
    Matcher queryParts = syntaxExp.matcher(query);
    String result = "";
    String fileName = "";
    boolean flag = false;
//    System.out.println(queryParts.find());
    if (queryParts.find()) {
      String tableName = queryParts.group("tablename");
      System.out.println(tableName);
      if (tableName != null && checkTableExist(tableName)) {
        List<String> conditions = new ArrayList<>();
        if(queryParts.group("conditions") != null){
          String[] tempCondition = queryParts.group("conditions").split(",");
         conditions = Arrays.asList(tempCondition);}

        System.out.println(conditions);
        if(conditions.size() == 0)
        {
          fileName =
              "src/main/java/FileStorage/Database/METADATA_" + DATABASE_NAME.trim().toUpperCase() + ".txt";
          List<String> rows = getAllRows(fileName);
          for (String line : rows) {
            String[] temp = line.split(" \\|\\|");
            if(!tableName.equals(temp[0]))
            {
              result += line+"\n";
            }
          }
          File myObj = new File("src/main/java/FileStorage/Database/" + DATABASE_NAME.trim().toUpperCase() + "_" + tableName.trim().toUpperCase() + ".txt");
          if (myObj.delete()) {
            System.out.println("Table " + tableName+" Deleted Successfully !");
          } else {
            System.out.println("Failed to delete table "+tableName);
          }
        }
        else {
          fileName = "src/main/java/FileStorage/Database/" + DATABASE_NAME.trim().toUpperCase() + "_" + tableName.trim().toUpperCase() + ".txt";
          List<String> rows = getAllRows(fileName);
          for (String line : rows) {
            String[] temp = line.split(" \\|\\|");
            Map<String, String> rowData = convertToMap(Arrays.asList(temp), ":");
            if (conditions.size() == 0)
              flag = true;
            else
              flag = false;
            for (String condition : conditions) {
              Matcher conditionParts = operatorExp.matcher(condition);
              String operator = null;
              if (conditionParts.find()) {
                operator = conditionParts.group("operator");
                System.out.println(operator);
                String conditionKey = condition.split(operator)[0].trim();
                String conditionValue = condition.split(operator)[1].trim();
                if (rowData.containsKey(conditionKey)) {
                  switch (operator) {
                    case "=":
                      if (rowData.get(conditionKey).equals(conditionValue))
                        flag = true;
                      break;
                    case "!=":
                      if (!rowData.get(conditionKey).equals(conditionValue))
                        flag = true;
                      break;
                    case ">":
                      if (Double.parseDouble(rowData.get(conditionKey)) > Double.parseDouble(conditionValue))
                        flag = true;
                      break;
                    case "<":
                      if (Double.parseDouble(rowData.get(conditionKey)) < Double.parseDouble(conditionValue))
                        flag = true;
                      break;
                    case ">=":
                      if (Double.parseDouble(rowData.get(conditionKey)) >= Double.parseDouble(conditionValue))
                        flag = true;
                      break;
                    case "<=":
                      if (Double.parseDouble(rowData.get(conditionKey)) <= Double.parseDouble(conditionValue))
                        flag = true;
                      break;
                    default:
                      flag = false;

                  }
                } else {
                  System.out.println("Invalid Condition " + conditionKey + "=" + conditionValue + " !");
                  return false;
                }
              }
            }
            if (!flag) {
              result += mapToString(rowData) + "\n";
            }
          }
        }
        System.out.println(result);
        try {
          FileWriter fWriter = new FileWriter(fileName);
          fWriter.write(result);
          fWriter.close();
          System.out.println("File is created successfully with the content.");
        } catch (IOException e) {
          System.out.print(e.getMessage());
        }

      }
    } else {
      System.out.println("Invalid Query !");
    }
    return true;
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

  public static void AlterParser(String query) {

    String alterRegex = "ALTER TABLE (\\w+) ADD FOREIGN KEY[(](\\w+)[)] REFERENCES (\\w+)[(](\\w+)[)];";

    Pattern syntaxExp = Pattern.compile(alterRegex, Pattern.CASE_INSENSITIVE);
    Matcher queryParts = syntaxExp.matcher(query);
    if (queryParts.find()) {
      String tableName = queryParts.group(1).trim().toLowerCase();
      if (checkTableExist(tableName)) {
        String colName = queryParts.group(2).trim().toLowerCase();
        List<String> columnsNameList = getColumnsNameList(tableName);
        if (columnsNameList.contains(colName)) {
          String refTableName = queryParts.group(3).trim().toLowerCase();
          if (checkTableExist(refTableName)) {
            String pk = getPrimaryKey(refTableName);
            System.out.println(pk);
            if (pk != null) {
              String refColName = queryParts.group(4).trim().toLowerCase();
              if (pk.equals(refColName)) {
                String tableMetaData = getTableMetaData(tableName);
                List<String> fileContent;
                try {
                  String filePath = "src/main/java/FileStorage/Database/METADATA_" + DATABASE_NAME.trim().toUpperCase() + ".txt";
                  fileContent = new ArrayList<>(Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8));
                  String newTableMetaData = tableMetaData + " || fk:" + colName + ":" + refTableName + ":" + refColName;
                  for (int i = 0; i < fileContent.size(); i++) {
                    if (fileContent.get(i).trim().equals(tableMetaData)) {
                      fileContent.set(i, newTableMetaData);
                      break;
                    }
                  }
                  Files.write(Paths.get(filePath), fileContent, StandardCharsets.UTF_8);
                } catch (IOException e) {
                  e.printStackTrace();
                }
                System.out.println(tableMetaData);
              } else {
                System.out.println("Invalid Reference Field : " + refColName + ". Only Primary Key of Reference Table use as foreign key.");
              }
            } else {
              System.out.println("Foreign key cannot be added as there is no primary key in the table");
            }
          } else {
            System.out.println("Table " + refTableName + " does not exist!!");
          }
        } else {
          System.out.println("Column " + colName + " does not exist!!");
        }
      } else {
        System.out.println("Table " + tableName + " does not exist!!");
      }

    } else {
      display("Invalid query");
      logQueryExecute(query, "Invalid query");
    }
  }
}
