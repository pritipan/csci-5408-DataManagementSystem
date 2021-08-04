package Basic;

import java.util.HashMap;
import java.util.Map;

public class TableMeta {

  public Map<String, ColumnMeta> tableMetaData = new HashMap<>();

  public Map<String, ColumnMeta> getTableMeta() {

    return tableMetaData;
  }

  public void setTableMeta(String[] tableMeta) {

    boolean nn = false;
    String tableName = tableMeta[0].trim();

    for (String column : tableMeta[1].split(",")) {

      column = column.replace("not null", "notnull");
      String[] columnMeta = column.trim().split(" ");

      String columnName = columnMeta[0].trim();
      String columnType = columnMeta[1].trim();
      if (columnMeta.length == 3)
        nn = true;
      else
        nn = false;
      ColumnMeta columnMetaData = new ColumnMeta();
      columnMetaData.setColumnName(columnName);
      columnMetaData.setColumnType(columnType);
      columnMetaData.setNotNull(nn);
      tableMetaData.put(columnName, columnMetaData);
    }


    if (tableMeta.length >= 3 && tableMetaData.containsKey(tableMeta[2].trim())) {
      tableMetaData.get(tableMeta[2].trim()).setPrimaryKey(true);
      if (tableMeta.length == 4) {
        String[] fkData = tableMeta[3].trim().split(":");
        tableMetaData.get(fkData[1].trim()).setFkTable(fkData[2].trim());
        tableMetaData.get(fkData[1].trim()).setFkColumn(fkData[3].trim());
      }
    } else if (tableMeta.length >= 3) {
      String[] fkData = tableMeta[2].trim().split(":");
      tableMetaData.get(fkData[1].trim()).setFkTable(fkData[2].trim());
      tableMetaData.get(fkData[1].trim()).setFkColumn(fkData[3].trim());
    }
  }

}
