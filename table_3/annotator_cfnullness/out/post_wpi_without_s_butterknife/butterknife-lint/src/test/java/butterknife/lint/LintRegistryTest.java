package butterknife.lint;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;
import android.support.annotation.Nullable;

public final class LintRegistryTest {

    @Test
    public void issues() {
        assertThat(new LintRegistry().getIssues()).contains(InvalidR2UsageDetector.ISSUE);
    }
}
