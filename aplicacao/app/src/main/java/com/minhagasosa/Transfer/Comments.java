package com.minhagasosa.Transfer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comments {

@SerializedName("text")
@Expose
private String text;
@SerializedName("author")
@Expose
private TUser author;
@SerializedName("creationDate")
@Expose
private String creationDate;
@SerializedName("thumbsUp")
@Expose
private Integer thumbsUp;

/**
* 
* @return
* The text
*/
public String getText() {
return text;
}

/**
* 
* @param text
* The text
*/
public void setText(String text) {
this.text = text;
}

/**
* 
* @return
* The author
*/
public TUser getAuthor() {
return author;
}

/**
* 
* @param author
* The author
*/
public void setAuthor(TUser author) {
this.author = author;
}

/**
* 
* @return
* The creationDate
*/
public String getCreationDate() {
return creationDate;
}

/**
* 
* @param creationDate
* The creationDate
*/
public void setCreationDate(String creationDate) {
this.creationDate = creationDate;
}

/**
* 
* @return
* The thumbsUp
*/
public Integer getThumbsUp() {
return thumbsUp;
}

/**
* 
* @param thumbsUp
* The thumbsUp
*/
public void setThumbsUp(Integer thumbsUp) {
this.thumbsUp = thumbsUp;
}

}