package Query;

import java.util.Locale;

public class CheckQuery {
    QueryParser queryParser = new QueryParser();
    boolean useDatabase = false;
    String dbName;

    public void errorMessage(String message) {
        System.out.println(message);
    }

    public boolean checkType(String query, String username) {
        String temp = query.toLowerCase(Locale.ROOT);
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
                    QueryParser.CreateParser(query, dbName);
                } else {
                    if (divideQuery[1].equals("database")) {
                        if (!QueryParser.CreateSchemaParser(query)) {
                            System.out.println("Invalid Query !!");
                        }
                    } else {
                        errorMessage("Please select database first!!");
                    }
                }
                break;
            case "use":
                dbName = queryParser.UseDatabase(query);
                if (dbName == null) {
                    System.out.println("PLease create database first!! As no such database exist in the system.");
                } else if (dbName == "Invalid") {
                    System.out.println("Invalid Query !!");
                } else {
                    System.out.println("You can work on database: " + dbName.toUpperCase(Locale.ROOT));
                    useDatabase = true;
                }
                break;
            case "insert":
                if (useDatabase) {
                    System.out.println(divideQuery[0]);
                } else {
                    errorMessage("Please select database first!!");
                }
                break;
            case "update":
                if (useDatabase) {
                    System.out.println(divideQuery[0]);
                } else {
                    errorMessage("Please select database first!!");
                }
                break;
            case "drop":
                if (useDatabase) {
                    System.out.println(divideQuery[0]);
                } else {
                    errorMessage("Please select database first!!");
                }
                break;
            case "delete":
                if (useDatabase) {
                    System.out.println(divideQuery[0]);
                } else {
                    errorMessage("Please select database first!!");
                }
                break;
            default:
                System.out.println("Invalid Query !!");
                return false;
        }
        return true;
    }


}
