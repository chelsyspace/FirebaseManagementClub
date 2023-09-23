package com.example.firebaseclubmanagement_a187991.Objects;

public class Student {

    private String studentID;
    private String studentMatricNo;
    private String studentName;
    private String studentClub;
    private String studentEmail;
    private Boolean studentClubActiveStatus;

    public Student(){}

    public Student(String id, String matric, String name, String club, Boolean status){}
    public Student(String studentID, String studentMatricNo, String studentName, String studentEmail, String studentClub, Boolean studentClubActiveStatus) {
        this.studentID = studentID;
        this.studentMatricNo = studentMatricNo;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentClub = studentClub;
        this.studentClubActiveStatus = studentClubActiveStatus;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentMatricNo() {
        return studentMatricNo;
    }

    public void setStudentMatricNo(String studentMatricNo) {
        this.studentMatricNo = studentMatricNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentClub() {
        return studentClub;
    }

    public void setStudentClub(String studentClub) {
        this.studentClub = studentClub;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public Boolean getStudentClubActiveStatus() {
        return studentClubActiveStatus;
    }

    public void setStudentClubActiveStatus(Boolean studentClubActiveStatus) {
        this.studentClubActiveStatus = studentClubActiveStatus;
    }
}
