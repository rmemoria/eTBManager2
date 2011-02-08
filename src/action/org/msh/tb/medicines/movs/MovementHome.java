package org.msh.tb.medicines.movs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.international.Messages;
import org.msh.mdrtb.entities.Batch;
import org.msh.mdrtb.entities.BatchMovement;
import org.msh.mdrtb.entities.BatchQuantity;
import org.msh.mdrtb.entities.Medicine;
import org.msh.mdrtb.entities.Movement;
import org.msh.mdrtb.entities.Source;
import org.msh.mdrtb.entities.StockPosition;
import org.msh.mdrtb.entities.Tbunit;
import org.msh.mdrtb.entities.enums.MovementType;
import org.msh.utils.date.DateUtils;


@Name("movementHome")
public class MovementHome {

	@In(create=true)
	private EntityManager entityManager;
	
/*	private int qtd;
	private float totalPrice;
*/
	private List<PreparedMovement> preparedMovements;
	private List<Movement> movementsToBeRemoved;


	/**
	 * Initialize the class {@link MovementHome} to start recording batch of movement transactions
	 */
	public void initMovementRecording() {
		preparedMovements = null;
	}


	/**
	 * Prepare a new movement to be registered and include it in the list of prepared movements.
	 * When all movements are ready to be saved, the method savePreparedMovements() must be called to
	 * commit the movement recording.
	 * If the movement cannot be saved, the system will raise a {@link MovementException} exception
	 * @param date
	 * @param unit
	 * @param source
	 * @param medicine
	 * @param type
	 * @param batches
	 * @param comment
	 * @return an instance of {@link Movement} class, that will be saved when the method savePreparedMovements() is called.
	 */
	public Movement prepareNewMovement(Date date, Tbunit unit, Source source, Medicine medicine, MovementType type, Map<Batch, Integer> batches, String comment) {
		// check unit dates
		if (!unit.isMedicineManagementStarted())
			throw new MovementException("Error creating movement. Unit not in medicine management control");

		if (date.before(unit.getMedManStartDate())) {
			String s = Messages.instance().get("medicines.movs.datebefore");
			s = MessageFormat.format(s, unit.getMedManStartDate());
			throw new MovementException(s);
		}

		// create batches
		if ((batches == null) || (batches.size() == 0)) 
			throw new MovementException("No batches were specified for movement of " + medicine.toString());
		

		// initialize some variables
		date = DateUtils.getDatePart(date);
		int oper = type.getOper();
		float totalPrice = 0;
		int qtd = 0;
		for (Batch b: batches.keySet()) {
			int qtdaux = batches.get(b);
			qtd += qtdaux;
			totalPrice += b.getUnitPrice() * qtdaux;
			b = entityManager.merge(b);
		}
		
		Map<BatchQuantity, Integer> batchQuantities = prepareBatches(unit, source, batches, oper);

		// get stock position of the day
		StockPosition sp = getDayPosition(date, unit, source, medicine);

		// update stock position
		if ((sp != null) && (sp.getDate().equals(date))) {
			sp.setQuantity(sp.getQuantity() + (qtd * oper));
			sp.setTotalPrice(sp.getTotalPrice() + (totalPrice * oper));
		}
		else {
			StockPosition aux = new StockPosition();
			aux.setDate(date);
			aux.setMedicine(medicine);
			aux.setSource(source);
			aux.setTbunit(unit);
			aux.setQuantity(qtd);
			if (sp != null) {
				 aux.setQuantity(sp.getQuantity() + (qtd * oper));
				 aux.setTotalPrice(sp.getTotalPrice() + (totalPrice * oper));
			}
			else {
				aux.setQuantity(qtd * oper);
				aux.setTotalPrice(totalPrice * oper);
			}
			sp = aux;
		}

		if (sp.getQuantity() < 0)
			throw new MovementException(MessageFormat.format(Messages.instance().get("MovementException.NEGATIVE_STOCK"), sp.getMedicine().toString() + "[" + sp.getSource().toString() + "]"));
		
		PreparedMovement pm = new PreparedMovement();

		Movement mov = new Movement();
		mov.setDate(date);
		mov.setTbunit(unit);
		mov.setSource(source);
		mov.setMedicine(medicine);
		mov.setType(type);
		mov.setComment(comment);
		mov.setOper(oper);
		mov.setRecordDate(new Date());
		mov.setQuantity(qtd);
		mov.setUnitPrice(totalPrice / qtd);
		mov.setStockQuantity(sp.getQuantity());

		pm.setMovement(mov);
		pm.setBatchQuantities(batchQuantities);
		pm.setStockPosition(sp);
		
		if (preparedMovements == null)
			preparedMovements = new ArrayList<PreparedMovement>();
		preparedMovements.add(pm);

		return mov;
	}

	
	/**
	 * Prepare movements to be removed. The movement will just be removed when the method savePreparedMovements() is called. 
	 * The movement to be removed will be considered when preparing new movements.   
	 * @param mov instance of {@link Movement} to be removed from the system
	 */
	public void prepareMovementsToRemove(Movement mov) {
		if (movementsToBeRemoved == null)
			movementsToBeRemoved = new ArrayList<Movement>();

		movementsToBeRemoved.add(mov);
	}


	/**
	 * Save prepared movements in database and update stock position of medicines
	 */
	@Transactional
	public void savePreparedMovements() {
		if (preparedMovements == null)
			return;

		// create batch movements and save changes to the batch quantities of the unit
		for (PreparedMovement pm: preparedMovements) {
			Movement mov = pm.getMovement();
			for (BatchQuantity batchQuantity: pm.getBatchQuantities().keySet()) {
				Batch batch = batchQuantity.getBatch();
				int qtd = pm.getBatchQuantities().get(batchQuantity);
				
				BatchMovement bm = new BatchMovement();
				bm.setBatch(batch);
				bm.setQuantity(qtd);
				bm.setMovement(mov);
				mov.getBatches().add(bm);
				entityManager.persist( entityManager.merge( batchQuantity ));
			}

			entityManager.persist(pm.getStockPosition());
			entityManager.persist(mov);

			int qtd = mov.getQuantity() * mov.getOper();
			float totalPrice = mov.getTotalPrice() * mov.getOper();
			
			// update stock positions over the date of the movement
			entityManager.createQuery("update StockPosition sp " +
					"set sp.quantity = sp.quantity + :qtd, " +
					"sp.totalPrice = sp.totalPrice + :price " +
					"where sp.tbunit.id = :unitid and sp.source.id = :sourceid and sp.medicine.id = :medid " +
					"and sp.date > :dt")
					.setParameter("qtd", qtd)
					.setParameter("price", totalPrice)
					.setParameter("unitid", mov.getTbunit().getId())
					.setParameter("sourceid", mov.getSource().getId())
					.setParameter("medid", mov.getMedicine().getId())
					.setParameter("dt", mov.getDate())
					.executeUpdate();

			// update stock position in the movement records
			entityManager.createQuery("update Movement m set m.stockQuantity = m.stockQuantity + :qtd " + 
					"where (m.date > :data) " +
					"and m.medicine.id = :medid and m.source.id = :sourceid " +
					"and m.tbunit.id = :unitid")
					.setParameter("data", mov.getDate())
					.setParameter("medid", mov.getMedicine().getId())
					.setParameter("sourceid", mov.getSource().getId())
					.setParameter("unitid", mov.getTbunit().getId())
					.setParameter("qtd", qtd)
					.executeUpdate();
		}
		
		preparedMovements = null;
	}

	
	/**
	 * Prepare batches to be saved
	 * @param unit
	 * @param source
	 * @param batches
	 * @return
	 */
	protected Map<BatchQuantity, Integer> prepareBatches(Tbunit unit, Source source, Map<Batch, Integer> batches, int oper) {
		Map<BatchQuantity, Integer> lst = new HashMap<BatchQuantity, Integer>();

		for (Batch b: batches.keySet()) {
			Integer qtd = batches.get(b);
			if ((qtd != null) && (qtd > 0)) {
				BatchQuantity batchQuantity = loadBatchQuantity(b, unit, source);
				
				if (batchQuantity == null)
					throw new MovementException("No batch of " + b.getMedicine().toString() + 
							" found for " + unit.toString() + ", " + source.toString());

				int availableQtd = batchQuantity.getQuantity() + (qtd * oper);
				if (availableQtd < 0)
					throw new MovementException(MessageFormat.format(Messages.instance().get("MovementException.NEGATIVE_STOCK"), b.getMedicine().toString()));

				// update batch quantity
				batchQuantity.setQuantity( availableQtd );
				lst.put(batchQuantity, qtd);
			}
		}
		
		return lst;
	}
	

	/**
	 * Calculate the quantity of the medicine from the source x unit to be removed based on the 
	 * movements prepared to be removed.
	 * @param dt
	 * @param med
	 * @param source
	 * @param unit
	 * @return
	 */
	protected int calcQuantityToBeRemoved(Date dt, Medicine med, Source source, Tbunit unit) {
		if (movementsToBeRemoved == null)
			return 0;

		int qtd = 0;
		for (Movement mov: movementsToBeRemoved) {
			if ((!mov.getDate().after(dt)) &&
				(mov.getMedicine().equals(med)) &&
				(mov.getSource().equals(source)) &&
				(mov.getTbunit().equals(unit))) {
				qtd += mov.getQuantity();
			}
		}
		return qtd;
	}
	
	
	/**
	 * Load batch quantity information from a unit and source
	 * @param batch to load information
	 * @param unit to find batch information
	 * @param source to find batch information
	 * @return BatchQuantity instance
	 */
	protected BatchQuantity loadBatchQuantity(Batch batch, Tbunit unit, Source source) {
		// load information about batch quantity
		try {
			return (BatchQuantity)entityManager.createQuery("from BatchQuantity b where b.batch.id = :batchid " +
				"and b.tbunit.id = :unitid and b.source.id = :sourceid")
				.setParameter("batchid", batch.getId())
				.setParameter("unitid", unit.getId())
				.setParameter("sourceid", source.getId())
				.getSingleResult();
			
		} catch (NoResultException e) {
			BatchQuantity bq = new BatchQuantity();
			bq.setBatch(batch);
			bq.setTbunit(unit);
			bq.setSource(source);
			return bq;
		}
		
	}

	/**
	 * Remove a stock transaction updating the stock position
	 * @param mov
	 */
	@Transactional
	public void removeMovement(Movement mov) {
		int qtd = mov.getQtdOperation();
		
		entityManager.createQuery("update StockPosition sp set sp.quantity = sp.quantity - :qtd " +
				"where sp.date >= :data and sp.medicine.id = :medid and sp.source.id = :sourceid " +
				"and sp.tbunit.id = :unitid")
				.setParameter("data", mov.getDate())
				.setParameter("medid", mov.getMedicine().getId())
				.setParameter("sourceid", mov.getSource().getId())
				.setParameter("unitid", mov.getTbunit().getId())
				.setParameter("qtd", qtd)
				.executeUpdate();

		// update quantity recorded in movements
		entityManager.createQuery("update Movement m set m.stockQuantity = m.stockQuantity - :qtd " + 
				"where (m.date > :data or (m.date = :data and m.recordDate < :datarec)) " +
				"and m.medicine.id = :medid and m.source.id = :sourceid " +
				"and m.tbunit.id = :unitid")
				.setParameter("data", mov.getDate())
				.setParameter("datarec", mov.getRecordDate())
				.setParameter("medid", mov.getMedicine().getId())
				.setParameter("sourceid", mov.getSource().getId())
				.setParameter("unitid", mov.getTbunit().getId())
				.setParameter("qtd", qtd)
				.executeUpdate();
		
//		MovementType type = mov.getType();
		for (BatchMovement bm: mov.getBatches()) {
			Batch b = bm.getBatch();
			
			BatchQuantity bq = loadBatchQuantity(b, mov.getTbunit(), mov.getSource());
			
			bq.setQuantity(bq.getQuantity() - bm.getQtdOperation());
			
/*			// update reserved batch, in case of dispensing
			if (type == MovementType.DISPENSING) {
				int res = bq.getQuantityDispensing();
				if (res == 0)
					 res = bm.getQtdOperation();
				else res -= bm.getQtdOperation();
				bq.setQuantityDispensing(res);
			}
			
			if ((bq.getQuantity() < 0) || (bq.getQuantityDispensing() < 0))
*/
			if (bq.getQuantity() < 0)
				throw new MovementException("The batch quantity cannot be negative");
			
			if (bq.getQuantity() == 0)
				 entityManager.remove(bq);
			else entityManager.persist(bq);
		}

		entityManager.remove(mov);
		entityManager.flush();
	}
	
	/**
	 * Retorna a posi��o de estoque em uma determinada data
	 * @param dt
	 * @param tbunit
	 * @param source
	 * @param prod
	 * @return
	 */
	protected StockPosition getDayPosition(Date dt, Tbunit tbunit, Source source, Medicine prod) {
		List<StockPosition> lst = entityManager.createQuery("from StockPosition sp where sp.date = " +
				"(select max(sp2.date) from StockPosition sp2 where sp2.tbunit.id = sp.tbunit.id " +
				"and sp2.date <= :data " + 
				"and sp2.source.id = sp.source.id and sp2.medicine.id = sp.medicine.id) " +
				"and sp.tbunit.id = :unitid and sp.source.id = :sourceid and sp.medicine.id = :prodid")
				.setParameter("data", dt)
				.setParameter("prodid", prod.getId())
				.setParameter("sourceid", source.getId())
				.setParameter("unitid", tbunit.getId())
				.getResultList();

		if (lst.size() == 0)
		 	 return null;
		else return lst.get(0);
	}



	
	/**
	 * Store information about a prepared movement
	 * @author Ricardo Memoria
	 *
	 */
	private class PreparedMovement {
		private Movement movement;
		private StockPosition stockPosition;
		private Map<BatchQuantity, Integer> batchQuantities;
		/**
		 * @return the movement
		 */
		public Movement getMovement() {
			return movement;
		}
		/**
		 * @param movement the movement to set
		 */
		public void setMovement(Movement movement) {
			this.movement = movement;
		}
		/**
		 * @return the stockPosition
		 */
		public StockPosition getStockPosition() {
			return stockPosition;
		}
		/**
		 * @param stockPosition the stockPosition to set
		 */
		public void setStockPosition(StockPosition stockPosition) {
			this.stockPosition = stockPosition;
		}
		/**
		 * @return the batchQuantities
		 */
		public Map<BatchQuantity, Integer> getBatchQuantities() {
			return batchQuantities;
		}
		/**
		 * @param batchQuantities the batchQuantities to set
		 */
		public void setBatchQuantities(Map<BatchQuantity, Integer> batchQuantities) {
			this.batchQuantities = batchQuantities;
		}
	}
}
