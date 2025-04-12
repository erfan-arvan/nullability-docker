package butterknife.compiler;

import com.squareup.javapoet.CodeBlock;
import static butterknife.compiler.BindingSet.ANIMATION_UTILS;
import android.support.annotation.Nullable;

final class FieldAnimationBinding implements ResourceBinding {

    private final Id id;

    private final String name;

    FieldAnimationBinding(Id id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Id id() {
        return id;
    }

    @Override
    public boolean requiresResources(@Nullable() int sdk) {
        return false;
    }

    @Override
    public CodeBlock render(@Nullable() int sdk) {
        return CodeBlock.of("target.$L = $T.loadAnimation(context, $L)", name, ANIMATION_UTILS, id.code);
    }
}
