package sd.cmps121.com.hw4_171211;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shivamndave on 5/13/15.
 */
public class MainActivity extends Activity {

    private class NewsElement {
        NewsElement() {
        }

        public String newsLabel;
        public String newsUrl;

    }

    private class MyAdapter extends ArrayAdapter<NewsElement> {

        int resource;
        Context context;
        TextView _newsLabelTextView;
        TextView _newsUrlTextView;

        ArrayList<NewsElement> _items = new ArrayList<NewsElement>();

        public MyAdapter(Context _context, int _resource, ArrayList<NewsElement> items) {
            super(_context, _resource, items);
            resource = _resource;
            context = _context;
            this.context = _context;
            _items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout newView;

            String siteName = _items.get(position).newsLabel;
            String siteUrl = _items.get(position).newsUrl;

            // Inflate a new view if necessary.
            if (convertView == null) {
                newView = new LinearLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
                vi.inflate(resource, newView, true);
            } else {
                newView = (LinearLayout) convertView;
            }

            _newsLabelTextView = (TextView) newView.findViewById(R.id.newsLabel);
            _newsUrlTextView = (TextView) newView.findViewById(R.id.newsUrl);
            _newsLabelTextView.setText(siteName);
            _newsUrlTextView.setText(siteUrl);

            final String tempSiteUrl = siteUrl;

            newView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent readerActivity = new Intent(MainActivity.this, ReaderActivity.class);
                    readerActivity.putExtra("url", tempSiteUrl);
                    startActivity(readerActivity);
                }
            });


            return newView;
        }
    }

    protected MyAdapter _adapter;
    protected ListView _newListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<NewsElement> newsL = new ArrayList<NewsElement>();
        newsL = createNewsList();

        _newListView = (ListView) findViewById(R.id.newsList);
        _adapter = new MyAdapter(this, R.layout.listview_news, newsL);
        _newListView.setAdapter(_adapter);
    }

    ArrayList<NewsElement> createNewsList() {
        ArrayList<NewsElement> tempL = new ArrayList<NewsElement>();
        NewsElement newsOne = createNewsElement("SFGate", "http://m.sfgate.com");
        NewsElement newsTwo = createNewsElement("BBC News", "http://m.bbc.com");
        NewsElement newsThree = createNewsElement("Santa Cruz Sentinel", "http://santacruzsentinel.com");
        tempL.add(newsOne);
        tempL.add(newsTwo);
        tempL.add(newsThree);
        return tempL;
    }

    NewsElement createNewsElement(String name, String url) {
        NewsElement tempNE = new NewsElement();
        tempNE.newsLabel = name;
        tempNE.newsUrl = url;
        return tempNE;
    }

}
