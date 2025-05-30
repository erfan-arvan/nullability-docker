package butterknife.functional;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import butterknife.BindBool;
import butterknife.Unbinder;
import butterknife.runtime.test.R;
import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;
import android.support.annotation.Nullable;

public final class BindBoolTest {

    private final Context context = InstrumentationRegistry.getContext();

    static class Target {

        @BindBool(R.bool.just_true)
        boolean actual;
    }

    @Test
    public void asBoolean() {
        Target target = new Target();
        boolean expected = context.getResources().getBoolean(R.bool.just_true);
        Unbinder unbinder = new BindBoolTest$Target_ViewBinding(target, context);
        assertThat(target.actual).isEqualTo(expected);
        unbinder.unbind();
        assertThat(target.actual).isEqualTo(expected);
    }
}
