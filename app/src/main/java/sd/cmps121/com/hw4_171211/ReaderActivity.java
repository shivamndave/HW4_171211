package sd.cmps121.com.hw4_171211;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by shivamndave on 5/13/15.
 */
public class ReaderActivity extends Activity {

    static final public String MYPREFS = "myprefs";
    static final public String PREF_URL = "restore_url";
    static final public String WEBPAGE_NOTHING = "about:blank";
    static final public String LOG_TAG = "webview_example";
    public String MY_WEBPAGE;
    public String MY_WEBNAME;

    WebView myWebView;
    TextView _siteNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();


        if (bundle != null) {
            MY_WEBPAGE = bundle.getString("url");
            MY_WEBNAME = bundle.getString("name");
        } else {
            MY_WEBPAGE = "http://www.google.com";
            MY_WEBNAME = "Site Not Loaded";
        }
        setContentView(R.layout.activity_reader);
        myWebView = (WebView) findViewById(R.id.webView1);
        _siteNameView = (TextView) findViewById(R.id.readerSiteName);

        _siteNameView.setText(MY_WEBNAME);

        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        // Binds the Javascript interface
        myWebView.loadUrl(MY_WEBPAGE);

        myWebView.setWebViewClient(new MyWebViewClient());

        Button v = (Button) findViewById(R.id.shareButton);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, myWebView.getUrl());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String parsedUrlHost = Uri.parse(url).getHost();
            String parsedMyWebPageHost = Uri.parse(MY_WEBPAGE).getHost();

            if (parsedUrlHost.equals(parsedMyWebPageHost)) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

    public class JavaScriptInterface {
        Context mContext; // Having the context is useful for lots of things,
        // like accessing preferences.

        /**
         * Instantiate the interface and set the context
         */
        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void myFunction(String args) {
            final String myArgs = args;
            Log.i(LOG_TAG, "I am in the javascript call.");
            runOnUiThread(new Runnable() {
                public void run() {
                    Button v = (Button) findViewById(R.id.shareButton);
                }
            });

        }

    }

    // Source: http://developer.android.com/guide/webapps/webview.html#HandlingNavigation
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Checks if the back button and if their is history
        // in the webview, if their is, then it will go to a
        // previous webview page
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If their is no history, it will go to the default page
        // This allows us to get back to the MainActivity
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPause() {

        Method pause = null;
        try {
            pause = WebView.class.getMethod("onPause");
        } catch (SecurityException e) {
            // Nothing
        } catch (NoSuchMethodException e) {
            // Nothing
        }
        if (pause != null) {
            try {
                pause.invoke(myWebView);
            } catch (InvocationTargetException e) {
            } catch (IllegalAccessException e) {
            }
        } else {
            // No such method.  Stores the current URL.
            String suspendUrl = myWebView.getUrl();
            SharedPreferences settings = getSharedPreferences(MYPREFS, 0);
            SharedPreferences.Editor ed = settings.edit();
            ed.putString(PREF_URL, suspendUrl);
            ed.commit();
            // And loads a URL without any processing.
            myWebView.clearView();
            myWebView.loadUrl(WEBPAGE_NOTHING);
        }
        super.onPause();
    }
}

