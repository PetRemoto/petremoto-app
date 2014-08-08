
package com.thiner.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;

import com.thiner.R;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public final class ThinerUtils {

    private ThinerUtils() {
    }

    private static final String URL = "http://thiner.herokuapp.com/api/user";
    // private static final String URL = "http://192.168.1.244:5000/api/ti";

    /**
     * The Constant mFormat.
     */
    private static final String FORMAT_SHORT = "HH:mm";

    public static void showToast(final Context context, final String message) {
        final Toast makeText = Toast.makeText(context, message, Toast.LENGTH_LONG);
        makeText.setGravity(Gravity.TOP, 0, 0);
        makeText.show();
    }

    public static String getServerURL() {
        return URL;
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    public static String downloadUrl(final String myurl) {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            final URL url = new URL(myurl);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            is = conn.getInputStream();
            isr = new InputStreamReader(is, "UTF-8");
            br = new BufferedReader(isr);

            String line;
            final StringBuffer buf = new StringBuffer();

            while ((line = br.readLine()) != null) {
                buf.append(line);
            }

            is.close();
            isr.close();
            br.close();

            return buf.toString();

        } catch (final IOException e) {
            MyLog.error("Error when downloading url", e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (final IOException e) {
                MyLog.error("Error when downloading url", e);
            }
        }

        return null;
    }

    public static String sendPOST(final String myurl, final String paramns) throws IOException {

        final URL url = new URL(myurl);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        final OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
        osw.write(paramns);
        osw.flush();

        final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),
                "UTF-8"));

        final StringBuffer contentAsString = new StringBuffer();
        String line;

        while ((line = br.readLine()) != null) {
            contentAsString.append(line);
        }

        osw.close();
        br.close();

        return contentAsString.toString();
    }

    public static boolean isConnected(final Activity act) {
        final ConnectivityManager connMgr = (ConnectivityManager) act
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }

        return false;
    }

    public static void showNotConnected(final Context context) {
        Toast.makeText(context, context.getString(R.string.connection_unavailable),
                Toast.LENGTH_LONG).show();
    }

    public static void showCantWithoutConnection(final Context context) {
        Toast.makeText(context, context.getString(R.string.cant_without_connection),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Gets the date time format.
     *
     * @return the date time format
     */
    public static DateTimeFormatter getDateTimeFormat() {
        return DateTimeFormat.forPattern(FORMAT_SHORT);
    }

    public static String formatDouble(final double num) {
        final DecimalFormat df = new DecimalFormat("#,###.00");

        boolean hasFloating = false;

        if (num - (int) num != 0) {
            hasFloating = true;
        }

        if (hasFloating) {
            return df.format(num);
        } else {
            return String.valueOf((int) num);
        }

    }

}
