package Query;

import static Basic.FeatureMenu.DATABASE_NAME;
import static Query.QueryParser.*;

public class CheckQuery {
    static boolean useDatabase = false;

    public static void checkType(String query) {
        String temp = query.toLowerCase();
        String[] divideQuery = temp.split(" ");
        switch (divideQuery[0]) {
            case "select":
                if (useDatabase) {
                    System.out.println(divideQuery[0]);
                } else {
                    errorMessage("Please select database first!!");
                }
                break;
            case "create":
                if (useDatabase) {
                    System.out.println(divideQuery[0]);
                    CreateParser(query);
                } else {
                    if (divideQuery[1].equals("database")) {
                        if (!CreateSchemaParser(query)) {
                            System.out.println("Invalid Query !!");
                        }
                    } else {
                        errorMessage("Please select database first!!");
                    }
                }
                break;
            case "use":
                DATABASE_NAME = UseDatabase(query);
                if (DATABASE_NAME == null) {
                    System.out.println("PLease create database first!! As no such database exist in the system.");
                } else {
                    System.out.println("You can work on database: " + DATABASE_NAME.toUpperCase());
                    useDatabase = true;
                }
                break;
            case "insert":
                if (useDatabase) {
                    System.out.println("db " + DATABASE_NAME);
                    //InsertParser(query, dbName);
                    System.out.println(divideQuery[0]);
                } else {
                    errorMessage("Please select database first!!");
                }
                break;
            case "update":
                if (useDatabase) {
                    System.out.println("UPDATE : " + divideQuery[0]);
                } else {
                    errorMessage("Please select database first!!");
                }
                break;
            case "drop":
                if (useDatabase) {
                    System.out.println("DROP : " + divideQuery[0]);
                } else {
                    errorMessage("Please select database first!!");
                }
                break;
            case "delete":
                if (useDatabase) {
                    System.out.println("DELETE : " + divideQuery[0]);
                } else {
                    errorMessage("Please select database first!!");
                }
                break;
            default:
                System.out.println("Invalid Query !!");
        }
    }

    public static void errorMessage(String message) {
        System.out.println(message);
    }
}
