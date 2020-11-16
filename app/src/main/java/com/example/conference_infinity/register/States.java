package com.example.conference_infinity.register;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class States {

    public static final int STATE_MAIL = 0;
    public static final int STATE_NICKNAME = 1;
    public static final int STATE_PASSWORD = 2;
    public static final int STATE_LANGUAGE = 3;
    public static final int STATE_THEME = 4;
    public static final int STATE_DENSITY = 5;
    public static final int STATE_TOPIC = 6;
    public static final int STATE_DONE = 7;

    @IntDef({STATE_MAIL, STATE_NICKNAME, STATE_PASSWORD, STATE_LANGUAGE, STATE_THEME, STATE_DENSITY, STATE_TOPIC, STATE_DONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StateInt {
    }

    public int getFragmentNum(@States.StateInt int state) {
        return state;
    }
}
