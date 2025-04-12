package com.leinardi.android.speeddial;
public class SpeedDialOverlayLayout extends RelativeLayout {
    private static final String TAG = SpeedDialOverlayLayout.class.getSimpleName();
    private boolean mClickableOverlay;
    private int mAnimationDuration;
    private OnClickListener mClickListener;
    public SpeedDialOverlayLayout( Context context) {
        super(context);
    }
    public SpeedDialOverlayLayout( Context context,  AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public SpeedDialOverlayLayout( Context context,  AttributeSet attrs, @AttrRes int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    public boolean hasClickableOverlay() {
        return mClickableOverlay;
    }
    public void setClickableOverlay(boolean clickableOverlay) {
        this.mClickableOverlay = clickableOverlay;
        setOnClickListener(mClickListener);
    }
    public void setAnimationDuration(int animationDuration) {
        this.mAnimationDuration = animationDuration;
    }
    public void show() {
        toggle(true);
    }
    public void show(boolean immediately) {
        toggle(true, immediately);
    }
    public void hide() {
        toggle(false);
    }
    public void hide(boolean immediately) {
        toggle(false, immediately);
    }
    public void toggle(boolean show) {
        toggle(show, false);
    }
    public void toggle(final boolean show, boolean immediately) {
        if (show) {
            if (immediately) {
                setVisibility(VISIBLE);
            } else {
                UiUtils.fadeInAnim(this);
            }
        } else {
            if (immediately) {
                setVisibility(GONE);
            } else {
                UiUtils.fadeOutAnim(this);
            }
        }
    }
    @Override
    public void setOnClickListener( OnClickListener clickListener) {
        mClickListener = clickListener;
        super.setOnClickListener(hasClickableOverlay() ? clickListener : null);
    }
    private void init(Context context,  AttributeSet attrs) {
        TypedArray attr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SpeedDialOverlayLayout, 0, 0);
        int overlayColor = ResourcesCompat.getColor(getResources(), R.color.sd_overlay_color, context.getTheme());
        try {
            overlayColor = attr.getColor(R.styleable.SpeedDialOverlayLayout_android_background, overlayColor);
            mClickableOverlay = attr.getBoolean(R.styleable.SpeedDialOverlayLayout_clickable_overlay, true);
        } catch (Exception e) {
            Log.e(TAG, "Failure setting FabOverlayLayout attrs", e);
        } finally {
            attr.recycle();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(getResources().getDimension(R.dimen.sd_overlay_elevation));
        }
        setBackgroundColor(overlayColor);
        setVisibility(View.GONE);
        mAnimationDuration = getResources().getInteger(android.R.integer.config_longAnimTime);
    }
}
