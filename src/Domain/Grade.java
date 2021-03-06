package Domain;

public class Grade implements IdEntity<String>{

    private String studentId;
    private int homeworkId;
    private String id;
    private double grade;

    private int studGroup;
    private String studTeacher;
    private String studName;
    private String feedback;
    private int week;

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public Grade(String studentId, int homeworkId, double grade, String feedback){
        this.studentId = studentId;
        this.homeworkId = homeworkId;
        this.id = "" + studentId + homeworkId;
        this.grade = grade;
        this.feedback = feedback;
    }

    public String getStudId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(int homeworkId) {
        this.homeworkId = homeworkId;
    }



    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }



    @Override
    public void setId(String s) {
        this.id = s;
    }

    @Override
    public String getId() {
        return this.id;
    }


    public int getStudGroup() {
        return studGroup;
    }

    public void setStudGroup(int studGroup) {
        this.studGroup = studGroup;
    }

    public String getStudTeacher() {
        return studTeacher;
    }

    public void setStudTeacher(String studTeacher) {
        this.studTeacher = studTeacher;
    }

    public String getStudName() {
        return studName;
    }

    public void setStudName(String studName) {
        this.studName = studName;
    }


    public String getFeedback() {
        return this.feedback;
    }

    public void setFeedback(String feedback){
        this.feedback = feedback;
    }


}
