
package com.thiner.utils;

import android.util.Log;

/**
 * The Class MyLog.
 */
public final class MyLog {

    private MyLog() {

    }

    /**
     * The Constant TAG.
     */
    private static final String TAG = "POMT";

    /**
     * The Constant ERROR.
     */
    private static final String ERROR = "ERROR";

    /**
     * The Constant WARNING.
     */
    private static final String WARNING = "WARNING";

    /**
     * The Constant INFO.
     */
    private static final String INFO = "INFO";

    /**
     * The Constant DEBUG.
     */
    private static final String DEBUG = "DEBUG";

    /**
     * The Constant LEVEL. None: 0; ERROR: 1; WARN: 2; INFO: 3; DEBUG: 4;
     */
    private static final int LEVEL = 4;

    /**
     * Sends a debug message.
     *
     * @param msg the msg to be sent
     */
    public static void debug(final String msg) {
        if (LEVEL >= 4) {
            Log.d(TAG + ":" + DEBUG, msg);
        }
    }

    /**
     * Sends a debug message and the exception message.
     *
     * @param msg the msg to be sent
     * @param e the exception
     */
    public static void debug(final String msg, final Exception e) {
        if (LEVEL >= 4) {
            Log.d(TAG + ":" + DEBUG, msg + ": " + e.getMessage());
        }
    }

    /**
     * Sends a error message.
     *
     * @param msg the msg to be sent
     */
    public static void error(final String msg) {
        if (LEVEL >= 1) {
            Log.e(TAG + ":" + ERROR, msg);
        }
    }

    /**
     * Sends a error message and the exception message.
     *
     * @param msg the msg to be sent
     * @param e the exception
     */
    public static void error(final String msg, final Exception e) {
        if (LEVEL >= 1) {
            Log.e(TAG + ":" + ERROR, msg + ": " + e.getMessage());
        }
    }

    /**
     * Sends a info message.
     *
     * @param msg the msg to be sent
     */
    public static void info(final String msg) {
        if (LEVEL >= 3) {
            Log.i(TAG + ":" + INFO, msg);
        }
    }

    /**
     * Sends a warning message.
     *
     * @param msg the msg to be sent
     */
    public static void warning(final String msg) {
        if (LEVEL >= 2) {
            Log.w(TAG + ":" + WARNING, msg);
        }
    }
}
