import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class Unit3 extends JFrame {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Unit3 frame = new Unit3();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private JPanel contentPane;
    private Connection conn;
    private JPanel createPanelWithTable(ResultSet resultSet, String[] columnNames) {
        JPanel panel = new JPanel(new GridBagLayout());
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        DefaultTableModel tableModel = new DefaultTableModel(new Object[][] {}, columnNames);
        try {
            while (resultSet.next()) {
                Object[] row = new Object[columnNames.length];
                for (int i = 0; i < columnNames.length; i++) {
                    row[i] = resultSet.getObject(columnNames[i]);
                }
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table.setModel(tableModel);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 5, 0);
        panel.add(scrollPane, c);

        return panel;
    }

    private JButton createButtonWithAction(String title, String query, String[] columnNames, JPanel parentPanel) {
        JButton button = new JButton(title);
        button.addActionListener(actionEvent -> {
            try {
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                JPanel panel = createPanelWithTable(rs, columnNames);
                JOptionPane.showMessageDialog(parentPanel, panel);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentPanel, "Error retrieving data from database");
            }
        });
        return button;
    }


    public Unit3() {
    	  // create the content pane
        contentPane = new JPanel(new GridBagLayout());

        // create the login panel
        JPanel loginPanel = new JPanel(new GridBagLayout());

        // create the username label and text field
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);

        // create the password label and text field
        JLabel passwordLabel = new JLabel("Password:");
        JTextField passwordField = new JPasswordField(20);

        // create the login button
        JButton loginButton = new JButton("Login");

        // add the components to the login panel using GridBagLayout
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 0, 0);
        loginPanel.add(usernameLabel, c);

        c.gridx = 1;
        loginPanel.add(usernameField, c);

        c.gridx = 0;
        c.gridy = 1;
        loginPanel.add(passwordLabel, c);

        c.gridx = 1;
        loginPanel.add(passwordField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(loginButton, c);

        // add the login panel to the content pane
        contentPane.add(loginPanel);

        // set the content pane of the frame
        setContentPane(contentPane);
        setSize(600, 400);

        // add action listener to the login button
        
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(((JPasswordField) passwordField).getPassword());
            try {
                String dbURL = "jdbc:sqlserver://LAPTOP-SFGHK0FI\\SQLEXPRESS;"
                        + "database=Northwind;"
                        + "encrypt=false;"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;";
                conn = DriverManager.getConnection(dbURL, username, password);
                if (conn != null) {
                    PreparedStatement stmt = conn.prepareStatement("SELECT IS_MEMBER('SalesRole'), IS_MEMBER('HRRole'), IS_MEMBER('CEORole')");
                    ResultSet rs = stmt.executeQuery();
                    boolean isSales = false, isHR = false, isCEO = false;
                    while (rs.next()) {
                        isSales = rs.getBoolean(1);
                        isHR = rs.getBoolean(2);
                        isCEO = rs.getBoolean(3);
                    }
					
					
					if (isSales) {
					    JPanel salesPanel = new JPanel(new GridBagLayout());
					    JButton ordersButton = createButtonWithAction("Orders", "SELECT * FROM Orders", new String[] {"OrderID", "CustomerID", "OrderDate", "RequiredDate"}, salesPanel);
						 
					    JLabel welcomeLabel = new JLabel("Welcome Sales Team! - Yazan's Search Tool");
						    GridBagConstraints salesWelcomeC = new GridBagConstraints();
					    
					    
					    GridBagConstraints salesC = new GridBagConstraints();
					    salesC.gridx = 0;
					    salesC.gridy = 0;
					    salesC.insets = new Insets(0, 0, 5, 0);
					    salesPanel.add(ordersButton, salesC);
					    
					    salesWelcomeC.gridx = 0;
					    salesWelcomeC.gridy = -5;
					    salesWelcomeC.gridwidth = 10;
					    salesWelcomeC.insets = new Insets(10, 0, 10, 0);
					    salesPanel.add(welcomeLabel, salesWelcomeC);
					
					    contentPane.remove(loginPanel);
					    contentPane.add(salesPanel);
					    setContentPane(contentPane);
					
					} else if (isHR) {
						 JLabel welcomeLabel = new JLabel("Welcome HR! - Yazan's Search Tool");
						    GridBagConstraints hrWelcomeC = new GridBagConstraints();
						
					    JPanel hrPanel = new JPanel(new GridBagLayout());
					    JButton employeesButton = createButtonWithAction("Employees", "SELECT * FROM Employees", new String[] {"EmployeeID", "LastName", "FirstName", "Title", "BirthDate", "HireDate"}, hrPanel);
					
					    GridBagConstraints hrC = new GridBagConstraints();
					    hrC.gridx = 0;
					    hrC.gridy = 0;
					    hrC.insets = new Insets(0, 0, 5, 0);
					    hrPanel.add(employeesButton, hrC);
					    
					    hrWelcomeC.gridx = 0;
					    hrWelcomeC.gridy = -5;
					    hrWelcomeC.gridwidth = 10;
					    hrWelcomeC.insets = new Insets(10, 0, 10, 0);
					    hrPanel.add(welcomeLabel, hrWelcomeC);
					
					    contentPane.remove(loginPanel);
					    contentPane.add(hrPanel);
					    setContentPane(contentPane);
					
					}else if (isCEO) {
						
						 JLabel welcomeLabel = new JLabel("Welcome CEO! - Yazan's Search Tool");
						    GridBagConstraints ceoWelcomeC = new GridBagConstraints();
						    
					    JPanel ceoPanel = new JPanel(new GridBagLayout());
					    JButton customersButton = createButtonWithAction("Customers", "SELECT * FROM Customers", new String[] {"CustomerID", "CompanyName", "ContactName", "Country"}, ceoPanel);
					    JButton employeesButton = createButtonWithAction("Employees", "SELECT * FROM Employees", new String[] {"EmployeeID", "LastName", "FirstName", "Title", "BirthDate", "HireDate"}, ceoPanel);
					    JButton ordersButton = createButtonWithAction("Orders", "SELECT * FROM Orders", new String[] {"OrderID", "CustomerID", "OrderDate", "RequiredDate"}, ceoPanel);
					    JButton customerCountButton = createButtonWithAction("Customer Count", "SELECT COUNT(*) as CustomerCount FROM Customers", new String[] {"CustomerCount"}, ceoPanel);

					    JLabel customerIdLabel = new JLabel("Customer ID:");
					    JTextField customerIdField = new JTextField(10);
					    JButton customerLookupButton = new JButton("Lookup Customer");

					    GridBagConstraints ceoC = new GridBagConstraints();
					    ceoC.gridx = 0;
					    ceoC.gridy = 0;
					    ceoC.insets = new Insets(0, 0, 5, 0);
					    ceoPanel.add(customersButton, ceoC);

					    ceoC.gridy = 1;
					    ceoPanel.add(employeesButton, ceoC);

					    ceoC.gridy = 2;
					    ceoPanel.add(ordersButton, ceoC);

					    ceoC.gridy = 3;
					    ceoPanel.add(customerCountButton, ceoC);

					    ceoC.gridy = 4;
					    ceoC.gridx = 0;
					    ceoC.fill = GridBagConstraints.NONE;
					    ceoPanel.add(customerIdLabel, ceoC);

					    ceoC.gridx = 1;
					    ceoPanel.add(customerIdField, ceoC);

					    ceoC.gridy = 5;
					    ceoC.gridx = 0;
					    ceoC.gridwidth = 2;
					    ceoC.fill = GridBagConstraints.HORIZONTAL;
					    ceoPanel.add(customerLookupButton, ceoC);
					    
					    
					    ceoWelcomeC.gridx = 0;
					    ceoWelcomeC.gridy = -5;
					    ceoWelcomeC.gridwidth = 10;
					    ceoWelcomeC.insets = new Insets(10, 0, 10, 0);
					    ceoPanel.add(welcomeLabel, ceoWelcomeC);

					    customerLookupButton.addActionListener(j -> {
					        String customerId = customerIdField.getText();
					        try {
					            PreparedStatement lookUpStmt = conn.prepareStatement("SELECT * FROM Customers WHERE CustomerID = ?");
					            lookUpStmt.setString(1, customerId);
					            ResultSet resSet = lookUpStmt.executeQuery();
					            JPanel panel = createPanelWithTable(resSet, new String[] {"CustomerID", "CompanyName", "ContactName", "Country"});
					            JOptionPane.showMessageDialog(ceoPanel, panel);
					        } catch (Exception ex) {
					            JOptionPane.showMessageDialog(ceoPanel, "Error retrieving data from database");
					        }
					    });

					    contentPane.remove(loginPanel);
					    contentPane.add(ceoPanel);
					    setContentPane(contentPane);
					}

					  } else {
		                JOptionPane.showMessageDialog(contentPane, "Invalid login");
		               }
		                } catch (SQLException ex) {
		                    String errorMessage = "Error connecting to database: " + ex.getMessage().replaceAll("ClientConnectionId:[a-zA-Z0-9\\-]+", "").trim();
		                    JOptionPane.showMessageDialog(contentPane, errorMessage);
		                    ex.printStackTrace();
		                }


		                });
		
					    }
					}

