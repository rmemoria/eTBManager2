package org.msh.tb.cases;


import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.msh.tb.adminunits.AdminUnitSelection;
import org.msh.tb.cases.exams.ExamCultureHome;
import org.msh.tb.cases.exams.ExamMicroscopyHome;
import org.msh.tb.cases.exams.MedicalExaminationHome;
import org.msh.tb.cases.treatment.StartTreatmentHome;
import org.msh.tb.entities.*;
import org.msh.tb.entities.enums.*;
import org.msh.tb.login.UserSession;
import org.msh.tb.tbunits.TBUnitSelection;
import org.msh.tb.tbunits.TBUnitType;
import org.msh.utils.date.DateUtils;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;


/**
 * Handle TB and DR-TB cases editing and new notification
 * @author Ricardo Memoria
 *
 */
@Name("caseEditingHome")
@Scope(ScopeType.CONVERSATION)
public class CaseEditingHome {

	@In(create=true) protected CaseHome caseHome;
	@In(create=true)
	protected PatientHome patientHome;
	@In
	protected EntityManager entityManager;
	@In(create=true) protected PrevTBTreatmentHome prevTBTreatmentHome;
	@In(create=true) MedicalExaminationHome medicalExaminationHome;
	@In(create=true) FacesMessages facesMessages;
	
	@In(required=false) StartTreatmentHome startTreatmentHome;
	@In(required=false) ExamMicroscopyHome examMicroscopyHome;
	@In(required=false) ExamCultureHome examCultureHome;

	private TBUnitSelection tbunitselection;
	private AdminUnitSelection notifAdminUnit;
	private AdminUnitSelection currentAdminUnit;
	protected boolean initialized;

	/**
	 * 1 - Standard, 2 - Individualized
	 */
	protected int regimenType;
	
	
	public String selectPatientData() {
		initialized = false;
		if (initializeNewNotification().equals("initialized"))
			return "/cases/casenew.xhtml";
		return "error";
	}

	/**
	 * Initialize a new notification
	 */
	public String initializeNewNotification() {
		if (initialized)
			return "initialized";
		
		Patient p = patientHome.getInstance();
		
		if (caseHome.getInstance().getClassification() == null)
			return "/cases/index.xhtml";
		
		if ((!patientHome.isManaged()) && 
			(p.getName() == null) && (p.getMiddleName() == null) && (p.getLastName() == null) &&
			(p.getBirthDate() == null))
		return "patient-searching";
		
		caseHome.getInstance().setPatient(patientHome.getInstance());
		
		if (p.getBirthDate() != null)
			updatePatientAge();
		
		// initialize default values
		UserWorkspace userWorkspace = (UserWorkspace)Component.getInstance("userWorkspace");
		if (userWorkspace != null) {
			if (userWorkspace.getTbunit().isNotifHealthUnit())
				getTbunitselection().setSelected(userWorkspace.getTbunit());
			
			AdministrativeUnit au = userWorkspace.getAdminUnit();
			if (au == null)
				au = userWorkspace.getTbunit().getAdminUnit();
			
			if (au != null) {
				au = entityManager.find(AdministrativeUnit.class, au.getId());
				
				getNotifAdminUnit().setSelectedUnit(au);

				if (getTbunitselection().getSelected() == null) {
					List<AdministrativeUnit> lst = getTbunitselection().getAdminUnits();
					
					if (lst != null)
					for (AdministrativeUnit adminUnit: lst) {
						if (adminUnit.isSameOrChildCode(au.getCode())) {
							getTbunitselection().setAdminUnit(adminUnit);
						}
					}					
				}
			}
		}

		// initialize items with previous TB treatments
//		prevTBTreatmentHome.getTreatments();
		
		Events.instance().raiseEvent("new-notification");
		
		initialized = true;
		return "initialized";
	}


	/**
	 * Update the patient's age according to his birth date and the diagnosis date 
	 */
	protected void updatePatientAge() {
		Date dtBirth = caseHome.getInstance().getPatient().getBirthDate();
		if (dtBirth == null)
			return;
		
		Date dtDiag = caseHome.getInstance().getDiagnosisDate();
		if (dtDiag == null)
			dtDiag = new Date();
		
		int age = DateUtils.yearsBetween(dtBirth, dtDiag);
		caseHome.getInstance().setAge(age);
	}


	
	/**
	 * Initialize the caseHome object for editing
	 */
	public void initializeEditing() {
		if (initialized)
			return;
		
		prevTBTreatmentHome.setEditing(true);
//		prevTBTreatmentHome.setNumItems(prevTBTreatmentHome.getTreatments().size());
		
		TbCase tbcase = caseHome.getInstance();
		Address addr = tbcase.getCurrentAddress();
		if (addr == null)
			tbcase.setCurrentAddress(new Address());
		addr = tbcase.getNotifAddress();
		if (addr == null)
			tbcase.setNotifAddress(new Address());
		
		if (getTbunitselection().getSelected() == null)
			tbunitselection.setSelected(tbcase.getNotificationUnit());
		
		if (getNotifAdminUnit().getSelectedUnit() == null)
			notifAdminUnit.setSelectedUnit(tbcase.getNotifAddress().getAdminUnit());
		if (getCurrentAdminUnit().getSelectedUnit() == null)
			currentAdminUnit.setSelectedUnit(tbcase.getCurrentAddress().getAdminUnit());

		updatePatientAge();

		initialized = true;
	}


	
	/**
	 * Check if the register date is valid. If not, return the register date of the case that is already 
	 * registered in the period
	 * @return Register date of the case that is in the date registered
	 */
	public Date registerDateValid() {
		if (caseHome.getInstance().getPatient().getId() == null)
			return null;
		
		Date dt = caseHome.getInstance().getRegistrationDate();

		try {
			// uses a range of one week
			Date dt2 = DateUtils.incDays(dt, -7);
			Date dt3 = DateUtils.incDays(dt, 7);

			return (Date)entityManager.createQuery("select max(registerDate) from TbCase c " +
					"where c.registerDate = :dt or (c.registerDate <= :dt2 and c.treatmentPeriod.endDate >= :dt2) " +
					"and c.paitent.id = #{tbcase.patient.id}")
					.setParameter("dt", dt)
					.setParameter("dt2", dt2)
					.setParameter("dt3", dt3)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}



	/**
	 * Save changes made to a case
	 * @return
	 */
	public String saveEditing() {
		if (!validateData())
			return "error";
		
		TbCase tbcase = caseHome.getInstance();

		tbcase.setNotificationUnit(tbunitselection.getSelected());
		tbcase.getNotifAddress().setAdminUnit(notifAdminUnit.getSelectedUnit());
		tbcase.getCurrentAddress().setAdminUnit(currentAdminUnit.getSelectedUnit());
		
		if (!tbcase.isNotifAddressChanged()) {
			tbcase.getCurrentAddress().copy(tbcase.getNotifAddress());
		}

		prevTBTreatmentHome.persist();
		
		updatePatientAge();

		String s = caseHome.persist();
		
		if ("persisted".equals(s))
			caseHome.updateCaseTags();

		return s;
	}

	/**
	 * Save changes made to a case
	 * @return
	 */
	public String saveEditingWithoutValidation() {
		TbCase tbcase = caseHome.getInstance();

		tbcase.setNotificationUnit(tbunitselection.getSelected());
		tbcase.getNotifAddress().setAdminUnit(notifAdminUnit.getSelectedUnit());
		tbcase.getCurrentAddress().setAdminUnit(currentAdminUnit.getSelectedUnit());
		
		if (!tbcase.isNotifAddressChanged()) {
			tbcase.getCurrentAddress().copy(tbcase.getNotifAddress());
		}

		prevTBTreatmentHome.persist();
		
		updatePatientAge();

		String s = caseHome.persist();
		
		if ("persisted".equals(s))
			caseHome.updateCaseTags();

		return s;
	}


	/**
	 * Start a standard regimen for a case
	 */
	protected void startTreatment() {
		if (startTreatmentHome == null)
			return;
		
		TbCase tbcase = caseHome.getInstance();
		if (startTreatmentHome.getIniTreatmentDate() == null) {
			tbcase.setState(CaseState.WAITING_TREATMENT);
			return;
		}

		startTreatmentHome.getTbunitselection().setSelected(tbcase.getNotificationUnit());
		startTreatmentHome.setUseDefaultDoseUnit(true);
		startTreatmentHome.updatePhases();
		startTreatmentHome.startStandardRegimen();		
	}




	/**
	 * Register a new TB or MDR-TB case
	 * @return "persisted" if successfully registered
	 */
	@Transactional
	public String saveNew() {
		if (!validateData())
			return "error";
		
		TbCase tbcase = caseHome.getInstance();
		
		// is this a suspect case?
		if (tbcase.getDiagnosisType() == DiagnosisType.SUSPECT) {
			tbcase.setSuspectClassification(tbcase.getClassification());
		}
		
		// save the patient's data
		patientHome.persist();

		// get notification unit
		tbcase.setNotificationUnit(getTbunitselection().getSelected());
		tbcase.getNotifAddress().setAdminUnit(notifAdminUnit.getSelectedUnit());
		
		tbcase.setOwnerUnit(tbcase.getNotificationUnit());
		
		Address notifAddress = tbcase.getNotifAddress();
		Address curAddress = tbcase.getCurrentAddress();
		//curAddress.copy(notifAddress);

		tbcase.setNotifAddress(notifAddress);
		tbcase.setCurrentAddress(curAddress);

		if (tbcase.getValidationState() == null)
			tbcase.setValidationState(ValidationState.WAITING_VALIDATION);
		
		if (tbcase.getState() == null)
			tbcase.setState(CaseState.WAITING_TREATMENT);
		
		updatePatientAge();

		// treatment was defined ?
		caseHome.setTransactionLogActive(true);
		if (!caseHome.persist().equals("persisted"))
			return "error";

		caseHome.setTransactionLogActive(false);

		// define the treatment regimen if it's not individualized (==2)
		if (regimenType != 2)
			startTreatment();
		
		if (medicalExaminationHome != null) {
			MedicalExamination medExa = medicalExaminationHome.getInstance();
			if (medExa.getDate() != null) {
				medExa.setAppointmentType(MedAppointmentType.SCHEDULLED);
				medExa.setUsingPrescMedicines(YesNoType.YES);
				medicalExaminationHome.persist();			
			}
		}

		// save additional information
		if (prevTBTreatmentHome != null)
			prevTBTreatmentHome.persist();

		caseHome.updateCaseTags();

		return (regimenType == 2? "individualized": "persisted");
	}

	/**
	 * Register a new TB or MDR-TB case
	 * @return "persisted" if successfully registered
	 */
	@Transactional
	public String saveNewWithoutValidation() {
		TbCase tbcase = caseHome.getInstance();
		
		// is this a suspect case?
		if (tbcase.getDiagnosisType() == DiagnosisType.SUSPECT) {
			tbcase.setSuspectClassification(tbcase.getClassification());
		}
		
		// save the patient's data
		patientHome.persist();

		// get notification unit
		tbcase.setNotificationUnit(getTbunitselection().getSelected());
		tbcase.getNotifAddress().setAdminUnit(notifAdminUnit.getSelectedUnit());
		
		tbcase.setOwnerUnit(tbcase.getNotificationUnit());
		
		Address notifAddress = tbcase.getNotifAddress();
		Address curAddress = tbcase.getCurrentAddress();
		//curAddress.copy(notifAddress);

		tbcase.setNotifAddress(notifAddress);
		tbcase.setCurrentAddress(curAddress);

		if (tbcase.getValidationState() == null)
			tbcase.setValidationState(ValidationState.WAITING_VALIDATION);
		
		if (tbcase.getState() == null)
			tbcase.setState(CaseState.WAITING_TREATMENT);
		
		updatePatientAge();

		// treatment was defined ?
		caseHome.setTransactionLogActive(true);
		if (!caseHome.persist().equals("persisted"))
			return "error";

		caseHome.setTransactionLogActive(false);

		// define the treatment regimen if it's not individualized (==2)
		if (regimenType != 2)
			startTreatment();
		
		if (medicalExaminationHome != null) {
			MedicalExamination medExa = medicalExaminationHome.getInstance();
			if (medExa.getDate() != null) {
				medExa.setAppointmentType(MedAppointmentType.SCHEDULLED);
				medExa.setUsingPrescMedicines(YesNoType.YES);
				medicalExaminationHome.persist();			
			}
		}

		// save additional information
		if (prevTBTreatmentHome != null)
			prevTBTreatmentHome.persist();

		caseHome.updateCaseTags();

		return (regimenType == 2? "individualized": "persisted");
	}

	/**
	 * Checks if information entered or modified is valid to be recorded
	 * @return true if successfully validated, otherwise returns false if fail
	 */
	public boolean validateData() {
		TbCase tbcase = caseHome.getInstance();

		if (tbcase.getRegistrationDate() == null) {
			facesMessages.addToControlFromResourceBundle("edtregdate", "javax.faces.component.UIInput.REQUIRED");
			return false;
		}
		
		if ((tbcase.getDiagnosisType() == DiagnosisType.CONFIRMED) && (tbcase.getDiagnosisDate() == null)) {
			facesMessages.addToControlFromResourceBundle("diagdateedt", "javax.faces.component.UIInput.REQUIRED");
			return false;
		}
		
		if ((regimenType == 1) && ((startTreatmentHome != null) && (startTreatmentHome.getRegimen() == null))) {
			facesMessages.addToControlFromResourceBundle("cbregimen", "javax.faces.component.UIInput.REQUIRED");
			return false;
		}
		
		//Checks if the treatment iniDate is before the diagnosis date
		//Workspace configuration allows it? 
		if (tbcase.getDiagnosisDate() != null) {
			//treatment has been defined?
			Date iniTreatmentDate = null;
			if(tbcase.getTreatmentPeriod() != null)
				iniTreatmentDate = tbcase.getTreatmentPeriod().getIniDate();
			else if (startTreatmentHome != null && startTreatmentHome.getIniTreatmentDate() != null)
				iniTreatmentDate = startTreatmentHome.getIniTreatmentDate();

            Workspace ws = UserSession.getWorkspace();

			//Validates if defined
			if ((!ws.isAllowDiagAfterTreatment()) && (iniTreatmentDate  != null) && (tbcase.getDiagnosisDate().after(iniTreatmentDate))) {
				facesMessages.addToControlFromResourceBundle("diagdateedt", "cases.treat.inidatemsg");
				return false;
			}

            if ((!ws.isAllowRegAfterDiagnosis()) && (tbcase.getRegistrationDate().after(tbcase.getDiagnosisDate()))) {
                facesMessages.addToControlFromResourceBundle("diagdateedt", "cases.details.valerror1");
            }
		}

        if(!tbcase.getPatientType().equals(PatientType.PREVIOUSLY_TREATED)){
            tbcase.setPreviouslyTreatedType(null);
        }
		
		return true;
	}
	
	/**
	 * Solve redirecting problem when the user changes diagnosis type but cancel the form
	 */
	public String cancel(){
		caseHome.setInstance(null);
		return "cancelcaseediting";
	}


	/**
	 * @return the currentAdminUnit
	 */
	public AdminUnitSelection getCurrentAdminUnit() {
		if (currentAdminUnit == null)
			currentAdminUnit = new AdminUnitSelection(false);
		return currentAdminUnit;
	}


	/**
	 * @return the notifAdminUnit
	 */
	public AdminUnitSelection getNotifAdminUnit() {
		if (notifAdminUnit == null)
			notifAdminUnit = new AdminUnitSelection(false);
		return notifAdminUnit;
	}



	public TbCase getTbcase() {
		return caseHome.getInstance();
	}


	
	public TBUnitSelection getTbunitselection() {
		if (tbunitselection == null) {
			tbunitselection = new TBUnitSelection("newuaid", true, TBUnitType.NOTIFICATION_UNITS);
		}
		return tbunitselection;
	}


	public int getRegimenType() {
		return regimenType;
	}


	public void setRegimenType(int regimenType) {
		this.regimenType = regimenType;
	}

	public PatientHome getPatientHome() {
		return patientHome;
	}

	public PrevTBTreatmentHome getPrevTBTreatmentHome() {
		return prevTBTreatmentHome;
	}

	public MedicalExaminationHome getMedicalExaminationHome() {
		return medicalExaminationHome;
	}

	/**
	 * @param tbunitselection the tbunitselection to set
	 */
	public void setTbunitselection(TBUnitSelection tbunitselection) {
		this.tbunitselection = tbunitselection;
	}

}
