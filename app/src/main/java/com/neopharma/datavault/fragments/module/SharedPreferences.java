package com.neopharma.datavault.fragments.module;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.neopharma.datavault.utility.Store;
import com.securepreferences.SecurePreferences;

import static com.neopharma.datavault.model.Constants.Preference.PREFERENCES_NAME;
import static com.neopharma.datavault.model.Constants.Preference.TOKEN;

public class SharedPreferences {

    private Context context;

    public SharedPreferences(Context context) {
        this.context = context;
    }

    private android.content.SharedPreferences getDefaultPreferences() {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private SecurePreferences getSecurePreferences() {
        return new SecurePreferences(context);
    }

    public String getToken() {
        //To compensate speed loss on accessing secure preference used a static cache
        if (Store.token == null) {
            Store.token = getSecurePreferences().getString(TOKEN, "");
        }
        return Store.token;
    }

    public LiveData<Boolean> fillToken() {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        //Prefetch token
        new Thread() {
            @Override
            public void run() {
                Store.token = getSecurePreferences().getString(TOKEN, "");
                result.postValue(true);
            }
        }.start();
        return result;
    }

    public LiveData<Boolean> setToken(String token) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        //To Handle UI thread lock during secure shared preference access
        new Thread() {
            @Override
            public void run() {
                Store.token = token;
                SecurePreferences.Editor editor = getSecurePreferences().edit();
                editor.putString(TOKEN, token);
                editor.apply();
                result.postValue(true);
            }
        }.start();
        return result;
    }

    public String getString(String key) {
        return getDefaultPreferences().getString(key, "");
    }

    public void putString(String key, String value) {
        android.content.SharedPreferences.Editor editor = getDefaultPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        android.content.SharedPreferences.Editor editor = getDefaultPreferences().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return getDefaultPreferences().getBoolean(key, false);
    }
}