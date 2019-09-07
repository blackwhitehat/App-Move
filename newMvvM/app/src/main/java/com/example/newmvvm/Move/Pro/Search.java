
package com.example.newmvvm.Move.Pro;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Search {

    @SerializedName("imdbID")
    private String mImdbID;
    @SerializedName("Poster")
    private String mPoster;
    @SerializedName("Title")
    private String mTitle;
    @SerializedName("Type")
    private String mType;
    @SerializedName("Year")
    private String mYear;

    public String getImdbID() {
        return mImdbID;
    }

    public void setImdbID(String imdbID) {
        mImdbID = imdbID;
    }

    public String getPoster() {
        return mPoster;
    }

    public void setPoster(String poster) {
        mPoster = poster;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getYear() {
        return mYear;
    }

    public void setYear(String year) {
        mYear = year;
    }

}
