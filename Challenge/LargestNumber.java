import java.util.Scanner;

public class LargestNumber {
    public static void main(String[] args) {
        Scanner number1 = new Scanner(System.in);
        System.out.print("Enter the first number: ");
        int num1 = number1.nextInt();

        Scanner number2 = new Scanner(System.in);
        System.out.print("Enter the second number: ");
        int num2 = number2.nextInt();
        
        Scanner number3 = new Scanner(System.in);
        System.out.print("Enter the third number: ");
        int num3 = number3.nextInt();

        int largest = findLargest(num1, num2, num3);
        System.out.println("The largest number is: " + largest);
    }

    public static int findLargest(int a, int b, int c) {
        if (a >= b && a >= c) {
            return a;
        } else if (b >= a && b >= c) {
            return b;
        } else {
            return c;
        }
    }
}
