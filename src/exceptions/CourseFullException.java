package exceptions;

public class CourseFullException extends Exception {
    
    private String courseId;
    private String courseName;
    private int maxCapacity;

    public CourseFullException(String courseId, String courseName, int maxCapacity) {
        super(String.format(
            "Course '%s' (%s) is FULL! Maximum capacity of %d students reached. " +
            "Please try another section or add yourself to the waitlist.",
            courseName, courseId, maxCapacity
        ));
        this.courseId = courseId;
        this.courseName = courseName;
        this.maxCapacity = maxCapacity;
    }

    public CourseFullException(String message) {
        super(message);
    }

    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public int getMaxCapacity() { return maxCapacity;
    }
    
}
