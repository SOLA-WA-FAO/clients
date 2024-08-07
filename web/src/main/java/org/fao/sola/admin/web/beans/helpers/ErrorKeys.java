package org.fao.sola.admin.web.beans.helpers;

/**
 * Holds list of error keys, used to extract messages from errors bundle
 */
public class ErrorKeys {

// General errors
    /**
     * Form Submission was not successful. Please review and correct listed
     * errors:
     */
    public static final String GENERAL_ERROR_LIST_HEADER = "GENERAL_ERROR_LIST_HEADER";

    /**
     * Unexpected errors have occured while executing requested action :
     */
    public static final String GENERAL_UNEXPECTED_ERROR = "GENERAL_UNEXPECTED_ERROR";

    /** Failed to redirect */
    public static final String GENERAL_REDIRECT_FAILED = "GENERAL_REDIRECT_FAILED";
    
// Login erros
    /**
     * Provide user name
     */
    public static final String LOGIN_USERNAME_REQUIRED = "LOGIN_USERNAME_REQUIRED";

    /**
     * Provide password
     */
    public static final String LOGIN_PASSWORD_REQUIRED = "LOGIN_PASSWORD_REQUIRED";

    /**
     * Login failed
     */
    public static final String LOGIN_FAILED = "LOGIN_FAILED";

    /**
     * Logout failed
     */
    public static final String LOGIN_LOGOUT_FAILED = "LOGIN_LOGOUT_FAILED";

    /**
     * Your account is not active. If you just registered, you need to activate
     * it first.
     */
    public static final String LOGIN_ACCOUNT_BLOCKED = "LOGIN_ACCOUNT_BLOCKED";
    /**
     * You don't have administration access rights
     */
    public static final String LOGIN_NO_ADMIN_RIGHTS = "LOGIN_NO_ADMIN_RIGHTS";

    // Dynamic forms
    /**
     * Form template cannot be deleted because it has related claim records.
     */
    public static final String FORMS_PAGE_FORM_HAS_PAYLOAD = "FORMS_PAGE_FORM_HAS_PAYLOAD";
    /**
     * - Fill in name
     */
    public static final String FORMS_PAGE_FILL_NAME = "FORMS_PAGE_FILL_NAME";
    /**
     * - Fill in display name
     */
    public static final String FORMS_PAGE_FILL_DISPLAY_NAME = "FORMS_PAGE_FILL_DISPLAY_NAME";
    /**
     * - Fill in element name
     */
    public static final String FORMS_PAGE_FILL_ELEMENT_NAME = "FORMS_PAGE_FILL_ELEMENT_NAME";
    /**
     * - Fill in element display name
     */
    public static final String FORMS_PAGE_FILL_ELEMENT_DISPLAY_NAME = "FORMS_PAGE_FILL_ELEMENT_DISPLAY_NAME";
    /**
     * - Fill in error message
     */
    public static final String FORMS_PAGE_FILL_ERROR_MESSAGE = "FORMS_PAGE_FILL_ERROR_MESSAGE";
    /**
     * - Minimum occurrence should not be grater than maximum occurrence
     */
    public static final String FORMS_PAGE_MIN_OCCUR_GRATER_MAX_OCCUR = "FORMS_PAGE_MIN_OCCUR_GRATER_MAX_OCCUR";
    /**
     * - Fill in hint
     */
    public static final String FORMS_PAGE_FILL_HINT = "FORMS_PAGE_FILL_HINT";
    /**
     * - Select field type
     */
    public static final String FORMS_PAGE_SELECT_FIELD_TYPE = "FORMS_PAGE_SELECT_FIELD_TYPE";
    /**
     * - Select field constraint type
     */
    public static final String FORMS_PAGE_SELECT_FIELD_CONSTRAINT_TYPE = "FORMS_PAGE_SELECT_FIELD_CONSTRAINT_TYPE";
    /**
     * Fill in form name and display name
     */
    public static final String FORMS_PAGE_FILL_FORM_NAME_AND_DISPLAY_NAME = "FORMS_PAGE_FILL_FORM_NAME_AND_DISPLAY_NAME";
    /**
     * Add at least 1 section
     */
    public static final String FORMS_PAGE_ADD_1_SECTION = "FORMS_PAGE_ADD_1_SECTION";
    /**
     * Add at least 1 field into section %s
     */
    public static final String FORMS_PAGE_ADD_1_FIELD = "FORMS_PAGE_ADD_1_FIELD";
    /**
     * Section with name "%s" has duplication in the existing list of sections
     */
    public static final String FORMS_PAGE_SECTION_NAME_DUPLICATION = "FORMS_PAGE_SECTION_NAME_DUPLICATION";
    /**
     * Field with name "%s" has duplication in the existing list of fields
     */
    public static final String FORMS_PAGE_FIELD_NAME_DUPLICATION = "FORMS_PAGE_FIELD_NAME_DUPLICATION";
    /**
     * Constraint with name "%s" has duplication in the existing list of constraints
     */
    public static final String FORMS_PAGE_CONSTRAINT_NAME_DUPLICATION = "FORMS_PAGE_CONSTRAINT_NAME_DUPLICATION";
    /**
     * Constraint option with name "%s" has duplication in the existing list of constraint options
     */
    public static final String FORMS_PAGE_CONSTRAINT_OPTION_NAME_DUPLICATION = "FORMS_PAGE_CONSTRAINT_OPTION_NAME_DUPLICATION";
    
    /**Select form file to import*/
    public static final String FORMS_PAGE_SELECT_FORM_FILE = "FORMS_PAGE_SELECT_FORM_FILE";
    
    // Reference data
    /**
     * - Fill in code
     */
    public static final String REFDATA_PAGE_FILL_CODE = "REFDATA_PAGE_FILL_CODE";
    /**
     * - Fill in display value
     */
    public static final String REFDATA_PAGE_FILL_DISPLAY_VALUE = "REFDATA_PAGE_FILL_DISPLAY_VALUE";
    /**
     * - Select status
     */
    public static final String REFDATA_PAGE_SELECT_STATUS = "REFDATA_PAGE_SELECT_STATUS";
    /**
     * - Select panel launcher group
     */
    public static final String REFDATA_PAGE_SELECT_PANEL_LAUNCHER_GROUP = "REFDATA_PAGE_SELECT_PANEL_LAUNCHER_GROUP";
    /**
     * - Select request category
     */
    public static final String REFDATA_PAGE_SELECT_REQUEST_CATEGORY = "REFDATA_PAGE_SELECT_REQUEST_CATEGORY";
    /**
     * - Fill in days to complete
     */
    public static final String REFDATA_PAGE_FILL_DAYS_TO_COMPLETE = "REFDATA_PAGE_FILL_DAYS_TO_COMPLETE";
    /**
     * - Fill in base fee
     */
    public static final String REFDATA_PAGE_FILL_BASE_FEE = "REFDATA_PAGE_FILL_BASE_FEE";
    /**
     * - Fill in area base fee
     */
    public static final String REFDATA_PAGE_FILL_AREA_BASE_FEE = "REFDATA_PAGE_FILL_AREA_BASE_FEE";
    /**
     * - Fill in value base fee
     */
    public static final String REFDATA_PAGE_FILL_VALUE_BASE_FEE = "REFDATA_PAGE_FILL_VALUE_BASE_FEE";
    /**
     * - Fill in number of required properties
     */
    public static final String REFDATA_PAGE_FILL_REQ_PROP_NUMBER = "REFDATA_PAGE_FILL_REQ_PROP_NUMBER";
    /**
     * - Select RRR group
     */
    public static final String REFDATA_PAGE_SELECT_RRR_GROUP = "REFDATA_PAGE_SELECT_RRR_GROUP";
    
    /**
     * - FIll in report ID
     */
    public static final String REPORTS_FILL_ID = "REPORTS_FILL_ID";
    
    /**
     * - FIll in display name
     */
    public static final String REPORTS_FILL_DISPLAY_NAME = "REPORTS_FILL_DISPLAY_NAME";
    
    /**
     * - Report ID already exists
     */
    public static final String REPORTS_ID_EXISTS = "REPORTS_ID_EXISTS";
    
    /**
     * - Select report file
     */
    public static final String REPORTS_SELECT_FILE = "REPORTS_SELECT_FILE";
    
    /**
     * - File %s was not found in the archive. Make sure the archive name is equal to the jasper file name. The use of sub-folders in the archive are not allowed.
     */
    public static final String REPORTS_JASPER_FILE_NOT_FOUND = "REPORTS_JASPER_FILE_NOT_FOUND";
    
    /**
     * - Upload only JASPER or ZIP files
     */
    public static final String REPORTS_WRONG_FILE_FORMAT = "REPORTS_WRONG_FILE_FORMAT";
    
    /**
     * - Reports folder path was not found
     */
    public static final String REPORTS_FOLDER_PATH_NOT_FOUND = "REPORTS_FOLDER_PATH_NOT_FOUND";
        
    // Settings
    /**
     * - Fill in name
     */
    public static final String SETTINGS_PAGE_FILL_IN_NAME = "SETTINGS_PAGE_FILL_IN_NAME";
    /**
     * - Fill in value
     */
    public static final String SETTINGS_PAGE_FILL_IN_VALUE = "SETTINGS_PAGE_FILL_IN_VALUE";
    
    // Community area
    /**
     * Provide community area
     */
    public static final String MAP_CONTROL_PROVIDE_COMMUNITY_AREA = "MAP_CONTROL_PROVIDE_COMMUNITY_AREA";
    
    // Group
    
    /** - Fill in name */
    public static final String GROUP_PAGE_FILL_IN_NAME = "GROUP_PAGE_FILL_IN_NAME";
    /** - Select at least 1 role */
    public static final String GROUP_PAGE_SELECT_ROLE = "GROUP_PAGE_SELECT_ROLE";
    
    // User
    
    /** - Fill in user name */
    public static final String USER_PAGE_FILL_IN_USER_NAME = "USER_PAGE_FILL_IN_USER_NAME";
    /** - Fill in first name */
    public static final String USER_PAGE_FILL_IN_FIRST_NAME = "USER_PAGE_FILL_IN_FIRST_NAME";
    /** - Fill in last name */
    public static final String USER_PAGE_FILL_IN_LAST_NAME = "USER_PAGE_FILL_IN_LAST_NAME";
    /** - Fill in password */
    public static final String USER_PAGE_FILL_IN_PASSWORD = "USER_PAGE_FILL_IN_PASSWORD";
    /** - Fill in password confirmation */
    public static final String USER_PAGE_FILL_IN_PASSWORD_CONFIRMATION = "USER_PAGE_FILL_IN_PASSWORD_CONFIRMATION";
    /** - Provided password and password confirmation don't match */
    public static final String USER_PAGE_PASSWORD_NOT_MATCH_CONFIRMATION = "USER_PAGE_PASSWORD_NOT_MATCH_CONFIRMATION";
    /** - Select at least 1 group */
    public static final String USER_PAGE_SELECT_GROUP = "USER_PAGE_SELECT_GROUP";
    
    // Business rules
    /** Fill in display name */
    public static final String BR_PAGE_FILL_IN_DISPLAY_NAME = "BR_PAGE_FILL_IN_DISPLAY_NAME";
    /** Select technical type */
    public static final String BR_PAGE_SELECT_TECHNICAL_TYPE = "BR_PAGE_SELECT_TECHNICAL_TYPE";
    /** Provide at least 1 definition */
    public static final String BR_PAGE_PROVIDE_DEFINITION = "BR_PAGE_PROVIDE_DEFINITION";
    /** Provide at least 1 validation */
    public static final String BR_PAGE_PROVIDE_VALIDATION = "BR_PAGE_PROVIDE_VALIDATION";
    /** - Fill in active from field */
    public static final String BR_PAGE_FILL_IN_ACTIVE_FROM = "BR_PAGE_FILL_IN_ACTIVE_FROM";
    /** - Fill in active until field */
    public static final String BR_PAGE_FILL_IN_ACTIVE_UNTIL = "BR_PAGE_FILL_IN_ACTIVE_UNTIL";
    /** - Fill in rule body */
    public static final String BR_PAGE_FILL_IN_BODY = "BR_PAGE_FILL_IN_BODY";
    /** - Select validation target */
    public static final String BR_PAGE_SELECT_TARGET = "BR_PAGE_SELECT_TARGET";
    /** - Select severity type */
    public static final String BR_PAGE_SELECT_SEVERITY = "BR_PAGE_SELECT_SEVERITY";
    /** - Fill in execution order */
    public static final String BR_PAGE_FILL_IN_ORDER = "BR_PAGE_FILL_IN_ORDER";
    /** - Active from date should be less than active until date */
    public static final String BR_PAGE_ACTIVE_LESS_THAN_UNTIL = "BR_PAGE_ACTIVE_LESS_THAN_UNTIL";
    
    /** Database utilities folder is not set. Configure "db-utilities-folder" in the settings first. */
    public static final String DB_BACKUP_PAGE_DB_UTILITIES_FOLDER_NOT_SET = "DB_BACKUP_PAGE_DB_UTILITIES_FOLDER_NOT_SET";
    /** - Fill in password. */
    public static final String DB_BACKUP_PAGE_FILL_PASSWORD = "DB_BACKUP_PAGE_FILL_PASSWORD";
    /** - Fill in username. */
    public static final String DB_BACKUP_PAGE_FILL_USERNAME = "DB_BACKUP_PAGE_FILL_USERNAME";
    /** Failed to get DB settings. Backup or restore features will not be available. */
    public static final String DB_BACKUP_PAGE_FAILED_TO_GET_DB_SETTINGS = "DB_BACKUP_PAGE_FAILED_TO_GET_DB_SETTINGS";
    /** Failed to delete backup file because of the following reason:<br>%s. */
    public static final String DB_BACKUP_PAGE_FAILED_DELETE_FILE = "DB_BACKUP_PAGE_FAILED_DELETE_FILE";
    /** - File to restore was not selected. */
    public static final String DB_BACKUP_PAGE_SELECT_FILE_TO_RESTORE = "DB_BACKUP_PAGE_SELECT_FILE_TO_RESTORE";
    
    // CRS
    /** - Fill in SRID. */
    public static final String CRS_PAGE_FILL_SRID = "CRS_PAGE_FILL_SRID";
    
    // Query page
    
    /** - Fill in name */
    public static final String QUERY_PAGE_FILL_NAME = "QUERY_PAGE_FILL_NAME";
    /** - Fill in SQL */
    public static final String QUERY_PAGE_FILL_SQL = "QUERY_PAGE_FILL_SQL";
    
    // Layers page
    
    /** - Fill in name */
    public static final String LAYERS_PAGE_FILL_NAME = "LAYERS_PAGE_FILL_NAME";
    /** - Fill in value */
    public static final String LAYERS_PAGE_FILL_VALUE = "LAYERS_PAGE_FILL_VALUE";
    /** - Fill in title */
    public static final String LAYERS_PAGE_FILL_TITLE = "LAYERS_PAGE_FILL_TITLE";
    /** - Select type */
    public static final String LAYERS_PAGE_SELECT_TYPE = "LAYERS_PAGE_SELECT_TYPE";
    /** - Fill in WMS layers */
    public static final String LAYERS_PAGE_FILL_WMS_LAYERS = "LAYERS_PAGE_FILL_WMS_LAYERS";
    /** - Fill in POJO query */
    public static final String LAYERS_PAGE_FILL_POJO_QUERY = "LAYERS_PAGE_FILL_POJO_QUERY";
    /** - Fill in POJO structure */
    public static final String LAYERS_PAGE_FILL_POJO_STRUCTURE = "LAYERS_PAGE_FILL_POJO_STRUCTURE";
    /** - Fill in style */
    public static final String LAYERS_PAGE_FILL_STYLE = "LAYERS_PAGE_FILL_STYLE";
    /** - Fill in shape location */
    public static final String LAYERS_PAGE_FILL_SHAPE_LOCATION = "LAYERS_PAGE_FILL_SHAPE_LOCATION";
    
    // Cache
    
    /** Cache has not been reset. CacheEJB may not be found */
    public static final String RESET_CACHE_PAGE_FAILED = "RESET_CACHE_PAGE_FAILED";
    
    // Projects
    
    /** Enter project name */
    public static final String PROJECTS_FILL_NAME = "PROJECTS_FILL_NAME";
   
    /** Provide project area */
    public static final String PROJECTS_PROVIDE_PROJECT_AREA = "PROJECTS_PROVIDE_PROJECT_AREA";
}
