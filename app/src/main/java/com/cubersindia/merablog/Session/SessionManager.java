package com.cubersindia.merablog.Session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    Context context;
    public SessionManager(Context context){
        this.context = context;
    }

    public void saveUserId(String userId) {
        SharedPreferences preferences = context.getSharedPreferences("Login_details",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USER_ID",userId);
        editor.commit();
    }

    public String getUserId() {
        SharedPreferences preferences = context.getSharedPreferences("Login_details",Context.MODE_PRIVATE);
        return preferences.getString("USER_ID","null");
    }


    public void logoutUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Login_details",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public boolean isUserLoggedOut(){
        SharedPreferences preferences = context.getSharedPreferences("Login_details",context.MODE_PRIVATE);
        boolean isUserIdEmpty = preferences.getString("USER_ID","").isEmpty();
        return  isUserIdEmpty;
    }
}
