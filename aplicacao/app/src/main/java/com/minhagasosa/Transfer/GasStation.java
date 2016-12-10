package com.minhagasosa.Transfer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GasStation {

@SerializedName("name")
@Expose
private String name;
@SerializedName("city")
@Expose
private String city;
@SerializedName("state")
@Expose
private String state;
@SerializedName("rating")
@Expose
private Integer rating;
@SerializedName("comments")
@Expose
private List<String> comments;
@SerializedName("description")
@Expose
private String description;
@SerializedName("location")
@Expose
private Location location;

/**
* 
* @return
* The name
*/
public String getName() {
return name;
}

/**
* 
* @param name
* The name
*/
public void setName(String name) {
this.name = name;
}

/**
* 
* @return
* The city
*/
public String getCity() {
return city;
}

/**
* 
* @param city
* The city
*/
public void setCity(String city) {
this.city = city;
}

/**
* 
* @return
* The state
*/
public String getState() {
return state;
}

/**
* 
* @param state
* The state
*/
public void setState(String state) {
this.state = state;
}

/**
* 
* @return
* The rating
*/
public Integer getRating() {
return rating;
}

/**
* 
* @param rating
* The rating
*/
public void setRating(Integer rating) {
this.rating = rating;
}

/**
* 
* @return
* The comments
*/
public List<String> getComments() {
return comments;
}

/**
* 
* @param comments
* The comments
*/
public void setComments(List<String> comments) {
this.comments = comments;
}

/**
* 
* @return
* The description
*/
public String getDescription() {
return description;
}

/**
* 
* @param description
* The description
*/
public void setDescription(String description) {
this.description = description;
}

/**
* 
* @return
* The location
*/
public Location getLocation() {
return location;
}

/**
* 
* @param location
* The location
*/
public void setLocation(Location location) {
this.location = location;
}

}