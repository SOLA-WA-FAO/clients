package org.fao.sola.admin.web.beans.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.fao.sola.admin.web.beans.AbstractBackingBean;
import org.fao.sola.admin.web.beans.helpers.ErrorKeys;
import org.fao.sola.admin.web.beans.helpers.MessageProvider;
import org.fao.sola.admin.web.beans.language.LanguageBean;
import org.sola.common.StringUtility;
import org.sola.services.common.EntityAction;
import org.sola.admin.services.ejb.search.businesslogic.SearchAdminEJBLocal;
import org.sola.admin.services.ejb.search.repository.entities.ProjectSearchResult;
import org.sola.admin.services.ejb.search.repository.entities.UserSearchParams;
import org.sola.admin.services.ejb.search.repository.entities.UserSearchResult;
import org.sola.admin.services.ejbs.admin.businesslogic.AdministratorEJBLocal;
import org.sola.admin.services.ejbs.admin.businesslogic.repository.entities.Group;
import org.sola.admin.services.ejbs.admin.businesslogic.repository.entities.User;
import org.sola.admin.services.ejbs.admin.businesslogic.repository.entities.UserGroup;
import org.sola.admin.services.ejbs.admin.businesslogic.repository.entities.UserProject;

/**
 * Contains methods and properties to manage {@link Group}
 */
@Named
@ViewScoped
public class UserPageBean extends AbstractBackingBean {

    private User user;
    private List<UserSearchResult> searchResults;
    private ProjectSearchResult[] allProjects;
    private String[] selectedProjects;
    private Group[] groups;
    private Map<String, String> mapProjects;
    private Map<String, String> mapGroups;
    private String[] selectedGroupCodes;
    private UserSearchParams searchParams;
    private String passwordConfirmation;
    private String oldPassword;

    @Inject
    MessageProvider msgProvider;

    @Inject
    LanguageBean langBean;
    
    @EJB
    AdministratorEJBLocal adminEjb;

    @EJB
    SearchAdminEJBLocal searchEjb;

    public User getUser() {
        return user;
    }

    public List<UserSearchResult> getSearchResults() {
        return searchResults;
    }

    public Group[] getGroups() {
        return groups;
    }

    public Map<String, String> getMapGroups() {
        return mapGroups;
    }

    public Map<String, String> getMapProjects() {
        return mapProjects;
    }
    
    public String[] getSelectedGroupCodes() {
        return selectedGroupCodes;
    }

    public void setSelectedGroupCodes(String[] selectedGroupCodes) {
        this.selectedGroupCodes = selectedGroupCodes;
    }

    public UserSearchParams getSearchParams() {
        if (searchParams == null) {
            searchParams = new UserSearchParams();
        }
        return searchParams;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public ProjectSearchResult[] getAllProjects() {
        return allProjects;
    }

    public String[] getSelectedProjects() {
        return selectedProjects;
    }

    public void setSelectedProjects(String[] selectedProjects) {
        this.selectedProjects = selectedProjects;
    }
    
    public String getGroupName(String id) {
        if (id != null && groups != null) {
            for (Group item : groups) {
                if (item.getId().equalsIgnoreCase(id)) {
                    return item.getName();
                }
            }
        }
        return "";
    }

    @PostConstruct
    private void init() {
        List<Group> groupsList = adminEjb.getGroups();
        mapGroups = new HashMap<>();
        if (groupsList != null) {
            groups = groupsList.toArray(new Group[groupsList.size()]);
            for (Group g : groups) {
                mapGroups.put(g.getName(), g.getId());
            }
        }
        mapProjects = new HashMap<>();
        List<ProjectSearchResult> projectsList = searchEjb.getAllProjects(langBean.getLocale());
        if (projectsList != null) {
            allProjects = projectsList.toArray(new ProjectSearchResult[projectsList.size()]);
            for (ProjectSearchResult p : projectsList) {
                mapProjects.put(p.getDisplayName(), p.getId());
            }
        }
        
        mapProjects.put("", "");
        mapGroups.put("", "");
        searchParams = new UserSearchParams();
        search();
    }

    public void search() {
        searchResults = searchEjb.searchUsers(searchParams, langBean.getLocale());
    }

    public void loadUser(String userName) {
        if (StringUtility.isEmpty(userName)) {
            user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setUserGroups(new ArrayList<UserGroup>());
            user.setProjects(new ArrayList<UserProject>());
        } else {
            user = adminEjb.getUser(userName);
        }

        passwordConfirmation = user.getPassword();
        oldPassword = user.getPassword();

        // Select/unselect groups
        List<Group> groupsToSelect = new ArrayList<>();
        if (user.getUserGroups() != null) {
            for (UserGroup userGroup : user.getUserGroups()) {
                if (groups != null) {
                    for (Group group : groups) {
                        if (userGroup.getGroupId().equalsIgnoreCase(group.getId())) {
                            groupsToSelect.add(group);
                            break;
                        }
                    }
                }
            }
        }
        selectedGroupCodes = new String[groupsToSelect.size()];
        int i = 0;
        for (Group group : groupsToSelect) {
            selectedGroupCodes[i] = group.getId();
            i += 1;
        }
        
        // Select/unselect projects
        List<UserProject> projectsToSelect = new ArrayList<>();
        if (user.getProjects()!= null) {
            for (UserProject userProject : user.getProjects()) {
                if (allProjects != null) {
                    for (ProjectSearchResult project : allProjects) {
                        if (userProject.getProjectId().equalsIgnoreCase(project.getId())) {
                            projectsToSelect.add(userProject);
                            break;
                        }
                    }
                }
            }
        }
        selectedProjects = new String[projectsToSelect.size()];
        i = 0;
        for (UserProject project : projectsToSelect) {
            selectedProjects[i] = project.getProjectId();
            i += 1;
        }
    }

    public void deleteUser(String userName) {
        if (!StringUtility.isEmpty(userName)) {
            User usr = adminEjb.getUser(userName);
            usr.setEntityAction(EntityAction.DELETE);
            adminEjb.saveUser(usr);
            search();
        }
    }

    public void saveUser() throws Exception {
        if (user == null) {
            return;
        }

        // Validate
        String errors = "";
        if (StringUtility.isEmpty(user.getUserName())) {
            errors += msgProvider.getErrorMessage(ErrorKeys.USER_PAGE_FILL_IN_USER_NAME) + "\r\n";
        }
        if (StringUtility.isEmpty(user.getFirstName())) {
            errors += msgProvider.getErrorMessage(ErrorKeys.USER_PAGE_FILL_IN_FIRST_NAME) + "\r\n";
        }
        if (StringUtility.isEmpty(user.getLastName())) {
            errors += msgProvider.getErrorMessage(ErrorKeys.USER_PAGE_FILL_IN_LAST_NAME) + "\r\n";
        }
        if (StringUtility.isEmpty(user.getPassword())) {
            errors += msgProvider.getErrorMessage(ErrorKeys.USER_PAGE_FILL_IN_PASSWORD) + "\r\n";
        }
        if (StringUtility.isEmpty(passwordConfirmation)) {
            errors += msgProvider.getErrorMessage(ErrorKeys.USER_PAGE_FILL_IN_PASSWORD_CONFIRMATION) + "\r\n";
        }
        if (!StringUtility.empty(passwordConfirmation).equals(user.getPassword())) {
            errors += msgProvider.getErrorMessage(ErrorKeys.USER_PAGE_PASSWORD_NOT_MATCH_CONFIRMATION) + "\r\n";
        }
        if (selectedGroupCodes == null || selectedGroupCodes.length < 1) {
            errors += msgProvider.getErrorMessage(ErrorKeys.USER_PAGE_SELECT_GROUP) + "\r\n";
        }

        if (!errors.equals("")) {
            throw new Exception(errors);
        }

        // Prepare groups related to the user
        // Delete
        if (user.getUserGroups() != null) {
            for (UserGroup userGroup : user.getUserGroups()) {
                boolean found = false;
                if (selectedGroupCodes != null) {
                    for (String groupId : selectedGroupCodes) {
                        if (userGroup.getGroupId().equalsIgnoreCase(groupId)) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    userGroup.setEntityAction(EntityAction.DELETE);
                }
            }
        }

        // Add
        if (selectedGroupCodes != null) {
            for (String groupId : selectedGroupCodes) {
                boolean found = false;
                if (user.getUserGroups() != null) {
                    for (UserGroup userGroup : user.getUserGroups()) {
                        if (userGroup.getGroupId().equalsIgnoreCase(groupId)) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    UserGroup userGroup = new UserGroup();
                    userGroup.setGroupId(groupId);
                    userGroup.setUserId(user.getId());
                    user.getUserGroups().add(userGroup);
                }
            }
        }
        
        // Prepare projects related to the user
        // Delete
        if (user.getProjects()!= null) {
            for (UserProject userProject : user.getProjects()) {
                boolean found = false;
                if (selectedProjects != null) {
                    for (String projectId : selectedProjects) {
                        if (userProject.getProjectId().equalsIgnoreCase(projectId)) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    userProject.setEntityAction(EntityAction.DELETE);
                }
            }
        }

        // Add
        if (selectedProjects != null) {
            for (String projectId : selectedProjects) {
                boolean found = false;
                if (user.getProjects()!= null) {
                    for (UserProject userProject : user.getProjects()) {
                        if (userProject.getProjectId().equalsIgnoreCase(projectId)) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    UserProject userProject = new UserProject();
                    userProject.setProjectId(projectId);
                    userProject.setUserId(user.getId());
                    user.getProjects().add(userProject);
                }
            }
        }
        
        if(StringUtility.isEmpty(user.getEmail())){
            user.setEmail(null);
        }
        if(StringUtility.isEmpty(user.getMobileNumber())){
            user.setMobileNumber(null);
        }
        if(StringUtility.isEmpty(user.getDescription())){
            user.setDescription(null);
        }
        if(StringUtility.isEmpty(user.getActivationCode())){
            user.setActivationCode(null);
        }

        String passwd = user.getPassword();
        
        adminEjb.saveUser(user);
        if (!StringUtility.empty(oldPassword).equals(passwd)) {
            adminEjb.changePassword(user.getUserName(), passwd);
        }
        search();
    }
}
