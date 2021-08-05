package Query;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static Basic.FeatureMenu.DATABASE_NAME;
import static Basic.LogGenerator.logQueryExecute;
import static Basic.Message.display;
import static Query.QueryParser.*;

public class CheckQuery {
    static boolean useDatabase = false;
    static Lock lock = new ReentrantLock();

    public static void checkType(String query) throws IOException, InterruptedException {
        String temp = query.toLowerCase();
        String[] divideQuery = temp.split(" ");
        switch (divideQuery[0]) {
            case "select":
                acquireLocks(lock, "select");
                if (useDatabase) {
                    if (selectQueryParser(query)) {
                    } else {
                        display("Invalid Query !!");
                    }
                } else {
                    display("Please select database first!!");
                    logQueryExecute(query,"Please select database first!!");
                }
                releaseLock(lock, "select");
                break;
            case "create":
                //acquireLocks(lock, "create");
                if(divideQuery[1].equals("database")){
                    if (!CreateSchemaParser(query)) {
                        display("Invalid Query !!");
                        logQueryExecute(query,"Invalid Query !!");
                    }
                }else{
                    if(useDatabase){
                        CreateParser(query);
                    }else{
                        display("Please select database first!!");
                        logQueryExecute(query,"Please select database first!!");
                    }
                }
                //releaseLock(lock, "create");
                break;
            case "use":
                DATABASE_NAME = UseDatabase(query);
                if (DATABASE_NAME == null) {
                    display("PLease create database first!! As no such database exist in the system.");
                    logQueryExecute(query,"PLease create database first!! As no such database exist in the system.");
                } else {
                    display("You are working on database: " + DATABASE_NAME.toUpperCase());
                    logQueryExecute(query,"You are working on database: " + DATABASE_NAME.toUpperCase());
                    useDatabase = true;
                }
                break;
            case "insert":
                acquireLocks(lock, "insert");
                if (useDatabase) {
                    InsertParser(query);
                } else {
                    display("Please select database first!!");
                    logQueryExecute(query,"Please select database first!!");
                }
                releaseLock(lock, "insert");
                break;
            case "update":
                acquireLocks(lock, "update");
                if (useDatabase) {
                    System.out.println("UPDATE : " + divideQuery[0]);
                    UpdateParser(query);
                } else {
                    display("Please select database first!!");
                    logQueryExecute(query,"Please select database first!!");
                }
                releaseLock(lock, "update");
                break;
            case "drop":
                acquireLocks(lock, "drop");
                if (useDatabase) {
                    System.out.println("DROP : " + divideQuery[0]);
                } else {
                    display("Please select database first!!");
                    logQueryExecute(query,"Please select database first!!");
                }
                releaseLock(lock, "drop");
                break;
            case "delete":
                acquireLocks(lock, "delete");
                if (useDatabase) {
                    System.out.println("DELETE : " + divideQuery[0]);
                    DeleteParser(query);
                } else {
                    display("Please select database first!!");
                    logQueryExecute(query,"Please select database first!!");
                }
                releaseLock(lock, "delete");
                break;
            case "alter":
                acquireLocks(lock, "alter");
                if (useDatabase) {
                    System.out.println("Alter : " + divideQuery[0]);
                    AlterParser(query);
                } else {
                    display("Please select database first!!");
                    logQueryExecute(query,"Please select database first!!");
                }
                releaseLock(lock, "alter");
                break;
            default:
                display("Invalid Query !!");
                logQueryExecute(query,"Invalid Query !!");
        }
    }
}
