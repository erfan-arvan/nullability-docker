package butterknife.compiler;

import com.squareup.javapoet.CodeBlock;
import android.support.annotation.Nullable;

interface ResourceBinding {

    Id id();

    /**
     * True if the code for this binding requires a 'res' variable for {@code Resources} access.
     */
    boolean requiresResources(@Nullable() int sdk);

    CodeBlock render(@Nullable() int sdk);
}
