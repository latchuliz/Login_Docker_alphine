package com.neopharma.datavault.fragments.main;

import android.os.Handler;
import androidx.fragment.app.FragmentManager;

import com.neopharma.datavault.R;
import com.neopharma.datavault.databinding.FragmentFieldTestSuccessBinding;
import com.neopharma.datavault.listener.OnBackPressedListener;
import com.neopharma.datavault.model.Constants.FragmentType;

public class FieldTestSuccessFragment extends MainFragment<FragmentFieldTestSuccessBinding> implements OnBackPressedListener {

    private Handler popBackStackHandler;
    private Runnable popstack;

    @Override
    public int layoutId() {
        return R.layout.fragment_field_test_success;
    }

    @Override
    protected String getToolbarTitle() {
        return FragmentType.FIELD_TEST_TEST_RESULT;
    }

    private void popStackQueue() {
        activity.getSupportFragmentManager().popBackStack(FieldTestFirstFragment.class.getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideToolBarItem();
        popBackStackHandler = new Handler();
        popstack = () -> popStackQueue();
        popBackStackHandler.postDelayed(popstack, 2000);
    }

    @Override
    public boolean onBackPressed() {
        if (popBackStackHandler != null && popstack != null) {
            popBackStackHandler.removeCallbacks(popstack);
        }
        popStackQueue();
        return true;
    }

    @Override
    public void onPause() {
        if (popBackStackHandler != null && popstack != null) {
            popBackStackHandler.removeCallbacks(popstack);
        }
        super.onPause();
    }
}
