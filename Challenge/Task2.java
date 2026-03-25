public class Task2 {
    byte zero = 0;
    short e = 3;
    int l = 1;
    float ver = 2.0f;
    boolean bol = true;
    char  hello = 'H';
    char r ='r';
    char d = 'd';
    String output = "W" + zero + "w";

    public static void main(String[] args) {
        Task2 task = new Task2();
        System.out.println(
            "" + task.hello
            + task.e
            + task.l
            + task.l
            + task.zero
            + " "
            + task.output.charAt(2)
            + task.output.charAt(1)
            + task.r
            + task.l
            + task.d
            + " "
            + task.ver
            + " "
            + task.bol
        );
    }

}
