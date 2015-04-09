package org.msh.tb;

import org.hibernate.exception.SQLGrammarException;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.TransactionPropagationType;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.transaction.Transaction;
import org.jboss.seam.transaction.UserTransaction;
import org.msh.etbm.transactionlog.mapping.LogInfo;
import org.msh.tb.entities.Tag;
import org.msh.utils.EntityQuery;

import javax.persistence.PersistenceException;

@Name("tagHome")
@LogInfo(roleName="TAGS", entityClass=Tag.class)
public class TagHome extends EntityHomeEx<Tag> {
	private static final long serialVersionUID = 2346498717179017533L;

	private boolean autogenerated;

    private String sqlError;

    @Override
    @Transactional(TransactionPropagationType.NEVER)
	public String persist() {
		Tag tag = getInstance();
		if ((autogenerated) && (!tag.isAutogenerated())) {
			FacesMessages.instance().addToControlFromResourceBundle("edtsql", "javax.faces.component.UIInput.REQUIRED");
			return "error";
		}
		
		if (!tag.isAutogenerated())
			tag.setSqlCondition(null); 
		else {
			// test tag condition
			if (!testTagCondition()) {
				FacesMessages.instance().addToControlFromResourceBundle("edtsql", "admin.tags.conderror");
				return "error";
			}
		}

        try {
            UserTransaction transaction = Transaction.instance();
            transaction.begin();

            String ret = super.persist();
            if ((ret.equals("persisted")) && (tag.isAutogenerated())) {
                TagsCasesHome tagsCasesHome = (TagsCasesHome)Component.getInstance("tagsCasesHome", true);
                if (!tagsCasesHome.updateCases(tag))
                    return "error";
            }

            transaction.commit();

            return ret;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}

	
	/**
	 * Check if the SQL condition given to the tag is correct
	 * @return
	 */
	public boolean testTagCondition() {
        try {
            UserTransaction transaction = null;
            try {
                transaction = Transaction.instance();
                if (!transaction.isActive()) {
                    transaction.begin();
                }

                String sql = "select count(*) from tbcase a inner join patient p on p.id=a.patient_id where " + getInstance().getSqlCondition();
                getEntityManager().createNativeQuery(sql).getSingleResult();
                transaction.commit();
                return true;
            } catch (PersistenceException e) {

                if (e.getCause() instanceof SQLGrammarException) {
                    SQLGrammarException sqlerror = (SQLGrammarException)e.getCause();
                    sqlError = sqlerror.getSQLException().getMessage();
                }
                if (transaction != null) {
                    transaction.rollback();
                }
                return false;
            }

        } catch (Exception e) {
            return false;
        }
	}


	@Override
	public EntityQuery<Tag> getEntityQuery() {
		return (TagsQuery)Component.getInstance("tagsQuery", false);
	}


	/**
	 * @return the autogenerated
	 */
	public boolean isAutogenerated() {
		return getInstance().isAutogenerated();
	}

	/**
	 * @param autogenerated the autogenerated to set
	 */
	public void setAutogenerated(boolean autogenerated) {
		this.autogenerated = autogenerated;
	}

    public String getSqlError() {
        return sqlError;
    }
}
