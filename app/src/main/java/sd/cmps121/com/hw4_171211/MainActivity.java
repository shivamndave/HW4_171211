package sd.cmps121.com.hw4_171211;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

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

    // Adapter for the list of news sites, uses the custom NewsElement
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

            // Each list item itself displays a name and url that you would
            // go to if you click on it
            _newsLabelTextView = (TextView) newView.findViewById(R.id.newsLabel);
            _newsUrlTextView = (TextView) newView.findViewById(R.id.newsUrl);
            _newsLabelTextView.setText(siteName);
            _newsUrlTextView.setText(siteUrl);

            // Sets temporary variables for the url and name, which will
            // be used inside the onClick
            final String tempSiteUrl = siteUrl;
            final String tempSiteName = siteName;

            newView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Sets the intent for the ReaderActivity where we pass the site url and name
                    Intent readerActivity = new Intent(MainActivity.this, ReaderActivity.class);
                    readerActivity.putExtra("url", tempSiteUrl);
                    readerActivity.putExtra("name", tempSiteName);
                    startActivity(readerActivity);
                }
            });


            return newView;
        }
    }

    protected MyAdapter _adapter;
    protected ListView _newListView;

    // On Create we initialize the list of news sites
    // (NewsElements) and then load those into an adapter
    // which is then fed into a ListView
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<NewsElement> newsL = new ArrayList<NewsElement>();
        newsL = createNewsList();

        _newListView = (ListView) findViewById(R.id.newsList);
        _adapter = new MyAdapter(this, R.layout.listview_news, newsL);
        _newListView.setAdapter(_adapter);
    }

    // Used to create each NewsElement that is put into the list adapter
    // A news element is made up of a URL and name of the site
    ArrayList<NewsElement> createNewsList() {
        ArrayList<NewsElement> tempL = new ArrayList<NewsElement>();
        NewsElement newsOne = createNewsElement("SFGate", "http://m.sfgate.com");
        NewsElement newsTwo = createNewsElement("The Guardian", "http://www.theguardian.com/us");
        NewsElement newsThree = createNewsElement("Santa Cruz Sentinel", "http://www.santacruzsentinel.com");
        NewsElement newsFour = createNewsElement("ESPN", "http://espn.go.com");
        NewsElement newsFive = createNewsElement("CNN", "http://www.cnn.com");

        tempL.add(newsOne);
        tempL.add(newsTwo);
        tempL.add(newsThree);
        tempL.add(newsFour);
        tempL.add(newsFive);
        return tempL;
    }

    // Actually does the setting of each of the NewsElement variables
    NewsElement createNewsElement(String name, String url) {
        NewsElement tempNE = new NewsElement();
        tempNE.newsLabel = name;
        tempNE.newsUrl = url;
        return tempNE;
    }

}
