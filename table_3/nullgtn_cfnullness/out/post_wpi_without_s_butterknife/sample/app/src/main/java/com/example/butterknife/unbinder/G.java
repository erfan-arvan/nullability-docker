package com.example.butterknife.unbinder;

import android.support.annotation.ColorInt;
import android.view.View;
import butterknife.BindView;
import butterknife.BindColor;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.support.annotation.Nullable;

public class G extends E {

    @BindColor(android.R.color.darker_gray)
    @ColorInt
    int grayColor;

    @BindView(android.R.id.button2)
    View button2;

    public G(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    @OnClick(android.R.id.content)
    public void onClick() {
    }
}
