package com.neopharma.datavault.ui;

import com.neopharma.datavault.activity.InitialActivity;
import com.neopharma.datavault.activity.MainActivity;
import com.neopharma.datavault.activity.SplashActivity;
import com.neopharma.datavault.fragments.CameraFragment;
import com.neopharma.datavault.fragments.initial.DataFetchFragment;
import com.neopharma.datavault.fragments.initial.InfoFragment;
import com.neopharma.datavault.fragments.initial.SignInFragment;
import com.neopharma.datavault.fragments.main.ChangePasswordFragment;
import com.neopharma.datavault.fragments.main.DashBoardFragment;
import com.neopharma.datavault.fragments.main.DonorKitHistoryFragment;
import com.neopharma.datavault.fragments.main.DonorListFragment;
import com.neopharma.datavault.fragments.main.FieldTestFirstFragment;
import com.neopharma.datavault.fragments.main.FieldTestSecondFragment;
import com.neopharma.datavault.fragments.main.FieldTestSuccessFragment;
import com.neopharma.datavault.fragments.main.FieldTestSummaryFragment;
import com.neopharma.datavault.fragments.main.FieldTestThirdFragment;
import com.neopharma.datavault.fragments.main.ProfileFragment;
import com.neopharma.datavault.fragments.main.SettingFragment;
import com.neopharma.datavault.fragments.main.SyncFragment;
import com.neopharma.datavault.fragments.main.TaskFragment;
import com.neopharma.datavault.fragments.module.ChangePasswordModule;
import com.neopharma.datavault.fragments.module.DashBoardModule;
import com.neopharma.datavault.fragments.module.DataFetchModule;
import com.neopharma.datavault.fragments.module.DonorHistoryModule;
import com.neopharma.datavault.fragments.module.DonorListModule;
import com.neopharma.datavault.fragments.module.FieldTestModule;
import com.neopharma.datavault.fragments.module.InfoPresenterModule;
import com.neopharma.datavault.fragments.module.LoginModule;
import com.neopharma.datavault.fragments.module.MainModule;
import com.neopharma.datavault.fragments.module.ProfileModule;
import com.neopharma.datavault.fragments.module.SettingModule;
import com.neopharma.datavault.fragments.module.SyncModule;
import com.neopharma.datavault.fragments.module.TaskModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract InitialActivity contributeInitialActivity();

    @ContributesAndroidInjector(modules = {InfoPresenterModule.class})
    abstract SplashActivity contributeSplashActivity();

    @ContributesAndroidInjector(modules = {InfoPresenterModule.class})
    abstract InfoFragment contributeInfoFragment();

    @ContributesAndroidInjector(modules = {LoginModule.class})
    abstract SignInFragment contributeSignInFragment();

    @ContributesAndroidInjector(modules = {DataFetchModule.class})
    abstract DataFetchFragment contributeDataFetchFragment();

    @ContributesAndroidInjector(modules = {MainModule.class, ProfileModule.class})
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = {ProfileModule.class})
    abstract ProfileFragment contributeProfileFragment();

    @ContributesAndroidInjector(modules = {DashBoardModule.class, DataFetchModule.class})
    abstract DashBoardFragment contributeDashBoardFragment();

    @ContributesAndroidInjector(modules = {SettingModule.class})
    abstract SettingFragment contributeSettingsFragment();

    @ContributesAndroidInjector(modules = {DonorHistoryModule.class})
    abstract DonorKitHistoryFragment contributePasswordFragment();

    @ContributesAndroidInjector(modules = {SyncModule.class, DonorHistoryModule.class})
    abstract SyncFragment contributeSyncFragment();

    @ContributesAndroidInjector(modules = {FieldTestModule.class, TaskModule.class})
    abstract FieldTestFirstFragment contributeFieldTestFirstFragment();

    @ContributesAndroidInjector(modules = {FieldTestModule.class})
    abstract FieldTestSecondFragment contributeFieldTestSecondFragment();

    @ContributesAndroidInjector(modules = {FieldTestModule.class})
    abstract FieldTestThirdFragment contributeFieldTestThirdFragment();

    @ContributesAndroidInjector(modules = {FieldTestModule.class})
    abstract FieldTestSummaryFragment contributeFieldTestSummaryFragment();

    @ContributesAndroidInjector
    abstract FieldTestSuccessFragment contributeFieldTestSuccessFragment();

    @ContributesAndroidInjector(modules = {FieldTestModule.class})
    abstract CameraFragment contributeCameraFragment();

    @ContributesAndroidInjector(modules = {TaskModule.class, DonorHistoryModule.class})
    abstract TaskFragment contributeTaskFragment();

    @ContributesAndroidInjector(modules = {DonorListModule.class})
    abstract DonorListFragment contributeDonorListFragment();

    @ContributesAndroidInjector(modules = {ChangePasswordModule.class})
    abstract ChangePasswordFragment contributeChangePasswordFragment();
}
