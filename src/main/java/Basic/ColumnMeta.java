package Basic;

public class ColumnMeta {

  
  private String columnName;
  private String columnType;
  private boolean primaryKey=false;
  private boolean notNull=false;
  private String fkTable=null;
  private String fkColumn=null;

  public String getColumnName() {

    return columnName;
  }

  public void setColumnName(String columnName) {

    this.columnName = columnName;
  }

  public String getColumnType() {

    return columnType;
  }

  public void setColumnType(String columnType) {

    this.columnType = columnType;
  }

  public boolean getPrimaryKey() {

    return primaryKey;
  }

  public void setPrimaryKey(boolean primaryKey) {

    this.primaryKey = primaryKey;
  }

  public boolean getNotNull() {

    return notNull;
  }

  public void setNotNull(boolean notNull) {

    this.notNull = notNull;
  }


  public String getFkTable() {

    return fkTable;
  }

  public void setFkTable(String fkTable) {

    this.fkTable = fkTable;
  }

  public String getFkColumn() {

    return fkColumn;
  }

  public void setFkColumn(String fkColumn) {

    this.fkColumn = fkColumn;
  }


}
