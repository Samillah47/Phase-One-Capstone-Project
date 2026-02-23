package model;

import java.util.ArrayList;
import java.util.List;

public class Instructor extends Person {

    private String instructorId;
    private String department;
    private String title;         
    private double salary;

    /**
     * List of courses this instructor is assigned to teach.
     * One instructor can teach MANY courses.
     */
    private List<Course> assignedCourses;

    public Instructor(String name, String email, int age,
                      String instructorId, String department, String title) {
        super(name, email, age); 
        this.instructorId = instructorId;
        this.department = department;
        this.title = title;
        this.salary = 0.0;
        this.assignedCourses = new ArrayList<>();
    }

    public void assignCourse(Course course) {
        if (!assignedCourses.contains(course)) {
            assignedCourses.add(course);
        }
    }

    public void removeCourse(Course course) {
        assignedCourses.remove(course);
    }

    public int getCourseLoad() {
        return assignedCourses.size();
    }

    public String getInstructorId() { return instructorId; }
    public String getDepartment() { return department; }
    public String getTitle() { return title; }
    public double getSalary() { return salary; }
    public List<Course> getAssignedCourses() {
        return new ArrayList<>(assignedCourses);
    }
    public void setTitle(String title) { this.title = title; }
    public void setSalary(double salary) {
        if (salary < 0) throw new IllegalArgumentException("Salary cannot be negative");
        this.salary = salary;
    }
    
    @Override
    public String getRole() {
        return title + " - Instructor";
    }

    @Override
    public String toString() {
        return String.format("Instructor[%s] %s %s | Dept: %s | Courses: %d",
                instructorId, title, getName(), department, assignedCourses.size());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Instructor)) return false;
        Instructor other = (Instructor) obj;
        return this.instructorId.equals(other.instructorId);
    }

    @Override
    public int hashCode() {
        return instructorId.hashCode();
    }
}
