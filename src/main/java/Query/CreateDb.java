package Query;

import java.io.File;
import java.io.IOException;

public class CreateDb {
    public void create(String dbName) {
        File file = new File("src/main/java/Files/Database/METADATA_" + dbName.trim().toUpperCase() + ".txt");
        try {
            file.createNewFile();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
