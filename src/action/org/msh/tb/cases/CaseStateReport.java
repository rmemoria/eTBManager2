package org.msh.tb.cases;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.international.Messages;
import org.msh.mdrtb.entities.UserWorkspace;
import org.msh.mdrtb.entities.Workspace;
import org.msh.mdrtb.entities.enums.CaseClassification;
import org.msh.mdrtb.entities.enums.CaseState;
import org.msh.mdrtb.entities.enums.UserView;
import org.msh.mdrtb.entities.enums.ValidationState;
import org.msh.tb.login.UserSession;

/**
 * Generate report by case state to be displayed in the main page
 * @author Ricardo Memoria
 *
 */
@Name("caseStateReport")
public class CaseStateReport {

	@In EntityManager entityManager;
	@In Workspace defaultWorkspace;
	@In(create=true) UserSession userSession;
	@In(create=true) UserWorkspace userWorkspace;
	
	
	private List<Item> items;
	private List<ValidationItem> validationItems;
	private Item total;
	private Map<String, String> messages;


	/**
	 * Return list of consolidated values by case state
	 * @return
	 */
	public List<Item> getItems() {
		if (items == null)
			createItems();
		return items;
	}


	/**
	 * Create items of the report
	 */
	public void createItems() {
		messages = Messages.instance();

		items = new ArrayList<Item>();
		validationItems = new ArrayList<ValidationItem>();
		
		String aucond;
		if (userWorkspace.getView() == UserView.ADMINUNIT)
			 aucond = "inner join AdministrativeUnit a on a.id = u.adminunit_id ";
		else aucond = "";

		String cond = generateSQLConditionByUserView();

		String condByCase = generateSQLConditionByCase();

		Integer hsID = null;
		if (userWorkspace.getHealthSystem() != null)
			hsID = userWorkspace.getHealthSystem().getId();
		
		String sql = "select c.state, c.validationState, count(*) " +
				"from TbCase c " +
				"inner join Tbunit u on u.id = c.notification_unit_id " + aucond +
				"where c.state not in (" + CaseState.ONTREATMENT.ordinal() + ',' + CaseState.TRANSFERRING.ordinal() + ") " +
				(hsID != null? "and u.healthSystem_id = " + hsID.toString(): "") +
				" and u.workspace_id = " + defaultWorkspace.getId() + cond + condByCase +
				" group by c.state, c.validationState " +
				"union " +
				"select c.state, c.validationState, count(*) " +
				"from TbCase c " +
				"inner join TreatmentHealthUnit h on h.case_id = c.id " +
				"inner join Tbunit u on u.id = h.unit_id " + aucond + 
				"where c.state in (" + CaseState.ONTREATMENT.ordinal() + ',' + CaseState.TRANSFERRING.ordinal() + ") " + 
				" and u.workspace_id = " + defaultWorkspace.getId() + " and h.enddate = c.endtreatmentdate " + cond + condByCase +
				(hsID != null? " and u.healthSystem_id = " + hsID.toString(): "") +
				" group by c.state, c.validationState";
		List<Object[]> lst = entityManager.createNativeQuery(sql).getResultList();
		
		total = new Item();
		total.setDescription(messages.get("global.total"));
		
		for (Object[] val: lst) {
			int qty = ((BigInteger)val[2]).intValue();
			Item item = findItem(CaseState.values()[(Integer)val[0]]);
			item.add(qty);
			total.add(qty);
			
			ValidationState vs = ValidationState.values()[(Integer)val[1]];
			if (!ValidationState.VALIDATED.equals(vs)) {
				ValidationItem valItem = findValidationItem(vs);
				valItem.addCases(qty);
			}
		}
		
		Collections.sort(items, new Comparator<Item>() {

			public int compare(Item o1, Item o2) {
				CaseState cs1 = o1.getState();
				CaseState cs2 = o2.getState();
				if (cs1 == null)
					return 1;
				if (cs2 == null)
					return -1;
				
				if (cs1.ordinal() > cs2.ordinal())
					return 1;
				if (cs1.ordinal() < cs2.ordinal())
					return -1;
				return 0;
			}
			
		});
	}


	/**
	 * Generate SQL condition to filter cases by user view
	 * @return
	 */
	protected String generateSQLConditionByUserView() {
		switch (userWorkspace.getView()) {
		case ADMINUNIT: 
			return " and (a.code like '" + userWorkspace.getAdminUnit().getCode() + "%')"; 
		case TBUNIT: 
			return " and u.id = " + userWorkspace.getTbunit().getId();
		default: return "";
		}
	}
	
	/**
	 * Generate SQL condition to filter cases
	 * @return SQL condition to be used in a where clause
	 */
	protected String generateSQLConditionByCase() {
		boolean tbcases = userSession.isCanOpenTBCases();
		boolean mdrcases = userSession.isCanOpenMDRTBCases();
		
		if (tbcases && mdrcases) {
			return "";
		}
		
		String caseCondition = "";
		
		if (tbcases)
			 caseCondition = " and (c.classification = " + CaseClassification.TB_DOCUMENTED.ordinal() + ")";
		else
		if (mdrcases)
			caseCondition = " and (c.classification = " + CaseClassification.MDRTB_DOCUMENTED.ordinal() + ")";
		
		return caseCondition;
	}


	/**
	 * Return an item from the validation list from its validation state
	 * @param state
	 * @return Instance of {@link ValidationState}
	 */
	private ValidationItem findValidationItem(ValidationState state) {
		for (ValidationItem item: validationItems) {
			if (item.getValidationState().equals(state)) {
				return item;
			}
		}
		
		ValidationItem item = new ValidationItem();
		item.setValidationState(state);
		validationItems.add(item);
		return item;
	}


	/**
	 * Search for a specific item based on the state
	 * @param state
	 * @return
	 */
	private Item findItem(CaseState state) {
		if (state.ordinal() >= CaseState.CURED.ordinal())
			state = null;

		for (Item item: items) {
			if (item.getState() == state)
				return item;
		}
		
		Item item = new Item();
		item.setState(state);
		
		if (state == null) {
			item.setDescription(messages.get("cases.closed"));
			item.setStateIndex(100);
		}
		else {
			item.setDescription(messages.get(state.getKey()));
			item.setStateIndex(state.ordinal());
		}

		items.add(item);
		
		return item;
	}

	/**
	 * @return the total
	 */
	public Item getTotal() {
		if (total == null)
			createItems();
		return total;
	}

	
	/**
	 * Return list of items to be Validated
	 * @return List of {@link ValidationItem} instances
	 */
	public List<ValidationItem> getValidationItems() {
		if (validationItems == null)
			createItems();
		return validationItems;
	}
	

	/**
	 * Store consolidated information about cases under validation
	 * @author Ricardo Memoria
	 *
	 */
	public class ValidationItem {
		private ValidationState validationState;
		private long cases;

		public ValidationState getValidationState() {
			return validationState;
		}
		public void setValidationState(ValidationState validationState) {
			this.validationState = validationState;
		}
		public long getCases() {
			return cases;
		}
		public void setCases(long cases) {
			this.cases = cases;
		}
		public void addCases(long num) {
			cases += num;
		}
	}


	/**
	 * Store consolidated information about a case state
	 * @author Ricardo Memoria
	 *
	 */
	public class Item {
		private String description;
		private CaseState state;
		private int cases;
		private int stateIndex;

		public void add(int val) {
			cases += val;
		}
		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}
		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
		}
		/**
		 * @return the state
		 */
		public CaseState getState() {
			return state;
		}
		/**
		 * @param state the state to set
		 */
		public void setState(CaseState state) {
			this.state = state;
		}
		/**
		 * @return the cases
		 */
		public int getCases() {
			return cases;
		}
		/**
		 * @param cases the cases to set
		 */
		public void setCases(int cases) {
			this.cases = cases;
		}
		/**
		 * @return the stateIndex
		 */
		public int getStateIndex() {
			return stateIndex;
		}
		/**
		 * @param stateIndex the stateIndex to set
		 */
		public void setStateIndex(int stateIndex) {
			this.stateIndex = stateIndex;
		}
	}
}
