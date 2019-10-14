package com.neopharma.datavault.listener;

/**
 * Custom OnBackPressedListener for fragments
 * onBackPressed should return true super.onBackPressed not called.
 */
public interface OnBackPressedListener {
    boolean onBackPressed();
}