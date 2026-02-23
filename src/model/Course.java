package model;

import java.util.ArrayList;
import java.util.List;

public class Course {

    private String courseId;
    private String courseName;
    private String department;
    private int credits;
    private int maxCapacity;
    private String instructorName;

    private List<Student> enrolledStudents;

    public Course(String courseId, String courseName, String department,
                  int credits, int maxCapacity, String instructorName) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.department = department;
        this.credits = credits;
        this.maxCapacity = maxCapacity;
        this.instructorName = instructorName;
        this.enrolledStudents = new ArrayList<>();
    }

    public boolean addStudent(Student student) {
        if (enrolledStudents.contains(student)) {
            return false;
        }
        enrolledStudents.add(student);
        return true;
    }

    public boolean removeStudent(Student student) {
        return enrolledStudents.remove(student);
    }

    public boolean hasSpace() {
        return enrolledStudents.size() < maxCapacity;
    }

    public boolean isStudentEnrolled(Student student) {
        return enrolledStudents.contains(student);
    }

    public int getCurrentEnrollment() {
        return enrolledStudents.size();
    }

    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public String getDepartment() { return department; }
    public int getCredits() { return credits; }
    public int getMaxCapacity() { return maxCapacity; }
    public String getInstructorName() { return instructorName; }

    public List<Student> getEnrolledStudents() {
        return new ArrayList<>(enrolledStudents);
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public void setMaxCapacity(int maxCapacity) {
        if (maxCapacity < enrolledStudents.size()) {
            throw new IllegalArgumentException(
                "Cannot reduce capacity below current enrollment count");
        }
        this.maxCapacity = maxCapacity;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Dept: %s | Credits: %d | Enrolled: %d/%d | Instructor: %s",
                courseId, courseName, department, credits,
                enrolledStudents.size(), maxCapacity, instructorName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Course)) return false;
        Course other = (Course) obj;
        return this.courseId.equals(other.courseId);
    }

    @Override
    public int hashCode() {
        return courseId.hashCode();
    }
}
