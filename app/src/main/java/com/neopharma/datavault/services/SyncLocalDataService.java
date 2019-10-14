package com.neopharma.datavault.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.MutableLiveData;

import com.neopharma.datavault.R;
import com.neopharma.datavault.data.local.database.AppDatabase;
import com.neopharma.datavault.data.local.entity.FieldTest;
import com.neopharma.datavault.data.local.entity.Image;
import com.neopharma.datavault.fragments.module.SharedPreferences;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.repository.Repository;
import com.neopharma.datavault.utility.Store;
import com.neopharma.datavault.utility.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SyncLocalDataService extends LifecycleService {

    @Inject
    public AppDatabase db;

    @Inject
    public Repository repo;

    @Inject
    public SharedPreferences preferences;

    private static final String CHANNEL_ID = "channel_01";
    private final IBinder mBinder = new LocalBinder();
    private static String CHANNEL = "default";
    private static final int NOTIFICATION_ID = 12345678;
    private NotificationManager mNotificationManager;
    private int synchronizedCount;
    private Intent selfIntent;
    private NotificationCompat.Builder builder;
    private Timer timer = null;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidInjection.inject(this);
        selfIntent = new Intent(this, SyncLocalDataService.class);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(mChannel);
        }
        timer = new Timer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        synchronizedCount = 0;
        builder = getNotificationBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(NOTIFICATION_ID, builder.build());
        }
        setUpTimer();
        return START_NOT_STICKY;
    }

    private void setUpTimer() {
        MutableLiveData<Boolean> t = new MutableLiveData<>();
        timer.schedule(new TimerTask() {
            public void run() {
                if (Store.isNetworkAvailable) {
                    if (!preferences.getBoolean(Constants.Settings.AUTO_SYNC)) {
                        t.postValue(true);
                    } else {
                        removeSyncData();
                    }
                } else {
                    removeSyncData();
                }
            }
        }, 0, 30000); // update every 30 sec
        t.observe(this, res -> {
            syncData();
        });
    }

    private void syncData() {
        List<FieldTest> fieldTests = db.fieldTestDao().findAllPending(Store.userId);
        if (fieldTests != null && !fieldTests.isEmpty()) {
            synchronizedCount = 0;
            for (FieldTest test : fieldTests) {
                test.setGender(test.getGender().toLowerCase());
                test.setResult(test.getResult().toLowerCase());
                test.setAdulterantResult(test.getAdulterantResult().toLowerCase());
                builder.setContentText(String.format(Locale.US, "%d/%d", synchronizedCount, fieldTests.size()));
                builder.setProgress(0, 0, true);
                mNotificationManager.notify(NOTIFICATION_ID, builder.build());
                List<Image> incompleteImages = db.imageDao().findByAllImage(test.getId(), Constants.FieldTest.INCOMPLETE);
                if (!incompleteImages.isEmpty()) {
                    uploadImages(incompleteImages);
                } else {
                    setImagesInTest(test, fieldTests);
                }
            }
        } else {
            removeSyncData();
        }
    }

    private void setImagesInTest(FieldTest test, List<FieldTest> fieldTests) {
        List<Image> pendingImages = db.imageDao().findByAllImage(test.getId(), Constants.FieldTest.PENDING);
        List<String> donorImages = new ArrayList<>();
        List<String> kitImages = new ArrayList<>();
        List<String> officerImages = new ArrayList<>();
        List<String> idImages = new ArrayList<>();
        for (Image image : pendingImages) {
            if (image.methodName.contains(Constants.ImageType.DONOR.name)) {
                donorImages.add(image.path);
            } else if (image.methodName.contains(Constants.ImageType.KIT.name)) {
                kitImages.add(image.path);
            } else if (image.methodName.contains(Constants.ImageType.TEST.name)) {
                officerImages.add(image.path);
            } else if (image.methodName.contains(Constants.ImageType.ID.name)) {
                idImages.add(image.path);
            }
        }

        test.setDonorImage(donorImages);
        test.setKitUploads(kitImages);
        test.setOfficerUploads(officerImages);
        test.setIdReferenceImage(idImages);
        test.setDob(Utility.formattedUTCDate(test.getDob()));
        if (!donorImages.isEmpty() && !kitImages.isEmpty() && !officerImages.isEmpty())
            repo.syncCloud(test).observe(this, response -> {
                if (fieldTests.size() == ++synchronizedCount) {
                    removeSyncData();
                }
            });
    }

    private void uploadImages(List<Image> images) {
        for (Image image : images) {
            File file = new File(image.path);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("image/jpeg"), file);
            repo.imageUpload(MultipartBody.Part.createFormData("file_to_upload",
                    file.getName(), requestFile), image);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        startMyForegroundService(selfIntent);
        return mBinder;
    }

    public void startMyForegroundService(Intent intent) {
        ContextCompat.startForegroundService(this, intent);
    }

    public void requestSyncData() {
        Intent i = new Intent(getApplicationContext(), SyncLocalDataService.class);
        startMyForegroundService(i);
    }

    public void removeSyncData() {
        stopForeground(true);
        stopSelf();
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    public void cancelTimer() {
        timer.cancel();
    }

    public void restartSyncData() {
        removeSyncData();
        requestSyncData();
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL)
                .setContentTitle("Synchronization")
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis());
        builder.setProgress(100, 0, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }
        return builder;
    }

    public class LocalBinder extends Binder {
        public SyncLocalDataService getService() {
            return SyncLocalDataService.this;
        }
    }
}