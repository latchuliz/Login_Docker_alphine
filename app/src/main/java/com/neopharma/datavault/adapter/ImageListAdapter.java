package com.neopharma.datavault.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neopharma.datavault.BR;
import com.neopharma.datavault.R;
import com.neopharma.datavault.data.local.entity.Image;
import com.neopharma.datavault.databinding.AdapterImagelistBinding;
import com.neopharma.datavault.listener.OnImageClickListener;

import java.util.List;


public class ImageListAdapter extends RecyclerView.Adapter<BaseViewHolders<AdapterImagelistBinding>> {

    private List<Image> images;
    private OnImageClickListener listener;
    private boolean showDeleteIcon;

    public ImageListAdapter(List<Image> images, boolean showDeleteIcon) {
        this.images = images;
        this.showDeleteIcon = showDeleteIcon;
    }

    @NonNull
    @Override
    public BaseViewHolders<AdapterImagelistBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolders<AdapterImagelistBinding>(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_imagelist, parent, false)) {
            @Override
            public int getVariable() {
                return BR.imagedata;
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolders<AdapterImagelistBinding> holder, int position) {
        holder.bind(this.images.get(position));
        holder.getBinding().imageDel.setOnClickListener(view -> listener.onDelete(view, position));
        holder.getBinding().imageLyt.setOnClickListener(view -> listener.onClick(view, position));
        holder.getBinding().imageDel.setVisibility(showDeleteIcon ? View.VISIBLE : View.GONE);
    }

    public void setOnClickListener(OnImageClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
