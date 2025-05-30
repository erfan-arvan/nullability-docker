/*
 * Copyright (C) 2014 Square, Inc.
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
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;

@org.checkerframework.framework.qual.AnnotatedFor("org.checkerframework.checker.nullness.NullnessChecker")
abstract class RemoteViewsAction extends Action<RemoteViewsAction.RemoteViewsTarget> {

    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Target<RemoteViewsTarget> remoteWrapper;

    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Callback callback;

    @org.checkerframework.dataflow.qual.SideEffectFree
    RemoteViewsAction(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Picasso picasso, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Request data, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Target<RemoteViewsTarget> wrapper, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Callback callback) {
        super(picasso, null, data);
        this.remoteWrapper = wrapper;
        this.callback = callback;
    }

    @org.checkerframework.dataflow.qual.Impure
    void complete(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RemoteViewsAction this, RequestHandler.@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Result result) {
        RemoteViewsTarget target = remoteWrapper.target;
        target.remoteViews.setImageViewBitmap(target.viewId, result.getBitmap());
        update();
        if (callback != null) {
            callback.onSuccess();
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    void cancel(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RemoteViewsAction this) {
        super.cancel();
        if (callback != null) {
            callback = null;
        }
    }

    @org.checkerframework.dataflow.qual.Impure
    public void error(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RemoteViewsAction this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Exception e) {
        if (remoteWrapper.errorResId != 0) {
            setImageResource(remoteWrapper.errorResId);
        }
        if (callback != null) {
            callback.onError(e);
        }
    }

    @org.checkerframework.dataflow.qual.Pure
    @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RemoteViewsTarget getTarget(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RemoteViewsAction this) {
        return remoteWrapper.target;
    }

    @org.checkerframework.dataflow.qual.Impure
    void setImageResource( @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int resId) {
        RemoteViewsTarget target = remoteWrapper.target;
        target.remoteViews.setImageViewResource(target.viewId, resId);
        update();
    }

    @org.checkerframework.dataflow.qual.Impure
    abstract void update();

    static class RemoteViewsTarget {

        final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.MonotonicNonNull RemoteViews remoteViews;

        final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int viewId;

        @org.checkerframework.dataflow.qual.SideEffectFree
        RemoteViewsTarget(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable RemoteViews remoteViews,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int viewId) {
            this.remoteViews = remoteViews;
            this.viewId = viewId;
        }

        @org.checkerframework.dataflow.qual.Pure
        public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull boolean equals(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RemoteViewsTarget this, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            RemoteViewsTarget remoteViewsTarget = (RemoteViewsTarget) o;
            return viewId == remoteViewsTarget.viewId && remoteViews.equals(remoteViewsTarget.remoteViews);
        }

        @org.checkerframework.dataflow.qual.Pure
        public  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int hashCode(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull RemoteViewsTarget this) {
            return 31 * remoteViews.hashCode() + viewId;
        }
    }

    static class AppWidgetAction extends RemoteViewsAction {

        private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull [] appWidgetIds;

        @org.checkerframework.dataflow.qual.SideEffectFree
        AppWidgetAction(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Picasso picasso, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Request data, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Target<RemoteViewsTarget> wrapper,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull [] appWidgetIds, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Callback callback) {
            super(picasso, data, wrapper, callback);
            this.appWidgetIds = appWidgetIds;
        }

        @org.checkerframework.dataflow.qual.Impure
        void update(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull AppWidgetAction this) {
            AppWidgetManager manager = AppWidgetManager.getInstance(picasso.context);
            manager.updateAppWidget(appWidgetIds, remoteWrapper.target.remoteViews);
        }
    }

    static class NotificationAction extends RemoteViewsAction {

        private final  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int notificationId;

        private final @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.MonotonicNonNull String notificationTag;

        private final @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Notification notification;

        @org.checkerframework.dataflow.qual.SideEffectFree
        NotificationAction(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.Nullable Picasso picasso, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Request data, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Target<RemoteViewsTarget> wrapper,  @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull int notificationId, @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull Notification notification, @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable String notificationTag, @org.checkerframework.checker.initialization.qual.FBCBottom @org.checkerframework.checker.nullness.qual.Nullable Callback callback) {
            super(picasso, data, wrapper, callback);
            this.notificationId = notificationId;
            this.notificationTag = notificationTag;
            this.notification = notification;
        }

        @org.checkerframework.dataflow.qual.Impure
        void update(@org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker.nullness.qual.NonNull NotificationAction this) {
            NotificationManager manager = ContextCompat.getSystemService(picasso.context, NotificationManager.class);
            manager.notify(notificationTag, notificationId, notification);
        }
    }
}
