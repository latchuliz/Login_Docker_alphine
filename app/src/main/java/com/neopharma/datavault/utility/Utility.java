package com.neopharma.datavault.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.neopharma.datavault.BuildConfig;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {

    private static AlertDialog alertDialog;

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static String sha256HashString(String phrase) {
        MessageDigest md;
        String sha256HashString = "";
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(phrase.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            sha256HashString = String.format("%064x", new BigInteger(1, digest));
        } catch (NoSuchAlgorithmException ignored) {
        }
        return sha256HashString;
    }

    public static String md5String(String phrase) {
        MessageDigest md;
        StringBuilder md5String = new StringBuilder();
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(phrase.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            for (byte aMessageDigest : digest) {
                StringBuilder h = new StringBuilder(Integer.toHexString(0xFF & aMessageDigest));
                while (h.length() < 2)
                    h.insert(0, "0");
                md5String.append(h);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return md5String.toString();
    }

    public static String formattedDate(String datePhrase) {
        String formattedDate = "";
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(datePhrase);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            formattedDate = sdf.format(date);
        } catch (ParseException ignored) {

        }
        return formattedDate;
    }

    public static String formattedUTCDate(String datePhrase) {
        String formattedDate = "";
        try {
            Date date = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(datePhrase);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            formattedDate = sdf.format(date);
        } catch (ParseException ignored) {

        }
        return formattedDate;
    }

    public static Date formatDate(String datePhrase) {
        try {
            return new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(datePhrase);
        } catch (ParseException e) {

        }
        return null;
    }

    public static void log(String msg) {
        if (BuildConfig.DEBUG)
            Log.println(Log.ASSERT, "LOGGER", msg);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static InputFilter EMOJI_FILTER = (source, start, end, dest, dstart, dend) -> {
        for (int index = start; index < end; index++) {
            int type = Character.getType(source.charAt(index));
            if (type == Character.SURROGATE) {
                return "";
            }
        }
        return null;
    };

    public static String toUpperCaseWord(String word) {
        if (word != null && !word.isEmpty()) {
            StringBuilder sb = new StringBuilder(word);
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            return sb.toString();
        }
        return word;
    }
}
