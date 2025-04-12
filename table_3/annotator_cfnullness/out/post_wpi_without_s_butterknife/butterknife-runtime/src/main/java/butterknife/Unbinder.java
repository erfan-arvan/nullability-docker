package butterknife;

import android.support.annotation.UiThread;
import android.support.annotation.Nullable;

/**
 * An unbinder contract that will unbind views when called.
 */
public interface Unbinder {

    @UiThread
    void unbind();

    Unbinder EMPTY = new Unbinder() {

        @Override
        public void unbind() {
        }
    };
}
