package com.example.pozx.myapplication;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/4/10.
 */

//数据库数据，可以用SimpleCursorAdpter

public class MySpAdapter extends SimpleAdapter {

    private int num;
    private int allCount;
    private boolean isAll=false;


    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public MySpAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        allCount=super.getCount();
    }



    @Override
    public int getCount()
    {
       if(isAll)
       {
           return allCount;
       }
        else {
           return num;
       }
    }

    public int getAllCount()
    {
        return allCount;
    }

    public void setGetAllCount(boolean t)
    {
        this.isAll=t;
    }

    public void setNum(int num)
    {
        this.num=num;
    }
}
