package butterknife.functional;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import butterknife.BindInt;
import butterknife.Unbinder;
import butterknife.runtime.test.R;
import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;
import android.support.annotation.Nullable;

public final class BindIntTest {

    private final Context context = InstrumentationRegistry.getContext();

    static class Target {

        @BindInt(R.integer.twelve)
        int actual;
    }

    @Test
    public void asInt() {
        Target target = new Target();
        int expected = context.getResources().getInteger(R.integer.twelve);
        Unbinder unbinder = new BindIntTest$Target_ViewBinding(target, context);
        assertThat(target.actual).isEqualTo(expected);
        unbinder.unbind();
        assertThat(target.actual).isEqualTo(expected);
    }
}
