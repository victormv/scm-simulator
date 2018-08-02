package br.gov.ifpb.scm.core;

import java.util.List;

import br.gov.ifpb.scm.dao.DAO;
import br.gov.ifpb.scm.model.Proceeding;

public class CoreProceeding {
	
	public static Proceeding getAvaliableProceedingInSector(Integer idSector, List<Integer> productionLineStatus, DAO dao) {
		return dao.getAvaliableProceedingInSector(idSector, productionLineStatus);
	}
}
