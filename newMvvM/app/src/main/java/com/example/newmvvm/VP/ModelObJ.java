package com.example.newmvvm.VP;

import com.example.newmvvm.R;

public enum ModelObJ
{
    RED(R.string.red, R.layout.view_red),
    BLUE(R.string.red, R.layout.view_blue),
    GREEN(R.string.red, R.layout.view_green),
    GREY(R.string.red, R.layout.view_grey);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObJ(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
