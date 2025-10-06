```mermaid
classDiagram
    %% Enumerations
    class UserRole {
        <<enumeration>>
        ADMIN
        INSTRUCTOR
        STUDENT
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
        ACCOUNT_ACTIVATION
        ACCOUNT_DEACTIVATION
        PASSWORD_RESET
        RECOVERY_PLAN
        RECOVERY_REMINDER
        RECOVERY_GRADING
        PERFORMANCE_REPORT
        ELIGIBILITY_ALERT
        COURSE_ASSIGNMENT
        ENROLLMENT_CONFIRMATION
    }

    class EmailStatus {
        <<enumeration>>
        SENT
        PENDING
        FAILED
    }

    class EligibilityStatus {
        <<enumeration>>
        ELIGIBLE
        NOT_ELIGIBLE
        PROBATION
    }

    %% Abstract User Class
    class User {
        <<abstract>>
        -String userId
        -UserRole role
        -String roleId
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
        -String adminId
        +addUser(User user) void
        +updateUser(User user) void
        +deactivateUser(String userId) void
        +viewAllUsers() List~User~
        +generateSystemReports() void
    }

    class Instructor {
        -String instructorId
        -String instructorName
        -List~Course~ assignedCourses
        +createRecoveryPlan(Student student, Course course, String task) RecoveryPlan
        +updateRecoveryPlan(RecoveryPlan plan) void
        +trackProgress(RecoveryPlan plan) void
        +gradeRecovery(Grade grade) void
        +getAssignedCourses() List~Course~
        +assignCourse(Course course) void
    }

    %% Student Class
    class Student {
        -String studentId
        -String firstName
        -String lastName
        -String major
        -String email
        -String status
        -int year
        -int semester
        -boolean retake
        -List~Grade~ grades
        -List~Result~ results
        -List~RecoveryPlan~ recoveryPlans
        +calculateCGPA() double
        +getFailedCourses() List~Course~
        +isEligibleForProgression() boolean
        +getFullName() String
        +addGrade(Grade grade) void
        +getMajorRequiredCourses() List~Course~
    }

    %% Course Class
    class Course {
        -String courseId
        -String courseName
        -int credits
        -int semester
        -int capacity
        -String instructorId
        -List~RecoveryCourseAction~ recoveryActions
        +getCourseDetails() String
        +hasAvailableSeats() boolean
        +getInstructor() Instructor
        +addRecoveryAction(RecoveryCourseAction action) void
    }

    %% Semester Class
    class Semester {
        -String semesterId
        -String semesterName
        +toString() String
    }

    %% Major Course Mapping
    class MajorCourse {
        -String major
        -int year
        -int semester
        -String courseId
        +getCourse() Course
        +isRequiredForMajor(String major, int year, int semester) boolean
    }

    %% Major Student Mapping
    class MajorStudent {
        -String studentId
        -String major
        -int year
        -int semester
        -boolean retake
        +needsRetake() boolean
        +getStudent() Student
    }

    %% Grade Class
    class Grade {
        -String gradeId
        -String studentId
        -String courseId
        -String courseName
        -int attemptNumber
        -String component
        -String grade
        -double gradePoint
        -GradeStatus status
        -String semesterId
        -String instructorId
        +calculateGradePoint() double
        +isPassed() boolean
        +isFailed() boolean
        +getComponent() ComponentType
    }

    %% Result Class
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
        +isPassed() boolean
        +needsRecovery() boolean
        +getFailedComponents() List~ComponentType~
    }

    %% Recovery Plan
    class RecoveryPlan {
        -String studentId
        -String courseId
        -String instructorId
        -String currentTask
        -RecoveryStatus status
        -String notes
        -Date startDate
        -Date endDate
        +updateTask(String task) void
        +updateStatus(RecoveryStatus status) void
        +addNotes(String notes) void
        +getProgress() String
        +isCompleted() boolean
        +getStudent() Student
        +getCourse() Course
        +getInstructor() Instructor
    }

    %% Recovery Course Action
    class RecoveryCourseAction {
        -String courseId
        -int actionNumber
        -String actionDescription
        -String notes
        +getActionDetails() String
        +toString() String
    }

    %% Eligibility Class
    class Eligibility {
        -String studentId
        -String semesterId
        -double cgpa
        -int failedCoursesCount
        -EligibilityStatus status
        +checkEligibility() boolean
        +updateStatus() void
        +meetsMinimumCGPA() boolean
        +hasExcessiveFailures() boolean
    }

    %% Email Notification
    class EmailNotification {
        -String emailId
        -String recipientId
        -String recipientEmail
        -String subject
        -String body
        -Date sentDate
        -EmailStatus status
        -NotificationType notificationType
        +send() void
        +markAsSent() void
        +markAsFailed() void
        +getRecipient() User
    }

    %% Login Log
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
        -Student student
        -String semester
        -String year
        -List~Grade~ grades
        -List~Result~ results
        -double semesterGPA
        -double cumulativeCGPA
        -Date generatedDate
        -String filePath
        +generateReport() String
        +exportToPDF() void
        +calculateSemesterGPA() double
        +calculateCGPA() double
        +includeGrade(Grade grade) void
    }

    %% Services and Utilities
    class AuthenticationService {
        -static AuthenticationService instance
        -List~LoginLog~ loginLogs
        -AuthenticationService()
        +static getInstance() AuthenticationService
        +authenticate(String email, String password) User
        +createLoginLog(User user) LoginLog
        +logout(String loginId) void
        +validateSession(String userId) boolean
        +getLoginHistory(String userId) List~LoginLog~
    }

    class EmailNotificationService {
        -static EmailNotificationService instance
        -String smtpHost
        -int smtpPort
        -List~EmailNotification~ emailQueue
        -EmailNotificationService()
        +static getInstance() EmailNotificationService
        +sendEmail(EmailNotification email) void
        +sendNotification(User user, NotificationType type, String content) void
        +sendRecoveryPlanNotification(Student student, RecoveryPlan plan) void
        +sendEligibilityNotification(Student student, Eligibility eligibility) void
        +sendReportNotification(Student student, AcademicReport report) void
        +sendPasswordResetNotification(User user, String token) void
    }

    class ReportGenerator {
        -static ReportGenerator instance
        -ReportGenerator()
        +static getInstance() ReportGenerator
        +generateAcademicReport(Student student, String semester, String year) AcademicReport
        +exportReportToPDF(AcademicReport report) String
        +generateEligibilityReport(List~Eligibility~ eligibilities) String
    }

    class EligibilityChecker {
        -static double MIN_CGPA = 2.0
        -static int MAX_FAILED_COURSES = 3
        +static checkEligibility(Student student, Semester semester) Eligibility
        +static getFailedCoursesCount(Student student) int
        +static getIneligibleStudents(List~Student~ students) List~Student~
        +static calculateCGPA(List~Grade~ grades) double
    }

    class CGPACalculator {
        +static calculateCGPA(List~Grade~ grades) double
        +static calculateSemesterGPA(List~Grade~ grades, String semesterId) double
        +static getTotalCreditHours(List~Grade~ grades) int
        +static getTotalGradePoints(List~Grade~ grades) double
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
        +loadRecoveryPlan(String studentId, String courseId) RecoveryPlan
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

    class EligibilityDAO {
        -FileManager fileManager
        +saveEligibility(Eligibility eligibility) void
        +loadEligibility(String studentId, String semesterId) Eligibility
        +loadAllEligibilities() List~Eligibility~
        +updateEligibility(Eligibility eligibility) void
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

    class MajorCourseDAO {
        -FileManager fileManager
        +saveMajorCourse(MajorCourse mc) void
        +loadCoursesByMajor(String major, int year, int semester) List~Course~
        +loadAllMajorCourses() List~MajorCourse~
    }

    class MajorStudentDAO {
        -FileManager fileManager
        +saveMajorStudent(MajorStudent ms) void
        +loadMajorStudent(String studentId) MajorStudent
        +loadAllMajorStudents() List~MajorStudent~
        +updateMajorStudent(MajorStudent ms) void
    }

    %% Relationships - Core Domain
    User <|-- Admin : extends
    User <|-- Instructor : extends
    User --> UserRole : has
    User --> LoginLog : creates

    Admin --> User : manages
    Admin --> EligibilityChecker : uses

    Instructor --> RecoveryPlan : creates/manages
    Instructor --> Course : assigned to
    Instructor --> Grade : grades

    Student --> MajorStudent : has profile
    Student "1" --> "*" Grade : has
    Student "1" --> "*" Result : has
    Student "1" --> "*" RecoveryPlan : has
    Student --> Eligibility : has

    Course "1" --> "*" Grade : enrolled in
    Course "1" --> "*" MajorCourse : part of
    Course "1" --> "*" RecoveryCourseAction : has actions
    Course --> Instructor : taught by

    MajorCourse --> Course : refers to
    MajorStudent --> Student : refers to

    Grade --> Student : belongs to
    Grade --> Course : for
    Grade --> Semester : in
    Grade --> Instructor : graded by
    Grade --> GradeEnum : has
    Grade --> ComponentType : type
    Grade --> GradeStatus : status

    Result --> Student : belongs to
    Result --> Course : for
    Result --> Semester : in
    Result --> GradeStatus : status

    RecoveryPlan --> Student : for
    RecoveryPlan --> Course : for
    RecoveryPlan --> Instructor : managed by
    RecoveryPlan --> RecoveryStatus : has

    RecoveryCourseAction --> Course : for

    Eligibility --> Student : for
    Eligibility --> Semester : in
    Eligibility --> EligibilityStatus : has

    EmailNotification --> User : sent to
    EmailNotification --> NotificationType : type
    EmailNotification --> EmailStatus : has

    LoginLog --> User : for

    AcademicReport --> Student : for
    AcademicReport "1" --> "*" Grade : includes
    AcademicReport "1" --> "*" Result : includes

    %% Relationships - Services
    AuthenticationService --> LoginLog : manages
    AuthenticationService --> User : authenticates

    EmailNotificationService --> EmailNotification : sends
    EmailNotificationService --> NotificationType : uses

    ReportGenerator --> AcademicReport : generates
    ReportGenerator --> Student : for

    EligibilityChecker --> Student : checks
    EligibilityChecker --> Eligibility : creates

    CGPACalculator --> Grade : calculates from

    %% Relationships - Data Access
    UserDAO --> FileManager : uses
    StudentDAO --> FileManager : uses
    InstructorDAO --> FileManager : uses
    CourseDAO --> FileManager : uses
    GradeDAO --> FileManager : uses
    ResultDAO --> FileManager : uses
    RecoveryPlanDAO --> FileManager : uses
    RecoveryCourseActionDAO --> FileManager : uses
    EligibilityDAO --> FileManager : uses
    EmailNotificationDAO --> FileManager : uses
    LoginLogDAO --> FileManager : uses
    SemesterDAO --> FileManager : uses
    MajorCourseDAO --> FileManager : uses
    MajorStudentDAO --> FileManager : uses

    UserDAO --> User : persists
    StudentDAO --> Student : persists
    InstructorDAO --> Instructor : persists
    CourseDAO --> Course : persists
    GradeDAO --> Grade : persists
    ResultDAO --> Result : persists
    RecoveryPlanDAO --> RecoveryPlan : persists
    RecoveryCourseActionDAO --> RecoveryCourseAction : persists
    EligibilityDAO --> Eligibility : persists
    EmailNotificationDAO --> EmailNotification : persists
    LoginLogDAO --> LoginLog : persists
    SemesterDAO --> Semester : persists
    MajorCourseDAO --> MajorCourse : persists
    MajorStudentDAO --> MajorStudent : persists

    %% Service Dependencies
    Admin --> EmailNotificationService : uses
    Instructor --> EmailNotificationService : uses
    ReportGenerator --> EmailNotificationService : uses
    EligibilityChecker --> EmailNotificationService : uses
```
