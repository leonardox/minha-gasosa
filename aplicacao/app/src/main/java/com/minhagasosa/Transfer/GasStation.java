package com.minhagasosa.Transfer;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GasStation implements Parcelable{

@SerializedName("_id")
@Expose
private String id;

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
@SerializedName("gasPrice")
@Expose
private float gasPrice;
@SerializedName("gasPlusPrice")
@Expose
private float gasPlusPrice;
@SerializedName("alcoolPrice")
@Expose
private float alcoolPrice;
@SerializedName("comments")
@Expose
private List<String> comments;
@SerializedName("description")
@Expose
private String description;
@SerializedName("location")
@Expose
private Location location;


public String getId() {
    return id;
}

public void setId(String id) {
    this.id = id;
}

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
* The Gas Price
*/
public float getGasPrice() {
        return gasPrice;
    }


/**
*
* @param gasPrice
* The Gas Price
*/
public void setGasPrice(float gasPrice) {
this.gasPrice = gasPrice;
}
/**
*
* @return
* The Gas Plus Price
*/
public float getGasPlusPrice() {
        return gasPrice;
    }


/**
*
* @param gasPlusPrice
* The Gas Plus Price
*/
public void setGasPlusPrice(float gasPlusPrice) {
this.gasPlusPrice = gasPlusPrice;
}

/**
*
* @return
* The Alcool Price
*/
public float getAlcoolPrice() {
        return alcoolPrice;
    }


/**
*
* @param alcoolPrice
* The Alcool Price
*/
public void setAlcoolPrice(float alcoolPrice) {
this.alcoolPrice = alcoolPrice;
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

    protected GasStation(Parcel in) {
        id = in.readString();
        name = in.readString();
        city = in.readString();
        state = in.readString();
        rating = in.readByte() == 0x00 ? null : in.readInt();
        gasPrice = in.readFloat();
        gasPlusPrice = in.readFloat();
        alcoolPrice = in.readFloat();
        if (in.readByte() == 0x01) {
            comments = new ArrayList<String>();
            in.readList(comments, String.class.getClassLoader());
        } else {
            comments = null;
        }
        description = in.readString();
        location = (Location) in.readValue(Location.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(city);
        dest.writeString(state);
        if (rating == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(rating);
        }
        dest.writeFloat(gasPrice);
        dest.writeFloat(gasPlusPrice);
        dest.writeFloat(alcoolPrice);
        if (comments == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(comments);
        }
        dest.writeString(description);
        dest.writeValue(location);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GasStation> CREATOR = new Parcelable.Creator<GasStation>() {
        @Override
        public GasStation createFromParcel(Parcel in) {
            return new GasStation(in);
        }

        @Override
        public GasStation[] newArray(int size) {
            return new GasStation[size];
        }
    };
}
