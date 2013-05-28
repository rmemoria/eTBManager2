package org.msh.tb.az.eidss.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jboss.seam.Component;
import org.msh.tb.az.eidss.CaseImporting;
import org.msh.tb.az.eidss.CaseInfo;
import org.msh.tb.az.entities.TbCaseAZ;
import org.msh.tb.entities.Patient;
import org.msh.tb.entities.TbCase;
import org.msh.tb.entities.Workspace;
import org.msh.tb.entities.enums.CaseClassification;
import org.msh.tb.entities.enums.CaseState;
import org.msh.tb.entities.enums.DiagnosisType;
import org.msh.tb.entities.enums.Gender;
import org.msh.tb.entities.enums.ValidationState;

/**
 * @author ����
 *
 */
public class EidssImportTask {
	private EntityManager entityManager;
	//ArrayOfHumanCaseListInfo arrayCaseList;
	//private static EidssServiceSoap soap;
	//private List<HumanCaseInfo> caseInfoList;
	List<String> cases;
	List<CaseInfo> InfoForExport=new ArrayList<CaseInfo>();
	CaseImporting ci;
	
	public void execute() {
		 final EntityManager entityManager;
		String legacy="HAZBA00A120F4W";
		String firstname="test";
		String lastname="test";
		String  middlename="test";
		Calendar regDate=Calendar.getInstance();
		Date rd=XMLToDate(regDate,20, "10042003");
		entityManager = (EntityManager)Component.getInstance("entityManager");
		//String s=getWorkspace().getId().toString();
		String s="8";
		try {
			
			List<TbCase> lstcases = entityManager.createQuery(" from TbCase c " +
					"join fetch c.patient p " 
					+" where c.legacyId = :id "
					+ " and c.registrationDate= :rd "
					+" and p.lastName = :ln " 
					//+" and p.PATIENT.name = 'hertherht' " 
					+" and p.middleName = :mn " 
				//+ "and p.workspace.id = " + s)
					+" and p.workspace.id = 8" )
					.setParameter("id", legacy)
					.setParameter("ln", lastname)
					//.setParameter("fn", firstname)
					.setParameter("mn", middlename)
					.setParameter("rd", rd)
					.getResultList();

			if (lstcases.size() != 0) {
				System.out.println("find "+legacy);
			}
			else{
				System.out.println("empty ");
			}
		} catch (Exception e) {
		
		}
		
		//test();	
	
	}
	private void testImport(String s) {
		String[] sa = s.split(";");
		test(sa);
		
	}
	private Date XMLToDate(Calendar regDate,int age, String ageType){
		String Days = "10042001";
		String Month = "10042002";
		String Years = "10042003";

		if (ageType.equalsIgnoreCase(Years)){
			regDate.add(Calendar.YEAR,-age);

		}
		if (ageType.equalsIgnoreCase(Month)){
			regDate.add(Calendar.MONTH,-age);
		}
		if (ageType.equalsIgnoreCase(Month)){
			regDate.add(Calendar.DAY_OF_MONTH,-age);
		}
	
		return regDate.getTime();

	}
	/*
	public void execute1() {	
		Date d=new Date();
		d.setDate(5);
		d.setMonth(0);
		
		 ci=new CaseImporting();
		soap=loginEIDSS("Republican APS", "Administrator", "EIDSS", "en");
		caseInfoList=new 	ArrayList<HumanCaseInfo>();
		arrayCaseList=getHumanCaseList(d,"786360000000");
		cases=ci.CheckIfExistInEtb(ConvertString(arrayCaseList));
		caseInfoList=GetCaseInfoList(soap,cases,"Alive","Refused");
		InfoForExport=AddAll(caseInfoList);
		ci.importRecords(InfoForExport);
	}
	private List<CaseInfo> AddAll(List<HumanCaseInfo> caseInfoList) {
		List<CaseInfo> fe= new ArrayList<CaseInfo>();
		Iterator<HumanCaseInfo> it=caseInfoList.iterator();
		while (it.hasNext()){
			CaseInfo	onecase=convertRecord(it.next());
			fe.add(onecase);
			
		}
		return fe;
	}
	public  EidssServiceSoap loginEIDSS(String server, String login, String password,String language)
	{

		EidssService serv = new EidssService();

		EidssServiceSoap soap = serv.getEidssServiceSoap();

		Map<String, Object> context = ((BindingProvider)soap).getRequestContext();
		context.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);
		soap.login(server, login, password, language);


		return soap;
	}


	private CaseInfo convertRecord(HumanCaseInfo EIDSSData) {
		Days = 10042001,
Month = 10042002,
Years = 10042003
		CaseInfo onecase=new CaseInfo();
	
		String firstName = EIDSSData.getFirstName();
		String lastName = EIDSSData.getLastName();
		String fatherName = EIDSSData.getMiddleName();
		onecase.setLastName(lastName);
		onecase.setFirstName(firstName);
		onecase.setMiddleName(fatherName);
		Long gender = EIDSSData.getPatientGender().getId();
		onecase.setDateOfBirth(ConvertToDate(EIDSSData.getDateOfBirth()));
		AddressInfo addr=EIDSSData.getCurrentResidence();
		onecase.setFinalDiagnosisDate(ConvertToDate(EIDSSData.getNotificationDate()));
		String addInfo=addr.getCountry().getName()+"<br/>"+addr.getRegion().getName()+"<br/>"+addr.getSettlement().getName()+"<br/>"+addr.getRayon().getName()+"<br/>"+	addr.getStreet();
		onecase.setAdditionalComment(addInfo);	
		onecase.setCaseID(EIDSSData.getCaseID().toString());	
	
		return onecase;
	}
	 private List<String> ConvertString(ArrayOfHumanCaseListInfo arrayCaseList){
		 List <String> ids=new ArrayList<String>();
		 return ids;
	 }
	/*
	 * getHumanCaseList from EIDSS
	 * ID - tub code in EIDSS 
	 */
	/*
	private static ArrayOfHumanCaseListInfo getHumanCaseList(Date complDate, String id) {
		ArrayOfHumanCaseListInfo cases=null;
		ObjectFactory obj = new ObjectFactory();
		HumanCaseListInfo filter = obj.createHumanCaseListInfo();
		BaseReferenceItem diag = new BaseReferenceItem();
		diag.setId(new Long(id)); // Tuberculosis
		XMLGregorianCalendar dtXML = null;
		try {
			dtXML = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		dtXML.setDay(complDate.getDate());
		dtXML.setMonth(complDate.getMonth()+1);
		dtXML.setYear(complDate.getYear()+1900);

		dtXML.setTime(0, 0, 0);
		filter.setEnteredDate(dtXML);
		filter.setDiagnosis(diag);
		 cases = soap.getHumanCaseList(filter);
		return cases;
	}
	//TODO  ���� ��� �������� ������ �� ������ � ��������� ������� ��
	public  List<HumanCaseInfo> GetCaseInfoList(EidssServiceSoap soap, List<String> cases,  String patStateName, String caseStateName){
		Integer count=0;
		Iterator <String> it=cases.iterator();
		while (it.hasNext()){
			String item = it.next();
			HumanCaseInfo CI=soap.getHumanCase( item);
			if (suitable(CI, patStateName, caseStateName)){
				caseInfoList.add(CI); }
		}

	count=count+1;

	return caseInfoList;
	}
	
	
	
	
	
*/
	private void test(String[] all){
		entityManager = (EntityManager)Component.getInstance("entityManager");
		Integer i = Integer.valueOf(all[4]);
		Date d=new Date();
		d.setYear(d.getYear()-i.intValue());
		Integer iday=0;
		Integer imonth=0;
		Integer iyear=0;
		String dtArr = all[5];
		String[] rdate;
		if (!dtArr.equalsIgnoreCase("")){
			rdate=dtArr.split("-");
			iday = Integer.valueOf(rdate[0]);
			d.setDate(iday.intValue());
			imonth = Integer.valueOf(rdate[1]);
			d.setDate(imonth.intValue()-1);
			iyear=Integer.valueOf(rdate[2]);
		}
		Patient p = new Patient();
		p.setBirthDate(d);
		p.setLastName(all[1]);
		p.setName(all[2]);
		if(!all[3].equalsIgnoreCase("null"))p.setMiddleName(all[3]);
		p.setWorkspace(getWorkspace());
		if(all[7].equalsIgnoreCase("10043001")){
			p.setGender(Gender.FEMALE);
		}else{
		p.setGender(Gender.MALE);
		}
		TbCaseAZ tbcase = new TbCaseAZ();
		tbcase.setClassification(CaseClassification.TB);
		tbcase.setLegacyId(all[0]);
		String comment=all[6].replace("null", "");
		tbcase.setEIDSSComment(comment);	
		Date idate=new Date();
		if (iyear!=0){
			idate.setYear(iyear.intValue()-1900);
			idate.setDate(iday.intValue());
			idate.setMonth(imonth.intValue());

		}
		tbcase.setDiagnosisDate(idate); //TODO change
		tbcase.setRegistrationDate(idate);
		tbcase.setDiagnosisType(DiagnosisType.CONFIRMED); //TODO change
		tbcase.setValidationState(ValidationState.WAITING_VALIDATION); //TODO check IT
		tbcase.setPatient(p);
		tbcase.setAge(1);
		tbcase.setState(CaseState.WAITING_TREATMENT);
		tbcase.setNotifAddressChanged(true);
		
		entityManager.persist(p);
		entityManager.persist(tbcase);
		entityManager.flush();
		

	}
	private Workspace getWorkspace() {
		return (Workspace)entityManager.find(Workspace.class, 8);
	}
	
	private void ReadFromFile(){
		FileInputStream fstream;
		InputStreamReader ow;
		try {
			fstream = new FileInputStream("c:\\temp\\out.txt");

			try {
				ow = new InputStreamReader(fstream, "UTF-8");
				BufferedReader in = new BufferedReader(ow);
				try {
					String s = in.readLine();
					while (s!= null){
						testImport(s);
						s = in.readLine();
					}
					ow.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Date CalcDateOfBirth(XMLGregorianCalendar regDate1,int age, String ageType){
		String Days = "10042001";
		String Month = "10042002";
		String Years = "10042003";
		Calendar regDate=regDate1.toGregorianCalendar();
		if (ageType.equalsIgnoreCase(Years)){
			regDate.set(Calendar.YEAR,regDate.get(Calendar.YEAR)-age);

		}
		if (ageType.equalsIgnoreCase(Month)){
			regDate.set(Calendar.MONTH,regDate.get(Calendar.MONTH)-age);
		}
		if (ageType.equalsIgnoreCase(Month)){
			regDate.set(Calendar.DAY_OF_MONTH,regDate.get(Calendar.DAY_OF_MONTH)-age);
		}
	
		return regDate.getTime();
	}
}
