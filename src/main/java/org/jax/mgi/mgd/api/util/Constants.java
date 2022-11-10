package org.jax.mgi.mgd.api.util;

public class Constants {

	// HTTP status codes
	public static Integer HTTP_OK = 200;
	public static Integer HTTP_BAD_REQUEST = 400;
	public static Integer HTTP_NOT_FOUND = 404;
	public static Integer HTTP_SERVER_ERROR = 500;
	public static Integer HTTP_PERMISSION_DENIED = 550;

	// voc_term where _vocab_key = 53
	// qualifier 'Generic Annotation Qualifier', value = null	
	public static String VOC_GENERIC_ANNOTATION_QUALIFIER = "1614158";
	
	// default database user, if no other is specified
	public static String DEFAULT_USER = "mgd_dbo";
	
	// process status
	public static String PROCESS_CREATE = "c";
	public static String PROCESS_UPDATE = "u";
	public static String PROCESS_DELETE = "d";
	public static String PROCESS_SPLIT = "s";
	public static String PROCESS_NOTDIRTY = "x";

	// default search limit
	public static String SEARCH_RETURN_LIMIT = "limit 1000";
	public static String SEARCH_RETURN_LIMIT5000 = "limit 5000";

	// API logging using Logger.info()
	public static String LOG_MGI_API = "LOG_MGI_API|";
	public static String LOG_FAIL_USERAUTHENTICATION = LOG_MGI_API + "FAIL|USER AUTHENTICATION";
    public static String LOG_IN_JSON = LOG_MGI_API + "IN JSON FROM UI";
    public static String LOG_OUT_DOMAIN = LOG_MGI_API + "OUT RESULTS FROM ENTITY/DOMAIN";
    public static String LOG_IN_PKEY = LOG_MGI_API + "IN PRIMARY KEY";
	public static String LOG_FAIL_JSON = LOG_MGI_API + "FAIL|JSON FROM UI|INCORRECT FORMAT";
	public static String LOG_FAIL_DOMAIN = LOG_MGI_API + "FAIL|RESULTS FROM ENTITY/DOMAIN";
	public static String LOG_FAIL_PKEY = LOG_MGI_API + "FAIL|PRIMARY KEY";
	public static String LOG_FAIL_USERLOGIN = LOG_MGI_API + "FAIL|USER LOGIN|";
	public static String LOG_SUCCESS_USERLOGIN = LOG_MGI_API + "SUCCESSFUL|USER LOGIN|";
	public static String LOG_NOT_IMPLEMENTED = LOG_MGI_API + "ENDPOINT NOT IMPLEMENTED";
	public static String LOG_INPROGRESS_EIUTILITIES = LOG_MGI_API + "IN PROGRESS|EI UTILITIES|";
	public static String LOG_SUCCESS_EIUTILITIES = LOG_MGI_API + "SUCCESSFUL|EI UTILITIES|";
	public static String LOG_FAIL_EIUTILITIES = LOG_MGI_API + "FAIL|EI UTILITIES|";
	public static String LOG_FAIL_IMAGESUBMISSION = LOG_MGI_API + "FAIL|IMAGE SUBMISSION|";
	
}
