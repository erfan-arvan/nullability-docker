package butterknife;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.TextView;
import butterknife.internal.Constants;
import butterknife.internal.Utils;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;
import static java.util.Collections.singletonList;

public final class ButterKnife {

    private ButterKnife() {
        throw new AssertionError();
    }

    private static final String TAG = "ButterKnife";

    private static boolean debug = false;

    /**
     * Control whether debug logging is enabled.
     */
    public static void setDebug(boolean debug) {
        ButterKnife.debug = debug;
    }

    /**
     * BindView annotated fields and methods in the specified {@link Activity}. The current content
     * view is used as the view root.
     *
     * @param target Target activity for view binding.
     */
    @NonNull
    @UiThread
    public static Unbinder bind(@NonNull Activity target) {
        View sourceView = target.getWindow().getDecorView();
        return bind(target, sourceView);
    }

    /**
     * BindView annotated fields and methods in the specified {@link View}. The view and its children
     * are used as the view root.
     *
     * @param target Target view for view binding.
     */
    @NonNull
    @UiThread
    public static Unbinder bind(@NonNull View target) {
        return bind(target, target);
    }

    /**
     * BindView annotated fields and methods in the specified {@link Dialog}. The current content
     * view is used as the view root.
     *
     * @param target Target dialog for view binding.
     */
    @NonNull
    @UiThread
    public static Unbinder bind(@NonNull Dialog target) {
        View sourceView = target.getWindow().getDecorView();
        return bind(target, sourceView);
    }

    /**
     * BindView annotated fields and methods in the specified {@code target} using the {@code source}
     * {@link Activity} as the view root.
     *
     * @param target Target class for view binding.
     * @param source Activity on which IDs will be looked up.
     */
    @NonNull
    @UiThread
    public static Unbinder bind(@NonNull Object target, @NonNull Activity source) {
        View sourceView = source.getWindow().getDecorView();
        return bind(target, sourceView);
    }

    /**
     * BindView annotated fields and methods in the specified {@code target} using the {@code source}
     * {@link Dialog} as the view root.
     *
     * @param target Target class for view binding.
     * @param source Dialog on which IDs will be looked up.
     */
    @NonNull
    @UiThread
    public static Unbinder bind(@NonNull Object target, @NonNull Dialog source) {
        View sourceView = source.getWindow().getDecorView();
        return bind(target, sourceView);
    }

    /**
     * BindView annotated fields and methods in the specified {@code target} using the {@code source}
     * {@link View} as the view root.
     *
     * @param target Target class for view binding.
     * @param source View root on which IDs will be looked up.
     */
    @NonNull
    @UiThread
    public static Unbinder bind(@NonNull Object target, @NonNull View source) {
        List<Unbinder> unbinders = new ArrayList<>();
        Class<?> targetClass = target.getClass();
        if ((targetClass.getModifiers() & PRIVATE) != 0) {
            throw new IllegalArgumentException(targetClass.getName() + " must not be private.");
        }
        while (true) {
            String clsName = targetClass.getName();
            if (clsName.startsWith("android.") || clsName.startsWith("java.")) {
                break;
            }
            for (Field field : targetClass.getDeclaredFields()) {
                int unbinderStartingSize = unbinders.size();
                Unbinder unbinder;
                unbinder = parseBindView(target, field, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
                unbinder = parseBindViews(target, field, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
                unbinder = parseBindAnim(target, field, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
                unbinder = parseBindArray(target, field, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
                unbinder = parseBindBitmap(target, field, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
                unbinder = parseBindBool(target, field, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
                unbinder = parseBindColor(target, field, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
                unbinder = parseBindDimen(target, field, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
                unbinder = parseBindDrawable(target, field, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
                unbinder = parseBindFloat(target, field, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
                unbinder = parseBindFont(target, field, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
                unbinder = parseBindInt(target, field, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
                unbinder = parseBindString(target, field, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
                if (unbinders.size() - unbinderStartingSize > 1) {
                    throw new IllegalStateException("More than one bind annotation on " + targetClass.getName() + "." + field.getName());
                }
            }
            for (Method method : targetClass.getDeclaredMethods()) {
                Unbinder unbinder;
                unbinder = parseOnCheckedChanged(target, method, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
                unbinder = parseOnClick(target, method, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
                unbinder = parseOnEditorAction(target, method, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
                unbinder = parseOnFocusChange(target, method, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
                unbinder = parseOnItemClick(target, method, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
                unbinder = parseOnItemLongClick(target, method, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
                unbinder = parseOnLongClick(target, method, source);
                if (unbinder != null)
                    unbinders.add(unbinder);
            }
            targetClass = targetClass.getSuperclass();
        }
        if (unbinders.isEmpty()) {
            if (debug)
                Log.d(TAG, "MISS: Reached framework class. Abandoning search.");
            return Unbinder.EMPTY;
        }
        if (debug)
            Log.d(TAG, "HIT: Reflectively found " + unbinders.size() + " bindings.");
        return new CompositeUnbinder(unbinders);
    }

    private static Unbinder parseBindView(Object target, Field field, View source) {
        BindView bindView = field.getAnnotation(BindView.class);
        if (bindView == null) {
            return null;
        }
        validateMember(field);
        int id = bindView.value();
        boolean isRequired = isRequired(field);
        Class<?> viewClass = field.getType();
        String who = "field '" + field.getName() + "'";
        Object view;
        if (isRequired) {
            view = Utils.findRequiredViewAsType(source, id, who, viewClass);
        } else {
            view = Utils.findOptionalViewAsType(source, id, who, viewClass);
        }
        trySet(field, target, view);
        return new FieldUnbinder(target, field);
    }

    private static Unbinder parseBindViews(Object target, Field field, View source) {
        BindViews bindViews = field.getAnnotation(BindViews.class);
        if (bindViews == null) {
            return null;
        }
        validateMember(field);
        Class<?> fieldClass = field.getType();
        Class<?> viewClass;
        boolean isArray = fieldClass.isArray();
        if (isArray) {
            viewClass = fieldClass.getComponentType();
        } else if (fieldClass == List.class) {
            Type fieldType = field.getGenericType();
            if (fieldType instanceof ParameterizedType) {
                Type viewType = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
                // TODO real rawType impl!!!!
                viewClass = (Class<?>) viewType;
            } else {
                // TODO
                throw new IllegalStateException();
            }
        } else {
            // TODO
            throw new IllegalStateException();
        }
        int[] ids = bindViews.value();
        boolean isRequired = isRequired(field);
        List<Object> views = new ArrayList<>(ids.length);
        String who = "field '" + field.getName() + "'";
        for (int id : ids) {
            Object view;
            if (isRequired) {
                view = Utils.findRequiredViewAsType(source, id, who, viewClass);
            } else {
                view = Utils.findOptionalViewAsType(source, id, who, viewClass);
            }
            if (view != null) {
                views.add(view);
            }
        }
        Object value;
        if (isArray) {
            Object[] viewArray = (Object[]) Array.newInstance(viewClass, views.size());
            value = views.toArray(viewArray);
        } else {
            value = views;
        }
        trySet(field, target, value);
        return new FieldUnbinder(target, field);
    }

    @Nullable
    private static Unbinder parseBindAnim(Object target, Field field, View source) {
        BindAnim bindAnim = field.getAnnotation(BindAnim.class);
        if (bindAnim == null) {
            return null;
        }
        validateMember(field);
        int id = bindAnim.value();
        Resources resources = source.getContext().getResources();
        Object value;
        Class<?> fieldType = field.getType();
        if (fieldType == Animation.class) {
            value = resources.getAnimation(id);
        } else {
            // TODO
            throw new IllegalStateException();
        }
        trySet(field, target, value);
        return Unbinder.EMPTY;
    }

    @Nullable
    private static Unbinder parseBindArray(Object target, Field field, View source) {
        BindArray bindArray = field.getAnnotation(BindArray.class);
        if (bindArray == null) {
            return null;
        }
        validateMember(field);
        int id = bindArray.value();
        Resources resources = source.getContext().getResources();
        Object value;
        Class<?> fieldType = field.getType();
        if (fieldType == TypedArray.class) {
            value = resources.obtainTypedArray(id);
        } else if (fieldType.isArray()) {
            Class<?> componentType = fieldType.getComponentType();
            if (componentType == String.class) {
                value = resources.getStringArray(id);
            } else if (componentType == int.class) {
                value = resources.getIntArray(id);
            } else if (componentType == CharSequence.class) {
                value = resources.getTextArray(id);
            } else {
                // TODO
                throw new IllegalStateException();
            }
        } else {
            // TODO
            throw new IllegalStateException();
        }
        trySet(field, target, value);
        return Unbinder.EMPTY;
    }

    @Nullable
    private static Unbinder parseBindBitmap(Object target, Field field, View source) {
        BindBitmap bindBitmap = field.getAnnotation(BindBitmap.class);
        if (bindBitmap == null) {
            return null;
        }
        validateMember(field);
        int id = bindBitmap.value();
        Resources resources = source.getContext().getResources();
        Object value;
        Class<?> fieldType = field.getType();
        if (fieldType == Bitmap.class) {
            value = BitmapFactory.decodeResource(resources, id);
        } else {
            // TODO
            throw new IllegalStateException();
        }
        trySet(field, target, value);
        return Unbinder.EMPTY;
    }

    @Nullable
    private static Unbinder parseBindBool(Object target, Field field, View source) {
        BindBool bindBool = field.getAnnotation(BindBool.class);
        if (bindBool == null) {
            return null;
        }
        validateMember(field);
        int id = bindBool.value();
        Resources resources = source.getContext().getResources();
        Object value;
        Class<?> fieldType = field.getType();
        if (fieldType == boolean.class) {
            value = resources.getBoolean(id);
        } else {
            // TODO
            throw new IllegalStateException();
        }
        trySet(field, target, value);
        return Unbinder.EMPTY;
    }

    @Nullable
    private static Unbinder parseBindColor(Object target, Field field, View source) {
        BindColor bindColor = field.getAnnotation(BindColor.class);
        if (bindColor == null) {
            return null;
        }
        validateMember(field);
        int id = bindColor.value();
        Context context = source.getContext();
        Object value;
        Class<?> fieldType = field.getType();
        if (fieldType == int.class) {
            value = ContextCompat.getColor(context, id);
        } else if (fieldType == ColorStateList.class) {
            value = ContextCompat.getColorStateList(context, id);
        } else {
            // TODO
            throw new IllegalStateException();
        }
        trySet(field, target, value);
        return Unbinder.EMPTY;
    }

    @Nullable
    private static Unbinder parseBindDimen(Object target, Field field, View source) {
        BindDimen bindDimen = field.getAnnotation(BindDimen.class);
        if (bindDimen == null) {
            return null;
        }
        validateMember(field);
        int id = bindDimen.value();
        Resources resources = source.getContext().getResources();
        Class<?> fieldType = field.getType();
        Object value;
        if (fieldType == int.class) {
            value = resources.getDimensionPixelSize(id);
        } else if (fieldType == float.class) {
            value = resources.getDimension(id);
        } else {
            // TODO
            throw new IllegalStateException();
        }
        trySet(field, target, value);
        return Unbinder.EMPTY;
    }

    @Nullable
    private static Unbinder parseBindDrawable(Object target, Field field, View source) {
        BindDrawable bindDrawable = field.getAnnotation(BindDrawable.class);
        if (bindDrawable == null) {
            return null;
        }
        validateMember(field);
        int id = bindDrawable.value();
        int tint = bindDrawable.tint();
        Context context = source.getContext();
        Class<?> fieldType = field.getType();
        Object value;
        if (fieldType == Drawable.class) {
            value = tint != Constants.NO_RES_ID ? Utils.getTintedDrawable(context, id, tint) : ContextCompat.getDrawable(context, id);
        } else {
            // TODO
            throw new IllegalStateException();
        }
        trySet(field, target, value);
        return Unbinder.EMPTY;
    }

    @Nullable
    private static Unbinder parseBindFloat(Object target, Field field, View source) {
        BindFloat bindInt = field.getAnnotation(BindFloat.class);
        if (bindInt == null) {
            return null;
        }
        validateMember(field);
        int id = bindInt.value();
        Context context = source.getContext();
        Class<?> fieldType = field.getType();
        Object value;
        if (fieldType == float.class) {
            value = Utils.getFloat(context, id);
        } else {
            // TODO
            throw new IllegalStateException();
        }
        trySet(field, target, value);
        return Unbinder.EMPTY;
    }

    @Nullable
    private static Unbinder parseBindFont(Object target, Field field, View source) {
        BindFont bindFont = field.getAnnotation(BindFont.class);
        if (bindFont == null) {
            return null;
        }
        validateMember(field);
        int id = bindFont.value();
        int style = bindFont.style();
        Context context = source.getContext();
        Class<?> fieldType = field.getType();
        Object value;
        if (fieldType == Typeface.class) {
            Typeface font = ResourcesCompat.getFont(context, id);
            value = style != Typeface.NORMAL ? Typeface.create(font, style) : font;
        } else {
            // TODO
            throw new IllegalStateException();
        }
        trySet(field, target, value);
        return Unbinder.EMPTY;
    }

    @Nullable
    private static Unbinder parseBindInt(Object target, Field field, View source) {
        BindInt bindInt = field.getAnnotation(BindInt.class);
        if (bindInt == null) {
            return null;
        }
        validateMember(field);
        int id = bindInt.value();
        Resources resources = source.getContext().getResources();
        Class<?> fieldType = field.getType();
        Object value;
        if (fieldType == int.class) {
            value = resources.getInteger(id);
        } else {
            // TODO
            throw new IllegalStateException();
        }
        trySet(field, target, value);
        return Unbinder.EMPTY;
    }

    @Nullable
    private static Unbinder parseBindString(Object target, Field field, View source) {
        BindString bindString = field.getAnnotation(BindString.class);
        if (bindString == null) {
            return null;
        }
        validateMember(field);
        int id = bindString.value();
        Context context = source.getContext();
        Class<?> fieldType = field.getType();
        Object value;
        if (fieldType == String.class) {
            value = context.getString(id);
        } else {
            // TODO
            throw new IllegalStateException();
        }
        trySet(field, target, value);
        return Unbinder.EMPTY;
    }

    private static Unbinder parseOnCheckedChanged(final Object target, final Method method, View source) {
        OnCheckedChanged onCheckedChanged = method.getAnnotation(OnCheckedChanged.class);
        if (onCheckedChanged == null) {
            return null;
        }
        validateMember(method);
        validateReturnType(method, void.class);
        final ArgumentTransformer argumentTransformer = createArgumentTransformer(method, ON_CHECKED_CHANGED_TYPES);
        List<CompoundButton> views = findViews(source, onCheckedChanged.value(), isRequired(method), method.getName(), CompoundButton.class);
        ViewCollections.set(views, ON_CHECKED_CHANGE, new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tryInvoke(method, target, argumentTransformer.transform(buttonView, isChecked));
            }
        });
        return new ListenerUnbinder<>(views, ON_CHECKED_CHANGE);
    }

    private static Unbinder parseOnClick(final Object target, final Method method, View source) {
        OnClick onClick = method.getAnnotation(OnClick.class);
        if (onClick == null) {
            return null;
        }
        validateMember(method);
        validateReturnType(method, void.class);
        final ArgumentTransformer argumentTransformer = createArgumentTransformer(method, ON_CLICK_TYPES);
        List<View> views = findViews(source, onClick.value(), isRequired(method), method.getName(), View.class);
        ViewCollections.set(views, ON_CLICK, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tryInvoke(method, target, argumentTransformer.transform(v));
            }
        });
        return new ListenerUnbinder<>(views, ON_CLICK);
    }

    private static Unbinder parseOnEditorAction(final Object target, final Method method, View source) {
        OnEditorAction onEditorAction = method.getAnnotation(OnEditorAction.class);
        if (onEditorAction == null) {
            return null;
        }
        validateMember(method);
        final boolean propagateReturn = validateReturnType(method, boolean.class);
        final ArgumentTransformer argumentTransformer = createArgumentTransformer(method, ON_EDITOR_ACTION_TYPES);
        List<TextView> views = findViews(source, onEditorAction.value(), isRequired(method), method.getName(), TextView.class);
        ViewCollections.set(views, ON_EDITOR_ACTION, new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Object value = tryInvoke(method, target, argumentTransformer.transform(v, actionId, event));
                //noinspection SimplifiableConditionalExpression
                return propagateReturn ? (boolean) value : false;
            }
        });
        return new ListenerUnbinder<>(views, ON_EDITOR_ACTION);
    }

    private static Unbinder parseOnFocusChange(final Object target, final Method method, View source) {
        OnFocusChange onFocusChange = method.getAnnotation(OnFocusChange.class);
        if (onFocusChange == null) {
            return null;
        }
        validateMember(method);
        validateReturnType(method, void.class);
        final ArgumentTransformer argumentTransformer = createArgumentTransformer(method, ON_FOCUS_CHANGE_TYPES);
        List<View> views = findViews(source, onFocusChange.value(), isRequired(method), method.getName(), View.class);
        ViewCollections.set(views, ON_FOCUS_CHANGE, new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                tryInvoke(method, target, argumentTransformer.transform(v, hasFocus));
            }
        });
        return new ListenerUnbinder<>(views, ON_FOCUS_CHANGE);
    }

    private static Unbinder parseOnItemClick(final Object target, final Method method, View source) {
        OnItemClick onItemClick = method.getAnnotation(OnItemClick.class);
        if (onItemClick == null) {
            return null;
        }
        validateMember(method);
        validateReturnType(method, void.class);
        final ArgumentTransformer argumentTransformer = createArgumentTransformer(method, ON_ITEM_CLICK_TYPES);
        List<AdapterView<?>> views = findViews(source, onItemClick.value(), isRequired(method), method.getName(), AdapterView.class);
        ViewCollections.set(views, ON_ITEM_CLICK, new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tryInvoke(method, target, argumentTransformer.transform(parent, view, position, id));
            }
        });
        return new ListenerUnbinder<>(views, ON_ITEM_CLICK);
    }

    private static Unbinder parseOnItemLongClick(final Object target, final Method method, View source) {
        OnItemLongClick onItemLongClick = method.getAnnotation(OnItemLongClick.class);
        if (onItemLongClick == null) {
            return null;
        }
        validateMember(method);
        final boolean propagateReturn = validateReturnType(method, boolean.class);
        final ArgumentTransformer argumentTransformer = createArgumentTransformer(method, ON_ITEM_LONG_CLICK_TYPES);
        List<AdapterView<?>> views = findViews(source, onItemLongClick.value(), isRequired(method), method.getName(), AdapterView.class);
        ViewCollections.set(views, ON_ITEM_LONG_CLICK, new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Object value = tryInvoke(method, target, argumentTransformer.transform(parent, view, position, id));
                //noinspection SimplifiableConditionalExpression
                return propagateReturn ? (boolean) value : false;
            }
        });
        return new ListenerUnbinder<>(views, ON_ITEM_LONG_CLICK);
    }

    private static Unbinder parseOnLongClick(final Object target, final Method method, View source) {
        OnLongClick onLongClick = method.getAnnotation(OnLongClick.class);
        if (onLongClick == null) {
            return null;
        }
        validateMember(method);
        final boolean propagateReturn = validateReturnType(method, boolean.class);
        final ArgumentTransformer argumentTransformer = createArgumentTransformer(method, ON_LONG_CLICK_TYPES);
        List<View> views = findViews(source, onLongClick.value(), isRequired(method), method.getName(), View.class);
        ViewCollections.set(views, ON_LONG_CLICK, new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Object returnValue = tryInvoke(method, target, argumentTransformer.transform(v));
                //noinspection SimplifiableConditionalExpression
                return propagateReturn ? (boolean) returnValue : false;
            }
        });
        return new ListenerUnbinder<>(views, ON_LONG_CLICK);
    }

    @SuppressWarnings("unchecked")
    private static <T extends View> List<T> findViews(View source, int[] ids, boolean isRequired, String name, Class<? extends View> cls) {
        if (ids.length == 1 && ids[0] == View.NO_ID) {
            return singletonList((T) cls.cast(source));
        }
        String who = "method '" + name + "'";
        List<T> views = new ArrayList<>(ids.length);
        for (int id : ids) {
            if (isRequired) {
                views.add((T) Utils.findRequiredViewAsType(source, id, who, cls));
            } else {
                T view = (T) Utils.findOptionalViewAsType(source, id, who, cls);
                if (view != null) {
                    views.add(view);
                }
            }
        }
        return views;
    }

    private static <T extends AccessibleObject & Member> void validateMember(T object) {
        int modifiers = object.getModifiers();
        if ((modifiers & (PRIVATE | STATIC)) != 0) {
            throw new IllegalStateException(object.getDeclaringClass().getName() + "." + object.getName() + " must not be private or static");
        }
        if ((modifiers & PUBLIC) == 0) {
            object.setAccessible(true);
        }
    }

    /**
     * Returns true when the return value should be propagated. Use a default otherwise.
     */
    private static boolean validateReturnType(Method method, Class<?> expected) {
        Class<?> returnType = method.getReturnType();
        if (returnType == void.class) {
            return false;
        }
        if (returnType != expected) {
            String expectedType = "'" + expected.getName() + "'";
            if (expected != void.class) {
                expectedType = "'void' or " + expectedType;
            }
            throw new IllegalStateException(method.getDeclaringClass().getName() + "." + method.getName() + " must have return type of " + expectedType);
        }
        return true;
    }

    private static boolean isRequired(Field field) {
        for (Annotation annotation : field.getAnnotations()) {
            if (annotation.annotationType().getSimpleName().equals("Nullable")) {
                return false;
            }
        }
        return true;
    }

    private static boolean isRequired(Method method) {
        return method.getAnnotation(Optional.class) == null;
    }

    private static ArgumentTransformer createArgumentTransformer(Method method, Class<?>[] callbackParameterTypes) {
        Class<?>[] targetParameterTypes = method.getParameterTypes();
        int targetParameterLength = targetParameterTypes.length;
        if (targetParameterLength == 0) {
            // Special case the common case of no arguments.
            return ArgumentTransformer.EMPTY;
        }
        int callbackParameterLength = callbackParameterTypes.length;
        if (targetParameterLength > callbackParameterLength) {
            throw new IllegalStateException(method.getDeclaringClass().getName() + "." + method.getName() + " must have at most " + callbackParameterLength + " parameter(s).");
        }
        if (Arrays.equals(targetParameterTypes, callbackParameterTypes)) {
            // Special case the common case of exact argument match.
            return ArgumentTransformer.IDENTITY;
        }
        boolean[] callbackIndexUsed = new boolean[callbackParameterLength];
        final int[] indexMap = new int[targetParameterLength];
        nextTarget: for (int targetIndex = 0; targetIndex < targetParameterLength; targetIndex++) {
            Class<?> targetParameterType = targetParameterTypes[targetIndex];
            for (int callbackIndex = 0; callbackIndex < callbackParameterLength; callbackIndex++) {
                if (callbackIndexUsed[callbackIndex]) {
                    // We have already used this callback argument.
                    continue;
                }
                Class<?> callbackParameterType = callbackParameterTypes[callbackIndex];
                if (/* exact match */
                callbackParameterType.equals(targetParameterType) || /* or subtype of view */
                (View.class.isAssignableFrom(callbackParameterType) && callbackParameterType.isAssignableFrom(targetParameterType)) || /* or interface (like Checkable) */
                targetParameterType.isInterface()) {
                    indexMap[targetIndex] = callbackIndex;
                    callbackIndexUsed[callbackIndex] = true;
                    // This avoids the error handling code if loop exits normally.
                    continue nextTarget;
                }
            }
            StringBuilder builder = new StringBuilder();
            builder.append("Unable to match ").append(method.getDeclaringClass().getName()).append('.').append(method.getName()).append(" method arguments.");
            for (int i = 0; i < targetParameterLength; i++) {
                builder.append("\n\n  Parameter #").append(i + 1).append(": ").append(targetParameterTypes[i].getName()).append("\n    ");
                if (i < targetIndex) {
                    builder.append("matched listener parameter #").append(indexMap[i]).append(": ").append(callbackParameterTypes[indexMap[i]].getName());
                } else {
                    builder.append("did not match any listener parameters");
                }
            }
            builder.append("\n\nMethods may have up to ").append(callbackParameterLength).append(" parameter(s):\n");
            for (Class<?> callbackParameter : callbackParameterTypes) {
                builder.append("\n  ").append(callbackParameter.getName());
            }
            builder.append("\n\nThese may be listed in any order but will be searched for from top to bottom.");
            throw new IllegalStateException(builder.toString());
        }
        return new ArgumentTransformer() {

            @Override
            public Object[] transform(Object... arguments) {
                Object[] newArguments = new Object[indexMap.length];
                for (int i = 0; i < indexMap.length; i++) {
                    newArguments[i] = arguments[indexMap[i]];
                }
                return newArguments;
            }

            @Override
            public String toString() {
                StringBuilder builder = new StringBuilder("ArgumentTransformer[");
                for (int i = 0; i < indexMap.length; i++) {
                    if (i > 0) {
                        builder.append(", ");
                    }
                    builder.append(i).append(" => ").append(indexMap[i]);
                }
                return builder.append(']').toString();
            }
        };
    }

    static void trySet(Field field, Object target, Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to assign " + value + " to " + field + " on " + target, e);
        }
    }

    private static Object tryInvoke(Method method, Object target, Object... arguments) {
        Throwable cause;
        try {
            return method.invoke(target, arguments);
        } catch (IllegalAccessException e) {
            cause = e;
        } catch (InvocationTargetException e) {
            cause = e;
        }
        throw new RuntimeException("Unable to invoke " + method + " on " + target + " with arguments " + Arrays.toString(arguments), cause);
    }

    private static final Setter<CompoundButton, CompoundButton.OnCheckedChangeListener> ON_CHECKED_CHANGE = new Setter<CompoundButton, CompoundButton.OnCheckedChangeListener>() {

        @Override
        public void set(@NonNull CompoundButton view, CompoundButton.OnCheckedChangeListener value, @Nullable() int index) {
            view.setOnCheckedChangeListener(value);
        }
    };

    private static final Setter<View, View.OnClickListener> ON_CLICK = new Setter<View, View.OnClickListener>() {

        @Override
        public void set(@NonNull View view, View.OnClickListener value, @Nullable() int index) {
            view.setOnClickListener(value);
        }
    };

    private static final Setter<TextView, TextView.OnEditorActionListener> ON_EDITOR_ACTION = new Setter<TextView, TextView.OnEditorActionListener>() {

        @Override
        public void set(@NonNull TextView view, TextView.OnEditorActionListener value, @Nullable() int index) {
            view.setOnEditorActionListener(value);
        }
    };

    private static final Setter<View, View.OnFocusChangeListener> ON_FOCUS_CHANGE = new Setter<View, View.OnFocusChangeListener>() {

        @Override
        public void set(@NonNull View view, View.OnFocusChangeListener value, @Nullable() int index) {
            view.setOnFocusChangeListener(value);
        }
    };

    private static final Setter<AdapterView<?>, AdapterView.OnItemClickListener> ON_ITEM_CLICK = new Setter<AdapterView<?>, AdapterView.OnItemClickListener>() {

        @Override
        public void set(@NonNull AdapterView<?> view, AdapterView.OnItemClickListener value, @Nullable() int index) {
            view.setOnItemClickListener(value);
        }
    };

    private static final Setter<AdapterView<?>, AdapterView.OnItemLongClickListener> ON_ITEM_LONG_CLICK = new Setter<AdapterView<?>, AdapterView.OnItemLongClickListener>() {

        @Override
        public void set(@NonNull AdapterView<?> view, AdapterView.OnItemLongClickListener value, @Nullable() int index) {
            view.setOnItemLongClickListener(value);
        }
    };

    private static final Setter<View, View.OnLongClickListener> ON_LONG_CLICK = new Setter<View, View.OnLongClickListener>() {

        @Override
        public void set(@NonNull View view, View.OnLongClickListener value, @Nullable() int index) {
            view.setOnLongClickListener(value);
        }
    };

    private static final Class<?>[] ON_CHECKED_CHANGED_TYPES = { CompoundButton.class, boolean.class };

    private static final Class<?>[] ON_CLICK_TYPES = { View.class };

    private static final Class<?>[] ON_EDITOR_ACTION_TYPES = { TextView.class, int.class, KeyEvent.class };

    private static final Class<?>[] ON_FOCUS_CHANGE_TYPES = { View.class, boolean.class };

    private static final Class<?>[] ON_ITEM_CLICK_TYPES = { AdapterView.class, View.class, int.class, long.class };

    private static final Class<?>[] ON_ITEM_LONG_CLICK_TYPES = ON_ITEM_CLICK_TYPES;

    private static final Class<?>[] ON_LONG_CLICK_TYPES = ON_CLICK_TYPES;

    private interface ArgumentTransformer {

        ArgumentTransformer EMPTY = new ArgumentTransformer() {

            private final Object[] empty = new Object[0];

            @Override
            public Object[] transform(@Nullable() Object... arguments) {
                return empty;
            }

            @Override
            public String toString() {
                return "ArgumentTransformer[empty]";
            }
        };

        ArgumentTransformer IDENTITY = new ArgumentTransformer() {

            @Override
            public Object[] transform(Object... arguments) {
                return arguments;
            }

            @Override
            public String toString() {
                return "ArgumentTransformer[identity]";
            }
        };

        Object[] transform(@Nullable() Object... arguments);
    }
}
