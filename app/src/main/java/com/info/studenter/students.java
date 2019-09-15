package com.info.studenter;



public class students {
    private String student_id;
    private String student_name;
    private String student_tel;

    public students() {
    }

    public students(String student_id, String student_name, String student_tel) {
        this.student_id = student_id;
        this.student_name = student_name;
        this.student_tel = student_tel;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_tel() {
        return student_tel;
    }

    public void setStudent_tel(String student_tel) {
        this.student_tel = student_tel;
    }
}
