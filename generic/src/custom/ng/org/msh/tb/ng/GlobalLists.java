package org.msh.tb.ng;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.msh.tb.entities.enums.*;
import org.msh.tb.ng.entities.enums.Dot;
import org.msh.tb.ng.entities.enums.HealthFacility;
import org.msh.tb.ng.entities.enums.Qualification;
import org.msh.tb.ng.entities.enums.SuspectType;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;


@Name("globalLists_ng")
@BypassInterceptors
public class GlobalLists {

	private final static Nationality[] nationalityTypes = {
		Nationality.NIGERIA,
		Nationality.OTHER
	};	
	
	private final static HIVResult[] hivResultNgList = {
		HIVResult.NEGATIVE,
		HIVResult.POSITIVE,
		HIVResult.NOTDONE
	};	
	
	@Factory("nationalitiesNg")
	public Nationality[] getNationalitiesNg() {
		return nationalityTypes;
	}
	
	@Factory("hivResultNg")
	public HIVResult[] gethivResultNg() {
		return hivResultNgList;
	}
	
	private final static HIVResultNg[] hivResult = {
		HIVResultNg.POSITIVE,
		HIVResultNg.NEGATIVE,
		HIVResultNg.ONGOING,
		HIVResultNg.DECLINED
	};	
	
	private final static HIVResultNg[] partnerHIVResultNg = {
		HIVResultNg.NO_PARTNER,
		HIVResultNg.POSITIVE,
		HIVResultNg.NEGATIVE,
		HIVResultNg.ONGOING,
		HIVResultNg.DECLINED
	};		
	
	@Factory("partnerHIVResultNg")
	public HIVResultNg[] getPartnerHIVResult() {
		return partnerHIVResultNg;
	}	
	
	
	private final static ReferredBy[] refByTypes = {
		ReferredBy.PUBLIC,
		ReferredBy.PRIVATE_NON_PROFIT,
		ReferredBy.PRIVATE_PROFIT
	};
	
	private final static ReferredTo[] refToTypes = {
		ReferredTo.PUBLIC
	};
	
	
	private static final TbField tbFields[] = {
		TbField.COMORBIDITY,
		TbField.SIDEEFFECT,
		TbField.DST_METHOD,
		TbField.CULTURE_METHOD,
		TbField.CONTACTCONDUCT,
		TbField.CONTACTTYPE,
		TbField.XRAYPRESENTATION,
		TbField.PULMONARY_TYPES,
		TbField.EXTRAPULMONARY_TYPES,
		TbField.ART_REGIMEN,
		TbField.RISK_GROUP,
		TbField.ADJUSTMENT,
		TbField.SUSPECT_TYPE
	};
	
	private static final SuspectType suspectTypes[] = {
		SuspectType.SUSPECT_TYPE1
	};
	
	private static final DrugResistanceType drugResistanceType[] = {
		DrugResistanceType.MONO_RESISTANCE,
		DrugResistanceType.POLY_RESISTANCE,
		DrugResistanceType.MULTIDRUG_RESISTANCE,
		DrugResistanceType.EXTENSIVEDRUG_RESISTANCE,
	};
	
	private static final SampleType sampleType[] = {
		SampleType.SPUTUM,
		SampleType.PUS,
		SampleType.CSF,
		SampleType.URINE,
		SampleType.STOOL,
		SampleType.TISSUE,
	};

	
	private int[] comorb_years = {1,2,3,4,5,6,7,8,9,10};
	
	@Factory("tbFields.ng")
	public TbField[] getTbFields() {
		return tbFields;
	}
	
	public ReferredBy[] getRefByTypes() {
		return refByTypes;
	}
	
	public ReferredTo[] getRefToTypes() {
		return refToTypes;
	}
		
	@Factory("hivResults_ng")
	public HIVResultNg[] getHIVResults() {
		return hivResult;
	}	
	
	public DotBy[] getDotByTypes() {
		return DotBy.values();
	}		
	
	public Dot[] getDot() {
		return Dot.values();
	}	
	
	@Factory("qualification_ng")
	public Qualification[] getQualification() {
		return Qualification.values();
	}
	
	@Factory("healthfacility_ng")
	public HealthFacility[] gethealthFacility() {
		return HealthFacility.values();
	}

	public int[] getComorb_years() {
		return comorb_years;
	}
	
	public void setComorb_years(int[] comorb_years) {
		this.comorb_years = comorb_years;
	}

	public static SuspectType[] getSuspecttypes() {
		return suspectTypes;
	}

	@Factory("drugresisttypes_ng")
	public static DrugResistanceType[] getDrugresistancetype() {
		return drugResistanceType;
	}

	@Factory("sampletypes_ng")
	public static SampleType[] getSampletype() {
		return sampleType;
	}

	/**
	 * Return the list of years for the duration of the comorbidity
	 * @return list of {@link SelectItem} objects
	 */
	public List<SelectItem> getYears(){
		List<SelectItem> lst = new ArrayList<SelectItem>();
		String[] strYears = {"1","2","3","4","5","6","7","8","9","10",">10"};
		for (int i = 0; i< strYears.length; i++) {
			SelectItem item = new SelectItem();
			item.setLabel(strYears[i]);
			item.setValue(strYears[i]);
			lst.add(item);
		}
		return lst;
	}
	
}
