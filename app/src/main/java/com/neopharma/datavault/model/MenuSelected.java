package com.neopharma.datavault.model;

import androidx.lifecycle.LiveData;

public class MenuSelected extends LiveData<String> {

    public void post(String menu) {
        this.postValue(menu);
    }
}
