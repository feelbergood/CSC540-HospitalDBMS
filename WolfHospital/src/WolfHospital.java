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

	//fhy
	private static PreparedStatement prep_getAllTreatmentRecords;
	private static PreparedStatement prep_getTreatmentRecord;

	//private static PreparedStatement prep_updateTreatmentRecord;
	private static PreparedStatement prep_updateTreatmentEndDate;
	private static PreparedStatement prep_updateTreatmentPrescription;
	private static PreparedStatement prep_updateTreatmentDiagnosisDetails;

	private static PreparedStatement prep_addTestRecord;
	private static PreparedStatement prep_getAllTestRecords;
	private static PreparedStatement prep_getTestRecord;

	//private static PreparedStatement prep_updateTestRecord;
	private static PreparedStatement prep_updateTestEndDate;
	private static PreparedStatement prep_updateTestTestType;
	private static PreparedStatement prep_updateTestTestResult;

	private static PreparedStatement prep_addCheckinRecord;
	private static PreparedStatement prep_getAllCheckinRecords;
	private static PreparedStatement prep_getCheckinRecord;
	
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

			//fhy
			//	1
			//	SELECT * FROM `Medical Records` m JOIN `Treatment` t ON m.recordID=t.recordID WHERE patientID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Treatment` t ON m.recordID=t.recordID WHERE patientID=?;";
			prep_getAllTreatmentRecords = connection.prepareStatement(sql);
			//	2
			//	SELECT * FROM `Medical Records` m JOIN `Treatment` t ON m.recordID=t.recordID WHERE t.recordID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Treatment` t ON m.recordID=t.recordID WHERE t.recordID=?;";
			prep_getTreatmentRecord = connection.prepareStatement(sql);
			//	3
			//	UPDATE `Medical Records` SET `end date` = '2020-01-01' WHERE recordID = 13;
			//	UPDATE `Treatment` SET `prescription` = 'Use', `diagnosisDetails` = 'Muscle' WHERE recordID = '13';
			sql =
				"UPDATE `Medical Records`"+
				"SET `end date` = ?"+
				"WHERE recordID = ?;"+;
			prep_updateTreatmentEndDate = connection.prepareStatement(sql);

			sql =
				"UPDATE `Treatment`"+
				"SET `prescription` = ?"+
				"WHERE recordID = ?;";
			prep_updateTreatmentPrescription = connection.prepareStatement(sql);

			sql =
				"UPDATE `Treatment`"+
				"SET `diagnosisDetails` = ?"+
				"WHERE recordID = ?;";
			prep_updateTreatmentDiagnosisDetails = connection.prepareStatement(sql);

			//	4
			//	INSERT INTO `Test` (`recordID`, `testType`, `testResult`)VALUES ('14', 'testType5', 'testResult5');
			//	INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`, `endDate`, `responsibleDoctor`) VALUES ('14', '5', '2019-07-01', '2019-07-02', '3');
			sql =
				"INSERT INTO `Test` (`recordID`, `testType`, `testResult`)"+
				"VALUES (?, ?, ?);"+
				"INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`, `endDate`, `responsibleDoctor`)"+
				"VALUES (?, ?, ?, ?, ?);";
			prep_addTestRecord = connection.prepareStatement(sql);
			//	5
			//	SELECT * FROM `Medical Records` m JOIN `Test` t ON m.recordID=t.recordID WHERE patientID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Test` t ON m.recordID=t.recordID WHERE patientID=?;";
			prep_getAllTestRecords = connection.prepareStatement(sql);
			//	6
			//	SELECT * FROM `Medical Records` m JOIN `Test` t ON m.recordID=t.recordID WHERE t.recordID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Test` t ON m.recordID=t.recordID WHERE t.recordID=?;";
			prep_getTestRecord = connection.prepareStatement(sql);
			//	7
			//	UPDATE `Medical Records` SET `end date` = '2020-01-01' WHERE recordID=14;
			// 	UPDATE `Test` SET `testType` = 'Influenza B Rapid Assay', `testResult` = 'Influenza B Antigen value: positive, ref range: negative' WHERE recordID = '14';
			sql =
					"UPDATE `Medical Records`"+
					"SET `end date` = ?"+
					"WHERE recordID= ?;";
			prep_updateTestEndDate = connection.prepareStatement(sql);
			sql =
					"UPDATE `Test`"+
					"SET `testType` = ?"+
					"WHERE recordID = ?;"
			prep_updateTestTestType = connection.prepareStatement(sql);
			sql =
					"UPDATE `Test`"+
					"SET `testResult` = ?"+
					"WHERE recordID = ?;"
			prep_updateTestTestResult = connection.prepareStatement(sql);
			//	8
			//	INSERT INTO `Check-ins` (`recordID`, `wardNumber`, `bedNumber`)VALUES ('15', NULL, NULL);
			//	INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`, `endDate`, `responsibleDoctor`	) VALUES ('15', '5', '2019-07-01', '2019-07-07', '4');
			sql =
					"INSERT INTO `Check-ins` (`recordID`, `wardNumber`, `bedNumber`)"+
					"VALUES (?, ?, ?);"+
					"INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`, `endDate`, `responsibleDoctor`)"+
					"VALUES (?, ?, ?, ?, ?);";
			prep_addCheckinRecord = connection.prepareStatement(sql);
			//	9
			//	SELECT * FROM `Medical Records` m JOIN `Check-ins` c ON m.recordID=c.recordID WHERE patientID=1;
			sql ="SELECT * FROM `Medical Records` m JOIN `Check-ins` c ON m.recordID=c.recordID WHERE patientID=?;";
			prep_getAllCheckinRecords = connection.prepareStatement(sql);
			//	10
			//	SELECT * FROM `Medical Records` m JOIN `Check-ins` c ON m.recordID=c.recordID WHERE c.recordID=1;
			sql ="SELECT * FROM `Medical Records` m JOIN `Check-ins` c ON m.recordID=c.recordID WHERE c.recordID=?;";
			prep_getCheckinRecord = connection.prepareStatement(sql);
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

				//fhy: Medical Records, Treatment, Test, Check-ins
				statement.executeUpdate(
						"CREATE TABLE `Medical Records` ("+
						"`recordID` VARCHAR(255) NOT NULL UNIQUE,"+
						"`patientID` VARCHAR(255) NOT NULL,"+
						"`startDate` DATETIME NOT NULL,"+
						"`endDate` DATETIME DEFAULT NULL,"+
						"`responsibleDoctor` VARCHAR(255) NOT NULL,"+
						"PRIMARY KEY (`recordID`)"+
						");");
				statement.executeUpdate(
						"CREATE TABLE `Treatment` ("+
						"`recordID` VARCHAR(255) NOT NULL UNIQUE,"+
						"`prescription` VARCHAR(255) NOT NULL,"+
						"`diagnosisDetails` VARCHAR(255) NOT NULL,"+"PRIMARY KEY (`recordID`)"
						+");");
				statement.executeUpdate(
						"CREATE TABLE `Test` (" +
						"`recordID` VARCHAR(255) NOT NULL UNIQUE," +
						"`testType` VARCHAR(255) NOT NULL," +
						"`testResult` VARCHAR(255) NOT NULL," +
						"PRIMARY KEY (`recordID`)" +
						");");
				statement.executeUpdate(
						"CREATE TABLE `Check-ins` (" +
						"`recordID` VARCHAR(255) NOT NULL UNIQUE," +
						"`wardNumber` VARCHAR(255) DEFAULT NULL," +
						"`bedNumber` VARCHAR(255) DEFAULT NULL," +
						"PRIMARY KEY (`recordID`)" +
						");");

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
					//fhy: Medical Records(along with other tables), Treatment, Test, Check-ins
					case "Treatment":
						//manageTreatmentRecordAdd() should be done by other teammates
						break;
					case "Test":
						//INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`, `endDate`, `responsibleDoctor`	)
						// VALUES ('5', '1', '2019-03-01', '2019-03-02', '3');
						//INSERT INTO `Test` (`recordID`, `testType`, `testResult`)
						// VALUES ('5', 'DPC POC Urinalysis Chemical', 'Protein, Urinalysis value:2+, ref range:negative');
						manageTestRecordAdd(5, "DPC POC Urinalysis Chemical", "Protein, Urinalysis value:2+, ref range:negative", 1, "2019-03-01", "2019-03-02", 3);
						break;
					case "Check-ins":
						//INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`, `endDate`, `responsibleDoctor`	)
						// VALUES ('9', '1', '2019-03-01', '2019-03-07', '13');
						//INSERT INTO `Check-ins` (`recordID`, `wardNumber`, `bedNumber`)
						// VALUES ('9', '1', '2');
						manageCheckinRecordAdd(9, 1, 2, 1, "2019-03-01", "2019-03-07", 13);
						break;

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

	//fhy support_printQueryResultSet, error_handler not yet implemented
	//1
	public static boolean showAllTreatmentRecords(int patientID){
		boolean success = false;

		try {
			prep_getAllTreatmentRecords.setInt(1, patientID);
			result = prep_getAllTreatmentRecords.executeQuery();

			if (result.next()) {
				success = true;
				result.beforeFirst();
			}

			System.out.println("\nshowAllTreatmentRecords\n");
//			support_printQueryResultSet(result);

		}
		catch (Throwable err) {
			error_handler(err);
		}

		return success;
	}
	//2
	public static boolean showTreatmentRecord(int recordID){
		boolean success = false;

		try {
			prep_getTreatmentRecord.setInt(1, recordID);
			result = prep_getTreatmentRecord.executeQuery();

			if (result.next()) {
				success = true;
				result.beforeFirst();
			}

			System.out.println("\nshowTreatmentRecord\n");
//			support_printQueryResultSet(result);

		}
		catch (Throwable err) {
			error_handler(err);
		}

		return success;
	}
	//3
	public static void manageTreatmentUpdate(int recordID, String attributeToChange, String valueToChange){
		try {
			connection.setAutoCommit(true);
			switch (attributeToChange.toUpperCase()){

				case "END DATE":
				//should it be "`END DATE`"?
					prep_updateTreatmentEndDate.setString(1, valueToChange);
					prep_updateTreatmentEndDate.setInt(2, recordID);
					prep_updateTreatmentEndDate.executeUpdate();
					break;
				case "PRESCRIPTION":
					prep_updateTreatmentPrescription.setString(1, valueToChange);
					prep_updateTreatmentPrescription.setInt(2, recordID);
					prep_updateTreatmentPrescription.executeUpdate();
					break;
				case "DIAGNOSISDETAILS":
					prep_updateTreatmentDiagnosisDetails.setString(1, valueToChange);
					prep_updateTreatmentDiagnosisDetails.setInt(2, recordID);
					prep_updateTreatmentDiagnosisDetails.executeUpdate();
					break;
				default:
					System.out.println("\nCannot update the '" + attributeToChange);
					break;
			}
		}
		catch (Throwable err) {
//			error_handler(err);
		}
	}
	//4
	public static void manageTestRecordAdd(int recordID, String testType, String testResult, int patientID, String startDate, String endDate, int responsibleDoctor){
		//to be done: check success or not and report
		try {

			// Start transaction
			connection.setAutoCommit(false);

			try {
				prep_addTestRecord.setInt(1, recordID);
				prep_addTestRecord.setString(2, testType);
				prep_addTestRecord.setString(3, testResult);
				prep_addTestRecord.setInt(4, recordID);
				prep_addTestRecord.setInt(5, patientID);
				prep_addTestRecord.setString(6, startDate);
				prep_addTestRecord.setString(7, endDate);
				prep_addTestRecord.setString(8, responsibleDoctor);
				prep_addTestRecord.executeUpdate();
				connection.commit();
			}
			catch (Throwable err) {

				// Handle error
//				error_handler(err);

				// Roll back the entire transaction
				connection.rollback();

			}
			finally {
				// Restore normal auto-commit mode
				connection.setAutoCommit(true);
			}
		}
		catch (Throwable err) {
//			error_handler(err);
		}
	}
	//5
	public static boolean showAllTestRecords(int patientID){
		boolean success = false;

		try {
			prep_getAllTestRecords.setInt(1, patientID);
			result = prep_getAllTestRecords.executeQuery();

			if (result.next()) {
				success = true;
				result.beforeFirst();
			}

			System.out.println("\nshowAllTestRecords\n");
//			support_printQueryResultSet(result);

		}
		catch (Throwable err) {
//			error_handler(err);
		}

		return success;
	}
	//6
	public static boolean showTestRecord(int recordID){
		boolean success = false;

		try {
			prep_getTestRecord.setInt(1, recordID);
			result = prep_getTestRecord.executeQuery();

			if (result.next()) {
				success = true;
				result.beforeFirst();
			}

			System.out.println("\nshowTestRecord\n");
//			support_printQueryResultSet(result);

		}
		catch (Throwable err) {
//			error_handler(err);
		}

		return success;
	}
	//7
	public static void manageTestUpdate(int recordID, String attributeToChange, String valueToChange){
		try {
			connection.setAutoCommit(true);
			switch (attributeToChange.toUpperCase()){

				case "END DATE":
					//should it be "`END DATE`"?
					prep_updateTestEndDate.setString(1, valueToChange);
					prep_updateTestEndDate.setInt(2, recordID);
					prep_updateTestEndDate.executeUpdate();
					break;
				case "TESTTYPE":
					prep_updateTestTestType.setString(1, valueToChange);
					prep_updateTestTestType.setInt(2, recordID);
					prep_updateTestTestType.executeUpdate();
					break;
				case "TESTRESULT":
					prep_updateTestTestResult.setString(1, valueToChange);
					prep_updateTestTestResult.setInt(2, recordID);
					prep_updateTestTestResult.executeUpdate();
					break;
				default:
					System.out.println("\nCannot update the '" + attributeToChange);
					break;
			}
		}
		catch (Throwable err) {
//			error_handler(err);
		}
	}
	//8
	public static void manageCheckinRecordAdd(int recordID, int wardNumber, int bedNumber, int patientID, String startDate, String endDate, int responsibleDoctor){
		//to be done: check success or not and report
		try {

			// Start transaction
			connection.setAutoCommit(false);

			try {
				prep_addCheckinRecord.setInt(1, recordID);
				prep_addCheckinRecord.setInt(2, wardNumber);
				prep_addCheckinRecord.setInt(3, bedNumber);
				prep_addCheckinRecord.setInt(4, recordID);
				prep_addCheckinRecord.setInt(5, patientID);
				prep_addCheckinRecord.setInt(6, patientID);
				prep_addCheckinRecord.setString(6, startDate);
				prep_addCheckinRecord.setString(7, endDate);
				prep_addCheckinRecord.setInt(8, responsibleDoctor);
				prep_addCheckinRecord.executeUpdate();
				connection.commit();
			}
			catch (Throwable err) {

				// Handle error
//				error_handler(err);

				// Roll back the entire transaction
				connection.rollback();

			}
			finally {
				// Restore normal auto-commit mode
				connection.setAutoCommit(true);
			}
		}
		catch (Throwable err) {
//			error_handler(err);
		}
	}
	//9
	public static boolean showAllCheckinRecords(int patientID){
		boolean success = false;

		try {
			prep_getAllCheckinRecords.setInt(1, patientID);
			result = prep_getAllCheckinRecords.executeQuery();

			if (result.next()) {
				success = true;
				result.beforeFirst();
			}

			System.out.println("\nshowAllCheckinRecords\n");
//			support_printQueryResultSet(result);

		}
		catch (Throwable err) {
//			error_handler(err);
		}

		return success;
	}
	//10
	public static boolean showCheckinRecord(int recordID){
		boolean success = false;

		try {
			prep_getCheckinRecord.setInt(1, recordID);
			result = prep_getCheckinRecord.executeQuery();

			if (result.next()) {
				success = true;
				result.beforeFirst();
			}

			System.out.println("\nshowCheckinRecord\n");
//			support_printQueryResultSet(result);

		}
		catch (Throwable err) {
//			error_handler(err);
		}

		return success;
	}

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
