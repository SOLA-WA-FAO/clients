package org.fao.sola.admin.web.beans.refdata;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
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
import org.sola.admin.services.ejb.refdata.entities.AdministrativeBoundaryType;
import org.sola.admin.services.ejb.refdata.entities.ConfigPanelLauncher;

/**
 * Contains methods and properties to manage {@link ConfigPanelLauncher}
 */
@Named
@ViewScoped
public class BoundaryTypePageBean extends AbstractBackingBean {
    private AdministrativeBoundaryType boundaryType;
    private List<AdministrativeBoundaryType> boundaryTypes;
    
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

    public AdministrativeBoundaryType getBoundaryType() {
        return boundaryType;
    }

    public void setBoundaryType(AdministrativeBoundaryType boundaryType) {
        this.boundaryType = boundaryType;
    }

    public List<AdministrativeBoundaryType> getBoundaryTypes() {
        return boundaryTypes;
    }

    public void setBoundaryTypes(List<AdministrativeBoundaryType> boundaryTypes) {
        this.boundaryTypes = boundaryTypes;
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
    }
    
    private void loadList() {
        boundaryTypes = refEjb.getCodeEntityList(AdministrativeBoundaryType.class, languageBean.getLocale());
    }
    
    public void loadEntity(String code) {
        if (StringUtility.isEmpty(code)) {
            try {
                boundaryType = new AdministrativeBoundaryType();
                boundaryType.setCode("");
            } catch (Exception ex) {
                LogUtility.log("Failed to instantiate reference data class", ex);
            }
        } else {
            boundaryType = refEjb.getCodeEntity(AdministrativeBoundaryType.class, code, null);
        }

        localizedDisplayValues = new LocalizedValuesListBean(languageBean);
        localizedDescriptionValues = new LocalizedValuesListBean(languageBean);
        
        localizedDisplayValues.loadLocalizedValues(boundaryType.getDisplayValue());
        localizedDescriptionValues.loadLocalizedValues(boundaryType.getDescription());
    }

    public void deleteEntity(AbstractCodeEntity entity) {
        entity.setEntityAction(EntityAction.DELETE);
        refEjb.saveCode(entity);
        loadList();
    }

    public void saveEntity() throws Exception {
        if (boundaryType != null) {
            // Validate
            String errors = "";
            if (StringUtility.isEmpty(boundaryType.getCode())) {
                errors += msgProvider.getErrorMessage(ErrorKeys.REFDATA_PAGE_FILL_CODE) + "\r\n";
            }
            if (StringUtility.isEmpty(boundaryType.getStatus())) {
                errors += msgProvider.getErrorMessage(ErrorKeys.REFDATA_PAGE_SELECT_STATUS) + "\r\n";
            }
            if (localizedDisplayValues.getLocalizedValues() == null || localizedDisplayValues.getLocalizedValues().size() < 1
                    || StringUtility.isEmpty(localizedDisplayValues.getLocalizedValues().get(0).getLocalizedValue())) {
                errors += msgProvider.getErrorMessage(ErrorKeys.REFDATA_PAGE_FILL_DISPLAY_VALUE) + "\r\n";
            }
                        
            if (!errors.equals("")) {
                throw new Exception(errors);
            }

            boundaryType.setDisplayValue(localizedDisplayValues.buildMultilingualString());
            boundaryType.setDescription(localizedDescriptionValues.buildMultilingualString());
            refEjb.saveCode(boundaryType);
            loadList();
        }
    }
}