package lt.sveikata.patient;

import java.util.List;
import java.util.stream.Collectors;

import lt.sveikata.doctor.DoctorRepository;
import lt.sveikata.prescription.Prescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PatientService {

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// list of all patients

	public List<PatientForClient> receiveAllPatients() {
		List<Patient> patientsFromDatabase = getPatientRepository().findAll();
		List<PatientForClient> patientsForClient = patientsFromDatabase.stream().map((patient) -> {
			PatientForClient pfc = new PatientForClient();
			pfc.setUserName(patient.getUserName());
			pfc.setRole(patient.getRole());
			pfc.setFirstName(patient.getFirstName());
			pfc.setLastName(patient.getLastName());
			return pfc;
		}).collect(Collectors.toList());
		return patientsForClient;
	}

	// list of patients found by username

	public PatientForClient receiveAllPatients(String userName) {
		List<Patient> patientsFromDatabase = getPatientRepository().findByUserName(userName);
		Patient patient = patientsFromDatabase.get(0);
		PatientForClient patientForClient = new PatientForClient();
		patientForClient.setRole(patient.getRole());
		patientForClient.setFirstName(patient.getFirstName());
		patientForClient.setLastName(patient.getLastName());
		patientForClient.setSuspended(patient.isSuspended());
		patientForClient.setDateOfBirth(patient.getDateOfBirth());
		patientForClient.setUserName(patient.getUserName());
		return patientForClient;
	}

	public List<PatientForClient> recieveAllPatientsWithoutDoctors() {
		List<Patient> patientsWithoutDoctorFromDatabase = getPatientRepository().findAllByDoctorIsNull();
		List<PatientForClient> patientsWithoutDoctorForClient = patientsWithoutDoctorFromDatabase.stream()
				.map((patient) -> {
					PatientForClient ptc = new PatientForClient();
					ptc.setFirstName(patient.getFirstName());
					ptc.setLastName(patient.getLastName());
					ptc.setUserName(patient.getUserName());
					return ptc;
				}).collect(Collectors.toList());

		return patientsWithoutDoctorForClient;
	}

	// single patient found by personal id

	public PatientForClient receivePatientFromDatabase(long personalId) {
		Patient patientFromDatabase = getPatientRepository().findByPersonalId(personalId);
		PatientForClient patientForClient = new PatientForClient();
		// patientForClient.setRole(patientFromDatabase.getRole());
		patientForClient.setFirstName(patientFromDatabase.getFirstName());
		patientForClient.setLastName(patientFromDatabase.getLastName());
		// patientForClient.setDoctor(patientFromDatabase.getDoctor());
		patientForClient.setPersonalId(patientFromDatabase.getPersonalId());
		return patientForClient;
	}

	// add new patient to database

	public void addNewPatient(AddNewPatient newPatient) {
		Patient pat = new Patient();
		pat.setFirstName(newPatient.getFirstName());
		pat.setLastName(newPatient.getLastName());
		pat.setDateOfBirth(newPatient.getDateOfBirth());
		pat.setPersonalId(newPatient.getPersonalId());
		pat.setUserName(newPatient.getUserName());
		pat.setPassword(passwordEncoder.encode(newPatient.getPassword()));
		pat.setRole("PATIENT");
		patientRepository.save(pat);

	}

	public Patient getByUserId(long userId) {
		Patient pat = patientRepository.findByUserId(userId);
		pat.getFirstName();
		pat.getLastName();

		return pat;

	}

	public DoctorRepository getDoctorRepository() {
		return doctorRepository;
	}

	public void setDoctorRepository(DoctorRepository doctorRepository) {
		this.doctorRepository = doctorRepository;
	}

	public void assignDoctor(String patientUsername, String doctorUserName) {
		Patient pat = patientRepository.findAllByUserName(patientUsername);
		pat.setDoctor(doctorRepository.findOneByUserName(doctorUserName));
		patientRepository.save(pat);
	}

	// get patients list by doctorId
	public List<Patient> byDoctorId(long doctorId) {
		return patientRepository.getPatientById(doctorId);
	}

	public PatientRepository getPatientRepository() {
		return patientRepository;
	}

	public void setPatientRepository(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}
}
