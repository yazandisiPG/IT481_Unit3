import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;


public class DB {
	
	//Connection object
	private Connection conn = null;
	
	//Database URL to connect
	private String dbURL;
	
	//Default constructor
	public DB () {
		
		 dbURL =
                "jdbc:sqlserver://LAPTOP-SFGHK0FI\\SQLEXPRESS;"
                        + "database=Northwind;"
                        + "user=sa;"
                        + "password=123456;"
                        + "encrypt=false;"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;";
	}
	
	
	//Constructor taking a string connect request
	public DB(String connection) {
	
			dbURL = connection;
		 
	}
	
	//Method for querying for the customer count
	public String getCustomerCount() {
		
		String count = null;
		 
        try {
            conn = DriverManager.getConnection(dbURL);
            
            if (conn != null) {
            	
            	String countQuery = "select count(*) as count from customers;";
            	Statement stmt = conn.createStatement();
            	ResultSet rs = stmt.executeQuery(countQuery);
            	
            	while ( rs.next() ) {
                   count = rs.getString("count");
                }
               
            }
 
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        
        return count;
	}
	
	
	public String getCustomerNameById(String customerId) {
	    String customerName = null;

	    try {
	        conn = DriverManager.getConnection(dbURL);

	        if (conn != null) {
	            String nameQuery = "SELECT CompanyName FROM Customers WHERE CustomerID = ?;";
	            PreparedStatement stmt = conn.prepareStatement(nameQuery);
	            stmt.setString(1, customerId);
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                customerName = rs.getString("CompanyName");
	            }
	        }

	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    } finally {
	        try {
	            if (conn != null && !conn.isClosed()) {
	                conn.close();
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }

	    return customerName;
	}


	//Method for getting and returning the company names
	public String[] getCompanyNames() {
	    List<String> companyNamesList = new ArrayList<>();

	    try {
	        conn = DriverManager.getConnection(dbURL);

	        if (conn != null) {
	            String countQuery = "select companyname from customers;";
	            Statement stmt = conn.createStatement();
	            ResultSet rs = stmt.executeQuery(countQuery);

	            while (rs.next()) {
	                companyNamesList.add(rs.getString("companyname"));
	            }
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    } finally {
	        try {
	            if (conn != null && !conn.isClosed()) {
	                conn.close();
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }

	    return companyNamesList.toArray(new String[0]);
	}
	
	
	public String[] getCustomerLastNames() {
	    List<String> customerLastNamesList = new ArrayList<>();

	    try {
	        conn = DriverManager.getConnection(dbURL);

	        if (conn != null) {
	            String lastNameQuery = "SELECT ContactName FROM Customers;";
	            Statement stmt = conn.createStatement();
	            ResultSet rs = stmt.executeQuery(lastNameQuery);

	            while (rs.next()) {
	                String contactName = rs.getString("ContactName");
	                String[] nameParts = contactName.split(" ");
	                String lastName = nameParts.length > 1 ? nameParts[nameParts.length - 1] : "";
	                customerLastNamesList.add(lastName);
	            }
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    } finally {
	        try {
	            if (conn != null && !conn.isClosed()) {
	                conn.close();
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }

	    return customerLastNamesList.toArray(new String[0]);
	}

	

}