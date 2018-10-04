package br.gov.ifpb.scm.core.threads;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.gov.ifpb.scm.core.CoreConstants;
import br.gov.ifpb.scm.core.CoreSector;
import br.gov.ifpb.scm.dao.DAO;
import br.gov.ifpb.scm.model.Sector;
import br.gov.ifpb.scm.model.User;
import br.gov.ifpb.scm.model.to.ListPlanningIsCurrentDateResponseData;
import br.gov.ifpb.scm.model.to.ListProductionLineProducingResponseData;
import br.gov.ifpb.scm.model.to.LoginResponseData;
import br.gov.ifpb.scm.model.to.ResponseData;
import br.gov.ifpb.scm.requests.LoginRequest;
import br.gov.ifpb.scm.requests.OrderProductRequest;
import br.gov.ifpb.scm.requests.PocketRequest;
import br.gov.ifpb.scm.util.SequentialIncrement;
import br.gov.ifpb.scm.util.Util;
import hibernate.util.CryptographyUtil;
import hibernate.util.JpaUtil;

public class OperatorSectorThread extends AbstractOperatorThread {

	private DAO dao;
	private Sector sector;
	private List<Integer> listProductionLineStatus;
	private Character sectorType;
	private User user;
	private LoginResponseData login;


	public OperatorSectorThread(Sector sector) {

		this.sector = sector;

		this.dao = new DAO(JpaUtil.createEntityManager());

		this.sectorType = CoreSector.getSectorType(this.sector.getId(), dao);

		String password = CryptographyUtil.encrypt("12345");
		String email = this.sector.getAcronym() + "@gmail.com";

		this.user = this.dao.persistUser(
				this.sector.getName(),
				email,
				CryptographyUtil.encrypt(password),
				false,
				this.sector.getId().toString(),
				CoreConstants.ID_ACCESS_PROFILE_DEFAULT
				);

		Map<String,Object> loginParams = new LinkedHashMap<String,Object>();
		loginParams.put("email", email);
		loginParams.put("encryptedPassword", password);

		this.login = LoginRequest.loginPost(loginParams);
		if(!this.login.isSuccess()) {
			System.err.println("[ERROR] Ocorreu um erro no processo de login do usuario " + this.user.getName());
			System.err.println("[ERROR] " + this.login.getMessage());
			System.exit(1);
			return;
		}

		Map<String,Object> checkInParams = new LinkedHashMap<>();
		checkInParams.put("matriculation", this.user.getMatriculation());
		checkInParams.put("id_sector", this.sector.getId());

		ResponseData checkInResponseData = PocketRequest.checkInPost(checkInParams, this.login.getToken());
		if(!checkInResponseData.isSuccess()) {
			System.err.println("[ERROR] Ocorreu um erro no processo de checkin no setor " + this.sector.getAcronym() + ", usuario " + this.user.getName());
			System.err.println("[ERROR] " + this.login.getMessage());
			System.exit(1);
			return;
		}

		this.listProductionLineStatus = new ArrayList<Integer>();
		this.listProductionLineStatus.add(CoreConstants.PRODUCTION_LINE_STATUS_PRODUCING);
		this.listProductionLineStatus.add(CoreConstants.PRODUCTION_LINE_STATUS_OPENNED);
	}

	@Override
	public void run() {

		System.out.println("SECTOR [" + this.sector.getId() + "] working...");

		while(true) {

			Long idOp = null;
			String sequential = null;
			// SETOR DE INSPECAO
			if (this.sectorType == 'I') {
				ListPlanningIsCurrentDateResponseData listOpsInSector = 
						OrderProductRequest.listPlanningOpInCurrentDateGet(this.login.getToken());
				System.out.println("[LOG] Buscando OP(I) no setor " + this.sector.getAcronym());
				System.out.println("OP ACHOU: " + listOpsInSector.getIdOp());
				if(listOpsInSector != null && listOpsInSector.getIdOp() != null) {
					idOp = listOpsInSector.getIdOp();
					sequential = String.valueOf(SequentialIncrement.getInstance().getSequential());
				}
				System.out.println("SEQ ACHOU: " + sequential);
			} else {
				Map<String,Object> listParams = new LinkedHashMap<>();
				listParams.put("userId", this.user.getId());
				listParams.put("sectorId", this.sector.getId());
				ListProductionLineProducingResponseData listOpsProducing =
					OrderProductRequest.listProductionLineProducingGet(this.login.getToken(), listParams);
				System.out.println("[LOG] Buscando OP no setor " + this.sector.getAcronym());
				System.out.println("OP ACHOU: " + listOpsProducing.getIdOp());
				System.out.println("SEQ ACHOU: " + listOpsProducing.getSequential());
				if(listOpsProducing != null && 
					listOpsProducing.isSuccess() &&
					listOpsProducing.getIdOp() != null &&
					listOpsProducing.getSequential() != null) {
					idOp = listOpsProducing.getIdOp();
					sequential = listOpsProducing.getSequential();
				}
			}

			if(sequential == null || idOp == null) {
				try {
					Thread.sleep(Util.getRandom(CoreConstants.PROCEEDINGS_SLEEP_MS_TASK_MIN, CoreConstants.PROCEEDINGS_SLEEP_MS_TASK_MAX));
				} catch(Exception e) {

				}
				continue;
			}

			Map<String,Object> startTaskParams = new LinkedHashMap<>();
			startTaskParams.put("idOp", idOp);
			startTaskParams.put("idUser", this.user.getId());
			startTaskParams.put("sequential", sequential);

			ResponseData startTaskResponseData =
					PocketRequest.startTaskPost(startTaskParams, this.login.getToken());
			System.out.println("[LOG] Iniciando tarefa no setor " + this.sector.getAcronym() + "[" + idOp + "; " + sequential + "]");
			if(!startTaskResponseData.isSuccess()) {
				System.err.println("[ERROR] Não foi possível iniciar a tarefa no setor " + this.sector.getAcronym() + ", usuario " + this.user.getMatriculation());
				System.err.println("[ERROR] " + startTaskResponseData.getMessage());
				try {
					Thread.sleep(Util.getRandom(CoreConstants.PROCEEDINGS_SLEEP_MS_TASK_MIN, CoreConstants.PROCEEDINGS_SLEEP_MS_TASK_MAX));
				} catch(Exception e) {

				}
				continue;
			}

			try {
				Thread.sleep(Util.getRandom(CoreConstants.PROCEEDINGS_SLEEP_MS_TASK_MIN, CoreConstants.PROCEEDINGS_SLEEP_MS_TASK_MAX));
			} catch(Exception e) {

			}
			
			if (this.sectorType == 'D') {

				Map<String,Object> endTaskParams = new LinkedHashMap<>();
				endTaskParams.put("id_sector", this.sector.getId());
				endTaskParams.put("idUser", this.user.getId());
				endTaskParams.put("sequential", sequential);
				endTaskParams.put("id_sector_failure", "");
	
				ResponseData endTaskResponseData =
						PocketRequest.endTaskPost(endTaskParams, this.login.getToken());
				System.out.println("[LOG] Finalizando tarefa no setor " + this.sector.getAcronym() + "[" + idOp + "; " + sequential + "]");
				if(!endTaskResponseData.isSuccess()) {
					System.err.println("[ERROR] Não foi possível finalizar a tarefa no setor " + this.sector.getAcronym() + ", usuario " + this.user.getMatriculation());
					System.err.println("[ERROR] " + endTaskResponseData.getMessage());
					try {
						Thread.sleep(Util.getRandom(CoreConstants.PROCEEDINGS_SLEEP_MS_TASK_MIN, CoreConstants.PROCEEDINGS_SLEEP_MS_TASK_MAX));
					} catch(Exception e) {

					}
					continue;
				}
			}

			// SETOR DE INSPECAO
			//if(this.sectorType == 'D') {
				//				// REWORK
				//				if(CoreRework.checkRework()) {
				//					CoreRework.processRework(proceeding, productionLine, orderServiceProduct, this.dao);
				//					continue;
				//				}
				//
				//				// DELAY
				//				try {
				//					Thread.sleep(Util.getRandom(CoreConstants.PROCEEDINGS_SLEEP_MS_TASK_MIN, CoreConstants.PROCEEDINGS_SLEEP_MS_TASK_MAX));
				//				} catch(Exception e) {
				//					
				//				}
			//}


			//			// END TASK
			//			this.endSectorTask(proceeding, productionLine, orderServiceProduct);
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}