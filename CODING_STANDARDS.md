# Java Coding Standards

Standards for consistent, readable code across the team.

## 1. Naming Conventions

| Element | Convention | Example |
|---------|-----------|---------|
| **Classes** | PascalCase | `Student`, `CourseManager`, `LoginForm` |
| **Methods** | camelCase | `calculateGPA()`, `isEligible()` |
| **Variables** | camelCase | `studentName`, `totalCredits` |
| **Constants** | UPPER_SNAKE_CASE | `MAX_CREDITS`, `MIN_GPA` |
| **Packages** | lowercase | `model`, `service`, `gui` |

### Examples

```java
// ✅ GOOD
public class StudentRecord {
    private static final int MAX_COURSES = 6;
    private String studentName;
    private boolean isEligible;

    public void calculateGPA() {
        // implementation
    }
}

// ❌ BAD
public class student_record {           // Wrong: use PascalCase
    private String Student_Name;        // Wrong: use camelCase

    public void Calculate_GPA() {       // Wrong: use camelCase
        // implementation
    }
}
```

---

## 2. Class Design

### Single Responsibility Principle

One class, one job.

```java
// ✅ GOOD: Separate responsibilities
public class Student {
    private String name;
    private List<Course> courses;

    public void addCourse(Course course) {
        courses.add(course);
    }
}

public class StudentValidator {
    public boolean isEligible(Student student, Course course) {
        return hasPrerequisites(student, course);
    }
}

// ❌ BAD: One class doing everything
public class Student {
    private String name;

    public void sendEmail() { }         // Should be EmailService
    public void generatePDF() { }       // Should be PDFService
    public void validateCourse() { }    // Should be Validator
}
```

### Encapsulation

Private fields with getters/setters.

```java
// ✅ GOOD
public class Student {
    private String name;
    private int age;

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

// ❌ BAD
public class Student {
    public String name;  // Anyone can set to null!
    public int age;      // Can be -5 or 999!
}
```

---

## 3. Methods

Keep methods short and focused.

```java
// ✅ GOOD: Small, focused methods
public boolean isEligible(Student student, Course course) {
    return hasPrerequisites(student, course)
        && hasAvailableCredits(student)
        && !hasScheduleConflict(student, course);
}

private boolean hasPrerequisites(Student student, Course course) {
    return student.getCompletedCourses()
                  .containsAll(course.getPrerequisites());
}

// ❌ BAD: One giant method
public boolean isEligible(Student student, Course course) {
    // 100+ lines doing everything
}
```

**Rule:** If a method exceeds 30-40 lines, break it into smaller methods.

---

## 4. Exception Handling

Use specific exceptions, handle properly.

```java
// ✅ GOOD
public void enrollStudent(String studentId, String courseId)
        throws StudentNotFoundException, CourseFullException {

    if (studentId == null || studentId.isEmpty()) {
        throw new IllegalArgumentException("Student ID cannot be empty");
    }

    Student student = findStudent(studentId);
    if (student == null) {
        throw new StudentNotFoundException("Student not found: " + studentId);
    }

    Course course = findCourse(courseId);
    if (course.isFull()) {
        throw new CourseFullException("Course is full");
    }

    // enroll logic
}

// ❌ BAD: Catching everything
public void enrollStudent(String studentId, String courseId) {
    try {
        // everything
    } catch (Exception e) {
        e.printStackTrace();  // Hides errors!
    }
}

// ❌ WORSE: Empty catch
try {
    someOperation();
} catch (Exception e) {
    // Silent failure - never do this!
}
```

### Custom Exceptions

```java
public class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String message) {
        super(message);
    }
}

public class CourseFullException extends Exception {
    public CourseFullException(String message) {
        super(message);
    }
}
```

---

## 5. Collections

Use the right collection for the job.

```java
// ✅ GOOD: Appropriate collections
public class CourseManager {
    private Map<String, Course> coursesById = new HashMap<>();    // Fast lookup
    private List<Student> enrolledStudents = new ArrayList<>();   // Ordered
    private Set<String> completedCourseIds = new HashSet<>();     // No duplicates
}

// ❌ BAD: Arrays for everything
public class CourseManager {
    private Course[] courses = new Course[100];      // Fixed size, inflexible
    private Student[] students = new Student[1000];  // Inefficient
}
```

---

## 6. Constants

Use named constants instead of magic numbers.

```java
// ✅ GOOD
public class Course {
    private static final int MAX_STUDENTS = 30;
    private static final int MIN_CREDITS = 3;
    private static final double PASSING_GRADE = 50.0;

    public boolean canEnroll() {
        return enrolledStudents.size() < MAX_STUDENTS;
    }

    public boolean isPassing(double grade) {
        return grade >= PASSING_GRADE;
    }
}

// ❌ BAD
public boolean canEnroll() {
    return enrolledStudents.size() < 30;  // What is 30?
}

public boolean isPassing(double grade) {
    return grade >= 50.0;  // Why 50?
}
```

### Use Enums for Type Safety

```java
// ✅ GOOD
public enum UserRole {
    STUDENT,
    ADMIN,
    INSTRUCTOR
}

public enum Grade {
    A, B, C, D, F
}

if (user.getRole() == UserRole.STUDENT) {
    // type-safe
}

// ❌ BAD
public static final String ROLE_STUDENT = "STUDENT";

if (user.getRole().equals("STUDNET")) {  // Typo not caught!
    // won't execute
}
```

---

## 7. Null Safety

Always validate inputs.

```java
// ✅ GOOD
public void setStudentName(String name) {
    if (name == null || name.trim().isEmpty()) {
        throw new IllegalArgumentException("Name cannot be null or empty");
    }
    this.name = name;
}

public int getCourseCount(Student student) {
    if (student == null || student.getCourses() == null) {
        return 0;
    }
    return student.getCourses().size();
}

// ❌ BAD
public void setStudentName(String name) {
    this.name = name;  // Could be null!
}

public int getCourseCount(Student student) {
    return student.getCourses().size();  // NullPointerException!
}
```

---

## 8. String Operations

Use `StringBuilder` in loops.

```java
// ✅ GOOD: StringBuilder in loops
public String generateReport(List<Student> students) {
    StringBuilder report = new StringBuilder();
    report.append("Student Report\n");

    for (Student student : students) {
        report.append(student.getName())
              .append(" - GPA: ")
              .append(student.getGPA())
              .append("\n");
    }
    return report.toString();
}

// ❌ BAD: String concatenation in loop
public String generateReport(List<Student> students) {
    String report = "";
    for (Student student : students) {
        report += student.getName() + " - " + student.getGPA() + "\n";
        // Creates new String object each time - slow!
    }
    return report;
}

// ✅ OK: Single concatenation (no loop)
String message = "Hello, " + name + "!";  // This is fine
```

---

## 9. Comments & Documentation

Write JavaDoc for public methods, comment complex logic.

```java
/**
 * Calculates student's GPA based on completed courses.
 *
 * @param studentId unique identifier
 * @return GPA (0.0 to 4.0), or 0.0 if no courses completed
 * @throws StudentNotFoundException if student not found
 */
public double calculateGPA(String studentId) throws StudentNotFoundException {
    Student student = findStudent(studentId);

    if (student == null) {
        throw new StudentNotFoundException("Student not found: " + studentId);
    }

    List<Course> completed = student.getCompletedCourses();

    if (completed.isEmpty()) {
        return 0.0;
    }

    double totalPoints = 0.0;
    int totalCredits = 0;

    // Calculate weighted average based on credits
    for (Course course : completed) {
        totalPoints += course.getGrade() * course.getCredits();
        totalCredits += course.getCredits();
    }

    return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
}
```

### Comment Guidelines

```java
// ✅ GOOD: Explain WHY
// Check prerequisites before enrollment to prevent course sequence errors
if (hasPrerequisites(student, course)) {
    enrollStudent(student, course);
}

// ❌ BAD: Stating the obvious
// This if checks if student has prerequisites
if (hasPrerequisites(student, course)) {
    enrollStudent(student, course);
}
```

---

## 10. Resource Management

Always close resources using try-with-resources.

```java
// ✅ GOOD: Try-with-resources
public void saveToFile(String filename, String content) {
    try (FileWriter writer = new FileWriter(filename);
         BufferedWriter buffer = new BufferedWriter(writer)) {

        buffer.write(content);
        // Automatically closed, even if exception occurs

    } catch (IOException e) {
        System.err.println("Failed to save: " + e.getMessage());
    }
}

// ❌ BAD: Forgot to close
public void saveToFile(String filename, String content) {
    FileWriter writer = null;
    try {
        writer = new FileWriter(filename);
        writer.write(content);
    } catch (IOException e) {
        e.printStackTrace();
    }
    // Forgot to close - resource leak!
}
```

---

## Quick Reference

### Top 10 Rules

1. **camelCase** for methods/variables, **PascalCase** for classes
2. **One class, one job** - Single responsibility
3. **Private fields** with getters/setters
4. **Handle exceptions** - No empty catch blocks
5. **Meaningful names** - `studentName` not `sn`
6. **Validate inputs** - Check for null
7. **Use constants** - No magic numbers
8. **Comment complex logic** - Explain WHY
9. **Close resources** - Try-with-resources
10. **Test before PR** - Run your code

### Common Mistakes

```java
// ❌ DON'T
public void calculate_gpa() { }        // snake_case
public String studentName;             // public field
catch (Exception e) { }                // empty catch
if (credits > 18) { }                  // magic number
for (...) { str += x; }                // concatenate in loop
student.getName().length();            // no null check
```

---

See [CONTRIBUTING.md](CONTRIBUTING.md) for Git workflow
