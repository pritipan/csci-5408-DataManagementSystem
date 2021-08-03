package Query;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Basic.FeatureMenu.DATABASE_NAME;


public class MetadataHandle {
    public static void main(String[] arg) {
        //getColumnsNameList("student", "demo");
    }

    static boolean checkTableExist(String tableName) {
        String fileName = "src/main/java/Files/Database/METADATA_" + DATABASE_NAME.trim().toUpperCase() + ".txt";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(" \\|\\| ");
                if (tableName.equalsIgnoreCase(parts[0].trim())) {
                    return true;
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return false;
    }

    static List<String> getColumnsNameList(String tableName) {
        String fileName = "src/main/java/Files/Database/METADATA_" + DATABASE_NAME.trim().toUpperCase() + ".txt";
        List<String> columnNameArray = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(" \\|\\| ");
                if (tableName.equals(parts[0].trim())) {
                    String[] temp = parts[1].split(",");
                    for (String s : temp) {
                        columnNameArray.add(s.trim().split(" ")[0].trim().toLowerCase());
                    }
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return columnNameArray;
    }

    public static String getPrimaryKey(String tableName) {
        String fileName = "src/main/java/Files/Database/METADATA_" + DATABASE_NAME.trim().toUpperCase() + ".txt";
        String primaryKey = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(" \\|\\| ");
                if (tableName.equals(parts[0].trim())) {
                    if (parts.length > 2) {
                        primaryKey = parts[2].trim().toLowerCase();
                        break;
                    }
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return primaryKey;
    }

    static List<String> getPrimaryKeyValues(String tableName) {
        List<String> pkValues = new ArrayList<>();
        String primaryKey = getPrimaryKey(tableName);
        if (primaryKey != null) {
            String fileName = "src/main/java/Files/Database/" + DATABASE_NAME.trim().toUpperCase() + "_" + tableName.trim().toUpperCase() + ".txt";
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] colParts = line.split(" \\|\\| ");
                    for (String col : colParts) {
                        String colName = col.trim().split(":")[0].trim();
                        if (primaryKey.equalsIgnoreCase(colName)) {
                            pkValues.add(col.trim().split(":")[1].trim());
                        }
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return pkValues;
    }
}
