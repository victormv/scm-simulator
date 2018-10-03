package br.gov.ifpb.scm.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import br.gov.ifpb.scm.core.CoreConstants;
import br.gov.ifpb.scm.model.OrderService;
import br.gov.ifpb.scm.model.OrderServiceProduct;
import br.gov.ifpb.scm.model.PlanningDailyTime;
import br.gov.ifpb.scm.model.PlanningWeekly;
import br.gov.ifpb.scm.model.Proceeding;
import br.gov.ifpb.scm.model.ProductionLine;
import br.gov.ifpb.scm.model.Sector;
import br.gov.ifpb.scm.model.User;
import br.gov.ifpb.scm.model.Workflow;
import br.gov.ifpb.scm.model.to.ProductionLineStage;
import br.gov.ifpb.scm.util.SequentialIncrement;

public class DAO {
	
	private EntityManager em;

	public DAO(EntityManager em) {
		this.em = em;
	}

	public int updateProductionLineStatus(Long idProductionLine, Integer idProductionLineStatus) {
		String sqlBase = "update dashboard.production_lines " +
				"set id_production_line_status = " + idProductionLineStatus + " ";
		String sqlWhere = "where id = " + idProductionLine;
		
		if(idProductionLineStatus == CoreConstants.PRODUCTION_LINE_STATUS_DISCARDED) {
			sqlBase += ", moment_ended = now() ";
		}
		
		this.em.getTransaction().begin();
		int res = this.em.createNativeQuery(sqlBase + sqlWhere).executeUpdate();
		this.em.getTransaction().commit();
		return res;
	}

	public void wipeDB() {
		this.em.getTransaction().begin();
		this.em.createNativeQuery("select * from dashboard.wipe_db();").getResultList();
		this.em.getTransaction().commit();
	}

	public <T> T getEntity(Long id, Class<T> eClass) {
		return this.em.find(eClass, id);
	}

	public <T> T getEntity(Integer id, Class<T> eClass) {
		return this.em.find(eClass, id);
	}

	public <T> T updateEntity(T entity) {
		this.em.getTransaction().begin();
		entity = this.em.merge(entity);
		this.em.getTransaction().commit();
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<Sector> getSectorsInWorkflow(Integer idArea) {
		String sql;
		if(idArea == null) {
			sql = "select * from dashboard.sectors where id IN (select id_sector_destination from dashboard.workflows)";
		} else {
			sql = "select * from dashboard.sectors where id IN (select id_sector_destination from dashboard.workflows) and id_area = " + idArea;
		}
		return (List<Sector>) this.em.createNativeQuery(sql, Sector.class).getResultList();
	}

	public int getAmountOrderService(Long idOrderService) {
		String sql = "select sum(amount) " +
				"from dashboard.orders_service_products " +
				"where id_order_service = " + idOrderService + ";";

		return ((BigInteger) this.em.createNativeQuery(sql).getSingleResult()).intValue();
	}

	public int getProductionLineProducedFromOrderService(Long idOrderService) {
		String sql = "select count(*) " +
				"from dashboard.production_lines " +
				"where id_order_service_product IN " +
				"(" +
				"select id " +
				"from dashboard.orders_service_products " +
				"where id_order_service = " + idOrderService + " " +
				") " +
				"and id_production_line_status = 3;";

		return ((BigInteger) this.em.createNativeQuery(sql).getSingleResult()).intValue();
	}
	
	public int getTimesOverRework(Long idProductionLine) {
		String sql = "select count(*) " +
				"from dashboard.proceedings " +
				"where id_production_line = " + idProductionLine + " " +
				"and id_sector_failure is not null";
				
		return ((BigInteger) this.em.createNativeQuery(sql).getSingleResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	public Proceeding getAvaliableProceedingInSector(Integer idSector, List<Integer> productionLineStatus) {
		String filtro = "";
		for(int i = 0; i < productionLineStatus.size(); i++) {
			filtro += productionLineStatus.get(i);
			if(i < (productionLineStatus.size()-1)) {
				filtro += ", ";
			}
		}

		String sql = "select p.* " +
				"from dashboard.proceedings p " +
				"join dashboard.production_lines pl on (pl.id = p.id_production_line) " +
				"where p.id_sector_destination = " + idSector + " " +
				"and p.moment_started is null " +
				"and p.moment_ended is null " +
				"and pl.id_production_line_status IN (" + filtro + ") " +
				"order by pl.id asc " +
				"limit 1";

		List<Proceeding> listResult = (List<Proceeding>) this.em.createNativeQuery(sql, Proceeding.class).getResultList();
		if(listResult != null && !listResult.isEmpty()) {
			return listResult.get(0);
		} else {
			return null;
		}
	}

	public int updateSectorFailureFromProceeding(int idSectorFailure, long idProceeding, int idSectorRework) {
		String sql = "update dashboard.proceedings " +
				"set id_sector_failure = " + idSectorFailure + " " +
				"where id = " + idProceeding + " " +
				"and id_sector_destination = " + idSectorRework;

		this.em.getTransaction().begin();
		int res = this.em.createNativeQuery(sql).executeUpdate();
		this.em.getTransaction().commit();
		return res;
	}

	public Proceeding persistProceeding(Long idProductionLine, Integer idSectorDestination, Integer idSectorOrigin, Integer stage, Integer idSectorFailure, Character type) {
		this.em.getTransaction().begin();

		Proceeding proc = new Proceeding();
		proc.setIdProductionLine(idProductionLine);
		proc.setIdSectorDestination(idSectorDestination);
		proc.setIdSectorFailure(idSectorFailure);
		proc.setIdSectorOrigin(idSectorOrigin);
		proc.setStage(stage);
		
		
		
		proc.setType(type);

		this.em.persist(proc);
		this.em.getTransaction().commit();

		return proc;
	}

	@SuppressWarnings("unchecked")
	public Workflow getFirstWorkflowByProductAndArea(Integer idArea) {
		String sql = "select * from dashboard.workflows " +
				"where id_area = " + idArea + " " +
				"and id_sector_origin is null " +
				"order by stage asc " +
				"limit 1";

		List<Workflow> listResult = (List<Workflow>) this.em.createNativeQuery(sql, Workflow.class).getResultList();
		if(listResult != null && !listResult.isEmpty()) {
			return listResult.get(0);
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Workflow getWorkflow(Integer idSector, int stage, Integer idArea) {
		String sql = "select * from dashboard.workflows " +
				"where id_area = " + idArea + " " +
				"and id_sector_origin = " + idSector + " " +
				"and stage = " + stage + " " +
				"limit 1";

		List<Workflow> listResult = (List<Workflow>) this.em.createNativeQuery(sql, Workflow.class).getResultList();
		if(listResult != null && !listResult.isEmpty()) {
			return listResult.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Workflow> getWorkflowsFromSector(Integer idSector, Integer idArea) {
		String sql = "select * " + 
				"from dashboard.workflows " +
				"where (id_sector_destination = " +  idSector + " " +
				"or id_sector_origin = " + idSector + ") " +
				"and id_area = " + idArea;

		return (List<Workflow>) this.em.createNativeQuery(sql, Workflow.class).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ProductionLineStage> getProductionLinesOpennedForSectorInitiator(Integer idSectorInitiator, List<Long> listIdProducts, Integer idProductionLineStatusOpenned) {
		String andSql = "";
		if(listIdProducts != null && !listIdProducts.isEmpty()) {
			andSql = "and osp.id_product IN (";
			for(int i = 0; i < listIdProducts.size(); i++) {
				andSql += listIdProducts.get(i);
				if(i < (listIdProducts.size()-1)) {
					andSql += ", ";
				}
			}
			andSql += ")";
		}

		String baseSql = "select pl.*, p.stage " +
				"from dashboard.production_lines pl " +
				"join dashboard.orders_service_products osp on (osp.id = pl.id_order_service_product) " +
				"join dashboard.proceedings p on (p.id_production_line = pl.id) ";

		String whereSql = "where pl.id_production_line_status = " + idProductionLineStatusOpenned + " " + 
				"and p.id_sector_destination = " + idSectorInitiator + " " +
				"and p.moment_started is null " +
				andSql;

		String orderSql = "order by pl.id asc limit 1";

		return (List<ProductionLineStage>) this.em.createNativeQuery(baseSql + " " + whereSql + " " + orderSql, ProductionLineStage.class).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ProductionLine> getProductionLinesOpennedForSector(Integer idSector, List<Long> listIdProducts, Integer idProductionLineStatusOpenned) {
		String andSql = "";
		if(listIdProducts != null && !listIdProducts.isEmpty()) {
			andSql = "and osp.id_product IN (";
			for(int i = 0; i < listIdProducts.size(); i++) {
				andSql += listIdProducts.get(i);
				if(i < (listIdProducts.size()-1)) {
					andSql += ", ";
				}
			}
			andSql += ")";
		}

		String baseSql = "select pl.* " + 
				"from dashboard.production_lines pl " +
				"join dashboard.orders_service_products osp on (osp.id = pl.id_order_service_product)";

		String whereSql = "where pl.id_production_line_status = " + idProductionLineStatusOpenned + " " + 
				andSql;

		String orderSql = "order by pl.id asc limit 1";

		return (List<ProductionLine>) this.em.createNativeQuery(baseSql + " " + whereSql + " " + orderSql, ProductionLine.class).getResultList();
	}

	public OrderService persistOrderService(String code) {	
		
		this.em.getTransaction().begin();
		OrderService os = new OrderService();
		os.setCode(code);
		os.setIdCustomer(this.getIdCustomerRandom());
		os.setIdOrderServiceStatus(CoreConstants.ORDER_SERVICES_STATUS_OPENNED);
		os.setIdUserCreated(CoreConstants.USER_OPERATION_LEADER_ID);

		this.em.persist(os);
		this.em.getTransaction().commit();
		return os;
	}

	public OrderServiceProduct persistOrderServiceProduct(OrderService os, int amount, int idArea, long idProduct) {
		this.em.getTransaction().begin();
		OrderServiceProduct osp = new OrderServiceProduct();
		osp.setAmount(amount);
		osp.setIdArea(idArea);
		osp.setIdOrderService(os.getId());

		osp.setIdProduct(idProduct);

		this.em.persist(osp);
		this.em.getTransaction().commit();
		return osp;
	}

	public ProductionLine persistProductionLine(Long idOrderServiceProduct, int idProductionLineStatus) {
		this.em.getTransaction().begin();
		ProductionLine pl = new ProductionLine();

		pl.setIdOrderServiceProduct(idOrderServiceProduct);
		pl.setIdProductionLineStatus(idProductionLineStatus);
		pl.setMomentStarted(null);
		pl.setMomentEnded(null);
		pl.setSequential(SequentialIncrement.getInstance().getSequential());

		this.em.persist(pl);
		this.em.getTransaction().commit();
		return pl;
	}

	@SuppressWarnings("unchecked")
	public long getLastIdOrderService() {
		boolean isCalled = false;
		long id = -1;

		List<Object[]> list = (List<Object[]>) this.em.createNativeQuery("select last_value, is_called from dashboard.seq_orders_service;").getResultList();

		for(Object[] obj : list) {
			isCalled = (boolean) obj[1];
			id = ((BigInteger) obj[0]).longValue();
		}

		if(isCalled) {
			return id+1;
		} else {
			return (long) 1;
		}
	}

	@SuppressWarnings("unchecked")
	public int getIdCustomerRandom() {
		List<BigInteger> list = (List<BigInteger>) this.em.createNativeQuery("select id from dashboard.customers order by random();").getResultList();
		for(BigInteger obj : list) {
			return obj.intValue();
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	public int getIdSectorFailureRandom(int idSector) {
		List<Integer> list = (List<Integer>) this.em.createNativeQuery("select id from dashboard.sectors_failures where id_sector = " + idSector + " order by random();").getResultList();
		for(Integer obj : list) {
			return obj.intValue();
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	public long getIdProductRandom(List<Long> listIdProductUsed){
		String baseSql = "select id from dashboard.products";
		String orderSql = "order by random()";
		String whereSql = "";

		if(listIdProductUsed != null && !listIdProductUsed.isEmpty()) {
			whereSql = "where id not in (";
			for(int i = 0; i < listIdProductUsed.size(); i++) {
				whereSql += listIdProductUsed.get(i);
				if(i < (listIdProductUsed.size()-1)) {
					whereSql += ", ";
				}
			}
			whereSql += ")";
		}

		List<BigInteger> list = (List<BigInteger>) this.em.createNativeQuery(baseSql + " " + whereSql + " " + orderSql).getResultList();

		for(BigInteger obj : list) {
			return obj.longValue();
		}
		return 0;
	}
	
	public boolean isSectorTypeIn(Integer idSector) {
		String sql = "SELECT " +
				  "count(*) as counter " +
				  "FROM " +
				  "dashboard.workflows " +
				  "WHERE " +
				  "stage IN ( " +
				  "(select min(stage) from dashboard.workflows) " +
				  ") " +
				  "AND " +
				  "id_sector_destination = " + idSector; 
				
		int count = ((BigInteger) this.em.createNativeQuery(sql).getSingleResult()).intValue();
		if(count > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isSectorTypeOut(Integer idSector) {
		String sql = "SELECT " +
				  "count(*) as counter " +
				  "FROM " +
				  "dashboard.workflows " +
				  "WHERE " +
				  "stage IN ( " +
				  "(select max(stage) from dashboard.workflows) " +
				  ") " +
				  "AND " +
				  "id_sector_destination = " + idSector; 
				
		int count = ((BigInteger) this.em.createNativeQuery(sql).getSingleResult()).intValue();
		if(count > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Workflow getLastWorkflow() {
		String sql = "select * from dashboard.workflows where stage = (select max(stage) from dashboard.workflows)";
		List<Workflow> list = (List<Workflow>) this.em.createNativeQuery(sql, Workflow.class).getResultList();
		return list.get(0);
	}
	
	public PlanningWeekly persistPlanningWeekly(Date dateWeekStart, Date dateWeekEnd) {
		this.em.getTransaction().begin();
		PlanningWeekly pw = new PlanningWeekly();

		pw.setDateWeekStart(dateWeekStart);
		pw.setDateWeekEnd(dateWeekEnd);
		pw.setIdUserCreated(CoreConstants.USER_OPERATION_ID);

		this.em.persist(pw);
		this.em.getTransaction().commit();
		return pw;
	}
	
	public PlanningDailyTime persistPlanningDailyTime(
		Long idPlanningWeekly,
		Long idOrderService,
		Date date,
		String timeBegin,
		String timeEnd,
		Integer amount) {
		this.em.getTransaction().begin();
		PlanningDailyTime pdt = new PlanningDailyTime();

		pdt.setIdPlanningWeekly(idPlanningWeekly);
		pdt.setIdOrderService(idOrderService);
		pdt.setDate(date);
		pdt.setTimeBegin(new Date());
		pdt.setTimeEnd(new Date());
		pdt.setAmount(amount);
		pdt.setIdUserCreated(CoreConstants.USER_OPERATION_ID);

		this.em.persist(pdt);
		this.em.getTransaction().commit();
		return pdt;
	}
	
	@SuppressWarnings("unchecked")
	public PlanningWeekly getActualPlanningWeekly(Date dataAtual) {
		String sql = 
			"SELECT * FROM dashboard.planning_weekly WHERE '" + dataAtual + "' BETWEEN date_week_start and date_week_end;";
		List<PlanningWeekly> list = 
			(List<PlanningWeekly>) this.em.createNativeQuery(sql, PlanningWeekly.class).getResultList();
		if(list == null || list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}
	
	public User persistUser(
			String name,
			String email,
			String password,
			boolean blocked,
			String matriculation,
			Long idAccessProfile) {
			this.em.getTransaction().begin();
			User user = new User();

			user.setName(name);
			user.setEmail(email);
			user.setPassword(password);
			user.setIdUserCreated(CoreConstants.USER_OPERATION_LEADER_ID);
			user.setIdArea(CoreConstants.AREA_SPECIFIC_ID);
			user.setBlocked(blocked);
			user.setMatriculation(matriculation);
			user.setIdAccessProfile(idAccessProfile);

			this.em.persist(user);
			this.em.getTransaction().commit();
			return user;
		}
}
