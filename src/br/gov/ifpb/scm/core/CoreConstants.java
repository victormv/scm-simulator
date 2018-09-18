package br.gov.ifpb.scm.core;

public class CoreConstants {
	
	public final static boolean DB_WIPE = true;
	
	
	public final static int ORDER_SERVICES_LIMIT = 100;
	public final static int ORDER_SERVICES_CREATE_SLEEP_MS_MIN = 12000;
	public final static int ORDER_SERVICES_CREATE_SLEEP_MS_MAX = 16000;
	public final static int ORDER_SERVICES_OSP_MIN = 1;
	public final static int ORDER_SERVICES_OSP_MAX = 1;
	
	public final static int ORDER_SERVICES_STATUS_OPENNED = 1;
	public final static int ORDER_SERVICES_STATUS_PRODUCING = 2;
	public final static int ORDER_SERVICES_STATUS_PRODUCED = 3;
	
	
	public final static int ORDER_SERVICES_PRODUCT_MIN = 5;
	public final static int ORDER_SERVICES_PRODUCT_MAX = 10;
	
	
	public final static int SECTOR_CHECK_DEMAND_SLEEP_MS = 2000;
	
	
	public final static int PROCEEDINGS_STAGE_INIT = 1;
	
	
	public final static int PROCEEDINGS_SLEEP_MS_TASK_MIN = 2500;
	public final static int PROCEEDINGS_SLEEP_MS_TASK_MAX = 3500;
	
	
	public final static int PROCEEDINGS_SLEEP_MS_BETWEEN_DISPATCHED_ARRIVED_MIN = 1500;
	public final static int PROCEEDINGS_SLEEP_MS_BETWEEN_DISPATCHED_ARRIVED_MAX = 2000;
	
	
	public final static int PRODUCTION_LINE_STATUS_OPENNED = 1;
	public final static int PRODUCTION_LINE_STATUS_PRODUCING = 2;
	public final static int PRODUCTION_LINE_STATUS_PRODUCED = 3;
	public final static int PRODUCTION_LINE_STATUS_REWORK = 4;
	public final static int PRODUCTION_LINE_STATUS_DISCARDED = 5;
	
	public final static int PRODUCTION_LINE_REWORK_MAX = 3;
	public final static int PRODUCTION_LINE_REWORK_PROBABILITY = 75;
	
	
	public final static int USER_OPERATION_LEADER_ID = 1;
	public final static int USER_OPERATION_ID = 1;
	
	
	public final static boolean AREA_SPECIFIC = true;
	public final static int AREA_SPECIFIC_ID = 1;
}

