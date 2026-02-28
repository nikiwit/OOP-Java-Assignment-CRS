# Package Guide - Team Reference

Hey team! 

I've set up our project structure and wanted to explain where everything goes. This will help you know exactly which folder to put your code in. Read this before you start coding!

---

## `src/enums/`

This is where we put fixed lists of values that never change. Think of it like a dropdown menu - you have specific options and that's it.

**I've already created these for you:**
- `UserRole` → ADMIN, INSTRUCTOR, STUDENT
- `GradeEnum` → A, B, C, D, F
- `GradeStatus` → PASSED, FAILED, TRANSIT

**Use enums when:** You have a fixed set of options (like grades, user roles, statuses)

```java
if (user.getRole() == UserRole.ADMIN) {
    // Allow admin access
}
```

---

## `src/models/`

This is where we define the "things" in our system - basically all the nouns (Student, Course, Grade, etc.). Models are like containers that hold data.

**Think of it like:** A blank form with fields to fill in.

**I've set up these models:**
- `Student.java` → Has name, ID, email, grades
- `Course.java` → Has course name, credits, instructor
- `Grade.java` → Has student, course, grade value
- `User.java`, `Admin.java`, `Instructor.java` → User types
- And more...

```java
Student student = new Student();
student.setName("John");
student.setStudentId("S001");
```

**Important rule I want you to follow:** Models should ONLY hold data. Don't put file reading/writing code here - that goes in DAO!

---

## `src/dao/`

This is important! DAO stands for "Data Access Object" - basically the file reading/writing guy (just like we did in python).

**Here's why I separated this:** Instead of mixing file operations with our Student class (messy!), we have dedicated DAO classes that handle ALL file operations. This keeps code clean.

**Think of it like:** A librarian who knows exactly where to find and store books.

**I've created DAOs for:**
- `StudentDAO.java` → Saves/loads students from `students.txt`
- `GradeDAO.java` → Saves/loads grades from `grades.txt`
- `UserDAO.java` → Saves/loads users from `users.txt`
- And one for each data type...

```java
StudentDAO dao = new StudentDAO();
Student student = dao.loadStudent("S001");  // Read from file
dao.saveStudent(student);                    // Write to file
```

**Your job:** When you need to save or load data, use the DAO. Don't write file code directly in models or services!

---

## `src/services/`

This is where the "brains" of our system live - all the calculations, validations, and complex operations.

**Think of it like:** The workers who actually DO things with the data.

**I've set up these services:**
- `AuthenticationService.java` → Handles login/logout, password checks
- `CGPACalculator.java` → Calculates GPA from grades
- `EligibilityChecker.java` → Checks if student can progress (CGPA >= 2.0, ≤3 failed courses)
- `EmailNotificationService.java` → Sends emails (using JavaMail API)
- `ReportGenerator.java` → Creates PDF reports (using iText)

```java
// Example: Check if student is eligible
double cgpa = CGPACalculator.calculateCGPA(student.getGrades());
if (EligibilityChecker.checkEligibility(student)) {
    System.out.println("You can progress to next year!");
}
```

**When to use:** Put your business logic here - calculations, validations, complex workflows.

---

## `src/utils/`

Helper utilities that can be used anywhere in the project.

**Think of it like:** Your toolbox - generic tools that multiple people need.

**I've added:**
- `FileManager.java` → Handles reading/writing text and binary files (DAOs will use this)

```java
FileManager fm = new FileManager();
String data = fm.loadFromTextFile("students.txt");
```

**Add here:** Any helper methods you need across the project (date formatting, string validation, etc.)

---

## `src/gui/`

This is where ALL your Swing code goes - windows, buttons, panels, everything the user sees.

**I've created starter files:**
- `LoginFrame.java` → The login screen
- `AdminDashboard.java` → Admin control panel
- `InstructorDashboard.java` → Instructor interface
- `StudentDashboard.java` → Student view

```java
LoginFrame frame = new LoginFrame();
frame.setVisible(true);
```

**Your job:** Build out these interfaces - add buttons, text fields, tables, etc. Make it look good!

---

## Quick Decision Guide

**Not sure where your new class should go? Ask yourself:**

1. **Is it a fixed list of values?** → `enums/`
   - Example: If you're adding NotificationType, EmailStatus

2. **Is it a "thing" with data fields?** → `models/`
   - Example: If you're adding a new object like Notification, Attendance

3. **Does it read/write files?** → `dao/`
   - Example: If you need to save/load something from a .txt file

4. **Does it do calculations or business logic?** → `services/`
   - Example: If you're calculating something or validating data

5. **Is it a generic helper?** → `utils/`
   - Example: Date formatter, email validator, string helper

6. **Is it a screen/window?** → `gui/`
   - Example: Any Swing frame, panel, dialog

---

## How Everything Works Together

Let me show you the flow - here's what happens when a user logs in:

```
1. User clicks "Login" button
   └─> LoginFrame.java (GUI)

2. GUI calls the authentication service
   └─> AuthenticationService.login(email, password)

3. Service needs to check if user exists
   └─> UserDAO.findByEmail(email) - reads from users.txt

4. Service validates password, creates login log
   └─> Uses User model to store data

5. Service returns the User object back to GUI
   └─> GUI shows the right dashboard (Admin/Instructor/Student)
```

**See the pattern?** GUI → Service → DAO → Files. Then back up.

---

## Rules I Need You to Follow

Please stick to these to keep our code clean:

1. **Models = Data only**
   - Just fields, getters, setters. No file operations!

2. **DAO = File operations only**
   - All the reading/writing .txt files goes here

3. **Services = Business logic**
   - Calculations, validations, complex operations

4. **One class, one job**
   - Don't make a class that does everything

5. **Package names are plural**
   - It's `models/` not `model/`, `services/` not `service/`
   - I already set this up correctly, just follow the pattern

---

## Real Examples to Help You Understand

Let me show you how different files work together for actual features:

### Example 1: "Check if student can progress to next year"

You'll need to use:
- `Student.java` (models) - holds the student's data
- `Eligibility.java` (models) - holds eligibility information
- `GradeDAO.java` (dao) - loads student's grades from grades.txt
- `EligibilityChecker.java` (services) - does the actual checking (CGPA >= 2.0, ≤3 fails)
- `StudentDashboard.java` (gui) - displays "Eligible" or "Not Eligible" to student

### Example 2: "Instructor creates recovery plan for student"

You'll need to use:
- `RecoveryPlan.java` (models) - holds the plan details
- `Student.java` (models) - the student who needs recovery
- `RecoveryPlanDAO.java` (dao) - saves plan to recovery_plans.txt
- `EmailNotificationService.java` (services) - sends email to student
- `InstructorDashboard.java` (gui) - interface where instructor creates the plan

See how each folder has a specific role? That's the point of organizing this way!

---

## Questions?

If you're still confused about where something goes:
1. Read this guide again
2. Check [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md) for detailed info
3. Ask me in the group chat!

Happy coding! 
