package org.fao.sola.admin.web.beans.log;

import java.util.List;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.fao.sola.admin.web.beans.AbstractBackingBean;
import org.fao.sola.admin.web.beans.helpers.MessageBean;
import org.fao.sola.admin.web.beans.helpers.MessageProvider;
import org.fao.sola.admin.web.beans.language.LanguageBean;
import org.sola.services.common.EntityAction;
import org.sola.admin.services.ejb.system.businesslogic.SystemAdminEJBLocal;
import org.sola.admin.services.ejb.system.repository.entities.EmailTask;

/**
 * Contains methods to manage mail messages queue
 */
@Named
@ViewScoped
public class MailQueuePageBean extends AbstractBackingBean {

    @EJB
    SystemAdminEJBLocal systemEjb;

    @Inject
    MessageProvider msgProvider;

    @Inject
    LanguageBean langBean;

    @Inject
    MessageBean msgBean;

    private EmailTask[] emails;

    public EmailTask[] getEmails() {
        return emails;
    }

    public void init() {
        refresh();
    }

    public void refresh() {
        List<EmailTask> emailsList = systemEjb.getEmails();
        if (emailsList != null) {
            emails = emailsList.toArray(new EmailTask[emailsList.size()]);
        }
    }

    public void deleteEmail(EmailTask email) {
        try {
            if (email == null) {
                return;
            }
            email.setEntityAction(EntityAction.DELETE);
            systemEjb.saveEmailTask(email);
            refresh();
        } catch (Exception e) {
            msgBean.setErrorMessage(e.getMessage());
        }
    }
}
