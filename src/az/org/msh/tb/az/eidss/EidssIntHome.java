package org.msh.tb.az.eidss;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.msh.tb.application.tasks.AsyncTask;
import org.msh.tb.application.tasks.TaskManager;
import org.msh.tb.az.eidss.test.EidssImportTask;
import org.msh.tb.entities.SystemParam;
import org.msh.tb.entities.UserLogin;
import org.msh.tb.entities.Workspace;

/**
 * This class intended to configure and run export data from the EIDSS.
 * Manually by sysadmin or by timer event.
 * @author alexey
 *
 */
@Name("eidssIntHome")
@Scope(ScopeType.CONVERSATION)
public class EidssIntHome {
	@In EntityManager entityManager;
	@In TaskManager taskManager;
	@In(required=false) Workspace defaultWorkspace;
	@In(create=true) FacesMessages facesMessages;
	/**
	 * import launch configuration
	 */
	private EidssIntConfig config=null;
	/**
	 * Prefix for parameters keys
	 */
	private final String prefix = "admin.eidss";
	private UserLogin userLogin;
	private Workspace workspace;

	/**
	 * Show any message to UI
	 * @param message Any message
	 * @return 
	 */
	public void showMessage(String message) {
		//TODO show messages for background execution
		facesMessages.addFromResourceBundle(message);
	}


	/**
	 * Initialize GUI to make changes in the configuration
	 */
	@Begin(join=true)
	public void initConfigEditing() {
		getConfig();
	}

	/**
	 * Execute import asynchronously by user
	 * @return Success if import ran, or Error otherwise
	 */
	public String execute1(){
		saveConfig();
		loadConfig();
		setCurrentUser();
		setDefaultWorkspace();
		TaskManager manager = (TaskManager)Component.getInstance(TaskManager.class);
		if (manager != null){
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("config", getConfig());
			params.put("userId", userLogin.getId());
			AsyncTask task = manager.findTaskByClass(EidssTaskImport.class);
			if (task == null){
				manager.runTask(EidssTaskImport.class, params);
				showMessage("eidss.import.started");
			}else{
				showMessage("eidss.import.starting");
			}

		}
		return "Success";
	}

	/**
	 * Execute import asynchronously
	 * @return Success if import ran, or Error otherwise
	 */
	public String execute(){
		EidssImportTask et=new EidssImportTask();
		et.execute();
		return "Success";
	}
	/**
	 * Stop current async task
	 */
	public void stop(){
		EidssTaskImport task = (EidssTaskImport) taskManager.findTaskByClass(EidssTaskImport.class);
		if (task != null){
			task.cancel();
			showMessage("eidss.import.canceled");
		}else{
			showMessage("eidss.import.idle");
		}
	}
	/**
	 * check state of the current async task
	 */
	public void check(){
		AsyncTask task = taskManager.findTaskByClass(EidssTaskImport.class);
		if (task != null){
			showMessage(EidssTaskImport.getStateMessage());
		}else{
			showMessage("eidss.import.idle");
		}
	}

	/**
	 * Check if integration is running
	 * @return true, if export task running
	 */
	public boolean isTaskRunning() {
		//TODO body
		return true;
	}

	/**
	 * Automatically execute by timer event
	 * @return 
	 */
	@Observer("system-timer-event")
	public void systemTimerListener() {
		System.out.println("Executing automatic EIDSS integration...");
		execute();
	}
	/**
	 * get configuration, load from database if empty
	 * @return
	 */
	public EidssIntConfig getConfig(){
		if (config == null){
			loadConfig();
		}
		return config;
	}
	/**
	 * load configuration from the SysConfig table
	 * use reflection to determine keys name and fixed prefix for these keys
	 */
	private void loadConfig() {
		config = new EidssIntConfig();
		Field[] flds = config.getClass().getFields();
		for(Field fld : flds){
			loadParameter(fld,config);
		}

	}
	/**
	 * save configuration to the SysConfig table
	 * use reflection to determine keys name and fixed prefix for these keys
	 */
	public void saveConfig() {
		if (config != null){
			Field[] flds = config.getClass().getFields();
			for(Field fld : flds){
				try {
					if (fld.getType().getName().contains("Date")){
						Date dt = (Date) fld.get(config);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						saveParameter(prefix + "." + fld.getName(),sdf.format(dt));
					}else
						saveParameter(prefix + "." + fld.getName(),fld.get(config).toString());
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

	}
	/**
	 * Load particular parameter from SysConfig
	 * @param name name of parameter
	 * @param config object to place parameter
	 */
	private void loadParameter(Field fld, EidssIntConfig config) {
		String s = null;
		try {
			SystemParam sysparam = (SystemParam)entityManager
			.createQuery("from SystemParam sp where sp.workspace.id = :id and sp.key = :param")
			.setParameter("id", defaultWorkspace.getId())
			.setParameter("param", prefix+"."+fld.getName())
			.getSingleResult();
			s = sysparam.getValue();
		} catch (Exception e) {
			s = null;
		}
		if (s != null) {
			setValue(fld, s, config);
		}

	}
	/**
	 * set parameter value. If impossible - do nothing i.e. assign default value
	 * @param fld class field to set value
	 * @param value field value as string
	 * @param config EIDSS config
	 * Only String, Date and Boolean allowed
	 */
	private void setValue(Field fld, String value, EidssIntConfig config) {
		Object thisValue = null;
		try {
			if (fld.getType().getName().contains("Date")){
				java.sql.Date dt = java.sql.Date.valueOf(value);
				thisValue = new java.util.Date(dt.getTime());
			}else if (fld.getType().getName().contains("Boolean")){
				thisValue = Boolean.valueOf(value);
			} else{
				thisValue = value;
			}
			fld.set(config, thisValue);
		} catch (IllegalArgumentException e) {
			// do nothing
		} catch (IllegalAccessException e) {
			// do nothing
		}

	}

	/**
	 * Save configuration parameter to the common config table
	 * @param key
	 * @param value
	 */
	protected void saveParameter(String key, String value) {
		SystemParam p;
		try {
			p = entityManager.find(SystemParam.class, key);
		} catch (Exception e) {
			p = null;
		}

		if (p == null) {
			p = new SystemParam();
			p.setKey(key);
			//TODO ?
			defaultWorkspace = entityManager.merge(defaultWorkspace);
			p.setWorkspace(defaultWorkspace);
		}
		p.setValue(value.toString());
		entityManager.persist(p);
	}

	/**
	 * Notify user about current operation.
	 * @return notify message
	 * @return 
	 */
	public String notifyUser() {
		throw new UnsupportedOperationException();
	}



	/**
	 * 
	 * @return 
	 */
	public void setDefaultWorkspace() {
		workspace = getEntityManager().find(Workspace.class, getUserLogin().getDefaultWorkspace().getId());
	}

	public UserLogin getUserLogin() {
		return userLogin;
	}


	public void setUserLogin(UserLogin userLogin) {
		this.userLogin = userLogin;
	}


	public EntityManager getEntityManager() {
		return entityManager;
	}


	/**
	 * 
	 * @return 
	 */
	public void setCurrentUser() {
		if (userLogin == null)
			userLogin = (UserLogin)Component.getInstance("userLogin");
		//TODO resolve for background task
	}


}
