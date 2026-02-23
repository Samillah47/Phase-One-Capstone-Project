package model;

public class GraduateStudent extends Student {

    private static final double COST_PER_CREDIT = 1500.00;   
    private static final double RESEARCH_FEE = 2000.00;      
    private static final double LAB_FEE = 500.00;    

    
    private String researchTopic;
    private String advisor;         
    private boolean isThesisTrack;  
    public GraduateStudent(String name, String email, int age,
                            String studentId, String department,
                            String researchTopic, String advisor,
                            boolean isThesisTrack) {
        super(name, email, age, studentId, department, "GRADUATE");
        this.researchTopic = researchTopic;
        this.advisor = advisor;
        this.isThesisTrack = isThesisTrack;
    }
    @Override
    public double calculateTuition() {
        int totalCredits = getEnrolledCourses().stream()
                .mapToInt(Course::getCredits)
                .sum();

        if (totalCredits == 0) {
            totalCredits = 9; 
        }

        double tuition = (totalCredits * COST_PER_CREDIT)
                       + RESEARCH_FEE
                       + LAB_FEE;

        if (isThesisTrack) {
            tuition += 1500.00; // Thesis supervision fee
        }

        return tuition;
    }

    public String getResearchTopic() { return researchTopic; }
    public String getAdvisor() { return advisor; }
    public boolean isThesisTrack() { return isThesisTrack; }

    public void setResearchTopic(String researchTopic) {
        this.researchTopic = researchTopic;
    }

    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }

    public void setThesisTrack(boolean thesisTrack) {
        this.isThesisTrack = thesisTrack;
    }

    @Override
    public String toString() {
        return super.toString() +
               String.format(" | Research: %s | Advisor: %s | Thesis: %s | Tuition: $%.2f",
                       researchTopic, advisor,
                       isThesisTrack ? "Yes" : "No",
                       calculateTuition());
    }
}
