
package com.petremoto.utils;

public class APIUtils {

    private static final String URL = "http://petremoto.herokuapp.com/api/user";
    //    private static final String URL = "http://192.168.1.244:50001/api/user";

    private static final String URL_LOGIN = "/login";

    private static final String URL_FEEDING = "/dispenser/feed";

    private static final String URL_EDIT_PROFILE = "/edit";

    public static String getApiUrl() {
        return URL;
    }

    public static String getApiUrlLogin() {
        return getApiUrl() + URL_LOGIN;
    }

    public static String putAttrs(final String key, final String value) {
        return key + "=" + value;
    }

    public static String getApiUrlEditProfile() {
        return getApiUrl() + URL_EDIT_PROFILE;
    }

    public static String getApiUrlFeeding() {
        return getApiUrl() + URL_FEEDING;
    }
}
