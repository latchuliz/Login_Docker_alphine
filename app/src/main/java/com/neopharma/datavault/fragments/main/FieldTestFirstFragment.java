package com.neopharma.datavault.fragments.main;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.neopharma.datavault.R;
import com.neopharma.datavault.adapter.ImageListAdapter;
import com.neopharma.datavault.data.local.entity.FieldTest;
import com.neopharma.datavault.data.local.entity.Image;
import com.neopharma.datavault.databinding.FragmentFieldTestFirstBinding;
import com.neopharma.datavault.fragments.CameraFragment;
import com.neopharma.datavault.fragments.ImagePreviewFragment;
import com.neopharma.datavault.fragments.viewmodel.FieldTestViewModel;
import com.neopharma.datavault.fragments.viewmodel.TaskViewModel;
import com.neopharma.datavault.listener.OnBackPressedListener;
import com.neopharma.datavault.listener.OnImageClickListener;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.model.Constants.FragmentType;
import com.neopharma.datavault.model.area.Area;
import com.neopharma.datavault.model.donor.Donor;
import com.neopharma.datavault.model.task.Task;
import com.neopharma.datavault.permissions.PermissionsManager;
import com.neopharma.datavault.utility.CustomAlertDialog;
import com.neopharma.datavault.utility.Store;
import com.neopharma.datavault.utility.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.neopharma.datavault.model.Constants.FieldTest.INCOMPLETE;
import static com.neopharma.datavault.utility.Utility.EMOJI_FILTER;
import static com.neopharma.datavault.utility.Utility.hideKeyboard;

public class FieldTestFirstFragment extends MainFragment<FragmentFieldTestFirstBinding> implements DatePickerDialog.OnDateSetListener, OnBackPressedListener {

    @Inject
    public FieldTestViewModel viewModel;

    @Inject
    public TaskViewModel taskViewModel;

    private FieldTest fieldTest;
    private ArrayList<Image> donorImages = new ArrayList<>();
    private ImageListAdapter donorImagesAdapter;
    private List<Task> tasksResponse;
    private List<Area> areaResponse;
    private CustomAlertDialog alertDialog;
    private CameraFragment cameraFragment;

    @Override
    public int layoutId() {
        return R.layout.fragment_field_test_first;
    }

    @Override
    protected String getToolbarTitle() {
        return FragmentType.FIELD_TEST_DONOR_IDENTIFICATION;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        getViewDataBinding().addIdImage.imageDel.setVisibility(View.INVISIBLE);
        //Don't need api call so moved here
        showBackIcon();
        hideToolBarItem();
        initFieldTest();
        initDonorImages();
        setUI();
        getViewDataBinding().donorAddress.setFilters(new InputFilter[]{EMOJI_FILTER});
        getViewDataBinding().donorSsn.setFilters(new InputFilter[]{EMOJI_FILTER});
        getViewDataBinding().donorEmail.setFilters(new InputFilter[]{EMOJI_FILTER});
        getViewDataBinding().setEmailRegex("((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?");
        getViewDataBinding().radioGenderGroup.setOnCheckedChangeListener((group, checkedId) ->
                getViewDataBinding().testGenderError.setError(null));
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (cameraFragment != null && PermissionsManager.areCameraPermissionsGranted(requireActivity())) {
            cameraFragment.dismiss();
            activity.hideProgressBar();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.menuSelected.post(FragmentType.FIELD_TEST_DONOR_IDENTIFICATION);
        setAreaSpinner();
        setIdTypeSpinner();
        setTaskSpinner();
        viewModel.setDonorImages(donorImages);
        getViewDataBinding().idImageError.setError(null);
        getViewDataBinding().nextStepTwo.setOnClickListener(view -> {
            autoCompleteEmpty();
            //trigger xml validation
            validator.validate();
            if (getViewDataBinding().donorName.getError() != null) {
                getViewDataBinding().donorName.requestFocus();
                return;
            }
            if (getViewDataBinding().donorDob.getError() != null) {
                getViewDataBinding().donorDob.requestFocus();
                return;
            }
            if (getViewDataBinding().radioGenderGroup.getCheckedRadioButtonId() == -1) {
                getViewDataBinding().testGenderError.setError(getString(R.string.no_gender_validation));
                getViewDataBinding().testGenderError.requestFocus();
                return;
            }
            if (getViewDataBinding().donorEmail.getError() != null) {
                getViewDataBinding().donorEmail.requestFocus();
                return;
            }
            String ssn = getViewDataBinding().donorSsn.getText().toString();
            if (ssn.length() < 5 || ssn.length() > 20) {
                getViewDataBinding().donorSsn.setError(getString(R.string.valid_donor_ssn));
                getViewDataBinding().donorSsn.requestFocus();
                return;
            }
            if (getViewDataBinding().donorMobile.getError() != null) {
                getViewDataBinding().donorMobile.requestFocus();
                return;
            }

            if (getViewDataBinding().donorAddress.getError() != null) {
                getViewDataBinding().donorAddress.requestFocus();
                return;
            }

            viewModel.getDonorImages().observe(this, res -> {
                if (res != null && res.isEmpty()) {
                    getViewDataBinding().donorImageError.setError(getString(R.string.donor_image_list_empty));
                    getViewDataBinding().donorImageError.requestFocus();
                } else {
                    //Continue validation once image is validated
                    getViewDataBinding().donorImageError.setError(null);
                    if (getViewDataBinding().idType.getError() != null) {
                        getViewDataBinding().idType.requestFocus();
                        return;
                    }

                    if (getViewDataBinding().idReference.getError() != null) {
                        getViewDataBinding().idReference.requestFocus();
                        return;
                    }
                    if (fieldTest.getIdImage() == null) {
                        getViewDataBinding().idImageError.setError(getString(R.string.donor_id_empty));
                        getViewDataBinding().idImageError.requestFocus();
                        return;
                    }
                    fieldTest.setTestPosition(2);
                    viewModel.update();
                    activity.actionFieldTestFirstFragment_to_FieldTestSecondFragment(false);
                }
            });
        });
        getViewDataBinding().addImage.setOnClickListener(view -> startCamera(Constants.ImageType.DONOR, donorImages, donorImagesAdapter));
        getViewDataBinding().addIdImage.image.setOnClickListener(v -> {
            if (fieldTest.getIdImage() != null) {
                ImagePreviewFragment fragment = new ImagePreviewFragment();
                Bundle b = new Bundle();
                b.putString(Constants.IMAGE_PATH, fieldTest.getIdImage().path);
                fragment.setArguments(b);
                fragment.show(getChildFragmentManager(), Constants.IMAGE_PREVIEW);
            } else {
                cameraFragment = new CameraFragment()
                        .setImageType(Constants.ImageType.ID)
                        .setPickImageListener(image -> {
                            fieldTest.setIdImage(image);
                            viewModel.update();
                            Glide.with(this)
                                    .load(image.path)
                                    .apply(new RequestOptions().override(v.getWidth(), v.getHeight())
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true)
                                    )
                                    .into((ImageView) v);
                            getViewDataBinding().addIdImage.imageDel.setVisibility(View.VISIBLE);
                        });
                cameraFragment.show(getChildFragmentManager(), Constants.CAMERA);
            }
        });
        getViewDataBinding().addIdImage.imageDel.setOnClickListener(v -> {
            showDeleteImage();
        });
        getViewDataBinding().donorDobButton.setOnClickListener(v -> {
            if (getViewDataBinding().donorDob.getText().toString().isEmpty())
                showDob();
            else
                showDatePicker();
        });
        getViewDataBinding().donorName.requestFocus();
    }

    private void autoCompleteEmpty() {
        if (getViewDataBinding().task.getText().toString().isEmpty()) {
            fieldTest.setTaskId(null);
            fieldTest.setTaskName(null);
        }
        if (getViewDataBinding().area.getText().toString().isEmpty()) {
            fieldTest.setArea(null);
            fieldTest.setAreaId(null);
        }
        if (getViewDataBinding().idType.getText().toString().isEmpty()) {
            fieldTest.setIdType(null);
        }
    }

    private void setUI() {
        if (getArguments() != null) {
            Donor donor = new Gson().fromJson(getArguments().getString(Constants.DONOR), Donor.class);
            //TODO CLARIFICATION FOR CASE ID
            if (donor.task != null) {
                fieldTest.setTaskId(donor.task.taskId);
                fieldTest.setTaskName(donor.task.taskName);
                getViewDataBinding().task.setEnabled(false);
            }
            fieldTest.setName(donor.name);
            fieldTest.setMobileNumber(donor.mobileNumber);
            fieldTest.setGender(donor.gender);
            fieldTest.setDob(Utility.formattedDate(donor.dob));
            fieldTest.setEmail(donor.email);
            fieldTest.setSsn(donor.ssn);
            fieldTest.setIdType(donor.idType);
            fieldTest.setIdReferenceNo(donor.idReference);
            fieldTest.setAddress(donor.address);
            fieldTest.setDonorId(donor.id);
            fieldTest.setStatus(INCOMPLETE);
            viewModel.update();
            fieldTest.setTestPosition(2);
            setArguments(null);
        }
        getViewDataBinding().setFieldtest(fieldTest);
    }

    private void initFieldTest() {
        fieldTest = viewModel.findOrCreateIncompleteRecord();
        viewModel.setFieldTest(fieldTest);
        donorImages.clear();
        for (Image i : fieldTest.getImages()) {
            if (i.methodName.contains(Constants.ImageType.DONOR.name))
                donorImages.add(i);
            if (i.methodName.contains(Constants.ImageType.ID.name)) {
                fieldTest.setIdImage(i);
                getViewDataBinding().addIdImage.setImagedata(i);
                getViewDataBinding().addIdImage.imageDel.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initDonorImages() {
        addImageIconVisibility(donorImages);
        donorImagesAdapter = new ImageListAdapter(donorImages, true);
        initImages(getViewDataBinding().imageList, donorImagesAdapter, donorImages);
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
            viewModel.setDonorImages(images);
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

    public void showDeleteImage() {
        Image image = fieldTest.getIdImage();
        alertDialog = new CustomAlertDialog(activity);
        alertDialog.setMessage(Constants.DELETE_ALERT);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, Constants.OKAY, (dialog, which) -> {
            viewModel.removeImage(image);
            ImageView v = getViewDataBinding().addIdImage.image;
            fieldTest.setIdImage(null);
            viewModel.update();
            Glide.with(this)
                    .load(R.drawable.add_image)
                    .apply(new RequestOptions().override(v.getWidth(), v.getHeight())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                    )
                    .into(v);
            getViewDataBinding().addIdImage.imageDel.setVisibility(View.INVISIBLE);
            File fileDelete = new File(image.path);
            fileDelete.delete();
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

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        getViewDataBinding().donorDob.setText(String.format("%d-%d-%d", monthOfYear + 1, dayOfMonth, year));
        if (getViewDataBinding().donorDob.getError() != null)
            getViewDataBinding().donorDob.setError(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    @Override
    public boolean onBackPressed() {
        hideKeyboard(activity);
        if (emptyViewValidation()) {
            viewModel.deleteFieldTest();
            return false;
        } else {
            autoCompleteEmpty();
            activity.discardFieldTestDialog(viewModel);
            return true;
        }
    }

    private boolean emptyViewValidation() {
        return getViewDataBinding().task.getText().toString().trim().isEmpty()
                && getViewDataBinding().donorName.getText().toString().trim().isEmpty()
                && getViewDataBinding().donorDob.getText().toString().trim().isEmpty()
                && getViewDataBinding().donorEmail.getText().toString().trim().isEmpty()
                && getViewDataBinding().donorMobile.getText().toString().trim().isEmpty()
                && getViewDataBinding().donorAddress.getText().toString().trim().isEmpty()
                && getViewDataBinding().idType.getText().toString().trim().isEmpty()
                && getViewDataBinding().idReference.getText().toString().trim().isEmpty()
                && getViewDataBinding().area.getText().toString().trim().isEmpty();
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

    private void startCamera(Constants.ImageType imageType, ArrayList<Image> images, ImageListAdapter imageListAdapter) {
        cameraFragment = new CameraFragment()
                .setImageType(imageType)
                .setPickImageListener(image -> {
                    images.add(image);
                    viewModel.setDonorImages(images);
                    imageListAdapter.notifyDataSetChanged();
                    viewModel.update();
                    addImageIconVisibility(images);
                });
        cameraFragment.show(getChildFragmentManager(), Constants.CAMERA);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dpd;
        String selectDate = getViewDataBinding().donorDob.getText().toString();
        if (!selectDate.isEmpty()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(Utility.formatDate(selectDate));
            dpd = new DatePickerDialog(
                    activity,
                    this,
                    cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH));
        } else {
            dpd = new DatePickerDialog(
                    activity,
                    this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
        }
        calendar.add(Calendar.YEAR, -13);
        dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        dpd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dpd.show();
    }

    private void setTaskSpinner() {
        activity.showProgressBar();
        if (fieldTest.getTestPosition() > 1 && fieldTest.getTaskName() != null) {
            getViewDataBinding().task.setEnabled(false);
        }

        if (fieldTest.getTaskName() != null) {
            getViewDataBinding().task.setText(fieldTest.getTaskName());
            getViewDataBinding().task.setSelection(getViewDataBinding().task.getText().toString().length());
        }

        taskViewModel.getAdminTasks().observe(this, res -> {
            if (activity.validResponse(res)) {
                tasksResponse = res.data.tasks;
                if (!res.data.getTaskNames().isEmpty()) {
                    getViewDataBinding().task.setAdapter(new ArrayAdapter(activity, R.layout.spinner_item, res.data.getTaskNames()));
                    if (fieldTest.getTestPosition() == 0) {
                        getViewDataBinding().task.setEnabled(true);
                        getViewDataBinding().task.setHint(getResources().getString(R.string.select_task));
                    }
                } else {
                    getViewDataBinding().task.setEnabled(false);
                    getViewDataBinding().task.setHint(getResources().getString(R.string.no_task));
                }
                if (fieldTest.getTaskName() != null)
                    getViewDataBinding().task.requestFocus();
            }
            activity.hideProgressBar();
        });

        getViewDataBinding().task.setOnItemClickListener((parent, view, position, id) -> {
            for (Task t : tasksResponse) {
                if (t.name.equalsIgnoreCase(getViewDataBinding().task.getText().toString())) {
                    fieldTest.setTaskId(t.id);
                    fieldTest.setTaskName(t.name);
                }
            }
        });

        getViewDataBinding().task.setOnClickListener(v -> {
            getViewDataBinding().task.showDropDown();
        });

        getViewDataBinding().task.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                boolean found = false;
                for (Task t : tasksResponse) {
                    if (t.name.equalsIgnoreCase(getViewDataBinding().task.getText().toString())) {
                        found = true;
                    }
                }
                if (!found && fieldTest.getTaskName() == null) {
                    //Clear the search if the task is not found.
                    getViewDataBinding().task.setText("");
                }
            } else if (getViewDataBinding().task.getText().toString().isEmpty()) {
                getViewDataBinding().task.showDropDown();
            }
        });
    }

    private void setAreaSpinner() {
        activity.showProgressBar();

        if (fieldTest.getArea() != null) {
            getViewDataBinding().area.setText(fieldTest.getArea());
            getViewDataBinding().area.setSelection(getViewDataBinding().area.getText().toString().length());
        }

        viewModel.getAreas().observe(this, res -> {
            if (activity.validResponse(res)) {
                areaResponse = res.data.areas;
                if (!res.data.getAreaNames().isEmpty()) {
                    getViewDataBinding().area.setAdapter(new ArrayAdapter<>(activity, R.layout.spinner_item, res.data.getAreaNames()));
                    getViewDataBinding().area.setEnabled(true);
                    getViewDataBinding().area.setHint(getResources().getString(R.string.select_area));
                } else {
                    getViewDataBinding().area.setEnabled(false);
                    getViewDataBinding().area.setHint(getResources().getString(R.string.no_area));
                }
                if (fieldTest.getArea() != null)
                    getViewDataBinding().area.requestFocus();
            }
            activity.hideProgressBar();
        });

        getViewDataBinding().area.setOnItemClickListener((parent, view, position, id) -> {
            for (Area t : areaResponse) {
                if (t.area.equalsIgnoreCase(getViewDataBinding().area.getText().toString())) {
                    fieldTest.setArea(t.area);
                    fieldTest.setAreaId(t.id);
                }
            }
        });

        getViewDataBinding().area.setOnClickListener(v -> {
            getViewDataBinding().area.showDropDown();
        });

        getViewDataBinding().area.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                boolean found = false;
                for (Area t : areaResponse) {
                    if (t.area.equalsIgnoreCase(getViewDataBinding().area.getText().toString())) {
                        found = true;
                    }
                }
                if (!found) {
                    //Clear the search if the task is not found.
                    getViewDataBinding().area.setText("");
                }
            } else if (getViewDataBinding().area.getText().toString().isEmpty()) {
                getViewDataBinding().area.showDropDown();
                fieldTest.setArea(null);
                fieldTest.setAreaId(null);
            }
        });
    }

    private void setIdTypeSpinner() {
        if (fieldTest.getIdType() != null) {
            for (Map.Entry<String, String> entry : Store.Config.idTypes.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equalsIgnoreCase(fieldTest.getIdType())) {
                    getViewDataBinding().idType.setText(value);
                }
            }
        }
        List<String> Ids = new ArrayList<>();
        for (Map.Entry<String, String> entry : Store.Config.idTypes.entrySet()) {
            String value = entry.getValue();
            Ids.add(value);
        }
        getViewDataBinding().idType.setAdapter(new ArrayAdapter<String>(activity, R.layout.spinner_item, Ids));
        getViewDataBinding().idType.setEnabled(true);
        getViewDataBinding().idType.setHint(getResources().getString(R.string.select_id_type));
        getViewDataBinding().idType.setOnItemClickListener((parent, view, position, id) -> {
            for (Map.Entry<String, String> entry : Store.Config.idTypes.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (value.equalsIgnoreCase(getViewDataBinding().idType.getText().toString())) {
                    fieldTest.setIdType(key);
                }
            }
        });

        getViewDataBinding().idType.setOnClickListener(v -> {
            getViewDataBinding().idType.showDropDown();
        });

        getViewDataBinding().idType.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                boolean found = false;
                for (String t : Ids) {
                    if (t.equalsIgnoreCase(getViewDataBinding().idType.getText().toString())) {
                        found = true;
                    }
                }
                if (!found) {
                    getViewDataBinding().idType.setText("");
                    fieldTest.setIdType(null);
                }
            } else if (getViewDataBinding().idType.getText().toString().isEmpty()) {
                getViewDataBinding().idType.showDropDown();
                fieldTest.setIdType(null);
            }
        });
    }

    public void showDob() {
        alertDialog = new CustomAlertDialog(activity);
        alertDialog.setMessage(Constants.DOB_ALERT);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, Constants.OKAY, (dialog, which) -> {
            showDatePicker();
            dialog.dismiss();
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
