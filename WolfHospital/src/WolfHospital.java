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
	// Staff
	private static PreparedStatement prep_addStaff;
	private static PreparedStatement prep_getStaff;
	private static PreparedStatement prep_updateStaffName;
	private static PreparedStatement prep_updateStaffJobTitle;
	private static PreparedStatement prep_updateStaffProfTitle;
	private static PreparedStatement prep_updateStaffDepart;
	private static PreparedStatement prep_updateStaffPhone;
	private static PreparedStatement prep_updateStaffAddress;
	private static PreparedStatement prep_deleteStaff;

	// fhy
	// Medical Records - Treatment
	private static PreparedStatement prep_getAllTreatmentRecords;
	private static PreparedStatement prep_getTreatmentRecord;

	// private static PreparedStatement prep_updateTreatmentRecord;
	private static PreparedStatement prep_updateTreatmentEndDate;
	private static PreparedStatement prep_updateTreatmentPrescription;
	private static PreparedStatement prep_updateTreatmentDiagnosisDetails;

	// Medical Records - Test
	private static PreparedStatement prep_addTestRecord;
	private static PreparedStatement prep_getAllTestRecords;
	private static PreparedStatement prep_getTestRecord;

	// private static PreparedStatement prep_updateTestRecord;
	private static PreparedStatement prep_updateTestEndDate;
	private static PreparedStatement prep_updateTestTestType;
	private static PreparedStatement prep_updateTestTestResult;

	// Medical Records - Check-in
	private static PreparedStatement prep_addCheckinRecord;
	private static PreparedStatement prep_getAllCheckinRecords;
	private static PreparedStatement prep_getCheckinRecord;
	
	// Yudong RAO
	private static PreparedStatement prep_updateCheckinEndDate;
	private static PreparedStatement prep_updateCheckinWard;
	private static PreparedStatement prep_updateCheckinBed;

	// Reports
	private static PreparedStatement prep_reportPatientMedicalHistory;
	private static PreparedStatement prep_reportCurrentUsageStatus;
	private static PreparedStatement prep_reportNumberOfPatients;
	private static PreparedStatement prep_reportWardUsagePercentage;
	private static PreparedStatement prep_reportDoctorResponsiblity;

	// Billing Accounts
	private static PreparedStatement prep_addBillingAccount;
	private static PreparedStatement prep_getBillingAccount;
	private static PreparedStatement prep_updateBillingAccountSSN;
	private static PreparedStatement prep_updateBillingAccountAddress;
	private static PreparedStatement prep_updateBillingAccountPaymentInfo;
	private static PreparedStatement prep_updateBillingAccountBillingRecords;
	private static PreparedStatement prep_updateBillingAccountVisitDate;
	private static PreparedStatement prep_deleteBillingAccount;

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

			//fhy
			//	Get all treatment records
			//	SELECT * FROM `Medical Records` m JOIN `Treatment` t ON m.recordID=t.recordID WHERE patientID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Treatment` t ON m.recordID=t.recordID WHERE patientID=?;";
			prep_getAllTreatmentRecords = connection.prepareStatement(sql);

			//	Get treatment record
			//	SELECT * FROM `Medical Records` m JOIN `Treatment` t ON m.recordID=t.recordID WHERE t.recordID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Treatment` t ON m.recordID=t.recordID WHERE t.recordID=?;";
			prep_getTreatmentRecord = connection.prepareStatement(sql);

			//	Update treatment record
			//	UPDATE `Medical Records` SET `end date` = '2020-01-01' WHERE recordID = 13;
			//	UPDATE `Treatment` SET `prescription` = 'Use', `diagnosisDetails` = 'Muscle' WHERE recordID = '13';
			sql = "UPDATE `Medical Records`" +
				"SET `end date` = ?" +
				"WHERE recordID = ?;";
			prep_updateTreatmentEndDate = connection.prepareStatement(sql);

			sql = "UPDATE `Treatment`" +
				"SET `prescription` = ?" +
				"WHERE recordID = ?;";
			prep_updateTreatmentPrescription = connection.prepareStatement(sql);

			sql = "UPDATE `Treatment`" +
				"SET `diagnosisDetails` = ?" +
				"WHERE recordID = ?;";
			prep_updateTreatmentDiagnosisDetails = connection.prepareStatement(sql);

			//	Create new test record
			//	INSERT INTO `Test` (`recordID`, `testType`, `testResult`)VALUES ('14', 'testType5', 'testResult5');
			//	INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`, `endDate`, `responsibleDoctor`) VALUES ('14', '5', '2019-07-01', '2019-07-02', '3');
			sql = "INSERT INTO `Test` (`recordID`, `testType`, `testResult`)" +
				"VALUES (?, ?, ?);" +
				"INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`, `endDate`, `responsibleDoctor`)" +
				"VALUES (?, ?, ?, ?, ?);";
			prep_addTestRecord = connection.prepareStatement(sql);

			//	Get all test records
			//	SELECT * FROM `Medical Records` m JOIN `Test` t ON m.recordID=t.recordID WHERE patientID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Test` t ON m.recordID=t.recordID WHERE patientID=?;";
			prep_getAllTestRecords = connection.prepareStatement(sql);

			//	Get test record
			//	SELECT * FROM `Medical Records` m JOIN `Test` t ON m.recordID=t.recordID WHERE t.recordID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Test` t ON m.recordID=t.recordID WHERE t.recordID=?;";
			prep_getTestRecord = connection.prepareStatement(sql);

			//	Update test record
			//	UPDATE `Medical Records` SET `end date` = '2020-01-01' WHERE recordID=14;
			// 	UPDATE `Test` SET `testType` = 'Influenza B Rapid Assay', `testResult` = 'Influenza B Antigen value: positive, ref range: negative' WHERE recordID = '14';
			sql = "UPDATE `Medical Records`" +
				"SET `end date` = ?" +
				"WHERE recordID= ?;";
			prep_updateTestEndDate = connection.prepareStatement(sql);

			sql = "UPDATE `Test`" +
				"SET `testType` = ?" +
				"WHERE recordID = ?;";
			prep_updateTestTestType = connection.prepareStatement(sql);

			sql = "UPDATE `Test`" +
				"SET `testResult` = ?" +
				"WHERE recordID = ?;";
			prep_updateTestTestResult = connection.prepareStatement(sql);

			//	Create check-in record
			//	INSERT INTO `Check-ins` (`recordID`, `wardNumber`, `bedNumber`)VALUES ('15', NULL, NULL);
			//	INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`, `endDate`, `responsibleDoctor`	) VALUES ('15', '5', '2019-07-01', '2019-07-07', '4');
			sql = "INSERT INTO `Check-ins` (`recordID`, `wardNumber`, `bedNumber`)" +
				"VALUES (?, ?, ?);" +
				"INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`, `endDate`, `responsibleDoctor`)" +
				"VALUES (?, ?, ?, ?, ?);";
			prep_addCheckinRecord = connection.prepareStatement(sql);

			//	Get all check-in records
			//	SELECT * FROM `Medical Records` m JOIN `Check-ins` c ON m.recordID=c.recordID WHERE patientID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Check-ins` c ON m.recordID=c.recordID WHERE patientID=?;";
			prep_getAllCheckinRecords = connection.prepareStatement(sql);

			//	Get check-in record
			//	SELECT * FROM `Medical Records` m JOIN `Check-ins` c ON m.recordID=c.recordID WHERE c.recordID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Check-ins` c ON m.recordID=c.recordID WHERE c.recordID=?;";
			prep_getCheckinRecord = connection.prepareStatement(sql);
			
			// Yudong
			// Update check-in records
			sql = "";
			prep_updateCheckinEndDate = connection.prepareStatement(sql);

			sql = "";
			prep_updateCheckinWard = connection.prepareStatement(sql);

			sql = "";
			prep_updateCheckinBed = connection.prepareStatement(sql);

			// Report patient history
			sql = "";
			prep_reportPatientMedicalHistory = connection.prepareStatement(sql);

			// Report usage status
			sql = "";
			prep_reportCurrentUsageStatus = connection.prepareStatement(sql);

			// Report number of patients per month
			sql = "";
			prep_reportNumberOfPatients = connection.prepareStatement(sql);

			// Report ward usage percentage
			sql = "";
			prep_reportWardUsagePercentage = connection.prepareStatement(sql);

			// Report doctor responsibility
			sql = "";
			prep_reportDoctorResponsiblity = connection.prepareStatement(sql);

			// Create billing account
			sql = "";
			prep_addBillingAccount = connection.prepareStatement(sql);

			// Get billing account
			sql = "";
			prep_getBillingAccount = connection.prepareStatement(sql);

			// Update billing account
			sql = "";
			prep_updateBillingAccountSSN = connection.prepareStatement(sql);

			sql = "";
			prep_updateBillingAccountAddress = connection.prepareStatement(sql);

			sql = "";
			prep_updateBillingAccountPaymentInfo = connection.prepareStatement(sql);

			sql = "";
			prep_updateBillingAccountBillingRecords = connection.prepareStatement(sql);

			sql = "";
			prep_updateBillingAccountVisitDate = connection.prepareStatement(sql);

			// Delete billing account
			sql = "";
			prep_deleteBillingAccount = connection.prepareStatement(sql);

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
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS Staff");
				// Other tables...

				//fhy: Medical Records, Treatment, Test, Check-ins
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `Medical Records` (" +
						"`recordID` VARCHAR(255) NOT NULL UNIQUE," +
						"`patientID` VARCHAR(255) NOT NULL," +
						"`startDate` DATETIME NOT NULL," +
						"`endDate` DATETIME DEFAULT NULL," +
						"`responsibleDoctor` VARCHAR(255) NOT NULL," +
						"PRIMARY KEY (`recordID`)" +
						"FOREIGN KEY (`patientID`) REFERENCES Patients(`patientID`)" +
						"FOREIGN KEY (`responsibleDoctor`) REFERENCES Staff(`staffID`)" +
						");");
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `Treatment` (" +
						"`recordID` VARCHAR(255) NOT NULL UNIQUE," +
						"`prescription` VARCHAR(255) NOT NULL," +
						"`diagnosisDetails` VARCHAR(255) NOT NULL," +
						"PRIMARY KEY (`recordID`)" +
						"FOREIGN KEY (`recordID`) REFERENCES `Medical Records`(`recordID`)" +
						");");
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `Test` (" +
						"`recordID` VARCHAR(255) NOT NULL UNIQUE," +
						"`testType` VARCHAR(255) NOT NULL," +
						"`testResult` VARCHAR(255) NOT NULL," +
						"PRIMARY KEY (`recordID`)" +
						"FOREIGN KEY (`recordID`) REFERENCES `Medical Records`(`recordID`)" +
						");");
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `Check-ins` (" +
						"`recordID` VARCHAR(255) NOT NULL UNIQUE," +
						"`wardNumber` VARCHAR(255) DEFAULT NULL," +
						"`bedNumber` VARCHAR(255) DEFAULT NULL," +
						"PRIMARY KEY (`recordID`)" +
						"FOREIGN KEY (`recordID`) REFERENCES `Medical Records`(`recordID`)" +
						"FOREIGN KEY (`wardNumber`) REFERENCES Wards(`ward number`)" +
						");");

				// Yudong
				// Billing accounts
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS IF NOT EXISTS `Billing Accounts` (" +
						"`accountID` VARCHAR(255) NOT NULL UNIQUE," +
						"`patientID` VARCHAR(255) NOT NULL," +
						"`visitDate` datetime NOT NULL," +
						"`payerSSN` VARCHAR(255) NOT NULL," +
						"`paymentMethod` VARCHAR(255) NOT NULL," +
						"`cardNumber` VARCHAR(255) DEFAULT NULL" +
						"`registrationFee` DOUBLE NOT NULL" +
						"`medicationPrescribed` BIT DEFAULT NULL" +
						"`accommandation fee` DOUBLE NOT NULL" +
						"PRIMARY KEY (`accountID`)" +
						"FOREIGN KEY (`patientID`) REFERENCES Patients(`patientID`)" +
						"FOREIGN KEY (`payerSSN`) REFERENCES PayerInfo(`SSN`)" +
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
					//demo data: assuming Medical Records #1 #2 are Treatment, #3 is Test, #4 with 2 different doctors, consist of one test(by 103) and one treatment(by 105)
					//the only front desk staff is 104, so all check-ins are assumed to be done by 104
					//question: is empty string the correct way to deal with empty value of endDate?
					case "Treatment":
						//manageTreatmentRecordAdd() should be done by other teammates
						break;
					case "Test":
						//INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`, `endDate`, `responsibleDoctor`	)
						// VALUES ('5', '1', '2019-03-01', '2019-03-02', '3');
						//INSERT INTO `Test` (`recordID`, `testType`, `testResult`)
						// VALUES ('5', 'DPC POC Urinalysis Chemical', 'Protein, Urinalysis value:2+, ref range:negative');
						//manageTestRecordAdd(5, "DPC POC Urinalysis Chemical", "Protein, Urinalysis value:2+, ref range:negative", 1, "2019-03-01", "2019-03-02", 3);
						manageTestRecordAdd(3, "test", "prescription nervine, diagnosis details Hospitalization", 1003, "2019-03-15", "", 100);
						manageTestRecordAdd(4, "test", "prescription analgesic, diagnosis details Surgeon, Hospitalization", 1004, "2019-03-17", "2019-03-21", 103);
						break;
					case "Check-ins":
						//INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`, `endDate`, `responsibleDoctor`	)
						// VALUES ('9', '1', '2019-03-01', '2019-03-07', '13');
						//INSERT INTO `Check-ins` (`recordID`, `wardNumber`, `bedNumber`)
						// VALUES ('9', '1', '2');
						//manageCheckinRecordAdd(9, 1, 2, 1, "2019-03-01", "2019-03-07", 13);
						manageCheckinRecordAdd(1, 001, 1, 1001,"2019-03-01", "", 104);
						manageCheckinRecordAdd(2, 002, 1, 1002,"2019-03-10", "", 104);
						manageCheckinRecordAdd(3, 001, 2, 1003,"2019-03-15", "", 104);
						manageCheckinRecordAdd(4, 003, 1, 1004,"2019-03-17", "2019-03-21", 104);
						break;
					case "Billing Accounts":
						manageBillingAccountAdd("1001", "1004", "2019-03-17", "000-04-1234", 
												"Credit Card", "4044987612349123", "100",
												"yes", "400");
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
			// support_printQueryResultSet(result);

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
			// support_printQueryResultSet(result);

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
			// error_handler(err);
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
				prep_addTestRecord.setInt(8, responsibleDoctor);
				prep_addTestRecord.executeUpdate();
				connection.commit();
			}
			catch (Throwable err) {

				// Handle error
				// error_handler(err);

				// Roll back the entire transaction
				connection.rollback();

			}
			finally {
				// Restore normal auto-commit mode
				connection.setAutoCommit(true);
			}
		}
		catch (Throwable err) {
			// error_handler(err);
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
			// support_printQueryResultSet(result);

		}
		catch (Throwable err) {
			// error_handler(err);
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
			// support_printQueryResultSet(result);

		}
		catch (Throwable err) {
		// error_handler(err);
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
			// error_handler(err);
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
				// error_handler(err);

				// Roll back the entire transaction
				connection.rollback();

			}
			finally {
				// Restore normal auto-commit mode
				connection.setAutoCommit(true);
			}
		}
		catch (Throwable err) {
			// error_handler(err);
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
			// support_printQueryResultSet(result);

		}
		catch (Throwable err) {
			// error_handler(err);
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
			// support_printQueryResultSet(result);

		}
		catch (Throwable err) {
			// error_handler(err);
		}

		return success;
	}

	// Yudong
	// Update Check-in
	public static void manageCheckinUpdate(int recordID, String attributeToChange, String valueToChange){
		try {
			connection.setAutoCommit(true);
			switch (attributeToChange.toUpperCase()){
				case "END DATE":
					//should it be "`END DATE`"?
					prep_updateCheckinEndDate.setString(1, valueToChange);
					prep_updateCheckinEndDate.setInt(2, recordID);
					prep_updateTestEndDate.executeUpdate();
					break;
				case "WARD":
					break;
				case "BED":
					break;
				default:
					System.out.println("\nCannot update the '" + attributeToChange);
					break;
			}
		}
		catch (Throwable err) {
			// error_handler(err);
		}
	}

	// Report current usage status
	public static void reportCurrentUsageStatus() {

	}

	// Report number of patients
	public static void reportNumberOfPatients() {

	}

	// Report ward usage percentage
	public static void reportWardUsagePercentage() {

	}

	// Report doctor responsibility
	public static void reportDoctorResponsibility() {

	}

	// Create billing accounts
	public static boolean manageBillingAccountAdd(String accountID, String patientID, String visitDate, 
												String payerSSN, String paymentMethod, String cardNumber,
												String registrationFee, String medicationPrescribed,
												String acconmmandationFee) {
		try {
			// Start transaction
			connection.setAutoCommit(false);
			try {
				prep_addBillingAccount.setString(1, accountID);
				prep_addBillingAccount.setString(2, patientID);
				prep_addBillingAccount.setDate(3, java.sql.Date.valueOf(visitDate));
				prep_addBillingAccount.setString(4, payerSSN);
				prep_addBillingAccount.setString(5, paymentMethod);
				prep_addBillingAccount.setString(6, cardNumber);
				prep_addBillingAccount.setDouble(7, Double.parseDouble(registrationFee));
				prep_addBillingAccount.setBoolean(8, medicationPrescribed=="yes"?true:false);
				prep_addBillingAccount.setDouble(9, Double.parseDouble(acconmmandationFee));
				prep_addBillingAccount.executeUpdate();
				connection.commit();
			}
			catch (Throwable err) {
				// Roll back the entire transaction
				connection.rollback();
				return false;
			} finally {
				// Restore normal auto-commit mode
				connection.setAutoCommit(true);
				return true;
			}
		}
		catch (Throwable err) {
			error_handler(err);
			return false;
		}
		
	}

	// Get billing record
	public static void showBillingAccount() {

	}

	// Update billing accounts
	public static boolean manageBillingAccountUpdate() {
		return false;
	}

	// Delete billing account
	public static boolean deleteBillingAccount() {
		return false;
	}

	public static void error_handler(Throwable error) {

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
