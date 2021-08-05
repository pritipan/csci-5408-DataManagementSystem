package Basic;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static Basic.FeatureMenu.DATABASE_NAME;

public  class Erd {
  String result;
  static final String ARROW = "---------------->";
  static final String ONETOONE = "--1-----------1--";
  static final String ONETOMANY = "--1-----------N--";
  static final String MANYTOONE = "--N-----------1--";


  static  DatabaseMeta  database = new DatabaseMeta();

  public static void generateERD() {
//    DATABASE_NAME = "DEMO";
    database.loadDatabase();
//    System.out.println(getAllRelations());
    String out = "";

    Map<String, String> relations =  getAllRelations();
    Map<String, TableMeta> databaseMeta = database.getDatabaseMeta();
    for (Map.Entry<String, TableMeta> table : databaseMeta.entrySet()) {
      String heading = "\n";
      String relation = "";
      String foreignKey ="" ;
      String columns = "";
      String tableName = table.getKey();
      TableMeta tableMeta = table.getValue();
      heading += "******************** Table : "+tableName+" " +
          "********************";
      for (Map.Entry<String, String> parentRelation : relations.entrySet()) {
        String[] rel = parentRelation.getKey().split("->");
        String connector = parentRelation.getValue();
        if(tableName.equals(rel[1]))
        {
          if(connector.equals(MANYTOONE))
            connector = ONETOMANY;
          relation += rel[1]+connector+rel[0]+"\n";
        }
      }

        for (Map.Entry<String, ColumnMeta> column : tableMeta.getTableMeta().entrySet()) {
        String connector = ARROW;
        String columnName = column.getKey();
        ColumnMeta columnMeta = column.getValue();
        String fkTable = columnMeta.getFkTable();
        String fkColumn = columnMeta.getFkColumn();
        columns += columnName+" "+columnMeta.getColumnType();
        if(columnMeta.getPrimaryKey())
          columns += " (PK)\n";
        else
          columns += "\n";
        if (fkTable != null) {
          foreignKey += columnName+ARROW+fkTable+"("+fkColumn+")\n";
          connector = relations.get(tableName+"->"+fkTable);
          relation += tableName+connector+fkTable;
        }
      }
        if(relation.equals(""))
          relation += "Nothing";
        if(foreignKey.equals(""))
          foreignKey += "Nothing";
        if(columns.equals(""))
          columns += "Nothing";

        out += heading+"\n\nCardinality :- \n"+relation+"\n\nForeignKey :- " +
            "\n"+foreignKey+"\n\nColumns :- \n"+columns+
            "\n--------------------------------------------------\n";
    }
    System.out.println(out);
    try {
      FileWriter fWriter = new FileWriter("src/main/java/FileStorage/Database" +
          "/ERDiagram_" + DATABASE_NAME.trim().toUpperCase() + ".txt");
      fWriter.write(out);
      fWriter.close();
      System.out.println("File is created successfully with the content.");
    }
    catch (IOException e) {
      System.out.print(e.getMessage());
    }
  }

  private static Map<String,String> getAllRelations() {

    Map<String, String> relations = new HashMap<>();
    Map<String, TableMeta> databaseMeta = database.getDatabaseMeta();
    for (Map.Entry<String, TableMeta> table : databaseMeta.entrySet()) {
      String tableName = table.getKey();
      TableMeta tableMeta = table.getValue();
      for (Map.Entry<String, ColumnMeta> column : tableMeta.getTableMeta().entrySet()) {
        String connector = ARROW;
        String columnName = column.getKey();
        ColumnMeta columnMeta = column.getValue();
        String fkTable = columnMeta.getFkTable();
        String fkColumn = columnMeta.getFkColumn();
        if (fkTable != null) {
          ColumnMeta fkColumnMeta = databaseMeta.get(fkTable).getTableMeta().get(fkColumn);
//          System.out.println(columnMeta.getPrimaryKey());
          if (columnMeta.getPrimaryKey() && fkColumnMeta.getPrimaryKey())
            connector = ONETOONE;
          else
            connector = MANYTOONE;
          relations.put(tableName+"->"+fkTable,connector);
        }

      }

    }

  return relations;
  }


}
