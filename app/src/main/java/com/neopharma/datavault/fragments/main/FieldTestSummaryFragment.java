package com.neopharma.datavault.fragments.main;

import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.neopharma.datavault.R;
import com.neopharma.datavault.adapter.ImageListAdapter;
import com.neopharma.datavault.data.local.entity.FieldTest;
import com.neopharma.datavault.data.local.entity.Image;
import com.neopharma.datavault.databinding.FragmentFieldTestSummaryBinding;
import com.neopharma.datavault.fragments.ImagePreviewFragment;
import com.neopharma.datavault.fragments.module.SharedPreferences;
import com.neopharma.datavault.fragments.viewmodel.FieldTestViewModel;
import com.neopharma.datavault.listener.OnImageClickListener;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.model.Constants.FragmentType;
import com.neopharma.datavault.utility.Store;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

public class FieldTestSummaryFragment extends MainFragment<FragmentFieldTestSummaryBinding> {

    @Inject
    public FieldTestViewModel viewModel;
    private FieldTest fieldTest;

    @Inject
    SharedPreferences pref;

    @Override
    public int layoutId() {
        return R.layout.fragment_field_test_summary;
    }

    @Override
    protected String getToolbarTitle() {
        return FragmentType.SUMMARY;
    }

    private ArrayList<Image> donorImages = new ArrayList<>();
    private ArrayList<Image> kitImages = new ArrayList<>();
    private ArrayList<Image> testImages = new ArrayList<>();
    private ImageListAdapter donorSummaryImagesAdapter;
    private ImageListAdapter kitSummaryImagesAdapter;
    private ImageListAdapter testSummaryImagesAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideToolBarItem();
        initFieldTest();
        initDonorSummaryImages();
        initKitSummaryImages();
        initTestSummaryImages();

        getViewDataBinding().sumSubmit.setOnClickListener(v -> {
            fieldTest.setStatus(Constants.FieldTest.PENDING);
            viewModel.update();
            pref.putBoolean(Constants.Preference.USER_KIT_ALERT, false);
            activity.actionFieldTestSummaryFragment_to_FieldTestSuccessFragment(false);
        });

        getViewDataBinding().sumCancel.setOnClickListener(v -> activity.getSupportFragmentManager().popBackStack(FieldTestFirstFragment.class.getSimpleName(), 0));
        getViewDataBinding().setFieldtest(fieldTest);
        getViewDataBinding().idImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePreviewFragment fragment = new ImagePreviewFragment();
                Bundle b = new Bundle();
                b.putString(Constants.IMAGE_PATH, fieldTest.getIdImage().path);
                fragment.setArguments(b);
                fragment.show(getChildFragmentManager(), Constants.IMAGE_PREVIEW);
            }
        });
    }

    private void initFieldTest() {
        fieldTest = viewModel.findOrCreateIncompleteRecord();
        donorImages.clear();
        kitImages.clear();
        testImages.clear();
        for (Image i : fieldTest.getImages()) {
            if (i.methodName.contains(Constants.ImageType.DONOR.name))
                donorImages.add(i);
            if (i.methodName.contains(Constants.ImageType.KIT.name))
                kitImages.add(i);
            if (i.methodName.contains(Constants.ImageType.TEST.name))
                testImages.add(i);
            if(i.methodName.contains(Constants.ImageType.ID.name))
                fieldTest.setIdImage(i);
        }
        viewModel.setFieldTest(fieldTest);
        getViewDataBinding().idType.setText(Store.Config.idTypes.get(fieldTest.getIdType()));
        getViewDataBinding().tamperingType.setText(Store.Config.tamperingTypes.get(fieldTest.getTamperingType()));
    }

    private void initDonorSummaryImages() {
        donorSummaryImagesAdapter = new ImageListAdapter(donorImages, false);
        initImages(getViewDataBinding().sumImageList, donorSummaryImagesAdapter, donorImages);
    }

    private void initKitSummaryImages() {
        kitSummaryImagesAdapter = new ImageListAdapter(kitImages, false);
        initImages(getViewDataBinding().sumKitImageList, kitSummaryImagesAdapter, kitImages);
    }

    private void initTestSummaryImages() {
        testSummaryImagesAdapter = new ImageListAdapter(testImages, false);
        initImages(getViewDataBinding().sumTestImageList, testSummaryImagesAdapter, testImages);
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
                File fileDelete = new File(images.get(position).path);
                fileDelete.delete();
                Image image = images.remove(position);
                adapter.notifyDataSetChanged();
                viewModel.removeImage(image);
            }
        });
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
