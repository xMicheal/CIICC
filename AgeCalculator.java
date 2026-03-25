import java.time.LocalDate;
import java.time.Period;

public class AgeCalculator {
    private LocalDate birthDate;
    
    public AgeCalculator(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    
    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    
    public boolean isBirthdayToday() {
        LocalDate today = LocalDate.now();
        return birthDate.getMonth() == today.getMonth() && 
               birthDate.getDayOfMonth() == today.getDayOfMonth();
    }
}
