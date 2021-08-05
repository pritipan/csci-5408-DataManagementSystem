package Basic;

import java.io.IOException;
import java.util.Scanner;

import static Basic.Login.USERNAME;
import static Basic.SqlDump.generateDump;
import static Query.CheckQuery.checkType;
import static Basic.Erd.generateERD;


public class FeatureMenu {
    private static final Scanner scanner = new Scanner(System.in);
    public static String DATABASE_NAME;
    private static boolean flag = true;

    public static void main(String[] arg) throws IOException, InterruptedException {
        System.out.println("\n----------- WELCOME TO DATABASE MANAGEMENT SYSTEM -----------\n");
        USERNAME="Foram";
        menu();
    }
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
                        if(DATABASE_NAME==null){
                            System.out.println("Please select database first!!");
                        }else{
                            generateDump();
                        }
                        break;
                    case "3":
                        System.out.println(" ERD of DB : " + DATABASE_NAME);
                        generateERD();
                        break;
                    case "4":
                        System.out.println(" STATE of DB : " + DATABASE_NAME);
                        break;
                    case "5":
                        System.out.println(" Concurrent transaction " + DATABASE_NAME);
                        break;
                    case "6":
                        flag = false;
                        System.exit(0);
                        break;
                }
            }
        } while (flag);
    }
}
