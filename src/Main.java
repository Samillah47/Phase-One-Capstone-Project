import manager.UniversityManager;
import model.Course;
import model.GraduateStudent;
import model.Student;
import model.UndergraduateStudent;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import exceptions.CourseFullException;
import exceptions.StudentAlreadyEnrolledException;
import manager.FileManager; 

public class Main {
    private static UniversityManager manager = new UniversityManager();
    private static FileManager fileManager = new FileManager();

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        printWelcomeBanner();
        fileManager.loadAllData(manager);
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:  registerStudentMenu();      break;
                case 2:  createCourseMenu();         break;
                case 3:  enrollStudentMenu();         break;
                case 4:  updateGradeMenu();           break;
                case 5:  viewStudentRecord();         break;
                case 6:  viewCourseRoster();          break;
                case 7:  generateDeansList();         break;
                case 8:  viewDepartmentStats();       break;
                case 9:  viewTopStudent();            break;
                case 10: viewAllStudents();           break;
                case 11: viewAllCourses();            break;
                case 12: viewTuitionInfo();           break;
                case 0:  running = saveAndExit();     break;
                default:
                    System.out.println("Invalid choice. Please enter 0-12.");
            }
        }
    }

    private static void printWelcomeBanner() {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║    UNIVERSITY MANAGEMENT SYSTEM v1.0         ║");
        System.out.println("║    Java OOP & Collections Capstone           ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println();
    }

    private static void printMainMenu() {
        System.out.println("\n═══════════════ MAIN MENU ═══════════════");
        System.out.println("  STUDENT MANAGEMENT");
        System.out.println("  1.  Register Student");
        System.out.println("  2.  Create Course");
        System.out.println("  3.  Enroll Student in Course");
        System.out.println("  4.  Update Student Grade");
        System.out.println();
        System.out.println("  VIEW & REPORTS");
        System.out.println("  5.  View Student Record");
        System.out.println("  6.  View Course Roster");
        System.out.println("  7.  Generate Dean's List (GPA > 3.5)");
        System.out.println("  8.  Department Statistics");
        System.out.println("  9.  Top Performing Student");
        System.out.println("  10. View All Students");
        System.out.println("  11. View All Courses");
        System.out.println("  12. View Tuition Information");
        System.out.println();
        System.out.println("  0.  Save and Exit");
        System.out.println("═════════════════════════════════════════");
    }

    private static void registerStudentMenu() {
        System.out.println("\n--- REGISTER STUDENT ---");
        System.out.println("Student Type:");
        System.out.println("  1. Undergraduate");
        System.out.println("  2. Graduate");
        int type = getIntInput("Select type: ");

        String name = getStringInput("Full Name: ");
        String email = getStringInput("Email: ");
        int age = getIntInput("Age: ");
        String department = getStringInput("Department: ");

        try {
            if (type == 1) {
                int yearLevel = getIntInput("Year Level (1=Freshman, 2=Sophomore, 3=Junior, 4=Senior): ");
                String major = getStringInput("Major: ");

                UndergraduateStudent student = manager.registerUndergraduateStudent(
                        name, email, age, department, yearLevel, major);

                System.out.println("\nStudent registered successfully!");
                System.out.println(student);

            } else if (type == 2) {
                // Graduate-specific fields
                String research = getStringInput("Research Topic: ");
                String advisor = getStringInput("Advisor Name: ");
                String thesis = getStringInput("Thesis Track? (yes/no): ");
                boolean isThesis = thesis.toLowerCase().startsWith("y");

                GraduateStudent student = manager.registerGraduateStudent(
                        name, email, age, department, research, advisor, isThesis);

                System.out.println("\nStudent registered successfully!");
                System.out.println(student);

            } else {
                System.out.println("Invalid student type selected.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void createCourseMenu() {
        System.out.println("\n--- CREATE COURSE ---");
        String courseId = getStringInput("Course ID (e.g., CS101): ").toUpperCase();
        String courseName = getStringInput("Course Name: ");
        String department = getStringInput("Department: ");
        int credits = getIntInput("Credit Hours: ");
        int maxCapacity = getIntInput("Maximum Student Capacity: ");
        String instructor = getStringInput("Instructor Name: ");

        try {
            Course course = manager.createCourse(
                    courseId, courseName, department, credits, maxCapacity, instructor);
            System.out.println("\nCourse created successfully!");
            System.out.println(course);

        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void enrollStudentMenu() {
        System.out.println("\n--- ENROLL STUDENT IN COURSE ---");
        String studentId = getStringInput("Student ID: ").toUpperCase();
        String courseId = getStringInput("Course ID: ").toUpperCase();

        try {
            manager.enrollStudentInCourse(studentId, courseId);
            System.out.println("Enrollment successful!");

        } catch (CourseFullException e) {
            // Handle the case where course is at capacity
            System.out.println("\n⚠ ENROLLMENT FAILED - COURSE FULL");
            System.out.println(e.getMessage());
            System.out.println("Tip: Check other sections or courses with available space.");

        } catch (StudentAlreadyEnrolledException e) {
            // Handle duplicate enrollment attempt
            System.out.println("\n⚠ ENROLLMENT FAILED - ALREADY ENROLLED");
            System.out.println(e.getMessage());

        } catch (IllegalArgumentException e) {
            // Handle invalid student/course ID
            System.out.println("\n✗ ERROR: " + e.getMessage());
        }
    }

    private static void updateGradeMenu() {
        System.out.println("\n--- UPDATE STUDENT GRADE ---");
        String studentId = getStringInput("Student ID: ").toUpperCase();
        String courseId = getStringInput("Course ID: ").toUpperCase();
        double grade = getDoubleInput("Grade (0.0 - 4.0): ");

        try {
            manager.updateStudentGrade(studentId, courseId, grade);
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    
    private static void viewStudentRecord() {
        System.out.println("\n--- VIEW STUDENT RECORD ---");
        String studentId = getStringInput("Enter Student ID: ").toUpperCase();

        Optional<Student> result = manager.findStudentById(studentId);

        if (result.isEmpty()) {
            System.out.println("Student not found with ID: " + studentId);
            return;
        }

        Student student = result.get();
        System.out.println("\n╔══════════════════ STUDENT RECORD ══════════════════╗");
        System.out.println("  ID:         " + student.getStudentId());
        System.out.println("  Name:       " + student.getName());
        System.out.println("  Email:      " + student.getEmail());
        System.out.println("  Age:        " + student.getAge());
        System.out.println("  Department: " + student.getDepartment());
        System.out.println("  Type:       " + student.getStudentType());
        System.out.printf( "  GPA:        %.2f%n", student.getGpa());

        if (student instanceof UndergraduateStudent) {
            UndergraduateStudent ug = (UndergraduateStudent) student;
            System.out.println("  Year:       " + ug.getYearLevelName());
            System.out.println("  Major:      " + ug.getMajor());
        } else if (student instanceof GraduateStudent) {
            GraduateStudent grad = (GraduateStudent) student;
            System.out.println("  Research:   " + grad.getResearchTopic());
            System.out.println("  Advisor:    " + grad.getAdvisor());
            System.out.println("  Thesis:     " + (grad.isThesisTrack() ? "Yes" : "No"));
        }

        System.out.printf("  Tuition:    $%.2f%n", student.calculateTuition());

        System.out.println("\n  ENROLLED COURSES:");
        System.out.println("  ─────────────────────────────────────────────────");
        Map<Course, Double> grades = student.getCourseGrades();

        if (grades.isEmpty()) {
            System.out.println("  No courses enrolled.");
        } else {
            for (Map.Entry<Course, Double> entry : grades.entrySet()) {
                Course c = entry.getKey();
                Double grade = entry.getValue();
                String gradeStr = (grade == 0.0) ? "Not Graded" : String.format("%.1f", grade);
                System.out.printf("  %-10s %-35s Credits: %d  Grade: %s%n",
                        c.getCourseId(), c.getCourseName(),
                        c.getCredits(), gradeStr);
            }
        }

        System.out.println("╚════════════════════════════════════════════════════╝");
    }

    
    private static void viewCourseRoster() {
        System.out.println("\n--- VIEW COURSE ROSTER ---");
        String courseId = getStringInput("Enter Course ID: ").toUpperCase();

        Optional<Course> result = manager.findCourseById(courseId);

        if (result.isEmpty()) {
            System.out.println("Course not found: " + courseId);
            return;
        }

        Course course = result.get();

        System.out.println("\n╔══════════════════ COURSE ROSTER ══════════════════╗");
        System.out.println("  Course ID:   " + course.getCourseId());
        System.out.println("  Name:        " + course.getCourseName());
        System.out.println("  Department:  " + course.getDepartment());
        System.out.println("  Instructor:  " + course.getInstructorName());
        System.out.println("  Credits:     " + course.getCredits());
        System.out.printf( "  Enrollment:  %d / %d%n",
                course.getCurrentEnrollment(), course.getMaxCapacity());
        System.out.println("  ─────────────────────────────────────────────────");
        System.out.println("  ENROLLED STUDENTS:");

        List<Student> roster = course.getEnrolledStudents();
        if (roster.isEmpty()) {
            System.out.println("  No students enrolled.");
        } else {
            for (int i = 0; i < roster.size(); i++) {
                Student s = roster.get(i);
                System.out.printf("  %d. [%s] %s | %s | GPA: %.2f%n",
                        i + 1, s.getStudentId(), s.getName(),
                        s.getDepartment(), s.getGpa());
            }
        }

        System.out.println("╚════════════════════════════════════════════════════╝");
    }

    private static void generateDeansList() {
        System.out.println("\n╔══════════════════ DEAN'S LIST ══════════════════╗");
        System.out.println("  Students with GPA above 3.5");
        System.out.println("  ─────────────────────────────────────────────────");

        List<Student> deansList = manager.getDeansList();

        if (deansList.isEmpty()) {
            System.out.println("  No students currently qualify for the Dean's List.");
            System.out.println("  (Students need GPA > 3.5 and at least one graded course)");
        } else {
            System.out.printf("  %-5s %-12s %-25s %-20s %s%n",
                    "#", "ID", "Name", "Department", "GPA");
            System.out.println("  ─────────────────────────────────────────────────");

            for (int i = 0; i < deansList.size(); i++) {
                Student s = deansList.get(i);
                System.out.printf("  %-5d %-12s %-25s %-20s %.2f%n",
                        (i + 1), s.getStudentId(), s.getName(),
                        s.getDepartment(), s.getGpa());
            }
            System.out.println("\n  Total qualifying students: " + deansList.size());
        }

        System.out.println("╚════════════════════════════════════════════════════╝");
    }


    private static void viewDepartmentStats() {
        System.out.println("\n--- DEPARTMENT STATISTICS ---");
        String dept = getStringInput("Enter Department name: ");

        List<Student> deptStudents = manager.getStudentsByDepartment(dept);
        double avgGpa = manager.getAverageGpaByDepartment(dept);

        System.out.println("\n╔══════════════════ DEPARTMENT STATS ════════════════╗");
        System.out.println("  Department: " + dept);
        System.out.println("  Total Students: " + deptStudents.size());
        System.out.printf( "  Average GPA: %.2f%n", avgGpa);
        System.out.println("╚════════════════════════════════════════════════════╝");

        if (!deptStudents.isEmpty()) {
            System.out.println("\nStudents in " + dept + ":");
            deptStudents.forEach(s ->
                System.out.printf("  [%s] %s - GPA: %.2f (%s)%n",
                        s.getStudentId(), s.getName(), s.getGpa(), s.getStudentType()));
        }
    }

    private static void viewTopStudent() {
        System.out.println("\n--- TOP PERFORMING STUDENT ---");

        Optional<Student> top = manager.getTopPerformingStudent();

        if (top.isEmpty()) {
            System.out.println("No graded students found yet.");
        } else {
            Student student = top.get();
            System.out.println("\n🏆 TOP PERFORMING STUDENT:");
            System.out.println("  Name: " + student.getName());
            System.out.println("  ID:   " + student.getStudentId());
            System.out.printf( "  GPA:  %.2f%n", student.getGpa());
            System.out.println("  Dept: " + student.getDepartment());
        }
    }

    private static void viewAllStudents() {
        System.out.println("\n--- ALL REGISTERED STUDENTS ---");
        List<Student> allStudents = manager.getAllStudents();

        if (allStudents.isEmpty()) {
            System.out.println("No students registered yet.");
            return;
        }

        System.out.printf("%-12s %-25s %-20s %-15s %s%n",
                "ID", "Name", "Department", "Type", "GPA");
        System.out.println("─".repeat(80));

        for (Student s : allStudents) {
            System.out.printf("%-12s %-25s %-20s %-15s %.2f%n",
                    s.getStudentId(), s.getName(), s.getDepartment(),
                    s.getStudentType(), s.getGpa());
        }

        System.out.println("\nTotal: " + allStudents.size() + " students");
        manager.printEnrollmentStatistics();
    }

    
    private static void viewAllCourses() {
        System.out.println("\n--- ALL COURSES ---");
        List<Course> allCourses = manager.getAllCourses();

        if (allCourses.isEmpty()) {
            System.out.println("No courses created yet.");
            return;
        }

        System.out.printf("%-10s %-30s %-20s %-8s %s%n",
                "ID", "Name", "Department", "Credits", "Enrollment");
        System.out.println("─".repeat(80));

        for (Course c : allCourses) {
            System.out.printf("%-10s %-30s %-20s %-8d %d/%d%n",
                    c.getCourseId(), c.getCourseName(), c.getDepartment(),
                    c.getCredits(), c.getCurrentEnrollment(), c.getMaxCapacity());
        }

        System.out.println("\nTotal: " + allCourses.size() + " courses");
    }

    private static void viewTuitionInfo() {
        System.out.println("\n--- TUITION INFORMATION ---");
        String studentId = getStringInput("Enter Student ID: ").toUpperCase();

        Optional<Student> result = manager.findStudentById(studentId);

        if (result.isEmpty()) {
            System.out.println("Student not found.");
            return;
        }

        Student student = result.get();

        System.out.println("\n╔══════════════════ TUITION BREAKDOWN ═══════════════╗");
        System.out.println("  Student: " + student.getName());
        System.out.println("  Type: " + student.getStudentType());
        System.out.println();

        if (student instanceof UndergraduateStudent) {
            System.out.println("  UNDERGRADUATE FLAT RATE:");
            System.out.println("  Base Tuition:    $5,000.00");
            System.out.println("  Activity Fee:    $  250.00");
            System.out.println("  Technology Fee:  $  150.00");
            System.out.println("  ─────────────────────────────");
            System.out.printf( "  TOTAL DUE:       $%,.2f%n", student.calculateTuition());

        } else if (student instanceof GraduateStudent) {
            GraduateStudent grad = (GraduateStudent) student;
            int credits = student.getEnrolledCourses().stream()
                    .mapToInt(Course::getCredits).sum();
            System.out.println("  GRADUATE PER-CREDIT RATE:");
            System.out.printf( "  Credits Enrolled: %d%n", credits == 0 ? 9 : credits);
            System.out.printf( "  Per-Credit Cost: $1,500.00%n");
            System.out.printf( "  Research Fee:    $2,000.00%n");
            System.out.printf( "  Lab Fee:         $  500.00%n");
            if (grad.isThesisTrack()) {
                System.out.println("  Thesis Fee:      $1,500.00");
            }
            System.out.println("  ─────────────────────────────");
            System.out.printf( "  TOTAL DUE:       $%,.2f%n", student.calculateTuition());
        }

        System.out.println("╚════════════════════════════════════════════════════╝");
    }

    private static boolean saveAndExit() {
        System.out.println("\nSaving all data before exit...");
        fileManager.saveAllData(manager);
        System.out.println("\nThank you for using University Management System!");
        System.out.println("Goodbye! 👋");
        scanner.close();
        return false; // false = stop the running loop
    }

    private static String getStringInput(String prompt) {
        String input = "";
        while (input.trim().isEmpty()) {
            System.out.print(prompt);
            input = scanner.nextLine();
            if (input.trim().isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
            }
        }
        return input.trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid decimal number (e.g., 3.5)");
            }
        }
    }
}