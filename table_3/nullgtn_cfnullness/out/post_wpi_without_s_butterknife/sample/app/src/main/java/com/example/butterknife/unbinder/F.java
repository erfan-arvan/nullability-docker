package com.example.butterknife.unbinder;

import android.support.annotation.ColorInt;
import android.view.View;
import butterknife.BindColor;
import butterknife.ButterKnife;
import android.support.annotation.Nullable;

public final class F extends D {

    @BindColor(android.R.color.background_light)
    @ColorInt
    int backgroundLightColor;

    public F(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
