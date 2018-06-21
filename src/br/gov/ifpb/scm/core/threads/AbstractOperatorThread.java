package br.gov.ifpb.scm.core.threads;

import br.gov.ifpb.scm.dao.DAO;
import hibernate.util.JpaUtil;

public abstract class AbstractOperatorThread implements Runnable 
{
	protected DAO dao;
	
	public AbstractOperatorThread()
	{
		this.dao = new DAO(JpaUtil.createEntityManager());
	}
	
	public void out(String msg)
	{
		System.out.println("[" + this.getClass().getSimpleName() + "] " + msg);
	}

	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}
}
