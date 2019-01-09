package Service;

import Domain.Grade;
import Domain.Homework;
import Domain.Student;
import Exceptions.ServiceException;

import Repository.GenericHashMapRepository;

import Utils.Events.Event;
import Utils.Observable;
import Utils.Observer;
import Utils.Events.ServiceEvent;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TeacherService implements Observable<Event> {

    private GenericHashMapRepository<String, Student> studentRepo;
    private GenericHashMapRepository<Integer, Homework> homeworkRepo;
    private GenericHashMapRepository<String, Grade> gradeRepo;

    private String studentsPath = "M:\\School\\Metode Avansate de Programare\\StudentGradingApp\\src\\DataFiles\\Students\\";
    ArrayList<Observer> observers;
    private MailService mailService;

    public TeacherService(GenericHashMapRepository<String, Student> sRepo,
                          GenericHashMapRepository<Integer, Homework> hRepo,
                          GenericHashMapRepository<String, Grade> gradeRepo){
        studentRepo = sRepo;
        homeworkRepo = hRepo;
        this.gradeRepo = gradeRepo;
        this.observers = new ArrayList<>();
    }

    //Student functions

    /**
     *
     * @param student
     * @return
     * @throws RuntimeException
     */
    public Student addStudent(Student student){
        try{
            return studentRepo.save(student);
        }
        catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     *
     * @param id
     * @return
     * @throws RuntimeException
     */
    public Student findStudent(String id){
        try{
            return studentRepo.findOne(id);
        }
        catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     *
     * @param student
     * @return
     * @throws RuntimeException
     */
    public Student updateStudent(Student student){
        try{
            return studentRepo.update(student);
        }
        catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean hasGradeAssigned(String studentId){
        for (Grade g:this.gradeRepo.findAll()) {
            if(g.getStudId().equals(studentId))
                return true;
        }
        return false;
    }

    /**
     *
     * @param id
     * @return
     * @throws RuntimeException
     */
    public Student deleteStudent(String id){
        try{
            return studentRepo.delete(id);
        }
        catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //Homework functions

    /**
     *
     * @param homework
     * @return
     * @throws RuntimeException
     */
    public Homework addHomework(Homework homework){
        try{
            return homeworkRepo.save(homework);

        }
        catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     *
     * @param id
     * @return
     * @throws RuntimeException
     */
    public Homework findHomework(int id){
        try{
            return homeworkRepo.findOne(id);
        }
        catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     *
     * @param homework
     * @return
     * @throws RuntimeException
     */
    public Homework updateHomework(Homework homework){
        try{
            return homeworkRepo.update(homework);
        }
        catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


    //Grading methods


    /**
     *
     * @param studentId
     * @param homeworkId
     * @return return the homework if already existing, or null
     * @throws RuntimeException
     */
    public Homework addStudentHomework(String studentId, int homeworkId){
        try{
            if (hasHomework(studentId, homeworkId)) {
                return homeworkRepo.findOne(homeworkId);
            }
            else{
                Student student = studentRepo.findOne(studentId);
                Homework homework = homeworkRepo.findOne(homeworkId);
                student.addHomework(homework);
                return null;
            }

        }
        catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     *
     * @param studentId
     * @param homeworkId
     * @return true if has homework, false otherwise
     * @throws RuntimeException
     */
    public boolean hasHomework(String studentId, int homeworkId){


        //Predicate<Student> hasHomework = (s,h) -> s.findHomework()

        try{
            Student student = studentRepo.findOne(studentId);
            if(student == null){
                throw new Exception("Student " + studentId + " not found.");
            }
            Homework homework = homeworkRepo.findOne(homeworkId);
            if(homework == null){
                throw new Exception("Homework " + homeworkId + " not found.");
            }

            //Both student and homework found
            if(student.findHomework(homework.getId()) != null){
                return true;
            }
            return false;

        }
        catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public MailService getMailService(){
        return this.mailService;
    }

    /**
     *
     * @param studentId
     * @param homeworkId
     * @param grade
     * @param mailFlag
     * @throws RuntimeException
     */
    public void assignGrade(String studentId, int homeworkId, double grade, int assignmentWeek, String feedback, boolean mailFlag){


        try{
            addStudentHomework(studentId, homeworkId);
            if(grade < 0 || grade > 10.1){
                throw new Exception("Incorrect grade. Grade must be between 0 and 10.");
            }

            if(assignmentWeek!= -1 && (assignmentWeek < 1 || assignmentWeek > 14)){
                throw new Exception("Incorrect week. Week must be between 1 and 14.");
            }

            Student student = studentRepo.findOne(studentId);
            Homework h = student.findHomework(homeworkId);

            if(assignmentWeek >= h.getTargetWeek() + 2){
                throw new Exception("Cant't assign homework. More than 2 weeks passed.");
            }


            if(assignmentWeek == -1){
                assignmentWeek = h.getAssignmentWeek();
            }

            //h.setAssignmentWeek(assignmentWeek);
            //h.setGrade(grade);
            Grade grade1 = new Grade(studentId, homeworkId, h.getGrade(), feedback);
            grade1.setWeek(assignmentWeek);
            gradeRepo.save(grade1);

            // Print assignment
            String filePath = studentsPath + student.getId() + ".txt";
            String text = "Homework: " + h.getId() + "\n" +
                          "Description: " + h.getDescription() + "\n" +
                          "Assigned in week: " + h.getAssignmentWeek() + "\n"+
                          "Graded: " + grade1.getGrade() + "\n" +
                          "Deadline: " + h.getDeadlineWeek() + "\n"+
                          "Feedback: " + feedback + "\n\n";

            if(mailFlag){
                this.sendMail(grade1.getStudId(), text);
            }



            File f = new File(filePath);
            try{f.createNewFile();}
            catch (Exception e){}

            try{
                Files.write(Paths.get(filePath), text.getBytes() , StandardOpenOption.APPEND);
            }
            catch (Exception e){
            }


        }
        catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private void sendMail(String studId, String text) {
        Student s = studentRepo.findOne(studId);
        if(s == null){
            return;
        }
        else{
            //System.out.println("Before grade call");

            this.mailService.setMailData(s.getEmail(),"Grade notification",text);
            Thread mailThread = new Thread(this.mailService);
            mailThread.start();
            //System.out.println("after grade call");
        }
    }

    public void setMailService(MailService mailService){
        this.mailService = mailService;
    }


    /*
    ”Tema:” numărTema
o ”Predată în săptămâna:” nrSăptămânăPredat
o ”Deadline:” nrSăptămânăDeadline
o ”Feedback:” aprecieri, sugestii, precizări în legătură cu depunctările efectuate



     */
    /**
     *
     * @param studentId
     * @param homeworkId
     * @return the grade
     * @throws RuntimeException
     */
    public double getGrade(String studentId, int homeworkId){
        try {
            if(!hasHomework(studentId, homeworkId)){
                throw new Exception("Student " + studentId + " doesn't have the homework " + homeworkId + ".");
            }
            else{
                Homework h = studentRepo.findOne(studentId).findHomework(homeworkId);
                return h.getGrade();
            }
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getHomeworkList(){
       /* String list = "";

        for(Homework h : this.homeworkRepo.findAll()){
            list = list + "Homework " + h.getId() + ": " + h.getDescription() + ", Target week: " + h.getTargetWeek() + ", Deadline week: " + h.getDeadlineWeek() + "\n";
        }
        return list;
        */

        String s = homeworkRepo.getValues().stream().map(Object::toString).collect(Collectors.joining("\n"));


        return s;

    }

    /**
     *
     * @param studentId
     * @param homeworkId
     * @return
     */
    public String delayStudentHomework(String studentId, int homeworkId){
        try{

            Student student = studentRepo.findOne(studentId);
            if(student == null){
                return("Inexistent student.");
            }
            else{
                Homework homework = homeworkRepo.findOne(homeworkId);
                if(homework == null){
                    return("Inexistent homework.");
                }

                if(student.findHomework(homeworkId) == null){
                    return("Student " + studentId + " doesn't have homework " + homeworkId + " assigned.");
                }

                student.delayHomework(homeworkId);
            }
        }
        catch(Exception e){
            return e.getMessage();
        }

        return("Homework delayed.");
    }




    public Iterable<Student> getStudents() {
        return this.studentRepo.findAll();
    }

    public Homework deleteHomework(Homework hw) {
        return this.homeworkRepo.delete(hw.getId());
    }

    public Iterable<Homework> getHomeworkIterable() {
        return this.homeworkRepo.findAll();
    }

    public Iterable<Grade> getAllGrades(){
        return this.gradeRepo.findAll();
    }


    @Override
    public void addObserver(Observer<Event> observer) {
        this.observers.add(observer);
    }




    @Override
    public void removeObserver(Observer<Event> observer) {
        try{
            this.observers.remove(observer);
        }
        catch(Exception e){
            throw new ServiceException("Could not remove observer.");
        }
    }

    @Override
    public void notifyObserver(Event event) {
        for (Observer obs:this.observers       ) {
            obs.notify(event);
        }
    }

    public Grade findGrade(String s, int hid) {
        return gradeRepo.findOne(s+hid);
    }


    //chart reports
    public Map<String, Integer> getPassedStatus() {

        int passed = 0;
        int notPassed = 0;
        Map<String,Integer> passedStatus = new HashMap<>();
        for (Student student:this.studentRepo.findAll()) {

            double sum = 0;
            int count = 0;

            for (Grade g:gradeRepo.findAll()) {
                if(g.getStudId().equals(student.getId())){
                    Homework h = homeworkRepo.findOne(g.getHomeworkId());
                    int proportion = h.getDeadlineWeek() - h.getTargetWeek() + 1;
                    count += proportion;
                    sum += (g.getGrade() * proportion);

                }
            }

            if(sum / count > 5){
                passed++;
            }
            else{
                notPassed++;
            }
        }

        passedStatus.put("Passed", passed);
        passedStatus.put("Not passed",notPassed);


        return passedStatus;
    }
}


































