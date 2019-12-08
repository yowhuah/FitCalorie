package com.inti.student.fitcalorie;

public class Member {

    public String username, email, gender;
    public Integer age;
    public Double jobLevel;
    public Float hts, wts;

    public Member(){}

    public Member(String username, String email, String gender, Integer age, Double jobLevel, Float hts, Float wts){
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.jobLevel = jobLevel;
        this.hts = hts;
        this.wts = wts;
    }

}
