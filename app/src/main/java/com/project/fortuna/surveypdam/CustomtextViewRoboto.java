package com.project.fortuna.surveypdam;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Akautsar Efendi on 11/23/2017.
 */

public class CustomtextViewRoboto extends TextView {

    public CustomtextViewRoboto(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public CustomtextViewRoboto(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public CustomtextViewRoboto(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("roboto.light.ttf", context);
        setTypeface(customFont);
    }
}
