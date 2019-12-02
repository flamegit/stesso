package com.tmmt.innersect.mvp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flame on 2017/8/24.
 */

public class AfterInfo {

    public String phone;
    public List<Content> contents;

    public static class Content{
        public ArrayList<Problem> contents;
        public String title;
    }
}
