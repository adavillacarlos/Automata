import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int choice = 0;
        int ans=0;
        String valid = null;
        do{
            clearScreen();
            menu();
            System.out.print("Enter a number: ");
            ans = scan.nextInt();
            scan.nextLine();

            if(ans==7)
                break;

            System.out.print("Input (Please add spaces except for the semicolon): ");
            String statement = scan.nextLine();

            switch (ans) {
                case 1 -> valid = (isIdentifier(statement)) ? "Is Valid" : "Not Valid";
                case 2 -> valid = (isInteger(statement)) ? "Is Valid" : "Not Valid";
                case 3 -> valid = (isFloatReal(statement)) ? "Is Valid" : "Not Valid";
                case 4 -> valid = (isAssignment(statement)) ? "Is Valid" : "Not Valid";
                case 5 -> valid = (isAssignmentDFA(statement)) ? "Is Valid" : "Not Valid";
                case 6 -> valid = (isExpandedAssignment(statement)) ? "Is Valid" : "Not Valid";
            }

            System.out.println("Output: " + valid);

            //Press any key to continue
            System.out.print("Do you want to continue? (1 for yes or 0 for no): ");
            choice = scan.nextInt();
            scan.nextLine();


        } while (choice != 0);

    }

    public static void menu(){
        System.out.println("1. Check if it is an Identifier");
        System.out.println("2. Check if it is an Integer");
        System.out.println("3. Check if it is a Float or a Real Number");
        System.out.println("4. Check if it is a Valid Assignment");
        System.out.println("5. Check if it is a Valid Assignment using DFA");
        System.out.println("6. Check if it is an Valid Expanded Assignment using DFA");
        System.out.println("7. Exit");
    }

    public static void clearScreen() {
        // Clear Screen
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {}
    }

    public static boolean isIdentifier(String statement){
        int input=0;
        boolean isValid = false;
        int j=0, state=0;
        int[][] table = {
                {1, 2, 1 ,2},
                {1, 1, 1, 2},
                {2, 2, 2, 2,},
        };
//        statement = statement.replaceAll("\\s+","");
        char[] stateArray = statement.toCharArray();
        for(char c: stateArray){
            if(Character.isLetter(c))
                input = 0;
            else if(Character.isDigit(c))
                input = 1;
            else if(c == '_')
                input = 2;
            else
                input = 3;

            state = table[state][input];
        }

        if(state==1)
            isValid = true;

        return isValid;
    }

    public static boolean isInteger(String statement){
        int input=0;
        boolean isValid = false;
        int j=0, state=0;
        int[][] table = {
                {1, 2, 3},
                {3, 2, 3},
                {3, 2, 3},
                {3, 3, 3},
        };
//        statement = statement.replaceAll("\\s+","");
        char[] stateArray = statement.toCharArray();
        for(char c: stateArray){
            if(c == '+' || c == '-')
                input = 0;
            else if(Character.isDigit(c))
                input = 1;
            else
                input = 2;

            state = table[state][input];
        }

        if(state==2)
            isValid = true;

        return isValid;
    }


    public static boolean isFloatReal(String statement){
        int input=0;
        boolean isValid = false;
        int j=0, state=0;
        int[][] table = {
                {1, 3, 4, 5},
                {1, 5, 2, 5},
                {2, 5, 5, 5},
                {1, 5, 4, 5},
                {2, 5, 5, 5},
                {5, 5, 5, 5},
        };
        char[] stateArray = statement.toCharArray();
        for(char c: stateArray){
            if(Character.isDigit(c))
                input = 0;
            else if(c == '+' || c == '-')
                input = 1;
            else if (c == '.')
                input = 2;
            else
                input = 3;

            state = table[state][input];
        }

        if(state==1 || state == 2)
            isValid = true;

        return isValid;
    }

    // Version 2.1 (Not Using DFA)
    public static boolean isAssignment(String statement){
        boolean isValid = false;
        boolean hasSemiEquals = false;
        //Removing white spaces
        statement = statement.replaceAll("\\s+","");

        //Check if we have ";" or "=" on the string
        if((statement.contains("=") && statement.contains(";"))){
            hasSemiEquals = true;
        }

        if(hasSemiEquals){
            //Splitting variables and value
            String result1 = statement.substring(0, statement.indexOf("="));
            String result2 = statement.substring(statement.indexOf("=") + 1,statement.indexOf(";"));

            //Checking if the variables and value are valid
            isValid = isIdentifier(result1) && (isIdentifier(result2) || isFloatReal(result2) || isInteger(result2));
        }

        return isValid;
    }

    //Version 2.2 (Using DFA) (There should be spaces)
    public static boolean isAssignmentDFA(String statement){
        boolean isValid =  false;
        boolean hasSemiEquals = false;
        int input =0, state=0;
        int[][] table ={
                {1, 5, 5, 5, 5, 5},
                {5, 5, 5, 2, 5, 5},
                {3, 3, 3, 5, 5, 5},
                {5, 5, 5, 5, 4, 5},
                {5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5}
        };

        if((statement.contains("=") && statement.contains(";"))){
            hasSemiEquals = true;
        }

        //Making Semicolon separate as spaces
        if(hasSemiEquals){
            statement = statement.substring(0,statement.indexOf(";"));
            statement += " ;";
            //System.out.println(statement);
        }

        //Splitting the statement according to spaces
        String[] split = statement.split("\\s+");

        for(String s: split){
            if(isIdentifier(s))
                input = 0;
            else if(isFloatReal(s))
                input = 1;
            else if(isInteger(s))
                input = 2;
            else if(s.equals("="))
                input = 3;
            else if(s.equals(";"))
                input = 4;
            else
                input = 5;
            state = table[state][input];
        }

        if(state == 4)
            isValid = true;

        return isValid;

    }


    //Version 3
    //is Expanded Assignment using DFA. There should be spaces
    public static boolean isExpandedAssignment(String statement){
        boolean isValid =  false;
        boolean hasSemiEquals = false;
        int input =0, state=0;
        int[][] table ={
                {1, 7, 7, 7, 7, 7, 7},
                {7, 7, 7, 7, 2, 7, 7},
                {3, 3, 3, 7, 7, 7, 7},
                {7, 7, 7, 4, 7, 7, 7},
                {5, 5, 5, 7, 7, 7, 7},
                {7, 7, 7, 4, 7, 6, 7},
                {7, 7, 7, 7, 7, 7, 7},
                {7, 7, 7, 7, 7, 7, 7},
        };

        if((statement.contains("=") && statement.contains(";"))){
            hasSemiEquals = true;
        }

        //Making Semicolon separate as spaces
        if(hasSemiEquals){
            statement = statement.substring(0,statement.indexOf(";"));
            statement += " ;";
        }

        //Splitting the statement according to spaces
        String[] split = statement.split("\\s+");

        for(String s: split){
            if(isIdentifier((s)))
                input = 0;
            else if (isFloatReal(s))
                input = 1;
            else if (isInteger((s)))
                input = 2;
            else if(s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/"))
                input = 3;
            else if(s.equals("="))
                input = 4;
            else if(s.equals(";"))
                input = 5;
            else
                input = 6;
            state = table[state][input];
        }

        if(state == 6)
            isValid = true;

        return isValid;
    }

}
