package com.leinardi.android.speeddial;
public class SpeedDialView extends LinearLayout implements CoordinatorLayout.AttachedBehavior {
    private static final String TAG = SpeedDialView.class.getSimpleName();
    private List<FabWithLabelView> mFabWithLabelViews = new ArrayList<>();
    private FloatingActionButton mMainFab;
    private boolean mIsOpen = false;
    private Drawable mMainFabOpenDrawable = null;
    private Drawable mMainFabCloseDrawable = null;
    private SpeedDialOverlayLayout mOverlayLayout;
    @ExpansionMode
    private int mExpansionMode = TOP;
    private boolean mRotateOnToggle = true;
    private OnChangeListener mOnChangeListener;
    private OnActionSelectedListener mOnActionSelectedListener;
    private OnActionSelectedListener mOnActionSelectedProxyListener = new OnActionSelectedListener() {
        @Override
        public boolean onActionSelected(SpeedDialActionItem actionItem) {
            if (mOnActionSelectedListener != null) {
                boolean consumed = mOnActionSelectedListener.onActionSelected(actionItem);
                if (!consumed) {
                    close();
                }
                return consumed;
            } else {
                return false;
            }
        }
    };
    public SpeedDialView(Context context) {
        super(context);
        init(context, null);
    }
    public SpeedDialView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public SpeedDialView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    @ExpansionMode
    public int getExpansionMode() {
        return mExpansionMode;
    }
    public void setExpansionMode(@ExpansionMode int expansionMode) {
        mExpansionMode = expansionMode;
        switch (expansionMode) {
            case TOP:
            case BOTTOM:
                setOrientation(VERTICAL);
                for (FabWithLabelView fabWithLabelView : mFabWithLabelViews) {
                    fabWithLabelView.setOrientation(HORIZONTAL);
                }
                break;
            case LEFT:
            case RIGHT:
                setOrientation(HORIZONTAL);
                for (FabWithLabelView fabWithLabelView : mFabWithLabelViews) {
                    fabWithLabelView.setOrientation(VERTICAL);
                }
                break;
        }
    }
    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
    }
    public void show() {
        show(null);
    }
    public void show( final OnVisibilityChangedListener listener) {
        mMainFab.show(listener);
    }
    public void hide() {
        hide(null);
    }
    public void hide( OnVisibilityChangedListener listener) {
        if (isOpen()) {
            close();
            ViewCompat.animate(mMainFab).rotation(0).setDuration(0).start();
        }
        mMainFab.hide(listener);
    }
    public SpeedDialOverlayLayout getOverlayLayout() {
        return mOverlayLayout;
    }
    public void setOverlayLayout( SpeedDialOverlayLayout overlayLayout) {
        if (overlayLayout != null) {
            overlayLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    close();
                }
            });
        } else if (mOverlayLayout != null) {
            setOnClickListener(null);
        }
        mOverlayLayout = overlayLayout;
    }
    public void addAllActionItems(Collection<SpeedDialActionItem> actionItemCollection) {
        for (SpeedDialActionItem speedDialActionItem : actionItemCollection) {
            addActionItem(speedDialActionItem);
        }
    }
    public void addActionItem(SpeedDialActionItem speedDialActionItem) {
        addActionItem(speedDialActionItem, mFabWithLabelViews.size());
    }
    public void addActionItem(SpeedDialActionItem actionItem, int position) {
        addActionItem(actionItem, position, true);
    }
    public void addActionItem(SpeedDialActionItem actionItem, int position, boolean animate) {
        FabWithLabelView oldView = findFabWithLabelViewById(actionItem.getId());
        if (oldView != null) {
            replaceActionItem(oldView.getSpeedDialActionItem(), actionItem);
        } else {
            FabWithLabelView newView = createNewFabWithLabelView(actionItem);
            int layoutPosition = getLayoutPosition(position);
            addView(newView, layoutPosition);
            mFabWithLabelViews.add(position, newView);
            if (isOpen()) {
                if (animate) {
                    showWithAnimationFabWithLabelView(newView, 0);
                }
            } else {
                newView.setVisibility(GONE);
            }
        }
    }
    public SpeedDialActionItem removeActionItem(int position) {
        SpeedDialActionItem speedDialActionItem = mFabWithLabelViews.get(position).getSpeedDialActionItem();
        removeActionItem(speedDialActionItem);
        return speedDialActionItem;
    }
    public boolean removeActionItem( SpeedDialActionItem actionItem) {
        return actionItem != null && removeActionItemById(actionItem.getId()) != null;
    }
    public SpeedDialActionItem removeActionItemById(@IdRes int idRes) {
        return removeActionItem(findFabWithLabelViewById(idRes));
    }
    public boolean replaceActionItem(SpeedDialActionItem newActionItem, int position) {
        return replaceActionItem(mFabWithLabelViews.get(position).getSpeedDialActionItem(),
                newActionItem);
    }
    public boolean replaceActionItem( SpeedDialActionItem oldSpeedDialActionItem,
                                     SpeedDialActionItem newSpeedDialActionItem) {
        if (oldSpeedDialActionItem == null) {
            return false;
        } else {
            FabWithLabelView oldView = findFabWithLabelViewById(oldSpeedDialActionItem.getId());
            if (oldView != null) {
                int index = mFabWithLabelViews.indexOf(oldView);
                if (index < 0) {
                    return false;
                }
                removeActionItem(findFabWithLabelViewById(newSpeedDialActionItem.getId()), null, false);
                removeActionItem(findFabWithLabelViewById(oldSpeedDialActionItem.getId()), null, false);
                addActionItem(newSpeedDialActionItem, index, false);
                return true;
            } else {
                return false;
            }
        }
    }
    public void clearActionItems() {
        Iterator<FabWithLabelView> it = mFabWithLabelViews.iterator();
        while (it.hasNext()) {
            FabWithLabelView fabWithLabelView = it.next();
            removeActionItem(fabWithLabelView, it, true);
        }
    }
    @Override
    public CoordinatorLayout.Behavior getBehavior() {
        return new SnackbarBehavior();
    }
    public void setOnActionSelectedListener( OnActionSelectedListener listener) {
        mOnActionSelectedListener = listener;
        for (int index = 0; index < mFabWithLabelViews.size(); index++) {
            final FabWithLabelView fabWithLabelView = mFabWithLabelViews.get(index);
            fabWithLabelView.setOnActionSelectedListener(mOnActionSelectedProxyListener);
        }
    }
    public void setOnChangeListener( final OnChangeListener onChangeListener) {
        mOnChangeListener = onChangeListener;
    }
    public void open() {
        toggle(true);
    }
    public void close() {
        toggle(false);
    }
    public void toggle() {
        toggle(!mIsOpen);
    }
    public boolean isOpen() {
        return mIsOpen;
    }
    public boolean isRotateOnToggle() {
        return mRotateOnToggle;
    }
    public void setRotateOnToggle(boolean rotateOnToggle) {
        mRotateOnToggle = rotateOnToggle;
    }
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        ArrayList<SpeedDialActionItem> speedDialActionItems = new ArrayList<>(mFabWithLabelViews.size());
        for (FabWithLabelView fabWithLabelView : mFabWithLabelViews) {
            speedDialActionItems.add(fabWithLabelView.getSpeedDialActionItem());
        }
        bundle.putParcelableArrayList(SpeedDialActionItem.class.getName(), speedDialActionItems);
        bundle.putBoolean("IsOpen", mIsOpen);
        return bundle;
    }
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) { 
            Bundle bundle = (Bundle) state;
            ArrayList<SpeedDialActionItem> speedDialActionItems = bundle.getParcelableArrayList(SpeedDialActionItem
                    .class.getName());
            if (speedDialActionItems != null && !speedDialActionItems.isEmpty()) {
                addAllActionItems(speedDialActionItems);
            }
            toggle(bundle.getBoolean("IsOpen", mIsOpen));
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }
    private int getLayoutPosition(int position) {
        if (mExpansionMode == TOP || mExpansionMode == LEFT) {
            return mFabWithLabelViews.size() - position;
        } else {
            return position + 1;
        }
    }
    private SpeedDialActionItem removeActionItem( FabWithLabelView view,
                                                  Iterator<FabWithLabelView> it,
                                                 boolean animate) {
        if (view != null) {
            SpeedDialActionItem speedDialActionItem = view.getSpeedDialActionItem();
            if (it != null) {
                it.remove();
            } else {
                mFabWithLabelViews.remove(view);
            }
            if (isOpen()) {
                if (mFabWithLabelViews.isEmpty()) {
                    close();
                }
                if (animate) {
                    UiUtils.shrinkAnim(view, true);
                } else {
                    removeView(view);
                }
            } else {
                removeView(view);
            }
            return speedDialActionItem;
        } else {
            return null;
        }
    }
    private SpeedDialActionItem removeActionItem( FabWithLabelView view) {
        return removeActionItem(view, null, true);
    }
    private void init(Context context,  AttributeSet attrs) {
        mMainFab = createMainFab();
        addView(mMainFab);
        setClipChildren(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(getResources().getDimension(R.dimen.sd_close_elevation));
        }
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.SpeedDialView, 0, 0);
        try {
            @DrawableRes int openDrawableRes = attr.getResourceId(R.styleable.SpeedDialView_srcCompat, NOT_SET);
            if (openDrawableRes == NOT_SET) {
                openDrawableRes = attr.getResourceId(R.styleable.SpeedDialView_srcCompat, NOT_SET);
            }
            if (openDrawableRes != NOT_SET) {
                mMainFabOpenDrawable = AppCompatResources.getDrawable(getContext(), openDrawableRes);
            }
            int closeDrawableRes = attr.getResourceId(R.styleable.SpeedDialView_sdFabCloseSrc, NOT_SET);
            if (closeDrawableRes != NOT_SET) {
                final Drawable drawable = AppCompatResources.getDrawable(context, closeDrawableRes);
                mMainFabCloseDrawable = UiUtils.getRotateDrawable(drawable, -UiUtils.ROTATION_ANGLE);
            }
            mExpansionMode = attr.getInt(R.styleable.SpeedDialView_sdExpansionMode, mExpansionMode);
            mRotateOnToggle = attr.getBoolean(R.styleable.SpeedDialView_sdFabRotateOnToggle, mRotateOnToggle);
        } catch (Exception e) {
            Log.e(TAG, "Failure setting FabWithLabelView icon", e);
        } finally {
            attr.recycle();
        }
        mMainFab.setImageDrawable(mMainFabOpenDrawable);
        setExpansionMode(mExpansionMode);
    }
    private FloatingActionButton createMainFab() {
        FloatingActionButton floatingActionButton = new FloatingActionButton(getContext());
        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.END;
        int margin = UiUtils.dpToPx(getContext(), 4);
        layoutParams.setMargins(0, 0, margin, 0);
        floatingActionButton.setUseCompatPadding(true);
        floatingActionButton.setLayoutParams(layoutParams);
        floatingActionButton.setClickable(true);
        floatingActionButton.setFocusable(true);
        floatingActionButton.setSize(FloatingActionButton.SIZE_NORMAL);
        floatingActionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (!mIsOpen && !mFabWithLabelViews.isEmpty()) {
                    open();
                } else {
                    if (mOnChangeListener == null) {
                        close();
                    } else {
                        mOnChangeListener.onMainActionSelected();
                    }
                }
            }
        });
        return floatingActionButton;
    }
    private FabWithLabelView createNewFabWithLabelView(SpeedDialActionItem speedDialActionItem) {
        FabWithLabelView newView;
        int theme = speedDialActionItem.getTheme();
        if (theme == NOT_SET) {
            newView = new FabWithLabelView(getContext());
        } else {
            newView = new FabWithLabelView(new ContextThemeWrapper(getContext(), theme), null, theme);
        }
        newView.setSpeedDialActionItem(speedDialActionItem);
        newView.setOrientation(getOrientation() == VERTICAL ? HORIZONTAL : VERTICAL);
        newView.setOnActionSelectedListener(mOnActionSelectedProxyListener);
        return newView;
    }
    private void toggle(boolean show) {
        if (mIsOpen == show) {
            return;
        }
        mIsOpen = show;
        visibilitySetup(show);
        if (show) {
            if (mMainFabCloseDrawable != null) {
                mMainFab.setImageDrawable(mMainFabCloseDrawable);
            }
            UiUtils.rotateForward(mMainFab, mRotateOnToggle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setElevation(getResources().getDimension(R.dimen.sd_open_elevation));
            } else {
                bringToFront();
            }
        } else {
            UiUtils.rotateBackward(mMainFab, mRotateOnToggle);
            if (mMainFabCloseDrawable != null) {
                mMainFab.setImageDrawable(mMainFabOpenDrawable);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setElevation(getResources().getDimension(R.dimen.sd_close_elevation));
            }
        }
        showHideOverlay(show);
        if (mOnChangeListener != null) {
            mOnChangeListener.onToggleChanged(show);
        }
    }
    private void showHideOverlay(boolean show) {
        if (mOverlayLayout != null) {
            if (show) {
                mOverlayLayout.show();
            } else {
                mOverlayLayout.hide();
            }
        }
    }
    private FabWithLabelView findFabWithLabelViewById(@IdRes int id) {
        for (FabWithLabelView fabWithLabelView : mFabWithLabelViews) {
            if (fabWithLabelView.getId() == id) {
                return fabWithLabelView;
            }
        }
        return null;
    }
    private void enlargeAnim(View view, long startOffset) {
        ViewCompat.animate(view).cancel();
        view.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.sd_scale_fade_and_translate_in);
        anim.setStartOffset(startOffset);
        view.startAnimation(anim);
    }
    private void visibilitySetup(boolean visible) {
        int size = mFabWithLabelViews.size();
        if (visible) {
            for (int i = 0; i < size; i++) {
                FabWithLabelView fabWithLabelView = mFabWithLabelViews.get(i);
                showWithAnimationFabWithLabelView(fabWithLabelView, i * 50);
            }
        } else {
            for (int i = 0; i < size; i++) {
                UiUtils.shrinkAnim(mFabWithLabelViews.get(i), false);
            }
        }
    }
    private void showWithAnimationFabWithLabelView(FabWithLabelView fabWithLabelView, int delay) {
        ViewCompat.animate(fabWithLabelView).cancel();
        fabWithLabelView.setAlpha(1);
        fabWithLabelView.setVisibility(View.VISIBLE);
        enlargeAnim(fabWithLabelView.getFab(), delay);
        if (fabWithLabelView.isLabelEnable()) {
            CardView labelBackground = fabWithLabelView.getLabelBackground();
            ViewCompat.animate(labelBackground).cancel();
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.sd_fade_and_translate_in);
            animation.setStartOffset(delay);
            labelBackground.startAnimation(animation);
        }
    }
    public interface OnChangeListener {
        void onMainActionSelected();
        void onToggleChanged(boolean isOpen);
    }
    public interface OnActionSelectedListener {
        boolean onActionSelected(SpeedDialActionItem actionItem);
    }
    @Retention(SOURCE)
    @IntDef({TOP, BOTTOM, LEFT, RIGHT})
    public @interface ExpansionMode {
        int TOP = 0;
        int BOTTOM = 1;
        int LEFT = 2;
        int RIGHT = 3;
    }
    public static class SnackbarBehavior extends CoordinatorLayout.Behavior<View> {
        private static final boolean AUTO_HIDE_DEFAULT = true;
        private Rect mTmpRect;
        private OnVisibilityChangedListener mInternalAutoHideListener;
        private boolean mAutoHideEnabled;
        public SnackbarBehavior() {
            super();
            mAutoHideEnabled = AUTO_HIDE_DEFAULT;
        }
        public SnackbarBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs,
                    android.support.design.R.styleable.FloatingActionButton_Behavior_Layout);
            mAutoHideEnabled = a.getBoolean(
                    android.support.design.R.styleable.FloatingActionButton_Behavior_Layout_behavior_autoHide,
                    AUTO_HIDE_DEFAULT);
            a.recycle();
        }
        private static boolean isBottomSheet( View view) {
            final ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp instanceof CoordinatorLayout.LayoutParams) {
                return ((CoordinatorLayout.LayoutParams) lp)
                        .getBehavior() instanceof BottomSheetBehavior;
            }
            return false;
        }
        public boolean isAutoHideEnabled() {
            return mAutoHideEnabled;
        }
        public void setAutoHideEnabled(boolean autoHide) {
            mAutoHideEnabled = autoHide;
        }
        @Override
        public void onAttachedToLayoutParams( CoordinatorLayout.LayoutParams lp) {
            if (lp.dodgeInsetEdges == Gravity.NO_GRAVITY) {
                lp.dodgeInsetEdges = Gravity.BOTTOM;
            }
        }
        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, View child,
                                              View dependency) {
            if (dependency instanceof AppBarLayout) {
                updateFabVisibilityForAppBarLayout(parent, (AppBarLayout) dependency, child);
            } else if (isBottomSheet(dependency)) {
                updateFabVisibilityForBottomSheet(dependency, child);
            }
            return false;
        }
        @Override
        public boolean onLayoutChild(CoordinatorLayout parent, View child,
                                     int layoutDirection) {
            final List<View> dependencies = parent.getDependencies(child);
            for (int i = 0, count = dependencies.size(); i < count; i++) {
                final View dependency = dependencies.get(i);
                if (dependency instanceof AppBarLayout) {
                    if (updateFabVisibilityForAppBarLayout(
                            parent, (AppBarLayout) dependency, child)) {
                        break;
                    }
                } else if (isBottomSheet(dependency)) {
                    if (updateFabVisibilityForBottomSheet(dependency, child)) {
                        break;
                    }
                }
            }
            parent.onLayoutChild(child, layoutDirection);
            return true;
        }
        @VisibleForTesting
        void setInternalAutoHideListener(OnVisibilityChangedListener listener) {
            mInternalAutoHideListener = listener;
        }
        protected void show(View child) {
            if (child instanceof FloatingActionButton) {
                ((FloatingActionButton) child).show(mInternalAutoHideListener);
            } else if (child instanceof SpeedDialView) {
                ((SpeedDialView) child).show(mInternalAutoHideListener);
            } else {
                child.setVisibility(View.VISIBLE);
            }
        }
        protected void hide(View child) {
            if (child instanceof FloatingActionButton) {
                ((FloatingActionButton) child).hide(mInternalAutoHideListener);
            } else if (child instanceof SpeedDialView) {
                ((SpeedDialView) child).hide(mInternalAutoHideListener);
            } else {
                child.setVisibility(View.INVISIBLE);
            }
        }
        private boolean shouldUpdateVisibility(View dependency, View child) {
            final CoordinatorLayout.LayoutParams lp =
                    (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            if (!mAutoHideEnabled) {
                return false;
            }
            if (lp.getAnchorId() != dependency.getId()) {
                return false;
            }
            if (child.getVisibility() != VISIBLE) {
                return false;
            }
            return true;
        }
        private boolean updateFabVisibilityForAppBarLayout(CoordinatorLayout parent,
                                                           AppBarLayout appBarLayout, View child) {
            if (!shouldUpdateVisibility(appBarLayout, child)) {
                return false;
            }
            if (mTmpRect == null) {
                mTmpRect = new Rect();
            }
            final Rect rect = mTmpRect;
            ViewGroupUtils.getDescendantRect(parent, appBarLayout, rect);
            if (rect.bottom <= getMinimumHeightForVisibleOverlappingContent(appBarLayout)) {
                child.setVisibility(View.GONE);
            } else {
                child.setVisibility(View.VISIBLE);
            }
            return true;
        }
        private int getMinimumHeightForVisibleOverlappingContent(AppBarLayout appBarLayout) {
            int minHeight = ViewCompat.getMinimumHeight(appBarLayout);
            if (minHeight != 0) {
                return minHeight * 2;
            } else {
                int childCount = appBarLayout.getChildCount();
                return childCount >= 1 ? ViewCompat.getMinimumHeight(appBarLayout.getChildAt(childCount - 1)) * 2 : 0;
            }
        }
        private boolean updateFabVisibilityForBottomSheet(View bottomSheet,
                                                          View child) {
            if (!shouldUpdateVisibility(bottomSheet, child)) {
                return false;
            }
            CoordinatorLayout.LayoutParams lp =
                    (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            if (bottomSheet.getTop() < child.getHeight() / 2 + lp.topMargin) {
                hide(child);
            } else {
                show(child);
            }
            return true;
        }
    }
    public static class ScrollingViewSnackbarBehavior extends SnackbarBehavior {
        public ScrollingViewSnackbarBehavior() {
        }
        public ScrollingViewSnackbarBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
        @Override
        public boolean onStartNestedScroll( CoordinatorLayout coordinatorLayout,  View child, 
                View directTargetChild,  View target, int axes, int type) {
            return true;
        }
        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
            return dependency instanceof RecyclerView || super.layoutDependsOn(parent, child, dependency);
        }
        @Override
        public void onNestedScroll( CoordinatorLayout coordinatorLayout,  View child,  View
                target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                    type);
            if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
                hide(child);
            } else if (dyConsumed < 0) {
                show(child);
            }
        }
    }
    public static class NoBehavior extends CoordinatorLayout.Behavior<View> {
        public NoBehavior() {
        }
        public NoBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
    }
}
