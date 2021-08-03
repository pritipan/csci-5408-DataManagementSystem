package Basic;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static Basic.Login.USERNAME;

public class LogGenerator {

    public static void logQueryExecute(String query, String message){
        File file = new File("src/main/java/FileStorage/logs.txt");
        PrintWriter printWriter;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
            printWriter.println("Executed by: "+USERNAME);
            printWriter.println("Action: "+query);
            printWriter.println("Message: "+message);
            printWriter.println("Time: "+formatter.format(date));
            printWriter.println();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
