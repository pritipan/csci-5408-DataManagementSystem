package Basic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static Basic.FeatureMenu.DATABASE_NAME;

public class DatabaseState {
    public static void main(String[] arg) {
        DATABASE_NAME="Demo";
        generateDbState();
    }
    public static void generateDbState(){
        String fileMetadataName = "src/main/java/FileStorage/Database/METADATA_" + DATABASE_NAME.trim().toUpperCase() + ".txt";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileMetadataName));
            String line;
            System.out.println("----------------State of database: "+DATABASE_NAME.trim().toUpperCase()+" --------------------");
            System.out.println();
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(" \\|\\| ");
                System.out.println("Table Name: "+parts[0].trim().toUpperCase());
                System.out.println("Attributes: "+parts[1].trim().toUpperCase());
                BufferedReader bufferedReader1 = new BufferedReader(new FileReader(new File("src/main/java/FileStorage/Database/" + DATABASE_NAME.trim().toUpperCase() + "_" + parts[0].trim().toUpperCase() + ".txt")));
                int countRows=0;
                while ((bufferedReader1.readLine()) != null) {
                    countRows++;
                }
                System.out.println("Number of rows: "+countRows);
                System.out.println();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
