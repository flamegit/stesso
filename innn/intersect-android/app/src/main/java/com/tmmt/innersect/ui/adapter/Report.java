package com.tmmt.innersect.ui.adapter;

/**
 * Created by flame on 2017/9/15.
 */

public interface Report{
    void itemDeleted(int position,boolean showDialog);
    void checkedChange(boolean isChecked);
    void sizeChange(int position,int newSize);
}
