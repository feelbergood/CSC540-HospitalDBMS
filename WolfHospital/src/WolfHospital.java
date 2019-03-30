import java.sql.*;
import java.util.Scanner;

public class WolfHospital {
	// Update your user info alone here
	private static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/yrao3"; // Using SERVICE_NAME

	// Update your user and password info here!
	private static final String user = "yrao3";
	private static final String password = "200204773";
	
	// Commands
	// Operators: responsible for admitting patients, processing patient information and assigning beds.
	// Billing staff: responsible for creating billing information.
	// Doctors: treat, test patients and log medical records.
	// Administrators: manage all operations of users above and process staff and hospital information.
	// Patients: Retrieve own billing account, medical records, patient info
	private static final String CMD_MAIN = 						"MAIN";
	private static final String CMD_OPERATORS = 				"OPERATORS";
	private static final String CMD_BILLING = 					"BILLING STAFF";
	private static final String CMD_DOCTORS = 					"DOCTORS";
	private static final String CMD_PATIENTS = 					"PATIENTS";
	private static final String CMD_ADMIN = 					"ADMINISTRATORS";
	private static final String CMD_QUIT = 						"QUIT";

	private static final String CMD_STAFF_ADD = 				"ADD STAFF";
	private static final String CMD_STAFF_GET = 				"RETRIEVE STAFF";
	private static final String CMD_STAFF_UPDATE = 				"UPDATE STAFF";
	private static final String CMD_STAFF_DELETE = 				"DELETE STAFF";
	
	private static final String CMD_PATIENT_ADD = 				"ADD PATIENT";
	private static final String CMD_PATIENT_GET = 				"RETRIEVE PATIENT";
	private static final String CMD_PATIENT_UPDATE = 			"UPDATE PATIENT";
	private static final String CMD_PATIENT_DELETE = 			"DELETE PATIENT";
	
	private static final String CMD_WARD_ADD = 					"ADD WARD";
	private static final String CMD_WARD_GET = 					"RETRIEVE WARD";
	private static final String CMD_WARD_UPDATE = 				"UPDATE WARD";
	private static final String CMD_WARD_DELETE = 				"DELETE WARD";
	private static final String CMD_WARD_CHECK = 					"CHECK AVAILABLE WARD";
	private static final String CMD_WARD_ASSIGN = 				"ASSIGN WARD";
	private static final String CMD_WARD_RESERVE = 				"RESERVE WARD";
	private static final String CMD_WARD_RELEASE = 				"RELEASE WARD";

	private static final String CMD_BED_CHECK = 				"CHECK AVAILABLE BED";
	private static final String CMD_BED_ASSIGN = 				"ASSIGN BED";
	private static final String CMD_BED_RESERVE = 				"RESERVE BED";
	private static final String CMD_BED_RELEASE = 				"RELEASE BED";

	private static final String CMD_TREATMENT_ADD = 			"ADD TREATMENT";
	private static final String CMD_TREATMENT_GETALL = 			"GET ALL TREATMENTS";
	private static final String CMD_TREATMENT_GET = 			"GET A TREATMENT";
	private static final String CMD_TREATMENT_UPDATE = 			"UPDATE TREATMENT";
	private static final String CMD_TEST_ADD = 					"ADD TEST";
	private static final String CMD_TEST_GETALL = 				"GET ALL TESTS";
	private static final String CMD_TEST_GET = 					"GET A TEST";
	private static final String CMD_TEST_UPDATE = 				"UPDATE TEST";
	private static final String CMD_CHECKIN_ADD = 				"ADD CHECK-IN";
	private static final String CMD_CHECKIN_GETALL = 			"GET ALL CHECK-INS";
	private static final String CMD_CHECKIN_GET = 				"GET A CHECK-IN";
	private static final String CMD_CHECKIN_UPDATE = 			"UPDATE CHECK-IN";
	
	private static final String CMD_MEDICAL_HISTORY_REPORT = 	"REPORT MEDICAL HISTORY";
	private static final String CMD_USAGE_STATUS_REPORT = 		"REPORT USAGE STATUS";
	private static final String CMD_PATIENT_NUMBER_REPORT = 	"REPORT PATIENT NUMBER";
	private static final String CMD_WARD_USAGE_PERCENT_REPORT = "REPORT WARD USAGE PERCENTAGE";
	private static final String CMD_DOCTOR_RESPONS_REPORT = 	"REPORT DOCTOR REPONSIBLITIES";

	private static final String CMD_BILLING_ACCT_ADD = 			"ADD BILLING ACCOUNT";
	private static final String CMD_BILLING_ACCT_GET = 			"RETRIEVE BILLING ACCOUNT";
	private static final String CMD_BILLING_ACCT_UPDATE = 		"UPDATE BILLING ACCOUNT";
	private static final String CMD_BILLING_ACCT_DELETE = 		"DELETE BILLING ACCOUNT";

	private static Scanner scanner;
	private static String currentMenu;

	private static Connection connection;
	private static Statement statement;
	private static ResultSet result;
	
	
	// Prepared Statements pre-declared
	// TO-DO 1: instantiate preparedStatements
	// STAFF
	private static PreparedStatement prep_addStaff;
	private static PreparedStatement prep_getStaff;
	private static PreparedStatement prep_updateStaffName;
	private static PreparedStatement prep_updateStaffJobTitle;
	private static PreparedStatement prep_updateStaffProfTitle;
	private static PreparedStatement prep_updateStaffDepart;
	private static PreparedStatement prep_updateStaffPhone;
	private static PreparedStatement prep_updateStaffAddress;
	private static PreparedStatement prep_deleteStaff;
	
	// ... Remaining statements

	// Establish connection
	public static void connectToDatabase() {
		try {
	    	// Loading the driver. This creates an instance of the driver
	    	// and calls the registerDriver method to make MySql(MariaDB) Thin available to clients.
		    Class.forName("org.mariadb.jdbc.Driver");
		    connection = null;
		    statement = null;
		    result = null;

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

	public static void printCommands(String menu) {
		System.out.println(menu);
		System.out.println("Available Commands:");
		switch (menu) {
			case CMD_MAIN:
				System.out.println(CMD_OPERATORS);
				System.out.println(CMD_BILLING);
				System.out.println(CMD_DOCTORS);
				System.out.println(CMD_PATIENTS);
				System.out.println(CMD_ADMIN);
				System.out.println(CMD_QUIT);
				break;
			case CMD_OPERATORS:
				System.out.println();
				break;
			case CMD_BILLING:
				System.out.println();
				break;
			case CMD_DOCTORS:
				System.out.println();
				break;
			case CMD_PATIENTS:
				System.out.println();
				break;
			case CMD_ADMIN:
				System.out.println();
				break;
		}
	}

	// TO-DO 2: assign instantiated prepared statements
	public static void generatePreparedStatements() {
		try {
			String sql;

			sql = "INSERT INTO Staff";
			prep_addStaff = connection.prepareStatement(sql);
			sql = "SELECT * FROM Staff";
			prep_getStaff = connection.prepareStatement(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// TO-DO 3: create tables
	public static void generateTables() {
		try {
			connection.setAutoCommit(false);
			try {
				// Staff: 
				statement.executeUpdate("CREATE TABLE Staff");
				// Other tables...
								

				connection.commit();
				System.out.println("Tables created!");
			} catch (SQLException e) {
				connection.rollback();
				e.printStackTrace();
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	// TO-DO 4: define and implement table population tables
	public static void populateTables(String tableName) {
		try {
			connection.setAutoCommit(false);
			try {
				switch(tableName) {
					// Staff:
					case "Staff":
						prep_addStaff.setString(1, "xxx");
						break;
					// Other tables...

					default:
						break;
				}

				connection.commit();
				System.out.println("Tables populated!");
			} catch (SQLException e) {
				connection.rollback();
				e.printStackTrace();
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	// TO-DO 5: define and implement other functions
	// To get staff info
	public static void getStaff(String staffID) {
		
	}
	
	// Other functions...

	public static void main(String[] args) {
		printCommands(CMD_MAIN);
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
