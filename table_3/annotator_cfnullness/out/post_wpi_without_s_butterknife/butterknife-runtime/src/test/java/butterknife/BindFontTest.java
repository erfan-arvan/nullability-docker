package butterknife;

import butterknife.compiler.ButterKnifeProcessor;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Ignore;
import org.junit.Test;
import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import android.support.annotation.Nullable;

public class BindFontTest {

    @Test
    public void simpleTypeface() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", "" + "package test;\n" + "import android.graphics.Typeface;\n" + "import butterknife.BindFont;\n" + "public class Test {\n" + "  @BindFont(1) Typeface one;\n" + "}");
        JavaFileObject bindingSource = JavaFileObjects.forSourceString("test/Test_ViewBinding", "" + "// Generated code from Butter Knife. Do not modify!\n" + "package test;\n" + "import android.content.Context;\n" + "import android.support.annotation.CallSuper;\n" + "import android.support.annotation.UiThread;\n" + "import android.support.v4.content.res.ResourcesCompat;\n" + "import android.view.View;\n" + "import butterknife.Unbinder;\n" + "import java.lang.Deprecated;\n" + "import java.lang.Override;\n" + "import java.lang.SuppressWarnings;\n" + "public class Test_ViewBinding implements Unbinder {\n" + "  /**\n" + "   * @deprecated Use {@link #Test_ViewBinding(Test, Context)} for direct creation.\n" + "   *     Only present for runtime invocation through {@code ButterKnife.bind()}.\n" + "   */\n" + "  @Deprecated\n" + "  @UiThread\n" + "  public Test_ViewBinding(Test target, View source) {\n" + "    this(target, source.getContext());\n" + "  }\n" + "  @UiThread\n" + "  @SuppressWarnings(\"ResourceType\")\n" + "  public Test_ViewBinding(Test target, Context context) {\n" + "    target.one = ResourcesCompat.getFont(context, 1);\n" + "  }\n" + "  @Override\n" + "  @CallSuper\n" + "  public void unbind() {\n" + "  }\n" + "}");
        assertAbout(javaSource()).that(source).withCompilerOptions("-Xlint:-processing").processedWith(new ButterKnifeProcessor()).compilesWithoutWarnings().and().generatesSources(bindingSource);
    }

    @Test
    public void simpleIntSdk26() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", "" + "package test;\n" + "import android.graphics.Typeface;\n" + "import butterknife.BindFont;\n" + "public class Test {\n" + "  @BindFont(1) Typeface one;\n" + "}");
        JavaFileObject bindingSource = JavaFileObjects.forSourceString("test/Test_ViewBinding", "" + "// Generated code from Butter Knife. Do not modify!\n" + "package test;\n" + "import android.content.Context;\n" + "import android.content.res.Resources;\n" + "import android.support.annotation.CallSuper;\n" + "import android.support.annotation.UiThread;\n" + "import android.view.View;\n" + "import butterknife.Unbinder;\n" + "import java.lang.Deprecated;\n" + "import java.lang.Override;\n" + "import java.lang.SuppressWarnings;\n" + "public class Test_ViewBinding implements Unbinder {\n" + "  /**\n" + "   * @deprecated Use {@link #Test_ViewBinding(Test, Context)} for direct creation.\n" + "   *     Only present for runtime invocation through {@code ButterKnife.bind()}.\n" + "   */\n" + "  @Deprecated\n" + "  @UiThread\n" + "  public Test_ViewBinding(Test target, View source) {\n" + "    this(target, source.getContext());\n" + "  }\n" + "  @UiThread\n" + "  @SuppressWarnings(\"ResourceType\")\n" + "  public Test_ViewBinding(Test target, Context context) {\n" + "    Resources res = context.getResources();\n" + "    target.one = res.getFont(1);\n" + "  }\n" + "  @Override\n" + "  @CallSuper\n" + "  public void unbind() {\n" + "  }\n" + "}");
        assertAbout(javaSource()).that(source).withCompilerOptions("-Xlint:-processing", "-Abutterknife.minSdk=26").processedWith(new ButterKnifeProcessor()).compilesWithoutWarnings().and().generatesSources(bindingSource);
    }

    @Test
    public void style() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", "" + "package test;\n" + "import android.graphics.Typeface;\n" + "import butterknife.BindFont;\n" + "public class Test {\n" + "  @BindFont(value = 1, style = Typeface.BOLD) Typeface one;\n" + "}");
        JavaFileObject bindingSource = JavaFileObjects.forSourceString("test/Test_ViewBinding", "" + "// Generated code from Butter Knife. Do not modify!\n" + "package test;\n" + "import android.content.Context;\n" + "import android.graphics.Typeface;\n" + "import android.support.annotation.CallSuper;\n" + "import android.support.annotation.UiThread;\n" + "import android.support.v4.content.res.ResourcesCompat;\n" + "import android.view.View;\n" + "import butterknife.Unbinder;\n" + "import java.lang.Deprecated;\n" + "import java.lang.Override;\n" + "import java.lang.SuppressWarnings;\n" + "public class Test_ViewBinding implements Unbinder {\n" + "  /**\n" + "   * @deprecated Use {@link #Test_ViewBinding(Test, Context)} for direct creation.\n" + "   *     Only present for runtime invocation through {@code ButterKnife.bind()}.\n" + "   */\n" + "  @Deprecated\n" + "  @UiThread\n" + "  public Test_ViewBinding(Test target, View source) {\n" + "    this(target, source.getContext());\n" + "  }\n" + "  @UiThread\n" + "  @SuppressWarnings(\"ResourceType\")\n" + "  public Test_ViewBinding(Test target, Context context) {\n" + "    target.one = Typeface.create(ResourcesCompat.getFont(context, 1), Typeface.BOLD);\n" + "  }\n" + "  @Override\n" + "  @CallSuper\n" + "  public void unbind() {\n" + "  }\n" + "}");
        assertAbout(javaSource()).that(source).withCompilerOptions("-Xlint:-processing").processedWith(new ButterKnifeProcessor()).compilesWithoutWarnings().and().generatesSources(bindingSource);
    }

    @Test
    public void styleSdk26() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", "" + "package test;\n" + "import android.graphics.Typeface;\n" + "import butterknife.BindFont;\n" + "public class Test {\n" + "  @BindFont(value = 1, style = Typeface.BOLD) Typeface one;\n" + "}");
        JavaFileObject bindingSource = JavaFileObjects.forSourceString("test/Test_ViewBinding", "" + "// Generated code from Butter Knife. Do not modify!\n" + "package test;\n" + "import android.content.Context;\n" + "import android.content.res.Resources;\n" + "import android.graphics.Typeface;\n" + "import android.support.annotation.CallSuper;\n" + "import android.support.annotation.UiThread;\n" + "import android.view.View;\n" + "import butterknife.Unbinder;\n" + "import java.lang.Deprecated;\n" + "import java.lang.Override;\n" + "import java.lang.SuppressWarnings;\n" + "public class Test_ViewBinding implements Unbinder {\n" + "  /**\n" + "   * @deprecated Use {@link #Test_ViewBinding(Test, Context)} for direct creation.\n" + "   *     Only present for runtime invocation through {@code ButterKnife.bind()}.\n" + "   */\n" + "  @Deprecated\n" + "  @UiThread\n" + "  public Test_ViewBinding(Test target, View source) {\n" + "    this(target, source.getContext());\n" + "  }\n" + "  @UiThread\n" + "  @SuppressWarnings(\"ResourceType\")\n" + "  public Test_ViewBinding(Test target, Context context) {\n" + "    Resources res = context.getResources();\n" + "    target.one = Typeface.create(res.getFont(1), Typeface.BOLD);\n" + "  }\n" + "  @Override\n" + "  @CallSuper\n" + "  public void unbind() {\n" + "  }\n" + "}");
        assertAbout(javaSource()).that(source).withCompilerOptions("-Xlint:-processing", "-Abutterknife.minSdk=26").processedWith(new ButterKnifeProcessor()).compilesWithoutWarnings().and().generatesSources(bindingSource);
    }

    @Test
    public void typeMustBeTypeface() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", "" + "package test;\n" + "import butterknife.BindFont;\n" + "public class Test {\n" + "  @BindFont(1) String one;\n" + "}");
        assertAbout(javaSource()).that(source).processedWith(new ButterKnifeProcessor()).failsToCompile().withErrorContaining("@BindFont field type must be 'Typeface'. (test.Test.one)").in(source).onLine(4);
    }

    @Test
    public void styleMustBeValid() {
        JavaFileObject source = JavaFileObjects.forSourceString("test.Test", "" + "package test;\n" + "import android.graphics.Typeface;\n" + "import butterknife.BindFont;\n" + "public class Test {\n" + "  @BindFont(value = 1, style = 5) Typeface one;\n" + "}");
        assertAbout(javaSource()).that(source).processedWith(new ButterKnifeProcessor()).failsToCompile().withErrorContaining("@BindFont style must be NORMAL, BOLD, ITALIC, or BOLD_ITALIC. (test.Test.one)").in(source).onLine(5);
    }
}
