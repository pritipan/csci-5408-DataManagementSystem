package Basic;

import java.io.*;
import java.util.Scanner;

import static Basic.DatabaseState.generateDbState;
import static Basic.Erd.generateERD;
import static Basic.Login.USERNAME;
import static Basic.SqlDump.generateDump;
import static Query.CheckQuery.checkType;


public class FeatureMenu {
    private static final Scanner scanner = new Scanner(System.in);
    public static String DATABASE_NAME;
    public static boolean CONCURRENT_FLAG = false;
    private static boolean flag = true;

    public static void menu() throws IOException, InterruptedException {
        do {
            if (USERNAME != null) {
                System.out.println("----------- Choose from one of the operations -----------");
                System.out.println("1. Query to Execute");
                System.out.println("2. SQL Dump");
                System.out.println("3. Generate ERD");
                System.out.println("4. Generate Database state");
                System.out.println("5. Concurrent transaction");
                System.out.println("6. Exit");
                System.out.print("Enter: ");
                String userInput = scanner.nextLine();
                switch (userInput) {
                    case "1":
                        System.out.print("Enter a SQL query: ");
                        String sqlQuery = scanner.nextLine();
                        checkType(sqlQuery);
                        break;
                    case "2":
                        if (DATABASE_NAME == null) {
                            System.out.println("Please select database first!!");
                        } else {
                            generateDump();
                        }
                        break;
                    case "3":
                        if (DATABASE_NAME == null) {
                            System.out.println("Please select database first!!");
                        } else {
                            generateERD();
                        }
                        break;
                    case "4":
                        if (DATABASE_NAME == null) {
                            System.out.println("Please select database first!!");
                        } else {
                            generateDbState();
                        }
                        break;
                    case "5":
                        if (DATABASE_NAME == null) {
                            System.out.println("Please select database first!!");
                        } else {
                            CONCURRENT_FLAG = true;
                            String query;
                            String moreChoice;
                            String tableName;
                            System.out.println("Enter table Name:");
                            tableName = scanner.nextLine();
                            duplicateFile(tableName);
                            System.out.println("STARTED TRANSACTION");
                            do {
                                System.out.println("Enter query: ");
                                query = scanner.nextLine();
                                checkType(query);
                                System.out.println("Do you want enter more query?(Yes/No)");
                                moreChoice = scanner.nextLine();
                                if (moreChoice.equalsIgnoreCase("No")) {
                                    System.out.println("TRANSACTION ENDS");
                                    System.out.println("Do you want Commit the changes?(Yes/No)");
                                    String commit = scanner.nextLine();
                                    if (commit.equalsIgnoreCase("Yes")) {
                                        rewriteFile(tableName, commit);
                                        System.out.println("Transaction is Committed");
                                    } else {
                                        rewriteFile(tableName, commit);
                                        System.out.println("Transaction is not Committed");
                                    }
                                    CONCURRENT_FLAG = false;
                                }
                            } while (moreChoice.equalsIgnoreCase("Yes"));
                        }
                        break;
                    case "6":
                        flag = false;
                        System.exit(0);
                        break;
                }
            }
        } while (flag);
    }

    static void duplicateFile(String tableName) {
        try {
            BufferedReader bufferedReader1 = new BufferedReader(new FileReader(new File("src/main/java/FileStorage/Database/" + DATABASE_NAME.trim().toUpperCase() + "_" + tableName + ".txt")));
            String line;
            PrintWriter printWriter;
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter("src/main/java/FileStorage/Database/TEMP_" + DATABASE_NAME.trim().toUpperCase() + "_" + tableName + ".txt")));
            while ((line = bufferedReader1.readLine()) != null) {
                printWriter.println(line);
            }
            printWriter.close();
        } catch (Exception e) {

        }
    }

    static void rewriteFile(String tableName, String commit) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("src/main/java/FileStorage/Database/TEMP_" + DATABASE_NAME.trim().toUpperCase() + "_" + tableName + ".txt")));
            String line;
            PrintWriter printWriter;
            if (commit.equalsIgnoreCase("yes")) {
                printWriter = new PrintWriter(new BufferedWriter(new FileWriter("src/main/java/FileStorage/Database/" + DATABASE_NAME.trim().toUpperCase() + "_" + tableName + ".txt")));
                while ((line = bufferedReader.readLine()) != null) {
                    printWriter.println(line);
                }
                printWriter.close();
            }
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter("src/main/java/FileStorage/Database/TEMP_" + DATABASE_NAME.trim().toUpperCase() + "_" + tableName + ".txt")));
            printWriter.print("");
            printWriter.close();
        } catch (Exception e) {

        }
    }
}
