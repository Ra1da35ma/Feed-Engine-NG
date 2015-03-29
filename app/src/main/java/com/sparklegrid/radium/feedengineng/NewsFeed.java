package com.sparklegrid.radium.feedengineng;

import java.util.List;

/**
 * Created by Rahman on 3/27/2015.
 */
public class NewsFeed {
    //below are the XML nodes
    String _title;
    String _atom_link;
    String _link;
    String _description;
    String _language;
    List<NewsItem> _items;

    //here is a constructor method
    public NewsFeed(String title, String description, String link, String atom_link, String language) {
        this._title = title;
        this._description = description;
        this._link = link;
        this._atom_link = atom_link;
        this._language = language;
    }

    public void setItems(List<NewsItem> items) {
        this._items = items;
    }

    //methods to get information
    public List<NewsItem> get_items() {
        return this._items;
    }

    public String get_title() {
        return this._title;
    }

    public String get_atom_link() {
        return this._atom_link;
    }

    public String get_link() {
        return this._link;
    }

    public String get_language() {
        return this._language;
    }

    public String get_description() {
        return this._description;
    }
}
