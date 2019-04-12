import javax.xml.transform.Result;
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
    private static final String CMD_INFORMATION_PROCESSING = "INFORMATION PROCESSING"; // CWChen
    private static final String CMD_MEDICAL_RECORDS = "MAINTAINING MEDICAL RECORDS"; //fhy
    private static final String CMD_BILLING_ACCOUNTS = "MAINTAINING BILLING ACCOUNTS"; //GG
    private static final String CMD_REPORTS = "REPORTS"; // ryd
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
    private static final String CMD_BED_GET = "RETRIEVE BED";
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

    private static final String CMD_MEDICAL_HISTORY_BY_PATIENT_REPORT = "MEDICAL HISTORY I";
	private static final String CMD_MEDICAL_HISTORY_BY_TIME_REPORT = "MEDICAL HISTORY II";
    private static final String CMD_WARD_USAGE_STATUS_REPORT = "WARD USAGE STATUS";
	private static final String CMD_BED_USAGE_STATUS_REPORT = "BED USAGE STATUS";
    private static final String CMD_NUMBER_PATIENTS_REPORT = "NUMBER OF PATIENTS";
    private static final String CMD_WARD_USAGE_PERCENT_REPORT = "WARD USAGE PERCENTAGE";
    private static final String CMD_DOCTOR_RESPONS_REPORT = "DOCTOR REPONSIBLITIES";
	private static final String CMD_STAFF_INFO_REPORT = "STAFF INFORMATION";

    private static final String CMD_BILLING_ACCT_ADD = "ADD BILLING ACCOUNT";
    private static final String CMD_BILLING_ACCT_GET = "RETRIEVE BILLING ACCOUNT";
    private static final String CMD_BILLING_ACCT_UPDATE = "UPDATE BILLING ACCOUNT";
    private static final String CMD_BILLING_ACCT_DELETE = "DELETE BILLING ACCOUNT";

	private static Scanner scanner;
	private static String currentMenu;

	private static Connection connection;
	private static Statement statement;
	private static ResultSet result;

	private static String[] tableNames=new String[] { 
			"`AgeInfo`",
			"`Assigned`",
			"`Beds`",
			"`Billing Accounts`",
			"`Check-ins`",
			"`ContactInfo`",
			"`Medical Records`",
			"`Patients`",
			"`PayerInfo`",
			"`PersonInfo`",
			"`Staff`",
			"`Test`",
			"`Treatment`",
			"`Wards`"
	};

	// Prepared Statements pre-declared
	// TO-DO 1: instantiate preparedStatements
	// cchen31
	// Staff
	private static PreparedStatement prep_addStaff;
	private static PreparedStatement prep_getStaff;
	private static PreparedStatement prep_updateStaffName;
	private static PreparedStatement prep_updateStaffAge;
	private static PreparedStatement prep_updateStaffGender;
	private static PreparedStatement prep_updateStaffJobTitle;
	private static PreparedStatement prep_updateStaffProfTitle;
	private static PreparedStatement prep_updateStaffDepart;
	private static PreparedStatement prep_updateStaffPhone;
	private static PreparedStatement prep_updateStaffAddress;
	private static PreparedStatement prep_deleteStaff;
	// Wards
	// TODO: prep_updateWardsPatientsID
	private static PreparedStatement prep_addWards;
	private static PreparedStatement prep_getWards;
	private static PreparedStatement prep_updateWardsCapacity;
	private static PreparedStatement prep_updateWardsCharge;
	private static PreparedStatement prep_updateWardsNurse;
	private static PreparedStatement prep_updateWardsPatientsID; // To reflect what is showed in demo data, but how?
	private static PreparedStatement prep_deleteWards;
	private static PreparedStatement prep_deleteWardInformation;
	// Patients
	private static PreparedStatement prep_addPatients;
	private static PreparedStatement prep_addAgeInfo;
	private static PreparedStatement prep_addContactInfo;
	private static PreparedStatement prep_addPersonInfo;
	private static PreparedStatement prep_getPatients;
	private static PreparedStatement prep_updatePatientsName;
	private static PreparedStatement prep_updatePatientsAge;
	private static PreparedStatement prep_updatePatientsPhone;
	private static PreparedStatement prep_updatePatientsAddress;
	private static PreparedStatement prep_updatePatientsTreatmentPlan; // To reflect what is showed in demo data
	private static PreparedStatement prep_updatePatientsInWard;
	private static PreparedStatement prep_updatePatientsStatus;
	private static PreparedStatement prep_deletePatients;

	// fhy
	private static PreparedStatement prep_addMedicalRecord;
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

	// GG
	// Basic Information - Wards(partial, the rest should be done by others)
	private static PreparedStatement prep_deleteWardInfo;
	private static PreparedStatement prep_checkWardAvailability;
	// private static PreparedStatement prep_assignWard;
	// private static PreparedStatement prep_reserveWard;
	// private static PreparedStatement prep_releaseWard;

	// Basic Information - Beds
	private static PreparedStatement prep_addBedInfo;
	private static PreparedStatement prep_getBedInfo;
	private static PreparedStatement prep_deleteBedInfo;

	// Management - Beds
	private static PreparedStatement prep_assignBed;
	private static PreparedStatement prep_checkBedAvailability;
	private static PreparedStatement prep_checkBedinWardAvailability;    
	// private static PreparedStatement prep_reserveBed;
	private static PreparedStatement prep_releaseBed;
//	private static PreparedStatement prep_deleteBedInfo;
	private static PreparedStatement prep_addAssigned;

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
//				 close(result);
//				 close(statement);
//				 close(connection);
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
              System.out.println(CMD_INFORMATION_PROCESSING);
              System.out.println(CMD_MEDICAL_RECORDS);
              System.out.println(CMD_BILLING_ACCOUNTS);
              System.out.println(CMD_REPORTS);
              System.out.println(CMD_QUIT);
              break;
          case CMD_MEDICAL_RECORDS:
              //fhy      
              System.out.println(CMD_TREATMENT_ADD);
              System.out.println("\t- add a new treatment record");
              System.out.println(CMD_TREATMENT_GETALL);
              System.out.println("\t- retrieve all treatment records");
              System.out.println(CMD_TREATMENT_GET);
              System.out.println("\t- retrieve a treatment record");
              System.out.println(CMD_TREATMENT_UPDATE);
              System.out.println("\t- update treatment record");
              System.out.println(CMD_TEST_ADD);
              System.out.println("\t- add a new test record");
              System.out.println(CMD_TEST_GETALL);
              System.out.println("\t- retrieve all test records");
              System.out.println(CMD_TEST_GET);
              System.out.println("\t- retrieve a test record");
              System.out.println(CMD_TEST_UPDATE);
              System.out.println("\t- update test record");
              System.out.println(CMD_CHECKIN_ADD);
              System.out.println("\t- add a check-in record");  
              System.out.println(CMD_CHECKIN_GETALL);
              System.out.println("\t- retrieve all check-in records");
              System.out.println(CMD_CHECKIN_GET);
              System.out.println("\t- retrieve a check-in record");     
              System.out.println(CMD_QUIT);
              System.out.println("\t- exit the program");
              break;
          case CMD_BILLING_ACCOUNTS:
              //GG    
              System.out.println(CMD_BILLING_ACCT_ADD);
              System.out.println("\t- add billing account");
              System.out.println(CMD_BILLING_ACCT_GET);
              System.out.println("\t- retrieve billing account");
              System.out.println(CMD_BILLING_ACCT_UPDATE);
              System.out.println("\t- update billing account");
              System.out.println(CMD_BILLING_ACCT_DELETE);
              System.out.println("\t- delete billing account");
              break;
          case CMD_INFORMATION_PROCESSING:
              System.out.println(CMD_STAFF_ADD);
              System.out.println("\t- add a new staff");
              System.out.println(CMD_STAFF_UPDATE);
              System.out.println("\t- update a staff");
              System.out.println(CMD_STAFF_DELETE);
              System.out.println("\t- delete a staff");
              System.out.println(CMD_PATIENT_ADD);
              System.out.println("\t- add a new patient");
              System.out.println(CMD_PATIENT_UPDATE);
              System.out.println("\t- update a patient");
              System.out.println(CMD_PATIENT_DELETE);
              System.out.println("\t- delete a patient");
              System.out.println(CMD_WARD_ADD);
              System.out.println("\t- add a new ward");
              System.out.println(CMD_WARD_UPDATE);
              System.out.println("\t- update a ward");
              System.out.println(CMD_WARD_DELETE);
              System.out.println("\t- delete a ward");
              System.out.println(CMD_WARD_CHECK);
              System.out.println("\t- check available wards");
              System.out.println(CMD_BED_CHECK);
              System.out.println("\t- check available beds");
              System.out.println(CMD_WARD_ASSIGN);
              System.out.println("\t- assign available wards based on patient's need");
              System.out.println(CMD_BED_ASSIGN);
              System.out.println("\t- assign available beds to patients");
              System.out.println(CMD_WARD_RESERVE);
              System.out.println("\t- patient reserves a ward");
              System.out.println(CMD_BED_RESERVE);
              System.out.println("\t- patient reserves a bed");
              System.out.println(CMD_WARD_RELEASE);
              System.out.println("\t- release an ward");
              System.out.println(CMD_BED_RELEASE);
              System.out.println("\t- release a bed");
              break;
          case CMD_REPORTS:
            System.out.println(CMD_MEDICAL_HISTORY_BY_PATIENT_REPORT);
            System.out.println("\t- report medical history for a patient");
						System.out.println(CMD_MEDICAL_HISTORY_BY_TIME_REPORT);
            System.out.println("\t- report medical history for a time period");
    				System.out.println(CMD_WARD_USAGE_STATUS_REPORT);
            System.out.println("\t- report ward usage status");
						System.out.println(CMD_BED_USAGE_STATUS_REPORT);
            System.out.println("\t- report bed usage status");
    				System.out.println(CMD_NUMBER_PATIENTS_REPORT);
            System.out.println("\t- report number of patients per month");
    				System.out.println(CMD_WARD_USAGE_PERCENT_REPORT);
            System.out.println("\t- report ward usage precentage");
						System.out.println(CMD_DOCTOR_RESPONS_REPORT);
            System.out.println("\t- report doctors' responsibility");
						System.out.println(CMD_STAFF_INFO_REPORT);
            System.out.println("\t- report staff information grouped by role");
        }
    }

	// TO-DO 2: assign instantiated prepared statements
	public static void generatePreparedStatements() {
		try {
			String sql;
			// cchen31
			// Enter basic information about staff
			sql = "INSERT INTO `Staff` (`staffID`, `name`, `age`, `gender`, `jobTitle`, `profTitle`, `department`, `phone`, `address`)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
			prep_addStaff = connection.prepareStatement(sql);
			// Retrieve basic information about staff
			sql = "SELECT * FROM `Staff`" + " WHERE staffID = ?;";
			prep_getStaff = connection.prepareStatement(sql);
			// Update basic information about staff
			sql = "UPDATE `Staff`" + " SET `name` = ?" + " WHERE staffID = ?;";
			prep_updateStaffName = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + " SET `age` = ?" + " WHERE staffID = ?;";
			prep_updateStaffAge = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + " SET `gender` = ?" + " WHERE staffID = ?;";
			prep_updateStaffGender = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + " SET `jobTitle` = ?" + " WHERE staffID = ?;";
			prep_updateStaffJobTitle = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + " SET `profTitle` = ?" + " WHERE staffID = ?;";
			prep_updateStaffProfTitle = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + " SET `department` = ?" + " WHERE staffID = ?;";
			prep_updateStaffDepart = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + " SET `phone` = ?" + " WHERE staffID = ?;";
			prep_updateStaffPhone = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + " SET `address` = ?" + " WHERE staffID = ?;";
			prep_updateStaffAddress = connection.prepareStatement(sql);
			// Delete basic information about staff
			sql = "DELETE FROM `Staff`" + " WHERE staffID = ?;";
			prep_deleteStaff = connection.prepareStatement(sql);
			// Enter basic information about patients
			sql = "INSERT INTO `Patients` (`patientID`, `SSN`) VALUES (?, ?);";
			prep_addPatients = connection.prepareStatement(sql);
			
			sql	= "INSERT INTO `AgeInfo` (`DOB`, `age`) VALUES (?, ?);";
			prep_addAgeInfo = connection.prepareStatement(sql);
			
			sql	= "INSERT INTO `ContactInfo` (`phone`, `address`) VALUES (?, ?);";
			prep_addContactInfo = connection.prepareStatement(sql);
			
			sql = "INSERT INTO `PersonInfo` (`SSN`, `name`, `DOB`, `gender`, `phone`, " +
					"`processing treatment plan`, `in ward`, `completing treatment`) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
			prep_addPersonInfo = connection.prepareStatement(sql);
			
			// Retrieve basic information about patients
			sql = "SELECT * FROM `Patients` p JOIN `PersonInfo` i ON p.SSN = i.SSN" +
					" JOIN `AgeInfo` a ON i.DOB = a.DOB" +
					" JOIN ContactInfo con ON i.phone = con.phone" +
					" WHERE patientID = ?;";
			prep_getPatients = connection.prepareStatement(sql);
			// Update basic information about patients
			sql = "UPDATE `PersonInfo`" + " SET `name` = ?"
					+ " WHERE SSN IN (SELECT SSN FROM Patients WHERE patientID = ?);";
			prep_updatePatientsName = connection.prepareStatement(sql);
			sql = "UPDATE `PersonInfo`" + " SET `age` = ?"
					+ " WHERE SSN IN (SELECT SSN FROM Patients WHERE patientID = ?);";
			prep_updatePatientsAge = connection.prepareStatement(sql);
			sql = "UPDATE `PersonInfo` p `ContactInfo` ci" +
					" SET p.phone=?, ci.phone=?" +
					" WHERE p.phone=ci.phone AND p.SSN IN (SELECT SSN FROM Patients WHERE patientID = ?);";
			prep_updatePatientsPhone = connection.prepareStatement(sql);
			sql = "UPDATE `ContactInfo`" +
					" SET `address` = ?" +
					" WHERE phone IN (SELECT phone FROM PersonInfo WHERE SSN" +
					" IN (SELECT SSN FROM Patients WHERE patientID = ?));";
			prep_updatePatientsAddress = connection.prepareStatement(sql);
			sql = "UPDATE `PersonInfo`" + " SET `processing treatment plan` = ?"
					+ " WHERE SSN IN (SELECT SSN FROM Patients WHERE patientID = ?);";
			prep_updatePatientsTreatmentPlan = connection.prepareStatement(sql);
			sql = "UPDATE `PersonInfo`" + " SET `in ward` = ?"
					+ " WHERE SSN IN (SELECT SSN FROM Patients WHERE patientID = ?);";
			prep_updatePatientsInWard = connection.prepareStatement(sql);
			sql = "UPDATE `PersonInfo`" + " SET `completing treatment` = ?"
					+ " WHERE SSN IN (SELECT SSN FROM Patients WHERE patientID = ?);";
			prep_updatePatientsStatus = connection.prepareStatement(sql);
			// Delete basic information about patients
			sql = "DELETE `Patients` p, `PersonInfo` i, `AgeInfo` a, `ContactInfo` con FROM p JOIN i ON p.SSN = i.SSN" +
					" JOIN a ON i.DOB = a.DOB" +
					" JOIN  con ON i.phone = con.phone" +
					" WHERE patientID = ?;" ;
			prep_deletePatients = connection.prepareStatement(sql);
			// Enter basic information about wards
			sql = "INSERT INTO `Wards` (`ward number`, `capacity`, `charges per day`, `responsible nurse`)"
					+ " VALUES (?, ?, ?, ?);";
			prep_addWards = connection.prepareStatement(sql);
			// Retrieve basic information about wards
			sql = "SELECT * FROM `Wards`" + " WHERE ward number = ?;";
			prep_getWards = connection.prepareStatement(sql);
			// Update basic information about wards
			sql = "UPDATE `Wards`" + " SET `capacity` = ?" + " WHERE ward number = ?;";
			prep_updateWardsCapacity = connection.prepareStatement(sql);
			sql = "UPDATE `Wards`" + " SET `charges per day` = ?" + " WHERE ward number = ?;";
			prep_updateWardsCharge = connection.prepareStatement(sql);
			sql = "UPDATE `Wards`" + " SET `responsible nurse` = ?" + " WHERE ward number = ?;";
			prep_updateWardsNurse = connection.prepareStatement(sql);
			// fhy
			
			// Add new medical record
			sql = "INSERT INTO `Medical Records` (" + 
					"`recordID`, `patientID`, `startDate`, `endDate`, `responsibleDoctor`) " +
					"VALUES (?,?,?,?,?);";
			prep_addMedicalRecord = connection.prepareStatement(sql);
			
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
			sql = "INSERT INTO `Check-ins` (`recordID`, `wardNumber`, `bedNumber`) " + "VALUES (?, ?, ?); ";
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
					+ "`payerSSN`, `paymentMethod`, `cardNumber`, `registrationFee`, "
					+ "`medicationPrescribed`, `accommandationFee`) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
			prep_addBillingAccount = connection.prepareStatement(sql);

			sql = "INSERT INTO `PayerInfo` (`SSN`, `billingAddress`) " + "VALUES (?, ?);";
			prep_addPayerInfo = connection.prepareStatement(sql);
			
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
			prep_deleteWardInfo = connection.prepareStatement(sql);
			
			// Check availability of wards
			sql = "SELECT * FROM `Wards` WHERE `ward number` IN (SELECT DISTINCT `ward number` " + "FROM `Beds`" + "WHERE ISNULL(patientID)); ";
			prep_checkWardAvailability = connection.prepareStatement(sql);

			// Assign wards:
			// sql = "SELECT COUNT(`bed number`) FROM `Beds` WHERE `ward number` = ?; ";
			// prep_assignWard = connection.prepareStatement(sql);

			// Reserve wards
			// sql = "INSERT INTO `Assigned` (`patientID`, `ward number`, `bed number`,
			// `start-date`, `end-date`) " +
			// "VALUES (?, ?, ?, ?, ?) );" +
			// "UPDATE `Beds` SET "
			// ;
			// prep_reserveWard = connection.prepareStatement(sql);

			// Release wards
			// sql = "";
			// prep_releaseWard = connection.prepareStatement(sql);

			// Add basic information of a bed
			sql = "INSERT INTO `Beds` (`ward number`, `bed number`) " + "VALUES (?, ?); ";
			prep_addBedInfo = connection.prepareStatement(sql);

			// Get basic information of a bed
			sql = "SELECT * FROM `Beds` " + "WHERE `ward number` = ? AND `bed number` = ?; ";
			prep_getBedInfo = connection.prepareStatement(sql);

			// Delete basic information of a bed
			sql = "DELETE FROM `Beds` WHERE `ward number` = ? AND `bed number` = ?; ";
			prep_deleteBedInfo = connection.prepareStatement(sql);

			// Assign beds
			sql = "UPDATE `Beds` SET `patientID` = ? WHERE `ward number` = ? AND `bed number` = ?; ";
			prep_assignBed = connection.prepareStatement(sql);

			// Check availability of beds
			sql = "SELECT * FROM `Beds` " + "WHERE ISNULL(patientID); ";
			prep_checkBedAvailability = connection.prepareStatement(sql);

			// Check availability of beds in an appointed ward
			sql = "SELECT * FROM `Beds` " + "WHERE ISNULL(patientID) AND `ward number ` = ?; ";
			prep_checkBedinWardAvailability = connection.prepareStatement(sql);

			// Reserve beds
			// sql = "INSERT INTO `Assigned` (`patientID`, `ward number`, `bed number`,
			// `start-date`, `end-date`) " +
			// "VALUES (?, ?, ?, ?, ?); ";
			// prep_reserveBed = connection.prepareStatement(sql);

			// Release beds
			sql = "UPDATE `Beds` SET `patientID` = NULL WHERE `ward number` = ? AND `bed number` = ?; ";
			prep_releaseBed = connection.prepareStatement(sql);

			// Create treatment records
			sql = "INSERT `Treatment` (`recordID`, `prescription`, `diagnosisDetails`) " + "VALUES (?, ?, ?); ";
			prep_addTreatmentRecord = connection.prepareStatement(sql);

			//Create assigned
			sql = "INSERT `Assigned` (`patientID`, `ward number`, `bed number`, `start-date`, `end-date`) " + "VALUES (?, ?, ?, ?, ?); ";
			prep_addAssigned = connection.prepareStatement(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//04/09 drop all existing tables before populating tables
	public static void dropAllExistingTables() {
		try {
			statement.executeUpdate("SET FOREIGN_KEY_CHECKS=0;");
			for (String name: tableNames) {
				System.out.println("dropping "+name+"...");
				statement.executeUpdate("DROP TABLE " + name+";");
			}
			statement.executeUpdate("SET FOREIGN_KEY_CHECKS=1;");
		}
		catch(Throwable err) {
			error_handler(err);
		}
	}
	
	// TO-DO 3: create tables
	public static void generateTables() {
		try {
			connection.setAutoCommit(false);
			try {
				// Wayne: Staff, Patients, Wards:
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `Staff` (" +
								"`staffID` VARCHAR(255) NOT NULL, " +
								"`name` VARCHAR(255) NOT NULL, " +
								"`age` INT(3) NOT NULL, " +
								"`gender` VARCHAR(255) NOT NULL, " +
								"`jobTitle` VARCHAR(255) NOT NULL, " +
								"`profTitle` VARCHAR(255) DEFAULT NULL, " +
								"`department` VARCHAR(255) NOT NULL, " +
								"`phone` VARCHAR(255) NOT NULL, " +
								"`address` VARCHAR(255) NOT NULL, " +
								"PRIMARY KEY (`staffID`)" +
								");");
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `AgeInfo` (" +
								"`DOB` datetime NOT NULL, " +
								"`age` INT(2) NOT NULL, " +
								"PRIMARY KEY (`DOB`)" +
								");");
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `ContactInfo` (" +
								"`phone` VARCHAR(255) NOT NULL, " +
								"`address` VARCHAR(255) NOT NULL, " +
								"PRIMARY KEY (`phone`)" +
								");");
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `Patients` (" +
								"`patientID` varchar(255) NOT NULL UNIQUE, " +
								"`SSN` varchar(255) NOT NULL UNIQUE, " +
								"PRIMARY KEY (`patientID`) " +
								");");
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `PersonInfo` (" +
								"`SSN` varchar(255) NOT NULL UNIQUE, " +
								"`name` varchar(255) NOT NULL, " +
								"`DOB` datetime NOT NULL, " +
								"`gender` VARCHAR(255) NOT NULL, " +
								"`phone` VARCHAR(255) NOT NULL, " +
								"`processing treatment plan` VARCHAR(255) NOT NULL, " +
								"`in ward` BIT, " +
								"`completing treatment` BIT, " +
								"PRIMARY KEY (`SSN`), " +
								"FOREIGN KEY (`DOB`) REFERENCES AgeInfo(`DOB`), " +
								"FOREIGN KEY (`phone`) REFERENCES ContactInfo(`phone`)" +
								");");
				
				// GG
				// Wards
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `Wards` (" +
								"`ward number` VARCHAR(255) NOT NULL UNIQUE, " +
								"`capacity` INT NOT NULL, " +
								"`charges per day` INT NOT NULL, " +
								"`responsible nurse` VARCHAR(255) DEFAULT NULL, " +
								"PRIMARY KEY (`ward number`), " +
								"CONSTRAINT fk_ward FOREIGN KEY (`responsible nurse`) REFERENCES Staff(`staffID`) " +
								"ON DELETE SET NULL" +
								");");
				//fhy: Medical Records, Treatment, Test, Check-ins
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `Medical Records` (" +
						"`recordID` VARCHAR(255) NOT NULL UNIQUE, " +
						"`patientID` VARCHAR(255) NOT NULL, " +
						"`startDate` DATETIME NOT NULL, " +
						"`endDate` DATETIME DEFAULT NULL, " +
						"`responsibleDoctor` VARCHAR(255) NOT NULL, " +
						"PRIMARY KEY (`recordID`), " +
						"FOREIGN KEY (`patientID`) REFERENCES Patients(`patientID`), " +
						"FOREIGN KEY (`responsibleDoctor`) REFERENCES Staff(`staffID`)" +
						");");
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `Treatment` (" +
						"`recordID` VARCHAR(255) NOT NULL UNIQUE," +
						"`prescription` VARCHAR(255) NOT NULL," +
						"`diagnosisDetails` VARCHAR(255) NOT NULL, " +
						"PRIMARY KEY (`recordID`), " +
						"FOREIGN KEY (`recordID`) REFERENCES `Medical Records`(`recordID`)" +
						");");
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `Test` (" +
						"`recordID` VARCHAR(255) NOT NULL UNIQUE," +
						"`testType` VARCHAR(255) NOT NULL," +
						"`testResult` VARCHAR(255) NOT NULL, " +
						"PRIMARY KEY (`recordID`), " +
						"FOREIGN KEY (`recordID`) REFERENCES `Medical Records`(`recordID`)" +
						");");
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `Check-ins` (" +
						"`recordID` VARCHAR(255) NOT NULL UNIQUE," +
						"`wardNumber` VARCHAR(255) DEFAULT NULL," +
						"`bedNumber` VARCHAR(255) DEFAULT NULL," +
						"PRIMARY KEY (`recordID`), " +
						"FOREIGN KEY (`recordID`) REFERENCES `Medical Records`(`recordID`), " +
						"FOREIGN KEY (`wardNumber`) REFERENCES Wards(`ward number`)" +
						");");

				// Yudong
				// Billing accounts && PayerInfo
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `PayerInfo` ( " + 
						"`SSN` VARCHAR(255) NOT NULL UNIQUE, " + 
						"`billingAddress` VARCHAR(255) NOT NULL, " + 
						"PRIMARY KEY (`SSN`) " + 
						");");

				statement.executeUpdate("CREATE TABLE IF NOT EXISTS `Billing Accounts` (" + 
						"`accountID` VARCHAR(255) NOT NULL UNIQUE," + "`patientID` VARCHAR(255) NOT NULL," + 
						"`visitDate` datetime NOT NULL," + "`payerSSN` VARCHAR(255) NOT NULL," + 
						"`paymentMethod` VARCHAR(255) NOT NULL," + "`cardNumber` VARCHAR(255) DEFAULT NULL," + 
						"`registrationFee` DOUBLE NOT NULL," + "`medicationPrescribed` BIT DEFAULT NULL," + 
						"`accommandationFee` DOUBLE NOT NULL," + " PRIMARY KEY (`accountID`)," + 
						"FOREIGN KEY (`patientID`) REFERENCES Patients(`patientID`)," + 
						"FOREIGN KEY (`payerSSN`) REFERENCES PayerInfo(`SSN`)" + ");");

				// GG
				// Beds
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `Beds` (" +
						"`ward number` VARCHAR(255) NOT NULL," +
						"`bed number` VARCHAR(255) NOT NULL," +
						"PRIMARY KEY (`ward number`, `bed number`), " +
						"CONSTRAINT `fk_bedwn` FOREIGN KEY (`ward number`) REFERENCES Wards(`ward number`) ON DELETE CASCADE" +
						");");
				// Assigned
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS `Assigned` (" +
						"`patientID` VARCHAR(255) NOT NULL," +
						"`ward number` VARCHAR(255) NOT NULL," +
						"`bed number` VARCHAR(255) NOT NULL," +
						"`start-date` DATETIME NOT NULL," +
						"`end-date` DATETIME DEFAULT NULL," +
						"CONSTRAINT pk_assign PRIMARY KEY (`patientID`, `ward number`, `bed number`)," +
						"CONSTRAINT `fk_assignpi` FOREIGN KEY (`patientID`) REFERENCES Patients(`patientID`) ON DELETE CASCADE, " +
						"CONSTRAINT `fk_assginwb` FOREIGN KEY (`ward number`, `bed number`) REFERENCES Beds(`ward number`, `bed number`) " +
							"ON DELETE CASCADE" +
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
	public static void populateStaffTable() {
		addStaff("100", "Mary", "40", "Female", "Doctor", "senior", "Neurology", "654",
				"90 ABC St , Raleigh NC 27");
		addStaff("101", "John", "45", "Male", "Billing Staff", "", "Office", "564",
				"798 XYZ St , Rochester NY 54");
		addStaff("102", "Carol", "55", "Female", "Nurse", "", "ER", "911", 
				"351 MH St , Greensboro NC 27");
		addStaff("103", "Emma", "55", "Female", "Doctor", "Senior surgeon", "Oncological Surgery", "546", 
				"49 ABC St , Raleigh NC 27");
		addStaff("104", "Ava", "55", "Female", "Front Desk Staff", "", "Office", "777", 
				"425 RG St , Raleigh NC 27");
		addStaff("105", "Peter", "52", "Male", "Doctor", "Anesthetist", "Oncological Surgery", "724",
				"475 RG St , Raleigh NC 27");
		addStaff("106", "Olivia", "27", "Female", "Nurse", "", "Neurology", "799", 
				"325 PD St , Raleigh NC 27");
	}
	
	public static void populatePatientsTable() {
		addPatient("1001", "000-01-1234", "David", "1980-01-30", "Male", "39", "919-123-3324",
				"69 ABC St , Raleigh NC 27730", "20", "yes", "no");
		addPatient("1002", "000-02-1234", "Sarah", "1971-01-30", "Female", "48", "919-563-3478",
				"81 DEF St , Cary NC 27519", "20", "yes", "no");
		addPatient("1003", "000-03-1234", "Joseph", "1987-01-30", "Male", "32", "919-957-2199",
				"31 OPG St , Cary NC 27519", "10", "yes", "no");
		addPatient("1004", "000-04-1234", "Lucy", "1985-01-30", "Female", "34", "919-838-7123",
				"10 TBC St , Raleigh NC 27730", "5", "no", "yes");
	}
	
	public static void populateWardsTable() {
		// TODO: how to add multiple patients' IDs?
		addWard("001", "4", "50", "102");
		addWard("002", "4", "50", "102");
		addWard("003", "2", "100", "106");
		addWard("004", "2", "100", "106");
	}
	
	public static void populateBedsTable() {
		manageBedAdd("001", "1");
		manageBedAdd("001", "2");
		manageBedAdd("001", "3");
		manageBedAdd("001", "4");
		manageBedAdd("002", "1");
		manageBedAdd("002", "2");
		manageBedAdd("002", "3");
		manageBedAdd("002", "4");
		manageBedAdd("003", "1");
		manageBedAdd("003", "2");
		manageBedAdd("004", "1");
		manageBedAdd("004", "2");
	}

	// TO FIX: EMPTY DATE
	public static void populateAssignedTable() {
		manageAssignedAdd("1001", "001", "1", "2019-03-01", "2019-12-21");
		manageAssignedAdd("1002", "002", "1", "2019-03-10", "2019-12-21");
		manageAssignedAdd("1003", "001", "2", "2019-03-15", "2019-12-21");
		manageAssignedAdd("1004", "003", "1", "2019-03-17", "2019-03-21");
	}

	// TO FIX: EMPTY DATE
	public static void populateMedicalRecordsTable() {
		addMedicalRecord("1", "1001", "2019-03-01", "2019-12-21", "100");
		addMedicalRecord("2", "1002", "2019-03-10", "2019-12-21", "100");
		addMedicalRecord("3", "1003", "2019-03-15", "2019-12-21", "100");
		addMedicalRecord("4", "1004", "2019-03-17", "2019-03-21", "103");
	}
	
	public static void populatTreatmentTable() {
		manageTreatmentRecordAdd("1", "nervine", "Hospitalization");
		manageTreatmentRecordAdd("2", "nervine", "Hospitalization");
		manageTreatmentRecordAdd("3", "nervine", "Hospitalization");
		manageTreatmentRecordAdd("4", "analgestic", "Surgeon, Hospitalization");
	}
	
	public static void populatTestTable() {
		
	}
	
	// TO FIX: EMPTY DATE
	public static void populatCheckinTable() {
		manageCheckinRecordAdd("1", "001", "1", "1001", "2019-03-01", "2019-12-21", "104");
		manageCheckinRecordAdd("2", "002", "1", "1002", "2019-03-10", "2019-12-21", "104");
		manageCheckinRecordAdd("3", "001", "2", "1003", "2019-03-15", "2019-12-21", "104");
		manageCheckinRecordAdd("4", "003", "1", "1004", "2019-03-17", "2019-03-21", "104");
	}
	
	// TO FIX: EMPTY DOUBLE
	public static void populateBillingAccountsTable() {
		manageBillingAccountAdd("1001", "1001", "2019-03-01", "000-01-1234", "Credit Card",
			"4044875409613234", "100", "yes", "0", "69 ABC St , Raleigh NC 27730");
		manageBillingAccountAdd("1002", "1002", "2019-03-10", "000-02-1234", "Credit Card",
			"4401982398541143", "100", "yes", "0", "81 DEF St , Cary NC 27519");
		manageBillingAccountAdd("1003", "1003", "2019-03-15", "000-03-1234", "Check",
			"0", "100", "yes", "0", "31 OPG St , Cary NC 27519");
		manageBillingAccountAdd("1004", "1004", "2019-03-17", "000-04-1234", "Credit Card",
			"4044987612349123", "100", "yes", "400", "10 TBC St. Raleigh NC 27730");
	}

	// TO-DO 5: define and implement other functions
	// cchen31
	// Show an appointed row of Staff
	private static void printStaffRow(ResultSet rs) {
		try {
			String staffID = rs.getString("staffID");
			String name = rs.getString("name");
			int age = rs.getInt("age");
			String gender = rs.getString("gender");
			String jobTitle = rs.getString("jobTitle");
			String profTitle = rs.getString("profTitle");
			String department = rs.getString("department");
			String phone = rs.getString("phone");
			String address = rs.getString("address");
			System.out.println("Staff ID: " + staffID + ", name: " + name + ", age: " + age + ", gender: " + gender +
					", job title: " + jobTitle + ", professional title: " + profTitle + ", department: " + department +
					", phone: " + phone + ", address: " + address);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Show an appointed row of patient
	private static void printPatientsRow(ResultSet rs) {
		try {
			String patientID = rs.getString("patientID");
			String SSN = rs.getString("SSN");
			String name = rs.getString("name");
			String gender = rs.getString("gender");
			String DOB =  rs.getDate("DOB").toString();
			int age = rs.getInt("age");
			String processing = rs.getString("processing treatment plan");
			String completing = rs.getString("completing treatment").equals("true")? "yes": "no";
			String inWard = rs.getString("in ward").equals("true")? "yes": "no";
			String phone = rs.getString("phone");
			String address = rs.getString("address");
			System.out.println("Patient ID: " + patientID + ", SSN: " + SSN + ", name: " + name + ", date of birth: " + DOB +
					", gender: " + gender + ", age: " + age + ", phone number: " + phone + ", address: " + address + ", processing treatment plan: " +
					processing + ", in ward: " + inWard + ", completing treatment: " + completing +
					phone + "\t" + address + "\t");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Show an appointed row of wards
	private static void printWardsRow(ResultSet rs) {
		try {
			// TODO: print patients' IDs too
			String wardNumber = rs.getString("ward number");
			int capacity = rs.getInt("capacity");
			int dayCharge = rs.getInt("charges per day");
			String nurse = rs.getString("responsible nurse");
			System.out.println("Ward number: " + wardNumber + ", capacity: " + capacity + ", charges per day: " + dayCharge + ", responsible nurse: " + nurse);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// Add a new staff
	// need to deal with duplicate add?
	public static void addStaff(String staffID, String name, String age, String gender, String jobTitle, String profTitle,
								String department, String phone, String address) {
		try {
			connection.setAutoCommit(false);
			try {
				prep_addStaff.setString(1, staffID);
				prep_addStaff.setString(2, name);
				prep_addStaff.setInt(3, Integer.parseInt(age));
				prep_addStaff.setString(4, gender);
				prep_addStaff.setString(5, jobTitle);
				prep_addStaff.setString(6, profTitle);
				prep_addStaff.setString(7, department);
				prep_addStaff.setString(8, phone);
				prep_addStaff.setString(9, address);
				prep_addStaff.executeUpdate();
				connection.commit();
			} catch (SQLException e) {
				connection.rollback();
				e.printStackTrace();
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// do we need another way to handle an exception?
		}
	}
	// Get staff info
	public static void getStaff(String staffID) {
		try {
			prep_getStaff.setString(1, staffID);
			ResultSet rs = prep_getStaff.executeQuery();
			if (rs.next()) {
				printStaffRow(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// update the value of an appointed field of an staff
	public static void updateStaff(String staffID, String attributeToChange, String newValue) {
		try {
			connection.setAutoCommit(false);
			try {
				switch (attributeToChange.toUpperCase()) {

					case "NAME":
						prep_updateStaffName.setString(1, newValue);
						prep_updateStaffName.setString(2, staffID);
						prep_updateStaffName.executeUpdate();
						break;
					case "AGE":
						prep_updateStaffAge.setInt(1, Integer.parseInt(newValue));
						prep_updateStaffAge.setString(2, staffID);
						prep_updateStaffAge.executeUpdate();
						break;
					case "GENDER":
						prep_updateStaffGender.setString(1, newValue);
						prep_updateStaffGender.setString(2, staffID);
						prep_updateStaffGender.executeUpdate();
						break;
					case "JOB TITLE":
						prep_updateStaffJobTitle.setString(1, newValue);
						prep_updateStaffJobTitle.setString(2, staffID);
						prep_updateStaffJobTitle.executeUpdate();
						break;
					case "PROFESSIONAL TITLE":
						prep_updateStaffProfTitle.setString(1, newValue);
						prep_updateStaffProfTitle.setString(2, staffID);
						prep_updateStaffProfTitle.executeUpdate();
						break;
					case "DEPARTMENT":
						prep_updateStaffDepart.setString(1, newValue);
						prep_updateStaffDepart.setString(2, staffID);
						prep_updateStaffDepart.executeUpdate();
						break;
					case "PHONE":
						prep_updateStaffPhone.setString(1, newValue);
						prep_updateStaffPhone.setString(2, staffID);
						prep_updateStaffPhone.executeUpdate();
						break;
					case "ADDRESS":
						prep_updateStaffAddress.setString(1, newValue);
						prep_updateStaffAddress.setString(2, staffID);
						prep_updateStaffAddress.executeUpdate();
						break;
					default:
						System.out.println("Cannot update the field " + attributeToChange + " for staff " + staffID + " .");
						break;
				}
				connection.commit();
			} catch (SQLException e) {
				connection.rollback();
				e.printStackTrace();
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// delete an appointed staff
	public static void deleteStaff(String staffID) {
		try {
			connection.setAutoCommit(false);
			try {
				prep_deleteStaff.setString(1, staffID);
				prep_deleteStaff.executeUpdate();
				connection.commit();
			} catch (SQLException e) {
				connection.rollback();
				e.printStackTrace();
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// Add a new patient
	public static void addPatient(String patientID, String SSN, String name, String DOB, String gender, String age, String phone,
								  String address, String processing, String inWard, String completing) {
		try {
			connection.setAutoCommit(false);
			try {
				prep_addPatients.setString(1, patientID);
				prep_addPatients.setString(2, SSN);
				
				prep_addAgeInfo.setDate(1, java.sql.Date.valueOf(DOB));
				prep_addAgeInfo.setInt(2, Integer.parseInt(age));
				
				prep_addContactInfo.setString(1, phone);
				prep_addContactInfo.setString(2, address);                
				
				prep_addPersonInfo.setString(1, SSN);
				prep_addPersonInfo.setString(2, name);
				prep_addPersonInfo.setDate(3, java.sql.Date.valueOf(DOB));
				prep_addPersonInfo.setString(4, gender);
				prep_addPersonInfo.setString(5, phone);
				prep_addPersonInfo.setString(6, processing);
				prep_addPersonInfo.setBoolean(7, inWard.equals("yes")? true : false);
				prep_addPersonInfo.setBoolean(8, completing.equals("yes")? true : false);

				// To-do: make use of variable treatmentPlan and wardNum. By calling prep_addTreatmentRecord and prep_assignWard here?
				prep_addPatients.executeUpdate();
				prep_addAgeInfo.executeUpdate();
				prep_addContactInfo.executeUpdate();
				prep_addPersonInfo.executeUpdate();
				connection.commit();
			} catch (SQLException e) {
				connection.rollback();
				e.printStackTrace();
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// To do: do we need another way to handle an exception?
		}
	}
	// Get patient info
	public static void getPatient(String patientID) {
		try {
			prep_getPatients.setString(1, patientID);
			ResultSet rs = prep_getPatients.executeQuery();
			if (rs.next()) {
				printPatientsRow(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// update the value of an appointed field of a patient
	public static void updatePatient(String patientID, String attributeChanged, String newValue) {
		try {
			connection.setAutoCommit(true);
			try {
				switch (attributeChanged.toUpperCase()) {

					case "NAME":
						prep_updatePatientsName.setString(1, newValue);
						prep_updatePatientsName.setString(2, patientID);
						prep_updatePatientsName.executeUpdate();
						break;
					case "AGE":
						prep_updatePatientsAge.setInt(1, Integer.parseInt(newValue));
						prep_updatePatientsAge.setString(2, patientID);
						prep_updatePatientsAge.executeUpdate();
						break;
					case "ADDRESS":
						prep_updatePatientsAddress.setString(1, newValue);
						prep_updatePatientsAddress.setString(2, patientID);
						prep_updatePatientsAddress.executeUpdate();
						break;
					case "PHONE":
						prep_updatePatientsPhone.setString(1, newValue);
						prep_updatePatientsPhone.setString(2, newValue);
						prep_updatePatientsPhone.setString(3, patientID);
						prep_updatePatientsPhone.executeUpdate();
						break;
					case "PROCESSING TREATMENT PLAN":
						prep_updatePatientsTreatmentPlan.setString(1, newValue);
						prep_updatePatientsTreatmentPlan.setString(2, patientID);
						prep_updatePatientsTreatmentPlan.executeUpdate();
						break;
					case "IN WARD":
						prep_updatePatientsInWard.setBoolean(1, newValue.equals("yes")? true : false);
						prep_updatePatientsInWard.setString(2, patientID);
						prep_updatePatientsInWard.executeUpdate();
						break;
					case "COMPLETING TREATMENT":
						prep_updatePatientsStatus.setBoolean(1, newValue.equals("yes")? true : false);
						prep_updatePatientsStatus.setString(2, patientID);
						prep_updatePatientsStatus.executeUpdate();
						break;
					default:
						System.out.println("Cannot update the field " + attributeChanged + " for patient " + patientID + " .");
						break;
				}
				connection.commit();
			} catch (SQLException e) {
				connection.rollback();
				e.printStackTrace();
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// delete an appointed patient
	public static void deletePatient(String patientID) {
		try {
			connection.setAutoCommit(false);
			try {
				prep_deletePatients.setString(1, patientID);
				// To-do: need to consider the effect on everything related to this patient? e.g. release bed, update record, etc.
				prep_deletePatients.executeUpdate();
				connection.commit();
			} catch (SQLException e) {
				connection.rollback();
				e.printStackTrace();
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// Add a new ward
	public static void addWard(String wardNumber, String capacity, String daycharge, String responsibleNurse) {
		try {
			connection.setAutoCommit(false);
			try {
				prep_addWards.setString(1, wardNumber);
				prep_addWards.setInt(2, Integer.valueOf(capacity));
				prep_addWards.setInt(3, Integer.valueOf(daycharge));
				prep_addWards.setString(4, responsibleNurse);
				prep_addWards.executeUpdate();
				connection.commit();
			} catch (SQLException e) {
				connection.rollback();
				e.printStackTrace();
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// To-do: do we need another way to handle an exception?
		}
	}
	// Get ward info
	public static void getWard(String wardNumber) {
		try {
			prep_getWards.setString(1, wardNumber);
			// To-do: need to get all the patients' SSN too?!
			ResultSet rs = prep_getWards.executeQuery();
			if (rs.next()) {
				printWardsRow(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// update the value of an appointed field of a ward
	public static void updateWard(String wardNumber, String attributeChanged, String newValue) {
		try {
			connection.setAutoCommit(true);
			try {
				switch (attributeChanged.toUpperCase()) {

					case "CAPACITY":
						prep_updateWardsCapacity.setInt(1, Integer.valueOf(newValue));
						prep_updateWardsCapacity.setString(2, wardNumber);
						prep_updateWardsCapacity.executeUpdate();
						break;
					case "CHARGE PER DAY":
						prep_updateWardsCharge.setInt(1, Integer.valueOf(newValue));
						prep_updateWardsCharge.setString(2, wardNumber);
						prep_updateWardsCharge.executeUpdate();
						break;
					case "RESPONSIBLE NURSE":
						prep_updateWardsNurse.setString(1, newValue);
						prep_updateWardsNurse.setString(2, wardNumber);
						prep_updateWardsNurse.executeUpdate();
						break;
					// TODO: how to update patients' IDs
					default:
						System.out.println("Cannot update the field " + attributeChanged + " for ward " + wardNumber + " .");
						break;
				}
				connection.commit();
			} catch (SQLException e) {
				connection.rollback();
				e.printStackTrace();
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//fhy support_printQueryResultSet, error_handler not yet implemented
	//1
	public static boolean showAllTreatmentRecords(String patientID){
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
	
	public static void addMedicalRecord(String recordID, String patientID, String startDate, String endDate, String resDoc) {
		try {
			connection.setAutoCommit(false);
			
			try {
				prep_addMedicalRecord.setString(1, recordID);
				prep_addMedicalRecord.setString(2, patientID);
				prep_addMedicalRecord.setDate(3, java.sql.Date.valueOf(startDate));
				prep_addMedicalRecord.setDate(4, java.sql.Date.valueOf(endDate));
				prep_addMedicalRecord.setString(5, resDoc);
				prep_addMedicalRecord.executeUpdate();
				connection.commit();
			} catch (Exception e) {
				connection.rollback();
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// Restore normal auto-commit mode
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				
				result.beforeFirst();
			}
			success = true;
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
			result.beforeFirst();
			while (result.next()) {
				System.out.print("staff ID :" + result.getString("staffID") + " ");
				System.out.print("name :" + result.getString("name") + " ");
				System.out.print("age :" + result.getInt("age") + " ");
				System.out.print("gender :" + result.getString("gender") + " ");
				System.out.print("job title :" + result.getString("jobTitle") + " ");
				System.out.print("professional title:" + result.getString("profTitle") + " ");
				System.out.print("department :" + result.getString("department"));
				System.out.print("phone :" + result.getString("phone"));
				System.out.print("address:" + result.getString("address"));
			}
			success = true;
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
												String accommandationFee, String address) {
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
				prep_addBillingAccount.setBoolean(8, medicationPrescribed.equals("yes")?true:false);
				prep_addBillingAccount.setDouble(9, Double.parseDouble(accommandationFee));
				prep_addBillingAccount.executeUpdate();
				connection.commit();
			}
			catch (Throwable err) {
				// Roll back the entire transaction
				err.printStackTrace();
				connection.rollback();
			} finally {
				// Restore normal auto-commit mode
				connection.setAutoCommit(true);
			}
		}
		catch (Throwable err) {
			err.printStackTrace();
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
			
			connection.setAutoCommit(false);
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
			connection.setAutoCommit(false);
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
				success = true;
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
				success = true;
				result.beforeFirst();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return success;
	}
	
	// Create bed information
	public static void manageBedAdd(String wardNum, String bedNum) {
		
		try {
			connection.setAutoCommit(false);
			try {
				prep_addBedInfo.setString(1, wardNum);
				prep_addBedInfo.setString(2, bedNum);
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

	// Assigned
	public static void manageAssignedAdd(String patientID, String wardNum, String bedNum, String start, String end){

		try{
			connection.setAutoCommit(false);
			try{
				connection.setAutoCommit(true);
				prep_addAssigned.setString(1, patientID);
				prep_addAssigned.setString(2, wardNum);
				prep_addAssigned.setString(3, bedNum);
				prep_addAssigned.setDate(4, java.sql.Date.valueOf(start));
				prep_addAssigned.setDate(5, java.sql.Date.valueOf(end));
				prep_addAssigned.executeUpdate();
				connection.commit();
			}
			catch (Throwable err) {
				err.printStackTrace();
				connection.rollback();
			}
			finally {
				connection.setAutoCommit(true);
			}
		}
		catch (Throwable err){
			err.printStackTrace();
			error_handler(err);
		}

	}

	// Create treatment records
	public static void manageTreatmentRecordAdd(String recordID, String pres, String diag){

		try{
			connection.setAutoCommit(false);
			try{
				
				prep_addTreatmentRecord.setString(1, recordID);
				prep_addTreatmentRecord.setString(2, pres);
				prep_addTreatmentRecord.setString(3, diag);
				prep_addTreatmentRecord.executeUpdate();
				connection.commit();
			}
			catch (Throwable err){
				connection.rollback();
			}
			finally{
				connection.setAutoCommit(true);
			}
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}

	//fhy
	public static void userTreatmentAdd(){
		//manageTreatmentRecordAdd(String recordID, String pres, String diag)
	}
	public static void userTreatmentGetAll(){
		//showAllTreatmentRecords(String patientID)
	}
	public static void userTreatmentGet(){
		//showTreatmentRecord(String recordID)
	}
	public static void userTreatmentUpdate(){
		//manageTreatmentUpdate(String recordID, String attributeToChange, String valueToChange)
	}
	public static void userTestAdd(){
		//manageTestRecordAdd(String recordID, String testType, String testResult, String patientID,
		//			String startDate, String endDate, String responsibleDoctor)
	}
	public static void userTestGetAll(){
		//showAllTestRecords(String patientID)
	}
	public static void userTestGet(){
		//showTestRecord(String recordID)
	}
	public static void userTestUpdate(){
		//manageTestUpdate(String recordID, String attributeToChange, String valueToChange)
	}
	public static void userCheckinAdd(){
		//manageCheckinRecordAdd(String recordID, String wardNumber, String bedNumber, String patientID,
		//			String startDate, String endDate, String responsibleDoctor)
	}
	public static void userCheckinGetAll(){
		//showAllCheckinRecords(String patientID)
	}
	public static void userCheckinGet(){
		//showCheckinRecord(String recordID)
	}
	public static void userCheckinUpdate(){
		//manageCheckinUpdate(String recordID, String attributeToChange, String valueToChange)
	}


	
	/*
	 * begin user-interaction methods
	 * */


	/*
	 * ChuWen - Information Processing
	 * Enter information for a new staff
	 * */
	public static void userStaffAdd() {
		//Declare local variables
		String staffID;
		String name;
		String age;
		String gender;
		String jobTitle;
		String profTitle;
		String department;
		String phone;
		String address;
		try {
			//Get staff id for the new staff
			System.out.println("\nEnter the staff ID of the new staff:\n");
			staffID = scanner.nextLine();
			//Get name
			System.out.println("\nEnter the name of the new staff:\n");
			name = scanner.nextLine();
			//Get age
			System.out.println("\nEnter the age of the new staff:\n");
			age = scanner.nextLine();
			//Get gender
			System.out.println("\nEnter the gender of the new staff:\n");
			gender = scanner.nextLine();
			//Get job title
			System.out.println("\nEnter the job title of the new staff:\n");
			jobTitle = scanner.nextLine();
			//Get professional title
			System.out.println("\nEnter the professional title of the new staff:\n");
			profTitle = scanner.nextLine();
			//Get department
			System.out.println("\nEnter the department of the new staff:\n");
			department = scanner.nextLine();
			//Get phone
			System.out.println("\nEnter the phone of the new staff:\n");
			phone = scanner.nextLine();
			//Get address
			System.out.println("\nEnter the address of the new staff:\n");
			address = scanner.nextLine();
			//call function that interacts with the Database
			addStaff(staffID, name, age, gender, jobTitle, profTitle, department, phone, address);
			System.out.println("A new staff is added successfully!");
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}

	/*
	 * Update an attribute of an appointed staff
	 * */
	public static void userStaffUpdate() {
		//Declare local variables
		String staffID;
		String attrToChange;
		String valueToChange;
		try {
			//Get staff id
			System.out.println("\nEnter the staff id of the staff you want to update:\n");
			staffID = scanner.nextLine();

			//Print the staff information you plan to update
			System.out.println("\nThe staff information you have chosen:\n");
			getStaff(staffID);

			//Get attribute to change
			//Print all possible attributes can be changed
			System.out.println("\nPlease select the attribute you wish to update[NAME, AGE, GENDER, JOBTITLE, PROFESSIONAL TITLE, DEPARTMENT, PHONE, ADDRESS]:\n");
			attrToChange = scanner.nextLine();
			//Get value to change
			System.out.println("\nEnter the new value:\n");
			valueToChange = scanner.nextLine();
			//Call method that interacts with the Database
			updateStaff(staffID, attrToChange, valueToChange);
			System.out.println("The staff is updated successfully!");
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}

	/*
	 * Delete an appointed staff
	 * */
	public static void userStaffDelete() {
		//Declare local variables
		String staffID;
		try {
			//Get staff id
			System.out.println("\nEnter the staff id of the staff you want to delete:\n");
			staffID = scanner.nextLine();
			//Print the staff information you plan to delete
			System.out.println("\nThe staff information you have chosen:\n");
			getStaff(staffID);
			//Call method that interacts with the Database
			deleteStaff(staffID);
			System.out.println("The staff is deleted successfully!");
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}

	/*
	 * Enter information for a new patient
	 * */
	public static void userPatientAdd() {
		//Declare local variables
		String patientID;
		String SSN;
		String name;
		String gender;
		String DOB;
		String age;
		String address;
		String phone;
		String treatmentPlan;
		String inWard;
		String CompletingTreatment;
		try {
			//Get patient id for the new patient
			System.out.println("\nEnter the patient ID of the new patient:\n");
			patientID = scanner.nextLine();
			//Get SSN
			System.out.println("\nEnter the SSN of the new patient:\n");
			SSN = scanner.nextLine();
			//Get name
			System.out.println("\nEnter the name of the new patient:\n");
			name = scanner.nextLine();
			//Get gender
			System.out.println("\nEnter the gender of the new patient:\n");
			gender = scanner.nextLine();
			//Get DOB
			System.out.println("\nEnter the date of birth of the new patient:\n");
			DOB = scanner.nextLine();
			//Get age
			System.out.println("\nEnter the age of the new patient:\n");
			age = scanner.nextLine();
			//Get address
			System.out.println("\nEnter the address of the new patient:\n");
			address = scanner.nextLine();
			//Get phone
			System.out.println("\nEnter the phone of the new patient:\n");
			phone = scanner.nextLine();
			//Get treatmentPlan
			System.out.println("\nEnter the processing treatment plan of the new patient:\n");
			treatmentPlan = scanner.nextLine();
			//Get inWard
			System.out.println("\nEnter the in ward status of the new patient:\n");
			inWard = scanner.nextLine();
			//Get CompletingTreatment
			System.out.println("\nEnter the treatment status of the new patient:\n");
			CompletingTreatment = scanner.nextLine();
			//call function that interacts with the Database
			addPatient(patientID, SSN, name, DOB, gender, age, phone,
					address, treatmentPlan, inWard, CompletingTreatment);
			System.out.println("A new patient is added successfully!");
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}

	/*
	 * Update an attribute of an appointed patient
	 * */
	public static void userPatientUpdate() {
		//Declare local variables
		String patientID;
		String attrToChange;
		String valueToChange;
		try {
			//Get patient id
			System.out.println("\nEnter the patient id of the patient you want to update:\n");
			patientID = scanner.nextLine();

			//Print the patient information you plan to update
			System.out.println("\nThe patient information you have chosen:\n");
			getPatient(patientID);

			//Get attribute to change
			//Print all possible attributes can be changed
			System.out.println("\nPlease select the attribute you wish to update[NAME, AGE, ADDRESS, PHONE, STATUS]:\n"); // need treatmentPlan and inWard too
			attrToChange = scanner.nextLine();
			//Get value to change
			System.out.println("\nEnter the new value:\n");
			valueToChange = scanner.nextLine();
			//Call method that interacts with the Database
			updatePatient(patientID, attrToChange, valueToChange);
			System.out.println("The patient is updated successfully!");
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}

	/*
	 * Delete an appointed patient
	 * */
	public static void userPatientDelete() {
		//Declare local variables
		String patientID;
		try {
			//Get patient id
			System.out.println("\nEnter the patient id of the patient you want to delete:\n");
			patientID = scanner.nextLine();
			//Print the patient information you plan to delete
			System.out.println("\nThe patient information you have chosen:\n");
			getPatient(patientID);
			//Call method that interacts with the Database
			deletePatient(patientID);
			System.out.println("The patient is deleted successfully!");
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}

	/*
	 * Enter information for a new ward
	 * */
	public static void userWardAdd() {
		//Declare local variables
		String wardNumber;
		String capacity;
		String dayCharge;
		String responsibleNurse;
		try {
			//Get ward number for the new ward
			System.out.println("\nEnter the ward number of the new ward:\n");
			wardNumber = scanner.nextLine();
			//Get capacity
			System.out.println("\nEnter the capacity of the new ward:\n");
			capacity = scanner.nextLine();
			//Get dayCharge
			System.out.println("\nEnter the charge per day of the new ward:\n");
			dayCharge = scanner.nextLine();
			//Get responsibleNurse
			System.out.println("\nEnter the nurse responsible for the new ward:\n");
			responsibleNurse = scanner.nextLine();
			addWard(wardNumber, capacity, dayCharge, responsibleNurse);
			System.out.println("A new ward is added successfully!");
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}

	/*
	 * Update an attribute of an appointed ward
	 * */
	public static void userWardUpdate() {
		//Declare local variables
		String wardNumber;
		String attrToChange;
		String valueToChange;
		try {
			//Get wardNumber
			System.out.println("\nEnter the ward number of the ward you want to update:\n");
			wardNumber = scanner.nextLine();

			//Print the ward information you plan to update
			System.out.println("\nThe ward information you have chosen:\n");
			getWard(wardNumber);

			//Get attribute to change
			//Print all possible attributes can be changed
			System.out.println("\nPlease select the attribute you wish to update[CAPACITY, CHARGE PER DAY, RESPONSIBLE NURSE]:\n"); // need patients' SSN too
			attrToChange = scanner.nextLine();
			//Get value to change
			System.out.println("\nEnter the new value:\n");
			valueToChange = scanner.nextLine();
			//Call method that interacts with the Database
			updateWard(wardNumber, attrToChange, valueToChange);
			System.out.println("The ward is updated successfully!");
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}

	/*
	 * Delete an appointed ward
	 * */
	public static void userWardDelete() {
		//Declare local variables
		String wardNumber;
		try {
			//Get wardNumber
			System.out.println("\nEnter the ward number of the ward you want to delete:\n");
			wardNumber = scanner.nextLine();
			//Print the ward information you plan to delete
			System.out.println("\nThe ward information you have chosen:\n");
			getWard(wardNumber);
			//Call method that interacts with the Database
			manageWardDelete(wardNumber);
			System.out.println("The ward is deleted successfully!");
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}

	/*
	 * Check available wards
	 */
	public static void userWardCheck() {
		try {
			ResultSet rs = prep_checkWardAvailability.executeQuery();
			rs.beforeFirst();
			System.out.println("\nBelow is the list of available wards:");
			while (rs.next()) {
				String wardNumber = rs.getString("ward number");
				String capacity = rs.getString("capacity");
				System.out.println(capacity + "-bed Ward numbered " + wardNumber);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Check available beds
	 */
	public static void userBedCheck() {
		try {
			ResultSet rs = prep_checkBedAvailability.executeQuery();
			rs.beforeFirst();
			System.out.println("\nBelow is the list of available beds:");
			while (rs.next()) {
				String wardNumber = rs.getString("ward number");
				String bedNumber = rs.getString("bed number");
				System.out.println("Bed numbered " + bedNumber + " in ward numbered " + wardNumber);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Assumption: we assume that when assigning a bed to a patient, a ward is assigned to
	 * the patient too, so there is no need to have a separate operation of assigning ward.
	 *
	 * Assign a bed and the ward of the bed to a patient according to his/her request
	 */
	public static void userBedAssign() {
		System.out.println("\nPlease enter the patient ID of the patient you are assigning a ward/bed for.\n");
		String patientID = scanner.nextLine();
		userWardCheck(); // should print the capacity of each ward
		System.out.println("\nPlease select a ward according to your need from the above ward\n");
		String wardNumber = scanner.nextLine();
		getWard(wardNumber);
		String bedNumber;
		try {
			prep_addTreatmentRecord.setString(1, wardNumber);
			ResultSet rs = prep_checkBedinWardAvailability.executeQuery();
			rs.beforeFirst();
			System.out.println("\nBelow is the list of available beds in ward numbered " + wardNumber + ": ");
			while (rs.next()) {
				if (wardNumber != rs.getString("ward number")) {
					System.out.println("Warning: a bed in wrong ward is skipped!");
					continue;
				}
				bedNumber = rs.getString("bed number");
				System.out.println("Bed numbered " + bedNumber);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("\nPlease select a bed from the available bed list above\n");
		bedNumber = scanner.nextLine();
		manageBedAssign(patientID, wardNumber, bedNumber);
		System.out.println("\nPatient " + patientID + " got the bed numbered " + bedNumber + " in ward numbered" +
				wardNumber + " successfully.");
	}

	/*
	 * Assumption: we assume that when releasing a bed from a patient, a ward is released from
	 * the patient too, so there is no need to have a separate operation for releasing ward.
	 *
	 * Release a bed and the ward of the bed from a patient
	 */
	public static void userBedRelease() {
		String wardNum;
		String bedNum;
		System.out.println("\nPlease enter the bed number of the bed to be released");
		bedNum = scanner.nextLine();
		System.out.println("\nPlease enter the ward number of the bed to be released");
		wardNum = scanner.nextLine();
		manageBedRelease(wardNum, bedNum);
		System.out.println("\nBed numbered " + bedNum + "in ward numbered " + wardNum + " is released");
	}

	// GG
	/*
	 * user task: Enter a new billing account for a patient
	 * return: none
	 * */
	public static void userBillingAcctAdd() {
		
		try {
			//Declare local variables
			String accountID;
			String patientID;
			String visitDate;
			String payerSSN;
			String paymentMethod;
			String cardNum;
			String regFee;
			String medPrescribed;
			String accomFee;
			String address;
			
			//Get account id you wish to enter a new billing account record
			System.out.println("\nEnter the account ID you wish to add:\n");
			accountID = scanner.nextLine();
			//Get patient id
			System.out.println("\nEnter the patient ID for this billing account:\n");
			patientID = scanner.nextLine();
			//Get visit date
			System.out.println("\nEnter the visit date:\n");
			visitDate = scanner.nextLine();
			//Get payer's ssn
			System.out.println("\nEnter the payer's SSN for this billing account:\n");
			payerSSN = scanner.nextLine();
			//Get payment method
			System.out.println("\nEnter the payment method:\n");
			paymentMethod = scanner.nextLine();
			//Get card number
			System.out.println("\nEnter the card number for this payment:\n");
			cardNum = scanner.nextLine();
			//Get registration fee
			System.out.println("\nEnter the registration fee:\n");
			regFee = scanner.nextLine();
			//Get medication prescribed
			System.out.println("\nEnter the medication prescribed:\n");
			medPrescribed = scanner.nextLine();
			//Get accomandation fee
			System.out.println("\nEnter the accomandation fee:\n");
			accomFee = scanner.nextLine();
			//Get billing address
			System.out.println("\nEnter the billing address for the payment:\n");
			address = scanner.nextLine();
			
			//call method that interacts with the Database
			manageBillingAccountAdd(accountID, patientID, visitDate, payerSSN, paymentMethod, cardNum, regFee, medPrescribed, accomFee, address);
			
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}
	
	/*
	 * user task: Retrieve a billing account
	 * return: none
	 * */
	public static void userBillingAcctGet() {
		
		try {			
			//Declare local variables
			String accountID;
			
			//Get accountID
			System.out.println("\nEnter the account ID you wish to retrieve:\n");
			accountID = scanner.nextLine();
			
			//Call method that interacts with the Database
			showBillingAccount(accountID);
			
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}
	
	/*
	 * user task: Update a billing account certain attribute
	 * return: none
	 * */
	public static void userBillingAcctUpdate() {
		
		try {
			//Declare local variables
			String accountID;
			String attrToChange;
			String valueToChange;
			
			//Get account id
			System.out.println("\nEnter the billing id you wish to update:\n");
			accountID = scanner.nextLine();
				
			//Print the billing account information you plan to update
			System.out.println("\nThe billing account information you have chosen:\n");
			result.beforeFirst();
			showBillingAccount(accountID);
			
			//Get attribute to change
			//Print all possible attribute can be changed
			System.out.println("\nPlease select the attribute you wish to update[BILLINGADDRESS, PAYMENTTYPE, CARDNUMBER, REGISTRATIONFEE, ACCOMMANDATIONFEE, MEDICATIONPRESCRIBED, VISITDATE]:\n");
			attrToChange = scanner.nextLine();
			//Get value to change
			System.out.println("\nEnter the new value:\n");
			valueToChange = scanner.nextLine();
			
			//Call method that interacts with the Database
			manageBillingAccountUpdate(accountID, attrToChange, valueToChange);
			
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}
	
	public static void userBillingAcctDelete() {
		
		try {
			//Declare local variables
			String accountID;
			
			//Get account id
			System.out.println("\nEnter the billing id you wish to delete:\n");
			accountID = scanner.nextLine();
				
			//Print the billing account information you plan to delete
			System.out.println("\nThe billing account information you have chosen:\n");
			result.beforeFirst();
			showBillingAccount(accountID);
			
			//Call method that interacts with the Database
			deleteBillingAccount(accountID);
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}

	public static void main(String[] args) {
		try {
        
            // Declare local variables
            boolean quit = false;
            String command;
            
            // Print welcome
            System.out.println("\nWelcome to Wolf Hospital Management System");
            
           
            connectToDatabase();
            generatePreparedStatements();
            dropAllExistingTables();
            generateTables();
			
			populateStaffTable();
			populatePatientsTable();
			populateWardsTable();
			populateMedicalRecordsTable();
			populatTreatmentTable();
			populatTestTable();
			populatCheckinTable();
			populateBillingAccountsTable();
			populateBedsTable();
			populateAssignedTable();
            
            // Print available commands
            printCommands(CMD_MAIN);

            // Watch for user input
            currentMenu = CMD_MAIN;
            scanner = new Scanner(System.in);
            while (quit == false) {
            	System.out.print("user -> ");
                command = scanner.nextLine();
                switch (currentMenu) {
                    case CMD_MAIN:
                         // Check user's input (case insensitively)
                         switch (command.toUpperCase()) {
                             //fhy
                             case CMD_MEDICAL_RECORDS:
                                 // Tell the user their options in this new menu
                                 printCommands(CMD_MEDICAL_RECORDS);
                                 // Remember what menu we're in
                                 currentMenu = CMD_MEDICAL_RECORDS;
                                 break;
                            //GG
                           	case CMD_BILLING_ACCOUNTS: 
                             	startup_printAvailableCommands(CMD_BILLING_ACCOUNTS);
                             	currentMenu = CMD_BILLING_ACCOUNTS;
                             	break;
                            case CMD_INFORMATION_PROCESSING:
                                // Tell the user their options in this new menu
                                startup_printAvailableCommands(CMD_INFORMATION_PROCESSING);
                                // Remember what menu we're in
                                currentMenu = CMD_INFORMATION_PROCESSING;
                                break;
                            case CMD_REPORTS:
                                // Tell the user their options in this new menu
                                startup_printAvailableCommands(CMD_REPORTS);
                                // Remember what menu we're in
                                currentMenu = CMD_REPORTS;
                                break;                                
                            case CMD_QUIT:
                                quit = true;
                                break;
                            default:
                                // Remind the user about what commands are available
                                System.out.println("\nCommand not recognized");
                                startup_printAvailableCommands(CMD_MAIN);
                                break;
                        }
                        break;
      
                    //fhy
                    case CMD_MEDICAL_RECORDS:
                        switch (command.toUpperCase()){
                            case CMD_TREATMENT_ADD:
                            		userTreatmentAdd();
                                break;
                            case CMD_TREATMENT_GETALL:
                            		userTreatmentGetAll();
                                break;
                            case CMD_TREATMENT_GET:
                            		userTreatmentGet();
                                break;
                            case CMD_TREATMENT_UPDATE:
                            		userTreatmentUpdate();
                                break;
                            case CMD_TEST_ADD:
                            		userTestAdd();
                                break;   
                            case CMD_TEST_GETALL:
                            		userTestGetAll();
                                break;   
                            case CMD_TEST_GET:
                            		userTestGet();
                                break;
                            case CMD_TEST_UPDATE:
                            		userTestUpdate();
                                break;
                            case CMD_CHECKIN_ADD:
                            		userCheckinAdd();
                                break;
                            case CMD_CHECKIN_GETALL:
                            		userCheckinGetAll();
                                break;
                            case CMD_CHECKIN_GET:
                            		userCheckinGet();
                                break;
							case CMD_CHECKIN_UPDATE:
									userCheckinUpdate();
								break;
                            case CMD_QUIT:
                                quit = true;
                                break;
                            default:
                                // Remind the user about what commands are available
                                System.out.println("\nCommand not recognized");
                                printCommands(CMD_MEDICAL_RECORDS);
                                break;     
                        }
                    case CMD_BILLING_ACCOUNTS:
                    //GG
                    	switch (command.toUpperCase()){
                          case CMD_BILLING_ACCT_ADD:
                          	userBillingAcctAdd();
                          	break;
                        	case CMD_BILLING_ACCT_GET:
                          	userBillingAcctGet();
                          	break;
                        	case CMD_BILLING_ACCT_UPDATE:
                          	userBillingAcctUpdate();
                          	break;
                        	case CMD_BILLING_ACCT_DELETE:
                          	userBillingAcctDelete();
                          	break;
                        	case CMD_QUIT:
                          	quit = true;
                          	break;
                        	default:
                          	System.out.println("\nCommand not recognized");
                          	printCommands(CMD_BILLING_ACCOUNTS);
                          	break;
                      }
                    case CMD_INFORMATION_PROCESSING:
                        switch (command.toUpperCase()) {
                            case CMD_STAFF_ADD:
                                	userStaffAdd();
                                	break;
                          	case CMD_STAFF_UPDATE:
                            		userStaffUpdate();
                            		break;
                          	case CMD_STAFF_DELETE:
                            		userStaffDelete();
                            		break;
                          	case CMD_PATIENT_ADD:
                            		userPatientAdd();
                            		break;
                            case CMD_PATIENT_UPDATE:
                            		userPatientUpdate();
                            		break;
                          	case CMD_PATIENT_DELETE:
                            		userPatientDelete();
                            		break;
                            case CMD_WARD_ADD:
                            		userWardAdd();
                            		break;
                            case CMD_WARD_UPDATE:
                            		userWardUpdate();
                            		break;
                          	case CMD_WARD_DELETE:
                            		userWardDelete();
                            		break;
                            case CMD_WARD_CHECK:
                            		userWardCheck();
                            		break;
                          	case CMD_BED_CHECK:
                            		userBedCheck();
                            		break;
                            // Assumption: we assume that when assigning a bed to a patient, a ward is assigned to
							// the patient too, so there is no need to have a separate operation of assigning a ward
                            case CMD_WARD_ASSIGN:
                            		userBedAssign();
                            		break;
                          	case CMD_BED_ASSIGN:
                            		userBedAssign();
                            		break;
                            case CMD_WARD_RESERVE:
									userBedAssign();
                            		break;
                          	case CMD_BED_RESERVE:
									userBedAssign();
                            		break;
							// Assumption: we assume that when releasing a bed from a patient, a ward is released from
							// the patient too, so there is no need to have a separate operation for releasing ward.
                            case CMD_WARD_RELEASE:
									userBedRelease();
                            		break;
                          	case CMD_BED_RELEASE:
                            		userBedRelease();
                            		break;
                          	default:
                            	  System.out.println("\nCommand not found");
                            		printCommands(CMD_INFORMATION_PROCESSING);
                            		break;
                        }
												break;
                    case CMD_REPORTS:
						// Check user's input (case insensitively)
						// private static final String CMD_MEDICAL_HISTORY_BY_PATIENT_REPORT = "MEDICAL HISTORY I";
						// private static final String CMD_MEDICAL_HISTORY_BY_TIME_REPORT = "MEDICAL HISTORY II";
						// private static final String CMD_WARD_USAGE_STATUS_REPORT = "WARD USAGE STATUS";
						// private static final String CMD_BED_USAGE_STATUS_REPORT = "BED USAGE STATUS";
						// private static final String CMD_NUMBER_PATIENTS_REPORT = "NUMBER OF PATIENTS";
						// private static final String CMD_WARD_USAGE_PERCENT_REPORT = "WARD USAGE PERCENTAGE";
						// private static final String CMD_DOCTOR_RESPONS_REPORT = "DOCTOR REPONSIBLITIES";
						// private static final String CMD_STAFF_INFO_REPORT = "STAFF INFORMATION";
                        switch (command.toUpperCase()) {
                            case CMD_MEDICAL_HISTORY_BY_PATIENT_REPORT:
                                userReportHistoryByPatient();
                            break;
                            case CMD_MEDICAL_HISTORY_BY_TIME_REPORT:
								userReportHistoryByTime();
                                break;
                            case CMD_WARD_USAGE_STATUS_REPORT:
                                userReportWardUsageStatus();
                                break;
                            case CMD_BED_USAGE_STATUS_REPORT:
                                userReportBedUsageStatus();
                                break;
                            case CMD_NUMBER_PATIENTS_REPORT:
                                userReportNumberOfPatients();
                                break;
                            case CMD_WARD_USAGE_PERCENT_REPORT:
                                userReportWardUsagePercentage();
                                break;
                            case CMD_DOCTOR_RESPONS_REPORT:
                                userReportDoctorResponse();
                                break;
                            case CMD_STAFF_INFO_REPORT:
                                userReportStaffInfo();
                                break;
                            case CMD_MAIN:
                                // Tell the user their options in this new menu
                                startup_printAvailableCommands(CMD_MAIN);
                                // Remember what menu we're in
                                currentMenu = CMD_MAIN;
                                break;
                            case CMD_QUIT:
                                quit = true;
                                break;
                            default:
                                // Remind the user about what commands are available
                                System.out.println("\nCommand not recognized");
                                startup_printAvailableCommands(CMD_REPORTS);
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
            // Connection
            connection.close();
        
        }
        catch (Throwable err) {
            error_handler(err);
        }
	}

	private static void userReportStaffInfo() {
		try {
			System.out.println("\nReport staff information grouped by role");

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private static void userReportDoctorResponse() {
	}

	private static void userReportWardUsagePercentage() {
	}

	private static void userReportNumberOfPatients() {
	}

	private static void userReportBedUsageStatus() {
	}

	private static void userReportWardUsageStatus() {
	}

	private static void userReportHistoryByTime() {
	}

	private static void userReportHistoryByPatient() {
	}

	private static void startup_printAvailableCommands(String cmdBillingAccounts) {
		// TODO Auto-generated method stub
		
	}

	public static void error_handler(Throwable error) {

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
