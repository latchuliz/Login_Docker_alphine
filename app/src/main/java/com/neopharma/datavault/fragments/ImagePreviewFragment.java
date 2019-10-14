package com.neopharma.datavault.fragments;

import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neopharma.datavault.BR;
import com.neopharma.datavault.R;
import com.neopharma.datavault.databinding.LayoutImagePreviewBinding;
import com.neopharma.datavault.model.Constants;

public class ImagePreviewFragment extends DialogFragment {

    private LayoutImagePreviewBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_image_preview, container, false);
        if (getArguments() != null) {
            String path = getArguments().getString(Constants.IMAGE_PATH);
            binding.setVariable(BR.path, path);
        }
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        binding.closeBtn.setOnClickListener(v -> getDialog().dismiss());
        return binding.getRoot();
    }
}