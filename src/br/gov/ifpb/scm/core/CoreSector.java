package br.gov.ifpb.scm.core;

import br.gov.ifpb.scm.dao.DAO;

public class CoreSector {

	public static Character getSectorType(Integer idSector, DAO dao) {
		if(dao.isSectorTypeIn(idSector)) {
			return 'I';
		} else if(dao.isSectorTypeOut(idSector)) {
			return 'O';
		} else {
			return 'D';
		}
	}
}
