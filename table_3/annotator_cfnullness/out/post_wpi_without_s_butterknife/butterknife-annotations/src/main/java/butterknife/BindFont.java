package butterknife;

import android.graphics.Typeface;
import android.support.annotation.FontRes;
import android.support.annotation.IntDef;
import android.support.annotation.RestrictTo;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static android.support.annotation.RestrictTo.Scope.LIBRARY;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import android.support.annotation.Nullable;

/**
 * Bind a field to the specified font resource ID.
 * <pre><code>
 * {@literal @}BindFont(R.font.comic_sans) Typeface comicSans;
 * </code></pre>
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface BindFont {

    /**
     * Font resource ID to which the field will be bound.
     */
    @FontRes
    int value();

    @TypefaceStyle
    int style() default Typeface.NORMAL;

    @IntDef({ Typeface.NORMAL, Typeface.BOLD, Typeface.ITALIC, Typeface.BOLD_ITALIC })
    @RestrictTo(LIBRARY)
    @interface TypefaceStyle {
    }
}
