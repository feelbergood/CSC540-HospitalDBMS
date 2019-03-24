import java.sql.*;

public class WolfHospital {
	// Update your user info alone here
	private static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/yrao3"; // Using SERVICE_NAME

	// Update your user and password info here!
	private static final String user = "yrao3";
	private static final String password = "200204773";

	private static final int CMD_MAIN = 1;
	private static final int CMD_OPERATORS = 2;
	private static final int CMD_BILLING = 3;
	private static final int CMD_DOCTORS = 4;
	private static final int CMD_PATIENTS = 5;
	private static final int CMD_ADMIN = 6;

//	Operators: responsible for admitting patients, processing patient information and assigning beds.
//	Billing staff: responsible for creating billing information.
//	Doctors: treat, test patients and log medical records.
//	Administrators: manage all operations of users above and process staff and hospital information.
//	Patients: Retrieve own billing account, medical records, patient info

	private static final int CMD_ADD_STAFF = 7;
	private static final int CMD_GET_STAFF = 8;
	private static final int CMD_UPDATE_STAFF = 9;
	private static final int CMD_DELETE_STAFF = 10;
	
	private static final int CMD_ADD_PATIENT = 11;
	private static final int CMD_GET_PATIENT = 12;
	private static final int CMD_UPDATE_PATIENT = 13;
	private static final int CMD_DELETE_PATIENT = 14;
	
	private static final int CMD_ADD_WARD = 15;
	private static final int CMD_GET_WARD = 16;
	private static final int CMD_UPDATE_WARD = 17;
	private static final int CMD_DELETE_WARD = 18;

	private static final int CMD_ADD_BED = 19;
	private static final int CMD_GET_BED = 20;
	private static final int CMD_UPDATE_BED = 21;
	private static final int CMD_DELETE_BED = 22;
	
	
	public static void main(String[] args) {
		try {
	    	// Loading the driver. This creates an instance of the driver
	    	// and calls the registerDriver method to make MySql(MariaDB) Thin available to clients.
		    Class.forName("org.mariadb.jdbc.Driver");
		    Connection connection = null;
		    Statement statement = null;
		    ResultSet result = null;

		    try {
		        // Get a connection instance from the first driver in the
		        // DriverManager list that recognizes the URL jdbcURL
		        connection = DriverManager.getConnection(jdbcURL, user, password);

		        // Create a statement instance that will be sending
		        // your SQL statements to the DBMS
		        statement = connection.createStatement();
		    } finally {
		        close(result);
		        close(statement);
		        close(connection);
		    }
		} catch(Throwable oops) {
			oops.printStackTrace();
		}
	}
	
	static void close(Connection connection) {
	    if(connection != null) {
	        try {
	            connection.close();
	        } catch(Throwable whatever) {}
	    }
	}
	     
	static void close(Statement statement) {
	    if(statement != null) {
	        try {
	            statement.close();
	        } catch(Throwable whatever) {}
	    }
	}
	     
	static void close(ResultSet result) {
	   if(result != null) {
	       try {
	           result.close();
	       } catch(Throwable whatever) {}
	   }
	}
}
