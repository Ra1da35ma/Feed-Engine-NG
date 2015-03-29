package com.sparklegrid.radium.feedengineng;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Rahman on 3/29/2015.
 */
public class AddNewSiteActivity extends Activity {

    Button btnSubmit, btnCancel;
    EditText txtUrl;
    TextView lblMessage;

    NewsParser newsParser = new NewsParser();
    NewsFeed newsFeed;

    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_site);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        txtUrl = (EditText) findViewById(R.id.txtUrl);
        lblMessage = (TextView) findViewById(R.id.lblMessage);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = txtUrl.getText().toString();

                Log.d("URL Length", " " + url.length());

                if (url.length() > 0) {
                    lblMessage.setText("");
                    String urlPattern = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
                    if (url.matches(urlPattern)) {


                        new loadNewsFeed().execute(url);
                    } else {
                        lblMessage.setText("please enter a valid url. we cant get the RSS feed");
                    }
                } else {
                    lblMessage.setText("please enter a web url. You've not entered any");
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

        class loadNewsFeed extends AsyncTask<String, String, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(AddNewSiteActivity.this);
                pDialog.setMessage("Fetching News Information...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected String doInBackground(String... args) {
                String url = args[0];
                newsFeed = newsParser.getNewsFeed(url);
                Log.d("newsFeed", " " + newsFeed);

                if (newsFeed != null) {
                    Log.e("NEWS URL", newsFeed.get_title() + " " + newsFeed.get_link() + " " +
                                newsFeed.get_description() + " " + newsFeed.get_language());

                    NewsDatabaseHandler newsDb = new NewsDatabaseHandler(getApplicationContext());

                    WebSite site = new WebSite(newsFeed.get_title(), newsFeed.get_link(), newsFeed.get_atom_link(), newsFeed.get_description());
                    //WebSite site = new WebSite(newsFeed.get_atom_link(),newsFeed.get_description(), newsFeed.get_title(), newsFeed.get_link());


                    newsDb.addSite(site);

                    Intent i = getIntent();
                    setResult(100, i);
                    finish();
                } else {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            lblMessage.setText("News URL not found, please check the URL and try again");
                        }
                    });
                }
                return null;
            }

            @Override
            protected void onPostExecute(String args) {
                pDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (newsFeed != null) {
                            //lblMessage.setText("URL is valid but ..maybe network error");
                        }
                    }
                });
            }
        }
    }





