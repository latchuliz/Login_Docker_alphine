package com.neopharma.datavault.fragments;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.neopharma.datavault.R;
import com.neopharma.datavault.data.local.entity.Image;
import com.neopharma.datavault.databinding.LayoutCameraBinding;
import com.neopharma.datavault.fragments.viewmodel.FieldTestViewModel;
import com.neopharma.datavault.listener.OnPickImageListener;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.permissions.PermissionsListener;
import com.neopharma.datavault.permissions.PermissionsManager;
import com.neopharma.datavault.utility.ExifDataCopier;
import com.neopharma.datavault.utility.ImageResizer;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import io.fotoapparat.Fotoapparat;
import io.fotoapparat.configuration.CameraConfiguration;
import io.fotoapparat.configuration.UpdateConfiguration;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.result.PhotoResult;

import static io.fotoapparat.selector.FlashSelectorsKt.autoFlash;
import static io.fotoapparat.selector.FlashSelectorsKt.autoRedEye;
import static io.fotoapparat.selector.FlashSelectorsKt.off;
import static io.fotoapparat.selector.FlashSelectorsKt.torch;
import static io.fotoapparat.selector.FocusModeSelectorsKt.autoFocus;
import static io.fotoapparat.selector.FocusModeSelectorsKt.continuousFocusPicture;
import static io.fotoapparat.selector.FocusModeSelectorsKt.fixed;
import static io.fotoapparat.selector.LensPositionSelectorsKt.back;
import static io.fotoapparat.selector.LensPositionSelectorsKt.front;
import static io.fotoapparat.selector.PreviewFpsRangeSelectorsKt.highestFps;
import static io.fotoapparat.selector.SelectorsKt.firstAvailable;
import static io.fotoapparat.selector.SensorSensitivitySelectorsKt.highestSensorSensitivity;

public class CameraFragment extends DialogFragment implements PermissionsListener {

    @Inject
    public FieldTestViewModel viewModel;

    private Fotoapparat fotoapparat;
    private LayoutCameraBinding binding;
    private PermissionsManager permissionsManager;
    private Constants.ImageType imageType;
    private OnPickImageListener pickImageListener;
    private boolean isChecked = false;
    private boolean activeCameraBack = true;

    public CameraFragment setImageType(Constants.ImageType imageType) {
        this.imageType = imageType;
        return this;
    }

    public CameraFragment setPickImageListener(OnPickImageListener pickImageListener) {
        this.pickImageListener = pickImageListener;
        return this;
    }

    private CameraConfiguration cameraConfiguration = CameraConfiguration
            .builder()
            .focusMode(firstAvailable(
                    continuousFocusPicture(),
                    autoFocus(),
                    fixed()
            ))
            .flash(firstAvailable(
                    autoRedEye(),
                    autoFlash(),
                    torch(),
                    off()
            ))
            .previewFpsRange(highestFps())
            .sensorSensitivity(highestSensorSensitivity())
            .build();

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_camera, container, false);
        permissionsManager = new PermissionsManager(this);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createFotoapparat();
        takePictureOnClick();
        switchCameraOnClick();
        toggleTorchOnSwitch();
        if (PermissionsManager.areCameraPermissionsGranted(requireActivity())) {
            fotoapparat.start();
        } else {
            permissionsManager.requestCameraPermissions(this);
        }

    }

    private void createFotoapparat() {
        fotoapparat = Fotoapparat
                .with(requireActivity())
                .into(binding.cameraView)
                .focusView(binding.focusView)
                .previewScaleType(ScaleType.CenterCrop)
                .lensPosition(back())
                .build();
    }

    private void switchCameraOnClick() {
        boolean hasFrontCamera = fotoapparat.isAvailable(front());
        binding.switchCamera.setVisibility(hasFrontCamera ? View.VISIBLE : View.GONE);
        if (hasFrontCamera) {
            switchCameraOnClick(binding.switchCamera);
        }
    }

    private void toggleTorchOnSwitch() {
        binding.torchSwitch.setOnClickListener(view -> {
            isChecked = !isChecked;
            fotoapparat.updateConfiguration(
                    UpdateConfiguration.builder()
                            .flash(isChecked ? torch() : off())
                            .build()
            );
            binding.torchSwitch.setImageDrawable(isChecked ?
                    getResources().getDrawable(R.drawable.ic_flash_on_white_24dp) :
                    getResources().getDrawable(R.drawable.ic_flash_off_white_24dp));
        });
    }

    private void switchCameraOnClick(View view) {
        view.setOnClickListener(v -> {
            activeCameraBack = !activeCameraBack;
            fotoapparat.switchTo(
                    activeCameraBack ? back() : front(),
                    cameraConfiguration
            );
        });
    }

    private void takePictureOnClick() {
        binding.capture.setOnClickListener(v -> takePicture());
    }

    private void takePicture() {
        binding.capture.setEnabled(false);
        PhotoResult photoResult = fotoapparat.takePicture();
        File f = new File(requireActivity().getExternalFilesDir(Constants.PHOTOS), imageType.name + System.currentTimeMillis() + ".jpg");
        photoResult.saveToFile(f).whenDone(d -> {
            //Resized the image if file greater than 4.5mb
            if ((f.length() / 1024) > 4608) {
                ImageResizer imageResizer = new ImageResizer(f, new ExifDataCopier());
                //6 MegaPixel
                imageResizer.resizeImageIfNeeded(f.getAbsolutePath(), 2560.0, 2560.0);
            }
            Image image = viewModel.saveImagePath(f.getAbsolutePath(), imageType);
            requireActivity().runOnUiThread(() -> pickImageListener.onPicked(image));
            dismiss();
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (PermissionsManager.areCameraPermissionsGranted(requireActivity()))
            fotoapparat.stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            fotoapparat.start();
        } else {
            fotoapparat.stop();
            getDialog().dismiss();
            Toast.makeText(getContext(), Constants.Temp.DENIED_PERMISSION, Toast.LENGTH_LONG).show();
        }
    }
}