package org.fao.sola.admin.web.beans.setting;

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
import org.sola.common.ConfigConstants;
import org.sola.common.StringUtility;
import org.sola.services.common.EntityAction;
import org.sola.admin.services.ejb.scheduler.businesslogic.MailerAdminLocal;
import org.sola.admin.services.ejb.system.businesslogic.SystemAdminEJBLocal;
import org.sola.admin.services.ejb.system.repository.entities.Setting;

/**
 * Contains methods and properties to manage {@link Setting}
 */
@Named
@ViewScoped
public class SettingsPageBean extends AbstractBackingBean {
    private Setting setting;
    private List<Setting> settings;
    
    @Inject
    MessageBean msg;

    @Inject
    MessageProvider msgProvider;

    @EJB
    SystemAdminEJBLocal systemEjb;
    
    @EJB
    MailerAdminLocal mailer;
    
    public Setting getSetting() {
        return setting;
    }

    public List<Setting> getSettings() {
        return settings;
    }
    
    @PostConstruct
    private void init(){
        loadList();
    }
    
    private void loadList(){
        settings = systemEjb.getAllSettings();
    }
    
    public void loadEntity(String name) {
        if (StringUtility.isEmpty(name)) {
            setting = new Setting();
            setting.setName("");
        } else {
            setting = systemEjb.getSetting(name);
        }
    }

    public void deleteEntity(Setting entity) {
        entity.setEntityAction(EntityAction.DELETE);
        systemEjb.saveSetting(entity);
        loadList();
    }

    public void saveEntity() throws Exception {
        if (setting != null) {
            // Validate
            String errors = "";
            if (StringUtility.isEmpty(setting.getName())) {
                errors += msgProvider.getErrorMessage(ErrorKeys.SETTINGS_PAGE_FILL_IN_NAME) + "\r\n";
            }
            if (StringUtility.isEmpty(setting.getValue())) {
                errors += msgProvider.getErrorMessage(ErrorKeys.SETTINGS_PAGE_FILL_IN_VALUE) + "\r\n";
            }
                    
            if (!errors.equals("")) {
                throw new Exception(errors);
            }

            systemEjb.saveSetting(setting);
            loadList();
            
            // Reset mailer configuration if any of related settings have been changed
            if(setting.getName().startsWith("email-")){
                mailer.init();
            }
        }
    }
}
