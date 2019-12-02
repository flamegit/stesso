package com.tmmt.innersect.mvp.model;

import java.util.List;

/**
 * Created by flame on 2018/1/22.
 */

public class LogisticsInfo {

    public List<Exps> exps;
    public String tips;

    public static class Exps {
        public String code;
        String expn;
        public String name;
    }

}
