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
	private static PreparedStatement prepAddStaff;
	private static PreparedStatement prepGetStaff;
	private static PreparedStatement prepUpdateStaffName;
	private static PreparedStatement prepUpdateStaffAge;
	private static PreparedStatement prepUpdateStaffGender;
	private static PreparedStatement prepUpdateStaffJobTitle;
	private static PreparedStatement prepUpdateStaffProfTitle;
	private static PreparedStatement prepUpdateStaffDepart;
	private static PreparedStatement prepUpdateStaffPhone;
	private static PreparedStatement prepUpdateStaffAddress;
	private static PreparedStatement prepDeleteStaff;
	// Wards
	// TODO: prepUpdateWardsPatientsID
	private static PreparedStatement prepAddWards;
	private static PreparedStatement prepGetWards;
	private static PreparedStatement prepUpdateWardsCapacity;
	private static PreparedStatement prepUpdateWardsCharge;
	private static PreparedStatement prepUpdateWardsNurse;
	private static PreparedStatement prepUpdateWardsPatientsID; // To reflect what is showed in demo data, but how?
	private static PreparedStatement prepDeleteWards;
	private static PreparedStatement prepDeleteWardInformation;
	// Patients
	private static PreparedStatement prepAddPatients;
	private static PreparedStatement prepAddAgeInfo;
	private static PreparedStatement prepAddContactInfo;
	private static PreparedStatement prepAddPersonInfo;
	private static PreparedStatement prepGetPatients;
	private static PreparedStatement prepUpdatePatientsName;
	private static PreparedStatement prepUpdatePatientsAge;
	private static PreparedStatement prepUpdatePatientsPhone;
	private static PreparedStatement prepUpdatePatientsAddress;
	private static PreparedStatement prepUpdatePatientsTreatmentPlan; // To reflect what is showed in demo data
	private static PreparedStatement prepUpdatePatientsInWard;
	private static PreparedStatement prepUpdatePatientsStatus;
	private static PreparedStatement prepDeletePatients;

	// fhy
	private static PreparedStatement prepAddMedicalRecord;
	// Medical Records - Treatment
	// GG
	private static PreparedStatement prepAddTreatmentRecord;
	// fhy
	private static PreparedStatement prepGetAllTreatmentRecords;
	private static PreparedStatement prepGetTreatmentRecord;

	// private static PreparedStatement prepUpdateTreatmentRecord;
	private static PreparedStatement prepUpdateTreatmentEndDate;
	private static PreparedStatement prepUpdateTreatmentPrescription;
	private static PreparedStatement prepUpdateTreatmentDiagnosisDetails;

	// Medical Records - Test
	private static PreparedStatement prepAddTestRecord;
	private static PreparedStatement prepGetAllTestRecords;
	private static PreparedStatement prepGetTestRecord;

	// private static PreparedStatement prepUpdateTestRecord;
	private static PreparedStatement prepUpdateTestEndDate;
	private static PreparedStatement prepUpdateTestTestType;
	private static PreparedStatement prepUpdateTestTestResult;

	// Medical Records - Check-in
	private static PreparedStatement prepAddCheckinRecord;
	private static PreparedStatement prepGetAllCheckinRecords;
	private static PreparedStatement prepGetCheckinRecord;

	// Yudong RAO
	private static PreparedStatement prepUpdateCheckinEndDate;
	private static PreparedStatement prepUpdateCheckinWard;
	private static PreparedStatement prepUpdateCheckinBed;

	// Reports
	private static PreparedStatement prepReportHistoryByTime;
	private static PreparedStatement prepReportHistoryByPatient;
	private static PreparedStatement prepReportCurrentWardUsageStatus;
	private static PreparedStatement prepReportCurrentBedUsageStatus;
	private static PreparedStatement prepReportNumberOfPatientsPerMonth;
	private static PreparedStatement prepReportWardUsagePercentage;
	private static PreparedStatement prepReportDoctorResponsibility;
	private static PreparedStatement prepReportStaffInformation;

	// Billing Accounts
	private static PreparedStatement prepAddBillingAccount;
	private static PreparedStatement prepGetBillingAccount;
	private static PreparedStatement prepUpdateBillingAccountAddress;
	// Update payment method and card number
	private static PreparedStatement prepUpdateBillingAccountPaymentType;
	private static PreparedStatement prepUpdateBillingAccountCardNumber;
	// Update registration fee and accommandation fee
	private static PreparedStatement prepUpdateBillingAccountRegistrationFee;
	private static PreparedStatement prepUpdateBillingAccountAccommandationFee;
	private static PreparedStatement prepUpdateBillingAccountMedicationPrescribed;
	private static PreparedStatement prepUpdateBillingAccountVisitDate;
	private static PreparedStatement prepDeleteBillingAccount;

	// GG
	// Basic Information - Wards(partial, the rest should be done by others)
	private static PreparedStatement prepDeleteWardInfo;
	private static PreparedStatement prep_checkWardAvailability;
	// private static PreparedStatement prepAssignWard;
	// private static PreparedStatement prepReserveWard;
	// private static PreparedStatement prepReleaseWard;

	// Basic Information - Beds
	private static PreparedStatement prepAddBedInfo;
	private static PreparedStatement prepGetBedInfo;
	private static PreparedStatement prepDeleteBedInfo;

	// Management - Beds
	private static PreparedStatement prepAssignBed;
	private static PreparedStatement prep_checkBedAvailability;
	private static PreparedStatement prep_checkBedinWardAvailability;    
	// private static PreparedStatement prepReserveBed;
	private static PreparedStatement prepReleaseBed;
//	private static PreparedStatement prepDeleteBedInfo;
	private static PreparedStatement prepAddAssigned;

	// Payer Info
	private static PreparedStatement prepAddPayerInfo;
	private static PreparedStatement prepUpdatePayerAddress;
	private static PreparedStatement prepDeletePayerInfo;

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
			prepAddStaff = connection.prepareStatement(sql);
			// Retrieve basic information about staff
			sql = "SELECT * FROM `Staff`" + " WHERE staffID = ?;";
			prepGetStaff = connection.prepareStatement(sql);
			// Update basic information about staff
			sql = "UPDATE `Staff`" + " SET `name` = ?" + " WHERE staffID = ?;";
			prepUpdateStaffName = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + " SET `age` = ?" + " WHERE staffID = ?;";
			prepUpdateStaffAge = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + " SET `gender` = ?" + " WHERE staffID = ?;";
			prepUpdateStaffGender = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + " SET `jobTitle` = ?" + " WHERE staffID = ?;";
			prepUpdateStaffJobTitle = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + " SET `profTitle` = ?" + " WHERE staffID = ?;";
			prepUpdateStaffProfTitle = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + " SET `department` = ?" + " WHERE staffID = ?;";
			prepUpdateStaffDepart = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + " SET `phone` = ?" + " WHERE staffID = ?;";
			prepUpdateStaffPhone = connection.prepareStatement(sql);
			sql = "UPDATE `Staff`" + " SET `address` = ?" + " WHERE staffID = ?;";
			prepUpdateStaffAddress = connection.prepareStatement(sql);
			// Delete basic information about staff
			sql = "DELETE FROM `Staff`" + " WHERE staffID = ?;";
			prepDeleteStaff = connection.prepareStatement(sql);
			// Enter basic information about patients
			sql = "INSERT INTO `Patients` (`patientID`, `SSN`) VALUES (?, ?);";
			prepAddPatients = connection.prepareStatement(sql);
			
			sql	= "INSERT INTO `AgeInfo` (`DOB`, `age`) VALUES (?, ?);";
			prepAddAgeInfo = connection.prepareStatement(sql);
			
			sql	= "INSERT INTO `ContactInfo` (`phone`, `address`) VALUES (?, ?);";
			prepAddContactInfo = connection.prepareStatement(sql);
			
			sql = "INSERT INTO `PersonInfo` (`SSN`, `name`, `DOB`, `gender`, `phone`, " +
					"`processing treatment plan`, `in ward`, `completing treatment`) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
			prepAddPersonInfo = connection.prepareStatement(sql);
			
			// Retrieve basic information about patients
			sql = "SELECT * FROM `Patients` p JOIN `PersonInfo` i ON p.SSN = i.SSN" +
					" JOIN `AgeInfo` a ON i.DOB = a.DOB" +
					" JOIN ContactInfo con ON i.phone = con.phone" +
					" WHERE patientID = ?;";
			prepGetPatients = connection.prepareStatement(sql);
			// Update basic information about patients
			sql = "UPDATE `PersonInfo`" + " SET `name` = ?"
					+ " WHERE SSN IN (SELECT SSN FROM Patients WHERE patientID = ?);";
			prepUpdatePatientsName = connection.prepareStatement(sql);
			sql = "UPDATE `PersonInfo`" + " SET `age` = ?"
					+ " WHERE SSN IN (SELECT SSN FROM Patients WHERE patientID = ?);";
			prepUpdatePatientsAge = connection.prepareStatement(sql);
			sql = "UPDATE `PersonInfo` p `ContactInfo` ci" +
					" SET p.phone=?, ci.phone=?" +
					" WHERE p.phone=ci.phone AND p.SSN IN (SELECT SSN FROM Patients WHERE patientID = ?);";
			prepUpdatePatientsPhone = connection.prepareStatement(sql);
			sql = "UPDATE `ContactInfo`" +
					" SET `address` = ?" +
					" WHERE phone IN (SELECT phone FROM PersonInfo WHERE SSN" +
					" IN (SELECT SSN FROM Patients WHERE patientID = ?));";
			prepUpdatePatientsAddress = connection.prepareStatement(sql);
			sql = "UPDATE `PersonInfo`" + " SET `processing treatment plan` = ?"
					+ " WHERE SSN IN (SELECT SSN FROM Patients WHERE patientID = ?);";
			prepUpdatePatientsTreatmentPlan = connection.prepareStatement(sql);
			sql = "UPDATE `PersonInfo`" + " SET `in ward` = ?"
					+ " WHERE SSN IN (SELECT SSN FROM Patients WHERE patientID = ?);";
			prepUpdatePatientsInWard = connection.prepareStatement(sql);
			sql = "UPDATE `PersonInfo`" + " SET `completing treatment` = ?"
					+ " WHERE SSN IN (SELECT SSN FROM Patients WHERE patientID = ?);";
			prepUpdatePatientsStatus = connection.prepareStatement(sql);
			// Delete basic information about patients
			sql = "DELETE `Patients` p, `PersonInfo` i, `AgeInfo` a, `ContactInfo` con FROM p JOIN i ON p.SSN = i.SSN" +
					" JOIN a ON i.DOB = a.DOB" +
					" JOIN  con ON i.phone = con.phone" +
					" WHERE patientID = ?;" ;
			prepDeletePatients = connection.prepareStatement(sql);
			// Enter basic information about wards
			sql = "INSERT INTO `Wards` (`ward number`, `capacity`, `charges per day`, `responsible nurse`)"
					+ " VALUES (?, ?, ?, ?);";
			prepAddWards = connection.prepareStatement(sql);
			// Retrieve basic information about wards
			sql = "SELECT * FROM `Wards`" + " WHERE ward number = ?;";
			prepGetWards = connection.prepareStatement(sql);
			// Update basic information about wards
			sql = "UPDATE `Wards`" + " SET `capacity` = ?" + " WHERE ward number = ?;";
			prepUpdateWardsCapacity = connection.prepareStatement(sql);
			sql = "UPDATE `Wards`" + " SET `charges per day` = ?" + " WHERE ward number = ?;";
			prepUpdateWardsCharge = connection.prepareStatement(sql);
			sql = "UPDATE `Wards`" + " SET `responsible nurse` = ?" + " WHERE ward number = ?;";
			prepUpdateWardsNurse = connection.prepareStatement(sql);
			// fhy
			
			// Add new medical record
			sql = "INSERT INTO `Medical Records` (" + 
					"`recordID`, `patientID`, `startDate`, `endDate`, `responsibleDoctor`) " +
					"VALUES (?,?,?,?,?);";
			prepAddMedicalRecord = connection.prepareStatement(sql);
			
			// Get all treatment records
			// SELECT * FROM `Medical Records` m JOIN `Treatment` t ON m.recordID=t.recordID
			// WHERE patientID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Treatment` t ON m.recordID=t.recordID WHERE patientID=?;";
			prepGetAllTreatmentRecords = connection.prepareStatement(sql);

			// Get treatment record
			// SELECT * FROM `Medical Records` m JOIN `Treatment` t ON m.recordID=t.recordID
			// WHERE t.recordID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Treatment` t ON m.recordID=t.recordID WHERE t.recordID=?;";
			prepGetTreatmentRecord = connection.prepareStatement(sql);

			// Update treatment record
			// UPDATE `Medical Records` SET `endDate` = '2020-01-01' WHERE recordID = 13;
			// UPDATE `Treatment` SET `prescription` = 'Use', `diagnosisDetails` = 'Muscle'
			// WHERE recordID = '13';
			sql = "UPDATE `Medical Records` " + "SET `endDate` = ? " + "WHERE recordID = ? " + "AND EXISTS "
					+ "(SELECT * FROM `Treatment` " + "WHERE recordID = ?;)";
			prepUpdateTreatmentEndDate = connection.prepareStatement(sql);

			sql = "UPDATE `Treatment` " + "SET `prescription` = ? " + "WHERE recordID = ?;";
			prepUpdateTreatmentPrescription = connection.prepareStatement(sql);

			sql = "UPDATE `Treatment` " + "SET `diagnosisDetails` = ? " + "WHERE recordID = ?;";
			prepUpdateTreatmentDiagnosisDetails = connection.prepareStatement(sql);

			// Create new test record
			// INSERT INTO `Test` (`recordID`, `testType`, `testResult`)VALUES ('14',
			// 'testType5', 'testResult5');
			// INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`,
			// `endDate`, `responsibleDoctor`) VALUES ('14', '5', '2019-07-01',
			// '2019-07-02', '3');
			sql = "INSERT INTO `Test` (`recordID`, `testType`, `testResult`) " + "VALUES (?, ?, ?); ";
			prepAddTestRecord = connection.prepareStatement(sql);

			// Get all test records
			// SELECT * FROM `Medical Records` m JOIN `Test` t ON m.recordID=t.recordID
			// WHERE patientID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Test` t ON m.recordID=t.recordID WHERE patientID=?;";
			prepGetAllTestRecords = connection.prepareStatement(sql);

			// Get test record
			// SELECT * FROM `Medical Records` m JOIN `Test` t ON m.recordID=t.recordID
			// WHERE t.recordID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Test` t ON m.recordID=t.recordID WHERE t.recordID=?;";
			prepGetTestRecord = connection.prepareStatement(sql);

			// Update test record
			// UPDATE `Medical Records` SET `endDate` = '2020-01-01' WHERE recordID=14;
			// UPDATE `Test` SET `testType` = 'Influenza B Rapid Assay', `testResult` =
			// 'Influenza B Antigen value: positive, ref range: negative' WHERE recordID =
			// '14';
			sql = "UPDATE `Medical Records` " + "SET `endDate` = ? " + "WHERE recordID= ? " + "AND EXISTS "
					+ "(SELECT * FROM `Test` " + "WHERE recordID = ?;)";
			prepUpdateTestEndDate = connection.prepareStatement(sql);

			sql = "UPDATE `Test` " + "SET `testType` = ? " + "WHERE recordID = ?;";
			prepUpdateTestTestType = connection.prepareStatement(sql);

			sql = "UPDATE `Test` " + "SET `testResult` = ? " + "WHERE recordID = ?;";
			prepUpdateTestTestResult = connection.prepareStatement(sql);

			// Create check-in record
			// INSERT INTO `Check-ins` (`recordID`, `wardNumber`, `bedNumber`)VALUES ('15',
			// NULL, NULL);
			// INSERT INTO `Medical Records` (`recordID`, `patientID`, `startDate`,
			// `endDate`, `responsibleDoctor` ) VALUES ('15', '5', '2019-07-01',
			// '2019-07-07', '4');
			sql = "INSERT INTO `Check-ins` (`recordID`, `wardNumber`, `bedNumber`) " + "VALUES (?, ?, ?); ";
			prepAddCheckinRecord = connection.prepareStatement(sql);

			// Get all check-in records
			// SELECT * FROM `Medical Records` m JOIN `Check-ins` c ON m.recordID=c.recordID
			// WHERE patientID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Check-ins` c ON m.recordID=c.recordID WHERE patientID=?;";
			prepGetAllCheckinRecords = connection.prepareStatement(sql);

			// Get check-in record
			// SELECT * FROM `Medical Records` m JOIN `Check-ins` c ON m.recordID=c.recordID
			// WHERE c.recordID=1;
			sql = "SELECT * FROM `Medical Records` m JOIN `Check-ins` c ON m.recordID=c.recordID WHERE c.recordID=?;";
			prepGetCheckinRecord = connection.prepareStatement(sql);

			// Yudong
			// Update check-in records
			sql = "UPDATE `Medical Records` " + "SET `endDate` = ? " + "WHERE recordID = ? " + "AND EXISTS "
					+ "(SELECT * FROM `Check-ins` " + "WHERE recordID = ?);";
			prepUpdateCheckinEndDate = connection.prepareStatement(sql);

			sql = "UPDATE `Check-ins` " + "SET `wardNumber` = ? " + "WHERE recordID = ?;";
			prepUpdateCheckinWard = connection.prepareStatement(sql);

			sql = "UPDATE `Check-ins` " + "SET `bedNumber` = ? " + "WHERE recordID = ?;";
			prepUpdateCheckinBed = connection.prepareStatement(sql);

			// Report medical history by time
			sql = "SELECT * FROM `Medical Records` WHERE ? BETWEEN startDate AND endDate;";
			prepReportHistoryByTime = connection.prepareStatement(sql);

			// Report medical history by patient
			sql = "SELECT * FROM `Medical Records` WHERE `patientID`=?";
			prepReportHistoryByPatient = connection.prepareStatement(sql);

			// Report ward usage
			sql = "SELECT `ward number`, " + "IF(patientID IS NULL, 'empty', 'not empty') AS `usage` "
					+ "FROM Beds GROUP BY `ward number`;";
			prepReportCurrentWardUsageStatus = connection.prepareStatement(sql);

			// Report bed usage
			sql = "SELECT *, " + "IF(patientID IS NULL, 'not used', 'used') AS `usage` " + "FROM Beds;";
			prepReportCurrentBedUsageStatus = connection.prepareStatement(sql);

			// Report number of patients per month
			sql = "SELECT MONTH(startDate) AS `month`, " + "COUNT(*) AS `num` " + "FROM `Medical Records` "
					+ "GROUP BY month;";
			prepReportNumberOfPatientsPerMonth = connection.prepareStatement(sql);

			// Report ward usage percentage
			sql = "SELECT 100*COUNT(patientID)/COUNT(*) " + "AS `usage percentage` " + "FROM Beds;";
			prepReportWardUsagePercentage = connection.prepareStatement(sql);

			// Report doctor responsibility
			sql = "SELECT * FROM `Medical Records` " + "WHERE responsibleDoctor=?;";
			prepReportDoctorResponsibility = connection.prepareStatement(sql);

			// Report staff information
			sql = "SELECT * FROM `Staff` " + "GROUP BY jobTitle;";
			prepReportStaffInformation = connection.prepareStatement(sql);

			// Create billing account
			sql = "INSERT INTO `Billing Accounts` (`accountID`, `patientID`, `visitDate`, "
					+ "`payerSSN`, `paymentMethod`, `cardNumber`, `registrationFee`, "
					+ "`medicationPrescribed`, `accommandationFee`) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
			prepAddBillingAccount = connection.prepareStatement(sql);

			sql = "INSERT INTO `PayerInfo` (`SSN`, `billingAddress`) " + "VALUES (?, ?);";
			prepAddPayerInfo = connection.prepareStatement(sql);
			
			// Get billing account
			sql = "SELECT b.accountID, b.patientID, b.visitDate, "
					+ "b.payerSSN, b.paymentMethod, b.cardNumber, b.registrationFee "
					+ "b.medicationPrescribed, b.accommandationFee, p.billingAddress "
					+ "FROM `Billing Accounts` b JOIN `PayerInfo` p " + "ON m.payerSSN=b.SSN "
					+ "WHERE `accountID` = ?;";
			prepGetBillingAccount = connection.prepareStatement(sql);

			// Update billing account
			sql = "UPDATE `PayerInfo` " + "SET `billingAddress` = ? " + "WHERE payerSSN IN ( " + "SELECT b.SSN "
					+ "FROM `Billing Accounts` b JOIN `PayerInfo` p " + "ON m.payerSSN=b.SSN "
					+ "WHERE accountID = ?);";
			prepUpdateBillingAccountAddress = connection.prepareStatement(sql);

			sql = "UPDATE `Billing Accounts` " + "SET `paymentMethod` = ? " + "WHERE accountID = ?;";
			prepUpdateBillingAccountPaymentType = connection.prepareStatement(sql);

			sql = "UPDATE `Billing Accounts` " + "SET `cardNumber` = ? " + "WHERE accountID = ? "
					+ "AND paymentMethod = `Credit Card`;";
			prepUpdateBillingAccountCardNumber = connection.prepareStatement(sql);

			sql = "UPDATE `Billing Accounts` " + "SET `registrationFee` = ? " + "WHERE accountID = ?;";
			prepUpdateBillingAccountRegistrationFee = connection.prepareStatement(sql);

			sql = "UPDATE `Billing Accounts` " + "SET `accommandationFee` = ? " + "WHERE accountID = ?;";
			prepUpdateBillingAccountAccommandationFee = connection.prepareStatement(sql);

			sql = "UPDATE `Billing Accounts` " + "SET `medicationPrescribed` = ? " + "WHERE accountID = ?;";
			prepUpdateBillingAccountMedicationPrescribed = connection.prepareStatement(sql);

			sql = "UPDATE `Billing Accounts` " + "SET `visitDate` = ? " + "WHERE accountID = ?;";
			prepUpdateBillingAccountVisitDate = connection.prepareStatement(sql);

			// Delete billing account
			sql = "DELETE FROM `Billing Accounts` " + "WHERE accountID = ?;";
			prepDeleteBillingAccount = connection.prepareStatement(sql);

			// GG
			// Delete basic information about wards
			sql = "DELETE FROM `Wards` " +
					"WHERE `ward number` = ?; ";
			prepDeleteWardInfo = connection.prepareStatement(sql);
			
			// Check availability of wards
			sql = "SELECT * FROM `Wards` WHERE `ward number` IN (SELECT DISTINCT `ward number` " + "FROM `Beds`" + "WHERE ISNULL(patientID)); ";
			prep_checkWardAvailability = connection.prepareStatement(sql);

			// Assign wards:
			// sql = "SELECT COUNT(`bed number`) FROM `Beds` WHERE `ward number` = ?; ";
			// prepAssignWard = connection.prepareStatement(sql);

			// Reserve wards
			// sql = "INSERT INTO `Assigned` (`patientID`, `ward number`, `bed number`,
			// `start-date`, `end-date`) " +
			// "VALUES (?, ?, ?, ?, ?) );" +
			// "UPDATE `Beds` SET "
			// ;
			// prepReserveWard = connection.prepareStatement(sql);

			// Release wards
			// sql = "";
			// prepReleaseWard = connection.prepareStatement(sql);

			// Add basic information of a bed
			sql = "INSERT INTO `Beds` (`ward number`, `bed number`) " + "VALUES (?, ?); ";
			prepAddBedInfo = connection.prepareStatement(sql);

			// Get basic information of a bed
			sql = "SELECT * FROM `Beds` " + "WHERE `ward number` = ? AND `bed number` = ?; ";
			prepGetBedInfo = connection.prepareStatement(sql);

			// Delete basic information of a bed
			sql = "DELETE FROM `Beds` WHERE `ward number` = ? AND `bed number` = ?; ";
			prepDeleteBedInfo = connection.prepareStatement(sql);

			// Assign beds
			sql = "UPDATE `Beds` SET `patientID` = ? WHERE `ward number` = ? AND `bed number` = ?; ";
			prepAssignBed = connection.prepareStatement(sql);

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
			// prepReserveBed = connection.prepareStatement(sql);

			// Release beds
			sql = "UPDATE `Beds` SET `patientID` = NULL WHERE `ward number` = ? AND `bed number` = ?; ";
			prepReleaseBed = connection.prepareStatement(sql);

			// Create treatment records
			sql = "INSERT `Treatment` (`recordID`, `prescription`, `diagnosisDetails`) " + "VALUES (?, ?, ?); ";
			prepAddTreatmentRecord = connection.prepareStatement(sql);

			//Create assigned
			sql = "INSERT `Assigned` (`patientID`, `ward number`, `bed number`, `start-date`, `end-date`) " + "VALUES (?, ?, ?, ?, ?); ";
			prepAddAssigned = connection.prepareStatement(sql);

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
						"CONSTRAINT pkAssign PRIMARY KEY (`patientID`, `ward number`, `bed number`)," +
						"CONSTRAINT `fkAssignpi` FOREIGN KEY (`patientID`) REFERENCES Patients(`patientID`) ON DELETE CASCADE, " +
						"CONSTRAINT `fkAssginwb` FOREIGN KEY (`ward number`, `bed number`) REFERENCES Beds(`ward number`, `bed number`) " +
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
	
	public static void populateTreatmentTable() {
		manageTreatmentRecordAdd("1", "nervine", "Hospitalization");
		manageTreatmentRecordAdd("2", "nervine", "Hospitalization");
		manageTreatmentRecordAdd("3", "nervine", "Hospitalization");
		manageTreatmentRecordAdd("4", "analgestic", "Surgeon, Hospitalization");
	}
	
	public static void populateTestTable() {
//		manageTestRecordAdd("1","testA","success");
//		manageTestRecordAdd("2","testA","success");
//		manageTestRecordAdd("3","testA","success");
//		manageTestRecordAdd("4","testB","unknown");
	}
	
	// TO FIX: EMPTY DATE
	public static void populateCheckinTable() {
		manageCheckinRecordAdd("1", "001", "1");
		manageCheckinRecordAdd("2", "002", "1");
		manageCheckinRecordAdd("3", "001", "2");
		manageCheckinRecordAdd("4", "003", "1");
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
				prepAddStaff.setString(1, staffID);
				prepAddStaff.setString(2, name);
				prepAddStaff.setInt(3, Integer.parseInt(age));
				prepAddStaff.setString(4, gender);
				prepAddStaff.setString(5, jobTitle);
				prepAddStaff.setString(6, profTitle);
				prepAddStaff.setString(7, department);
				prepAddStaff.setString(8, phone);
				prepAddStaff.setString(9, address);
				prepAddStaff.executeUpdate();
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
			prepGetStaff.setString(1, staffID);
			ResultSet rs = prepGetStaff.executeQuery();
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
						prepUpdateStaffName.setString(1, newValue);
						prepUpdateStaffName.setString(2, staffID);
						prepUpdateStaffName.executeUpdate();
						break;
					case "AGE":
						prepUpdateStaffAge.setInt(1, Integer.parseInt(newValue));
						prepUpdateStaffAge.setString(2, staffID);
						prepUpdateStaffAge.executeUpdate();
						break;
					case "GENDER":
						prepUpdateStaffGender.setString(1, newValue);
						prepUpdateStaffGender.setString(2, staffID);
						prepUpdateStaffGender.executeUpdate();
						break;
					case "JOB TITLE":
						prepUpdateStaffJobTitle.setString(1, newValue);
						prepUpdateStaffJobTitle.setString(2, staffID);
						prepUpdateStaffJobTitle.executeUpdate();
						break;
					case "PROFESSIONAL TITLE":
						prepUpdateStaffProfTitle.setString(1, newValue);
						prepUpdateStaffProfTitle.setString(2, staffID);
						prepUpdateStaffProfTitle.executeUpdate();
						break;
					case "DEPARTMENT":
						prepUpdateStaffDepart.setString(1, newValue);
						prepUpdateStaffDepart.setString(2, staffID);
						prepUpdateStaffDepart.executeUpdate();
						break;
					case "PHONE":
						prepUpdateStaffPhone.setString(1, newValue);
						prepUpdateStaffPhone.setString(2, staffID);
						prepUpdateStaffPhone.executeUpdate();
						break;
					case "ADDRESS":
						prepUpdateStaffAddress.setString(1, newValue);
						prepUpdateStaffAddress.setString(2, staffID);
						prepUpdateStaffAddress.executeUpdate();
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
				prepDeleteStaff.setString(1, staffID);
				prepDeleteStaff.executeUpdate();
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
				prepAddPatients.setString(1, patientID);
				prepAddPatients.setString(2, SSN);
				
				prepAddAgeInfo.setDate(1, java.sql.Date.valueOf(DOB));
				prepAddAgeInfo.setInt(2, Integer.parseInt(age));
				
				prepAddContactInfo.setString(1, phone);
				prepAddContactInfo.setString(2, address);                
				
				prepAddPersonInfo.setString(1, SSN);
				prepAddPersonInfo.setString(2, name);
				prepAddPersonInfo.setDate(3, java.sql.Date.valueOf(DOB));
				prepAddPersonInfo.setString(4, gender);
				prepAddPersonInfo.setString(5, phone);
				prepAddPersonInfo.setString(6, processing);
				prepAddPersonInfo.setBoolean(7, inWard.equals("yes")? true : false);
				prepAddPersonInfo.setBoolean(8, completing.equals("yes")? true : false);

				// To-do: make use of variable treatmentPlan and wardNum. By calling prepAddTreatmentRecord and prepAssignWard here?
				prepAddPatients.executeUpdate();
				prepAddAgeInfo.executeUpdate();
				prepAddContactInfo.executeUpdate();
				prepAddPersonInfo.executeUpdate();
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
			prepGetPatients.setString(1, patientID);
			ResultSet rs = prepGetPatients.executeQuery();
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
						prepUpdatePatientsName.setString(1, newValue);
						prepUpdatePatientsName.setString(2, patientID);
						prepUpdatePatientsName.executeUpdate();
						break;
					case "AGE":
						prepUpdatePatientsAge.setInt(1, Integer.parseInt(newValue));
						prepUpdatePatientsAge.setString(2, patientID);
						prepUpdatePatientsAge.executeUpdate();
						break;
					case "ADDRESS":
						prepUpdatePatientsAddress.setString(1, newValue);
						prepUpdatePatientsAddress.setString(2, patientID);
						prepUpdatePatientsAddress.executeUpdate();
						break;
					case "PHONE":
						prepUpdatePatientsPhone.setString(1, newValue);
						prepUpdatePatientsPhone.setString(2, newValue);
						prepUpdatePatientsPhone.setString(3, patientID);
						prepUpdatePatientsPhone.executeUpdate();
						break;
					case "PROCESSING TREATMENT PLAN":
						prepUpdatePatientsTreatmentPlan.setString(1, newValue);
						prepUpdatePatientsTreatmentPlan.setString(2, patientID);
						prepUpdatePatientsTreatmentPlan.executeUpdate();
						break;
					case "IN WARD":
						prepUpdatePatientsInWard.setBoolean(1, newValue.equals("yes")? true : false);
						prepUpdatePatientsInWard.setString(2, patientID);
						prepUpdatePatientsInWard.executeUpdate();
						break;
					case "COMPLETING TREATMENT":
						prepUpdatePatientsStatus.setBoolean(1, newValue.equals("yes")? true : false);
						prepUpdatePatientsStatus.setString(2, patientID);
						prepUpdatePatientsStatus.executeUpdate();
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
				prepDeletePatients.setString(1, patientID);
				// To-do: need to consider the effect on everything related to this patient? e.g. release bed, update record, etc.
				prepDeletePatients.executeUpdate();
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
				prepAddWards.setString(1, wardNumber);
				prepAddWards.setInt(2, Integer.valueOf(capacity));
				prepAddWards.setInt(3, Integer.valueOf(daycharge));
				prepAddWards.setString(4, responsibleNurse);
				prepAddWards.executeUpdate();
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
			prepGetWards.setString(1, wardNumber);
			// To-do: need to get all the patients' SSN too?!
			ResultSet rs = prepGetWards.executeQuery();
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
						prepUpdateWardsCapacity.setInt(1, Integer.valueOf(newValue));
						prepUpdateWardsCapacity.setString(2, wardNumber);
						prepUpdateWardsCapacity.executeUpdate();
						break;
					case "CHARGE PER DAY":
						prepUpdateWardsCharge.setInt(1, Integer.valueOf(newValue));
						prepUpdateWardsCharge.setString(2, wardNumber);
						prepUpdateWardsCharge.executeUpdate();
						break;
					case "RESPONSIBLE NURSE":
						prepUpdateWardsNurse.setString(1, newValue);
						prepUpdateWardsNurse.setString(2, wardNumber);
						prepUpdateWardsNurse.executeUpdate();
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
			prepGetAllTreatmentRecords.setString(1, patientID);
			result = prepGetAllTreatmentRecords.executeQuery();

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
			prepGetTreatmentRecord.setString(1, recordID);
			result = prepGetTreatmentRecord.executeQuery();

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
				prepUpdateTreatmentEndDate.setString(1, valueToChange);
				prepUpdateTreatmentEndDate.setString(2, recordID);
				prepUpdateTreatmentEndDate.setString(3, recordID);
				prepUpdateTreatmentEndDate.executeUpdate();
				break;
			case "PRESCRIPTION":
				prepUpdateTreatmentPrescription.setString(1, valueToChange);
				prepUpdateTreatmentPrescription.setString(2, recordID);
				prepUpdateTreatmentPrescription.executeUpdate();
				break;
			case "DIAGNOSISDETAILS":
				prepUpdateTreatmentDiagnosisDetails.setString(1, valueToChange);
				prepUpdateTreatmentDiagnosisDetails.setString(2, recordID);
				prepUpdateTreatmentDiagnosisDetails.executeUpdate();
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
				prepAddMedicalRecord.setString(1, recordID);
				prepAddMedicalRecord.setString(2, patientID);
				prepAddMedicalRecord.setDate(3, java.sql.Date.valueOf(startDate));
				prepAddMedicalRecord.setDate(4, java.sql.Date.valueOf(endDate));
				prepAddMedicalRecord.setString(5, resDoc);
				prepAddMedicalRecord.executeUpdate();
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
	public static void manageTestRecordAdd(String recordID, String testType, String testResult) {
		// to be done: check success or not and report
		try {

			// Start transaction
			connection.setAutoCommit(false);

			try {
				prepAddTestRecord.setString(1, recordID);
				prepAddTestRecord.setString(2, testType);
				prepAddTestRecord.setString(3, testResult);
				prepAddTestRecord.executeUpdate();
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
			prepGetAllTestRecords.setString(1, patientID);
			result = prepGetAllTestRecords.executeQuery();

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
			prepGetTestRecord.setString(1, recordID);
			result = prepGetTestRecord.executeQuery();

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
				prepUpdateTestEndDate.setString(1, valueToChange);
				prepUpdateTestEndDate.setString(2, recordID);
				prepUpdateTestEndDate.setString(3, recordID);
				prepUpdateTestEndDate.executeUpdate();
				break;
			case "TESTTYPE":
				prepUpdateTestTestType.setString(1, valueToChange);
				prepUpdateTestTestType.setString(2, recordID);
				prepUpdateTestTestType.executeUpdate();
				break;
			case "TESTRESULT":
				prepUpdateTestTestResult.setString(1, valueToChange);
				prepUpdateTestTestResult.setString(2, recordID);
				prepUpdateTestTestResult.executeUpdate();
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
	public static void manageCheckinRecordAdd(String recordID, String wardNumber, String bedNumber) {
		// to be done: check success or not and report
		try {

			// Start transaction
			connection.setAutoCommit(false);
			try {
				prepAddCheckinRecord.setString(1, recordID);
				prepAddCheckinRecord.setString(2, wardNumber);
				prepAddCheckinRecord.setString(3, bedNumber);
				prepAddCheckinRecord.executeUpdate();
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
			prepGetAllCheckinRecords.setString(1, patientID);
			result = prepGetAllCheckinRecords.executeQuery();

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
			prepGetCheckinRecord.setString(1, recordID);
			result = prepGetCheckinRecord.executeQuery();

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
				prepUpdateCheckinEndDate.setString(1, valueToChange);
				prepUpdateCheckinEndDate.setString(2, recordID);
				prepUpdateCheckinEndDate.setString(3, recordID);
				prepUpdateCheckinEndDate.executeUpdate();
				break;
			case "WARDNUMBER":
				prepUpdateCheckinWard.setString(1, valueToChange);
				prepUpdateCheckinWard.setString(2, recordID);
				prepUpdateCheckinWard.executeUpdate();
				break;
			case "BEDNUMBER":
				prepUpdateCheckinBed.setString(1, valueToChange);
				prepUpdateCheckinBed.setString(2, recordID);
				prepUpdateCheckinBed.executeUpdate();
				break;
			default:
				System.out.println("\nCannot update the '" + attributeToChange);
				break;
			}
		} catch (Throwable err) {
			// error_handler(err);
		}
	}

	private static boolean reportHistoryByTime(String date) {
		boolean success = false;
		try {
			prepReportHistoryByTime.setDate(1, java.sql.Date.valueOf(date));
			result = prepReportHistoryByTime.executeQuery();
			result.beforeFirst();
			if (result.next()) {
				System.out.print("\trecord ID : "+result.getString("recordID")+" | ");
				System.out.print("patient ID : "+result.getString("patientID")+" | ");
				System.out.print("startDate : "+result.getString("startDate")+" | ");
				System.out.print("endDate : "+result.getString("endDate")+" | ");
				System.out.println("responsible doctor : "+result.getString("responsibleDoctor"));
			}
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return success;
	}

	private static boolean reportHistoryByPatient(String patientID) {
		boolean success = false;
		try {
			prepReportHistoryByPatient.setString(1, patientID);
			result = prepReportHistoryByPatient.executeQuery();
			result.beforeFirst();
			if (result.next()) {
				System.out.print("\trecord ID : "+result.getString("recordID")+" | ");
				System.out.print("patient ID : "+result.getString("patientID")+" | ");
				System.out.print("startDate : "+result.getString("startDate")+" | ");
				System.out.print("endDate : "+result.getString("endDate")+" | ");
				System.out.println("responsible doctor : "+result.getString("responsibleDoctor"));
			}
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return success;
	}

	// Report current usage status
	public static boolean reportCurrentWardUsageStatus() {
		boolean success = false;
		try {
			result = prepReportCurrentWardUsageStatus.executeQuery();
			result.beforeFirst();
			if (result.next()) {
				System.out.print("\tward number : "+result.getString("ward number")+" | ");
				System.out.print("bed number : "+result.getString("bed number")+" | ");
				System.out.println("status : "+result.getString("usage"));
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
			result = prepReportCurrentBedUsageStatus.executeQuery();
			result.beforeFirst();
			if (result.next()) {
				System.out.print("\tward number : "+result.getString("ward number")+" | ");
				System.out.println("status : "+result.getString("usage"));
			}
			success = true;
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
			result = prepReportNumberOfPatientsPerMonth.executeQuery();
			result.beforeFirst();
			if (result.next()) {
				System.out.print("\tmonth : "+result.getString("month")+" | ");
				System.out.println("number of patients : "+result.getString("num"));
			}
			success = true;
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
			result = prepReportWardUsagePercentage.executeQuery();
			result.beforeFirst();
			if (result.next()) {
				System.out.println("Ward usage percentage is: "+result.getString("usage percentage"));
			}
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return success;
	}

	// Report doctor responsibility
	public static boolean reportDoctorResponsibility(String doctorID) {
		boolean success = false;
		try {
			prepReportDoctorResponsibility.setString(1, doctorID);
			result = prepReportDoctorResponsibility.executeQuery();
			result.beforeFirst();
			System.out.println("\nDoctor("+doctorID+") is reponsible for: \n");
			if (result.next()) {
				System.out.print("\tpatient ID : " + result.getString("patientID")+" | ");
				System.out.print("start date : " + result.getDate("startDate")+" | ");
				System.out.println("patient ID : " + result.getString("endDate"));
			}
			success = true;
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
			result = prepReportStaffInformation.executeQuery();
			result.beforeFirst();
			while (result.next()) {
				System.out.print("staff ID :" + result.getString("staffID") + " | ");
				System.out.print("name :" + result.getString("name") + " | ");
				System.out.print("age :" + result.getInt("age") + " | ");
				System.out.print("gender :" + result.getString("gender") + " | ");
				System.out.print("job title :" + result.getString("jobTitle") + " | ");
				System.out.print("professional title:" + result.getString("profTitle") + " | ");
				System.out.print("department :" + result.getString("department") + " | ");
				System.out.print("phone :" + result.getString("phone") + " | ");
				System.out.println("address:" + result.getString("address"));
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
				prepAddPayerInfo.setString(1, payerSSN);
				prepAddPayerInfo.setString(2, address);
				prepAddPayerInfo.executeUpdate();
				prepAddBillingAccount.setString(1, accountID);
				prepAddBillingAccount.setString(2, patientID);
				prepAddBillingAccount.setDate(3, java.sql.Date.valueOf(visitDate));
				prepAddBillingAccount.setString(4, payerSSN);
				prepAddBillingAccount.setString(5, paymentMethod);
				prepAddBillingAccount.setString(6, cardNumber);
				prepAddBillingAccount.setDouble(7, Double.parseDouble(registrationFee));
				prepAddBillingAccount.setBoolean(8, medicationPrescribed.equals("yes")?true:false);
				prepAddBillingAccount.setDouble(9, Double.parseDouble(accommandationFee));
				prepAddBillingAccount.executeUpdate();
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
			prepGetBillingAccount.setString(1, accountID);
			result = prepGetBillingAccount.executeQuery();
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
					prepUpdateCheckinEndDate.setString(1, valueToChange);
					prepUpdateCheckinEndDate.setString(2, accountID);
					prepUpdateTestEndDate.executeUpdate();
					break;
				case "PAYMENTTYPE":
					prepUpdateBillingAccountPaymentType.setString(1, valueToChange);
					prepUpdateBillingAccountPaymentType.setString(2, accountID);
					prepUpdateBillingAccountPaymentType.executeUpdate();
					break;
				case "CARDNUMBER":
					prepUpdateBillingAccountCardNumber.setString(1, valueToChange);
					prepUpdateBillingAccountCardNumber.setString(2, accountID);
					prepUpdateBillingAccountCardNumber.executeUpdate();
					break;
				case "REGISTRATIONFEE":
					prepUpdateBillingAccountRegistrationFee.setDouble(1, Double.parseDouble(valueToChange));
					prepUpdateBillingAccountRegistrationFee.setString(2, accountID);
					prepUpdateBillingAccountRegistrationFee.executeUpdate();
				case "ACCOMMANDATIONFEE":
					prepUpdateBillingAccountAccommandationFee.setDouble(1, Double.parseDouble(valueToChange));
					prepUpdateBillingAccountAccommandationFee.setString(2, accountID);
					prepUpdateBillingAccountAccommandationFee.executeUpdate();
				case "MEDICATIONPRESCRIBED":
					prepUpdateBillingAccountMedicationPrescribed.setBoolean(1, Boolean.parseBoolean(valueToChange));
					prepUpdateBillingAccountMedicationPrescribed.setString(2, accountID);
					prepUpdateBillingAccountMedicationPrescribed.executeUpdate();
				case "VISITDATE":
					prepUpdateBillingAccountVisitDate.setDate(1, java.sql.Date.valueOf(valueToChange));
					prepUpdateBillingAccountVisitDate.setString(2, accountID);
					prepUpdateBillingAccountVisitDate.executeUpdate();
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
				prepDeleteBillingAccount.setString(1, accountID);
				prepDeleteBillingAccount.executeUpdate();
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
				prepDeleteWardInfo.setString(1, wardNum);
				prepDeleteWardInfo.executeUpdate();
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
				prepDeleteBedInfo.setString(1, wardNum);
				prepDeleteBedInfo.setString(2, bedNum);
				prepDeleteBedInfo.executeUpdate();
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
				prepAddBedInfo.setString(1, wardNum);
				prepAddBedInfo.setString(2, bedNum);
				prepAddBedInfo.executeUpdate();
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
			prepGetBedInfo.setString(1, wardNum);
			prepGetBedInfo.setString(2, bedNum);
			result = prepGetBedInfo.executeQuery();
			
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
			prepAssignBed.setString(1, patientID);
			prepAssignBed.setString(2, wardNum);
			prepAssignBed.setString(3, bedNum);
			prepAssignBed.executeUpdate();
		} catch (Throwable err) {
			//error_handler(err);
		}
	}
	
	// Reserve bed(not sure whether we need this function currently)
	
	// Release bed
	public static void manageBedRelease(String wardNum, String bedNum) {
		
		try {
			connection.setAutoCommit(true);
			prepReleaseBed.setString(1, wardNum);
			prepReleaseBed.setString(2, bedNum);
			prepReleaseBed.executeUpdate();
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
				prepAddAssigned.setString(1, patientID);
				prepAddAssigned.setString(2, wardNum);
				prepAddAssigned.setString(3, bedNum);
				prepAddAssigned.setDate(4, java.sql.Date.valueOf(start));
				prepAddAssigned.setDate(5, java.sql.Date.valueOf(end));
				prepAddAssigned.executeUpdate();
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
				
				prepAddTreatmentRecord.setString(1, recordID);
				prepAddTreatmentRecord.setString(2, pres);
				prepAddTreatmentRecord.setString(3, diag);
				prepAddTreatmentRecord.executeUpdate();
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
		//addMedicalRecord(String recordID, String patientID, String startDate, String endDate, String resDoc)
		try {
			String recordID, pres, diag, patientID, startDate, endDate, resDoc;
			System.out.print("\nEnter recordID\n> ");
			recordID = scanner.nextLine();
			System.out.print("\nEnter prescription\n> ");
			pres = scanner.nextLine();
			System.out.print("\nEnter diagnosis\n> ");
			diag = scanner.nextLine();
			System.out.print("\nEnter patientID\n> ");
			patientID = scanner.nextLine();
			System.out.print("\nEnter startDate\n> ");
			startDate = scanner.nextLine();
			System.out.print("\nEnter endDate\n> ");
			endDate = scanner.nextLine();
			System.out.print("\nEnter responsible doctor\n> ");
			resDoc = scanner.nextLine();
			manageTreatmentRecordAdd(recordID, pres, diag);
			addMedicalRecord(recordID, patientID, startDate, endDate, resDoc);
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}
	public static void userTreatmentGetAll(){
		//showAllTreatmentRecords(String patientID)
		try {
			String patientID;
			System.out.print("\nEnter patientID\n> ");
			patientID = scanner.nextLine();
			showAllTreatmentRecords(patientID);
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}
	public static void userTreatmentGet(){
		//showTreatmentRecord(String recordID)
		try {
			String recordID;
			System.out.print("\nEnter recordID\n> ");
			recordID = scanner.nextLine();
			showTreatmentRecord(recordID);
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}
	public static void userTreatmentUpdate(){
		//manageTreatmentUpdate(String recordID, String attributeToChange, String valueToChange)
		try {
			String recordID, attributeToChange, valueToChange;
			System.out.print("\nEnter recordID\n> ");
			recordID = scanner.nextLine();
			System.out.print("\nEnter attribute to change\n> ");
			attributeToChange = scanner.nextLine();
			System.out.print("\nEnter value to change\n> ");
			valueToChange = scanner.nextLine();
			manageTreatmentUpdate(recordID, attributeToChange,valueToChange);
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}
	public static void userTestAdd(){
		//manageTestRecordAdd(String recordID, String testType, String testResult)
		//addMedicalRecord(String recordID, String patientID, String startDate, String endDate, String resDoc)
		try {
			String recordID,testType, testResult, patientID, startDate, endDate, resDoc;
			System.out.print("\nEnter recordID\n> ");
			recordID = scanner.nextLine();
			System.out.print("\nEnter testType\n> ");
			testType = scanner.nextLine();
			System.out.print("\nEnter testResult\n> ");
			testResult = scanner.nextLine();
			System.out.print("\nEnter patientID\n> ");
			patientID = scanner.nextLine();
			System.out.print("\nEnter startDate\n> ");
			startDate = scanner.nextLine();
			System.out.print("\nEnter endDate\n> ");
			endDate = scanner.nextLine();
			System.out.print("\nEnter responsible doctor\n> ");
			resDoc = scanner.nextLine();
			manageTestRecordAdd(recordID, testType, testResult);
			addMedicalRecord(recordID, patientID, startDate, endDate, resDoc);
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}
	public static void userTestGetAll(){
		//showAllTestRecords(String patientID)
		try {
			String patientID;
			System.out.print("\nEnter patientID\n> ");
			patientID = scanner.nextLine();
			showAllTestRecords(patientID);
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}
	public static void userTestGet(){
		//showTestRecord(String recordID)
		try {
			String recordID;
			System.out.print("\nEnter recordID\n> ");
			recordID = scanner.nextLine();
			showTestRecord(recordID);
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}
	public static void userTestUpdate(){
		//manageTestUpdate(String recordID, String attributeToChange, String valueToChange)
		try {
			String recordID, attributeToChange, valueToChange;
			System.out.print("\nEnter recordID\n> ");
			recordID = scanner.nextLine();
			System.out.print("\nEnter attribute to change\n> ");
			attributeToChange = scanner.nextLine();
			System.out.print("\nEnter value to change\n> ");
			valueToChange = scanner.nextLine();
			manageTestUpdate(recordID, attributeToChange,valueToChange);
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}
	public static void userCheckinAdd(){
		//manageCheckinRecordAdd(String recordID, String wardNumber, String bedNumber)
		//addMedicalRecord(String recordID, String patientID, String startDate, String endDate, String resDoc)
		try {
			String recordID,wardNumber, bedNumber, patientID, startDate, endDate, resDoc;
			System.out.print("\nEnter recordID\n> ");
			recordID = scanner.nextLine();
			System.out.print("\nEnter wardNumber\n> ");
			wardNumber = scanner.nextLine();
			System.out.print("\nEnter bedNumber\n> ");
			bedNumber = scanner.nextLine();
			System.out.print("\nEnter patientID\n> ");
			patientID = scanner.nextLine();
			System.out.print("\nEnter startDate\n> ");
			startDate = scanner.nextLine();
			System.out.print("\nEnter endDate\n> ");
			endDate = scanner.nextLine();
			System.out.print("\nEnter responsible doctor\n> ");
			resDoc = scanner.nextLine();
			manageCheckinRecordAdd(recordID, wardNumber, bedNumber);
			addMedicalRecord(recordID, patientID, startDate, endDate, resDoc);
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}
	public static void userCheckinGetAll(){
		//showAllCheckinRecords(String patientID)
		try {
			String patientID;
			System.out.print("\nEnter patientID\n> ");
			patientID = scanner.nextLine();
			showAllCheckinRecords(patientID);
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}
	public static void userCheckinGet(){
		//showCheckinRecord(String recordID)
		try {
			String recordID;
			System.out.print("\nEnter recordID\n> ");
			recordID = scanner.nextLine();
			showCheckinRecord(recordID);
		}
		catch (Throwable err) {
			error_handler(err);
		}
	}
	public static void userCheckinUpdate(){
		//manageCheckinUpdate(String recordID, String attributeToChange, String valueToChange)
		try {
			String recordID, attributeToChange, valueToChange;
			System.out.print("\nEnter recordID\n> ");
			recordID = scanner.nextLine();
			System.out.print("\nEnter attribute to change\n> ");
			attributeToChange = scanner.nextLine();
			System.out.print("\nEnter value to change\n> ");
			valueToChange = scanner.nextLine();
			manageCheckinUpdate(recordID, attributeToChange,valueToChange);
		}
		catch (Throwable err) {
			error_handler(err);
		}
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
			prepAddTreatmentRecord.setString(1, wardNumber);
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
			populateTreatmentTable();
			populateTestTable();
			populateCheckinTable();
			populateBillingAccountsTable();
			populateBedsTable();
			populateAssignedTable();
            
            // Print available commands
            printCommands(CMD_MAIN);

            // Watch for user input
            currentMenu = CMD_MAIN;
            scanner = new Scanner(System.in);
            while (quit == false) {
            	System.out.print("user > ");
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

	private static void userReportHistoryByTime() {
		try {
			System.out.print("\nReport medical record history by given time(YYYY-MM-DD)\n");
			String date = scanner.nextLine();
			reportHistoryByTime(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void userReportHistoryByPatient() {
		try {
			System.out.print("\nReport medical record history by given patient(patientID)\n");
			String pid = scanner.nextLine();
			reportHistoryByPatient(pid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void userReportWardUsageStatus() {
		try {
			System.out.print("\nReport ward usage status\n");
			reportCurrentWardUsageStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void userReportBedUsageStatus() {
		try {
			System.out.print("\nReport bed usage status\n");
			reportCurrentBedUsageStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private static void userReportNumberOfPatients() {
		try {
			System.out.print("\nReport number of patients per month\n");
			reportNumberOfPatientsPerMonth();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void userReportWardUsagePercentage() {
		try {
			System.out.print("\nReport ward usage percentage\n");
			reportWardUsagePercentage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void userReportDoctorResponse() {
		try {
			System.out.print("\nReport doctors' responsibilities");
			System.out.print("\nPlease input ID of the doctor whose responsibility you want to know:\n");
			String doctorID = scanner.nextLine();
			reportDoctorResponsibility(doctorID);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private static void userReportStaffInfo() {
		try {
			System.out.println("\nReport staff information grouped by role");
			reportStaffInformation();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private static void startup_printAvailableCommands(String cmd) {
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
