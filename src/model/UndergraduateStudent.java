package model;

public class UndergraduateStudent extends Student {

    private static final double BASE_TUITION = 5000.00;
    private static final double ACTIVITY_FEE = 250.00;
    private static final double TECHNOLOGY_FEE = 150.00;

    private int yearLevel;
    private String major;

    public UndergraduateStudent(String name, String email, int age,
                                 String studentId, String department,
                                 int yearLevel, String major) {
        super(name, email, age, studentId, department, "UNDERGRADUATE");
        this.yearLevel = yearLevel;
        this.major = major;
    }

    @Override
    public double calculateTuition() {
        double total = BASE_TUITION + ACTIVITY_FEE + TECHNOLOGY_FEE;

        if (yearLevel == 4) {
            total -= 200.00;
        }

        return total;
    }

    public String getYearLevelName() {
        switch (yearLevel) {
            case 1: return "Freshman";
            case 2: return "Sophomore";
            case 3: return "Junior";
            case 4: return "Senior";
            default: return "Unknown";
        }
    }

    public int getYearLevel() { return yearLevel; }
    public String getMajor() { return major; }

    public void setYearLevel(int yearLevel) {
        if (yearLevel < 1 || yearLevel > 4) {
            throw new IllegalArgumentException("Year level must be 1-4");
        }
        this.yearLevel = yearLevel;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @Override
    public String toString() {
        return super.toString() +
               String.format(" | Year: %s | Major: %s | Tuition: $%.2f",
                       getYearLevelName(), major, calculateTuition());
    }
}

