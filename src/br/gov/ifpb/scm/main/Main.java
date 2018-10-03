package br.gov.ifpb.scm.main;

import java.util.List;

import br.gov.ifpb.scm.core.CoreConstants;
import br.gov.ifpb.scm.core.threads.OperatorLeaderThread;
import br.gov.ifpb.scm.core.threads.OperatorSectorThread;
import br.gov.ifpb.scm.dao.DAO;
import br.gov.ifpb.scm.model.Sector;
import hibernate.util.JpaUtil;

public class Main {
	
	public static void main(String[] args) {
		
		DAO dao = new DAO(JpaUtil.createEntityManager());
		
		if(CoreConstants.DB_WIPE) {
			dao.wipeDB();
			System.out.println("[LOG] Limpando banco de dados...");
		}
		
		// Cria as threads responsaveis pelo papel do lider de produção
		OperatorLeaderThread operatoLeaderCreator = new OperatorLeaderThread();
		Thread operatoLeaderCreatorThread = new Thread(operatoLeaderCreator);
		
		operatoLeaderCreatorThread.start();
		
		List<Sector> listSectors;
		if(CoreConstants.AREA_SPECIFIC) {
			listSectors = dao.getSectorsInWorkflow(CoreConstants.AREA_SPECIFIC_ID);
		} else {
			listSectors = dao.getSectorsInWorkflow(null);
		}
		
		OperatorSectorThread operatorSector;
		Thread operatorSectorThread;
		for(Sector sector : listSectors) {
			operatorSector = new OperatorSectorThread(sector);
			operatorSectorThread = new Thread(operatorSector);
			operatorSectorThread.start();
		}
	}
}
