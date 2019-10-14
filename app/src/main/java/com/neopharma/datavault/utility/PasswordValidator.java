package com.neopharma.datavault.utility;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

public class PasswordValidator implements TextWatcher {

    private final TextInputLayout view2;
    private final TextInputEditText view1;
    private boolean enableTextWatcher;
    private int min;
    private final int resource;
    private int minLengthMessageResource;
    private int notEqualResource;
    private int emptyResource;

    public PasswordValidator(TextInputEditText view1, TextInputLayout view2, int resource) {
        this.view1 = view1;
        this.view2 = view2;
        this.resource = resource;
        this.minLengthMessageResource = resource;
        this.notEqualResource = resource;
        this.emptyResource = resource;
        this.view1.addTextChangedListener(this);
    }

    public boolean validate() {
        enableTextWatcher = true;
        if (view1.getText().length() == 0) {
            view2.setPasswordVisibilityToggleEnabled(false);
            view1.setError(view1.getContext().getResources().getString(emptyResource));
            return false;
        } else if (view1.getText().length() < min) {
            view2.setPasswordVisibilityToggleEnabled(false);
            view1.setError(view1.getContext().getResources().getString(minLengthMessageResource));
            return false;
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() != 0 && enableTextWatcher) {
            enableTextWatcher = false;
            view2.setPasswordVisibilityToggleEnabled(true);
            view1.setError(null);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void setMinLength(int min) {
        this.min = min;
    }

    public void setMinLengthMessage(int minLengthMessageResource) {
        this.minLengthMessageResource = minLengthMessageResource;
    }

    public boolean equals(PasswordValidator obj2) {
        enableTextWatcher = true;
        if (!this.view1.getText().toString().equals(obj2.view1.getText().toString())) {
            view2.setPasswordVisibilityToggleEnabled(false);
            view1.setError(view1.getContext().getResources().getString(notEqualResource));
            return false;
        }
        return true;
    }

    public void setNotEqualResource(int notEqualResource) {
        this.notEqualResource = notEqualResource;
    }

    public void setEmptyMessageResouce(int emptyResource) {
        this.emptyResource = emptyResource;
    }
}
