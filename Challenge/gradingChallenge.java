public class gradingChallenge {
    int gradeScore = 93;
    public static void main(String[] args) {
        gradingChallenge grade = new gradingChallenge();

        if (grade.gradeScore > 90) {
            System.out.println("You got A");
        } else if (grade.gradeScore > 80) {
            System.out.println("You got B");
        }
                 else if (grade.gradeScore > 80) {
            System.out.println("You got C");
        }
                 else if (grade.gradeScore > 70) {
            System.out.println("You got D");
        }
                 else if (grade.gradeScore < 60) {
            System.out.println("You got F");
        }
    }
}