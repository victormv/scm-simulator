package br.gov.ifpb.scm.main;

import br.gov.ifpb.scm.dao.DAO;
import hibernate.util.JpaUtil;

public class Teste 
{
	public static void main(String[] args)
	{
		DAO dao = new DAO(JpaUtil.createEntityManager());
		
		System.out.println(dao.getIdSectorFailureRandom(2));
	}
}
