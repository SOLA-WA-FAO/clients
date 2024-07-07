package org.fao.sola.admin.web.beans.report;

import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.lingala.zip4j.core.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.fao.sola.admin.web.beans.AbstractBackingBean;
import org.fao.sola.admin.web.beans.helpers.ErrorKeys;
import org.fao.sola.admin.web.beans.helpers.MessageBean;
import org.fao.sola.admin.web.beans.helpers.MessageProvider;
import org.fao.sola.admin.web.beans.language.LanguageBean;
import org.fao.sola.admin.web.beans.localization.LocalizedValuesListBean;
import org.sola.admin.services.ejb.refdata.businesslogic.RefDataAdminEJBLocal;
import org.sola.admin.services.ejb.refdata.entities.ReportGroup;
import org.sola.common.StringUtility;
import org.sola.services.common.EntityAction;
import org.sola.admin.services.ejb.system.businesslogic.SystemAdminEJBLocal;
import org.sola.admin.services.ejb.system.repository.entities.ReportDescription;
import org.sola.admin.services.ejb.system.repository.entities.Setting;
import org.sola.common.ConfigConstants;

/**
 * Contains methods and properties to manage {@link Setting}
 */
@Named
@ViewScoped
public class ReportsPageBean extends AbstractBackingBean {

    private ReportDescription report;
    private List<ReportDescription> reports;
    private ReportGroup[] reportGroups;
    private LocalizedValuesListBean localizedDisplayValues;
    private Part reportFile;

    @Inject
    MessageBean msg;

    @Inject
    MessageProvider msgProvider;

    @Inject
    private LanguageBean languageBean;

    @EJB
    SystemAdminEJBLocal systemEjb;

    @EJB
    RefDataAdminEJBLocal refEjb;

    public ReportDescription getReport() {
        return report;
    }

    public List<ReportDescription> getReports() {
        return reports;
    }

    @PostConstruct
    private void init() {
        loadList();
        loadReportGroups();
    }

    public ReportGroup[] getReportGroups() {
        return reportGroups;
    }

    private void loadReportGroups() {
        List<ReportGroup> reportGroupsList = refEjb.getCodeEntityList(ReportGroup.class, languageBean.getLocale());
        if (reportGroupsList != null && !reportGroupsList.isEmpty()) {
            ReportGroup dummy = new ReportGroup();
            dummy.setCode("");
            dummy.setDisplayValue(" ");
            reportGroupsList.add(0, dummy);
            reportGroups = reportGroupsList.toArray(new ReportGroup[reportGroupsList.size()]);
        } else {
            reportGroups = new ReportGroup[]{};
        }
    }

    public String getReportGroupName(String code) {
        if (!StringUtility.isEmpty(code) && reportGroups != null && reportGroups.length > 0) {
            for (ReportGroup reportGroup : reportGroups) {
                if (reportGroup.getCode().equalsIgnoreCase(code)) {
                    return reportGroup.getDisplayValue();
                }
            }
        }
        return "";
    }

    private void loadList() {
        reports = systemEjb.getAllReports(languageBean.getLocale());
    }

    public void loadReport(String id) {
        if (StringUtility.isEmpty(id)) {
            report = new ReportDescription();
            report.setId("");
            report.setDisplayInMenu(Boolean.TRUE);
            report.setDisplayName("");
        } else {
            report = systemEjb.getReportById(id, null);
        }
        localizedDisplayValues = new LocalizedValuesListBean(languageBean);
        localizedDisplayValues.loadLocalizedValues(report.getDisplayName());
    }

    public void deleteReport(ReportDescription report) {
        report.setEntityAction(EntityAction.DELETE);
        systemEjb.saveReport(report);

        // Delete folder
        String reportsFolderPath = getReportsFolderPath();
        if (!StringUtility.isEmpty(reportsFolderPath)) {
            File subFolder = new File(reportsFolderPath + File.separator + report.getId());
            if (subFolder.exists()) {
                try {
                    FileUtils.deleteDirectory(subFolder);
                } catch (IOException ex) {
                }
            }
        }
        loadList();
    }

    public void saveReport() throws Exception {
        if (report != null) {
            // Validate
            String errors = "";
            String reportsFolderPath = "";
            String fileExt = "";
            String fileName = "";
            String fileBaseName = "";

            if (reportFile != null) {
                fileName = reportFile.getSubmittedFileName();
                fileBaseName = FilenameUtils.getBaseName(reportFile.getSubmittedFileName());
                fileExt = FilenameUtils.getExtension(fileName);
            }
            // Check ID exists
            if (report.isNew() && !StringUtility.isEmpty(report.getId()) && reports != null) {
                for (ReportDescription r : reports) {
                    if (r.getId().equalsIgnoreCase(report.getId())) {
                        errors += msgProvider.getErrorMessage(ErrorKeys.REPORTS_ID_EXISTS) + "\r\n";
                        break;
                    }
                }
            }
            if (StringUtility.isEmpty(report.getId())) {
                errors += "- " + msgProvider.getErrorMessage(ErrorKeys.REPORTS_FILL_ID) + "\r\n";
            }
            if (localizedDisplayValues.getLocalizedValues() == null || localizedDisplayValues.getLocalizedValues().size() < 1
                    || StringUtility.isEmpty(localizedDisplayValues.getLocalizedValues().get(0).getLocalizedValue())) {
                errors += "- " + msgProvider.getErrorMessage(ErrorKeys.REPORTS_FILL_DISPLAY_NAME) + "\r\n";
            }
            if (report.isNew() && reportFile == null) {
                errors += "- " + msgProvider.getErrorMessage(ErrorKeys.REPORTS_SELECT_FILE) + "\r\n";
            }
            if (reportFile != null && !fileExt.endsWith("zip") && !fileExt.endsWith("jasper")) {
                errors += "- " + msgProvider.getErrorMessage(ErrorKeys.REPORTS_WRONG_FILE_FORMAT) + "\r\n";
            }
            if (reportFile != null) {
                reportsFolderPath = getReportsFolderPath();
                if (StringUtility.isEmpty(reportsFolderPath)) {
                    errors += "- " + msgProvider.getErrorMessage(ErrorKeys.REPORTS_FOLDER_PATH_NOT_FOUND) + "\r\n";
                }
            }

            if (!errors.equals("")) {
                throw new Exception(errors);
            }

            // Save file if provided
            if (reportFile != null) {

                // Create report sub-folder
                File subFolder = new File(reportsFolderPath + File.separator + report.getId());
                if (!subFolder.exists()) {
                    subFolder.mkdirs();
                }

                // Copy report file
                Path reportFilePath = Paths.get(subFolder.getPath(), fileName);
                try (InputStream input = reportFile.getInputStream()) {
                    Files.copy(input, reportFilePath, StandardCopyOption.REPLACE_EXISTING);
                }

                // Handle zip file
                if (fileExt.endsWith("zip")) {
                    ZipFile zipFile = new ZipFile(reportFilePath.toFile());
                    zipFile.extractAll(subFolder.getPath());

                    // Delete zip file
                    reportFilePath.toFile().delete();

                    // Check if there is jasper file matching zip file name
                    File[] files = subFolder.listFiles();
                    boolean found = false;

                    if (files != null) {
                        for (File f : files) {
                            if (f.isFile()) {
                                String fBaseName = FilenameUtils.getBaseName(f.getName());
                                if (fBaseName.equals(fileBaseName)) {
                                    if (FilenameUtils.getExtension(f.getName()).equalsIgnoreCase("jasper")) {
                                        found = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    if (!found) {
                        errors += "- " + String.format(msgProvider.getErrorMessage(ErrorKeys.REPORTS_JASPER_FILE_NOT_FOUND), fileBaseName + ".jasper") + "\r\n";
                        throw new Exception(errors);
                    }
                }

                report.setFileName(fileBaseName + ".jasper");
            }

            report.setDisplayName(localizedDisplayValues.buildMultilingualString());
            systemEjb.saveReport(report);
            loadList();
        }
    }

    private String getReportsFolderPath() {
        String reportsFolderPath = systemEjb.getSetting(ConfigConstants.REPORTS_FOLDER_PATH, "");
        if (StringUtility.isEmpty(reportsFolderPath)) {
            return "";
        }

        // Remove ending "/" or "\"
        if ((reportsFolderPath.lastIndexOf("/") == reportsFolderPath.length() - 1)
                || (reportsFolderPath.lastIndexOf("\\") == reportsFolderPath.length() - 1)) {
            reportsFolderPath = reportsFolderPath.substring(0, reportsFolderPath.length() - 1);
        }

        // Check if the path is absolute
        Pattern pattern = Pattern.compile(reportsFolderPath, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher("^([\\/])|([a-zA-Z]:)");

        if (!matcher.find()) {
            // Path is reletive. Let's append it to the application path
            Path path = Paths.get(getExtContext().getRealPath("/"), reportsFolderPath);
            reportsFolderPath = path.toString();
        }

        return reportsFolderPath;
    }

    public Part getReportFile() {
        return reportFile;
    }

    public void setReportFile(Part reportFile) {
        this.reportFile = reportFile;
    }

    public LocalizedValuesListBean getLocalizedDisplayValues() {
        return localizedDisplayValues;
    }

    public void setLocalizedDisplayValues(LocalizedValuesListBean localizedDisplayValues) {
        this.localizedDisplayValues = localizedDisplayValues;
    }
}
