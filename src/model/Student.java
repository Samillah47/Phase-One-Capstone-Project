package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Student extends Person {

    private String studentId;
    private double gpa;
    private String department;
    private String studentType;

    private Map<Course, Double> courseGrades;

    public Student(String name, String email, int age,
                   String studentId, String department, String studentType) {
        super(name, email, age);
        this.studentId = studentId;
        this.department = department;
        this.studentType = studentType;
        this.gpa = 0.0;
        this.courseGrades = new HashMap<>();
    }

    public void addCourse(Course course) {
        if (!courseGrades.containsKey(course)) {
            courseGrades.put(course, 0.0);
        }
    }

    public void updateGrade(Course course, double grade) {
        if (grade < 0.0 || grade > 4.0) {
            throw new IllegalArgumentException(
                "Grade must be between 0.0 and 4.0, got: " + grade);
        }
        if (!courseGrades.containsKey(course)) {
            throw new IllegalArgumentException(
                "Student not enrolled in: " + course.getCourseName());
        }
        courseGrades.put(course, grade);
        recalculateGPA();
    }

    public void removeCourse(Course course) {
        courseGrades.remove(course);
        recalculateGPA();
    }

    private void recalculateGPA() {
        if (courseGrades.isEmpty()) {
            this.gpa = 0.0;
            return;
        }

        double total = 0.0;
        int count = 0;

        for (Double grade : courseGrades.values()) {
            if (grade > 0.0) {
                total += grade;
                count++;
            }
        }

        this.gpa = (count > 0) ? (total / count) : 0.0;
    }

    public boolean isEnrolledIn(Course course) {
        return courseGrades.containsKey(course);
    }

    public Set<Course> getEnrolledCourses() {
        return courseGrades.keySet();
    }

    public double getGradeForCourse(Course course) {
        return courseGrades.getOrDefault(course, -1.0);
    }

    public Map<Course, Double> getCourseGrades() {
        return new HashMap<>(courseGrades);
    }

    public int getCourseCount() {
        return courseGrades.size();
    }

    public abstract double calculateTuition();

    public String getStudentId() { return studentId; }
    public double getGpa() { return gpa; }
    public String getDepartment() { return department; }
    public String getStudentType() { return studentType; }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String getRole() {
        return studentType + " Student";
    }

    @Override
    public String toString() {
        return String.format("Student[%s] %s | Dept: %s | GPA: %.2f | Courses: %d | Type: %s",
                studentId, getName(), department, gpa,
                courseGrades.size(), studentType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Student)) return false;
        Student other = (Student) obj;
        return this.studentId.equals(other.studentId);
    }

    @Override
    public int hashCode() {
        return studentId.hashCode();
    }
}
