
package com.thiner.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public final class AuthPreferences {

    private static final String KEY_USER = "user";
    private static final String KEY_ID = "id";

    private static SharedPreferences preferences;

    private AuthPreferences(final Context context) {
    }

    public static SharedPreferences getInstance(final Activity activity) {
        synchronized (activity) {
            if (preferences == null) {
                preferences = activity.getSharedPreferences("auth", Context.MODE_PRIVATE);
            }

            return preferences;
        }
    }

    public static void setUser(final Activity activity, final String user) {
        getInstance(activity).edit().putString(KEY_USER, user).commit();
    }

    public static void setId(final Activity activity, final String id) {
        getInstance(activity).edit().putString(KEY_ID, id).commit();
    }

    public static String getUser(final Activity activity) {
        return getInstance(activity).getString(KEY_USER, null);
    }

    public static String getID(final Activity activity) {
        return getInstance(activity).getString(KEY_ID, null);
    }
}
