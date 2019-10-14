package com.neopharma.datavault.di;

import com.neopharma.datavault.DataVaultApp;

public class AppInjector {
    private AppInjector() {
    }

    public static void init(DataVaultApp tamperlokApp) {
        DaggerAppComponent.builder().application(tamperlokApp)
                .build().inject(tamperlokApp);
    }
}
