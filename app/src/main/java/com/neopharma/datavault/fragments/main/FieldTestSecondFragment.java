package com.neopharma.datavault.fragments.main;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.neopharma.datavault.R;
import com.neopharma.datavault.adapter.ImageListAdapter;
import com.neopharma.datavault.data.local.entity.FieldTest;
import com.neopharma.datavault.data.local.entity.Image;
import com.neopharma.datavault.databinding.FragmentFieldTestSecondBinding;
import com.neopharma.datavault.fragments.CameraFragment;
import com.neopharma.datavault.fragments.ImagePreviewFragment;
import com.neopharma.datavault.fragments.QRScanFragment;
import com.neopharma.datavault.fragments.module.SharedPreferences;
import com.neopharma.datavault.fragments.viewmodel.FieldTestViewModel;
import com.neopharma.datavault.listener.OnImageClickListener;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.model.Constants.FragmentType;
import com.neopharma.datavault.model.kit.Kit;
import com.neopharma.datavault.utility.CustomAlertDialog;
import com.neopharma.datavault.utility.KitHelper;
import com.neopharma.datavault.utility.Store;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class FieldTestSecondFragment extends MainFragment<FragmentFieldTestSecondBinding> {

    @Inject
    public FieldTestViewModel viewModel;

    @Inject
    SharedPreferences pref;

    private FieldTest fieldTest;
    private CustomAlertDialog alertDialog;
    private CameraFragment cameraFragment;
    private QRScanFragment qrScanFragment;

    @Override
    public int layoutId() {
        return R.layout.fragment_field_test_second;
    }

    @Override
    protected String getToolbarTitle() {
        return FragmentType.FIELD_TEST_KIT_INFO;
    }

    private ArrayList<Image> kitImages = new ArrayList<>();
    private ImageListAdapter kitImagesAdapter;
    private List<Kit> kitResponse = new ArrayList<>();
    private boolean isValidationSuccess = false;
    private boolean isValidationSpinnerSuccess = false;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideToolBarItem();
        initFieldTest();
        initKitImages();
        viewModel.setKitImages(kitImages);
        setTamperingType();
        setKitSpinner();

        getViewDataBinding().nextStepThree.setOnClickListener(v -> {
            if (fieldTest.getKitId() == null) {
                isValidationSpinnerSuccess = false;
                getViewDataBinding().kitSpinner.requestFocus();
                TextView errorSpinner = ((TextView) getViewDataBinding().kitSpinner.getSelectedView());
                if (errorSpinner != null) {
                    errorSpinner.setError("");
                    errorSpinner.requestFocus();
                    getViewDataBinding().kitSpinnerError.requestFocus();
                    getViewDataBinding().kitSpinnerError.setError(!kitResponse.isEmpty() ? getResources().getString(R.string.kit_id_alert) : Constants.EMPTY_KIT_ERROR);
                    if (getViewDataBinding().kitImageError.getError() != null)
                        getViewDataBinding().kitImageError.setError(null);
                }
            } else {
                isValidationSpinnerSuccess = true;
            }
            if (isValidationSpinnerSuccess)
                viewModel.getKitImages().observe(this, res -> {
                    if (res.isEmpty()) {
                        isValidationSuccess = false;
                        getViewDataBinding().kitImageError.requestFocus();
                        getViewDataBinding().kitImageError.setError(getString(R.string.kit_image_list_empty));
                    } else {
                        isValidationSuccess = true;
                        getViewDataBinding().kitImageError.setError(null);
                    }
                });
            if (isValidationSuccess && isValidationSpinnerSuccess) {
                if (validator.validate()) {
                    fieldTest.setTestPosition(3);
                    viewModel.update();
                    activity.actionFieldTestSecondFragment_to_FieldTestThirdFragment(false);
                } else {
                    if (getViewDataBinding().tamperingType.getError() != null) {
                        getViewDataBinding().tamperingType.requestFocus();
                        return;
                    }
                    if (getViewDataBinding().tamperingReference.getError() != null) {
                        getViewDataBinding().tamperingReference.requestFocus();
                    }
                }
            }
        });
        getViewDataBinding().addImage.setOnClickListener(v -> startCamera(Constants.ImageType.KIT, kitImages, kitImagesAdapter));
        getViewDataBinding().kitScan.setOnClickListener(v -> scanQR());
        getViewDataBinding().setFieldtest(fieldTest);
    }

    private void setTamperingType() {
        if (fieldTest.getTamperingType() != null) {
            getViewDataBinding().tamperingType.setText(Store.Config.tamperingTypes.get(fieldTest.getTamperingType()));
        }
        List<String> Ids = new ArrayList<>();
        for (Map.Entry<String, String> entry : Store.Config.tamperingTypes.entrySet()) {
            String value = entry.getValue();
            Ids.add(value);
        }
        getViewDataBinding().tamperingType.setAdapter(new ArrayAdapter<String>(activity, R.layout.spinner_item, Ids));
        getViewDataBinding().tamperingType.setEnabled(true);
        getViewDataBinding().tamperingType.setHint(getResources().getString(R.string.tampering_type));
        getViewDataBinding().tamperingType.setOnItemClickListener((parent, view, position, id) -> {
            for (Map.Entry<String, String> entry : Store.Config.tamperingTypes.entrySet()) {
                String value = entry.getValue();
                if (value.equalsIgnoreCase(getViewDataBinding().tamperingType.getText().toString())) {
                    fieldTest.setTamperingType(entry.getKey());
                }
            }
        });

        getViewDataBinding().tamperingType.setOnClickListener(v -> {
            getViewDataBinding().tamperingType.showDropDown();
        });

        getViewDataBinding().tamperingType.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                boolean found = false;
                for (String t : Ids) {
                    if (t.equalsIgnoreCase(getViewDataBinding().tamperingType.getText().toString())) {
                        found = true;
                    }
                }
                if (!found) {
                    getViewDataBinding().tamperingType.setText("");
                    fieldTest.setTamperingType(null);
                }
            } else if (getViewDataBinding().tamperingType.getText().toString().isEmpty()) {
                getViewDataBinding().tamperingType.showDropDown();
                fieldTest.setTamperingType(null);
            }
        });
    }

    private void initFieldTest() {
        fieldTest = viewModel.findOrCreateIncompleteRecord();
        viewModel.setFieldTest(fieldTest);
        kitImages.clear();
        for (Image i : fieldTest.getImages()) {
            if (i.methodName.contains(Constants.ImageType.KIT.name))
                kitImages.add(i);
        }
    }

    private void initKitImages() {
        addImageIconVisibility(kitImages);
        kitImagesAdapter = new ImageListAdapter(kitImages, true);
        initImages(getViewDataBinding().kitImageList, kitImagesAdapter, kitImages);
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
            viewModel.setKitImages(images);
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

    private void addImageIconVisibility(ArrayList<Image> images) {
        if (images.size() < 3) {
            getViewDataBinding().addImage.setVisibility(View.VISIBLE);
        } else {
            getViewDataBinding().addImage.setVisibility(View.GONE);
        }
    }

    private void scanQR() {
        qrScanFragment = new QRScanFragment();
        qrScanFragment.setTargetFragment(this, 7);
        qrScanFragment.show(activity.getSupportFragmentManager(), Constants.CAMERA);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (cameraFragment != null)
            cameraFragment.dismiss();
        if (qrScanFragment != null)
            qrScanFragment.dismiss();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.Numbers.ONE)
            if (isValidKit(data.getStringExtra("kit_id"))) {
                fieldTest.setKitId(data.getStringExtra("kit_id"));
                pref.putBoolean(Constants.Preference.USER_KIT_ALERT, true);
                setKitSpinner();
            } else {
                activity.alertDialog(Constants.KIT_ERROR, Constants.OKAY);
            }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    private boolean isValidKit(String kitId) {
        kitResponse = viewModel.getUnusedKits();
        for (Kit kit : kitResponse) {
            if (kit.serialNo.equals(kitId)) {
                return true;
            }
        }
        return false;
    }

    private void setKitSpinner() {
        if (fieldTest.getTestPosition() > 2) {
            getViewDataBinding().kitSpinner.setEnabled(false);
            getViewDataBinding().kitScan.setEnabled(false);
        }
        getApiKits();

        getViewDataBinding().kitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (getViewDataBinding().kitSpinnerError.getError() != null)
                    getViewDataBinding().kitSpinnerError.setError(null);
                if (kitResponse != null)
                    if (i == 0) {
                        fieldTest.setKitId(null);
                        viewModel.update();
                        pref.putBoolean(Constants.Preference.USER_KIT_ALERT, false);
                    } else {
                        fieldTest.setKitId(kitResponse.get(i - 1).serialNo);
                        viewModel.update();
                        pref.putBoolean(Constants.Preference.USER_KIT_ALERT, true);
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getViewDataBinding().kitSpinner.setOnTouchListener((v, event) -> {
            if (kitResponse.isEmpty()) {
                activity.alertDialog(Constants.EMPTY_KIT, Constants.OKAY);
            } else {
                getViewDataBinding().kitSpinner.performClick();
            }
            return false;
        });
    }

    private void getApiKits() {
        activity.showProgressBar();
        viewModel.getKits().observe(this, res -> {
            if (activity.validResponse(res)) {
                kitResponse = viewModel.getUnusedKits();
                List<String> names = KitHelper.getKitsNames(kitResponse);
                names.add(0, activity.getString(R.string.select_kit));
                getViewDataBinding().kitSpinner.setAdapter(new ArrayAdapter(activity, R.layout.spinner_item, names));
                getViewDataBinding().kitSpinner.setClickable(!kitResponse.isEmpty());
                if (!kitResponse.isEmpty()) {
                    for (int i = 0; i < kitResponse.size(); i++) {
                        if (fieldTest.getKitId() != null && kitResponse.get(i).serialNo.equalsIgnoreCase(fieldTest.getKitId())) {
                            getViewDataBinding().kitSpinner.setSelection(i + 1);
                        }
                    }
                    if (!pref.getBoolean(Constants.Preference.USER_KIT_ALERT)) {
                        activity.alertDialog(Constants.USER_KIT_ALERT, Constants.OKAY);
                        pref.putBoolean(Constants.Preference.USER_KIT_ALERT, true);
                    }
                } else {
                    activity.alertDialog(Constants.EMPTY_KIT, Constants.OKAY);
                }
            }
            activity.hideProgressBar();
        });
    }

    private void startCamera(Constants.ImageType imageType, ArrayList<Image> images, ImageListAdapter imageListAdapter) {
        cameraFragment = new CameraFragment()
                .setImageType(imageType)
                .setPickImageListener(image -> {
                    images.add(image);
                    viewModel.setKitImages(images);
                    imageListAdapter.notifyDataSetChanged();
                    addImageIconVisibility(images);
                });
        cameraFragment.show(getChildFragmentManager(), Constants.CAMERA);
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
