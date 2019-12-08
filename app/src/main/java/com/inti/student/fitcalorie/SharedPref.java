package com.inti.student.fitcalorie;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences mSharedPreference;

    public SharedPref(Context context){
        mSharedPreference = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }

    public void setNightModeState(Boolean state){
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putBoolean("NightMode", state);
        editor.commit();
    }

    public Boolean loadNightModeState(){
        Boolean state = mSharedPreference.getBoolean("NightMode", false);
        return state;
    }

}
