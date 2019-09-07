
package com.example.newmvvm.Move.Pro;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Link {

    @SerializedName("Response")
    private String mResponse;
    @SerializedName("Search")
    @Expose
    private List<Search> mSearch;
    @SerializedName("totalResults")
    private String mTotalResults;

    public String getResponse() {
        return mResponse;
    }

    public void setResponse(String response) {
        mResponse = response;
    }

    public List<Search> getSearch() {
        return mSearch;
    }

    public void setSearch(List<Search> search) {
        mSearch = search;
    }

    public String getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(String totalResults) {
        mTotalResults = totalResults;
    }

}
