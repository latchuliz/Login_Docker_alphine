package com.neopharma.datavault.fragments.initial;

import androidx.viewpager.widget.ViewPager;

import com.neopharma.datavault.R;
import com.neopharma.datavault.activity.InitialActivity;
import com.neopharma.datavault.adapter.InfoPagerAdapter;
import com.neopharma.datavault.databinding.FragmentInfoBinding;
import com.neopharma.datavault.fragments.module.SharedPreferences;
import com.neopharma.datavault.model.CardItem;
import com.neopharma.datavault.model.Constants.Numbers;
import com.neopharma.datavault.utility.Store;

import javax.inject.Inject;

public class InfoFragment extends InitialFragment<FragmentInfoBinding> {

    @Inject
    public SharedPreferences preferences;

    private InfoPagerAdapter adapter;
    private int position = Numbers.ZERO;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_info;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = new InfoPagerAdapter(getContext());
        adapter.addCardItem(new CardItem(getResources().getString(R.string.donor_identification), getResources().getString(R.string.donor_identification_val), R.drawable.donor_identification));
        adapter.addCardItem(new CardItem(getResources().getString(R.string.kit_id), getResources().getString(R.string.kit_id_val), R.drawable.kit_id));
        adapter.addCardItem(new CardItem(getResources().getString(R.string.kit_result), getResources().getString(R.string.kit_result_val), R.drawable.kit_result));

        getViewDataBinding().viewPager.setAdapter(adapter);
        getViewDataBinding().viewPager.setOffscreenPageLimit(Numbers.THREE);
        getViewDataBinding().indicator.setViewPager(getViewDataBinding().viewPager);

        getViewDataBinding().viewPager.setCurrentItem(position);
        getViewDataBinding().next.setOnClickListener(view -> {
            position = getViewDataBinding().viewPager.getCurrentItem();
            if (position == Numbers.TWO) {
                getViewDataBinding().skip.performClick();
            } else {
                getViewDataBinding().viewPager.setCurrentItem(++position);
            }
        });

        getViewDataBinding().viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getViewDataBinding().skip.setText(getResources().getString(position == Numbers.TWO ? R.string.tmp_done : R.string.tmp_skip));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        getViewDataBinding().skip.setOnClickListener(view -> {
            Store.isInfoSkipped = true;
            ((InitialActivity) getActivity()).actionToSignInFragment();
        });
    }
}
