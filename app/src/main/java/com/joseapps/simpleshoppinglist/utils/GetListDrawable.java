package com.joseapps.simpleshoppinglist.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.joseapps.simpleshoppinglist.R;

class GetListDrawable {

    public static Drawable getDrawable(String tvListName, Context mContext){
        Drawable color;
        switch (tvListName){
            case Const.sWalmart:
                color = mContext.getResources().getDrawable(R.drawable.background_leftround_walmart);
                break;
            case Const.sCostco :
                color = mContext.getResources().getDrawable(R.drawable.background_leftround_costco);
                break;
            case Const.sZhers :
                color = mContext.getResources().getDrawable(R.drawable.background_leftround_zhers);
                break;

            case Const.sMetro :
                color = mContext.getResources().getDrawable(R.drawable.background_leftround_metro);
                break;
            default:
                color = mContext.getResources().getDrawable(R.drawable.background_leftround_custom);
                break;
        }
        return color;
    }
}
