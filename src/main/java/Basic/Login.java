package Basic;

import java.io.*;
import java.util.Scanner;

public class Login {
    File file;
    private String username;
    private String password;
    private String rePassword;
    private String answer;
    private Scanner scanner;

    public Login() {
        scanner = new Scanner(System.in);
        System.out.println("----------- WELCOME -----------");
        file = new File("src/main/java/Files/Authentication/LoginInfo.txt");
    }


    void display() throws IOException {
        System.out.println("----------- CHOOSE -----------");
        System.out.println("1. Login\n2. Register\n3. Exit");
        System.out.print("Enter your Choice: ");
        String choose = scanner.nextLine();
        operation(choose);
    }

    void operation(String choose) throws IOException {
        switch (choose) {
            case "1":
                System.out.print("Enter your username: ");
                username = scanner.nextLine();
                System.out.print("Enter your password: ");
                password = scanner.nextLine();
                System.out.print("What is favourite car? ");
                answer = scanner.nextLine();
                readLoginInfo(username, password, answer);
                break;
            case "2":
                do{
                    System.out.print("Username: ");
                    username = scanner.nextLine();
                    if(checkUser(username)){
                        System.out.println("User already Exist!! PLease Basic.Login...\n");
                        display();
                    }
                }while (checkUser(username));
                String temp = "";
                do {
                    System.out.print("Password: ");
                    password = scanner.nextLine();
                    String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
                    if (password.matches(passwordRegex)) {
                        System.out.print("Re-conform password: ");
                        rePassword = scanner.nextLine();
                        if (rePassword.equals(password)) {
                            System.out.print("What is favourite car? ");
                            answer = scanner.nextLine();
                            System.out.println("Registered Successfully");
                            writeLoginInfo(username, password, answer);
                        } else {
                            System.out.println("Both password did not matched");
                            System.out.println("Try Again..");
                            display();
                        }
                    } else {
                        System.out.println("---Password must have--- \n--> 8-characters.\n--> At least 1 letter.\n--> At least 1 number. \n--> At least 1 special character.");
                        System.out.print("Try again (Yes/No)? ");
                        temp = scanner.nextLine();
                    }
                } while ((temp.equals("Yes") || temp.equals("yes")));
                break;
            case "3":
                System.exit(0);
                break;
        }
    }

    void readLoginInfo(String username, String password, String answer) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line;
        Boolean check = false;
        while ((line = bufferedReader.readLine()) != null) {
            String[] parts = line.split(" \\|\\| ");
            for (int i = 0; i < parts.length; i++) {
                try {
                    if (username.equals(parts[i].trim()) && password.equals(parts[i + 1].trim()) && answer.equals(parts[i + 2].trim())) {
                        check = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (check) {
            System.out.println("Login Successfully");
            callMenu(username);
        } else {
            System.out.println("\n Invalid Credentials or not registered \n");
            display();
        }
    }

    void writeLoginInfo(String username, String password, String answer) throws IOException {
        PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        printWriter.println(username + " || " + password + " || " + answer);
        printWriter.close();
        callMenu(username);
    }

    Boolean checkUser(String username) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] parts = line.split(" \\|\\| ");
            for (int i = 0; i < parts.length; i++) {
                try {
                    if (username.equals(parts[i].trim())) {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    void callMenu(String username) throws IOException {
        FeatureMenu featureMenu = new FeatureMenu();
        featureMenu.menu(username,null);
    }
}
