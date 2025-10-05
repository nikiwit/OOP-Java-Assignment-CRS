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

See [CODING_STANDARDS.md](CODING_STANDARDS.md) for details.

## Project Structure

```
OOP-Java-Assignment-CRS/
├── data/                     # .txt and .dat files (NO database)
├── docs/                     # Diagrams, screenshots
├── lib/                      # javax.mail.jar, itextpdf.jar
├── reports/                  # Generated PDFs
├── src/                      # Java source files
│   ├── enums/                # Enumerations (Grade, UserRole, etc.)
│   ├── filemanager/          # File I/O operations
│   ├── gui/                  # Swing UI components
│   ├── model/                # Student, Course, Enrollment
│   ├── service/              # Email, PDF, validation logic
│   ├── util/                 # Helper utilities, constants
│   └── Main.java             # Application entry point
├── .gitignore                # Git ignore rules
├── class_diagram.md          # System class diagram
├── CODING_STANDARDS.md       # Java coding conventions
├── CONTRIBUTING.md           # Git workflow guide
└── README.md                 # Project documentation
```

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

See [CODING_STANDARDS.md](CODING_STANDARDS.md) before coding | [CONTRIBUTING.md](CONTRIBUTING.md) for detailed workflow
