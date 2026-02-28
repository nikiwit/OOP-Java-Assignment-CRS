package gui;
import models.User;        
import dao.UserDAO;        
import dao.LoginLogDAO;    
import models.LoginLog; 
import utils.TableUtils;



import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * LoginHistoryPanel
 * ------------------
 * This panel displays all login/logout history in a table format.
 *
 * OOP Concepts Used:
 *  - Encapsulation: private fields (table, model, logDAO, userDAO)
 *  - Abstraction: DAO classes hide file operations (binary storage)
 *  - Modularity: A separate panel class used inside AdminDashboard
 *  - Reusability: Filtering logic can be reused easily
 *
 * Part of: Authentication & Authorization (Login/Logout Tracking)
 */
public class LoginHistoryPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> userFilterBox;

    private LoginLogDAO logDAO;
    private UserDAO userDAO;

    public LoginHistoryPanel() {

        this.logDAO = new LoginLogDAO();
        this.userDAO = new UserDAO();

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // ==============================================================
        //                     NORTH PANEL – USER FILTER
        // ==============================================================
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.WHITE);

        JLabel filterLabel = new JLabel("Filter by User:");
        filterPanel.add(filterLabel);

        userFilterBox = new JComboBox<String>();
        userFilterBox.addItem("ALL");

        // Add all users to dropdown
        List<User> allUsers = userDAO.loadAllUsers();

        // SORT USER IDs IN ASCENDING ORDER
        Collections.sort(allUsers, new Comparator<User>() {
            public int compare(User u1, User u2) {
                return u1.getUserId().compareTo(u2.getUserId());
            }
        });

        // Add sorted users to combo box
        for (int i = 0; i < allUsers.size(); i++) {
            User u = allUsers.get(i);
            userFilterBox.addItem(u.getUserId());
        }


        // Action listener for filter changes
        userFilterBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadTableData();   // reload table when filter changes
            }
        });

        filterPanel.add(userFilterBox);
        add(filterPanel, BorderLayout.NORTH);

        // ==============================================================
        //                          TABLE SETUP
        // ==============================================================
        String[] columns = {
                "LoginID",
                "UserID",
                "Login Time",
                "Logout Time",
                "Duration (ms)",
                "Login (Binary)",
                "Logout (Binary)"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setRowHeight(30);
        
        // Adjusting column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(20);   // LoginID
        table.getColumnModel().getColumn(1).setPreferredWidth(30);   // UserID
        table.getColumnModel().getColumn(2).setPreferredWidth(90);   // Login Time
        table.getColumnModel().getColumn(3).setPreferredWidth(90);   // Logout Time
        table.getColumnModel().getColumn(4).setPreferredWidth(60);   // Duration (ms)
        table.getColumnModel().getColumn(5).setPreferredWidth(45);   // Login Binary
        table.getColumnModel().getColumn(6).setPreferredWidth(45);   // Logout Binary

        // To Center-align the Duration (ms) column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Load all logs initially
        loadTableData();
        TableUtils.centerAlignTable(table);

    }

   

    /**
     * Load table data based on selected user filter.
     */
    private void loadTableData() {

    try {
        model.setRowCount(0); // clear table

        String selectedUser = (String) userFilterBox.getSelectedItem();
        List<LoginLog> logs;

        // Load all logs OR only specific user logs
        if (selectedUser.equals("ALL")) {
            logs = logDAO.loadAllLoginLogs();
        } else {
            logs = logDAO.loadLogsByUser(selectedUser);
        }

        // ================================
        // SORTING: NEWEST LOGIN FIRST
        // ================================
        Collections.sort(logs, new Comparator<LoginLog>() {
            public int compare(LoginLog l1, LoginLog l2) {

                // Handle null timestamps safely
                if (l1.getLoginTimestamp() == null && l2.getLoginTimestamp() == null)
                    return 0;
                if (l1.getLoginTimestamp() == null)
                    return 1;     // null goes last
                if (l2.getLoginTimestamp() == null)
                    return -1;

                // DESCENDING ORDER → latest first
                return l2.getLoginTimestamp().compareTo(l1.getLoginTimestamp());
            }
        });

        // FORMATTER FOR DISPLAY
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // ADD SORTED DATA INTO TABLE
        for (int i = 0; i < logs.size(); i++) {
            LoginLog log = logs.get(i);

            String loginTime;
            if (log.getLoginTimestamp() != null) {
                loginTime = sdf.format(log.getLoginTimestamp());
            } else {
                loginTime = "-";
            }

            String logoutTime;
            if (log.getLogoutTimestamp() != null) {
                logoutTime = sdf.format(log.getLogoutTimestamp());
            } else {
                logoutTime = "-";
            }

            model.addRow(new Object[]{
                    log.getLoginId(),
                    log.getUserId(),
                    loginTime,
                    logoutTime,
                    log.getSessionDuration(),
                    log.getBinaryLoginTimestamp(),
                    log.getBinaryLogoutTimestamp()
            });
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(
                this,
                "Error loading login history:\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
        ex.printStackTrace();
    }
}

}
