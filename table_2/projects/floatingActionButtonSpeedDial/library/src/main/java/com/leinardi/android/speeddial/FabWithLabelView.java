package com.leinardi.android.speeddial;
final class FabWithLabelView extends LinearLayout {
    private static final String TAG = FabWithLabelView.class.getSimpleName();
    private TextView mLabelTextView;
    private FloatingActionButton mFab;
    private CardView mLabelCardView;
    private boolean mIsLabelEnable;
    private SpeedDialActionItem mSpeedDialActionItem;
    private OnActionSelectedListener mOnActionSelectedListener;
    @FloatingActionButton.Size
    private int mCurrentFabSize;
    public FabWithLabelView(Context context) {
        super(context);
        init(context, null);
    }
    public FabWithLabelView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public FabWithLabelView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        getFab().setVisibility(visibility);
        if (isLabelEnable()) {
            getLabelBackground().setVisibility(visibility);
        }
    }
    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
        setFabSize(mCurrentFabSize);
        if (orientation == VERTICAL) {
            setLabelEnable(false);
        } else {
            setLabel(mLabelTextView.getText().toString());
        }
    }
    public boolean isLabelEnable() {
        return mIsLabelEnable;
    }
    private void setLabelEnable(boolean enable) {
        mIsLabelEnable = enable;
        mLabelCardView.setVisibility(enable ? View.VISIBLE : View.GONE);
    }
    public CardView getLabelBackground() {
        return mLabelCardView;
    }
    public FloatingActionButton getFab() {
        return mFab;
    }
    public SpeedDialActionItem getSpeedDialActionItem() {
        return mSpeedDialActionItem;
    }
    public void setSpeedDialActionItem(SpeedDialActionItem actionItem) {
        mSpeedDialActionItem = actionItem;
        setId(actionItem.getId());
        setLabel(actionItem.getLabel());
        SpeedDialActionItem speedDialActionItem = getSpeedDialActionItem();
        setLabelClickable(speedDialActionItem != null && speedDialActionItem.isLabelClickable());
        int iconTintColor = actionItem.getFabImageTintColor();
        Drawable drawable = actionItem.getFabImageDrawable(getContext());
        if (drawable != null && iconTintColor != NOT_SET) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable.mutate(), iconTintColor);
        }
        setFabIcon(drawable);
        int fabBackgroundColor = actionItem.getFabBackgroundColor();
        if (fabBackgroundColor == NOT_SET) {
            fabBackgroundColor = UiUtils.getPrimaryColor(getContext());
        }
        setFabBackgroundColor(fabBackgroundColor);
        int labelColor = actionItem.getLabelColor();
        if (labelColor == NOT_SET) {
            labelColor = ResourcesCompat.getColor(getResources(), R.color.sd_label_text_color,
                    getContext().getTheme());
        }
        setLabelColor(labelColor);
        int labelBackgroundColor = actionItem.getLabelBackgroundColor();
        if (labelBackgroundColor == NOT_SET) {
            labelBackgroundColor = ResourcesCompat.getColor(getResources(), R.color.cardview_light_background,
                    getContext().getTheme());
        }
        setLabelBackgroundColor(labelBackgroundColor);
        if (actionItem.getFabSize() == SIZE_AUTO) {
            getFab().setSize(SIZE_MINI);
        } else {
            getFab().setSize(actionItem.getFabSize());
        }
        setFabSize(actionItem.getFabSize());
    }
    public void setOnActionSelectedListener(OnActionSelectedListener listener) {
        mOnActionSelectedListener = listener;
        if (mOnActionSelectedListener != null) {
            getFab().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    SpeedDialActionItem speedDialActionItem = getSpeedDialActionItem();
                    if (mOnActionSelectedListener != null
                            && speedDialActionItem != null) {
                        mOnActionSelectedListener.onActionSelected(speedDialActionItem);
                    }
                }
            });
            getLabelBackground().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpeedDialActionItem speedDialActionItem = getSpeedDialActionItem();
                    if (mOnActionSelectedListener != null
                            && speedDialActionItem != null
                            && speedDialActionItem.isLabelClickable()
                            && isLabelEnable()) {
                        mOnActionSelectedListener.onActionSelected(speedDialActionItem);
                    }
                }
            });
        } else {
            getFab().setOnClickListener(null);
            getLabelBackground().setOnClickListener(null);
        }
    }
    private void init(Context context,  AttributeSet attrs) {
        View rootView = inflate(context, R.layout.sd_fab_with_label_view, this);
        mFab = rootView.findViewById(R.id.fab);
        mLabelTextView = rootView.findViewById(R.id.label);
        mLabelCardView = rootView.findViewById(R.id.label_container);
        setFabSize(SIZE_MINI);
        setOrientation(LinearLayout.HORIZONTAL);
        setClipChildren(false);
        setClipToPadding(false);
        TypedArray attr = context.obtainStyledAttributes(attrs,
                R.styleable.FabWithLabelView, 0, 0);
        try {
            @DrawableRes int src = attr.getResourceId(R.styleable.FabWithLabelView_srcCompat, NOT_SET);
            if (src == NOT_SET) {
                src = attr.getResourceId(R.styleable.FabWithLabelView_android_src, NOT_SET);
            }
            SpeedDialActionItem.Builder builder = new SpeedDialActionItem.Builder(getId(), src);
            String labelText = attr.getString(R.styleable.FabWithLabelView_fabLabel);
            builder.setLabel(labelText);
            @ColorInt int fabBackgroundColor = UiUtils.getPrimaryColor(context);
            fabBackgroundColor = attr.getColor(R.styleable.FabWithLabelView_fabBackgroundColor, fabBackgroundColor);
            builder.setFabBackgroundColor(fabBackgroundColor);
            @ColorInt int labelColor = NOT_SET;
            labelColor = attr.getColor(R.styleable.FabWithLabelView_fabLabelColor, labelColor);
            builder.setLabelColor(labelColor);
            @ColorInt int labelBackgroundColor = NOT_SET;
            labelBackgroundColor = attr.getColor(R.styleable.FabWithLabelView_fabLabelBackgroundColor,
                    labelBackgroundColor);
            builder.setLabelBackgroundColor(labelBackgroundColor);
            boolean labelClickable = attr.getBoolean(R.styleable.FabWithLabelView_fabLabelClickable, true);
            builder.setLabelClickable(labelClickable);
            setSpeedDialActionItem(builder.create());
        } catch (Exception e) {
            Log.e(TAG, "Failure setting FabWithLabelView icon", e);
        } finally {
            attr.recycle();
        }
    }
    private void setFabSize(@FloatingActionButton.Size int fabSize) {
        int normalFabSizePx = getContext().getResources().getDimensionPixelSize(R.dimen.sd_fab_normal_size);
        int miniFabSizePx = getContext().getResources().getDimensionPixelSize(R.dimen.sd_fab_mini_size);
        int fabSideMarginPx = getContext().getResources().getDimensionPixelSize(R.dimen.sd_fab_side_margin);
        int fabSizePx = fabSize == SIZE_NORMAL ? normalFabSizePx : miniFabSizePx;
        LayoutParams rootLayoutParams;
        LayoutParams fabLayoutParams = (LayoutParams) mFab.getLayoutParams();
        if (getOrientation() == HORIZONTAL) {
            rootLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, fabSizePx);
            rootLayoutParams.gravity = Gravity.END;
            if (fabSize == SIZE_NORMAL) {
                int excessMargin = (normalFabSizePx - miniFabSizePx) / 2;
                fabLayoutParams.setMargins(fabSideMarginPx - excessMargin, 0, fabSideMarginPx - excessMargin, 0);
            } else {
                fabLayoutParams.setMargins(fabSideMarginPx, 0, fabSideMarginPx, 0);
            }
        } else {
            rootLayoutParams = new LayoutParams(fabSizePx, ViewGroup.LayoutParams.WRAP_CONTENT);
            rootLayoutParams.gravity = Gravity.CENTER_VERTICAL;
            fabLayoutParams.setMargins(0, 0, 0, 0);
        }
        setLayoutParams(rootLayoutParams);
        mFab.setLayoutParams(fabLayoutParams);
        mCurrentFabSize = fabSize;
    }
    private void setFabIcon( Drawable mDrawable) {
        mFab.setImageDrawable(mDrawable);
    }
    private void setLabel( CharSequence sequence) {
        if (!TextUtils.isEmpty(sequence)) {
            mLabelTextView.setText(sequence);
            setLabelEnable(getOrientation() == HORIZONTAL);
        } else {
            setLabelEnable(false);
        }
    }
    private void setLabelClickable(boolean clickable) {
        getLabelBackground().setClickable(clickable);
        getLabelBackground().setFocusable(clickable);
        getLabelBackground().setEnabled(clickable);
    }
    private void setFabBackgroundColor(int color) {
        mFab.setBackgroundTintList(ColorStateList.valueOf(color));
    }
    private void setLabelColor(int color) {
        mLabelTextView.setTextColor(color);
    }
    private void setLabelBackgroundColor(int color) {
        mLabelCardView.setCardBackgroundColor(ColorStateList.valueOf(color));
    }
}
