/*
* Copyright (C) 2013 Square, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.squareup.picasso3;

import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RemoteViews;
import com.squareup.picasso3.Picasso.RequestTransformer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import okhttp3.Call;
import okhttp3.Response;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static android.content.ContentResolver.SCHEME_ANDROID_RESOURCE;
import static android.graphics.Bitmap.Config.ALPHA_8;
import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.provider.ContactsContract.Contacts.CONTENT_URI;
import static android.provider.ContactsContract.Contacts.Photo.CONTENT_DIRECTORY;
import static com.squareup.picasso3.Picasso.LoadedFrom.MEMORY;
import static com.squareup.picasso3.Picasso.Priority;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
class TestUtils {

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Answer<Object> TRANSFORM_REQUEST_ANSWER = new Answer<Object>() {

        @org.checkerframework.dataflow.qual.Impure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Object answer(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull InvocationOnMock invocation) throws Throwable {
            return invocation.getArguments()[0];
        }
    };

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Uri URI_1 = Uri.parse("http://example.com/1.png");

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Uri URI_2 = Uri.parse("http://example.com/2.png");

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String STABLE_1 = "stableExampleKey1";

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String URI_KEY_1 = new Request.Builder(URI_1).build().key;

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String URI_KEY_2 = new Request.Builder(URI_2).build().key;

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String STABLE_URI_KEY_1 = new Request.Builder(URI_1).stableKey(STABLE_1).build().key;

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull File FILE_1 = new File("C:\\windows\\system32\\logo.exe");

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String FILE_KEY_1 = new Request.Builder(Uri.fromFile(FILE_1)).build().key;

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Uri FILE_1_URL = Uri.parse("file:///" + FILE_1.getPath());

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Uri FILE_1_URL_NO_AUTHORITY = Uri.parse("file:/" + FILE_1.getParent());

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Uri MEDIA_STORE_CONTENT_1_URL = Uri.parse("content://media/external/images/media/1");

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String MEDIA_STORE_CONTENT_KEY_1 = new Request.Builder(MEDIA_STORE_CONTENT_1_URL).build().key;

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Uri CONTENT_1_URL = Uri.parse("content://zip/zap/zoop.jpg");

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String CONTENT_KEY_1 = new Request.Builder(CONTENT_1_URL).build().key;

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Uri CONTACT_URI_1 = CONTENT_URI.buildUpon().appendPath("1234").build();

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String CONTACT_KEY_1 = new Request.Builder(CONTACT_URI_1).build().key;

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Uri CONTACT_PHOTO_URI_1 = CONTENT_URI.buildUpon().appendPath("1234").appendPath(CONTENT_DIRECTORY).build();

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String CONTACT_PHOTO_KEY_1 = new Request.Builder(CONTACT_PHOTO_URI_1).build().key;

    static final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int RESOURCE_ID_1 = 1;

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String RESOURCE_ID_KEY_1 = new Request.Builder(RESOURCE_ID_1).build().key;

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Uri ASSET_URI_1 = Uri.parse("file:///android_asset/foo/bar.png");

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String ASSET_KEY_1 = new Request.Builder(ASSET_URI_1).build().key;

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String RESOURCE_PACKAGE = "com.squareup.picasso3";

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String RESOURCE_TYPE = "drawable";

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String RESOURCE_NAME = "foo";

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Uri RESOURCE_ID_URI = new Uri.Builder().scheme(SCHEME_ANDROID_RESOURCE).authority(RESOURCE_PACKAGE).appendPath(Integer.toString(RESOURCE_ID_1)).build();

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String RESOURCE_ID_URI_KEY = new Request.Builder(RESOURCE_ID_URI).build().key;

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Uri RESOURCE_TYPE_URI = new Uri.Builder().scheme(SCHEME_ANDROID_RESOURCE).authority(RESOURCE_PACKAGE).appendPath(RESOURCE_TYPE).appendPath(RESOURCE_NAME).build();

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String RESOURCE_TYPE_URI_KEY = new Request.Builder(RESOURCE_TYPE_URI).build().key;

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Uri CUSTOM_URI = Uri.parse("foo://bar");

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String CUSTOM_URI_KEY = new Request.Builder(CUSTOM_URI).build().key;

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String BITMAP_RESOURCE_VALUE = "foo.png";

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String XML_RESOURCE_VALUE = "foo.xml";

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Context mockPackageResourceContext() {
        Context context = mock(Context.class);
        PackageManager pm = mock(PackageManager.class);
        Resources res = mock(Resources.class);
        doReturn(pm).when(context).getPackageManager();
        try {
            doReturn(res).when(pm).getResourcesForApplication(RESOURCE_PACKAGE);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        doReturn(RESOURCE_ID_1).when(res).getIdentifier(RESOURCE_NAME, RESOURCE_TYPE, RESOURCE_PACKAGE);
        return context;
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Resources mockResources(final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String resValueString) {
        Resources resources = mock(Resources.class);
        doAnswer(new Answer<Void>() {

            @org.checkerframework.dataflow.qual.Impure
            public @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Void answer(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                ((TypedValue) args[1]).string = resValueString;
                return null;
            }
        }).when(resources).getValue(anyInt(), any(TypedValue.class), anyBoolean());
        return resources;
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Action mockAction(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String key, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Uri uri) {
        return mockAction(key, uri, null, 0, null, null);
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Action mockAction(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String key, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Uri uri, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Object target) {
        return mockAction(key, uri, target, 0, null, null);
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Action mockAction(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String key, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Uri uri, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Priority priority) {
        return mockAction(key, uri, null, 0, priority, null);
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Action mockAction(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String key, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Uri uri, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String tag) {
        return mockAction(key, uri, null, 0, null, tag);
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Action mockAction(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String key, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Uri uri, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Object target, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String tag) {
        return mockAction(key, uri, target, 0, null, tag);
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Action mockAction(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String key, @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Uri uri, @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Object target,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int resourceId) {
        return mockAction(key, uri, target, resourceId, null, null);
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Action mockAction(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String key, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Uri uri, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Object target,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int resourceId, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Priority priority, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String tag) {
        Request request = new Request.Builder(uri, resourceId, ARGB_8888).build();
        return mockAction(key, request, target, priority, tag);
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Action mockAction(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String key, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Request request) {
        return mockAction(key, request, null, null, null);
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Action mockAction(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String key, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Request request, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Object target, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Priority priority, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable String tag) {
        Action action = mock(Action.class);
        when(action.getKey()).thenReturn(key);
        when(action.getRequest()).thenReturn(request);
        when(action.getTarget()).thenReturn(target);
        when(action.getPriority()).thenReturn(priority != null ? priority : Priority.NORMAL);
        when(action.getTag()).thenReturn(tag != null ? tag : action);
        Picasso picasso = mockPicasso();
        when(action.getPicasso()).thenReturn(picasso);
        return action;
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Action mockCanceledAction() {
        Action action = mock(Action.class);
        action.cancelled = true;
        when(action.isCancelled()).thenReturn(true);
        return action;
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ImageView mockImageViewTarget() {
        return mock(ImageView.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RemoteViews mockRemoteViews() {
        return mock(RemoteViews.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Notification mockNotification() {
        return mock(Notification.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull ImageView mockFitImageViewTarget( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean alive) {
        ViewTreeObserver observer = mock(ViewTreeObserver.class);
        when(observer.isAlive()).thenReturn(alive);
        ImageView mock = mock(ImageView.class);
        when(mock.getWindowToken()).thenReturn(mock(IBinder.class));
        when(mock.getViewTreeObserver()).thenReturn(observer);
        return mock;
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BitmapTarget mockTarget() {
        return mock(BitmapTarget.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    static RemoteViewsAction.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RemoteViewsTarget mockRemoteViewsTarget() {
        return mock(RemoteViewsAction.RemoteViewsTarget.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Callback mockCallback() {
        return mock(Callback.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull DeferredRequestCreator mockDeferredRequestCreator() {
        return mock(DeferredRequestCreator.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NetworkInfo mockNetworkInfo() {
        return mockNetworkInfo(false);
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NetworkInfo mockNetworkInfo( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean isConnected) {
        NetworkInfo mock = mock(NetworkInfo.class);
        when(mock.isConnected()).thenReturn(isConnected);
        when(mock.isConnectedOrConnecting()).thenReturn(isConnected);
        return mock;
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull InputStream mockInputStream() throws IOException {
        return mock(InputStream.class);
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BitmapHunter mockHunter(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String key, RequestHandler.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Result result) {
        return mockHunter(key, result, null);
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull BitmapHunter mockHunter(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull String key, RequestHandler.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Result result, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Action action) {
        Request data = new Request.Builder(URI_1).build();
        BitmapHunter hunter = mock(BitmapHunter.class);
        when(hunter.getKey()).thenReturn(key);
        when(hunter.getResult()).thenReturn(result);
        when(hunter.getData()).thenReturn(data);
        when(hunter.getAction()).thenReturn(action);
        Picasso picasso = mockPicasso();
        when(hunter.getPicasso()).thenReturn(picasso);
        return hunter;
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Picasso mockPicasso() {
        // Inject a RequestHandler that can handle any request.
        RequestHandler requestHandler = new RequestHandler() {

            @org.checkerframework.dataflow.qual.Pure
            public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean canHandleRequest(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Request data) {
                return true;
            }

            @org.checkerframework.dataflow.qual.Impure
            public void load(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Picasso picasso, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Request request, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Callback callback) {
                Bitmap defaultResult = makeBitmap();
                RequestHandler.Result result = new RequestHandler.Result(defaultResult, MEMORY);
                callback.onSuccess(result);
            }
        };
        return mockPicasso(requestHandler);
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Picasso mockPicasso(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RequestHandler requestHandler) {
        Picasso picasso = mock(Picasso.class);
        when(picasso.getRequestHandlers()).thenReturn(Collections.singletonList(requestHandler));
        return picasso;
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap makeBitmap() {
        return makeBitmap(10, 10);
    }

    @org.checkerframework.dataflow.qual.Impure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Bitmap makeBitmap( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int width,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int height) {
        return Bitmap.createBitmap(width, height, ALPHA_8);
    }

    @org.checkerframework.dataflow.qual.Pure
    static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull DrawableLoader makeLoaderWithDrawable(final @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Drawable drawable) {
        return new DrawableLoader() {

            @org.checkerframework.dataflow.qual.Pure
            public @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Drawable load( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int resId) {
                return drawable;
            }
        };
    }

    static final Call.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Factory UNUSED_CALL_FACTORY = new Call.Factory() {

        @org.checkerframework.dataflow.qual.Pure
        public Call newCall(okhttp3.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Request request) {
            throw new AssertionError();
        }
    };

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<RequestTransformer> NO_TRANSFORMERS = Collections.emptyList();

    static final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull List<RequestHandler> NO_HANDLERS = Collections.emptyList();

    static final class PremadeCall implements Call {

        private final okhttp3.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Request request;

        private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Response response;

        @org.checkerframework.dataflow.qual.SideEffectFree
        PremadeCall(okhttp3.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Request request, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Response response) {
            this.request = request;
            this.response = response;
        }

        @org.checkerframework.dataflow.qual.Pure
        public okhttp3.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Request request(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PremadeCall this) {
            return request;
        }

        @org.checkerframework.dataflow.qual.Pure
        public @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Response execute(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PremadeCall this) {
            return response;
        }

        @org.checkerframework.dataflow.qual.Impure
        public void enqueue(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PremadeCall this, okhttp3.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Callback responseCallback) {
            try {
                responseCallback.onResponse(this, response);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        @org.checkerframework.dataflow.qual.SideEffectFree
        public void cancel(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PremadeCall this) {
            throw new AssertionError();
        }

        @org.checkerframework.dataflow.qual.Pure
        public boolean isExecuted(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PremadeCall this) {
            throw new AssertionError();
        }

        @org.checkerframework.dataflow.qual.Pure
        public boolean isCanceled(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PremadeCall this) {
            throw new AssertionError();
        }

        @org.checkerframework.dataflow.qual.Pure
        public Call clone(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull PremadeCall this) {
            throw new AssertionError();
        }
    }

    @org.checkerframework.dataflow.qual.SideEffectFree
    private TestUtils() {
    }
}
