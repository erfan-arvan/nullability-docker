package butterknife;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.List;

final class CompositeUnbinder implements Unbinder {

    @Nullable
    private List<Unbinder> unbinders;

    CompositeUnbinder(@NonNull List<Unbinder> unbinders) {
        this.unbinders = unbinders;
    }

    @Override
    public void unbind() {
        if (unbinders == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        for (Unbinder unbinder : unbinders) {
            unbinder.unbind();
        }
        unbinders = null;
    }
}
