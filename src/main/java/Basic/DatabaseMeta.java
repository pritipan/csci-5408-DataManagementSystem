package Basic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Basic.FeatureMenu.DATABASE_NAME;

public class DatabaseMeta {
  Map<String,TableMeta> databaseMeta = new HashMap<>();
  public void loadDatabase()
  {
    List<String> result = new ArrayList<>();
    try (Stream<String> lines = Files.lines(Paths.get( "src/main/java/FileStorage/Database/METADATA_" + DATABASE_NAME.trim().toUpperCase() + ".txt"))) {
      result = lines.collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }
//    System.out.println(result);
    for(String table : result)
    {
      String[] tableMeta = table.split(" \\|\\|");
      String tableName = tableMeta[0];
      TableMeta tableMetaData = new TableMeta();
      tableMetaData.setTableMeta(tableMeta);
      databaseMeta.put(tableName,tableMetaData);
    }
//    System.out.println(databaseMeta);
  }

  public Map<String,TableMeta> getDatabaseMeta()
  {
    return databaseMeta;
  }
}
