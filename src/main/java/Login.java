import java.io.*;
import java.util.Scanner;

public class Login {
    private String username;
    private String password;
    private String rePassword;
    private Scanner scanner;
    File file;

    public Login() {
        scanner = new Scanner(System.in);
        System.out.println("-------- Welcome ---------");
        file = new File("src/main/java/Files/Authentication/LoginInfo.txt");
    }


    void display() throws IOException {

        System.out.println("Choose");
        System.out.println("1. Login\n2. Register\n3. Exit");
        String choose = scanner.nextLine();
        operation(choose);
    }

    void operation(String choose) throws IOException {
        switch (choose) {
            case "1":
                System.out.println("Enter your username");
                username = scanner.nextLine();
                System.out.println("Enter your password");
                password = scanner.nextLine();
                readLoginInfo(username, password);
                break;
            case "2":
                System.out.println("Username: ");
                username = scanner.nextLine();
                System.out.println("Password: ");
                password = scanner.nextLine();
                String passwordRegex="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
                if(password.matches(passwordRegex)){
                    System.out.println("Re-conform password: ");
                    rePassword = scanner.nextLine();
                    if (rePassword.equals(password)) {
                        System.out.println("Registered Successfully");
                        writeLoginInfo(username,password);
                    } else {
                        System.out.println("Both password did not matched");
                        System.out.println("Try Again");
                        display();
                    }
                }else{
                    System.out.println("Password must have: \n--> 8-characters\n--> At least 1 letter\n--> At least 1 number \n--> At least 1 special character");
                    System.out.println("Try again (Yes/No)");
                    String temp = scanner.nextLine();
                    if(temp.equals("Yes") || temp.equals("yes")){
                        display();
                    }
                }
                break;
        }
    }

    void readLoginInfo(String username, String password) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line;
        Boolean check = false;
        while ((line = bufferedReader.readLine()) != null) {
            String[] parts = line.split(" \\|\\| ");
            for (int i = 0; i < parts.length; i++) {
                try {
                    if (username.equals(parts[i].trim()) && password.equals(parts[i + 1].trim())) {
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
            System.out.println("Invalid Credentials or not registered");
            display();
        }
    }

    void writeLoginInfo(String username, String password) throws IOException {
        PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        printWriter.println(username + " || "+ password);
        printWriter.close();
        callMenu(username);
    }

    void callMenu(String username){
        FeatureMenu featureMenu = new FeatureMenu();
        featureMenu.menu(username);
    }
}
