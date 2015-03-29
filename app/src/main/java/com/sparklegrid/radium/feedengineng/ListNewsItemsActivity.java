package com.sparklegrid.radium.feedengineng;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class ListNewsItemsActivity extends ListActivity {

    private ProgressDialog pDialog;

    ArrayList<HashMap<String, String>> newsItemList = new ArrayList<HashMap<String, String>>();

    NewsParser newsParser = new NewsParser();

    List<NewsItem> newsItems = new ArrayList<NewsItem>();

    NewsFeed newsFeed;

    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_DESCRIPTION = "description";
    private static String TAG_PUBDATE = "pubdate";
    private static String TAG_GUID = "guid";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_item_list);

        Intent i = getIntent();
        Integer site_id = Integer.parseInt(i.getStringExtra("id"));

        NewsDatabaseHandler newsDb = new NewsDatabaseHandler(getApplicationContext());

        WebSite site = newsDb.getSite(site_id);
        String atom_link = site.getAtomLink();

        new loadNewsFeedItems().execute(atom_link);

        ListView lv = getListView();

        //ListView lv = (ListView) findViewById(R.id.list);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(getApplicationContext(), DisplayWebPageActivity.class);

                String page_url = ((TextView) view.findViewById(R.id.page_url)).getText().toString();
                Toast.makeText(getApplicationContext(), page_url, Toast.LENGTH_SHORT).show();
                in.putExtra("page_url", page_url);
                startActivity(in);
            }
        });

    }

    class loadNewsFeedItems extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListNewsItemsActivity.this);
            pDialog.setMessage("Loading websites...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String news_url = args[0];

            newsItems = newsParser.getNewsFeedItems(news_url);
            for (NewsItem item : newsItems) {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_TITLE, item.getTitle());
                map.put(TAG_LINK, item.getLink());
                map.put(TAG_PUBDATE, item.getPubdate());

                String description = item.getDescription();

                if (description.length() > 100) {
                    description = description.substring(0, 97) + ". .";
                }
                map.put(TAG_DESCRIPTION, description);

                newsItemList.add(map);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(ListNewsItemsActivity.this,
                            newsItemList, R.layout.news_item_list_row,
                            new String[] {TAG_LINK, TAG_TITLE, TAG_PUBDATE, TAG_DESCRIPTION},
                            new int[] {R.id.page_url, R.id.title, R.id.pub_date, R.id.link} );

                    setListAdapter(adapter);
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String args) {
            pDialog.dismiss();
        }
    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_list_news_items, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
