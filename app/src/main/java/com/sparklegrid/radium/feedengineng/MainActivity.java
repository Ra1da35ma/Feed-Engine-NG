package com.sparklegrid.radium.feedengineng;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends Activity {

    private ProgressDialog pDialog;

    ArrayList<HashMap<String, String>> newsFeedList;

    NewsParser newsParser = new NewsParser();

    NewsFeed newsFeed;

    ImageButton btnAddSite;

    String[] sqliteIds;

    public static String TAG_ID = "id";
    public static String TAG_TITLE = "title";
    public static String TAG_LINK = "link";

    ListView lv;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_list);

        btnAddSite = (ImageButton) findViewById(R.id.btnAddSite);

        newsFeedList = new ArrayList<HashMap<String, String>>();

        new loadStoreSites().execute();

        lv = (ListView) findViewById(R.id.list);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sqlite_id = ((TextView) view.findViewById(R.id.sqlite_id)).getText().toString();
                Intent in = new Intent(getApplicationContext(), ListNewsItemsActivity.class);
                in.putExtra(TAG_ID, sqlite_id);
                startActivity(in);
            }
        });

        btnAddSite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                Intent i = new Intent(getApplicationContext(), AddNewSiteActivity.class);
                startActivityForResult(i, 100);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {
            Intent intent = getIntent();
            finish();

            startActivity(intent);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.list) {
            menu.setHeaderTitle("Delete");
            menu.add(Menu.NONE, 0, 0, "Delete Feed");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();

        if(menuItemIndex == 0) {
            NewsDatabaseHandler newsDb = new NewsDatabaseHandler(getApplicationContext());
            WebSite site = new WebSite();
            site.setId(Integer.parseInt(sqliteIds[info.position]));
            newsDb.deleteSite(site);

            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

        return true;
    }

    class loadStoreSites extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading websites...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            runOnUiThread(new Runnable() {
                //@Override
                public void run() {
                    NewsDatabaseHandler newsDb = new NewsDatabaseHandler(getApplicationContext());

                    List<WebSite> siteList = newsDb.getAllSites();

                    sqliteIds = new String[siteList.size()];

                    for (int i = 0; i < siteList.size(); i++) {
                        WebSite s = siteList.get(i);

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(TAG_ID, s.getId().toString());
                        map.put(TAG_TITLE, s.getTitle());
                        map.put(TAG_LINK, s.getLink());

                        newsFeedList.add(map);

                        sqliteIds[i] = s.getId().toString();
                    }

                    ListAdapter adapter = new SimpleAdapter(MainActivity.this,
                            newsFeedList, R.layout.site_list_row,
                            new String[] { TAG_ID, TAG_TITLE, TAG_LINK },
                            new int[] {R.id.sqlite_id, R.id.title, R.id.link} );
                    lv.setAdapter(adapter);
                    registerForContextMenu(lv);
                }
            });
            return null;
        }

        //@Override
        protected void onPostExecute(String args) {
            pDialog.dismiss();
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
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
