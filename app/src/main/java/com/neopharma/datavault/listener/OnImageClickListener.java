package com.neopharma.datavault.listener;

import android.view.View;

public interface OnImageClickListener {
    void onClick(View view, int position);
    void onDelete(View view, int position);
}