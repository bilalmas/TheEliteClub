package com.bilalmas.eliteclub;

public class Users {
    private String username;
    private String bio;
    private String interests;
    private String image;

    public Users(){

    }

    public Users(String username){
        this.username = username;
        this.bio = "Edit to Add details";
        this.interests = "Edit to Add details";

    }

    public Users(String username, String bio,String interests ) {
        this.username = username;
        this.bio = bio;
        this.interests = interests;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
