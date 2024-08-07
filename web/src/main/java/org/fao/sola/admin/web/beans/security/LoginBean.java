package org.fao.sola.admin.web.beans.security;

import java.io.IOException;
import java.io.Serializable;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.fao.sola.admin.web.beans.helpers.ErrorKeys;
import org.fao.sola.admin.web.beans.helpers.MessageProvider;
import org.fao.sola.admin.web.beans.system.SystemBean;
import org.sola.admin.services.ejbs.admin.businesslogic.AdministratorEJBLocal;

@Named
@RequestScoped
public class LoginBean implements Serializable {

    @Inject
    MessageProvider msgProvider;
    
    @Inject
    SystemBean systemBean;
            
    @Inject
    ActiveUserBean activeUserBean;
    
    @EJB
    AdministratorEJBLocal admin;
       
    public ActiveUserBean getActiveUserBean(){
        return activeUserBean;
    }
     
    public void setActiveUserBean(ActiveUserBean activeUser){
        this.activeUserBean = activeUser;
    }
     
    public void login() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        if(!validate(context, true)){
            return;
        }
        
        try {
            request.login(activeUserBean.getUserName(), activeUserBean.getPassword());
            // Check user is active
            if(!admin.isUserActive(activeUserBean.getUserName())){
                // Log out user
                request.logout();
                context.addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.LOGIN_ACCOUNT_BLOCKED)));
                return;
            }
            // Check user has administrative rights
            if(!admin.isUserAdmin()){
                // Log out user
                request.logout();
                context.addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.LOGIN_NO_ADMIN_RIGHTS)));
                return;
            }
             
            // Init system settings
            systemBean.init();
            // Redirect
            context.getExternalContext().redirect(request.getContextPath() + "/index.xhtml");
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.LOGIN_FAILED)));
        }
    }
    
    private boolean validate(FacesContext context, boolean showMessage){
        boolean isValid = true;
        
        // TODO: ADD USER NAME AND PASSWORD VALIDATION
        
        return isValid;
    }
    
    public void logout() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
            // Flush system settings from session
            systemBean.flush();
            // Redirect
            context.getExternalContext().redirect(request.getContextPath() + "/login.xhtml");
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.LOGIN_LOGOUT_FAILED)));
        }
    }
}
