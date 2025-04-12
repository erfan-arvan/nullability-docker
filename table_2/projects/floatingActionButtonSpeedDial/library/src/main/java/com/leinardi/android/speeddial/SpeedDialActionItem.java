package com.leinardi.android.speeddial;
public class SpeedDialActionItem implements Parcelable {
    public static final int NOT_SET = Integer.MIN_VALUE;
    @IdRes
    private final int mId;
    private final String mLabel;
    @DrawableRes
    private final int mFabImageResource;
    private final Drawable mFabImageDrawable;
    @ColorInt
    private final int mFabImageTintColor;
    @ColorInt
    private final int mFabBackgroundColor;
    @ColorInt
    private final int mLabelColor;
    @ColorInt
    private final int mLabelBackgroundColor;
    private final boolean mLabelClickable;
    @FloatingActionButton.Size
    private final int mFabSize;
    @StyleRes
    private final int mTheme;
    private SpeedDialActionItem(Builder builder) {
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
    public int getId() {
        return mId;
    }
    public String getLabel() {
        return mLabel;
    }
    public Drawable getFabImageDrawable(Context context) {
        if (mFabImageDrawable != null) {
            return mFabImageDrawable;
        } else if (mFabImageResource != NOT_SET) {
            return AppCompatResources.getDrawable(context, mFabImageResource);
        } else {
            return null;
        }
    }
    @ColorInt
    public int getFabImageTintColor() {
        return mFabImageTintColor;
    }
    @ColorInt
    public int getFabBackgroundColor() {
        return mFabBackgroundColor;
    }
    @ColorInt
    public int getLabelColor() {
        return mLabelColor;
    }
    public int getLabelBackgroundColor() {
        return mLabelBackgroundColor;
    }
    public boolean isLabelClickable() {
        return mLabelClickable;
    }
    @StyleRes
    public int getTheme() {
        return mTheme;
    }
    @FloatingActionButton.Size
     int getFabSize() {
        return mFabSize;
    }
    public static class Builder {
        @IdRes
        private final int mId;
        @DrawableRes
        private final int mFabImageResource;
        private Drawable mFabImageDrawable;
        @ColorInt
        private int mFabImageTintColor = NOT_SET;
        private String mLabel;
        @ColorInt
        private int mFabBackgroundColor = NOT_SET;
        @ColorInt
        private int mLabelColor = NOT_SET;
        @ColorInt
        private int mLabelBackgroundColor = NOT_SET;
        private boolean mLabelClickable = true;
        @FloatingActionButton.Size
        private int mFabSize = SIZE_AUTO;
        @StyleRes
        private int mTheme = NOT_SET;
        public Builder(@IdRes int id, @DrawableRes int fabImageResource) {
            mId = id;
            mFabImageResource = fabImageResource;
            mFabImageDrawable = null;
        }
        public Builder(@IdRes int id,  Drawable drawable) {
            mId = id;
            mFabImageDrawable = drawable;
            mFabImageResource = NOT_SET;
        }
        public Builder setLabel( String label) {
            mLabel = label;
            return this;
        }
        public Builder setFabImageTintColor(int fabImageTintColor) {
            mFabImageTintColor = fabImageTintColor;
            return this;
        }
        public Builder setFabBackgroundColor(@ColorInt int fabBackgroundColor) {
            mFabBackgroundColor = fabBackgroundColor;
            return this;
        }
        public Builder setLabelColor(@ColorInt int labelColor) {
            mLabelColor = labelColor;
            return this;
        }
        public Builder setLabelBackgroundColor(@ColorInt int labelBackgroundColor) {
            mLabelBackgroundColor = labelBackgroundColor;
            return this;
        }
        public Builder setLabelClickable(boolean labelClickable) {
            mLabelClickable = labelClickable;
            return this;
        }
        public Builder setTheme(int mTheme) {
            this.mTheme = mTheme;
            return this;
        }
        public SpeedDialActionItem create() {
            return new SpeedDialActionItem(this);
        }
         Builder setFabSize(@FloatingActionButton.Size int fabSize) {
            mFabSize = fabSize;
            return this;
        }
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
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
    protected SpeedDialActionItem(Parcel in) {
        this.mId = in.readInt();
        this.mLabel = in.readString();
        this.mFabImageResource = in.readInt();
        this.mFabImageDrawable =
                UiUtils.getDrawableFromBitmap((Bitmap) in.readParcelable(Bitmap.class.getClassLoader()));
        this.mFabImageTintColor = in.readInt();
        this.mFabBackgroundColor = in.readInt();
        this.mLabelColor = in.readInt();
        this.mLabelBackgroundColor = in.readInt();
        this.mLabelClickable = in.readByte() != 0;
        this.mFabSize = in.readInt();
        this.mTheme = in.readInt();
    }
    public static final Creator<SpeedDialActionItem> CREATOR = new Creator<SpeedDialActionItem>() {
        @Override
        public SpeedDialActionItem createFromParcel(Parcel source) {
            return new SpeedDialActionItem(source);
        }
        @Override
        public SpeedDialActionItem[] newArray(int size) {
            return new SpeedDialActionItem[size];
        }
    };
}
