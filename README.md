# University Management System
### Java Backend Phase 1 Capstone Project

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=java)
![OOP](https://img.shields.io/badge/OOP-Principles-blue?style=for-the-badge)
![Collections](https://img.shields.io/badge/Java-Collections-green?style=for-the-badge)
![Git](https://img.shields.io/badge/Git-Version%20Control-red?style=for-the-badge&logo=git)

---

## Table of Contents
- [About The Project](#about-the-project)
- [Features](#features)
- [Project Structure](#project-structure)
- [OOP Concepts Used](#oop-concepts-used)
- [Java Collections Used](#java-collections-used)
- [Exception Handling](#exception-handling)
- [File Persistence](#file-persistence)
- [Menu Guide](#menu-guide)
- [Sample Session](#sample-session)
- [What I Learned](#what-i-learned)

---

##  About The Project

The **University Management System** is a console-based Java application.

It simulates a real university system where you can:
- Register undergraduate and graduate students
- Create and manage courses
- Enroll students into courses
- Track grades and calculate GPA automatically
- Generate academic reports like the Dean's List
- Save and load all data using CSV files

This project was built to demonstrate mastery of:
> **Java OOP · Collections Framework · Exception Handling · File I/O · Git/GitHub**

---

##  Features

| Feature | Description |
|---|---|
| 👩‍🎓 Register Students | Add undergraduate or graduate students with unique auto-generated IDs |
| 📚 Create Courses | Add courses with capacity limits and credit hours |
| 📝 Enroll Students | Safely enroll students with business rule validation |
| 🎯 Track Grades | Update individual course grades, GPA auto-recalculates |
| 🏆 Dean's List | Auto-generate list of students with GPA above 3.5 |
| 📊 Department Stats | View average GPA per department |
| 💰 Tuition Calculator | Different tuition logic for undergrad vs graduate students |
| 💾 Save & Load | All data persists between sessions using CSV files |
| ⚠️ Error Handling | Custom exceptions prevent invalid operations |
| 🖥️ Console Menu | Clean, user-friendly menu-driven interface |

## How to Run
1. Compile all .java files
2. Run Main.java
3. Follow the console menu

## Project Structure
- model/     - Domain classes (Person, Student, Course)
- exception/ - Custom business logic exceptions
- manager/   - Business logic controllers
- data/      - CSV persistence files


---

## 🧱 OOP Concepts Used

### 1. Abstraction
Abstract classes define the blueprint without full implementation.
```java
// Person is abstract - you cannot create a Person directly
// You MUST create a Student or Instructor instead
public abstract class Person {
    public abstract String getRole(); // Must be implemented by subclass
}

// Student is also abstract
public abstract class Student extends Person {
    public abstract double calculateTuition(); // Must be implemented by subclass
}
```

### 2. Inheritance
Classes extend parent classes to reuse and build on existing code.

Person  (grandparent)
  └── Student  (parent - abstract)
        ├── UndergraduateStudent  (child)
        └── GraduateStudent      (child)
  └── Instructor  (child)

```Java
// UndergraduateStudent INHERITS everything from Student and Person
public class UndergraduateStudent extends Student {
    // Gets name, email, age FROM Person
    // Gets studentId, gpa, courseGrades FROM Student
    // Adds its own: yearLevel, major
}
```
### 3. Polymorphism
Same method name, different behaviour depending on the object type.

```Java

// calculateTuition() is called the SAME WAY for both types
// but produces DIFFERENT results

Student ugStudent = new UndergraduateStudent(...);
Student gradStudent = new GraduateStudent(...);

// Flat rate: $5,400.00
System.out.println(ugStudent.calculateTuition());

// Per credit: $16,000.00 (depends on credits enrolled)
System.out.println(gradStudent.calculateTuition());

// Java automatically calls the RIGHT version at runtime!
```

### 4. Encapsulation
All fields are private. Access is controlled through getters and setters.

```Java

public class Student extends Person {

    private String studentId;   // ← PRIVATE - no direct access
    private double gpa;         // ← PRIVATE - cannot be set from outside
    private String department;  // ← PRIVATE - protected

    // CONTROLLED READ access
    public String getStudentId() { return studentId; }
    public double getGpa() { return gpa; } // read-only!

    // CONTROLLED WRITE access with validation
    public void setDepartment(String department) {
        this.department = department;
    }

    // GPA has NO setter - it only updates internally via recalculateGPA()
    // This PROTECTS data integrity!
}
```

---

### Java Collections Used

## List<Student> — Course Roster
```Java

// In Course.java
// One course has MANY students
private List<Student> enrolledStudents = new ArrayList<>();

// Used for:
enrolledStudents.add(student);      // Enroll a student
enrolledStudents.remove(student);   // Drop a student
enrolledStudents.contains(student); // Check if enrolled
enrolledStudents.size();            // Count enrolled
```

## Map<Course, Double> — Student Grade Book
```Java

// In Student.java
// Maps each Course to the student's grade in that course
private Map<Course, Double> courseGrades = new HashMap<>();

// Visual representation:
// | Course Key        | Grade Value |
// |-------------------|-------------|
// | CS101 (Intro)     |     3.7     |
// | MATH201 (Calc)    |     3.2     |
// | ENG101 (English)  |     4.0     |

// Used for:
courseGrades.put(course, 0.0);          // Add new course
courseGrades.put(course, grade);        // Update grade
courseGrades.containsKey(course);       // Check enrollment
courseGrades.values();                  // Get all grades (for GPA)
courseGrades.entrySet();                // Get course+grade pairs
```

### Map<String, Course> — Course Catalog

```Java

// In UniversityManager.java
// Fast O(1) lookup of any course by its ID
private Map<String, Course> courses = new HashMap<>();

// Example:
courses.put("CS101", course);  // Store course
courses.get("CS101");          // Retrieve instantly - O(1)!
courses.containsKey("CS101");  // Check if exists
```

## Java Streams — Data Queries
```Java

// Dean's List - filter, sort, collect
List<Student> deansList = students.stream()
    .filter(s -> s.getGpa() > 3.5)           // Keep GPA > 3.5
    .sorted((s1, s2) -> Double.compare(       // Sort highest first
            s2.getGpa(), s1.getGpa()))
    .collect(Collectors.toList());             // Make a List

// Average GPA by Department
OptionalDouble avg = students.stream()
    .filter(s -> s.getDepartment().equals(dept))
    .mapToDouble(Student::getGpa)              // Extract GPA values
    .average();                                // Calculate average

// Top Student
Optional<Student> top = students.stream()
    .max(Comparator.comparingDouble(Student::getGpa));

```

---

### Exception Handling
## Why Custom Exceptions?
Instead of letting the program crash or showing confusing errors,
we create meaningful exceptions that tell the user exactly what went wrong.

## CourseFullException
```Java

// Thrown when: student tries to enroll in a full course
// Type: Checked Exception (compiler forces you to handle it)

public class CourseFullException extends Exception {
    // Stores: courseId, courseName, maxCapacity
    // Message: "Course 'Data Structures' (CS301) is FULL!
    //           Maximum capacity of 30 students reached."
}
```

## StudentAlreadyEnrolledException
```Java

// Thrown when: student tries to enroll in a course twice
// Type: Checked Exception

public class StudentAlreadyEnrolledException extends Exception {
    // Stores: studentId, studentName, courseId, courseName
    // Message: "Student 'John Doe' (STU1000) is ALREADY
    //           enrolled in 'Data Structures' (CS301)."
}
```
## How They Are Handled
```Java

try {
    // This single line can throw TWO different exceptions!
    manager.enrollStudentInCourse(studentId, courseId);

} catch (CourseFullException e) {
    // Course is at capacity - tell user and suggest alternatives
    System.out.println("ENROLLMENT FAILED - COURSE FULL");
    System.out.println(e.getMessage());

} catch (StudentAlreadyEnrolledException e) {
    // Duplicate attempt - explain the issue clearly
    System.out.println("ENROLLMENT FAILED - ALREADY ENROLLED");
    System.out.println(e.getMessage());

} catch (IllegalArgumentException e) {
    // Student or Course ID not found
    System.out.println("ERROR: " + e.getMessage());
}
// Program NEVER crashes - all cases are handled!
```

---

### File Persistence
Data is saved to and loaded from CSV files automatically.
No database is needed - everything lives in simple text files.

## How Saving Works
```text

When user selects "Save and Exit":

students.csv    ← All student records
courses.csv     ← All course details  
enrollments.csv ← Who is in what course with what grade
```
## File Format Examples
students.csv

```text

# Students Data File - University Management System
# Format: TYPE,ID,Name,Email,Age,Department,[type-specific fields]
UNDERGRAD,STU1000,John Doe,john@email.com,20,Computer Science,2,Software Engineering
GRAD,STU1001,Jane Smith,jane@email.com,25,Physics,Quantum Computing,Dr. Brown,true
```
courses.csv

```text

# Courses Data File
# Format: CourseID,Name,Department,Credits,MaxCapacity,Instructor
CS101,Intro to Programming,Computer Science,3,30,Dr. Smith
MATH201,Calculus I,Mathematics,4,25,Dr. Johnson
```
enrollments.csv

```text

# Enrollments Data File
# Format: StudentID,CourseID,Grade
STU1000,CS101,3.8
STU1000,MATH201,3.2
STU1001,CS101,3.5
```
## How Loading Works
```text

When application starts:

1. Load students.csv  → Recreate Student objects
2. Load courses.csv   → Recreate Course objects
3. Load enrollments.csv → Re-link students to courses
                          Restore all grades
                          GPA auto-recalculates
```
---

### Menu Guide

```text

╔══════════════════════════════════════════════╗
║    UNIVERSITY MANAGEMENT SYSTEM              ║
╚══════════════════════════════════════════════╝

═══════════════ MAIN MENU ═══════════════
  STUDENT MANAGEMENT
  1.  Register Student
  2.  Create Course
  3.  Enroll Student in Course
  4.  Update Student Grade

  VIEW & REPORTS
  5.  View Student Record
  6.  View Course Roster
  7.  Generate Dean's List (GPA > 3.5)
  8.  Department Statistics
  9.  Top Performing Student
  10. View All Students
  11. View All Courses
  12. View Tuition Information

  0.  Save and Exit
═════════════════════════════════════════
```

| Option | Description |
|--------|------------|
| 1 | Register a new undergraduate or graduate student |
| 2 | Create a new course with capacity and credits |
| 3 | Enroll a student in a course (with exception handling) |
| 4 | Update a student's grade for a specific course |
| 5 | View full student profile, courses, grades and GPA |
| 6 | View all students enrolled in a specific course |
| 7 | List all students with GPA above 3.5 (sorted highest first) |
| 8 | View average GPA for an entire department |
| 9 | Find the single highest GPA student |
| 10 | View all registered students in a table |
| 11 | View all available courses |
| 12 | See detailed tuition breakdown for a student |
| 0 | Save everything to CSV files and exit |

---
### Sample Session

```text
╔══════════════════════════════════════════════╗
║    UNIVERSITY MANAGEMENT SYSTEM              ║
╚══════════════════════════════════════════════╝

Loading saved data...
No saved data found. Starting fresh.

═══════════════ MAIN MENU ═══════════════
  STUDENT MANAGEMENT
  1.  Register Student
  2.  Create Course
  3.  Enroll Student in Course
  4.  Update Student Grade

  VIEW & REPORTS
  5.  View Student Record
  6.  View Course Roster
  7.  Generate Dean's List (GPA > 3.5)
  8.  Department Statistics
  9.  Top Performing Student
  10. View All Students
  11. View All Courses
  12. View Tuition Information

  0.  Save and Exit
═════════════════════════════════════════
Enter your choice: 1

--- REGISTER STUDENT ---
Student Type:
  1. Undergraduate
  2. Graduate
Select type: 2
Full Name: samillah mutoni
Email: samillah.mutoni@gmail.com
Age: 21
Department: software engineering 
Research Topic: ai
Advisor Name: prince
Thesis Track? (yes/no): no
SUCCESS: Registered graduate student: samillah mutoni (ID: STU1000)

Student registered successfully!
Student[STU1000] samillah mutoni | Dept: software engineering | GPA: 0.00 | Courses: 0 | Type: GRADUATE | Research: ai | Advisor: prince | Thesis: No | Tuition: $16000.00

═══════════════ MAIN MENU ═══════════════
  STUDENT MANAGEMENT
  1.  Register Student
  2.  Create Course
  3.  Enroll Student in Course
  4.  Update Student Grade

  VIEW & REPORTS
  5.  View Student Record
  6.  View Course Roster
  7.  Generate Dean's List (GPA > 3.5)
  8.  Department Statistics
  9.  Top Performing Student
  10. View All Students
  11. View All Courses
  12. View Tuition Information

  0.  Save and Exit
═════════════════════════════════════════
Enter your choice: 2

--- CREATE COURSE ---
Course ID: CS101
Course Name: Computer Maintainance
Department: Software Engineering
Credit Hours: 3
Maximum Student Capacity: 60
Instructor Name: Emmanuel
SUCCESS: Created course: Computer Maintainance (CS101)

Course created successfully!
[CS101] Computer Maintainance | Dept: Software Engineering | Credits: 3 | Enrolled: 0/60 | Instructor: Emmanuel

═══════════════ MAIN MENU ═══════════════
  STUDENT MANAGEMENT
  1.  Register Student
  2.  Create Course
  3.  Enroll Student in Course
  4.  Update Student Grade

  VIEW & REPORTS
  5.  View Student Record
  6.  View Course Roster
  7.  Generate Dean's List (GPA > 3.5)
  8.  Department Statistics
  9.  Top Performing Student
  10. View All Students
  11. View All Courses
  12. View Tuition Information

  0.  Save and Exit
═════════════════════════════════════════
Enter your choice: 3

--- ENROLL STUDENT IN COURSE ---
Student ID: STU1000
Course ID: CS101
SUCCESS: samillah mutoni enrolled in Computer Maintainance
Enrollment successful!

═══════════════ MAIN MENU ═══════════════
  STUDENT MANAGEMENT
  1.  Register Student
  2.  Create Course
  3.  Enroll Student in Course
  4.  Update Student Grade

  VIEW & REPORTS
  5.  View Student Record
  6.  View Course Roster
  7.  Generate Dean's List (GPA > 3.5)
  8.  Department Statistics
  9.  Top Performing Student
  10. View All Students
  11. View All Courses
  12. View Tuition Information

  0.  Save and Exit
═════════════════════════════════════════
Enter your choice: 4

--- UPDATE STUDENT GRADE ---
Student ID: STU1000
Course ID: CS101
Grade (0.0 - 4.0): 3.0
SUCCESS: Updated grade for samillah mutoni in Computer Maintainance: 3.0

═══════════════ MAIN MENU ═══════════════
  STUDENT MANAGEMENT
  1.  Register Student
  2.  Create Course
  3.  Enroll Student in Course
  4.  Update Student Grade

  VIEW & REPORTS
  5.  View Student Record
  6.  View Course Roster
  7.  Generate Dean's List (GPA > 3.5)
  8.  Department Statistics
  9.  Top Performing Student
  10. View All Students
  11. View All Courses
  12. View Tuition Information

  0.  Save and Exit
═════════════════════════════════════════
Enter your choice: 5

--- VIEW STUDENT RECORD ---
Enter Student ID: STU1000

╔══════════════════ STUDENT RECORD ══════════════════╗
  ID:         STU1000
  Name:       samillah mutoni
  Email:      samillah.mutoni@gmail.com
  Age:        21
  Department: software engineering
  Type:       GRADUATE
  GPA:        3.00
  Research:   ai
  Advisor:    prince
  Thesis:     No
  Tuition:    $7000.00

  ENROLLED COURSES:
  ─────────────────────────────────────────────────
  CS101      Computer Maintainance               Credits: 3  Grade: 3.0
╚════════════════════════════════════════════════════╝

═══════════════ MAIN MENU ═══════════════
  STUDENT MANAGEMENT
  1.  Register Student
  2.  Create Course
  3.  Enroll Student in Course
  4.  Update Student Grade

  VIEW & REPORTS
  5.  View Student Record
  6.  View Course Roster
  7.  Generate Dean's List (GPA > 3.5)
  8.  Department Statistics
  9.  Top Performing Student
  10. View All Students
  11. View All Courses
  12. View Tuition Information

  0.  Save and Exit
═════════════════════════════════════════
Enter your choice: 6

--- VIEW COURSE ROSTER ---
Enter Course ID: CS101

╔══════════════════ COURSE ROSTER ══════════════════╗
  Course ID:   CS101
  Name:        Computer Maintainance
  Department:  Software Engineering
  Instructor:  Emmanuel
  Credits:     3
  Enrollment:  1 / 60
  ─────────────────────────────────────────────────
  ENROLLED STUDENTS:
  1. [STU1000] samillah mutoni | software engineering | GPA: 3.00
╚════════════════════════════════════════════════════╝

═══════════════ MAIN MENU ═══════════════
  STUDENT MANAGEMENT
  1.  Register Student
  2.  Create Course
  3.  Enroll Student in Course
  4.  Update Student Grade

  VIEW & REPORTS
  5.  View Student Record
  6.  View Course Roster
  7.  Generate Dean's List (GPA > 3.5)
  8.  Department Statistics
  9.  Top Performing Student
  10. View All Students
  11. View All Courses
  12. View Tuition Information

  0.  Save and Exit
═════════════════════════════════════════
Enter your choice: 7

╔══════════════════ DEAN'S LIST ══════════════════╗
  Students with GPA above 3.5
  ─────────────────────────────────────────────────
  No students currently qualify for the Dean's List.
  (Students need GPA > 3.5 and at least one graded course)
╚════════════════════════════════════════════════════╝

═══════════════ MAIN MENU ═══════════════
  STUDENT MANAGEMENT
  1.  Register Student
  2.  Create Course
  3.  Enroll Student in Course
  4.  Update Student Grade

  VIEW & REPORTS
  5.  View Student Record
  6.  View Course Roster
  7.  Generate Dean's List (GPA > 3.5)
  8.  Department Statistics
  9.  Top Performing Student
  10. View All Students
  11. View All Courses
  12. View Tuition Information

  0.  Save and Exit
═════════════════════════════════════════
Enter your choice: 8

--- DEPARTMENT STATISTICS ---
Enter Department name: Software Engineering

╔══════════════════ DEPARTMENT STATS ════════════════╗
  Department: Software Engineering
  Total Students: 1
  Average GPA: 3.00
╚════════════════════════════════════════════════════╝

Students in Software Engineering:
  [STU1000] samillah mutoni - GPA: 3.00 (GRADUATE)

═══════════════ MAIN MENU ═══════════════
  STUDENT MANAGEMENT
  1.  Register Student
  2.  Create Course
  3.  Enroll Student in Course
  4.  Update Student Grade

  VIEW & REPORTS
  5.  View Student Record
  6.  View Course Roster
  7.  Generate Dean's List (GPA > 3.5)
  8.  Department Statistics
  9.  Top Performing Student
  10. View All Students
  11. View All Courses
  12. View Tuition Information

  0.  Save and Exit
═════════════════════════════════════════
Enter your choice: 9

--- TOP PERFORMING STUDENT ---

? TOP PERFORMING STUDENT:
  Name: samillah mutoni
  ID:   STU1000
  GPA:  3.00
  Dept: software engineering

═══════════════ MAIN MENU ═══════════════
  STUDENT MANAGEMENT
  1.  Register Student
  2.  Create Course
  3.  Enroll Student in Course
  4.  Update Student Grade

  VIEW & REPORTS
  5.  View Student Record
  6.  View Course Roster
  7.  Generate Dean's List (GPA > 3.5)
  8.  Department Statistics
  9.  Top Performing Student
  10. View All Students
  11. View All Courses
  12. View Tuition Information

  0.  Save and Exit
═════════════════════════════════════════
Enter your choice: 10

--- ALL REGISTERED STUDENTS ---
ID           Name                      Department           Type            GPA
────────────────────────────────────────────────────────────────────────────────
STU1000      samillah mutoni           software engineering GRADUATE        3.00

Total: 1 students

========== ENROLLMENT STATISTICS ==========
Total Students: 1
Total Courses: 1
Undergraduates: 0
Graduates: 1
Overall Average GPA: 3.00
===========================================

═══════════════ MAIN MENU ═══════════════
  STUDENT MANAGEMENT
  1.  Register Student
  2.  Create Course
  3.  Enroll Student in Course
  4.  Update Student Grade

  VIEW & REPORTS
  5.  View Student Record
  6.  View Course Roster
  7.  Generate Dean's List (GPA > 3.5)
  8.  Department Statistics
  9.  Top Performing Student
  10. View All Students
  11. View All Courses
  12. View Tuition Information

  0.  Save and Exit
═════════════════════════════════════════
Enter your choice: 11

--- ALL COURSES ---
ID         Name                           Department           Credits  Enrollment
────────────────────────────────────────────────────────────────────────────────
CS101      Computer Maintainance          Software Engineering 3        1/60

Total: 1 courses

═══════════════ MAIN MENU ═══════════════
  STUDENT MANAGEMENT
  1.  Register Student
  2.  Create Course
  3.  Enroll Student in Course
  4.  Update Student Grade

  VIEW & REPORTS
  5.  View Student Record
  6.  View Course Roster
  7.  Generate Dean's List (GPA > 3.5)
  8.  Department Statistics
  9.  Top Performing Student
  10. View All Students
  11. View All Courses
  12. View Tuition Information

  0.  Save and Exit
═════════════════════════════════════════
Enter your choice: 12

--- TUITION INFORMATION ---
Enter Student ID: STU1000

╔══════════════════ TUITION BREAKDOWN ═══════════════╗
  Student: samillah mutoni
  Type: GRADUATE

  GRADUATE PER-CREDIT RATE:
  Credits Enrolled: 3
  Per-Credit Cost: $1,500.00
  Research Fee:    $2,000.00
  Lab Fee:         $  500.00
  ─────────────────────────────
  TOTAL DUE:       $7,000.00
╚════════════════════════════════════════════════════╝
═══════════════ MAIN MENU ═══════════════
  STUDENT MANAGEMENT
  1.  Register Student
  2.  Create Course
  3.  Enroll Student in Course
  4.  Update Student Grade

  VIEW & REPORTS
  5.  View Student Record
  6.  View Course Roster
  7.  Generate Dean's List (GPA > 3.5)
  8.  Department Statistics
  9.  Top Performing Student
  10. View All Students
  11. View All Courses
  12. View Tuition Information

  0.  Save and Exit
═════════════════════════════════════════
Enter your choice: 0

Saving all data before exit...

Saving data...
  Saved 1 students to data/students.csv
  Saved 1 courses to data/courses.csv
  Saved 1 enrollment records to data/enrollments.csv   
All data saved successfully!

Thank you for using University Management System!
```
---
### What I Learned
## Java OOP
```text

✅ How abstract classes define contracts for subclasses
✅ How inheritance avoids code duplication
✅ How polymorphism lets one method work differently per type
✅ How encapsulation protects data integrity
✅ When to use abstract class vs interface
```
## Java Collections
```text

✅ List<T>     - ordered collection, allows iteration
✅ Map<K,V>    - key-value pairs for fast lookup
✅ Set<T>      - unique elements (used via Map.keySet())
✅ ArrayList   - resizable array implementation of List
✅ HashMap     - hash table implementation of Map
✅ Optional<T> - safely handles null/not-found cases
```
## Exception Handling
```text

✅ Difference between checked and unchecked exceptions
✅ How to create meaningful custom exceptions
✅ How to throw exceptions at the right moment
✅ How to catch multiple exception types cleanly
✅ How to write code that NEVER crashes on bad input
```
## File I/O
```text

✅ BufferedWriter for efficient file writing
✅ BufferedReader for line-by-line file reading
✅ try-with-resources for automatic resource closing
✅ CSV format for human-readable data storage
✅ How to rebuild object relationships from saved data
```
## Java Streams
```text

✅ stream().filter() for searching collections
✅ stream().sorted() for ordering results
✅ stream().mapToDouble().average() for calculations
✅ stream().max() for finding best result
✅ stream().collect() for gathering results
✅ Method references e.g. Student::getGpa
```
---
### Author
- GitHub: @Samillah47
- Project: Phase-One-Capstone-Project

### Programme
SheCanCode - Advanced Java Backend Development
Phase 1 Capstone Project
