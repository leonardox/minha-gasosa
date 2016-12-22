package com.minhagasosa.Transfer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "firstName","lastName","login","email","gender","address","city","state","country","phoneNumber","accountCreationDate","lastLogin","birthDate","fb_id"})

public class TUser {

    @SerializedName("firstName")
    @JsonProperty("firstName")
    @Expose
    private String firstName;

    @SerializedName("lastName")
    @JsonProperty("lastName")
    @Expose
    private String lastName;

    @SerializedName("login")
    @JsonProperty("login")
    @Expose
    private String login;

    @SerializedName("email")
    @JsonProperty("email")
    @Expose
    private String email;

    @SerializedName("gender")
    @JsonProperty("gender")
    @Expose
    private String gender;

    @SerializedName("address")
    @JsonProperty("address")
    @Expose
    private String address;

    @SerializedName("city")
    @JsonProperty("city")
    @Expose
    private String city;

    @SerializedName("state")
    @JsonProperty("state")
    @Expose
    private String state;

    @SerializedName("country")
    @JsonProperty("country")
    @Expose
    private String country;

    @SerializedName("phoneNumber")
    @JsonProperty("phoneNumber")
    @Expose
    private String phoneNumber;

    @SerializedName("lastLogin")
    @JsonProperty("lastLogin")
    @Expose
    private Integer lastLogin;

    @SerializedName("birthDate")
    @JsonProperty("birthDate")
    @Expose
    private Integer birthDate;

    @SerializedName("created_at")
    @JsonProperty("created_at")
    @Expose
    private Integer createdAt;

    @SerializedName("fb_id")
    @JsonProperty("fb_id")
    @Expose
    private String fbId;

    @SerializedName("google_id")
    @JsonProperty("google_id")
    @Expose
    private String googleId;

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("login")
    public String getLogin() {
        return login;
    }

    @JsonProperty("login")
    public void setLogin(String login) {
        this.login = login;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("phoneNumber")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty("lastLogin")
    public Integer getLastLogin() {
        return lastLogin;
    }

    @JsonProperty("lastLogin")
    public void setLastLogin(Integer lastLogin) {
        this.lastLogin = lastLogin;
    }

    @JsonProperty("birthDate")
    public Integer getBirthDate() {
        return birthDate;
    }

    @JsonProperty("birthDate")
    public void setBirthDate(Integer birthDate) {
        this.birthDate = birthDate;
    }

    @JsonProperty("created_at")
    public Integer getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("fb_id")
    public String getFbId() {
        return fbId;
    }

    @JsonProperty("fb_id")
    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    @JsonProperty("google_id")
    public String getGoogleId() {
        return googleId;
    }

    @JsonProperty("google_id")
    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

}
