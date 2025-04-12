/*
 * Copyright 2018 Roberto Leinardi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.leinardi.android.speeddial;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.content.res.AppCompatResources;
import static android.support.design.widget.FloatingActionButton.SIZE_AUTO;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class SpeedDialActionItem implements Parcelable {

    public static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int NOT_SET = Integer.MIN_VALUE;

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int mId;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull String mLabel;

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int mFabImageResource;

    private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull Drawable mFabImageDrawable;

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int mFabImageTintColor;

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int mFabBackgroundColor;

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int mLabelColor;

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int mLabelBackgroundColor;

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean mLabelClickable;

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int mFabSize;

    private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int mTheme;

    @org.checkerframework.dataflow.qual.SideEffectFree
    private SpeedDialActionItem(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder builder) {
        mId = builder.mId;
        mLabel = builder.mLabel;
        mFabImageTintColor = builder.mFabImageTintColor;
        mFabImageResource = builder.mFabImageResource;
        mFabImageDrawable = builder.mFabImageDrawable;
        mFabBackgroundColor = builder.mFabBackgroundColor;
        mLabelColor = builder.mLabelColor;
        mLabelBackgroundColor = builder.mLabelBackgroundColor;
        mLabelClickable = builder.mLabelClickable;
        mFabSize = builder.mFabSize;
        mTheme = builder.mTheme;
    }

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int getId() {
        return mId;
    }

    @org.checkerframework.dataflow.qual.Pure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String getLabel() {
        return mLabel;
    }

    /**
     * Gets the current Drawable, or null if no Drawable has been assigned.
     *
     * @param context A context to retrieve the Drawable from (needed for SpeedDialActionItem.Builder(int, int).
     * @return the speed dial item drawable, or null if no drawable has been assigned.
     */
    @org.checkerframework.dataflow.qual.Impure
    public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Drawable getFabImageDrawable(Context context) {
        if (mFabImageDrawable != null) {
            return mFabImageDrawable;
        } else if (mFabImageResource != NOT_SET) {
            return AppCompatResources.getDrawable(context, mFabImageResource);
        } else {
            return null;
        }
    }

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int getFabImageTintColor() {
        return mFabImageTintColor;
    }

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int getFabBackgroundColor() {
        return mFabBackgroundColor;
    }

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int getLabelColor() {
        return mLabelColor;
    }

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int getLabelBackgroundColor() {
        return mLabelBackgroundColor;
    }

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isLabelClickable() {
        return mLabelClickable;
    }

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int getTheme() {
        return mTheme;
    }

    // Disabled due to https://issuetracker.google.com/issues/77303906
    @org.checkerframework.dataflow.qual.Pure
    /* public */
     @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int getFabSize() {
        return mFabSize;
    }

    public static class Builder {

        private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int mId;

        private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int mFabImageResource;

        private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull Drawable mFabImageDrawable;

        private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int mFabImageTintColor = NOT_SET;

        private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull String mLabel;

        private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int mFabBackgroundColor = NOT_SET;

        private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int mLabelColor = NOT_SET;

        private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int mLabelBackgroundColor = NOT_SET;

        private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean mLabelClickable = true;

        private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int mFabSize = SIZE_AUTO;

        private  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int mTheme = NOT_SET;

        /**
         * Creates a builder for a speed dial action item that uses the a {@link DrawableRes} as icon.
         * <p class="note">This does Bitmap reading and decoding on the UI thread, which can cause a latency hiccup.
         * If that's a concern, consider using Builder(int, Drawable) instead.</p>
         *
         * @param id               the identifier for this action item. The identifier must be unique to the instance
         *                         of {@link SpeedDialView}. The identifier should be a positive number.
         * @param fabImageResource resId the resource identifier of the drawable
         */
        @org.checkerframework.dataflow.qual.SideEffectFree
        public Builder(int id, int fabImageResource) {
            mId = id;
            mFabImageResource = fabImageResource;
            mFabImageDrawable = null;
        }

        /**
         * Creates a builder for a speed dial action item that uses the a {@link Drawable} as icon.
         *
         * @param id       the identifier for this action item. The identifier must be unique to the instance
         *                 of {@link SpeedDialView}. The identifier should be a positive number.
         * @param drawable the Drawable to set, or null to clear the content
         */
        @org.checkerframework.dataflow.qual.SideEffectFree
        public Builder(int id, Drawable drawable) {
            mId = id;
            mFabImageDrawable = drawable;
            mFabImageResource = NOT_SET;
        }

        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder setLabel(String label) {
            mLabel = label;
            return this;
        }

        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder setFabImageTintColor(int fabImageTintColor) {
            mFabImageTintColor = fabImageTintColor;
            return this;
        }

        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder setFabBackgroundColor(int fabBackgroundColor) {
            mFabBackgroundColor = fabBackgroundColor;
            return this;
        }

        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder setLabelColor(int labelColor) {
            mLabelColor = labelColor;
            return this;
        }

        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder setLabelBackgroundColor(int labelBackgroundColor) {
            mLabelBackgroundColor = labelBackgroundColor;
            return this;
        }

        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder setLabelClickable(boolean labelClickable) {
            mLabelClickable = labelClickable;
            return this;
        }

        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder setTheme(int mTheme) {
            this.mTheme = mTheme;
            return this;
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull SpeedDialActionItem create() {
            return new SpeedDialActionItem(this);
        }

        // Disabled due to https://issuetracker.google.com/issues/77303906
        /* public */
        @org.checkerframework.dataflow.qual.Impure
        @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Builder setFabSize(int fabSize) {
            mFabSize = fabSize;
            return this;
        }
    }

    @org.checkerframework.dataflow.qual.Pure
    public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int describeContents(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull SpeedDialActionItem this) {
        return 0;
    }

    @org.checkerframework.dataflow.qual.Impure
    public void writeToParcel(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull SpeedDialActionItem this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Parcel dest,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mLabel);
        dest.writeInt(this.mFabImageResource);
        dest.writeParcelable(UiUtils.getBitmapFromDrawable(this.mFabImageDrawable), flags);
        dest.writeInt(this.mFabImageTintColor);
        dest.writeInt(this.mFabBackgroundColor);
        dest.writeInt(this.mLabelColor);
        dest.writeInt(this.mLabelBackgroundColor);
        dest.writeByte(this.mLabelClickable ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mFabSize);
        dest.writeInt(this.mTheme);
    }

    @org.checkerframework.dataflow.qual.Impure
    protected SpeedDialActionItem(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Parcel in) {
        this.mId = in.readInt();
        this.mLabel = in.readString();
        this.mFabImageResource = in.readInt();
        this.mFabImageDrawable = UiUtils.getDrawableFromBitmap((Bitmap) in.readParcelable(Bitmap.class.getClassLoader()));
        this.mFabImageTintColor = in.readInt();
        this.mFabBackgroundColor = in.readInt();
        this.mLabelColor = in.readInt();
        this.mLabelBackgroundColor = in.readInt();
        this.mLabelClickable = in.readByte() != 0;
        this.mFabSize = in.readInt();
        this.mTheme = in.readInt();
    }

    public static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Creator<SpeedDialActionItem> CREATOR = new Creator<SpeedDialActionItem>() {

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull SpeedDialActionItem createFromParcel(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Parcel source) {
            return new SpeedDialActionItem(source);
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull SpeedDialActionItem @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull [] newArray( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int size) {
            return new SpeedDialActionItem[size];
        }
    };
}
