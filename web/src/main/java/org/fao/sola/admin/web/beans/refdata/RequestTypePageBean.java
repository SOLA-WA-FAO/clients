package org.fao.sola.admin.web.beans.refdata;

import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.fao.sola.admin.web.beans.AbstractBackingBean;
import org.fao.sola.admin.web.beans.helpers.ErrorKeys;
import org.fao.sola.admin.web.beans.helpers.MessageBean;
import org.fao.sola.admin.web.beans.helpers.MessageProvider;
import org.fao.sola.admin.web.beans.language.LanguageBean;
import org.fao.sola.admin.web.beans.localization.LocalizedValuesListBean;
import org.sola.common.StringUtility;
import org.sola.common.logging.LogUtility;
import org.sola.services.common.EntityAction;
import org.sola.services.common.repository.entities.AbstractCodeEntity;
import org.sola.admin.services.ejb.refdata.businesslogic.RefDataAdminEJBLocal;
import org.sola.admin.services.ejb.refdata.entities.ConfigPanelLauncher;
import org.sola.admin.services.ejb.refdata.entities.RequestCategoryType;
import org.sola.admin.services.ejb.refdata.entities.RequestDisplayGroup;
import org.sola.admin.services.ejb.refdata.entities.RequestType;
import org.sola.admin.services.ejb.refdata.entities.RequestTypeRequiresSourceType;
import org.sola.admin.services.ejb.refdata.entities.RrrType;
import org.sola.admin.services.ejb.refdata.entities.SourceType;
import org.sola.admin.services.ejb.refdata.entities.TypeAction;

/**
 * Contains methods and properties to manage {@link RequestType}
 */
@Named
@ViewScoped
public class RequestTypePageBean extends AbstractBackingBean {

    private RequestType requestType;
    private List<RequestType> requestTypeList;
    private RequestCategoryType[] requestCategoryTypes;
    private ConfigPanelLauncher[] configPanelLaunchers;
    private RrrType[] rrrTypes;
    private TypeAction[] typeActions;
    private SourceType[] sourceTypes;
    private RequestDisplayGroup[] displayGroups;
    private String[] selectedSourceCodes;
    
    @Inject
    MessageBean msg;

    @Inject
    MessageProvider msgProvider;

    @Inject
    private LanguageBean languageBean;

    LocalizedValuesListBean localizedDisplayValues;
    LocalizedValuesListBean localizedDescriptionValues;

    @EJB
    RefDataAdminEJBLocal refEjb;

    public String[] getSelectedSourceCodes() {
        return selectedSourceCodes;
    }

    public void setSelectedSourceCodes(String[] selectedSourceCodes) {
        this.selectedSourceCodes = selectedSourceCodes;
    }

    public RequestDisplayGroup[] getDisplayGroups() {
        return displayGroups;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public List<RequestType> getRequestTypeList() {
        return requestTypeList;
    }

    public RequestCategoryType[] getRequestCategoryTypes() {
        return requestCategoryTypes;
    }

    public ConfigPanelLauncher[] getConfigPanelLaunchers() {
        return configPanelLaunchers;
    }

    public RrrType[] getRrrTypes() {
        return rrrTypes;
    }

    public TypeAction[] getTypeActions() {
        return typeActions;
    }

    public SourceType[] getSourceTypes() {
        return sourceTypes;
    }

    public LocalizedValuesListBean getLocalizedDisplayValues() {
        return localizedDisplayValues;
    }

    public LocalizedValuesListBean getLocalizedDescriptionValues() {
        return localizedDescriptionValues;
    }

    @PostConstruct
    private void init() {
        loadList();
        // Load supporting lists
        List<RequestCategoryType> requestCategoryTypeList = refEjb.getCodeEntityList(RequestCategoryType.class, languageBean.getLocale());
        List<RrrType> rrrTypeList = refEjb.getCodeEntityList(RrrType.class, languageBean.getLocale());
        List<ConfigPanelLauncher> configPanelLauncherList = refEjb.getCodeEntityList(ConfigPanelLauncher.class, languageBean.getLocale());
        List<TypeAction> typeActionList = refEjb.getCodeEntityList(TypeAction.class, languageBean.getLocale());
        List<SourceType> sourceTypeList = refEjb.getCodeEntityList(SourceType.class, languageBean.getLocale());
        List<RequestDisplayGroup> displayGroupList = refEjb.getCodeEntityList(RequestDisplayGroup.class, languageBean.getLocale());
        
        if (sourceTypeList != null) {
            sourceTypes = sourceTypeList.toArray(new SourceType[sourceTypeList.size()]);
        }
        
        if (displayGroupList != null) {
            displayGroupList = (ArrayList<RequestDisplayGroup>)((ArrayList)displayGroupList).clone();
            // Add dummy
            RequestDisplayGroup dummy = new RequestDisplayGroup();
            dummy.setCode("");
            dummy.setDisplayValue(" ");
            displayGroupList.add(0, dummy);
            displayGroups = displayGroupList.toArray(new RequestDisplayGroup[displayGroupList.size()]);
        }
        
        if (requestCategoryTypeList != null) {
            requestCategoryTypeList = (ArrayList<RequestCategoryType>)((ArrayList)requestCategoryTypeList).clone();
            // Add dummy
            RequestCategoryType dummy = new RequestCategoryType();
            dummy.setCode("");
            dummy.setDisplayValue(" ");
            requestCategoryTypeList.add(0, dummy);
            requestCategoryTypes = requestCategoryTypeList.toArray(new RequestCategoryType[requestCategoryTypeList.size()]);
        }

        if (rrrTypeList != null) {
            rrrTypeList = (ArrayList<RrrType>)((ArrayList)rrrTypeList).clone();
            // Add dummy
            RrrType dummy = new RrrType();
            dummy.setCode("");
            dummy.setDisplayValue(" ");
            rrrTypeList.add(0, dummy);
            rrrTypes = rrrTypeList.toArray(new RrrType[rrrTypeList.size()]);
        }

        if (configPanelLauncherList != null) {
            configPanelLauncherList = (ArrayList<ConfigPanelLauncher>)((ArrayList)configPanelLauncherList).clone();
            // Add dummy
            ConfigPanelLauncher dummy = new ConfigPanelLauncher();
            dummy.setCode("");
            dummy.setDisplayValue(" ");
            configPanelLauncherList.add(0, dummy);
            configPanelLaunchers = configPanelLauncherList.toArray(new ConfigPanelLauncher[configPanelLauncherList.size()]);
        }

        if (typeActionList != null) {
            typeActionList = (ArrayList<TypeAction>)((ArrayList)typeActionList).clone();
            // Add dummy
            TypeAction dummy = new TypeAction();
            dummy.setCode("");
            dummy.setDisplayValue(" ");
            typeActionList.add(0, dummy);
            typeActions = typeActionList.toArray(new TypeAction[typeActionList.size()]);
        }
    }

    public String getSourceTypeName(String code) {
        if (code != null && sourceTypes != null) {
            for (SourceType item : sourceTypes) {
                if (item.getCode().equalsIgnoreCase(code)) {
                    return item.getDisplayValue();
                }
            }
        }
        return "";
    }

    public String getRequestCategoryName(String code) {
        if (code != null && requestCategoryTypes != null) {
            for (RequestCategoryType item : requestCategoryTypes) {
                if (item.getCode().equalsIgnoreCase(code)) {
                    return item.getDisplayValue();
                }
            }
        }
        return "";
    }

    public String getRrrTypeName(String code) {
        if (code != null && rrrTypes != null) {
            for (RrrType item : rrrTypes) {
                if (item.getCode().equalsIgnoreCase(code)) {
                    return item.getDisplayValue();
                }
            }
        }
        return "";
    }

    public String getDisplayGroupName(String code) {
        if (code != null && displayGroups != null) {
            for (RequestDisplayGroup item : displayGroups) {
                if (item.getCode().equalsIgnoreCase(code)) {
                    return item.getDisplayValue();
                }
            }
        }
        return "";
    }
    
    public String getPanelLauncherName(String code) {
        if (code != null && configPanelLaunchers != null) {
            for (ConfigPanelLauncher item : configPanelLaunchers) {
                if (item.getCode().equalsIgnoreCase(code)) {
                    return item.getDisplayValue();
                }
            }
        }
        return "";
    }

    public String getTypeActionName(String code) {
        if (code != null && typeActions != null) {
            for (TypeAction item : typeActions) {
                if (item.getCode().equalsIgnoreCase(code)) {
                    return item.getDisplayValue();
                }
            }
        }
        return "";
    }

    private void loadList() {
        requestTypeList = refEjb.getCodeEntityList(RequestType.class, languageBean.getLocale());
    }

    public void loadEntity(String code) {
        if (StringUtility.isEmpty(code)) {
            try {
                requestType = new RequestType();
                requestType.setCode("");
                requestType.setSourceTypeCodes(new ArrayList<RequestTypeRequiresSourceType>());
            } catch (Exception ex) {
                LogUtility.log("Failed to instantiate reference data class", ex);
            }
        } else {
            requestType = refEjb.getCodeEntity(RequestType.class, code, null);
        }

        // Select/unselect source types
        List<SourceType> sourceTypesToSelect = new ArrayList<>();
        if (requestType.getSourceTypeCodes() != null) {
            for (RequestTypeRequiresSourceType rst : requestType.getSourceTypeCodes()) {
                if (sourceTypes != null) {
                    for (SourceType sourceType : sourceTypes) {
                        if (rst.getSourceTypeCode().equalsIgnoreCase(sourceType.getCode())) {
                            sourceTypesToSelect.add(sourceType);
                            break;
                        }
                    }
                }
            }
        }
        selectedSourceCodes = new String[sourceTypesToSelect.size()];
        int i=0;
        for(SourceType sType : sourceTypesToSelect){
            selectedSourceCodes[i] = sType.getCode();
            i+=1;
        }

        localizedDisplayValues = new LocalizedValuesListBean(languageBean);
        localizedDescriptionValues = new LocalizedValuesListBean(languageBean);
        
        localizedDisplayValues.loadLocalizedValues(requestType.getDisplayValue());
        localizedDescriptionValues.loadLocalizedValues(requestType.getDescription());
    }

    public void deleteEntity(AbstractCodeEntity entity) {
        entity.setEntityAction(EntityAction.DELETE);
        refEjb.saveCode(entity);
        loadList();
    }

    public void saveEntity() throws Exception {
        if (requestType != null) {
            // Validate
            String errors = "";
            if (StringUtility.isEmpty(requestType.getCode())) {
                errors += msgProvider.getErrorMessage(ErrorKeys.REFDATA_PAGE_FILL_CODE) + "\r\n";
            }
            if (StringUtility.isEmpty(requestType.getStatus())) {
                errors += msgProvider.getErrorMessage(ErrorKeys.REFDATA_PAGE_SELECT_STATUS) + "\r\n";
            }
            if (localizedDisplayValues.getLocalizedValues() == null || localizedDisplayValues.getLocalizedValues().size() < 1
                    || StringUtility.isEmpty(localizedDisplayValues.getLocalizedValues().get(0).getLocalizedValue())) {
                errors += msgProvider.getErrorMessage(ErrorKeys.REFDATA_PAGE_FILL_DISPLAY_VALUE) + "\r\n";
            }
            if (StringUtility.isEmpty(requestType.getRequestCategoryCode())) {
                errors += msgProvider.getErrorMessage(ErrorKeys.REFDATA_PAGE_SELECT_REQUEST_CATEGORY) + "\r\n";
            }

            if (!errors.equals("")) {
                throw new Exception(errors);
            }

            requestType.setDisplayValue(localizedDisplayValues.buildMultilingualString());
            requestType.setDescription(localizedDescriptionValues.buildMultilingualString());

            // Prepare source types related to request type
            // Delete
            if (requestType.getSourceTypeCodes() != null) {
                for (RequestTypeRequiresSourceType rtst : requestType.getSourceTypeCodes()) {
                    boolean found = false;
                    if (selectedSourceCodes != null) {
                        for (String sourceCode : selectedSourceCodes) {
                            if (rtst.getSourceTypeCode().equalsIgnoreCase(sourceCode)) {
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        rtst.setEntityAction(EntityAction.DELETE);
                    }
                }
            }
            
            // Add
            if (selectedSourceCodes != null) {
                for (String sourceCode : selectedSourceCodes) {
                    boolean found = false;
                    if (requestType.getSourceTypeCodes() != null) {
                        for (RequestTypeRequiresSourceType rtst : requestType.getSourceTypeCodes()) {
                            if (rtst.getSourceTypeCode().equalsIgnoreCase(sourceCode)) {
                                found = true;
                                break;
                            }
                        }
                    }
                    if(!found){
                        RequestTypeRequiresSourceType rtst = new RequestTypeRequiresSourceType();
                        rtst.setRequestTypeCode(requestType.getCode());
                        rtst.setSourceTypeCode(sourceCode);
                        requestType.getSourceTypeCodes().add(rtst);
                    }
                }
            }

            refEjb.saveCode(requestType);
            loadList();
        }
    }
}
