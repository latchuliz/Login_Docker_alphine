package com.neopharma.datavault.navigation;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import com.neopharma.datavault.R;

public class Navigator {
    private AppCompatActivity a;
    private Bundle b;
    private int f;
    private boolean fin = false;
    private boolean aTB = false; //Add to backstack

    private boolean cBS = false; //Clear backstack

    public Navigator clearBackStack(boolean cBS) {
        this.cBS = cBS;
        return this;
    }

    public void init(AppCompatActivity a) {
        this.a = a;
    }

    public Navigator bundle(Bundle b) {
        this.b = b;
        return this;
    }

    public Navigator flag(int f) {
        this.f = f;
        return this;
    }

    public Navigator finish(boolean fin) {
        this.fin = fin;
        return this;
    }

    public Navigator backStack(boolean aTB) {
        this.aTB = aTB;
        return this;
    }

    public void navigate(Class c) {
        Intent i = new Intent(a, c);
        if (b != null)
            i.putExtras(b);
        if (f != 0)
            i.addFlags(f);
        a.startActivity(i);
        if (fin) {
            a.finish();
        }
    }

    public void navigate(Fragment f) {
        if (isSameFragment(f))
            return;
        if (cBS) {
            clearBackStack();
        }
        if (b != null)
            f.setArguments(b);

        FragmentTransaction t = a.getSupportFragmentManager().beginTransaction();
        //Animation
        t.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out
        );

        t.replace(R.id.container, f, f.getClass().getSimpleName());
        if (aTB)
            t.addToBackStack(f.getClass().getSimpleName());

        t.commit();
    }

    private boolean isSameFragment(Fragment f) {
        return getCurrentFragment() != null && getCurrentFragment().getClass().getSimpleName().equals(f.getClass().getSimpleName());
    }

    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = a.getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            return fragmentManager.findFragmentByTag(fragmentTag);
        }
        return null;
    }

    public void clearBackStack() {
        FragmentManager fm = a.getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }
}
