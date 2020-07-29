package com.bunakari.sambalpurifashion.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class CustomProgressBar extends AlertDialog {
    protected CustomProgressBar(@NonNull Context context) {
        super(context);
    }

    protected CustomProgressBar(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomProgressBar(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
