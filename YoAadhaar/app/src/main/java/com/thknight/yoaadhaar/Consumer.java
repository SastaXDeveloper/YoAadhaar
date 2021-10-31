package com.thknight.yoaadhaar;

public class Consumer {
    private String name,father,dob,pincode,dist,street,post,state,subdist,vill;
    public Consumer(String name, String father, String dob, String pincode, String dist, String street, String post, String state, String subdist, String vill) {
        this.name = name;
        this.father = father;
        this.dob = dob;
        this.pincode = pincode;
        this.dist = dist;
        this.street = street;
        this.post = post;
        this.state = state;
        this.subdist = subdist;
        this.vill = vill;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSubdist() {
        return subdist;
    }

    public void setSubdist(String subdist) {
        this.subdist = subdist;
    }

    public String getVill() {
        return vill;
    }

    public void setVill(String vill) {
        this.vill = vill;
    }




}
