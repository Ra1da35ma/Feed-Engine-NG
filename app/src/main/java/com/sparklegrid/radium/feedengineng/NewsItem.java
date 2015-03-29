package com.sparklegrid.radium.feedengineng;

/**
 * Created by Rahman on 3/27/2015.
 */
public class NewsItem {
    String _title;
    String _description;
    String _link;
    String _pubdate;
    String _guid;

    public NewsItem() {

    }

    public NewsItem(String title, String description, String link, String pubdate, String guid) {
        this._description = description;
        this._guid = guid;
        this._link = link;
        this._pubdate = pubdate;
        this._title = title;
    }



    //all methods to set
    public void setDescription(String description) {
        this._description = description;
    }

    public void setGuid(String guid) {
        this._guid = guid;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    public void setLink(String link) {
        this._link = link;
    }

    public void setPubdate(String pubdate) {
        this._pubdate = pubdate;
    }



    //all methods to get
    public String getGuid() {
        return this._guid;
    }

    public String getPubdate() {
        return this._pubdate;
    }

    public String getTitle() {
        return this._title;
    }

    public String getDescription() {
        return this._description;
    }

    public String getLink() {
        return this._link;
    }
}

