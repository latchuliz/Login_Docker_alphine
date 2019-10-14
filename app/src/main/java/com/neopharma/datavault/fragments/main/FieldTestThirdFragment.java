package com.neopharma.datavault.fragments.main;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;

import com.neopharma.datavault.R;
import com.neopharma.datavault.adapter.ImageListAdapter;
import com.neopharma.datavault.data.local.entity.FieldTest;
import com.neopharma.datavault.data.local.entity.Image;
import com.neopharma.datavault.databinding.FragmentFieldTestThirdBinding;
import com.neopharma.datavault.fragments.CameraFragment;
import com.neopharma.datavault.fragments.ImagePreviewFragment;
import com.neopharma.datavault.fragments.viewmodel.FieldTestViewModel;
import com.neopharma.datavault.listener.OnBackPressedListener;
import com.neopharma.datavault.listener.OnImageClickListener;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.model.Constants.FragmentType;
import com.neopharma.datavault.utility.CustomAlertDialog;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import static com.neopharma.datavault.utility.Utility.EMOJI_FILTER;

public class FieldTestThirdFragment extends MainFragment<FragmentFieldTestThirdBinding> implements OnBackPressedListener {

    @Inject
    public FieldTestViewModel viewModel;
    private CustomAlertDialog alertDialog;
    private CameraFragment cameraFragment;

    @Override
    public int layoutId() {
        return R.layout.fragment_field_test_third;
    }

    @Override
    protected String getToolbarTitle() {
        return FragmentType.FIELD_TEST_TEST_RESULT;
    }

    private FieldTest fieldTest;

    private ArrayList<Image> testImages = new ArrayList<>();
    private ImageListAdapter testImagesAdapter;
    private boolean isValidationSuccess = false;
    private boolean isResultSuccess = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().testComment.setFilters(new InputFilter[]{EMOJI_FILTER});
        getViewDataBinding().radioResultGroup.setOnCheckedChangeListener((group, checkedId) ->
                getViewDataBinding().testResultError.setError(null));
        hideToolBarItem();
        initFieldTest();
        initTestImages();
        viewModel.setTestResultImages(testImages);
        getViewDataBinding().addImage.setOnClickListener(v -> startCamera(Constants.ImageType.TEST, testImages, testImagesAdapter));
        getViewDataBinding().submit.setOnClickListener(v -> {
            viewModel.getTestResultImages().observe(this, res -> {
                if (res.isEmpty()) {
                    isValidationSuccess = false;
                    getViewDataBinding().testImageError.requestFocus();
                    getViewDataBinding().testImageError.setError(getString(R.string.result_image_list_empty));
                } else {
                    isValidationSuccess = true;
                    getViewDataBinding().testImageError.setError(null);
                }
            });

            if (isValidationSuccess){
                if (getViewDataBinding().radioResultGroup.getCheckedRadioButtonId() == -1) {
                    isResultSuccess = false;
                    getViewDataBinding().testResultError.requestFocus();
                    getViewDataBinding().testResultError.setError(getString(R.string.no_result_validation));
                } else {
                    getViewDataBinding().testResultError.setError(null);
                    if (getViewDataBinding().adulterantResultGroup.getCheckedRadioButtonId() == -1) {
                        isResultSuccess = false;
                        getViewDataBinding().adulterantResultError.requestFocus();
                        getViewDataBinding().adulterantResultError.setError(getString(R.string.no_adult_result_validation));
                    } else {
                        isResultSuccess = true;
                        getViewDataBinding().adulterantResultError.setError(null);
                    }
                }
            }

            if (isValidationSuccess && isResultSuccess) {
                if (validator.validate()) {
                    viewModel.update();
                    activity.actionFieldTestThirdFragment_to_FieldTestSummaryFragment(false);
                }
            }

        });
        getViewDataBinding().setFieldtest(fieldTest);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (cameraFragment != null)
            cameraFragment.dismiss();

    }

    private void initFieldTest() {
        fieldTest = viewModel.findOrCreateIncompleteRecord();
        viewModel.setFieldTest(fieldTest);
        testImages.clear();
        for (Image i : fieldTest.getImages()) {
            if (i.methodName.contains(Constants.ImageType.TEST.name))
                testImages.add(i);
        }
    }

    private void initTestImages() {
        addImageIconVisibility(testImages);
        testImagesAdapter = new ImageListAdapter(testImages, true);
        initImages(getViewDataBinding().testImageList, testImagesAdapter, testImages);
    }

    private void startCamera(Constants.ImageType imageType, ArrayList<Image> images, ImageListAdapter imageListAdapter) {
        cameraFragment = new CameraFragment()
                .setImageType(imageType)
                .setPickImageListener(image -> {
                    images.add(image);
                    viewModel.setTestResultImages(images);
                    imageListAdapter.notifyDataSetChanged();
                    addImageIconVisibility(images);
                });
        cameraFragment.show(getChildFragmentManager(), Constants.CAMERA);
    }

    private void initImages(RecyclerView recyclerView, ImageListAdapter adapter, ArrayList<Image> images) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new VerticalSpacingDecoration(30));
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new OnImageClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImagePreviewFragment fragment = new ImagePreviewFragment();
                Bundle b = new Bundle();
                b.putString(Constants.IMAGE_PATH, images.get(position).path);
                fragment.setArguments(b);
                fragment.show(getChildFragmentManager(), Constants.IMAGE_PREVIEW);
            }

            @Override
            public void onDelete(View view, int position) {
                showDeleteImage(images, adapter, position);
            }
        });
    }

    public void showDeleteImage(ArrayList<Image> images, ImageListAdapter adapter, int position) {
        alertDialog = new CustomAlertDialog(activity);
        alertDialog.setMessage(Constants.DELETE_ALERT);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, Constants.OKAY, (dialog, which) -> {
            Image image = images.get(position);
            viewModel.removeImage(image);
            images.remove(position);
            viewModel.setTestResultImages(images);
            adapter.notifyDataSetChanged();
            File fileDelete = new File(image.path);
            fileDelete.delete();
            addImageIconVisibility(images);
            dialog.dismiss();
        });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, Constants.CANCEL, (dialog, which) -> {
            dialog.dismiss();
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    private void addImageIconVisibility(ArrayList<Image> images) {
        if (images.size() < 3) {
            getViewDataBinding().addImage.setVisibility(View.VISIBLE);
        } else {
            getViewDataBinding().addImage.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onBackPressed() {
        viewModel.update();
        return false;
    }

    private class VerticalSpacingDecoration extends RecyclerView.ItemDecoration {

        private int spacing;

        VerticalSpacingDecoration(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = spacing;
        }
    }
}
