import java.sql.*;
import java.util.Scanner;

public class WolfHospital {
	// Update your user info alone here
	private static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/yrao3"; // Using SERVICE_NAME

	// Update your user and password info here!
	private static final String user = "yrao3";
	private static final String password = "200204773";

	// Commands
	// Operators: responsible for admitting patients, processing patient information
	// and assigning beds.
	// Billing staff: responsible for creating billing information.
	// Doctors: treat, test patients and log medical records.
	// Administrators: manage all operations of users above and process staff and
	// hospital information.
	// Patients: Retrieve own billing account, medical records, patient info
	private static final String CMD_MAIN = "MAIN";
	private static final String CMD_OPERATORS = "OPERATORS";
	private static final String CMD_BILLING = "BILLING STAFF";
	private static final String CMD_DOCTORS = "DOCTORS";
	private static final String CMD_PATIENTS = "PATIENTS";
	private static final String CMD_ADMIN = "ADMINISTRATORS";
	private static final String CMD_QUIT = "QUIT";

	private static final String CMD_STAFF_ADD = "ADD STAFF";
	private static final String CMD_STAFF_GET = "RETRIEVE STAFF";
	private static final String CMD_STAFF_UPDATE = "UPDATE STAFF";
	private static final String CMD_STAFF_DELETE = "DELETE STAFF";

	private static final String CMD_PATIENT_ADD = "ADD PATIENT";
	private static final String CMD_PATIENT_GET = "RETRIEVE PATIENT";
	private static final String CMD_PATIENT_UPDATE = "UPDATE PATIENT";
	private static final String CMD_PATIENT_DELETE = "DELETE PATIENT";

	private static final String CMD_WARD_ADD = "ADD WARD";
	private static final String CMD_WARD_GET = "RETRIEVE WARD";
	private static final String CMD_WARD_UPDATE = "UPDATE WARD";
	private static final String CMD_WARD_DELETE = "DELETE WARD";
	private static final String CMD_WARD_CHECK = "CHECK AVAILABLE WARD";
	private static final String CMD_WARD_ASSIGN = "ASSIGN WARD";
	private static final String CMD_WARD_RESERVE = "RESERVE WARD";
	private static final String CMD_WARD_RELEASE = "RELEASE WARD";

	private static final String CMD_BED_CHECK = "CHECK AVAILABLE BED";
	private static final String CMD_BED_ASSIGN = "ASSIGN BED";
	private static final String CMD_BED_RESERVE = "RESERVE BED";
	private static final String CMD_BED_RELEASE = "RELEASE BED";

	private static final String CMD_TREATMENT_ADD = "ADD TREATMENT";
	private static final String CMD_TREATMENT_GETALL = "GET ALL TREATMENTS";
	private static final String CMD_TREATMENT_GET = "GET A TREATMENT";
	private static final String CMD_TREATMENT_UPDATE = "UPDATE TREATMENT";
	private static final String CMD_TEST_ADD = "ADD TEST";
	private static final String CMD_TEST_GETALL = "GET ALL TESTS";
	private static final String CMD_TEST_GET = "GET A TEST";
	private static final String CMD_TEST_UPDATE = "UPDATE TEST";
	private static final String CMD_CHECKIN_ADD = "ADD CHECK-IN";
	private static final String CMD_CHECKIN_GETALL = "GET ALL CHECK-INS";
	private static final String CMD_CHECKIN_GET = "GET A CHECK-IN";
	private static final String CMD_CHECKIN_UPDATE = "UPDATE CHECK-IN";

	private static final String CMD_MEDICAL_HISTORY_REPORT = "REPORT MEDICAL HISTORY";
	private static final String CMD_USAGE_STATUS_REPORT = "REPORT USAGE STATUS";
	private static final String CMD_PATIENT_NUMBER_REPORT = "REPORT PATIENT NUMBER";
	private static final String CMD_WARD_USAGE_PERCENT_REPORT = "REPORT WARD USAGE PERCENTAGE";
	private static final String CMD_DOCTOR_RESPONS_REPORT = "REPORT DOCTOR REPONSIBLITIES";

	private static final String CMD_BILLING_ACCT_ADD = "ADD BILLING ACCOUNT";
	private static final String CMD_BILLING_ACCT_GET = "RETRIEVE BILLING ACCOUNT";
	private static final String CMD_BILLING_ACCT_UPDATE = "UPDATE BILLING ACCOUNT";
	private static final String CMD_BILLING_ACCT_DELETE = "DELETE BILLING ACCOUNT";

	private static Scanner scanner;
	private static String currentMenu;

	private static Connection connection;
	private static Statement statement;
	private static ResultSet result;

	// Prepared Statements pre-declared
	// TO-DO 1: instantiate preparedStatements
	// cchen31
	// Staff
	private static PreparedStatement prep_addStaff;
	private static PreparedStatement prep_getStaff;
	private static PreparedStatement prep_updateStaffName;
	private static PreparedStatement prep_updateStaffAge;
	private static PreparedStatement prep_updateStaffJobTitle;
	private static PreparedStatement prep_updateStaffProfTitle;
	private static PreparedStatement prep_updateStaffDepart;
	private static PreparedStatement prep_updateStaffPhone;
	private static PreparedStatement prep_updateStaffAddress;
	private static PreparedStatement prep_deleteStaff;

	// Wards
	private static PreparedStatement prep_addWards;
	private static PreparedStatement prep_getWards;
	private static PreparedStatement prep_updateWardsCapacity;
	private static PreparedStatement prep_updateWardsCharge;
	private static PreparedStatement prep_updateWardsNurse;
	private static PreparedStatement prep_deleteWards;
	// Patients
	private static PreparedStatement prep_addPatients;
	private static PreparedStatement prep_getPatients;
	private static PreparedStatement prep_updatePatientsName;
	private static PreparedStatement prep_updatePatientsAge;
	private static PreparedStatement prep_updatePatientsPhone;
	private static PreparedStatement prep_updatePatientsAddress;
	private static PreparedStatement prep_updatePatientsStatus;
	private static PreparedStatement prep_deletePatients;

	// fhy
	// Medical Records - Treatment
	// GG
	private static PreparedStatement prep_addTreatmentRecord;
	// fhy
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
	private static PreparedStatement prep_reportCurrentWardUsageStatus;
	private static PreparedStatement prep_reportCurrentBedUsageStatus;
	private static PreparedStatement prep_reportNumberOfPatientsPerMonth;
	private static PreparedStatement prep_reportWardUsagePercentage;
	private static PreparedStatement prep_reportDoctorResponsiblity;
	private static PreparedStatement prep_reportStaffInformation;

	// Billing Accounts
	private static PreparedStatement prep_addBillingAccount;
	private static PreparedStatement prep_getBillingAccount;
	private static PreparedStatement prep_updateBillingAccountAddress;
	// Update payment method and card number
	private static PreparedStatement prep_updateBillingAccountPaymentType;
	private static PreparedStatement prep_updateBillingAccountCardNumber;
	// Update registration fee and accommandation fee
	private static PreparedStatement prep_updateBillingAccountRegistrationFee;
	private static PreparedStatement prep_updateBillingAccountAccommandationFee;
	private static PreparedStatement prep_updateBillingAccountMedicationPrescribed;
	private static PreparedStatement prep_updateBillingAccountVisitDate;
	private static PreparedStatement prep_deleteBillingAccount;
	
	//GG
	// Basic Information - Wards(partial, the rest should be done by others)
	private static PreparedStatement prep_deleteWardInfo;
	private static PreparedStatement prep_checkWardAvailability;
	//private static PreparedStatement prep_assignWard;
	//private static PreparedStatement prep_reserveWard;
	//private static PreparedStatement prep_releaseWard;
	
	//Basic Information - Beds
	private static PreparedStatement prep_addBedInfo;
	private static PreparedStatement prep_getBedInfo;
	private static PreparedStatement prep_deletebBedInfo;
	
	// Management - Beds
	private static PreparedStatement prep_assignBed;
	private static PreparedStatement prep_checkBedAvailability;
	//private static PreparedStatement prep_reserveBed;
	private static PreparedStatement prep_releaseBed;

	// Payer Info
	private static PreparedStatement prep_addPayerInfo;
	private static PreparedStatement prep_updatePayerAddress;
	private static PreparedStatement prep_deletePayerInfo;


	// Establish connection
	public static void connectToDatabase() {
		try {
			// Loading the driver. This creates an instance of the driver
			// and calls the registerDriver method to make MySql(MariaDB) Thin available to
			// clients.
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
		} catch (Throwable oops) {
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
			// cchen31
			// Enter basic information about staff
			sql = "INSERT INTO `Staff` (`staffID`, `name`, `age`, `gender`, `jobTitle`, `profTitle`, `department`, `phone`, `address`)"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
			prep_addStaff = connection.prepareStatement(sql);
			// Retrieve basic information about staff
			sql = "SELECT * FROM `Staff`" + "WHERE staffID = ?;";
			prep_getStaff = connection.prepareStatement(sql);
			// Update basic information about staff
			sql = "UPDATE `Staff`" + "SET `name` = ?" + "WHERE staffID = ?;";
			prep_updateStaffName = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + "SET `age` = ?" + "WHERE staffID = ?;";
			prep_updateStaffAge = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + "SET `jobTitle` = ?" + "WHERE staffID = ?;";
			prep_updateStaffJobTitle = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + "SET `profTitle` = ?" + "WHERE staffID = ?;";
			prep_updateStaffProfTitle = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + "SET `department` = ?" + "WHERE staffID = ?;";
			prep_updateStaffDepart = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + "SET `phone` = ?" + "WHERE staffID = ?;";
			prep_updateStaffPhone = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + "SET `address` = ?" + "WHERE staffID = ?;";
			prep_updateStaffAddress = connection.prepareStatement(sql);
			// Delete basic information about staff
			sql = "DELETE FROM `Staff`" + " WHERE staffID = ?;";
			prep_deleteStaff = connection.prepareStatement(sql);
			// Enter basic information about patients
			sql = "INSERT INTO `Patients` (`patientID`, `SSN`)" + "VALUES (?, ?);"
					+ "INSERT  INTO `PersonInfo` (`SSN`, `name`, `DOB`, `gender`, `age`, `phone`, `address`, `status`)"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
			prep_addPatients = connection.prepareStatement(sql);
			// Retrieve basic information about patients
			sql = "SELECT * FROM `Patients` p JOIN `PersonInfo` i ON p.SSN = i.SSN WHERE patientID = ?;";
			prep_getPatients = connection.prepareStatement(sql);
			// Update basic information about patients
			sql = "UPDATE `PersonInfo`" + "SET `name` = ?" + "WHERE SSN = ?;";
			prep_updatePatientsName = connection.prepareStatement(sql);
			sql = "UPDATE `PersonInfo`" + "SET `age` = ?" + "WHERE SSN = ?;";
			prep_updatePatientsAge = connection.prepareStatement(sql);
			sql = "UPDATE `PersonInfo`" + "SET `phone` = ?" + "WHERE SSN = ?;";
			prep_updatePatientsPhone = connection.prepareStatement(sql);
			sql = "UPDATE `PersonInfo`" + "SET `address` = ?" + "WHERE SSN = ?;";
			prep_updatePatientsAddress = connection.prepareStatement(sql);
			sql = "UPDATE `PersonInfo`" + "SET `completing treatment` = ?" + "WHERE SSN = ?;";
			prep_updatePatientsStatus = connection.prepareStatement(sql);
			// Delete basic information about patients
			sql = "DELETE FROM `Patients` p JOIN `PersonInfo` i ON p.SSN = i.SSN WHERE patientID = ?;";
			prep_deletePatients = connection.prepareStatement(sql);
			// Enter basic information about wards
			sql = "INSERT INTO `Wards` (`ward number`, `capacity`, `charges per day`, `responsible nurse`)"
					+ "VALUES (?, ?, ?, ?);";
			prep_addWards = connection.prepareStatement(sql);
			// Retrieve basic information about wards
			sql = "SELECT * FROM `Wards`" + "WHERE ward number = ?;";
			prep_getWards = connection.prepareStatement(sql);
			// Update basic information about wards
			sql = "UPDATE `Wards`" + "SET `capacity` = ?" + "WHERE ward number = ?;";
			prep_updateWardsCapacity = connection.prepareStatement(sql);
			sql = "UPDATE `Wards`" + "SET `charges per day` = ?" + "WHERE ward number = ?;";
			prep_updateWardsCharge = connection.prepareStatement(sql);
			sql = "UPDATE `Wards`" + "SET `responsible nurse` = ?" + "WHERE ward number = ?;";
			prep_updateWardsNurse = connection.prepareStatement(sql);
			// fhy
			// Get all treatment records
			// SELECT * FROM `Medical Records` m JOIN `Treatment` t ON m.recordID=t.recordID
			// WHERE patientID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Treatment` t ON m.recordID=t.recordID WHERE patientID=?;";
			prep_getAllTreatmentRecords = connection.prepareStatement(sql);

			// Get treatment record
			// SELECT * FROM `Medical Records` m JOIN `Treatment` t ON m.recordID=t.recordID
			// WHERE t.recordID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Treatment` t ON m.recordID=t.recordID WHERE t.recordID=?;";
			prep_getTreatmentRecord = connection.prepareStatement(sql);

			// Update treatment record
			// UPDATE `Medical Records` SET `endDate` = '2020-01-01' WHERE recordID = 13;
			// UPDATE `Treatment` SET `prescription` = 'Use', `diagnosisDetails` = 'Muscle'
			// WHERE recordID = '13';
			sql = "UPDATE `Medical Records` " + "SET `endDate` = ? " + "WHERE recordID = ? " + "AND EXISTS "
					+ "(SELECT * FROM `Treatment` " + "WHERE recordID = ?;)";
			prep_updateTreatmentEndDate = connection.prepareStatement(sql);

			sql = "UPDATE `Treatment` " + "SET `prescription` = ? " + "WHERE recordID = ?;";
			prep_updateTreatmentPrescription = connection.prepareStatement(sql);

			sql = "UPDATE `Treatment` " + "SET `diagnosisDetails` = ? " + "WHERE recordID = ?;";
			prep_updateTreatmentDiagnosisDetails = connection.prepareStatement(sql);

			// Create new test record
			// INSERT INTO `Test` (`recordID`, `testType`, `testResult`)VALUES ('14',
			// 'testType5', 'testResult5');
			// INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`,
			// `endDate`, `responsibleDoctor`) VALUES ('14', '5', '2019-07-01',
			// '2019-07-02', '3');
			sql = "INSERT INTO `Test` (`recordID`, `testType`, `testResult`) " + "VALUES (?, ?, ?); "
					+ "INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`, `endDate`, `responsibleDoctor`) "
					+ "VALUES (?, ?, ?, ?, ?);";
			prep_addTestRecord = connection.prepareStatement(sql);

			// Get all test records
			// SELECT * FROM `Medical Records` m JOIN `Test` t ON m.recordID=t.recordID
			// WHERE patientID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Test` t ON m.recordID=t.recordID WHERE patientID=?;";
			prep_getAllTestRecords = connection.prepareStatement(sql);

			// Get test record
			// SELECT * FROM `Medical Records` m JOIN `Test` t ON m.recordID=t.recordID
			// WHERE t.recordID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Test` t ON m.recordID=t.recordID WHERE t.recordID=?;";
			prep_getTestRecord = connection.prepareStatement(sql);

			// Update test record
			// UPDATE `Medical Records` SET `endDate` = '2020-01-01' WHERE recordID=14;
			// UPDATE `Test` SET `testType` = 'Influenza B Rapid Assay', `testResult` =
			// 'Influenza B Antigen value: positive, ref range: negative' WHERE recordID =
			// '14';
			sql = "UPDATE `Medical Records` " + "SET `endDate` = ? " + "WHERE recordID= ? " + "AND EXISTS "
					+ "(SELECT * FROM `Test` " + "WHERE recordID = ?;)";
			prep_updateTestEndDate = connection.prepareStatement(sql);

			sql = "UPDATE `Test` " + "SET `testType` = ? " + "WHERE recordID = ?;";
			prep_updateTestTestType = connection.prepareStatement(sql);

			sql = "UPDATE `Test` " + "SET `testResult` = ? " + "WHERE recordID = ?;";
			prep_updateTestTestResult = connection.prepareStatement(sql);

			// Create check-in record
			// INSERT INTO `Check-ins` (`recordID`, `wardNumber`, `bedNumber`)VALUES ('15',
			// NULL, NULL);
			// INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`,
			// `endDate`, `responsibleDoctor` ) VALUES ('15', '5', '2019-07-01',
			// '2019-07-07', '4');
			sql = "INSERT INTO `Check-ins` (`recordID`, `wardNumber`, `bedNumber`) " + "VALUES (?, ?, ?); "
					+ "INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`, `endDate`, `responsibleDoctor`) "
					+ "VALUES (?, ?, ?, ?, ?);";
			prep_addCheckinRecord = connection.prepareStatement(sql);

			// Get all check-in records
			// SELECT * FROM `Medical Records` m JOIN `Check-ins` c ON m.recordID=c.recordID
			// WHERE patientID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Check-ins` c ON m.recordID=c.recordID WHERE patientID=?;";
			prep_getAllCheckinRecords = connection.prepareStatement(sql);

			// Get check-in record
			// SELECT * FROM `Medical Records` m JOIN `Check-ins` c ON m.recordID=c.recordID
			// WHERE c.recordID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Check-ins` c ON m.recordID=c.recordID WHERE c.recordID=?;";
			prep_getCheckinRecord = connection.prepareStatement(sql);

			// Yudong
			// Update check-in records
			sql = "UPDATE `Medical Records` " + "SET `endDate` = ? " + "WHERE recordID = ? " + "AND EXISTS "
					+ "(SELECT * FROM `Check-ins` " + "WHERE recordID = ?);";
			prep_updateCheckinEndDate = connection.prepareStatement(sql);

			sql = "UPDATE `Check-ins` " + "SET `wardNumber` = ? " + "WHERE recordID = ?;";
			prep_updateCheckinWard = connection.prepareStatement(sql);

			sql = "UPDATE `Check-ins` " + "SET `bedNumber` = ? " + "WHERE recordID = ?;";
			prep_updateCheckinBed = connection.prepareStatement(sql);

			// Report ward usage
			sql = "SELECT `ward number`, " + "IF(patientID IS NULL, 'empty', 'not empty') AS `usage` "
					+ "FROM Beds GROUP BY `ward number`;";
			prep_reportCurrentWardUsageStatus = connection.prepareStatement(sql);

			// Report bed usage
			sql = "SELECT *, " + "IF(patientID IS NULL, 'not used', 'used') AS `usage` " + "FROM Beds;";
			prep_reportCurrentBedUsageStatus = connection.prepareStatement(sql);

			// Report number of patients per month
			sql = "SELECT MONTH(startDate) AS `month`, " + "COUNT(*) AS `num` " + "FROM `Medical Records` "
					+ "GROUP BY month;";
			prep_reportNumberOfPatientsPerMonth = connection.prepareStatement(sql);

			// Report ward usage percentage
			sql = "SELECT 100*COUNT(patientID)/COUNT(*) " + "AS `usage percentage` " + "FROM Beds;";
			prep_reportWardUsagePercentage = connection.prepareStatement(sql);

			// Report doctor responsibility
			sql = "SELECT * FROM `Medical Records` " + "WHERE responsibleDoctor=?;";
			prep_reportDoctorResponsiblity = connection.prepareStatement(sql);

			// Report staff information
			sql = "SELECT * FROM `Staff` " + "GROUP BY jobTitle;";
			prep_reportStaffInformation = connection.prepareStatement(sql);

			// Create billing account
			sql = "INSERT INTO `Billing Accounts` (`accountID`, `patientID`, `visitDate`, "
					+ "`payerSSN`, `paymentMethod`, `cardNumber`, `registrationFee` "
					+ "`medicationPrescribed`, `accommandationFee`) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
			prep_addBillingAccount = connection.prepareStatement(sql);

			sql = "INSERT INTO `PayerInfo` (`SSN`, `billingAddress`) " + "VALUES (?, ?);";

			// Get billing account
			sql = "SELECT b.accountID, b.patientID, b.visitDate, "
					+ "b.payerSSN, b.paymentMethod, b.cardNumber, b.registrationFee "
					+ "b.medicationPrescribed, b.accommandationFee, p.billingAddress "
					+ "FROM `Billing Accounts` b JOIN `PayerInfo` p " + "ON m.payerSSN=b.SSN "
					+ "WHERE `accountID` = ?;";
			prep_getBillingAccount = connection.prepareStatement(sql);

			// Update billing account
			sql = "UPDATE `PayerInfo` " + "SET `billingAddress` = ? " + "WHERE payerSSN IN ( " + "SELECT b.SSN "
					+ "FROM `Billing Accounts` b JOIN `PayerInfo` p " + "ON m.payerSSN=b.SSN "
					+ "WHERE accountID = ?);";
			prep_updateBillingAccountAddress = connection.prepareStatement(sql);

			sql = "UPDATE `Billing Accounts` " + "SET `paymentMethod` = ? " + "WHERE accountID = ?;";
			prep_updateBillingAccountPaymentType = connection.prepareStatement(sql);

			sql = "UPDATE `Billing Accounts` " + "SET `cardNumber` = ? " + "WHERE accountID = ? "
					+ "AND paymentMethod = `Credit Card`;";
			prep_updateBillingAccountCardNumber = connection.prepareStatement(sql);

			sql = "UPDATE `Billing Accounts` " + "SET `registrationFee` = ? " + "WHERE accountID = ?;";
			prep_updateBillingAccountRegistrationFee = connection.prepareStatement(sql);

			sql = "UPDATE `Billing Accounts` " + "SET `accommandationFee` = ? " + "WHERE accountID = ?;";
			prep_updateBillingAccountAccommandationFee = connection.prepareStatement(sql);

			sql = "UPDATE `Billing Accounts` " + "SET `medicationPrescribed` = ? " + "WHERE accountID = ?;";
			prep_updateBillingAccountMedicationPrescribed = connection.prepareStatement(sql);

			sql = "UPDATE `Billing Accounts` " + "SET `visitDate` = ? " + "WHERE accountID = ?;";
			prep_updateBillingAccountVisitDate = connection.prepareStatement(sql);

			// Delete billing account
			sql = "DELETE FROM `Billing Accounts` " + "WHERE accountID = ?;";
			prep_deleteBillingAccount = connection.prepareStatement(sql);
			
			// GG
			// Delete basic information about wards
			sql = "DELETE FROM `Wards` " +
					"WHERE `ward number` = ?; ";
			prep_deleteWardInformation = connection.prepareStatement(sql);
			
			// Check availability of wards
			sql = "SELECT DISTINCT `ward number` " +
					"FROM `Beds`" +
					"WHERE ISNULL(patientID); ";
			prep_checkWardAvailability = connection.prepareStatement(sql);
			
			// Assign wards:
			//sql = "SELECT COUNT(`bed number`) FROM `Beds` WHERE `ward number` = ?; ";
			//prep_assignWard = connection.prepareStatement(sql);
			
			// Reserve wards
			//sql = "INSERT INTO `Assigned` (`patientID`, `ward number`, `bed number`, `start-date`, `end-date`) " +
			//		"VALUES (?, ?, ?, ?, ?) );" +
			//	  "UPDATE `Beds` SET "
			//		;
			//prep_reserveWard = connection.prepareStatement(sql);
			
			// Release wards
			//sql = "";
			//prep_releaseWard = connection.prepareStatement(sql);
			
			// Add basic information of a bed
			sql = "INSERT INTO `Beds` (`ward number`, `bed number`, `patientID`) " +
					"VALUES (?, ?, ?); ";
			prep_addBedInfo = connection.prepareStatement(sql);
			
			// Get basic information of a bed
			sql = "SELECT * FROM `Beds` " +
					"WHERE `ward number` = ? AND `bed number` = ?; ";
			prep_getBedInfo = connection.prepareStatement(sql);
			
			
			// Delete basic information of a bed
			sql = "DELETE FROM `Beds` WHERE `ward number` = ? AND `bed number` = ?; ";
			prep_deleteBedInfo = connection.prepareStatement(sql);
			
			// Assign beds
			sql = "UPDATE `Beds` SET `patientID` = ? WHERE `ward number` = ? AND `bed number` = ?; ";
			prep_assignBed = connection.prepareStatement(sql);
			
			// Check availability of beds
			sql = "SELECT * FROM `Beds` " +
					"WHERE ISNULL(patientID); ";
			prep_checkBedAvailability = connection.prepareStatement(sql);
			
			// Reserve beds
			//sql = "INSERT INTO `Assigned` (`patientID`, `ward number`, `bed number`, `start-date`, `end-date`) " +
			//		"VALUES (?, ?, ?, ?, ?); ";
			//prep_reserveBed = connection.prepareStatement(sql);
			
			// Release beds
			sql = "UPDATE `Beds` SET `patientID` = NULL WHERE `ward number` = ? AND `bed number` = ?; ";
			prep_releaseBed = connection.prepareStatement(sql);
			
			// Create treatment records
			sql = "INSERT `Treatment` (`recordID`, `prescription`, `diagnosisDetails`) " +
					"VALUES (?, ?, ?); ";
			prep_addTreatmentRecord = connection.prepareStatement(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// TO-DO 3: create tables
	public static void generateTables() {
		try {
			connection.setAutoCommit(false);
			try {
				// Wayne: Staff, Patients, Wards:
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS `Staff` ("
						+ "`staffID` VARCHAR(255) NOT NULL UNIQUE, " + "`name` VARCHAR(255) NOT NULL,"
						+ "`age` INT(3) NOT NULL," + "`gender` VARCHAR(255) NOT NULL,"
						+ "`jobTitle` VARCHAR(255) NOT NULL," + "`profTitle` VARCHAR(255) NULL,"
						+ "`department` VARCHAR(255) NOT NULL," + "`phone` VARCHAR(255) NOT NULL,"
						+ "`address` VARCHAR(255) NOT NULL," + "PRIMARY KEY (`staffID`)" + ");");
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS `Patients` ("
						+ "`patientID` varchar(255) NOT NULL, " + "`SSN` varchar(255) NOT NULL UNIQUE, "
						+ "PRIMARY KEY (`patientID`)" + "FOREIGN KEY (`SSN`) REFERENCES PersonInfo(`SSN`)" + ");");
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS `PersonInfo` (" + "`SSN` varchar(255) NOT NULL, "
						+ "`name` varchar(255) NOT NULL, " + "`DOB` datetime NOT NULL, "
						+ "`gender` VARCHAR(255) NOT NULL, " + "`age` int(3) NOT NULL, "
						+ "`phone` VARCHAR(255) NOT NULL," + "`address` VARCHAR(255) NOT NULL,"
						+ "`completing treatment` varchar(255) NOT NULL, " + "PRIMARY KEY (`SSN`)" + ");");
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS `Wards` (" + "`ward number` varchar(255) NOT NULL, "
						+ "`capacity` varchar(255) NOT NULL, " + "`charges per day` varchar(255) NOT NULL, "
						+ "`responsible nurse` varchar(255) NOT NULL, " + "PRIMARY KEY (`ward number`)"
						+ "FOREIGN KEY (`responsible nurse`) REFERENCES Staff(`staffID`)" + ");");
				// fhy: Medical Records, Treatment, Test, Check-ins
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS `Medical Records` ("
						+ "`recordID` VARCHAR(255) NOT NULL UNIQUE," + "`patientID` VARCHAR(255) NOT NULL,"
						+ "`startDate` DATETIME NOT NULL," + "`endDate` DATETIME DEFAULT NULL,"
						+ "`responsibleDoctor` VARCHAR(255) NOT NULL," + "PRIMARY KEY (`recordID`)"
						+ "FOREIGN KEY (`patientID`) REFERENCES Patients(`patientID`)"
						+ "FOREIGN KEY (`responsibleDoctor`) REFERENCES Staff(`staffID`)" + ");");
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS `Treatment` ("
						+ "`recordID` VARCHAR(255) NOT NULL UNIQUE," + "`prescription` VARCHAR(255) NOT NULL,"
						+ "`diagnosisDetails` VARCHAR(255) NOT NULL," + "PRIMARY KEY (`recordID`)"
						+ "FOREIGN KEY (`recordID`) REFERENCES `Medical Records`(`recordID`)" + ");");
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS `Test` ("
						+ "`recordID` VARCHAR(255) NOT NULL UNIQUE," + "`testType` VARCHAR(255) NOT NULL,"
						+ "`testResult` VARCHAR(255) NOT NULL," + "PRIMARY KEY (`recordID`)"
						+ "FOREIGN KEY (`recordID`) REFERENCES `Medical Records`(`recordID`)" + ");");
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS `Check-ins` ("
						+ "`recordID` VARCHAR(255) NOT NULL UNIQUE," + "`wardNumber` VARCHAR(255) DEFAULT NULL,"
						+ "`bedNumber` VARCHAR(255) DEFAULT NULL," + "PRIMARY KEY (`recordID`)"
						+ "FOREIGN KEY (`recordID`) REFERENCES `Medical Records`(`recordID`)"
						+ "FOREIGN KEY (`wardNumber`) REFERENCES Wards(`ward number`)" + ");");

				// Yudong
				// Billing accounts && PayerInfo
				statement.executeUpdate(
<<<<<<< HEAD
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
				
				// GG
				// Wards & Beds
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `Wards` (" +
						"`ward number` VARCHAR(255) NOT NULL UNIQUE," +
						"`capacity` TINYINT NOT NULL," +
						"`charges per day` DOUBLE NOT NULL," +
						"`responsible nurse` VARCHAR(255) NOT NULL," +
						"PRIMARY KEY (`ward number`) " +
						"CONSTRAINT fk_ward FOREIGN KEY (`responsible nurse`) REFERENCES Staff(`staffID`) " +
						"ON DELETE CASCADE" +
						");");
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `Beds` (" +
						"`ward number` VARCHAR(255) NOT NULL," +
						"`bed number` VARCHAR(255) NOT NULL," +
						"`patientID` VARCHAR(255) DEFAULT NULL," +
						"PRIMARY KEY (`ward number`, `bed number`) " +
						"CONSTRAINT `fk_bed`" +
							"FOREIGN KEY (`ward number`) REFERENCES Wards(`ward number`) " +
							"FOREIGN KEY (`patientID`) REFERENCES Patients(`patientID`) " +
							"ON DELETE CASCADE" +
						");");
				// Assigned
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `Assigned` (" +
						"`patientID` VARCHAR(255) NOT NULL," +
						"`ward number` VARCHAR(255) NOT NULL," +
						"`bed number` VARCHAR(255) NOT NULL," +
						"`start-date` DATETIME NOT NULL," +
						"`end-date` DATETIME DEFAULT NULL," +
						"CONSTRAINT pk_assign PRIMARY KEY (`patientID`, `ward number`, `bed number`)" +
						"CONSTRAINT `fk_assign`" +
							"FOREIGN KEY (`patientID`) REFERENCES Patients(`patientID`) " +
							"FOREIGN KEY (`ward number`) REFERENCES Wards(`ward number`) " +
							"FOREIGN KEY (`bed number`) REFERENCES Beds(`bed number`) " +
							"ON DELETE CASCADE" +
						");");
				
=======
						"CREATE TABLE IF NOT EXISTS`PayerInfo` ( " + "`SSN` VARCHAR(255) NOT NULL UNIQUE, "
								+ "`billingAddress` VARCHAR(255) NOT NULL, " + "PRIMARY KEY (`SSN`) " + ");");

				statement.executeUpdate("CREATE TABLE IF NOT EXISTS`Billing Accounts` ( "
						+ "`accountID` VARCHAR(255) NOT NULL UNIQUE, " + "`patientID` VARCHAR(255) NOT NULL, "
						+ "`visitDate` datetime NOT NULL, " + "`payerSSN` VARCHAR(255) NOT NULL, "
						+ "`paymentMethod` VARCHAR(255) NOT NULL, " + "`cardNumber` VARCHAR(255) DEFAULT NULL "
						+ "`registrationFee` DOUBLE NOT NULL " + "`medicationPrescribed` BIT DEFAULT NULL "
						+ "`accommandationFee` DOUBLE NOT NULL " + "PRIMARY KEY (`accountID`) "
						+ "FOREIGN KEY (`patientID`) REFERENCES Patients(`patientID`) "
						+ "FOREIGN KEY (`payerSSN`) REFERENCES PayerInfo(`SSN`) " + ");");
>>>>>>> 214bfb42e18a241e76aefa0c651fa34d065a03ef

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
<<<<<<< HEAD
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
						//GG
						/* Populating data for Treatment
						 * INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`, `endDate`, `responsibleDoctor`	)
						 * INSERT INTO `Treatment` (`recordID`, `prescription`, `diagnosisDetails`)
						 * String recordID,
						 * String patientID,
						 * Datetime startDate, endDate, 
						 * String responsibleDoctor,
						 * String prescription, diagnosisDetails
						 */
						manageTreatmentRecordAdd("1", "1001", "2019-03-01", "", "100", "nervine", "Hospitalization");
						manageTreatmentRecordAdd("2", "1002", "2019-03-10", "", "100", "nervine", "Hospitalization");
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
						break;
					
					//GG
					/* Populating data for Wards
					 * String ward number,
					 * Int capacity,
					 * Double charges per day,
					 * String responsible nurse
					 * */
					case "Wards":
						manageWardAdd("001", 4, 50, "102");
						manageWardAdd("002", 4, 50, "102");
						manageWardAdd("003", 2, 100, "106");
						manageWardAdd("004", 2, 100, "106");						
					/* Populating data for Beds
					 * String ward number,
					 * String bed number,
					 * String patientID
					 * Demo data: for ward#001 beds#1,2,3,4; for ward#002 beds#1,2,3,4; for ward#003 beds#1,2; for ward#004 beds#1,2;
					 * Patient000-01-1234 is assigned to w1b1, 000-03-1234 w1b2, 000-02-1234 w2b1, 000-04-1234 w3b1(finish treatment)
					 * */
					case "Beds":
						manageBedAdd("001", "1", "000-01-1234");
						manageBedAdd("001", "2", "000-03-1234");
						manageBedAdd("001", "3", "");
						manageBedAdd("001", "4", "");
						manageBedAdd("002", "1", "000-02-1234");
						manageBedAdd("002", "2", "");
						manageBedAdd("002", "3", "");
						manageBedAdd("002", "4", "");
						manageBedAdd("003", "1", "000-04-1234");
						manageBedAdd("003", "2", "");
						manageBedAdd("004", "1", "");
						manageBedAdd("004", "2", "");
					/* Populating data for Assigned
					 * String patientID,
					 * String ward number,
					 * String bed number,
					 * Datetime start-date,
					 * Datetime end-date
					 * */
					case "Assigned":
						manageAssignedAdd("1001", "001", "1", "2019-03-01", "");
						manageAssignedAdd("1002", "002", "1", "2019-03-10", "");
						manageAssignedAdd("1003", "001", "2", "2019-03-15", "");
						manageAssignedAdd("1004", "003", "1", "2019-03-17", "2019-03-21");

					default:
						break;
				}
=======
				switch (tableName) {
				// Staff:
				case "Staff":
					prep_addStaff.setString(1, "xxx");
					break;
				// Other tables...
				// fhy: Medical Records(along with other tables), Treatment, Test, Check-ins
				// demo data: assuming Medical Records #1 #2 are Treatment, #3 is Test, #4 with
				// 2 different doctors, consist of one test(by 103) and one treatment(by 105)
				// the only front desk staff is 104, so all check-ins are assumed to be done by
				// 104
				// question: is empty string the correct way to deal with empty value of
				// endDate?
				case "Treatment":
					// manageTreatmentRecordAdd() should be done by other teammates
					break;
				case "Test":
					// INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`,
					// `endDate`, `responsibleDoctor` )
					// VALUES ('5', '1', '2019-03-01', '2019-03-02', '3');
					// INSERT INTO `Test` (`recordID`, `testType`, `testResult`)
					// VALUES ('5', 'DPC POC Urinalysis Chemical', 'Protein, Urinalysis value:2+,
					// ref range:negative');
					// manageTestRecordAdd(5, "DPC POC Urinalysis Chemical", "Protein, Urinalysis
					// value:2+, ref range:negative", 1, "2019-03-01", "2019-03-02", 3);
					manageTestRecordAdd("3", "test", "prescription nervine, diagnosis details Hospitalization", "1003",
							"2019-03-15", "", "100");
					manageTestRecordAdd("4", "test",
							"prescription analgesic, diagnosis details Surgeon, Hospitalization", "1004", "2019-03-17",
							"2019-03-21", "103");
					break;
				case "Check-ins":
					// INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`,
					// `endDate`, `responsibleDoctor` )
					// VALUES ('9', '1', '2019-03-01', '2019-03-07', '13');
					// INSERT INTO `Check-ins` (`recordID`, `wardNumber`, `bedNumber`)
					// VALUES ('9', '1', '2');
					// manageCheckinRecordAdd(9, 1, 2, 1, "2019-03-01", "2019-03-07", 13);
					manageCheckinRecordAdd("1", "001", "1", "1001", "2019-03-01", "", "104");
					manageCheckinRecordAdd("2", "002", "1", "1002", "2019-03-10", "", "104");
					manageCheckinRecordAdd("3", "001", "2", "1003", "2019-03-15", "", "104");
					manageCheckinRecordAdd("4", "003", "1", "1004", "2019-03-17", "2019-03-21", "104");
					break;
				case "Billing Accounts":
					manageBillingAccountAdd("1001", "1004", "2019-03-17", "000-04-1234", "Credit Card",
							"4044987612349123", "100", "yes", "400", "10 TBC St. Raleigh NC 27730");
					break;
>>>>>>> 214bfb42e18a241e76aefa0c651fa34d065a03ef

				default:
					break;
				}
				connection.commit();
			} catch (SQLException e) {
				connection.rollback();
				e.printStackTrace();
			} finally {
				System.out.println("Tables populated!");
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

	// fhy support_printQueryResultSet, error_handler not yet implemented
	// 1
	public static boolean showAllTreatmentRecords(String patientID) {
		boolean success = false;

		try {
			prep_getAllTreatmentRecords.setString(1, patientID);
			result = prep_getAllTreatmentRecords.executeQuery();

			if (result.next()) {
				success = true;
				result.beforeFirst();
			}

			System.out.println("\nshowAllTreatmentRecords\n");
			// support_printQueryResultSet(result);

		} catch (Throwable err) {
			error_handler(err);
		}

		return success;
	}

	// 2
	public static boolean showTreatmentRecord(String recordID) {
		boolean success = false;

		try {
			prep_getTreatmentRecord.setString(1, recordID);
			result = prep_getTreatmentRecord.executeQuery();

			if (result.next()) {
				success = true;
				result.beforeFirst();
			}

			System.out.println("\nshowTreatmentRecord\n");
			// support_printQueryResultSet(result);

		} catch (Throwable err) {
			error_handler(err);
		}

		return success;
	}

	// 3
	public static void manageTreatmentUpdate(String recordID, String attributeToChange, String valueToChange) {
		try {
			connection.setAutoCommit(true);
			switch (attributeToChange.toUpperCase()) {
			case "ENDDATE":
				prep_updateTreatmentEndDate.setString(1, valueToChange);
				prep_updateTreatmentEndDate.setString(2, recordID);
				prep_updateTreatmentEndDate.setString(3, recordID);
				prep_updateTreatmentEndDate.executeUpdate();
				break;
			case "PRESCRIPTION":
				prep_updateTreatmentPrescription.setString(1, valueToChange);
				prep_updateTreatmentPrescription.setString(2, recordID);
				prep_updateTreatmentPrescription.executeUpdate();
				break;
			case "DIAGNOSISDETAILS":
				prep_updateTreatmentDiagnosisDetails.setString(1, valueToChange);
				prep_updateTreatmentDiagnosisDetails.setString(2, recordID);
				prep_updateTreatmentDiagnosisDetails.executeUpdate();
				break;
			default:
				System.out.println("\nCannot update the '" + attributeToChange);
				break;
			}
		} catch (Throwable err) {
			// error_handler(err);
		}
	}

	// 4
	public static void manageTestRecordAdd(String recordID, String testType, String testResult, String patientID,
			String startDate, String endDate, String responsibleDoctor) {
		// to be done: check success or not and report
		try {

			// Start transaction
			connection.setAutoCommit(false);

			try {
				prep_addTestRecord.setString(1, recordID);
				prep_addTestRecord.setString(2, testType);
				prep_addTestRecord.setString(3, testResult);
				prep_addTestRecord.setString(4, recordID);
				prep_addTestRecord.setString(5, patientID);
				prep_addTestRecord.setDate(6, java.sql.Date.valueOf(startDate));
				prep_addTestRecord.setDate(7, java.sql.Date.valueOf(endDate));
				prep_addTestRecord.setString(8, responsibleDoctor);
				prep_addTestRecord.executeUpdate();
				connection.commit();
			} catch (Throwable err) {

				// Handle error
				// error_handler(err);

				// Roll back the entire transaction
				connection.rollback();

			} finally {
				// Restore normal auto-commit mode
				connection.setAutoCommit(true);
			}
		} catch (Throwable err) {
			// error_handler(err);
		}
	}

	// 5
	public static boolean showAllTestRecords(String patientID) {
		boolean success = false;

		try {
			prep_getAllTestRecords.setString(1, patientID);
			result = prep_getAllTestRecords.executeQuery();

			if (result.next()) {
				success = true;
				result.beforeFirst();
			}

			System.out.println("\nshowAllTestRecords\n");
			// support_printQueryResultSet(result);

		} catch (Throwable err) {
			// error_handler(err);
		}

		return success;
	}

	// 6
	public static boolean showTestRecord(String recordID) {
		boolean success = false;

		try {
			prep_getTestRecord.setString(1, recordID);
			result = prep_getTestRecord.executeQuery();

			if (result.next()) {
				success = true;
				result.beforeFirst();
			}

			System.out.println("\nshowTestRecord\n");
			// support_printQueryResultSet(result);

		} catch (Throwable err) {
			// error_handler(err);
		}

		return success;
	}

	// 7
	public static void manageTestUpdate(String recordID, String attributeToChange, String valueToChange) {
		try {
			connection.setAutoCommit(true);
			switch (attributeToChange.toUpperCase()) {
			case "ENDDATE":
				prep_updateTestEndDate.setString(1, valueToChange);
				prep_updateTestEndDate.setString(2, recordID);
				prep_updateTestEndDate.setString(3, recordID);
				prep_updateTestEndDate.executeUpdate();
				break;
			case "TESTTYPE":
				prep_updateTestTestType.setString(1, valueToChange);
				prep_updateTestTestType.setString(2, recordID);
				prep_updateTestTestType.executeUpdate();
				break;
			case "TESTRESULT":
				prep_updateTestTestResult.setString(1, valueToChange);
				prep_updateTestTestResult.setString(2, recordID);
				prep_updateTestTestResult.executeUpdate();
				break;
			default:
				System.out.println("\nCannot update the '" + attributeToChange);
				break;
			}
		} catch (Throwable err) {
			// error_handler(err);
		}
	}

	// 8
	public static void manageCheckinRecordAdd(String recordID, String wardNumber, String bedNumber, String patientID,
			String startDate, String endDate, String responsibleDoctor) {
		// to be done: check success or not and report
		try {

			// Start transaction
			connection.setAutoCommit(false);
			try {
				prep_addCheckinRecord.setString(1, recordID);
				prep_addCheckinRecord.setString(2, wardNumber);
				prep_addCheckinRecord.setString(3, bedNumber);
				prep_addCheckinRecord.setString(4, recordID);
				prep_addCheckinRecord.setString(5, patientID);
				prep_addCheckinRecord.setDate(6, java.sql.Date.valueOf(startDate));
				prep_addCheckinRecord.setDate(7, java.sql.Date.valueOf(endDate));
				prep_addCheckinRecord.setString(8, responsibleDoctor);
				prep_addCheckinRecord.executeUpdate();
				connection.commit();
			} catch (Throwable err) {

				// Handle error
				// error_handler(err);

				// Roll back the entire transaction
				connection.rollback();

			} finally {
				// Restore normal auto-commit mode
				connection.setAutoCommit(true);
			}
		} catch (Throwable err) {
			// error_handler(err);
		}
	}

	// 9
	public static boolean showAllCheckinRecords(String patientID) {
		boolean success = false;

		try {
			prep_getAllCheckinRecords.setString(1, patientID);
			result = prep_getAllCheckinRecords.executeQuery();

			if (result.next()) {
				success = true;
				result.beforeFirst();
			}

			System.out.println("\nshowAllCheckinRecords\n");
			// support_printQueryResultSet(result);

		} catch (Throwable err) {
			// error_handler(err);
		}

		return success;
	}

	// 10
	public static boolean showCheckinRecord(String recordID) {
		boolean success = false;

		try {
			prep_getCheckinRecord.setString(1, recordID);
			result = prep_getCheckinRecord.executeQuery();

			if (result.next()) {
				success = true;
				result.beforeFirst();
			}

			System.out.println("\nshowCheckinRecord\n");
			// support_printQueryResultSet(result);

		} catch (Throwable err) {
			// error_handler(err);
		}

		return success;
	}

	// Yudong
	// Update Check-in
	public static void manageCheckinUpdate(String recordID, String attributeToChange, String valueToChange) {
		try {
			connection.setAutoCommit(true);
			switch (attributeToChange.toUpperCase()) {
			case "ENDDATE":
				prep_updateCheckinEndDate.setString(1, valueToChange);
				prep_updateCheckinEndDate.setString(2, recordID);
				prep_updateCheckinEndDate.setString(3, recordID);
				prep_updateCheckinEndDate.executeUpdate();
				break;
			case "WARDNUMBER":
				prep_updateCheckinWard.setString(1, valueToChange);
				prep_updateCheckinWard.setString(2, recordID);
				prep_updateCheckinWard.executeUpdate();
				break;
			case "BEDNUMBER":
				prep_updateCheckinBed.setString(1, valueToChange);
				prep_updateCheckinBed.setString(2, recordID);
				prep_updateCheckinBed.executeUpdate();
				break;
			default:
				System.out.println("\nCannot update the '" + attributeToChange);
				break;
			}
		} catch (Throwable err) {
			// error_handler(err);
		}
	}

	// Report current usage status
	public static boolean reportCurrentWardUsageStatus() {
		boolean success = false;
		try {
			result = prep_reportCurrentWardUsageStatus.executeQuery();
			if (result.next()) {
				success = true;
				result.beforeFirst();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	public static boolean reportCurrentBedUsageStatus() {
		boolean success = false;
		try {
			result = prep_reportCurrentBedUsageStatus.executeQuery();
			if (result.next()) {
				success = true;
				result.beforeFirst();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	// Report number of patients
	public static boolean reportNumberOfPatientsPerMonth() {
		boolean success = false;
		try {
			result = prep_reportNumberOfPatientsPerMonth.executeQuery();
			if (result.next()) {
				success = true;
				result.beforeFirst();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	// Report ward usage percentage
	public static boolean reportWardUsagePercentage() {
		boolean success = false;
		try {
			result = prep_reportWardUsagePercentage.executeQuery();
			if (result.next()) {
				success = true;
				result.beforeFirst();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	// Report doctor responsibility
	public static boolean reportDoctorResponsibility(String doctorID) {
		boolean success = false;
		try {
			prep_reportDoctorResponsiblity.setString(1, doctorID);
			result = prep_reportDoctorResponsiblity.executeQuery();
			if (result.next()) {
				success = true;
				result.beforeFirst();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	// Report staff information
	public static boolean reportStaffInformation() {
		boolean success = false;
		try {
			result = prep_reportStaffInformation.executeQuery();
			if (result.next()) {
				success = true;
				result.beforeFirst();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	// Create billing accounts
	public static void manageBillingAccountAdd(String accountID, String patientID, String visitDate, 
												String payerSSN, String paymentMethod, String cardNumber,
												String registrationFee, String medicationPrescribed,
												String acconmmandationFee, String address) {
		try {
			// Start transaction
			connection.setAutoCommit(false);
			try {
				prep_addPayerInfo.setString(1, payerSSN);
				prep_addPayerInfo.setString(2, address);
				prep_addPayerInfo.executeUpdate();
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
			} finally {
				// Restore normal auto-commit mode
				connection.setAutoCommit(true);
			}
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}

	// Get billing record
	public static boolean showBillingAccount(String accountID) {
		boolean success = false;
		try {
			prep_getBillingAccount.setString(1, accountID);
			result = prep_getBillingAccount.executeQuery();
			// Process resultSet
			if (result.next()) {
				success = true;
				result.beforeFirst();
			}
			System.out.println("\nshowBillingAccount\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return success;
	}

	// Update billing accounts
	public static void manageBillingAccountUpdate(String accountID, String attributeToChange, String valueToChange) {
		try {
			connection.setAutoCommit(true);
			switch (attributeToChange.toUpperCase()){
				case "BILLINGADDRESS":
					prep_updateCheckinEndDate.setString(1, valueToChange);
					prep_updateCheckinEndDate.setString(2, accountID);
					prep_updateTestEndDate.executeUpdate();
					break;
				case "PAYMENTTYPE":
					prep_updateBillingAccountPaymentType.setString(1, valueToChange);
					prep_updateBillingAccountPaymentType.setString(2, accountID);
					prep_updateBillingAccountPaymentType.executeUpdate();
					break;
				case "CARDNUMBER":
					prep_updateBillingAccountCardNumber.setString(1, valueToChange);
					prep_updateBillingAccountCardNumber.setString(2, accountID);
					prep_updateBillingAccountCardNumber.executeUpdate();
					break;
				case "REGISTRATIONFEE":
					prep_updateBillingAccountRegistrationFee.setDouble(1, Double.parseDouble(valueToChange));
					prep_updateBillingAccountRegistrationFee.setString(2, accountID);
					prep_updateBillingAccountRegistrationFee.executeUpdate();
				case "ACCOMMANDATIONFEE":
					prep_updateBillingAccountAccommandationFee.setDouble(1, Double.parseDouble(valueToChange));
					prep_updateBillingAccountAccommandationFee.setString(2, accountID);
					prep_updateBillingAccountAccommandationFee.executeUpdate();
				case "MEDICATIONPRESCRIBED":
					prep_updateBillingAccountMedicationPrescribed.setBoolean(1, Boolean.parseBoolean(valueToChange));
					prep_updateBillingAccountMedicationPrescribed.setString(2, accountID);
					prep_updateBillingAccountMedicationPrescribed.executeUpdate();
				case "VISITDATE":
					prep_updateBillingAccountVisitDate.setDate(1, java.sql.Date.valueOf(valueToChange));
					prep_updateBillingAccountVisitDate.setString(2, accountID);
					prep_updateBillingAccountVisitDate.executeUpdate();
				default:
					System.out.println("\nCannot update the '" + attributeToChange);
					break;
			}
		}
		catch (Throwable err) {
			// error_handler(err);
		}
		
	}

	// Delete billing account
	public static void deleteBillingAccount(String accountID) {
		try {
			// Start transaction
			connection.setAutoCommit(false);
			try {
				prep_deleteBillingAccount.setString(1, accountID);
				prep_deleteBillingAccount.executeUpdate();
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
	
	//GG
	/* Delete ward basic information
	 * 
	 * Return: none
	 * 
	 */
	public static void manageWardDelete(String wardNum) {
		
		try {
			
			connecttion.setAutoCommit(false);
			try {
				prep_deleteWardInfo.setString(1, wardNum);
				prep_deleteWardInfo.executeUpdate();
				connection.commit();
			}
			catch (Throwable err) {
				connection.rollback();
			}
			finally {
				connection.setAutoCommit(true);
			}
		}
		catch (Throwable err) {
			//error_handler(err);
		}
	}
	
	/* Delete bed basic information
	 * 
	 * Return: none
	 * 
	 */
	public static void manageBedDelete(String wardNum, String bedNum) {
		
		try {
			
			connecttion.setAutoCommit(false);
			try {
				prep_deleteBedInfo.setString(1, wardNum);
				prep_deleteBedInfo.setString(2, bedNum);
				prep_deleteBedInfo.executeUpdate();
				connection.commit();
			}
			catch (Throwable err) {
				connection.rollback();
			}
			finally {
				connection.setAutoCommit(true);
			}
		}
		catch (Throwable err) {
			//error_handler(err);
		}
	}
	
	/* Check ward availability
	 * 
	 */
	public static boolean checkWardAvailability() {
		
		boolean success = false;
		try {
			result = prep_checkWardAvailability.executeQuery();
			if (result.next()) {
				succuss = true;
				result.beforeFirst();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return success;
	}
	
	/* Check bed availability
	 * 
	 */
	public static boolean checkBedAvailability() {
		
		boolean success = false;
		try {
			result = prep_checkBedAvailability.executeQuery();
			if (result.next()) {
				succuss = true;
				result.beforeFirst();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return success;
	}
	
	// Create bed information
	public static void manageBedAdd(String wardNum, String bedNum, String patientID) {
		
		try {
			connection.setAutoCommit(false);
			try {
				prep_addBedInfo.setString(1, wardNum);
				prep_addBedInfo.setString(2, bedNum);
				prep_addBedInfo.setString(3, patientID);
				prep_addBedInfo.executeUpdate();
				connection.commit();
			}
			catch (Throwable err) {
				connection.rollback();
			}
			finally {
				connection.setAutoCommit(true);
			}
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}
	
	// Get bed information
	public static boolean showBedInfo(String wardNum, String bedNum) {
		
		boolean success = false;
		try {
			prep_getBedInfo.setString(1, wardNum);
			prep_getBedInfo.setString(2, bedNum);
			result = prep_getBedInfo.executeQuery();
			
			if(result.next()) {
				success = true;
				result.beforeFirst();
			}
			System.out.println("\nShowBedInformation\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return success;
	}
	
	// Assign bed to patient
	public static void manageBedAssign(String patientID, String wardNum, String bedNum) {
		
		try {
			connection.setAutoCommit(true);
			prep_assignBed.setString(1, patientID);
			prep_assignBed.setString(2, wardNum);
			prep_assignBed.setString(3, bedNum);
			prep_assignBed.executeUpdate();
		} catch (Throwable err) {
			//error_handler(err);
		}
	}
	
	// Reserve bed(not sure whether we need this function currently)
	
	// Release bed
	public static void manageBedRelease(String wardNum, String bedNum) {
		
		try {
			connection.setAutoCommit(true);
			prep_releaseBed.setString(1, wardNum);
			prep_releaseBed.setString(2, bedNum);
			prep_releaseBed.executeUpdate();
		} catch (Throwable err) {
			//error_handler(err);
		}
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
