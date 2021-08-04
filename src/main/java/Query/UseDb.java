package Query;

import java.io.File;

public class UseDb {
    public String checkDb(String dbName) {
        File file = new File("src/main/java/FileStorage/Database/METADATA_" + dbName + ".txt");
        if (file.exists()) {
            return dbName;
        }
        return null;
    }
}
