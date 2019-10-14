package com.neopharma.datavault.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.neopharma.datavault.R;
import com.neopharma.datavault.model.Constants;

public class QRScanFragment extends DialogFragment {

    private CodeScanner mCodeScanner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View root = inflater.inflate(R.layout.qr_layout, container, false);
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        scannerView.setAutoFocusButtonVisible(false);
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setFlashEnabled(true);
        if (mCodeScanner.isFlashEnabled()) {
            mCodeScanner.setFlashEnabled(false);
        }
        mCodeScanner.setDecodeCallback(result -> {
                    getDialog().dismiss();
                    activity.runOnUiThread(() -> {
                        Intent intent = new Intent();
                        intent.putExtra("kit_id", result.getText());
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Constants.Numbers.ONE, intent);
                    });
                }
        );
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}