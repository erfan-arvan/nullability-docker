package butterknife.compiler;

import com.squareup.javapoet.CodeBlock;

import static butterknife.compiler.BindingSet.CONTEXT_COMPAT;
import static butterknife.compiler.BindingSet.CONTEXT_COMPAT_ANDROIDX;
import static butterknife.compiler.BindingSet.UTILS;
import static butterknife.internal.Constants.NO_RES_ID;

final class FieldDrawableBinding implements ResourceBinding {
  private final Id id;
  private final String name;
  @javax.annotation.Nullable private final Id tintAttributeId;
  private final boolean androidX;

  FieldDrawableBinding(Id id, String name, Id tintAttributeId, boolean useAndroidX) {
    this.id = id;
    this.name = name;
    this.tintAttributeId = tintAttributeId;
    this.androidX = useAndroidX;
  }

  @Override public Id id() {
    return id;
  }

  @Override public boolean requiresResources(int sdk) {
    return false;
  }

  @Override public CodeBlock render(int sdk) {
    if (tintAttributeId.value != NO_RES_ID) {
      return CodeBlock.of("target.$L = $T.getTintedDrawable(context, $L, $L)", name, UTILS, id.code,
          tintAttributeId.code);
    }
    if (sdk >= 21) {
      return CodeBlock.of("target.$L = context.getDrawable($L)", name, id.code);
    }
    return CodeBlock.of("target.$L = $T.getDrawable(context, $L)", name,
        androidX ? CONTEXT_COMPAT_ANDROIDX : CONTEXT_COMPAT, id.code);
  }
}
