package com.mainstreetcode.teammate.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;


public class SettingsItem {

    @StringRes private final int stringRes;
    @DrawableRes private final int drawableRes;

    public SettingsItem(@StringRes int stringRes, @DrawableRes int drawableRes) {
        this.stringRes = stringRes;
        this.drawableRes = drawableRes;
    }

    @StringRes
    public int getStringRes() {
        return stringRes;
    }

    @DrawableRes
    public int getDrawableRes() {
        return drawableRes;
    }
}