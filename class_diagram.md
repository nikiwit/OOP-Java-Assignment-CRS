```mermaid
classDiagram
    %% Enumerations
    class UserRole {
        <<enumeration>>
        ADMIN
        INSTRUCTOR
    }

    class GradeEnum {
        <<enumeration>>
        A_PLUS
        A
        A_MINUS
        B_PLUS
        B
        B_MINUS
        C_PLUS
        C
        C_MINUS
        D_PLUS
        D
        F
        +getGradePoint() double
    }

    class ComponentType {
        <<enumeration>>
        ASSIGNMENT
        EXAM
    }

    class GradeStatus {
        <<enumeration>>
        PASSED
        FAILED
        TRANSIT
    }

    class RecoveryStatus {
        <<enumeration>>
        ACTIVE
        COMPLETED
        PENDING
    }

    class NotificationType {
        <<enumeration>>
        ACCOUNT_NOTIFICATION
        PASSWORD_RESET
        RECOVERY_PLAN
        PERFORMANCE_REPORT
    }

    %% Abstract User Class (Only Admin and Instructor can login)
    class User {
        <<abstract>>
        -String userId
        -UserRole role
        -String email
        -String password
        -boolean isActive
        -Date createdAt
        +login(String email, String password) boolean
        +logout() void
        +resetPassword(String newPassword) void
        +updateProfile() void
        +activate() void
        +deactivate() void
    }

    %% User Implementations
    class Admin {
        +addUser(User user) void
        +updateUser(User user) void
        +deactivateUser(String userId) void
        +viewAllUsers() List~User~
        +generateSystemReports() void
        +checkEligibility(Student student) boolean
    }

    class Instructor {
        -String instructorName
        +createRecoveryPlan(Student student, Course course) RecoveryPlan
        +updateRecoveryPlan(RecoveryPlan plan) void
        +trackProgress(RecoveryPlan plan) void
        +gradeRecovery(Grade grade) void
        +getAssignedCourses() List~Course~
    }

    %% Student Class (NOT a User - students don't login)
    class Student {
        -String studentId
        -String firstName
        -String lastName
        -String major
        -int year
        -int semester
        -boolean retake
        -String email
        -String status
        +calculateCGPA() double
        +getFailedCourses() List~Course~
        +isEligibleForProgression() boolean
        +getFullName() String
        +addGrade(Grade grade) void
        +getGrades() List~Grade~
        +getResults() List~Result~
        +getRecoveryPlans() List~RecoveryPlan~
    }

    %% Course Class
    class Course {
        -String courseId
        -String courseName
        -int credits
        -int capacity
        -String instructorId
        +getCourseDetails() String
        +hasAvailableSeats() boolean
        +getInstructor() Instructor
        +getRecoveryActions() List~RecoveryCourseAction~
    }

    %% Semester Class (YYYYMM format: 202501, 202505, 202509)
    class Semester {
        -String semesterId
        -String semesterName
        -Date startDate
        -Date endDate
        +toString() String
        +isCurrentSemester() boolean
    }

    %% Grade Class (component-level: Assignment or Exam)
    class Grade {
        -String gradeId
        -String studentId
        -String courseId
        -String courseName
        -int attemptNumber
        -ComponentType component
        -String grade
        -double gradePoint
        -GradeStatus status
        -String semesterId
        -String instructorId
        -Date gradedDate
        +calculateGradePoint() double
        +isPassed() boolean
        +isFailed() boolean
        +getComponentType() ComponentType
    }

    %% Result Class (course-level: overall result)
    class Result {
        -String resultId
        -String studentId
        -String courseId
        -String courseName
        -int attemptNumber
        -String grade
        -double gradePoint
        -boolean passedExam
        -boolean passedAssignment
        -GradeStatus status
        -String semesterId
        -Date resultDate
        +isPassed() boolean
        +needsRecovery() boolean
        +getFailedComponents() List~ComponentType~
    }

    %% Recovery Plan
    class RecoveryPlan {
        -String planId
        -String studentId
        -String courseId
        -String instructorId
        -int currentActionNumber
        -RecoveryStatus status
        -String notes
        -Date startDate
        -Date targetEndDate
        -Date completedDate
        +updateActionNumber(int actionNumber) void
        +updateStatus(RecoveryStatus status) void
        +addNotes(String notes) void
        +isOverdue() boolean
        +isCompleted() boolean
        +getStudent() Student
        +getCourse() Course
        +getInstructor() Instructor
    }

    %% Recovery Course Action (Templates based on course)
    class RecoveryCourseAction {
        -String courseId
        -int actionNumber
        -String actionDescription
        -String notes
        +getActionDetails() String
        +toString() String
    }

    %% Email Notification
    class EmailNotification {
        -String emailId
        -String recipientId
        -String recipientEmail
        -String subject
        -String body
        -Date sentDate
        -NotificationType notificationType
        +send() void
        +getFormattedMessage() String
    }

    %% Login Log (Binary file storage for timestamps)
    class LoginLog {
        -String loginId
        -String userId
        -Date loginTimestamp
        -Date logoutTimestamp
        -long sessionDuration
        +calculateSessionDuration() long
        +logout(Date logoutTime) void
        +isActive() boolean
        +getUser() User
    }

    %% Academic Report
    class AcademicReport {
        -String reportId
        -String studentId
        -String semesterId
        -double semesterGPA
        -double cumulativeCGPA
        -Date generatedDate
        -String filePath
        +generateReport() String
        +exportToPDF() void
        +calculateSemesterGPA() double
        +calculateCGPA() double
        +getStudent() Student
        +getGrades() List~Grade~
        +getResults() List~Result~
    }

    %% Services and Utilities
    class AuthenticationService {
        <<singleton>>
        -static AuthenticationService instance
        -AuthenticationService()
        +static getInstance() AuthenticationService
        +authenticate(String email, String password) User
        +createLoginLog(User user) LoginLog
        +logout(String loginId) void
        +validateSession(String userId) boolean
        +getLoginHistory(String userId) List~LoginLog~
    }

    class EmailNotificationService {
        <<singleton>>
        -static EmailNotificationService instance
        -String smtpHost
        -int smtpPort
        -EmailNotificationService()
        +static getInstance() EmailNotificationService
        +sendEmail(EmailNotification email) void
        +sendNotification(String recipientId, String email, NotificationType type, String content) void
        +sendRecoveryPlanNotification(Student student, RecoveryPlan plan) void
        +sendPasswordResetNotification(User user, String token) void
    }

    class ReportGenerator {
        <<singleton>>
        -static ReportGenerator instance
        -ReportGenerator()
        +static getInstance() ReportGenerator
        +generateAcademicReport(Student student, String semesterId) AcademicReport
        +exportReportToPDF(AcademicReport report) String
    }

    class EligibilityChecker {
        <<utility>>
        -static double MIN_CGPA = 2.0
        -static int MAX_FAILED_COURSES = 3
        +static boolean checkEligibility(Student student) boolean
        +static int getFailedCoursesCount(Student student) int
        +static List~Student~ getIneligibleStudents(List~Student~ students)
        +static double calculateCGPA(Student student) double
    }

    class CGPACalculator {
        <<utility>>
        +static double calculateCGPA(List~Grade~ grades) double
        +static double calculateSemesterGPA(List~Grade~ grades, String semesterId) double
        +static int getTotalCreditHours(List~Grade~ grades) int
        +static double getTotalGradePoints(List~Grade~ grades) double
    }

    %% Data Access Layer
    class FileManager {
        -String dataDirectory
        +saveToTextFile(String fileName, String data) void
        +loadFromTextFile(String fileName) String
        +saveToBinaryFile(String fileName, Object data) void
        +loadFromBinaryFile(String fileName) Object
        +appendToFile(String fileName, String data) void
        +fileExists(String fileName) boolean
        +parseCSVLine(String line) String[]
    }

    class UserDAO {
        -FileManager fileManager
        +saveUser(User user) void
        +loadUser(String userId) User
        +loadAllUsers() List~User~
        +updateUser(User user) void
        +deleteUser(String userId) void
        +findByEmail(String email) User
    }

    class StudentDAO {
        -FileManager fileManager
        +saveStudent(Student student) void
        +loadStudent(String studentId) Student
        +loadAllStudents() List~Student~
        +updateStudent(Student student) void
        +deleteStudent(String studentId) void
    }

    class InstructorDAO {
        -FileManager fileManager
        +saveInstructor(Instructor instructor) void
        +loadInstructor(String instructorId) Instructor
        +loadAllInstructors() List~Instructor~
        +updateInstructor(Instructor instructor) void
    }

    class CourseDAO {
        -FileManager fileManager
        +saveCourse(Course course) void
        +loadCourse(String courseId) Course
        +loadAllCourses() List~Course~
        +updateCourse(Course course) void
        +loadCoursesByInstructor(String instructorId) List~Course~
    }

    class GradeDAO {
        -FileManager fileManager
        +saveGrade(Grade grade) void
        +loadGrade(String gradeId) Grade
        +loadGradesByStudent(String studentId) List~Grade~
        +loadGradesByCourse(String courseId) List~Grade~
        +updateGrade(Grade grade) void
    }

    class ResultDAO {
        -FileManager fileManager
        +saveResult(Result result) void
        +loadResult(String resultId) Result
        +loadResultsByStudent(String studentId) List~Result~
        +updateResult(Result result) void
    }

    class RecoveryPlanDAO {
        -FileManager fileManager
        +saveRecoveryPlan(RecoveryPlan plan) void
        +loadRecoveryPlan(String planId) RecoveryPlan
        +loadPlansByStudent(String studentId) List~RecoveryPlan~
        +loadPlansByInstructor(String instructorId) List~RecoveryPlan~
        +updateRecoveryPlan(RecoveryPlan plan) void
    }

    class RecoveryCourseActionDAO {
        -FileManager fileManager
        +saveAction(RecoveryCourseAction action) void
        +loadActionsByCourse(String courseId) List~RecoveryCourseAction~
        +loadAllActions() List~RecoveryCourseAction~
    }

    class EmailNotificationDAO {
        -FileManager fileManager
        +saveEmail(EmailNotification email) void
        +loadEmail(String emailId) EmailNotification
        +loadEmailsByRecipient(String recipientId) List~EmailNotification~
        +loadAllEmails() List~EmailNotification~
    }

    class LoginLogDAO {
        -FileManager fileManager
        +saveLoginLog(LoginLog log) void
        +loadLoginLog(String loginId) LoginLog
        +loadLogsByUser(String userId) List~LoginLog~
        +updateLoginLog(LoginLog log) void
    }

    class SemesterDAO {
        -FileManager fileManager
        +saveSemester(Semester semester) void
        +loadSemester(String semesterId) Semester
        +loadAllSemesters() List~Semester~
    }

    %% RELATIONSHIPS WITH PROPER UML NOTATION

    %% Inheritance (Generalization) - hollow triangle
    User <|-- Admin : extends
    User <|-- Instructor : extends

    %% Composition (strong ownership) - filled diamond
    User *-- UserRole : has
    Grade *-- ComponentType : has
    Grade *-- GradeStatus : has
    Grade *-- GradeEnum : uses
    Result *-- GradeStatus : has
    RecoveryPlan *-- RecoveryStatus : has
    EmailNotification *-- NotificationType : has

    %% Aggregation (weak ownership) - hollow diamond
    AcademicReport o-- Student : for
    AcademicReport o-- Grade : includes
    AcademicReport o-- Result : includes

    %% Association - simple line with multiplicity
    Student "1" -- "0..*" Grade : earns
    Student "1" -- "0..*" Result : has
    Student "1" -- "0..*" RecoveryPlan : enrolled in

    Course "1" -- "0..*" Grade : given for
    Course "1" -- "0..*" Result : produces
    Course "1" -- "0..*" RecoveryCourseAction : has templates
    Course "1" -- "1" Instructor : taught by

    Instructor "1" -- "0..*" RecoveryPlan : manages
    Instructor "1" -- "0..*" Grade : assigns

    Grade "1" -- "1" Semester : in
    Grade "1" -- "1" Instructor : graded by

    Result "1" -- "1" Semester : in

    RecoveryPlan "1" -- "1" Student : for
    RecoveryPlan "1" -- "1" Course : for
    RecoveryPlan "1" -- "1" Instructor : managed by

    %% Dependency (uses) - dashed arrow
    User ..> LoginLog : creates
    Admin ..> User : manages
    Admin ..> EligibilityChecker : uses
    EmailNotification ..> Student : can notify

    %% Service Dependencies
    AuthenticationService ..> LoginLog : manages
    AuthenticationService ..> User : authenticates
    EmailNotificationService ..> EmailNotification : sends
    ReportGenerator ..> AcademicReport : generates
    ReportGenerator ..> Student : uses
    EligibilityChecker ..> Student : evaluates
    CGPACalculator ..> Grade : calculates from

    %% Services use EmailNotificationService
    Admin ..> EmailNotificationService : uses
    Instructor ..> EmailNotificationService : uses
    ReportGenerator ..> EmailNotificationService : uses

    %% Data Access Layer - Aggregation (DAOs aggregate FileManager)
    UserDAO o-- FileManager : uses
    StudentDAO o-- FileManager : uses
    InstructorDAO o-- FileManager : uses
    CourseDAO o-- FileManager : uses
    GradeDAO o-- FileManager : uses
    ResultDAO o-- FileManager : uses
    RecoveryPlanDAO o-- FileManager : uses
    RecoveryCourseActionDAO o-- FileManager : uses
    EmailNotificationDAO o-- FileManager : uses
    LoginLogDAO o-- FileManager : uses
    SemesterDAO o-- FileManager : uses

    %% DAO Dependencies on Domain Objects
    UserDAO ..> User : persists
    StudentDAO ..> Student : persists
    InstructorDAO ..> Instructor : persists
    CourseDAO ..> Course : persists
    GradeDAO ..> Grade : persists
    ResultDAO ..> Result : persists
    RecoveryPlanDAO ..> RecoveryPlan : persists
    RecoveryCourseActionDAO ..> RecoveryCourseAction : persists
    EmailNotificationDAO ..> EmailNotification : persists
    LoginLogDAO ..> LoginLog : persists
    SemesterDAO ..> Semester : persists
```
