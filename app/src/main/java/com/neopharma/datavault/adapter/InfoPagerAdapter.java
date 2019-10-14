package com.neopharma.datavault.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neopharma.datavault.BR;
import com.neopharma.datavault.R;
import com.neopharma.datavault.databinding.AdapterInfoViewBinding;
import com.neopharma.datavault.model.CardItem;

import java.util.ArrayList;
import java.util.List;

public class InfoPagerAdapter extends PagerAdapter {

    private final List<CardItem> mData;
    private Context context;
    private AdapterInfoViewBinding binding;

    public InfoPagerAdapter(Context context) {
        mData = new ArrayList<>();
        this.context = context;
    }

    public void addCardItem(CardItem item) {
        mData.add(item);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.adapter_info_view, container, false);
        View view = binding.getRoot();
        container.addView(view);
        binding.setVariable(BR.cardItem, mData.get(position));
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
