package com.example.conference_infinity;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Root_Register_States {

    public static final int STATE_MAIL = 0;
    public static final int STATE_NICKNAME = 1;
    public static final int STATE_PASSWORD = 2;
    public static final int STATE_LANGUAGE = 3;
    public static final int STATE_THEME = 4;
    public static final int STATE_TOPIC = 5;
    public static final int STATE_DONE = 6;

    @IntDef({STATE_MAIL, STATE_NICKNAME, STATE_PASSWORD, STATE_LANGUAGE, STATE_THEME, STATE_TOPIC, STATE_DONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StateInt {
    }

    public int getFragmentNum(@Root_Register_States.StateInt int state) {
        return state;
    }
}
