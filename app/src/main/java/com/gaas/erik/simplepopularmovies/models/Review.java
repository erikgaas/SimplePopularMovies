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
public class Review implements Parcelable {

    private int id;
    private int page;
    @SerializedName("results")
    private List<ReviewResult> reviewResults = new ArrayList<ReviewResult>();
    private int totalPages;
    private int totalResults;
    //private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Review (){

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
     * The page
     */
    public int getPage() {
        return page;
    }

    /**
     *
     * @param page
     * The page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     *
     * @return
     * The results
     */
    public List<ReviewResult> getResults() {
        return reviewResults;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<ReviewResult> reviewResults) {
        this.reviewResults = reviewResults;
    }

    /**
     *
     * @return
     * The totalPages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     *
     * @param totalPages
     * The total_pages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     *
     * @return
     * The totalResults
     */
    public int getTotalResults() {
        return totalResults;
    }

    /**
     *
     * @param totalResults
     * The total_results
     */
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

/*    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }*/


    protected Review(Parcel in) {
        id = in.readInt();
        page = in.readInt();
        if (in.readByte() == 0x01) {
            reviewResults = new ArrayList<ReviewResult>();
            in.readList(reviewResults, ReviewResult.class.getClassLoader());
        } else {
            reviewResults = null;
        }
        totalPages = in.readInt();
        totalResults = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(page);
        if (reviewResults == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(reviewResults);
        }
        dest.writeInt(totalPages);
        dest.writeInt(totalResults);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}