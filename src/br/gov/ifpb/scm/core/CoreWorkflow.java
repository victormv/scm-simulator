package br.gov.ifpb.scm.core;

import br.gov.ifpb.scm.dao.DAO;
import br.gov.ifpb.scm.model.Workflow;
import hibernate.util.JpaUtil;

public class CoreWorkflow {

	private DAO dao;
	private static CoreWorkflow instance;

	private CoreWorkflow() {
		
		this.dao = new DAO(JpaUtil.createEntityManager());
	}

	public static CoreWorkflow getInstance() {
		
		if(CoreWorkflow.instance == null) {
			CoreWorkflow.instance = new CoreWorkflow();
		} 
		return CoreWorkflow.instance;
	}

	public Workflow getNextWorkflow(Integer idSector, int actualStage, Integer idArea) {

		int newStage = actualStage+1;
		return this.dao.getWorkflow(idSector, newStage, idArea);
	}
	
	public Workflow getFirstWorkflowByProductAndArea(Integer idArea, Long idProduct) {
		
		return this.dao.getFirstWorkflowByProductAndArea(idArea);
	}
}
