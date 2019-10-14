package com.neopharma.datavault.fragments.main;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.view.View;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.neopharma.datavault.BR;
import com.neopharma.datavault.R;
import com.neopharma.datavault.data.local.entity.FieldTest;
import com.neopharma.datavault.data.local.entity.Image;
import com.neopharma.datavault.databinding.FragmentSyncBinding;
import com.neopharma.datavault.fragments.viewmodel.DonorHistoryViewModel;
import com.neopharma.datavault.fragments.viewmodel.SyncViewModel;
import com.neopharma.datavault.listener.OnBackPressedListener;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.model.Constants.FragmentType;
import com.neopharma.datavault.utility.Store;
import com.neopharma.datavault.utility.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.content.Context.NOTIFICATION_SERVICE;

public class SyncFragment extends MainFragment<FragmentSyncBinding> implements OnBackPressedListener {

    @Inject
    public DonorHistoryViewModel viewModel;

    @Inject
    SyncViewModel syncViewModel;

    int j = 0;

    private static final String CHANNEL_ID = "channel_01";
    private static String CHANNEL = "default";
    private NotificationManager manager;
    private static final int NOTIFICATION_ID = 12345678;
    private int pendingImageCount;

    private NotificationCompat.Builder builder;
    private Timer timer;
    private Handler backPressHandler;
    private Runnable backpress;

    @Override
    public int layoutId() {
        return R.layout.fragment_sync;
    }

    @Override
    protected String getToolbarTitle() {
        return FragmentType.SYNCHRONIZATION;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        builder = getNotificationBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(mChannel);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getViewDataBinding().syncSuccess.getVisibility() == View.VISIBLE) {
            activity.getSupportFragmentManager().popBackStack();
        } else {
            activity.menuSelected.post(FragmentType.SYNCHRONIZATION);
            showBackIcon();
            getViewDataBinding().setVariable(BR.syncViewModel, syncViewModel);
            hideToolBarItem();
            if (Store.isNetworkAvailable) {
                syncView();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        activity.runOnUiThread(() -> startSync());
                    }
                }, 0, 30000);
            } else {
                getViewDataBinding().noInternet.setVisibility(View.VISIBLE);
                getViewDataBinding().syncView.setVisibility(View.GONE);
                getViewDataBinding().syncSuccess.setVisibility(View.GONE);
                getViewDataBinding().noRecord.setVisibility(View.GONE);
            }
        }
    }

    private void startSync() {
        List<FieldTest> fieldTests = viewModel.getPendingDonors();
        if (fieldTests == null || fieldTests.isEmpty()) {
            getViewDataBinding().noRecord.setVisibility(View.VISIBLE);
            getViewDataBinding().syncView.setVisibility(View.GONE);
            getViewDataBinding().syncSuccess.setVisibility(View.GONE);
            getViewDataBinding().noInternet.setVisibility(View.GONE);
            return;
        }

        getViewDataBinding().syncView.setVisibility(View.VISIBLE);
        getViewDataBinding().noRecord.setVisibility(View.GONE);
        getViewDataBinding().syncSuccess.setVisibility(View.GONE);
        getViewDataBinding().noInternet.setVisibility(View.GONE);
        if (syncViewModel.getPendingCount() == 0)
            syncViewModel.setPendingCount(fieldTests.size());
        getViewDataBinding().syncCount.setText("Synchronization " + 0 + "/" + syncViewModel.getPendingCount());
        builder.setContentText(String.format(Locale.US, "%d/%d", 0, syncViewModel.getPendingCount()));
        builder.setProgress(0, 0, true);
        manager.notify(NOTIFICATION_ID, builder.build());
        for (FieldTest test : fieldTests) {
            test.setGender(test.getGender().toLowerCase());
            test.setResult(test.getResult().toLowerCase());
            test.setAdulterantResult(test.getAdulterantResult().toLowerCase());
            List<Image> incompleteImages = viewModel.findByAllImage(test.getId(), Constants.FieldTest.INCOMPLETE);
            if (!incompleteImages.isEmpty())
                uploadImages(test, incompleteImages);
            else
                setImagesInTest(test);
        }
    }

    private void uploadImages(FieldTest test, List<Image> images) {
        pendingImageCount = images.size();
        for (Image image : images) {
            File file = new File(image.path);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("image/jpeg"), file);
            viewModel.imageUpload(
                    MultipartBody.Part.createFormData(
                            "file_to_upload",
                            file.getName(),
                            requestFile
                    ), image).observe(this, res -> {
                if (activity.validResponse(res))
                    if (--pendingImageCount == 0) {
                        //Images uploaded
                        setImagesInTest(test);
                    }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        manager.cancel(NOTIFICATION_ID);
        if (timer != null)
            timer.cancel();
        if (backPressHandler != null && backpress != null)
            backPressHandler.removeCallbacks(backpress);
    }

    private void setImagesInTest(FieldTest test) {
        List<Image> pendingImages = viewModel.findByAllImage(test.getId(), Constants.FieldTest.PENDING);
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
            } else if (image.methodName.contains(Constants.ImageType.ID.name)){
                idImages.add(image.path);
            }
        }

        test.setDonorImage(donorImages);
        test.setKitUploads(kitImages);
        test.setIdReferenceImage(idImages);
        test.setOfficerUploads(officerImages);

        test.setDob(Utility.formattedUTCDate(test.getDob()));
        if (!donorImages.isEmpty() && !kitImages.isEmpty() && !officerImages.isEmpty())
            viewModel.syncCloud(test).observe(this, response -> {
                if (activity.validResponseCallback(response)) {
                    syncViewModel.setCompletedCount(syncViewModel.getCompletedCount() + 1);
                    syncViewModel.setCompletedLiveCount(syncViewModel.getCompletedCount());
                    builder.setContentText(String.format(Locale.US, "%d/%d", syncViewModel.getCompletedCount(), syncViewModel.getPendingCount()));
                    builder.setProgress(0, 0, true);
                    manager.notify(NOTIFICATION_ID, builder.build());
                    if (syncViewModel.getPendingCount() == syncViewModel.getCompletedCount()) {
                        //Sync Complete
                        manager.cancel(NOTIFICATION_ID);
                    }
                } else {
                    if (response != null) {
                        if (Constants.SESSION_EXPIRED.equals(response.errorCode)) {
                            activity.showLogoutDialog(response.message);
                        } else {
                            activity.alertDialog(response.message, Constants.OKAY);
                        }
                    }
                }
            });
    }

    private void syncView() {
        String format = "%.0f%%";
        DecoView arcView = getViewDataBinding().dynamicArcView;

        SeriesItem seriesItem = new SeriesItem.Builder(getResources().getColor(R.color.colorMildBlue))
                .setRange(Constants.Numbers.ZERO, Constants.Numbers.HUNDRED, Constants.Numbers.ZERO)
                .setLineWidth(120f)
                .setCapRounded(false)
                .build();

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                if (format.contains("%%")) {
                    float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                    getViewDataBinding().targetValue.setText(String.format(format, percentFilled * 100f));
                } else {
                    getViewDataBinding().targetValue.setText(String.format(format, currentPosition));
                }
                if (currentPosition == 100) {
                    new Handler().postDelayed(() -> {
                        getViewDataBinding().syncSuccess.setVisibility(View.VISIBLE);
                        getViewDataBinding().syncView.setVisibility(View.GONE);
                        getViewDataBinding().noRecord.setVisibility(View.GONE);
                        getViewDataBinding().noInternet.setVisibility(View.GONE);
                    }, 2000);
                    backPressCall();
                }
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {
            }
        });

        int series = arcView.addSeries(seriesItem);

        syncViewModel.getCompletedLiveCount().observe(this, count -> {
            getViewDataBinding().syncCount.setText("Synchronization " + count + "/" + syncViewModel.getPendingCount());
            int completedCount = (100 * count) / syncViewModel.getPendingCount();
            arcView.addEvent(new DecoEvent.Builder(completedCount)
                    .setIndex(series)
                    .setDelay(Constants.Numbers.THOUSAND)
                    .build());
        });
    }

    private void backPressCall() {
        backPressHandler = new Handler();
        backpress = () -> activity.onBackPressed();
        backPressHandler.postDelayed(backpress, 4000);
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL)
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

    @Override
    public boolean onBackPressed() {
        if (backPressHandler != null && backpress != null)
            backPressHandler.removeCallbacks(backpress);
        return false;
    }
}
