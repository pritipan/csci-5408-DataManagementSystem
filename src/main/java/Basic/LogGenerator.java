package Basic;

import java.io.*;

public class LogGenerator {

    public static void logQueryExecute(String query, String message){
        File file = new File("src/main/java/Files/logs.txt");
        PrintWriter printWriter;
        try {
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            printWriter.println();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
