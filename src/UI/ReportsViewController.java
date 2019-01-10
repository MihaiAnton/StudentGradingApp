package UI;

import Service.ReportService;
import Service.SecurityService;
import Utils.Events.Event;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Map;

public class ReportsViewController extends TemplateController<String>{


    @FXML
    Text loginStatus;

    @FXML
    PieChart reportPC;

    @FXML
    Button infoBtn;

    @FXML
    TextField name;

    @FXML
    TextField locationTF;

    @FXML
    RadioButton studGrades, hardesHw, examableStuds, ontimeStuds, groupAvg;

    @FXML
    RadioButton txtRB, pdfRB;

    @FXML
    ChoiceBox<String> chartChoices;
    String previousChoice = "";




    private boolean showMoreActivated = false;
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
        initPieChart();
        initChartChoices();
    }

    private void initChartChoices() {
        this.chartChoices.getItems().add("Students passed");
        this.chartChoices.getItems().add("Grades distribution");
        this.chartChoices.getItems().add("Assignments per homework");

        this.chartChoices.setValue("Students passed");

        this.chartChoices.setOpacity(0);
        hideAdditionalInfo();

    }

    public void initPieChart(){
        reportPC.setOpacity(0);
    }

    @FXML
    public void handleChangeChart(){

        if(showMoreActivated){
            ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
            String s = chartChoices.getValue();

            if(s.equals(previousChoice)){
                return;
            }
            else{
                previousChoice = s;
            }

            if(s.equals("Students passed")){
                Map<String, Integer> passed = service.getPassedStatus();
                for (String key:passed.keySet()) {
                    list.add(new PieChart.Data(key, passed.get(key)));
                }
                list.add(new PieChart.Data("",0));
                updatePieChart("Students passed", list);
            }
            else if(s.equals("Grades distribution")){
                Map<String ,Integer> grades = service.getGradesDistribution();
                for(String key:grades.keySet()){
                    list.add(new PieChart.Data(key, grades.get(key)));
                }
                updatePieChart("Grade distribution", list);
            }
            else if(s.equals("Assignments per homework")){
                Map<String ,Integer> hwAssignment = service.getHomeworkAsignmentDistribution();
                for(String key:hwAssignment.keySet()){
                    list.add(new PieChart.Data(key, hwAssignment.get(key)));
                }
                updatePieChart("Homework Assignment", list);
            }
        }

    }


    public void updatePieChart(String title, ObservableList<PieChart.Data> list){

        reportPC.setOpacity(1);
        reportPC.getData().clear();

        double angle = Math.random()*360;
        reportPC.setStartAngle(angle);

        reportPC.getData().addAll(list);

        reportPC.setLabelsVisible(false);


        reportPC.setLegendSide(Side.LEFT);
        reportPC.setLegendVisible(true);

    }
    
    @FXML
    public void handleShowMore(){
        if(!showMoreActivated){
            this.infoBtn.setText("Show less");
            showAdditionalInfo();
            showMoreActivated = true;
            handleChangeChart();
        }
        else{
           this.infoBtn.setText("Show more");
           hideAdditionalInfo();
           showMoreActivated = false;
        }
    }

    private void hideAdditionalInfo() {
        this.reportPC.setOpacity(0);
        chartChoices.setOpacity(0);

    }

    private void showAdditionalInfo() {
        this.reportPC.setOpacity(1);
        chartChoices.setOpacity(1);

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


    public void onOpen() {
        if(showMoreActivated){
            handleShowMore();
        }
    }
}
