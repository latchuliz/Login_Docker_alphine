package com.neopharma.datavault.adapter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.neopharma.datavault.R;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.utility.Utility;

public class BindingAdapters {

    @BindingAdapter("customDrawable")
    public static void setImageDrawable(ImageView view, int drawable) {
        Context context = view.getContext();
        Glide.with(context)
                .load(drawable)
                .into(view);
    }

    @BindingAdapter({"customUrl", "customWidth", "customHeight"})
    public static void setImageUrl(ImageView view, String url, int width, int height) {
        Context context = view.getContext();
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().override(Utility.dpToPx(width), Utility.dpToPx(height))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                )
                .into(view);
    }

    @BindingAdapter({"customUrl", "placeholder"})
    public static void setImageUrl(ImageView view, String url, Drawable resource) {
        Context context = view.getContext();
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        if (resource != null) {
            options = options.error(resource).placeholder(resource);
        }
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(view);
    }

    @BindingAdapter({"customUrl"})
    public static void setImageUrl(ImageView view, String url) {
        setImageUrl(view, url, null);
    }

    @BindingAdapter("visibility")
    public static void setVisibility(View view, boolean b) {
        view.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter({"completedCount", "pendingCount"})
    public static void setCompletedCount(DecoView view, int completedCount, int pendingCount) {
        if (pendingCount > 0) {
            int series1Index = view.addSeries(
                    new SeriesItem.Builder(view.getContext().getResources().getColor(R.color.colorMildBlue))
                            .setRange(Constants.Numbers.ZERO, pendingCount, Constants.Numbers.ZERO)
                            .setLineWidth(120f)
                            .build()
            );
            view.addEvent(new DecoEvent.Builder(completedCount)
                    .setIndex(series1Index)
                    .setDelay(Constants.Numbers.THOUSAND)
                    .build());
        }
    }

    @BindingAdapter("donorStatus")
    public static void setTextColor(TextView view, String status) {
        view.setTextColor(view.getContext().getResources().getColor(!Constants.Results.POSITIVE.equalsIgnoreCase(status) ? R.color.colorGreen : R.color.colorRed));
    }

    @BindingAdapter("adulterantStatus")
    public static void adulterantStatus(TextView view, String status) {
        view.setTextColor(view.getContext().getResources().getColor(!"Passed".equalsIgnoreCase(status) ? R.color.colorGreen : R.color.colorRed));
    }

    @BindingAdapter("onClickShowError")
    public static void showError(TextView view, boolean b) {
        view.setOnClickListener(v -> v.requestFocus());
    }

    @BindingAdapter({"completedCount", "pendingCount"})
    public static void setPercentage(TextView view, int completedCount, int pendingCount) {
        int value = completedCount / (pendingCount == 0 ? 1 : pendingCount);
        String percentage = String.valueOf(Math.ceil(value * 100.0));
        view.setText(String.format("%s%%", percentage));
    }

    @BindingAdapter({"version"})
    public static void version(TextView view, boolean show) {
        try {
            PackageInfo pInfo = view.getContext().getPackageManager().getPackageInfo(view.getContext().getPackageName(), 0);
            view.setText(String.format("v%s", pInfo.versionName));
        } catch (PackageManager.NameNotFoundException ignored) {
        }
    }
}
