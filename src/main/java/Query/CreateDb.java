package Query;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class CreateDb {
    public void create(String dbName) throws IOException {
        File file = new File("src/main/java/Files/Database/METADATA_" + dbName.trim().toUpperCase(Locale.ROOT) + ".txt");
        file.createNewFile();
    }
}
