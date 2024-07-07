package org.fao.sola.admin.web.beans.project;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import org.fao.sola.admin.web.beans.AbstractBackingBean;
import org.fao.sola.admin.web.beans.language.LanguageBean;
import org.sola.admin.services.ejb.search.businesslogic.SearchAdminEJBLocal;
import org.sola.admin.services.ejb.search.repository.entities.ProjectSearchResult;
import org.sola.admin.services.ejb.system.businesslogic.SystemAdminEJBLocal;
import org.sola.admin.services.ejb.system.repository.entities.Project;
import org.sola.services.common.EntityAction;
import org.sola.services.common.LocalInfo;

/**
 * Contains methods and properties to manage {@link Project}
 */
@Named
@ViewScoped
public class ProjectsPageBean extends AbstractBackingBean {

    private List<ProjectSearchResult> projects;

    @Inject
    private LanguageBean languageBean;

    @EJB
    private SearchAdminEJBLocal searchEjb;

    @EJB
    SystemAdminEJBLocal systemEjb;

    public List<ProjectSearchResult> getProjects() {
        return projects;
    }

    @PostConstruct
    private void init() {
        loadList();
    }

    private void loadList() {
        projects = searchEjb.getAllProjects(languageBean.getLocale());
    }

    public void deleteProject(String id) {
        final Project project = searchEjb.getEntityById(Project.class, id);
        project.setEntityAction(EntityAction.DELETE);
        systemEjb.saveProject(project);
        loadList();
    }
}
