# Course Registration System (CRS)

> OOP Java Assignment | Team Size: 5 Members

Course Registration System managing student enrollment, eligibility checking, course recovery, and notifications.

## Quick Start

```bash
# Clone and setup
git clone <repo-url>
cd OOP-Java-Assignment-CRS
git checkout dev
git pull origin dev

# Create your feature branch
git checkout -b feature/your-feature-name

# After making changes
git add .
git commit -m "Brief description of what you did"
git push origin feature/your-feature-name
```

## Branch Structure

```
main         → Final submission code
dev          → Integration branch
feature/*    → Your feature work (feature/swing-gui, feature/user-management, etc.)
```

## Workflow

1. **Start work**: Pull latest from `dev`, create/switch to your feature branch
2. **Code**: Make changes, test locally
3. **Commit**: Clear message describing what you did
4. **Push**: Push to your feature branch
5. **PR**: Create Pull Request from your feature → `dev` on GitHub
6. **Merge**: After approval, merge and delete branch

## Naming Conventions

```java
public class StudentRecord {              // Classes: PascalCase
    private static final int MAX = 6;     // Constants: UPPER_SNAKE_CASE
    private String studentName;           // Variables: camelCase

    public void calculateGPA() { }        // Methods: camelCase
}
```

## Before You Code

See [CODING_STANDARDS.md](CODING_STANDARDS.md) and [PACKAGE_GUIDE.md](PACKAGE_GUIDE.md) for details.

These guides explain which folders to put your code in (`models/`, `services/`, `dao/`, etc.) and how everything works together. Don't skip this - it will save you (and essentially ours) time!

## Project Structure

```
OOP-Java-Assignment-CRS/
├── data/                     # .txt and .dat files (NO database)
├── docs/                     # Diagrams, screenshots
├── lib/                      # javax.mail.jar, itextpdf.jar
├── reports/                  # Generated PDFs
├── src/                      # Java source files
│   ├── dao/                  # Data Access Objects (CRUD operations)
│   ├── enums/                # Enumerations (Grade, UserRole, etc.)
│   ├── gui/                  # Swing UI components
│   ├── models/               # Student, Course, Enrollment, User, etc.
│   ├── services/             # Email, PDF, Authentication, CGPA calculation
│   ├── utils/                # Helper utilities (FileManager, etc.)
│   └── Main.java             # Application entry point
├── .gitignore                # Git ignore rules
├── class_diagram.md          # System class diagram
├── CODING_STANDARDS.md       # Java coding conventions
├── CONTRIBUTING.md           # Git workflow guide
├── PACKAGE_GUIDE.md          # Project structure justification
└── README.md                 # Project documentation
```

**Not sure where your new class goes?** Check [PACKAGE_GUIDE.md](PACKAGE_GUIDE.md)

**Not sure how to write it?** [CODING_STANDARDS.md](CODING_STANDARDS.md)

## Technologies

- Java SE + Swing GUI
- JavaMail API (email notifications)
- iText PDF (report generation)
- File I/O (text/binary storage)

## Running the Project

```bash
# Compile
javac -cp "lib/*" -d bin src/**/*.java

# Run (macOS/Linux)
java -cp "bin:lib/*" Main

# Run (Windows)
java -cp "bin;lib/*" Main
```

## Merge Conflicts?

1. Open the conflicting file
2. Find markers: `<<<<<<< HEAD` / `=======` / `>>>>>>>`
3. Keep the correct code, remove markers
4. `git add .` → `git commit -m "Resolve conflict"`

## Rules

- **Never push to `main`** - use feature branches
- **Test before PR** - run your code locally
- **Clear commits** - "Added login validation" not "update"
- **Pull before push** - avoid conflicts

---

## Documentation

- [PACKAGE_GUIDE.md](PACKAGE_GUIDE.md) - **Start here!** Learn where to put your code
- [CODING_STANDARDS.md](CODING_STANDARDS.md) - Java naming and style conventions
- [CONTRIBUTING.md](CONTRIBUTING.md) - Git workflow and collaboration guide
