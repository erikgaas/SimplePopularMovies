package com.gaas.erik.simplepopularmovies.models;

/**
 * Created by erik on 8/21/15.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Trailer implements Parcelable {

    private int id;
    @SerializedName("results")
    private List<TrailerResult> trailerResults = new ArrayList<TrailerResult>();
    //private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Trailer() {

    }

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The results
     */
    public List<TrailerResult> getTrailerResults() {
        return trailerResults;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<TrailerResult> trailerResults) {
        this.trailerResults = trailerResults;
    }

/*    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }*/


    protected Trailer(Parcel in) {
        id = in.readInt();
        if (in.readByte() == 0x01) {
            trailerResults = new ArrayList<TrailerResult>();
            in.readList(trailerResults, TrailerResult.class.getClassLoader());
        } else {
            trailerResults = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        if (trailerResults == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(trailerResults);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}