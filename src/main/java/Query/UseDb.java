package Query;

import java.io.File;
import java.util.Locale;

public class UseDb {
    public String checkDb(String dbName){
        File file = new File("src/main/java/Files/Database/METADATA_" + dbName + ".txt");
        if(file.exists()){
            return dbName;
        }
        return null;
    }
}
