package com.neopharma.datavault.utility;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import com.neopharma.datavault.R;
import com.neopharma.datavault.databinding.DialogCustomBinding;

public class CustomAlertDialog extends Dialog {
    private String message;
    private DialogCustomBinding binding;
    private String positiveButtonTxt;
    private String neutralButtonTxt;
    private String negativeButtonTxt;
    private OnClickListener positiveOnClickListener;
    private OnClickListener negativeOnClickListener;
    private OnClickListener neutralOnClickListener;
    private boolean imageVisible;

    public CustomAlertDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_custom, null, false);
        setContentView(binding.getRoot());
        if (positiveButtonTxt != null) {
            binding.setPositiveTxt(positiveButtonTxt);
            binding.button1.setOnClickListener(v -> positiveOnClickListener.onClick(CustomAlertDialog.this, DialogInterface.BUTTON_POSITIVE));
        }
        if (negativeButtonTxt != null) {
            binding.setNegativeTxt(negativeButtonTxt);
            binding.button2.setOnClickListener(v -> negativeOnClickListener.onClick(CustomAlertDialog.this, DialogInterface.BUTTON_NEGATIVE));
        }
        if (neutralButtonTxt != null) {
            binding.setNeutralTxt(neutralButtonTxt);
            binding.button3.setOnClickListener(v -> neutralOnClickListener.onClick(CustomAlertDialog.this, DialogInterface.BUTTON_NEUTRAL));
        }
        binding.setMessage(message);
        binding.setImage(imageVisible);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setImage(boolean imageVisible) {
        this.imageVisible = imageVisible;
    }

    public void setButton(int whichButton, String text, OnClickListener listener) {
        switch (whichButton) {
            case DialogInterface.BUTTON_POSITIVE:
                positiveButtonTxt = text;
                positiveOnClickListener = listener;
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                negativeButtonTxt = text;
                negativeOnClickListener = listener;
                break;

            case DialogInterface.BUTTON_NEUTRAL:
                neutralButtonTxt = text;
                neutralOnClickListener = listener;
                break;
        }
    }
}
