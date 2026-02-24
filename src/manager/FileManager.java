package manager;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import model.Course;
import model.GraduateStudent;
import model.Student;
import model.UndergraduateStudent;

public class FileManager {

    private static final String DATA_DIRECTORY = "data/";
    private static final String STUDENTS_FILE = DATA_DIRECTORY + "students.csv";
    private static final String COURSES_FILE = DATA_DIRECTORY + "courses.csv";
    private static final String ENROLLMENTS_FILE = DATA_DIRECTORY + "enrollments.csv";

    private static final String DELIMITER = ",";

    public FileManager() {
        createDataDirectory();
    }

    private void createDataDirectory() {
        try {
            Files.createDirectories(Paths.get(DATA_DIRECTORY));
        } catch (IOException e) {
            System.err.println("WARNING: Could not create data directory: "
                    + e.getMessage());
        }
    }

    public void saveAllData(UniversityManager manager) {
        System.out.println("\nSaving data...");
        saveStudents(manager);
        saveCourses(manager);
        saveEnrollments(manager);
        System.out.println("All data saved successfully!");
    }

    
    private void saveStudents(UniversityManager manager) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(STUDENTS_FILE))) {

            writer.write("# Students Data File - University Management System");
            writer.newLine();
            writer.write("# Format: TYPE,ID,Name,Email,Age,Department,[type-specific fields]");
            writer.newLine();

            for (Student student : manager.getAllStudents()) {
                String line = buildStudentCsvLine(student);
                writer.write(line);
                writer.newLine();
            }

            System.out.println("  Saved " + manager.getAllStudents().size()
                    + " students to " + STUDENTS_FILE);

        } catch (IOException e) {
            System.err.println("ERROR saving students: " + e.getMessage());
        }
    }

    private String buildStudentCsvLine(Student student) {

        StringBuilder sb = new StringBuilder();

        if (student instanceof UndergraduateStudent) {
            UndergraduateStudent ug = (UndergraduateStudent) student;
            sb.append("UNDERGRAD").append(DELIMITER)
              .append(ug.getStudentId()).append(DELIMITER)
              .append(ug.getName()).append(DELIMITER)
              .append(ug.getEmail()).append(DELIMITER)
              .append(ug.getAge()).append(DELIMITER)
              .append(ug.getDepartment()).append(DELIMITER)
              .append(ug.getYearLevel()).append(DELIMITER)
              .append(ug.getMajor());

        } else if (student instanceof GraduateStudent) {
            GraduateStudent grad = (GraduateStudent) student;
            sb.append("GRAD").append(DELIMITER)
              .append(grad.getStudentId()).append(DELIMITER)
              .append(grad.getName()).append(DELIMITER)
              .append(grad.getEmail()).append(DELIMITER)
              .append(grad.getAge()).append(DELIMITER)
              .append(grad.getDepartment()).append(DELIMITER)
              .append(grad.getResearchTopic()).append(DELIMITER)
              .append(grad.getAdvisor()).append(DELIMITER)
              .append(grad.isThesisTrack());
        }

        return sb.toString();
    }

    
    private void saveCourses(UniversityManager manager) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(COURSES_FILE))) {

            writer.write("# Courses Data File");
            writer.newLine();
            writer.write("# Format: CourseID,Name,Department,Credits,MaxCapacity,Instructor");
            writer.newLine();

            for (Course course : manager.getAllCourses()) {
                String line = String.join(DELIMITER,
                        course.getCourseId(),
                        course.getCourseName(),
                        course.getDepartment(),
                        String.valueOf(course.getCredits()),
                        String.valueOf(course.getMaxCapacity()),
                        course.getInstructorName());

                writer.write(line);
                writer.newLine();
                
            }

            System.out.println("  Saved " + manager.getAllCourses().size()
                    + " courses to " + COURSES_FILE);

        } catch (IOException e) {
            System.err.println("ERROR saving courses: " + e.getMessage());
        }
    }

    private void saveEnrollments(UniversityManager manager) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(ENROLLMENTS_FILE))) {

            writer.write("# Enrollments Data File");
            writer.newLine();
            writer.write("# Format: StudentID,CourseID,Grade");
            writer.newLine();

            int count = 0;
            for (Student student : manager.getAllStudents()) {
                for (Map.Entry<Course, Double> entry
                        : student.getCourseGrades().entrySet()) {
                    String line = String.join(DELIMITER,
                            student.getStudentId(),
                            entry.getKey().getCourseId(),
                            String.valueOf(entry.getValue()));
                    writer.write(line);
                    writer.newLine();
                    count++;
                }
            }

            System.out.println("  Saved " + count + " enrollment records to "
                    + ENROLLMENTS_FILE);

        } catch (IOException e) {
            System.err.println("ERROR saving enrollments: " + e.getMessage());
        }
    }

    public void loadAllData(UniversityManager manager) {
        System.out.println("Loading saved data...");

        if (!dataFilesExist()) {
            System.out.println("No saved data found. Starting fresh.");
            return;
        }

        loadStudents(manager);
        loadCourses(manager);
        loadEnrollments(manager);

        System.out.println("Data loaded successfully!");
    }

    private boolean dataFilesExist() {
        return Files.exists(Paths.get(STUDENTS_FILE))
                || Files.exists(Paths.get(COURSES_FILE));
    }

    private void loadStudents(UniversityManager manager) {
        File file = new File(STUDENTS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int loadedCount = 0;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) {
                    continue;
                }

                Student student = parseStudentFromCsv(line);
                if (student != null) {
                    manager.addStudentDirectly(student);
                    loadedCount++;
                }
            }

            System.out.println("  Loaded " + loadedCount + " students.");

        } catch (IOException e) {
            System.err.println("ERROR loading students: " + e.getMessage());
        }
    }

    private Student parseStudentFromCsv(String line) {
        try {
            String[] parts = line.split(DELIMITER);
            String type = parts[0].trim();

            if (type.equals("UNDERGRAD")) {
                return new UndergraduateStudent(
                        parts[2].trim(),              // name
                        parts[3].trim(),              // email
                        Integer.parseInt(parts[4].trim()), // age
                        parts[1].trim(),              // studentId
                        parts[5].trim(),              // department
                        Integer.parseInt(parts[6].trim()), // yearLevel
                        parts[7].trim()               // major
                );

            } else if (type.equals("GRAD")) {
                return new GraduateStudent(
                        parts[2].trim(),              // name
                        parts[3].trim(),              // email
                        Integer.parseInt(parts[4].trim()), // age
                        parts[1].trim(),              // studentId
                        parts[5].trim(),              // department
                        parts[6].trim(),              // researchTopic
                        parts[7].trim(),              // advisor
                        Boolean.parseBoolean(parts[8].trim()) // isThesisTrack
                );
            }
        } catch (Exception e) {
            System.err.println("WARNING: Could not parse student line: " + line);
            System.err.println("  Error: " + e.getMessage());
        }
        return null;
    }

    private void loadCourses(UniversityManager manager) {
        File file = new File(COURSES_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int loadedCount = 0;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) continue;

                Course course = parseCourseFromCsv(line);
                if (course != null) {
                    manager.addCourseDirectly(course);
                    loadedCount++;
                }
            }

            System.out.println("  Loaded " + loadedCount + " courses.");

        } catch (IOException e) {
            System.err.println("ERROR loading courses: " + e.getMessage());
        }
    }

    private Course parseCourseFromCsv(String line) {
        try {
            String[] parts = line.split(DELIMITER);
            return new Course(
                    parts[0].trim(),              // courseId
                    parts[1].trim(),              // courseName
                    parts[2].trim(),              // department
                    Integer.parseInt(parts[3].trim()), // credits
                    Integer.parseInt(parts[4].trim()), // maxCapacity
                    parts[5].trim()               // instructorName
            );
        } catch (Exception e) {
            System.err.println("WARNING: Could not parse course line: " + line);
        }
        return null;
    }

    private void loadEnrollments(UniversityManager manager) {
        File file = new File(ENROLLMENTS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int loadedCount = 0;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) continue;

                String[] parts = line.split(DELIMITER);
                if (parts.length < 3) continue;

                String studentId = parts[0].trim();
                String courseId = parts[1].trim();
                double grade = Double.parseDouble(parts[2].trim());

                Optional<Student> studentOpt = manager.findStudentById(studentId);
                Optional<Course> courseOpt = manager.findCourseById(courseId);

                if (studentOpt.isPresent() && courseOpt.isPresent()) {
                    Student student = studentOpt.get();
                    Course course = courseOpt.get();

                    student.addCourse(course);
                    course.addStudent(student);

                    if (grade > 0.0) {
                        student.updateGrade(course, grade);
                    }
                    loadedCount++;
                }
            }

            System.out.println("  Loaded " + loadedCount + " enrollment records.");

        } catch (IOException e) {
            System.err.println("ERROR loading enrollments: " + e.getMessage());
        }
    }
}
