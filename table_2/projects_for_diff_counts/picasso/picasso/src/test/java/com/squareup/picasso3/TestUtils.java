package com.squareup.picasso3;
class TestUtils {
  static final Answer<Object> TRANSFORM_REQUEST_ANSWER = new Answer<Object>() {
    @Override public Object answer(InvocationOnMock invocation) throws Throwable {
      return invocation.getArguments()[0];
    }
  };
  static final Uri URI_1 = Uri.parse("http:
  static final Uri URI_2 = Uri.parse("http:
  static final String STABLE_1 = "stableExampleKey1";
  static final String URI_KEY_1 = new Request.Builder(URI_1).build().key;
  static final String URI_KEY_2 = new Request.Builder(URI_2).build().key;
  static final String STABLE_URI_KEY_1 = new Request.Builder(URI_1).stableKey(STABLE_1).build().key;
  static final File FILE_1 = new File("C:\\windows\\system32\\logo.exe");
  static final String FILE_KEY_1 = new Request.Builder(Uri.fromFile(FILE_1)).build().key;
  static final Uri FILE_1_URL = Uri.parse("file:
  static final Uri FILE_1_URL_NO_AUTHORITY = Uri.parse("file:/" + FILE_1.getParent());
  static final Uri MEDIA_STORE_CONTENT_1_URL = Uri.parse("content:
  static final String MEDIA_STORE_CONTENT_KEY_1 =
      new Request.Builder(MEDIA_STORE_CONTENT_1_URL).build().key;
  static final Uri CONTENT_1_URL = Uri.parse("content:
  static final String CONTENT_KEY_1 = new Request.Builder(CONTENT_1_URL).build().key;
  static final Uri CONTACT_URI_1 = CONTENT_URI.buildUpon().appendPath("1234").build();
  static final String CONTACT_KEY_1 = new Request.Builder(CONTACT_URI_1).build().key;
  static final Uri CONTACT_PHOTO_URI_1 =
      CONTENT_URI.buildUpon().appendPath("1234").appendPath(CONTENT_DIRECTORY).build();
  static final String CONTACT_PHOTO_KEY_1 = new Request.Builder(CONTACT_PHOTO_URI_1).build().key;
  static final int RESOURCE_ID_1 = 1;
  static final String RESOURCE_ID_KEY_1 = new Request.Builder(RESOURCE_ID_1).build().key;
  static final Uri ASSET_URI_1 = Uri.parse("file:
  static final String ASSET_KEY_1 = new Request.Builder(ASSET_URI_1).build().key;
  static final String RESOURCE_PACKAGE = "com.squareup.picasso3";
  static final String RESOURCE_TYPE = "drawable";
  static final String RESOURCE_NAME = "foo";
  static final Uri RESOURCE_ID_URI = new Uri.Builder().scheme(SCHEME_ANDROID_RESOURCE)
      .authority(RESOURCE_PACKAGE)
      .appendPath(Integer.toString(RESOURCE_ID_1))
      .build();
  static final String RESOURCE_ID_URI_KEY = new Request.Builder(RESOURCE_ID_URI).build().key;
  static final Uri RESOURCE_TYPE_URI = new Uri.Builder().scheme(SCHEME_ANDROID_RESOURCE)
      .authority(RESOURCE_PACKAGE)
      .appendPath(RESOURCE_TYPE)
      .appendPath(RESOURCE_NAME)
      .build();
  static final String RESOURCE_TYPE_URI_KEY =
      new Request.Builder(RESOURCE_TYPE_URI).build().key;
  static final Uri CUSTOM_URI = Uri.parse("foo:
  static final String CUSTOM_URI_KEY = new Request.Builder(CUSTOM_URI).build().key;
  static final String BITMAP_RESOURCE_VALUE = "foo.png";
  static final String XML_RESOURCE_VALUE = "foo.xml";
  static Context mockPackageResourceContext() {
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
  static Resources mockResources(final String resValueString) {
    Resources resources = mock(Resources.class);
    doAnswer(new Answer<Void>() {
      @Override public Void answer(InvocationOnMock invocation) {
        Object[] args = invocation.getArguments();
        ((TypedValue) args[1]).string = resValueString;
        return null;
      }
    }).when(resources).getValue(anyInt(), any(TypedValue.class), anyBoolean());
    return resources;
  }
  static Action mockAction(String key, Uri uri) {
    return mockAction(key, uri, null, 0, null, null);
  }
  static Action mockAction(String key, Uri uri, Object target) {
    return mockAction(key, uri, target, 0, null, null);
  }
  static Action mockAction(String key, Uri uri, Priority priority) {
    return mockAction(key, uri, null, 0, priority, null);
  }
  static Action mockAction(String key, Uri uri, String tag) {
    return mockAction(key, uri, null, 0, null, tag);
  }
  static Action mockAction(String key, Uri uri, Object target, String tag) {
    return mockAction(key, uri, target, 0, null, tag);
  }
  static Action mockAction(String key, Uri uri, Object target, int resourceId) {
    return mockAction(key, uri, target, resourceId, null, null);
  }
  static Action mockAction(String key, Uri uri, Object target, int resourceId, Priority priority,
                           String tag) {
    Request request = new Request.Builder(uri, resourceId, ARGB_8888).build();
    return mockAction(key, request, target, priority, tag);
  }
  static Action mockAction(String key, Request request) {
    return mockAction(key, request, null, null, null);
  }
  static Action mockAction(String key, Request request, Object target, Priority priority,
                           String tag) {
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
  static Action mockCanceledAction() {
    Action action = mock(Action.class);
    action.cancelled = true;
    when(action.isCancelled()).thenReturn(true);
    return action;
  }
  static ImageView mockImageViewTarget() {
    return mock(ImageView.class);
  }
  static RemoteViews mockRemoteViews() {
    return mock(RemoteViews.class);
  }
  static Notification mockNotification() {
    return mock(Notification.class);
  }
  static ImageView mockFitImageViewTarget(boolean alive) {
    ViewTreeObserver observer = mock(ViewTreeObserver.class);
    when(observer.isAlive()).thenReturn(alive);
    ImageView mock = mock(ImageView.class);
    when(mock.getWindowToken()).thenReturn(mock(IBinder.class));
    when(mock.getViewTreeObserver()).thenReturn(observer);
    return mock;
  }
  static BitmapTarget mockTarget() {
    return mock(BitmapTarget.class);
  }
  static RemoteViewsAction.RemoteViewsTarget mockRemoteViewsTarget() {
    return mock(RemoteViewsAction.RemoteViewsTarget.class);
  }
  static Callback mockCallback() {
    return mock(Callback.class);
  }
  static DeferredRequestCreator mockDeferredRequestCreator() {
    return mock(DeferredRequestCreator.class);
  }
  static NetworkInfo mockNetworkInfo() {
    return mockNetworkInfo(false);
  }
  static NetworkInfo mockNetworkInfo(boolean isConnected) {
    NetworkInfo mock = mock(NetworkInfo.class);
    when(mock.isConnected()).thenReturn(isConnected);
    when(mock.isConnectedOrConnecting()).thenReturn(isConnected);
    return mock;
  }
  static InputStream mockInputStream() throws IOException {
    return mock(InputStream.class);
  }
  static BitmapHunter mockHunter(String key, RequestHandler.Result result) {
    return mockHunter(key, result, null);
  }
  static BitmapHunter mockHunter(String key, RequestHandler.Result result, Action action) {
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
  static Picasso mockPicasso() {
    RequestHandler requestHandler = new RequestHandler() {
      @Override public boolean canHandleRequest( Request data) {
        return true;
      }
      @Override public void load( Picasso picasso,  Request request, 
          Callback callback) {
        Bitmap defaultResult = makeBitmap();
        RequestHandler.Result result = new RequestHandler.Result(defaultResult, MEMORY);
        callback.onSuccess(result);
      }
    };
    return mockPicasso(requestHandler);
  }
  static Picasso mockPicasso(RequestHandler requestHandler) {
    Picasso picasso = mock(Picasso.class);
    when(picasso.getRequestHandlers()).thenReturn(Collections.singletonList(requestHandler));
    return picasso;
  }
  static Bitmap makeBitmap() {
    return makeBitmap(10, 10);
  }
  static Bitmap makeBitmap(int width, int height) {
    return Bitmap.createBitmap(width, height, ALPHA_8);
  }
  static DrawableLoader makeLoaderWithDrawable(final Drawable drawable) {
    return new DrawableLoader() {
      @Override public Drawable load(int resId) {
        return drawable;
      }
    };
  }
  static final Call.Factory UNUSED_CALL_FACTORY = new Call.Factory() {
    @Override public Call newCall(okhttp3.Request request) {
      throw new AssertionError();
    }
  };
  static final List<RequestTransformer> NO_TRANSFORMERS = Collections.emptyList();
  static final List<RequestHandler> NO_HANDLERS = Collections.emptyList();
  static final class PremadeCall implements Call {
    private final okhttp3.Request request;
    private final Response response;
    PremadeCall(okhttp3.Request request, Response response) {
      this.request = request;
      this.response = response;
    }
    @Override public okhttp3.Request request() {
      return request;
    }
    @Override public Response execute() {
      return response;
    }
    @Override public void enqueue( okhttp3.Callback responseCallback) {
      try {
        responseCallback.onResponse(this, response);
      } catch (IOException e) {
        throw new AssertionError(e);
      }
    }
    @Override public void cancel() {
      throw new AssertionError();
    }
    @Override public boolean isExecuted() {
      throw new AssertionError();
    }
    @Override public boolean isCanceled() {
      throw new AssertionError();
    }
    @Override public Call clone() {
      throw new AssertionError();
    }
  }
  private TestUtils() {
  }
}
