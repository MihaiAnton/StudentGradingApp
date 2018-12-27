package UI;

import Service.ReportService;
import Service.SecurityService;
import Utils.Events.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ReportsViewController extends TemplateController<String>{


    @FXML
    Text loginStatus;

    @FXML
    TextField name;

    @FXML
    TextField locationTF;

    @FXML
    RadioButton studGrades, hardesHw, examableStuds, ontimeStuds, groupAvg;

    @FXML
    RadioButton txtRB, pdfRB;


    private ReportService reportsService;

    @Override
    protected void addThisToServiceList() {
        this.service.addObserver(this);
        this.securityService.addObserver(this);
    }

    private SecurityService securityService;

    public void setSecurityService(SecurityService securityService){
        this.securityService = securityService;
    }

    public void setReportsService(ReportService reportsService){
        this.reportsService = reportsService;
        this.locationTF.setText("C:\\Users\\mihai\\Desktop\\");
        this.name.setText("report");
        this.pdfRB.setSelected(true);
    }


    @Override
    public void updateFields(String newValue) {}

    @Override
    public String getEntityFromFields() {return null;}

    @Override
    public void populateList() {}

    @Override
    public void notify(Event event) {
        if(event.getEventType().equals("security")){
            this.loginStatus.setText(securityService.getLogStatus());
        }
    }

    @FXML
    public void handleGenerateReport(){

        int reportCode = 0;
        if(this.studGrades.isSelected()) reportCode |= 1;
        if(this.hardesHw.isSelected()) reportCode |= 2;
        if(this.examableStuds.isSelected()) reportCode |= 4;
        if(this.ontimeStuds.isSelected()) reportCode |= 8;
        if(this.groupAvg.isSelected()) reportCode |= 16;

        if(this.pdfRB.isSelected()) {
            try {
                this.reportsService.createPDFReport(name.getText() + ".pdf", locationTF.getText(), reportCode);
                reportConfirmation("Report '" + name.getText() + "' created successfully.");
            } catch (Exception e) {
                handleError("Error generating report.");
            }
        }
        else if(this.txtRB.isSelected()){
            try{
                this.reportsService.createTxtReport(name.getText() + ".txt",locationTF.getText(), reportCode);
            }
            catch(Exception e){
                handleError("Error generating report.");
            }
        }
    }

    private void reportConfirmation(String text){
        Alert msg = new Alert(Alert.AlertType.CONFIRMATION);
        msg.setTitle("Report confirmation.");
        msg.setContentText(text);
        msg.showAndWait();
    }

    @FXML
    public void txtRadioButtonSelect(){
        this.pdfRB.setSelected(false);
    }

    @FXML
    public void pdfRadioButtonSelect(){
        this.txtRB.setSelected(false);
    }


}
