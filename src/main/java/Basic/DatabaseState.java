package Basic;

import java.io.*;

import static Basic.FeatureMenu.DATABASE_NAME;

public class DatabaseState {

    public static void generateDbState(){
        File file = new File("src/main/java/FileStorage/GeneralLogs.txt");
        PrintWriter printWriter;
        String fileMetadataName = "src/main/java/FileStorage/Database/METADATA_" + DATABASE_NAME.trim().toUpperCase() + ".txt";
        try {
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileMetadataName));
            String line;
            System.out.println("General logs of "+DATABASE_NAME.trim().toUpperCase()+" created");
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(" \\|\\| ");
                printWriter.println("Table Name: "+parts[0].trim().toUpperCase());
                printWriter.println("Attributes: "+parts[1].trim().toUpperCase());
                BufferedReader bufferedReader1 = new BufferedReader(new FileReader(new File("src/main/java/FileStorage/Database/" + DATABASE_NAME.trim().toUpperCase() + "_" + parts[0].trim().toUpperCase() + ".txt")));
                int countRows=0;
                while ((bufferedReader1.readLine()) != null) {
                    countRows++;
                }
                printWriter.println("Number of rows: "+countRows);
                printWriter.println();
            }
            printWriter.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
