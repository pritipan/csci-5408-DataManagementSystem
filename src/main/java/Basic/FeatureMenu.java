package Basic;

import Query.CheckQuery;

import java.io.IOException;
import java.util.Scanner;

public class FeatureMenu {
    private static Scanner scanner=new Scanner(System.in);
    private static CheckQuery checkQuery= new CheckQuery();
    private static String sqlQuery = "";
    private static boolean flag=true;

    public static void main(String[] arg) throws IOException {
        menu("Foram",null);
    }

    public FeatureMenu() {
        System.out.println("-----------------------------------------");
        System.out.println("----------- WELCOME TO DATABASE MANAGEMENT SYSTEM -----------");
        System.out.println("----------------------------------------");
    }

    public static void menu(String username, String dbName) throws IOException {
        do{
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
                        sqlQuery = scanner.nextLine();
                        checkQuery.checkType(sqlQuery,username);
                        break;
                    case "2":

                        break;
                    case "3":

                        break;
                    case "4":

                        break;
                    case "5":
                        flag=false;
                        System.exit(0);
                        break;
                }
            }
        }while(flag);
    }
}
