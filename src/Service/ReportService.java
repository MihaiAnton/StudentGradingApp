package Service;

import Domain.Grade;
import Domain.Homework;

import Domain.Student;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;


import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;


public class ReportService {

    private TeacherService teacherService;

    public ReportService(TeacherService teacherService){
        this.teacherService = teacherService;
    }


    public List<String> reportStudentsAverage(){

        HashMap<String, List<Grade>> grades = new HashMap<>();
        ArrayList<String> result = new ArrayList<>();

        for (Grade g:teacherService.getAllGrades()) {
            List list = grades.get(g.getStudId());
            if(list == null){
                list = new ArrayList();
            }

            list.add(g);
            grades.put(g.getStudId(),list);
        }

        for(String key: grades.keySet()){
            String studentName = teacherService.findStudent(key).getName();
            String studentId = key;

            double sum = 0;
            int count = 0;

            //compute the average for the student's homework
            for (Grade g:grades.get(key)) {
                Homework h = teacherService.findHomework(g.getHomeworkId());
                int ponder = h.getDeadlineWeek() - h.getTargetWeek() + 1;
                sum = sum + g.getGrade()*ponder;
                count = count + ponder;
            }

            String rez = "Id: " + studentId + " Name: " + studentName + " - average grade = " + sum/count;
            result.add(rez);
        }
        return result;
    }


    public String reportHarderstHomework(){

        Set<Integer> gradedHw  = new HashSet<>();

        for (Grade g:teacherService.getAllGrades() ) {
            gradedHw.add(g.getHomeworkId());
        }

        int hardestHw = -1;
        double lowestAvg = -1;

        for (int hid:gradedHw) {
            double sum = 0;
            int count = 0;
            for (Grade g:teacherService.getAllGrades()) {
                if(g.getHomeworkId() == hid){
                    sum +=  g.getGrade();
                    count++;
                }
            }
            double avg = sum/count;

            if(hardestHw == -1 || avg < lowestAvg){
                hardestHw = hid;
                lowestAvg = avg;
            }
        }

        Homework hardest = teacherService.findHomework(hardestHw);
        return "Hardest homework: " + hardest.getId() + " " + hardest.getDescription() + ". Average grade: " + lowestAvg;
    }



    public List<String> reportExamAbleStudents(){

        List<String> students = new ArrayList<>();

        for (Student s:teacherService.getStudents()) {
            boolean examAble = true;
            double avg = getStudentAvg(s);

            if(avg == 0){
                students.add("Student " + s.getId() + " " + s.getName() + " - no grade assigned yet.");
            }
            else if(avg > 4){
                students.add("Student " + s.getId() + " " + s.getName() + " - can enter exam.");
            }
        }
        return students;
    }

    private double getStudentAvg(Student s) {

        double sum = 0;
        int count = 0;

        for (Grade g:teacherService.getAllGrades()) {
            if(g.getStudId().equals(s.getId())){

                Homework h = teacherService.findHomework(g.getHomeworkId());
                int ponder = h.getDeadlineWeek() - h.getTargetWeek() + 1;
                sum += ponder*g.getGrade();
                count += ponder;
            }
        }

        if(count == 0){
            return 0;
        }
        else{
            return sum/count;
        }

    }

    public List<String> reportOnTimeStudents(){

        Set<String> studs = new HashSet<>();
        List<String> result = new ArrayList<>();

        for (Grade g:teacherService.getAllGrades()) {
            studs.add(g.getStudId());
        }


        for (Grade g:teacherService.getAllGrades()) {
            Homework h = teacherService.findHomework(g.getHomeworkId());
            if(g.getWeek() > h.getTargetWeek()){
                if(studs.contains(g.getStudId())){
                    studs.remove(g.getStudId());
                }
            }
        }

        for (String id:studs) {
            Student s = teacherService.findStudent(id);
            result.add("Student: " + s.getId() + " name: " + s.getName());

        }


       return result;
    }

    public void createPDFReport(String name, String dest){
        try {
            Document document = new Document();
            PdfWriter.getInstance(document,new FileOutputStream("test.pdf"));
            document.open();

            document.add(new Paragraph("\n                      Student report:\n\n"));

            document.add(new Paragraph("\n\nAverage grades:\n\n"));
            for (String s:reportStudentsAverage()) {
                document.add(new Paragraph(s));
            }

            document.add(new Paragraph("\n\nHardest homework:\n\n"));
            document.add(new Paragraph(this.reportHarderstHomework()));


            document.add(new Paragraph("\n\nStudents able to take the exam:\n\n"));
            for (String s:reportExamAbleStudents()) {
                document.add(new Paragraph(s));
            }

            document.add(new Paragraph("\n\nStudents that assigned the homework on time:\n\n"));
            for (String s:reportOnTimeStudents()) {
                document.add(new Paragraph(s));
            }
            document.close();


        }
        catch(Exception e){

        }
    }




}


















