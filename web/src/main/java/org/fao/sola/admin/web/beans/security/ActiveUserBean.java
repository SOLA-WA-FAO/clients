package org.fao.sola.admin.web.beans.security;

import java.io.Serializable;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.sola.admin.services.ejbs.admin.businesslogic.AdministratorEJBLocal;

/**
 * Stores user information
 */
@SessionScoped
@Named
public class ActiveUserBean implements Serializable {

    @EJB
    AdministratorEJBLocal adminEjb;
    
    private String username;
    private String password;
    
    public String getUserName() {
        return this.username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isAuthenticated(){
        String userName = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        return userName != null && !userName.equals("");
    }
 
    /**
     * Creates a new instance of ActiveUser bean
     */
    public ActiveUserBean() {
    }

    public boolean getHasAdminRights(){
        return adminEjb.isUserAdmin();
    }
}
