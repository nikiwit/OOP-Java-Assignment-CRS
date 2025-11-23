import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

import gui.InstructorDashboard;
import dao.InstructorDAO;
import models.Instructor;
import java.util.List;
import gui.LoginFrame;

/**
 * Main entry point for the Course Recovery System (CRS) application.
 * Initializes the GUI and starts the application.
 * This is a Java-based OOP system for managing student course recovery,
 * academic progression eligibility, and performance reporting.
 */
public class Main {

    /**
     * Main method to launch the application.
     * Loads the first instructor from instructors.txt and displays their dashboard.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}



// FOR INSTRUCTOR TESTING DASHBOARD  ONLY
// // Load the first instructor from the data file
        // InstructorDAO instructorDAO = new InstructorDAO();
        // List<Instructor> instructors = instructorDAO.loadAllInstructors();

        // if (instructors.isEmpty()) {
        //     System.err.println("No instructors found in instructors.txt");
        //     JOptionPane.showMessageDialog(null,
        //         "No instructors found in the system.",
        //         "Error",
        //         JOptionPane.PLAIN_MESSAGE);
        //     return;
        // }

        // // Get the first instructor (skip header line)
        // Instructor firstInstructor = instructors.get(0);

        // System.out.println("Loading dashboard for: " + firstInstructor.getInstructorName() +
        //                   " (ID: " + firstInstructor.getUserId() + ")");

        // // Launch the GUI on the Event Dispatch Thread
        // SwingUtilities.invokeLater(() -> {
        //     new InstructorDashboard(firstInstructor.getInstructorName(),
        //                            firstInstructor.getUserId());
        // });
