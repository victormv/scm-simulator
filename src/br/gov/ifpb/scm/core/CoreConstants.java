package br.gov.ifpb.scm.core;

public class CoreConstants {
	
	public final static boolean DB_WIPE = true;
	
	public final static String URL_API = "http://localhost:3004/api/v1/";
	
	
	public final static int ORDER_SERVICES_LIMIT = 50000;
	public final static int ORDER_SERVICES_CREATE_SLEEP_MS_MIN = 300000;
	public final static int ORDER_SERVICES_CREATE_SLEEP_MS_MAX = 350000;
	
	public final static int ORDER_SERVICES_STATUS_OPENNED = 1;
	public final static int ORDER_SERVICES_STATUS_PRODUCING = 2;
	public final static int ORDER_SERVICES_STATUS_PRODUCED = 3;
	
	
	public final static int ORDER_SERVICES_PRODUCT_MIN = 5;
	public final static int ORDER_SERVICES_PRODUCT_MAX = 10;
	
	
	public final static int SECTOR_CHECK_DEMAND_SLEEP_MS = 5000;
	
	
	public final static int PROCEEDINGS_SLEEP_MS_TASK_MIN = 5500;
	public final static int PROCEEDINGS_SLEEP_MS_TASK_MAX = 8500;
	
	
	public final static int PRODUCTION_LINE_STATUS_OPENNED = 1;
	public final static int PRODUCTION_LINE_STATUS_PRODUCING = 2;
	public final static int PRODUCTION_LINE_STATUS_PRODUCED = 3;
	public final static int PRODUCTION_LINE_STATUS_REWORK = 4;
	public final static int PRODUCTION_LINE_STATUS_DISCARDED = 5;
	
	public final static int PRODUCTION_LINE_REWORK_MAX = 3;
	public final static int PRODUCTION_LINE_REWORK_PROBABILITY = 30;
	
	
	public final static int USER_OPERATION_LEADER_ID = 1;
	public final static int USER_OPERATION_ID = 1;
	
	
	public final static boolean AREA_SPECIFIC = true;
	public final static int AREA_SPECIFIC_ID = 1;
	
	public final static long ID_ACCESS_PROFILE_DEFAULT = 1;
}

