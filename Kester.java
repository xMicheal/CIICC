import java.time.LocalDate;


public class Kester {
    String name = "Micheal Lance Kester Li";
    LocalDate birthDate = LocalDate.of(1998, 12, 27);
    LocalDate currentDate = LocalDate.now();

    public static void main(String[] args) {
        AgeCalculator calculator = new AgeCalculator(LocalDate.of(1998, 12, 27));
        int age = calculator.getAge();
        if (calculator.isBirthdayToday()) {
            System.out.println("Happy Birthday!" + age++);
        } else {
            System.out.println("Today is not your Birthday ):");
        }
        System.out.println("Age: " + age);
       
    }
}