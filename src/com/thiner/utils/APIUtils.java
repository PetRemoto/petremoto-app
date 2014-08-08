
package com.thiner.utils;

public class APIUtils {

    private static final String URL = "http://thiner.herokuapp.com/api/user";
    //    private static final String URL = "http://192.168.1.244:5000/api/user";

    private static final String URL_LOGIN = "/login";

    private static final String URL_EDIT_PROFILE = "/edit";

    private static final String URL_SEARCH = "/search";

    private static final String URL_REMOVE_FRIEND = "/friend/remove";

    private static final String URL_REQUEST_FRIEND = "/request";

    private static final String URL_ACCEPT_FRIEND = "/request/accept";

    private static final String URL_IGNORE_FRIEND = "/request/remove";

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

    public static String getApiUrlSearch() {
        return getApiUrl() + URL_SEARCH;
    }

    public static String getApiUrlRemoveFriend() {
        return getApiUrl() + URL_REMOVE_FRIEND;
    }

    public static String getApiUrlRequestFriend() {
        return getApiUrl() + URL_REQUEST_FRIEND;
    }

    public static String getApiUrlAcceptFriend() {
        return getApiUrl() + URL_ACCEPT_FRIEND;
    }

    public static String getApiUrlIgnoreFriend() {
        return getApiUrl() + URL_IGNORE_FRIEND;
    }
}
