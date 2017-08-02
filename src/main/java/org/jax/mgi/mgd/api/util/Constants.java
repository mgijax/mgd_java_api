package org.jax.mgi.mgd.api.util;

public class Constants {
	// logical databases
	public static int LDB_MGI = 1;			// MGI
	public static int LDB_PUBMED = 29;		// PubMed
	public static int LDB_DOI = 65;			// DOI / Journal Link
	public static int LDB_GOREF = 185;		// GO_Ref
	
	// MGI types
	public static int TYPE_REFERENCE = 1;	// reference
	public static int TYPE_MARKER = 2;		// marker
	
	// flags
	public static int PREFERRED = 1;
	public static int SECONDARY = 0;
	public static int PRIVATE = 1;
	public static int PUBLIC = 0;
	
	// reference workflow group abbreviations
	public static String WG_GO = "GO";
	public static String WG_GXD = "GXD";
	public static String WG_AP = "AP";
	public static String WG_TUMOR = "Tumor";
	public static String WG_QTL = "QTL";
	
	public static String[] WG_ALL = { WG_AP, WG_GO, WG_GXD, WG_TUMOR, WG_QTL };
	
	// reference workflow statuses
	public static String WS_CHOSEN = "Chosen";
	public static String WS_CURATED = "Fully curated";
	public static String WS_INDEXED = "Indexed";
	public static String WS_NOT_ROUTED = "Not Routed";
	public static String WS_REJECTED = "Rejected";
	public static String WS_ROUTED = "Routed";
	
	// HTTP status codes
	public static Integer HTTP_OK = 200;
	public static Integer HTTP_BAD_REQUEST = 400;
	public static Integer HTTP_NOT_FOUND = 404;
	public static Integer HTTP_SERVER_ERROR = 500;
	public static Integer HTTP_PERMISSION_DENIED = 550;
	
	// vocabulary keys
	public static Integer VOC_WORKFLOW_GROUP = 127;
	public static Integer VOC_WORKFLOW_STATUS = 128;
	public static Integer VOC_WORKFLOW_TAGS = 129;
	
	// accession ID prefixes
	public static String PREFIX_JNUM = "J:";
}
