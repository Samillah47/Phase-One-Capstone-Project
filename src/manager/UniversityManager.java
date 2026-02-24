package manager;

import java.util.*;
import java.util.stream.Collectors;

import exceptions.CourseFullException;
import exceptions.StudentAlreadyEnrolledException;
import model.Course;
import model.GraduateStudent;
import model.Instructor;
import model.Student;
import model.UndergraduateStudent;

public class UniversityManager {

    private List<Student> students;
    private Map<String, Course> courses;   
    private List<Instructor> instructors;

    private int studentIdCounter;
    private int instructorIdCounter;

    public UniversityManager() {
        this.students = new ArrayList<>();
        this.courses = new HashMap<>();
        this.instructors = new ArrayList<>();
        this.studentIdCounter = 1000; 
        this.instructorIdCounter = 100;
    }

    public UndergraduateStudent registerUndergraduateStudent(
            String name, String email, int age,
            String department, int yearLevel, String major) {

        
        String studentId = "STU" + studentIdCounter++;

        UndergraduateStudent student = new UndergraduateStudent(
                name, email, age, studentId, department, yearLevel, major);

        students.add(student);

        System.out.println("SUCCESS: Registered undergraduate student: "
                + name + " (ID: " + studentId + ")");
        return student;
    }
    
    public GraduateStudent registerGraduateStudent(
            String name, String email, int age,
            String department, String researchTopic,
            String advisor, boolean isThesisTrack) {

        String studentId = "STU" + studentIdCounter++;

        GraduateStudent student = new GraduateStudent(
                name, email, age, studentId, department,
                researchTopic, advisor, isThesisTrack);

        students.add(student);

        System.out.println("SUCCESS: Registered graduate student: "
                + name + " (ID: " + studentId + ")");
        return student;
    }

    public Optional<Student> findStudentById(String studentId) {
        return students.stream()
                .filter(s -> s.getStudentId().equalsIgnoreCase(studentId))
                .findFirst(); 
    }

    public List<Student> findStudentsByName(String name) {
        return students.stream()
                .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Course createCourse(String courseId, String courseName,
                                String department, int credits,
                                int maxCapacity, String instructorName) {


        if (courses.containsKey(courseId)) {
            throw new IllegalArgumentException(
                "Course with ID '" + courseId + "' already exists!");
        }

        Course course = new Course(courseId, courseName, department,
                                   credits, maxCapacity, instructorName);
        courses.put(courseId, course);

        System.out.println("SUCCESS: Created course: " + courseName
                + " (" + courseId + ")");
        return course;
    }

    public Optional<Course> findCourseById(String courseId) {
        Course course = courses.get(courseId); // Direct map lookup
        return Optional.ofNullable(course);    // Wrap in Optional
    }

    public void enrollStudentInCourse(String studentId, String courseId)
            throws CourseFullException, StudentAlreadyEnrolledException {

        Student student = findStudentById(studentId)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Student not found with ID: " + studentId));

        Course course = findCourseById(courseId)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Course not found with ID: " + courseId));

        if (student.isEnrolledIn(course)) {
            throw new StudentAlreadyEnrolledException(
                    studentId, student.getName(), courseId, course.getCourseName());
        }
        if (!course.hasSpace()) {
            throw new CourseFullException(
                    courseId, course.getCourseName(), course.getMaxCapacity());
        }

        course.addStudent(student);
        student.addCourse(course);

        System.out.println("SUCCESS: " + student.getName()
                + " enrolled in " + course.getCourseName());
    }

    public void updateStudentGrade(String studentId, String courseId, double grade) {
        Student student = findStudentById(studentId)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Student not found: " + studentId));

        Course course = findCourseById(courseId)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Course not found: " + courseId));

        student.updateGrade(course, grade);
        System.out.printf("SUCCESS: Updated grade for %s in %s: %.1f%n",
                student.getName(), course.getCourseName(), grade);
    }

    public List<Student> getAllStudents() {
        return Collections.unmodifiableList(students);
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }

    public List<Student> getDeansList() {
        return students.stream()
                .filter(s -> s.getGpa() > 3.5)        // Step 2: filter
                .sorted((s1, s2) -> Double.compare(    // Step 3: sort descending
                        s2.getGpa(), s1.getGpa()))
                .collect(Collectors.toList()); 
                          
    }

    public double getAverageGpaByDepartment(String department) {
        OptionalDouble average = students.stream()
                .filter(s -> s.getDepartment().equalsIgnoreCase(department))
                .filter(s -> s.getGpa() > 0.0) 
                .mapToDouble(Student::getGpa)   
                .average();                      

        return average.orElse(0.0); 
    }

    public Optional<Student> getTopPerformingStudent() {
        return students.stream()
                .filter(s -> s.getGpa() > 0.0) 
                .max(Comparator.comparingDouble(Student::getGpa)); 
    }

    public List<Student> getStudentsByDepartment(String department) {
        return students.stream()
                .filter(s -> s.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }

    public void printEnrollmentStatistics() {
        System.out.println("\n========== ENROLLMENT STATISTICS ==========");
        System.out.println("Total Students: " + students.size());
        System.out.println("Total Courses: " + courses.size());

        long undergrads = students.stream()
                .filter(s -> s instanceof UndergraduateStudent)
                .count();
        long grads = students.stream()
                .filter(s -> s instanceof GraduateStudent)
                .count();

        System.out.println("Undergraduates: " + undergrads);
        System.out.println("Graduates: " + grads);

        OptionalDouble avgGpa = students.stream()
                .filter(s -> s.getGpa() > 0.0)
                .mapToDouble(Student::getGpa)
                .average();

        System.out.printf("Overall Average GPA: %.2f%n",
                avgGpa.orElse(0.0));
        System.out.println("===========================================");
    }

    public void addStudentDirectly(Student student) {
        if (!students.contains(student)) {
            students.add(student);
            try {
                int idNum = Integer.parseInt(
                    student.getStudentId().replace("STU", ""));
                if (idNum >= studentIdCounter) {
                    studentIdCounter = idNum + 1;
                }
            } catch (NumberFormatException e) {
            }
        }
    }

    public void addCourseDirectly(Course course) {
        courses.put(course.getCourseId(), course);
    }

    public void clearAllData() {
        students.clear();
        courses.clear();
        instructors.clear();
        System.out.println("All data cleared.");
    }
}
