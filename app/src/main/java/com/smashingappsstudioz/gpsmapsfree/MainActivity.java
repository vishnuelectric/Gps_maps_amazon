package com.smashingappsstudioz.gpsmapsfree;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.format.Time;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity
        extends Activity
        implements LocationListener {
    public static final String prefs = "pref1";
    int b;
    int i = 0;
    LocationManager locationManager;
    long millis;
    long millis1;
    SharedPreferences preferences;
    Time time;
    Time time1;
    WebView webview;

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main);
        try {
            this.locationManager = ((LocationManager) getSystemService(LOCATION_SERVICE));

            this.locationManager.requestLocationUpdates("network", 0L, 0.0F, this);
            new AlertDialog.Builder(this).setTitle("Disclaimer").setMessage("On  Devices/tablets/Phones which do not support GPS location, the location icon wont work (Rest of the features works as normal), In some case it may return a approx location based on wifi.  ").setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                }
            }).show();
           // getActionBar().hide();
            this.i = PreferenceManager.getDefaultSharedPreferences(this).getInt("opentimes", 0);
            this.i += 1;
            PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("opentimes", this.i).commit();
            if ((this.i > 3) && (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("rated", false))) {
                rate();
            }
           webview = ((WebView)findViewById(R.id.webview));
            webview.getSettings().setJavaScriptEnabled(true);
            if (Build.VERSION.SDK_INT >= 19)
            {

                WebView.setWebContentsDebuggingEnabled(true);
            }
            webview.getSettings().setDomStorageEnabled(true);
            if (Build.VERSION.SDK_INT >= 16)
            {
                this.webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
                this.webview.getSettings().setAllowFileAccessFromFileURLs(true);
                this.webview.getSettings().setAllowContentAccess(true);
            }
            this.webview.addJavascriptInterface(new MyInterface(), "myInterface");
            this.webview.loadUrl("file:///android_asset/index.html");
            this.webview.setWebChromeClient(new WebChromeClient() {
                public void onGeolocationPermissionsShowPrompt(String paramAnonymousString, GeolocationPermissions.Callback paramAnonymousCallback) {
                    System.out.println("gelocation");
                    paramAnonymousCallback.invoke(paramAnonymousString, true, true);
                }
            });
            this.webview.setWebViewClient(new HelloWebViewClient());
            this.webview.setScrollbarFadingEnabled(true);



            return;
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
    }



    private void rate()
    {
        new AlertDialog.Builder(this).setMessage("Please rate this app 5 stars on Amazon App Store, if you like it").setTitle("Like this app").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("amzn://apps/android?p=com.smashingappsstudioz.gpsmapsfree"));
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this.getApplicationContext()).edit().putBoolean("rated", true).commit();
                MainActivity.this.startActivity(intent);
            }
        }).show();
    }

    private void share()
    {
        Intent localIntent = new Intent("android.intent.action.SEND");
        localIntent.setType("text/plain");
        localIntent.putExtra("android.intent.extra.TEXT", "Explore the world around you using gps maps check it out. http://www.amazon.com/gp/mas/dl/android?p=com.smashingappsstudioz.gpsmapsfree");
        startActivity(localIntent);
    }





    public void onDestroy()
    {
        super.onDestroy();

    }

    public void onLocationChanged(Location paramLocation) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onPause()
    {
       super.onPause();

    }

    public void onProviderDisabled(String paramString)
    {
        System.out.println(paramString + " disabled provider");
    }

    public void onProviderEnabled(String paramString)
    {
        System.out.println(paramString + " enabled provider");
    }

    public void onResume()
    {
        super.onResume();

    }

    public void onStart()
    {
        super.onStart();

    }



    public void onStop()
    {
        super.onStop();

    }

    public void showMessage(String paramString)
    {
        Toast.makeText(this, paramString, Toast.LENGTH_SHORT).show();
    }

    public static class HelloWebViewClient
            extends WebViewClient
    {
        public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString)
        {
            paramWebView.loadUrl(paramString);
            return true;
        }
    }

    private class MyInterface
    {
        private MyInterface() {}

        @JavascriptInterface
        public void getStreetView()
        {
            Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("amzn://apps/android?p=com.vishnu.streetview"));
            MainActivity.this.startActivity(localIntent);
        }

        @JavascriptInterface
        public void rate()
        {
            MainActivity.this.rate();
        }

        @JavascriptInterface
        public void share()
        {
            MainActivity.this.share();
        }

    }
}
