package Basic;

import java.util.Scanner;

import static Query.CheckQuery.checkType;

public class FeatureMenu {
    private static final Scanner scanner = new Scanner(System.in);
    public static String DATABASE_NAME;
    private static boolean flag = true;

    public static void main(String[] arg) {
        System.out.println("\n----------- WELCOME TO DATABASE MANAGEMENT SYSTEM -----------\n");
        menu("Test");
    }

    public static void menu(String username) {
        do {
            if (username != null) {
                System.out.println("----------- Choose from one of the operations -----------");
                System.out.println("1. Enter Query");
                System.out.println("2. SQL Dump");
                System.out.println("3. Generate ERD");
                System.out.println("4. Generate data dictionary");
                System.out.println("5. Exit");
                System.out.print("Enter: ");
                String userInput = scanner.nextLine();
                switch (userInput) {
                    case "1":
                        System.out.print("Enter a SQL query: ");
                        String sqlQuery = scanner.nextLine();
                        checkType(sqlQuery);
                        break;
                    case "2":
                        System.out.println(" DUMP of DB : " + DATABASE_NAME);
                        break;
                    case "3":
                        System.out.println(" ERD of DB : " + DATABASE_NAME);
                        break;
                    case "4":
                        System.out.println(" STATE of DB : " + DATABASE_NAME);
                        break;
                    case "5":
                        flag = false;
                        System.exit(0);
                        break;
                }
            }
        } while (flag);
    }
}
