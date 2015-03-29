package com.sparklegrid.radium.feedengineng;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Rahman on 3/27/2015.
 */
public class NewsParser {
    private static String TAG_CHANNEL = "channel";
    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_DESCRIPTION = "description";
    private static String TAG_LANGUAGE = "language";
    private static String TAG_ITEM = "item";
    private static String TAG_PUBDATE = "pubdate";
    private static String TAG_GUID = "guid";

    public NewsParser() {

    }

    public NewsFeed getNewsFeed(String url) {
        NewsFeed newsFeed = null;
        String news_feed_xml = null;

        //here is getting rss[news] link from html source code
        String news_url = this.getNewsLinkFromURL(url);

        //here is to check if the rss-link is found
        if (news_url != null) {
            //go ahead and get it
            news_feed_xml = this.getXmlFromUrl(news_url);
            //now check if its fetched or not
            if (news_feed_xml != null) {
                //just parse the xml
                try {
                    Document doc = this.getDomElement(news_feed_xml);
                    NodeList nodeList = doc.getElementsByTagName(TAG_CHANNEL);
                    Element e = (Element) nodeList.item(0);

                    //news node
                    String title = this.getValue(e, TAG_TITLE);
                    String link = this.getValue(e, TAG_LINK);
                    String description = this.getValue(e, TAG_DESCRIPTION);
                    String language = this.getValue(e, TAG_LANGUAGE);


                    //creating a new news feed
                    newsFeed = new NewsFeed(title, description, link, news_url, language);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

            }

        } else {

        }
        return newsFeed;

    }

    public List<NewsItem> getNewsFeedItems(String news_url) {

        List<NewsItem> itemsList = new ArrayList<NewsItem>();
        String news_feed_xml;

        //here we get the xml from the url
        news_feed_xml = this.getXmlFromUrl(news_url);

        //confirm if its gotten
        if (news_feed_xml != null) {
            //parse the xml
            try {
                Document doc = this.getDomElement(news_feed_xml);
                NodeList nodeList = doc.getElementsByTagName(TAG_CHANNEL);
                Element e = (Element) nodeList.item(0);

                //get items array
                NodeList items = e.getElementsByTagName(TAG_ITEM);

                //loop through each item
                for (int i = 0; i < items.getLength(); i++) {
                    Element e1 = (Element) items.item(i);

                    String title = this.getValue(e1, TAG_TITLE);
                    String link = this.getValue(e1, TAG_LINK);
                    String description = this.getValue(e1, TAG_DESCRIPTION);
                    String pubdate = this.getValue(e1, TAG_PUBDATE);
                    String guid = this.getValue(e1, TAG_GUID);

                    NewsItem newsItem = new NewsItem(title, description, link, pubdate, guid);

                    //add item to the list
                    itemsList.add(newsItem);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return itemsList;
    }

    public String getNewsLinkFromURL(String url) {
        String news_url = null;

        try {
            org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("link[type=application/rss+xml]");

            Log.d("No of RSS links found", " " + links.size());

            //check if the url is found
            if (links.size() > 0) {
                news_url = links.get(0).attr("href").toString();

            } else {
                Elements links1 = doc.select("link[type=application/atom+xml]");
                if (links1.size() > 0) {
                    news_url = links1.get(0).attr("href").toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return news_url;
    }


    public String getXmlFromUrl(String url) {
        String xml = null;

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return xml;
    }

    public Document getDomElement(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = (Document) db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return  null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }

        return doc;

    }

    public final String getElementValue(Node elem) {
        Node child;

        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE || (child.getNodeType() == Node.CDATA_SECTION_NODE)) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

}





















