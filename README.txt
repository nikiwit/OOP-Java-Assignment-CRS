COURSE RECOVERY SYSTEM - SETUP GUIDE
=====================================

PREREQUISITES
-------------
- JDK 8 or higher installed
- Terminal/Command Prompt access

LIBRARY INSTALLATION
--------------------
All required libraries are included in the lib/ folder:

1. javax.mail.jar     - For email notifications
2. itextpdf-5.5.13.3.jar - For PDF report generation
3. activation.jar     - JavaMail dependency

No additional installation needed - just ensure the lib/ folder is present with these JAR files before compiling.

If libraries are missing, download from:
- JavaMail: https://javaee.github.io/javamail/
- iText PDF: https://github.com/itext/itextpdf/releases/tag/5.5.13.3

COMPILE
-------
Navigate to project root folder, then run:

    javac -cp "lib/*" -d bin src/**/*.java

RUN
---
macOS/Linux:
    java -cp "bin:lib/*" Main

Windows:
    java -cp "bin;lib/*" Main

PROJECT STRUCTURE
-----------------
src/
  models/       - Data classes (Student, Course, User, Grade)
  dao/          - Data access layer (file operations)
  services/     - Business logic (authentication, reports)
  gui/          - Swing UI (LoginFrame, dashboards)
  enums/        - Constants (UserRole, GradeStatus)
  utils/        - Helpers (FileManager)

lib/            - External libraries (mail, PDF)
data/           - Text-based database files
config/         - Email configuration
reports/        - Generated PDF reports

LIBRARIES USED
--------------
- javax.mail.jar     (Email notifications)
- itextpdf-5.5.13.3  (PDF report generation)
- activation.jar     (JavaMail dependency)

APPLICATION FLOW
----------------
1. Run Main.java -> Opens LoginFrame
2. Login as Admin or Instructor
3. Admin: Manage users, courses
4. Instructor: Manage students, grades, recovery plans
