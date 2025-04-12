package com.leinardi.android.speeddial;
class ViewGroupUtils {
    private static final ThreadLocal<Matrix> MATRIX_THREAD_LOCAL = new ThreadLocal<>();
    private static final ThreadLocal<RectF> RECT_F = new ThreadLocal<>();
    private ViewGroupUtils() {
    }
    static void offsetDescendantRect(ViewGroup parent, View descendant, Rect rect) {
        Matrix m = MATRIX_THREAD_LOCAL.get();
        if (m == null) {
            m = new Matrix();
            MATRIX_THREAD_LOCAL.set(m);
        } else {
            m.reset();
        }
        offsetDescendantMatrix(parent, descendant, m);
        RectF rectF = RECT_F.get();
        if (rectF == null) {
            rectF = new RectF();
            RECT_F.set(rectF);
        }
        rectF.set(rect);
        m.mapRect(rectF);
        rect.set((int) (rectF.left + 0.5f), (int) (rectF.top + 0.5f),
                (int) (rectF.right + 0.5f), (int) (rectF.bottom + 0.5f));
    }
    static void getDescendantRect(ViewGroup parent, View descendant, Rect out) {
        out.set(0, 0, descendant.getWidth(), descendant.getHeight());
        offsetDescendantRect(parent, descendant, out);
    }
    private static void offsetDescendantMatrix(ViewParent target, View view, Matrix m) {
        final ViewParent parent = view.getParent();
        if (parent instanceof View && parent != target) {
            final View vp = (View) parent;
            offsetDescendantMatrix(target, vp, m);
            m.preTranslate(-vp.getScrollX(), -vp.getScrollY());
        }
        m.preTranslate(view.getLeft(), view.getTop());
        if (!view.getMatrix().isIdentity()) {
            m.preConcat(view.getMatrix());
        }
    }
}
