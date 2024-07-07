package org.fao.sola.admin.web.beans.project;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.fao.sola.admin.web.beans.AbstractBackingBean;
import org.fao.sola.admin.web.beans.helpers.ErrorKeys;
import org.fao.sola.admin.web.beans.helpers.MessageBean;
import org.fao.sola.admin.web.beans.helpers.MessageProvider;
import org.fao.sola.admin.web.beans.helpers.MessagesKeys;
import org.fao.sola.admin.web.beans.language.LanguageBean;
import org.sola.common.ConfigConstants;
import org.sola.common.StringUtility;
import org.sola.admin.services.ejb.search.businesslogic.SearchAdminEJBLocal;
import org.sola.admin.services.ejb.search.repository.entities.ConfigMapLayer;
import org.sola.admin.services.ejb.search.repository.entities.ConfigMapLayerMetadata;
import org.sola.admin.services.ejb.system.businesslogic.SystemAdminEJBLocal;
import org.sola.admin.services.ejb.system.repository.entities.Project;
import org.sola.admin.services.ejb.system.repository.entities.ProjectMapLayer;
import org.sola.admin.services.ejb.system.repository.entities.ProjectSetting;
import org.sola.admin.services.ejb.system.repository.entities.Setting;
import org.sola.services.common.EntityAction;

/**
 * Contains methods and properties to manage {@link Setting}
 */
@Named
@ViewScoped
public class ProjectPageBean extends AbstractBackingBean {

    @EJB
    SearchAdminEJBLocal searchEjb;

    @Inject
    LanguageBean langBean;

    @Inject
    MessageProvider msgProvider;

    @EJB
    SystemAdminEJBLocal systemEjb;

    @Inject
    MessageBean msgBean;

    private List<ConfigMapLayer> layers;
    private List<ConfigMapLayer> allLayers;
    private boolean isOffline;
    private Project project;
    private String[] selectedAvailableLayerIds;
    private String[] selectedProjectLayerIds;
    private List<Setting> allSettings;
    private ProjectSetting setting;

    public Project getProject() {
        return project;
    }

    public boolean getIsOffline() {
        return isOffline;
    }

    public ConfigMapLayer[] getLayers() {
        if (layers == null) {
            return null;
        }
        return layers.toArray(new ConfigMapLayer[layers.size()]);
    }

    public ConfigMapLayer[] getAllLayers() {
        if (allLayers == null) {
            return null;
        }
        return allLayers.toArray(new ConfigMapLayer[allLayers.size()]);
    }

    public String[] getSelectedAvailableLayerIds() {
        return selectedAvailableLayerIds;
    }

    public void setSelectedAvailableLayerIds(String[] selectedAvailableLayerIds) {
        this.selectedAvailableLayerIds = selectedAvailableLayerIds;
    }

    public String[] getSelectedProjectLayerIds() {
        return selectedProjectLayerIds;
    }

    public void setSelectedProjectLayerIds(String[] selectedProjectLayerIds) {
        this.selectedProjectLayerIds = selectedProjectLayerIds;
    }

    public String getSettingDescription(String name) {
        if (allSettings != null && !allSettings.isEmpty()) {
            for (Setting s : allSettings) {
                if (s.getName().equalsIgnoreCase(name)) {
                    return s.getDescription();
                }
            }
        }
        return "";
    }

    public Setting[] getSystemSettings() {
        List<Setting> filteredSettings = new ArrayList<>();
        Setting emptySetting = new Setting();
        emptySetting.setName("");
        filteredSettings.add(emptySetting);

        if (allSettings != null && !allSettings.isEmpty()) {
            boolean exists;
            for (Setting systemSetting : allSettings) {
                exists = false;
                if (project.getSettings() != null && (setting == null || StringUtility.isEmpty(setting.getName()))) {
                    for (ProjectSetting pSetting : project.getSettings()) {
                        if (pSetting.getName().equalsIgnoreCase(systemSetting.getName())) {
                            exists = true;
                            break;
                        }
                    }
                }
                if (!exists) {
                    filteredSettings.add(systemSetting);
                }
            }
        }
        return filteredSettings.toArray(new Setting[filteredSettings.size()]);
    }

    public ProjectSetting getSetting() {
        if (setting == null) {
            setting = new ProjectSetting();
        }
        return setting;
    }

    public void setSetting(ProjectSetting setting) {
        this.setting = setting;
    }

    public void init() {
        allSettings = systemEjb.getAllSettings();
        String action = getRequestParam("action");
        if (action.equalsIgnoreCase("saved")) {
            msgBean.setSuccessMessage(msgProvider.getMessage(MessagesKeys.PROJECTS_SAVED_SUCCESS));
        }

        String projectid = getRequestParam("id");
        if (StringUtility.isEmpty(projectid)) {
            project = new Project();
        } else {
            project = searchEjb.getEntityById(Project.class, projectid);
        }

        allLayers = searchEjb.getConfigMapLayerList(langBean.getLocale());
        layers = searchEjb.getMapLayersByProject(projectid, langBean.getLocale());
        sortProjectLayers();
        isOffline = systemEjb.getSetting(ConfigConstants.OT_OFFLINE_MODE, "0").equals("1");

        // Exlude project layers from all layers list
        if (layers != null && !layers.isEmpty() && allLayers != null && !allLayers.isEmpty()) {
            for (ConfigMapLayer layer : layers) {
                for (Iterator<ConfigMapLayer> iterator = allLayers.iterator(); iterator.hasNext();) {
                    ConfigMapLayer defLayer = iterator.next();
                    if (layer.getId().equals(defLayer.getId())) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    public ProjectSetting[] getSettings() {
        if (project != null) {
            return getEntitiesArray(project.getSettings());
        }
        return null;
    }

    public void loadSetting(String name) {
        setting = new ProjectSetting();

        if (StringUtility.isEmpty(name)) {
            setting.setProjectId(project.getId());
        } else {
            for (ProjectSetting pSetting : project.getSettings()) {
                if (pSetting.getName().equals(name)) {
                    setting.setName(pSetting.getName());
                    setting.setProjectId(pSetting.getProjectId());
                    setting.setValue(pSetting.getValue());
                }
            }
        }
    }

    public void deleteSetting(String name) {
        for (ProjectSetting pSetting : project.getSettings()) {
            if (pSetting.getName().equalsIgnoreCase(name)) {
                pSetting.setEntityAction(EntityAction.DELETE);
                break;
            }
        }
    }

    public void saveSetting() throws Exception {
        // Make checks
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

        if (project.getSettings() == null) {
            project.setSettings(new ArrayList<ProjectSetting>());
        }

        boolean found = false;

        for (ProjectSetting pSetting : project.getSettings()) {
            if (pSetting.getName().equalsIgnoreCase(setting.getName())) {
                // Update
                pSetting.setValue(setting.getValue());
                found = true;
                break;
            }
        }

        if (!found) {
            project.getSettings().add(setting);
        }
    }

    private void sortProjectLayers() {
        if (layers != null && !layers.isEmpty() && project.getMapLayers() != null && !project.getMapLayers().isEmpty()) {
            ArrayList<ConfigMapLayer> sortedLayers = new ArrayList<>();
            for (ProjectMapLayer pLayer : project.getMapLayers()) {
                for (ConfigMapLayer layer : layers) {
                    if (pLayer.getLayerId().equals(layer.getId())) {
                        sortedLayers.add(layer);
                        break;
                    }
                }
            }
            layers.clear();
            layers.addAll(sortedLayers);
        }
    }

    public void addToProjectLayers() {
        if (selectedAvailableLayerIds != null && selectedAvailableLayerIds.length > 0) {
            if (layers == null) {
                layers = new ArrayList<>();
            }
            for (Iterator<ConfigMapLayer> iterator = allLayers.iterator(); iterator.hasNext();) {
                ConfigMapLayer layer = iterator.next();
                for (String layerId : selectedAvailableLayerIds) {
                    if (layer.getId().equals(layerId)) {
                        layers.add(layer);
                        iterator.remove();
                    }
                }
            }
        }
    }

    public void removeToProjectLayers() {
        if (selectedProjectLayerIds != null && selectedProjectLayerIds.length > 0) {
            if (allLayers == null) {
                allLayers = new ArrayList<>();
            }
            for (Iterator<ConfigMapLayer> iterator = layers.iterator(); iterator.hasNext();) {
                ConfigMapLayer layer = iterator.next();
                for (String layerId : selectedProjectLayerIds) {
                    if (layer.getId().equals(layerId)) {
                        allLayers.add(layer);
                        iterator.remove();
                    }
                }
            }
        }
    }

    public void moveProjectLayersUp() {
        if (selectedProjectLayerIds != null && selectedProjectLayerIds.length > 0) {
            for (int j = 0; j < selectedProjectLayerIds.length; j++) {
                for (int i = 0; i < layers.size(); i++) {
                    if (layers.get(i).getId().equals(selectedProjectLayerIds[j])) {
                        // Move up
                        if (i - 1 >= 0) {
                            Collections.swap(layers, i, i - 1);
                        }
                        break;
                    }
                }
            }
        }
    }

    public void moveProjectLayersDown() {
        if (selectedProjectLayerIds != null && selectedProjectLayerIds.length > 0) {
            for (int j = selectedProjectLayerIds.length - 1; j >= 0; j--) {
                for (int i = layers.size() - 1; i >= 0; i--) {
                    if (layers.get(i).getId().equals(selectedProjectLayerIds[j])) {
                        // Move down
                        if (i + 1 <= layers.size() - 1) {
                            Collections.swap(layers, i, i + 1);
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * Returns WMS layer parameters sent to the server.
     */
    public String getLayerParamsString(ConfigMapLayer layer, boolean addCommaInFront) {
        if (layer == null || layer.getMetadataList() == null) {
            return "";
        }

        String result = "";

        for (ConfigMapLayerMetadata param : layer.getMetadataList()) {
            if (!param.isForClient() && layer.getTypeCode().equalsIgnoreCase("wms")
                    && !param.getName().equalsIgnoreCase("LEGEND_OPTIONS")) {
                if (!result.equals("")) {
                    result += ", ";
                }
                result += param.getName() + ": '" + param.getValue() + "'";
            }
        }

        if (!result.equals("") && addCommaInFront) {
            result = ", " + result;
        }
        return result;
    }

    /**
     * Return WMS legend options
     */
    public String getLegendOptions(ConfigMapLayer layer) {
        if (layer == null || layer.getMetadataList() == null) {
            return "";
        }

        for (ConfigMapLayerMetadata param : layer.getMetadataList()) {
            if (param.getName().equalsIgnoreCase("LEGEND_OPTIONS")) {
                return param.getValue();
            }
        }

        return "''";
    }

    /**
     * Returns WMS layer options used by map component.
     */
    public String getLayerOptionsString(ConfigMapLayer layer, boolean addCommaInFront) {
        if (layer == null || layer.getMetadataList() == null) {
            return "";
        }

        String result = "";

        for (ConfigMapLayerMetadata param : layer.getMetadataList()) {
            if (param.isForClient() && layer.getTypeCode().equalsIgnoreCase("wms")
                    && !param.getName().equalsIgnoreCase("LEGEND_OPTIONS")) {
                if (!result.equals("")) {
                    result += ", ";
                }
                result += param.getName() + ": '" + param.getValue() + "'";
            }
        }

        if (!result.equals("") && addCommaInFront) {
            result = ", " + result;
        }
        return result;
    }

    public void save() {
        try {
            // Validate
            if (StringUtility.isEmpty(project.getDisplayName())) {
                throw new Exception(msgProvider.getErrorMessage(ErrorKeys.PROJECTS_FILL_NAME) + "\r\n");
            }
            if (StringUtility.isEmpty(project.getBoundary())) {
                throw new Exception(msgProvider.getErrorMessage(ErrorKeys.PROJECTS_PROVIDE_PROJECT_AREA) + "\r\n");
            }

            // Prepare project layes
            // Delete
            if (project.getMapLayers() != null) {
                for (ProjectMapLayer pLayer : project.getMapLayers()) {
                    boolean found = false;
                    if (layers != null) {
                        for (ConfigMapLayer layer : layers) {
                            if (pLayer.getLayerId().equalsIgnoreCase(layer.getId())) {
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        pLayer.setEntityAction(EntityAction.DELETE);
                    }
                }
            }

            // Add and update order
            if (layers != null) {
                int index = 1;
                for (ConfigMapLayer layer : layers) {
                    boolean found = false;
                    if (project.getMapLayers() != null) {
                        for (ProjectMapLayer pLayer : project.getMapLayers()) {
                            if (pLayer.getLayerId().equalsIgnoreCase(layer.getId())) {
                                // Update order
                                if (pLayer.getLayerOrder() != index) {
                                    pLayer.setLayerOrder(index);
                                }
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        if (project.getMapLayers() == null) {
                            project.setMapLayers(new ArrayList());
                        }
                        ProjectMapLayer pLayer = new ProjectMapLayer();
                        pLayer.setLayerId(layer.getId());
                        pLayer.setLayerOrder(index);
                        pLayer.setProjectId(project.getId());
                        project.getMapLayers().add(pLayer);
                    }
                    index += 1;
                }
            }

            project = systemEjb.saveProject(project);
            getContext().getExternalContext().redirect(getRequest().getContextPath() + "/projects/Project.xhtml?id=" + project.getId() + "&action=saved");
        } catch (Exception e) {
            getContext().addMessage(null, new FacesMessage(processException(e, langBean.getLocale()).getMessage()));
        }
    }
}
