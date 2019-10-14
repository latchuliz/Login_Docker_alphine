package com.neopharma.datavault.di;

import android.app.Application;

import com.neopharma.datavault.DataVaultApp;
import com.neopharma.datavault.data.remote.NetworkModule;
import com.neopharma.datavault.fragments.module.ServiceBuilderModule;
import com.neopharma.datavault.ui.ActivityBuilder;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        NetworkModule.class,
        ServiceBuilderModule.class,
        ActivityBuilder.class
})
public interface AppComponent {
    void inject(DataVaultApp tamperlokApp);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
