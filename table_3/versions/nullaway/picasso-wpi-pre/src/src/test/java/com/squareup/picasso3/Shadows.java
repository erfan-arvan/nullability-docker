package com.squareup.picasso3;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import static com.squareup.picasso3.TestUtils.makeBitmap;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
final class Shadows {

    public static class ShadowVideoThumbnails {

        @org.checkerframework.dataflow.qual.Impure
        public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap getThumbnail(ContentResolver cr, long origId, int kind, BitmapFactory.Options options) {
            return makeBitmap();
        }

        @org.checkerframework.dataflow.qual.SideEffectFree
        private ShadowVideoThumbnails() {
        }
    }

    public static class ShadowImageThumbnails {

        @org.checkerframework.dataflow.qual.Impure
        public static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap getThumbnail(ContentResolver cr, long origId, int kind, BitmapFactory.Options options) {
            return makeBitmap(20, 20);
        }

        @org.checkerframework.dataflow.qual.SideEffectFree
        private ShadowImageThumbnails() {
        }
    }

    @org.checkerframework.dataflow.qual.SideEffectFree
    private Shadows() {
    }
}
