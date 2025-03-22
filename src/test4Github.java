// For experimenting with Git & Github since they're finicky

public class test4Github {
    private final int hi;
    private final int bye;

    public test4Github(int hi) {
        this.hi = hi;
        this.bye = 0;
    }

    public test4Github(int hi, int bye) {
        this.hi = hi;
        this.bye = bye;
    }

    public void print() {
        System.out.println(hi);
    }

    // Get name of current employee and new employee
    // Get description of order
    // Do desc = desc.replace(old Employee name, new Employee name)
    public static void testString() {
        String s = "Mohammed sent microphones to Amr, handled by Onika";
        String n = "Lizzie";
        s = s.replace("Onika", n);
        System.out.println(s);
    }

    public static void main(String[] args) {
        testString();
    }

}
