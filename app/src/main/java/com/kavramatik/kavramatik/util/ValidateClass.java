package com.kavramatik.kavramatik.util;

import android.util.Patterns;
import android.widget.EditText;
import com.kavramatik.kavramatik.R;

public class ValidateClass {

    public static boolean editTextEmailIsNull(EditText editText) {
        String value = editText.getText().toString();
        if (!value.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            return true;
        } else {
            editText.setError(editText.getContext().getString(R.string.check_again));
            return false;
        }
    }

    public static boolean editTextPassword(EditText editText) {
        String value = editText.getText().toString();
        if (!value.isEmpty() && value.length() >= 6) {
            return true;
        } else {
            editText.setError(editText.getContext().getString(R.string.password_length));
            return false;
        }
    }

    public static boolean editTextTextValue(EditText editText) {
        String value = editText.getText().toString();
        if (!value.isEmpty() && value.length() >= 3) {
            return true;
        } else {
            editText.setError(editText.getContext().getString(R.string.check_again));
            return false;
        }
    }

    public static boolean machPassword(EditText p1, EditText p2) {
        String value = p1.getText().toString();
        String valueValidate = p2.getText().toString();
        if (value.equals(valueValidate)) {
            return true;
        } else {
            p2.setError(p2.getContext().getResources().getString(R.string.password_not_match));
            return false;
        }
    }
}
