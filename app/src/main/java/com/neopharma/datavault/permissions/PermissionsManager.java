package com.neopharma.datavault.permissions;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PermissionsManager {

    private static final String CAMERA = Manifest.permission.CAMERA;
    private static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private final int REQUEST_PERMISSIONS_CODE = 0;

    private PermissionsListener listener;

    public PermissionsManager(PermissionsListener listener) {
        this.listener = listener;
    }

    public PermissionsListener getListener() {
        return listener;
    }

    public void setListener(PermissionsListener listener) {
        this.listener = listener;
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    private static boolean isCameraPermissionGranted(Context context) {
        return isPermissionGranted(context, CAMERA);
    }

    private static boolean isReadExternalStoragePermissionGranted(Context context) {
        return isPermissionGranted(context, READ_EXTERNAL_STORAGE);
    }

    private static boolean isWriteExternalStoragePermissionGranted(Context context) {
        return isPermissionGranted(context, WRITE_EXTERNAL_STORAGE);
    }

    public static boolean areCameraPermissionsGranted(Context context) {
        return isCameraPermissionGranted(context)
                && isReadExternalStoragePermissionGranted(context)
                && isWriteExternalStoragePermissionGranted(context);
    }

    public void requestCameraPermissions(Object observer) {
        String[] permissions = new String[]{CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
        requestPermissions(observer, permissions);
    }

    private void requestPermissions(Object observer, String[] permissions) {
        if (observer instanceof AppCompatActivity) {
            requestPermissions((AppCompatActivity) observer, permissions);
        } else if (observer instanceof Fragment) {
            requestPermissions((Fragment) observer, permissions);
        }

    }

    private void requestPermissions(AppCompatActivity activity, String[] permissions) {
        ArrayList<String> permissionsToExplain = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                permissionsToExplain.add(permission);
            }
        }

        if (listener != null && permissionsToExplain.size() > 0) {
            listener.onExplanationNeeded(permissionsToExplain);
        }

        ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSIONS_CODE);
    }

    private void requestPermissions(Fragment fragment, String[] permissions) {
        ArrayList<String> permissionsToExplain = new ArrayList<>();
        for (String permission : permissions) {
            if (fragment.shouldShowRequestPermissionRationale(permission)) {
                permissionsToExplain.add(permission);
            }
        }

        if (listener != null && permissionsToExplain.size() > 0) {
            listener.onExplanationNeeded(permissionsToExplain);
        }

        fragment.requestPermissions(permissions, REQUEST_PERMISSIONS_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_CODE:
                if (listener != null) {
                    boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    listener.onPermissionResult(granted);
                }
                break;
            default:
                // Ignored
        }
    }
}
