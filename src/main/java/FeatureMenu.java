import java.util.Scanner;

public class FeatureMenu {
    private Scanner scanner = new Scanner(System.in);
    void menu(String username){
        System.out.println("-----------------------------------------");
        System.out.println ("*** WELCOME TO DATABASE MANAGEMENT SYSTEM ***");
        System.out.println ("----------------------------------------");

        if(username!=null){
            System.out.println ("Choose from one of the operations");
            System.out.println ("1. Enter Query");
            System.out.println ("2. SQL Dump");
            System.out.println ("3. Generate ERD");
            System.out.println ("4. Generate data dictionary");
            String userInput = scanner.nextLine ();
            switch (userInput) {
                case "1":
                    System.out.println("Enter a SQL query to proceed or exit; to end!");
                    String sqlQuery = scanner.nextLine();
                    break;
                case "2":

                    break;
                case "3":

                    break;
                case "4":

                    break;
                default:
                    System.out.println("Invalid input!");
            }
        }
    }
}
