package butterknife.functional;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import butterknife.BindString;
import butterknife.Unbinder;
import butterknife.runtime.test.R;
import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;
import android.support.annotation.Nullable;

public final class BindStringTest {

    private final Context context = InstrumentationRegistry.getContext();

    static class Target {

        @BindString(R.string.hey)
        String actual;
    }

    @Test
    public void simpleInt() {
        Target target = new Target();
        String expected = context.getString(R.string.hey);
        Unbinder unbinder = new BindStringTest$Target_ViewBinding(target, context);
        assertThat(target.actual).isEqualTo(expected);
        unbinder.unbind();
        assertThat(target.actual).isEqualTo(expected);
    }
}
