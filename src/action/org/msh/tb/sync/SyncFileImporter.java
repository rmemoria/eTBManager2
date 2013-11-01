/**
 * 
 */
package org.msh.tb.sync;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.persistence.EntityManager;

import org.msh.tb.application.App;
import org.msh.tb.application.TransactionManager;
import org.msh.tb.entities.CaseDispensing;
import org.msh.tb.entities.Patient;
import org.msh.tb.entities.TbCase;
import org.msh.tb.entities.WSObject;
import org.msh.tb.entities.WeeklyFrequency;
import org.msh.tb.entities.Workspace;
import org.msh.tb.login.UserSession;
import org.msh.utils.DataStreamUtils;

import com.rmemoria.datastream.DataConverter;
import com.rmemoria.datastream.DataInterceptor;
import com.rmemoria.datastream.DataUnmarshaller;
import com.rmemoria.datastream.ObjectConsumer;
import com.rmemoria.datastream.StreamContext;


/**
 * @author Ricardo Memoria
 *
 */
public class SyncFileImporter {

	private static final int BUFFER_SIZE = 65535;
	
	FileInputStream fstream;
	private Workspace workspace;
	private TransactionManager transaction;
	private String errorMessage;
	// store in-memory list of entity keys (client ID and server ID)
	private EntityKeyList entityKeys;
	// list of last versions in use by the client (will be used to generate response file)
	private List<EntityLastVersion> entityVersions;


	/**
	 * Data interceptor to load entities
	 */
	private DataInterceptor interceptor = new DataInterceptor() {
		@Override
		public Object newObject(Class objectType, Map<String, Object> params) {
			if (params != null)
				 return createNewObject(objectType, params);
			else return null;
		}
		
		@Override
		public Class getObjectClass(Object obj) {
			return null;
		}
	};
	
	
	/**
	 * Start reading sync file sent from the client
	 * @param file instance of {@link File} that contains the server content
	 */
	public void start(File file) {
		// get the workspace in use
		workspace = UserSession.getWorkspace();
		
		// list of keys for the entity
		entityKeys = new EntityKeyList();

		// create list that will receive the last version of entities in use in the client
		entityVersions = new ArrayList<EntityLastVersion>();
		
		try {
			File destfile = File.createTempFile("temp", "etbm.tmp");
			uncompressFile(file, destfile);
			fstream = new FileInputStream(destfile);
			try {
				importData(fstream);
			}
			finally {
				fstream.close();
				destfile.delete();
			}
			
		} catch (Throwable e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
			throw new RuntimeException(e);
		}
	}

	
	/**
	 * Uncompress a file compressed with {@link GZIPInputStream}
	 * @param gzipfile instance of {@link File} containing the compressed file
	 * @param destfile instance of {@link File} where uncompressed file will be written to
	 */
	protected void uncompressFile(File gzipfile, File destfile) {
		try {
			if (destfile.exists())
				destfile.delete();
			
			byte[] buffer = new byte[BUFFER_SIZE];

			GZIPInputStream gzin = new GZIPInputStream(new FileInputStream(gzipfile));
			FileOutputStream out = new FileOutputStream(destfile);

			int noRead;
			while ((noRead = gzin.read(buffer)) != -1) {
			        out.write(buffer, 0, noRead);
			}
			gzin.close();
			out.close();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * Read the input stream provided and initialize the records in the database
	 * @param in instance of {@link InputStream}
	 */
	private void importData(InputStream in) {
		StreamContext context = DataStreamUtils.createContext("clientinifile-schema.xml");

		// add the interceptor
		context.addInterceptor(interceptor);

		// add converter to WeeklyFrequency object
		context.setConverter(WeeklyFrequency.class, new DataConverter() {
			@Override
			public String convertToString(Object obj) {
				return Integer.toString( ((WeeklyFrequency)obj).getValue() );
			}
			
			@Override
			public Object convertFromString(String s, Class clazz) {
				WeeklyFrequency wf = new WeeklyFrequency();
				wf.setValue( Integer.parseInt(s) );
				return wf;
			}
		});
		
		DataUnmarshaller um = DataStreamUtils.createXMLUnmarshaller(context);

		try {
			// start reading the objects
			um.unmarshall(in, new ObjectConsumer() {
				@Override
				public void onNewObject(Object obj) {
					handleNewObject(obj);
					
					// check if there is an on-going transaction
					if (getTransaction().isActive())
						getTransaction().commit();
				}

				@Override
				public void startObjectReading(Class objectClass) {
					if (objectClass != ServerSignature.class)
						getTransaction().begin();
				}
			});
			
		} finally {
			// if it's active that's because there was an error
			if (getTransaction().isActive())
				getTransaction().rollback();
		}
	}

	/**
	 * Create a new object based on its class and parameters from the XML data.
	 * This method is called by the data stream when it's necessary to restore an
	 * object by the parameters available
	 * @param objectType is the class of the object
	 * @param params is the list of parameters available in the XML
	 * @return instance of the object class
	 */
	protected Object createNewObject(Class objectType, Map<String, Object> params) {
		// is information about the last version used in each entity?
		if (objectType == EntityLastVersion.class) {
			// let the library create the instance
			return null;
		}

		Integer clientId = (Integer)params.get("clientId");
		Integer id = (Integer)params.get("id");

		// if there is a client ID, so the object is to be sync
		if (clientId != null) {
			// register the keys of the object
			entityKeys.registerEntityKey(objectType, clientId, id);
		}

		if (id != null)
			 return App.getEntityManager().find(objectType, id);
		else return null;
	}
	
	/**
	 * Called when a new object is just read from the input stream
	 * @param obj is the object read from the data stream
	 */
	protected void handleNewObject(Object obj) {
		saveEntity(obj);
	}


	/**
	 * Save entity. The method is under a transaction, so it's safe to persist and continue
	 * @param obj
	 */
	protected void saveEntity(Object obj) {
		if (obj instanceof EntityLastVersion) {
			entityVersions.add((EntityLastVersion)obj);
		}
		
		EntityManager em = App.getEntityManager();

		if (obj instanceof WSObject) {
			workspace = em.merge(workspace); 
			((WSObject)obj).setWorkspace(workspace);
		}

		// handle exceptions in the UserWorkspace class
		if (obj instanceof TbCase) {
			TbCase tbcase = (TbCase)obj;
			// save the patient
			Patient p = tbcase.getPatient();
			workspace = em.merge(workspace);
			p.setWorkspace(workspace);
			em.persist(p);
		}
		else
		if (obj instanceof CaseDispensing) {
			CaseDispensing cd = (CaseDispensing)obj;
			if (cd.getDispensingDays() != null)
				em.persist(cd.getDispensingDays());
		}
		
		em.persist(obj);
		em.flush();
		em.clear();
	}

	/**
	 * The component responsible for managing the database transaction
	 * @return instance of {@link TransactionManager}
	 */
	protected TransactionManager getTransaction() {
		if (transaction == null)
			transaction = TransactionManager.instance();
		return transaction;
	}


	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	
	
	/**
	 * Create the instance of the {@link EntityHomeEx} for the given object
	 * that will allow the object to be validated and persisted
	 * @param obj entity instance
	 * @return instance of {@link EntityHomeEx}
	 */
/*	protected EntityHomeEx createEntityHome(Object obj) {
		Class clazz = getHomeComponentClass(obj);
		EntityHomeEx home = (EntityHomeEx)Component.getInstance(clazz);
		if (home == null)
			throw new RuntimeException("No home class defined for entity class " + obj.getClass());

		return home;
	}


	*//**
	 * Return component name of the home class for entity object
	 * @param obj
	 * @return
	 *//*
	protected Class getHomeComponentClass(Object obj) {
		if (obj instanceof TbCase)
			return CaseHome.class;

		if (obj instanceof Patient)
			return PatientHome.class;

		if (obj instanceof ExamMicroscopy)
			return ExamMicroscopyHome.class;

		if (obj instanceof ExamCulture)
			return ExamCultureHome.class;

		if (obj instanceof ExamXRay)
			return ExamXRayHome.class;

		if (obj instanceof ExamDST)
			return ExamDSTHome.class;

		if
	}
*/}