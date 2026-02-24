package exceptions;

public class StudentAlreadyEnrolledException extends Exception {

    private String studentId;
    private String studentName;
    private String courseId;
    private String courseName;

    public StudentAlreadyEnrolledException(String studentId, String studentName,
                                            String courseId, String courseName) {
        super(String.format(
            "Student '%s' (ID: %s) is ALREADY enrolled in '%s' (%s). " +
            "Duplicate enrollment is not permitted.",
            studentName, studentId, courseName, courseId
        ));
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseName = courseName;
    }

    public StudentAlreadyEnrolledException(String message) {
        super(message);
    }

    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    
}
