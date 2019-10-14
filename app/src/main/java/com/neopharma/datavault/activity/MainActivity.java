package com.neopharma.datavault.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.neopharma.datavault.BR;
import com.neopharma.datavault.R;
import com.neopharma.datavault.adapter.NavigationDrawerAdapter;
import com.neopharma.datavault.databinding.ActivityMainBinding;
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
import com.neopharma.datavault.fragments.viewmodel.MainViewModel;
import com.neopharma.datavault.fragments.viewmodel.ProfileViewModel;
import com.neopharma.datavault.listener.OnBackPressedListener;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.model.Constants.Numbers;
import com.neopharma.datavault.model.DrawerItem;
import com.neopharma.datavault.model.MenuSelected;
import com.neopharma.datavault.model.response.UserResponse;
import com.neopharma.datavault.services.SyncLocalDataService;
import com.neopharma.datavault.utility.Connection;
import com.neopharma.datavault.utility.Store;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;


public class MainActivity extends BaseActivity
        implements HasSupportFragmentInjector {

    @Inject
    public MainViewModel mainViewModel;

    @Inject
    public ProfileViewModel profileViewModel;

    @Inject
    public Connection connection;

    @Inject
    public DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private ActionBarDrawerToggle toggle;
    private ActivityMainBinding binding;
    private ArrayList<DrawerItem> drawerItems = new ArrayList<>();
    private boolean mBound;
    private NavigationDrawerAdapter adapter;
    public SyncLocalDataService mService;
    public MenuSelected menuSelected = new MenuSelected();

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SyncLocalDataService.LocalBinder binder = (SyncLocalDataService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService.removeSyncData();
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, SyncLocalDataService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.appBar.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerItems.add(new DrawerItem(R.drawable.dashboard, getResources().getString(R.string.dashboard), true));
        drawerItems.add(new DrawerItem(R.drawable.field_test, getResources().getString(R.string.field_test), false));
        drawerItems.add(new DrawerItem(R.drawable.history, getResources().getString(R.string.donor_kit_history), false));
        drawerItems.add(new DrawerItem(R.drawable.task_menu, getResources().getString(R.string.task), false));
        drawerItems.add(new DrawerItem(R.drawable.sync, getResources().getString(R.string.sync), false));
        drawerItems.add(new DrawerItem(R.drawable.settings, getResources().getString(R.string.settings), false));
        drawerItems.add(new DrawerItem(R.drawable.profile, getResources().getString(R.string.profile), false));
        drawerItems.add(new DrawerItem(R.drawable.logout, getResources().getString(R.string.logout), false));

        adapter = new NavigationDrawerAdapter(this, R.layout.adapter_menu_list, drawerItems);
        binding.drawerList.setAdapter(adapter);
        binding.drawerList.setOnItemClickListener(new DrawerItemClickListener());

        toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.appBar.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setToolbarNavigationClickListener(view -> onBackPressed());

        hideBackIcon();

        actionToDashBoardFragment(true);

        binding.appBar.syncData.setOnClickListener(view -> {
            actionToSyncFragment(true);
        });

        binding.navHeader.navClose.setOnClickListener(view -> closeDrawers());
        binding.navHeader.profileView.setOnClickListener(view -> {
            closeDrawers();
            actionToMyProfileFragment(true);
        });

        connection.observe(this, res -> {
            Store.isNetworkAvailable = res;
            binding.navHeader.networkStatus.setImageDrawable(getResources().getDrawable(res ? R.drawable.online : R.drawable.offline));
        });

        binding.appBar.logout.setOnClickListener(view -> {
            showLogoutDialog(Constants.LOGOUT, true);
        });

        menuSelected.observe(this, menu -> {
            if (menu != null)
                switch (menu) {
                    case Constants.FragmentType.DASHBOARD:
                        notifyAdapter(Numbers.ZERO);
                        break;
                    case Constants.FragmentType.FIELD_TEST_DONOR_IDENTIFICATION:
                        notifyAdapter(Numbers.ONE);
                        break;
                    case Constants.FragmentType.DONOR_KIT_HISTORY:
                        notifyAdapter(Numbers.TWO);
                        break;
                    case Constants.FragmentType.TASKS:
                        notifyAdapter(Numbers.THREE);
                        break;
                    case Constants.FragmentType.SYNCHRONIZATION:
                        notifyAdapter(Numbers.FOUR);
                        break;
                    case Constants.FragmentType.SETTINGS:
                        notifyAdapter(Numbers.FIVE);
                        break;
                    case Constants.FragmentType.PROFILE:
                        notifyAdapter(Numbers.SIX);
                        break;
                }
        });
    }

    private void closeDrawers() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void setToolBarTitle(String title) {
        binding.appBar.toolbarTitle.setText(title);
    }

    public void hideToolBarItems() {
        binding.appBar.syncData.setVisibility(View.GONE);
        binding.appBar.logout.setVisibility(View.GONE);
    }

    public void showLogout() {
        binding.appBar.syncData.setVisibility(View.GONE);
        binding.appBar.logout.setVisibility(View.VISIBLE);
    }

    public void showSync() {
        binding.appBar.syncData.setVisibility(View.VISIBLE);
        binding.appBar.logout.setVisibility(View.GONE);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    public void notifyAdapter(int position) {
        for (int i = Numbers.ZERO; i < drawerItems.size(); i++) {
            DrawerItem drawerItem = drawerItems.get(i);
            drawerItem.setSelected(i == position);
        }
        adapter.notifyDataSetChanged();
    }

    private void selectItem(int position) {
        switch (position) {
            case Numbers.ZERO:
                hideBackIcon();
                actionToDashBoardFragment(true);
                break;
            case Numbers.ONE:
                actionDashBoard_to_FieldTestFragment(null, true);
                break;
            case Numbers.TWO:
                actionToDonorKitHistoryFragment(true);
                break;
            case Numbers.THREE:
                actionToTaskFragment(true);
                break;
            case Numbers.FOUR:
                actionToSyncFragment(true);
                break;
            case Numbers.FIVE:
                actionToSettingFragment(true);
                break;
            case Numbers.SIX:
                actionToMyProfileFragment(true);
                break;
            case Numbers.SEVEN:
                hideBackIcon();
                showLogoutDialog(Constants.LOGOUT, true);
                break;
        }
        closeDrawers();
    }

    public void showBackIcon() {
        toggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.appBar.toolbar.setNavigationIcon(R.drawable.back_front);
    }

    public void hideBackIcon() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(true);
        binding.appBar.toolbar.setNavigationIcon(R.drawable.nav);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mService.removeSyncData();
        mService.cancelTimer();
    }

    @Override
    public void onBackPressed() {
        closeDrawers();
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
        if (f instanceof OnBackPressedListener) {
            if (!((OnBackPressedListener) f).onBackPressed())
                super.onBackPressed();
        } else
            super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void actionToDashBoardFragment(boolean b) {
        navigator
                .clearBackStack(b)
                .backStack(false)
                .navigate(new DashBoardFragment());
    }

    public void actionToMyProfileFragment(boolean b) {
        navigator
                .clearBackStack(b)
                .backStack(true)
                .navigate(new ProfileFragment());
    }

    public void actionToSettingFragment(boolean b) {
        navigator
                .clearBackStack(b)
                .backStack(true)
                .navigate(new SettingFragment());
    }

    public void actionToDonorKitHistoryFragment(boolean b) {
        navigator
                .clearBackStack(b)
                .backStack(true)
                .navigate(new DonorKitHistoryFragment());
    }

    public void actionToTaskFragment(boolean b) {
        navigator
                .clearBackStack(b)
                .backStack(true)
                .navigate(new TaskFragment());
    }

    public void action_TaskFragment_to_DonorListFragment(Bundle bundle, boolean b) {
        navigator
                .clearBackStack(b)
                .bundle(bundle)
                .backStack(true)
                .navigate(new DonorListFragment());
    }

    public void actionToSyncFragment(boolean b) {
        navigator
                .clearBackStack(b)
                .backStack(true)
                .navigate(new SyncFragment());
    }

    public void actionDashBoard_to_FieldTestFragment(Bundle bundle, boolean b) {
        navigator
                .clearBackStack(b)
                .bundle(bundle)
                .backStack(true)
                .navigate(new FieldTestFirstFragment());
    }

    public void actionFieldTestFirstFragment_to_FieldTestSecondFragment(boolean b) {
        navigator
                .clearBackStack(b)
                .backStack(true)
                .navigate(new FieldTestSecondFragment());
    }

    public void actionFieldTestSecondFragment_to_FieldTestThirdFragment(boolean b) {
        navigator
                .clearBackStack(b)
                .backStack(true)
                .navigate(new FieldTestThirdFragment());
    }

    public void actionFieldTestThirdFragment_to_FieldTestSummaryFragment(boolean b) {
        navigator
                .clearBackStack(b)
                .backStack(true)
                .navigate(new FieldTestSummaryFragment());
    }

    public void actionFieldTestSummaryFragment_to_FieldTestSuccessFragment(boolean b) {
        navigator
                .clearBackStack(b)
                .backStack(true)
                .navigate(new FieldTestSuccessFragment());
    }

    public void actionSettingFragment_to_ChangePasswordFragment(boolean b) {
        navigator
                .clearBackStack(b)
                .backStack(true)
                .navigate(new ChangePasswordFragment());
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    protected void onResume() {
        super.onResume();
        profileViewModel.getProfile().observe(this, res -> {
            if (validResponse(res)) {
                setProfile(res);
            }
        });
    }

    public void setProfile(UserResponse res) {
        binding.setVariable(BR.data, res.data);
        Store.userId = res.data.id;
        Store.userRole = res.data.role.code;
    }
}
