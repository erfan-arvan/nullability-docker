package com.squareup.picasso3;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.squareup.picasso3.RequestHandler.Result;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowBitmap;
import static android.graphics.Bitmap.Config.ARGB_8888;
import static com.google.common.truth.Truth.assertThat;
import static com.squareup.picasso3.MediaStoreRequestHandler.PicassoKind.FULL;
import static com.squareup.picasso3.MediaStoreRequestHandler.PicassoKind.MICRO;
import static com.squareup.picasso3.MediaStoreRequestHandler.PicassoKind.MINI;
import static com.squareup.picasso3.MediaStoreRequestHandler.getPicassoKind;
import static com.squareup.picasso3.TestUtils.MEDIA_STORE_CONTENT_1_URL;
import static com.squareup.picasso3.TestUtils.MEDIA_STORE_CONTENT_KEY_1;
import static com.squareup.picasso3.TestUtils.makeBitmap;
import static com.squareup.picasso3.TestUtils.mockAction;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Shadows.shadowOf;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
public class MediaStoreRequestHandlerTest {

    @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull Context context;

    @org.checkerframework.dataflow.qual.Impure
    public void setUp() {
        initMocks(this);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void decodesVideoThumbnailWithVideoMimeType() {
        final Bitmap bitmap = makeBitmap();
        Request request = new Request.Builder(MEDIA_STORE_CONTENT_1_URL, 0, ARGB_8888).resize(100, 100).build();
        Action action = mockAction(MEDIA_STORE_CONTENT_KEY_1, request);
        MediaStoreRequestHandler requestHandler = create("video/");
        requestHandler.load(null, action.getRequest(), new RequestHandler.Callback() {

            @org.checkerframework.dataflow.qual.Impure
            public void onSuccess(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Result result) {
                assertBitmapsEqual(result.getBitmap(), bitmap);
            }

            @org.checkerframework.dataflow.qual.Impure
            public void onError(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Throwable t) {
                fail(t.getMessage());
            }
        });
    }

    @org.checkerframework.dataflow.qual.Impure
    public void decodesImageThumbnailWithImageMimeType() {
        final Bitmap bitmap = makeBitmap(20, 20);
        Request request = new Request.Builder(MEDIA_STORE_CONTENT_1_URL, 0, ARGB_8888).resize(100, 100).build();
        Action action = mockAction(MEDIA_STORE_CONTENT_KEY_1, request);
        MediaStoreRequestHandler requestHandler = create("image/png");
        requestHandler.load(null, action.getRequest(), new RequestHandler.Callback() {

            @org.checkerframework.dataflow.qual.Impure
            public void onSuccess(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Result result) {
                assertBitmapsEqual(result.getBitmap(), bitmap);
            }

            @org.checkerframework.dataflow.qual.Impure
            public void onError(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Throwable t) {
                fail(t.getMessage());
            }
        });
    }

    @org.checkerframework.dataflow.qual.Impure
    public void getPicassoKindMicro() {
        assertThat(getPicassoKind(96, 96)).isEqualTo(MICRO);
        assertThat(getPicassoKind(95, 95)).isEqualTo(MICRO);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void getPicassoKindMini() {
        assertThat(getPicassoKind(512, 384)).isEqualTo(MINI);
        assertThat(getPicassoKind(100, 100)).isEqualTo(MINI);
    }

    @org.checkerframework.dataflow.qual.Impure
    public void getPicassoKindFull() {
        assertThat(getPicassoKind(513, 385)).isEqualTo(FULL);
        assertThat(getPicassoKind(1000, 1000)).isEqualTo(FULL);
        assertThat(getPicassoKind(1000, 384)).isEqualTo(FULL);
        assertThat(getPicassoKind(1000, 96)).isEqualTo(FULL);
        assertThat(getPicassoKind(96, 1000)).isEqualTo(FULL);
    }

    @org.checkerframework.dataflow.qual.Impure
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull MediaStoreRequestHandler create(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String mimeType) {
        ContentResolver contentResolver = mock(ContentResolver.class);
        when(contentResolver.getType(any(Uri.class))).thenReturn(mimeType);
        return create(contentResolver);
    }

    @org.checkerframework.dataflow.qual.Impure
    private @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull MediaStoreRequestHandler create(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ContentResolver contentResolver) {
        when(context.getContentResolver()).thenReturn(contentResolver);
        return new MediaStoreRequestHandler(context);
    }

    @org.checkerframework.dataflow.qual.Impure
    static void assertBitmapsEqual(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Bitmap a, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap b) {
        ShadowBitmap shadowA = shadowOf(a);
        ShadowBitmap shadowB = shadowOf(b);
        if (shadowA.getHeight() != shadowB.getHeight()) {
            fail();
        }
        if (shadowA.getWidth() != shadowB.getWidth()) {
            fail();
        }
        if (shadowA.getDescription() != null ? !shadowA.getDescription().equals(shadowB.getDescription()) : shadowB.getDescription() != null) {
            fail();
        }
    }
}
