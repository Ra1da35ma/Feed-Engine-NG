package com.sparklegrid.radium.feedengineng;

/**
 * Created by Rahman on 3/27/2015.
 */
public class WebSite {
    Integer _id;
    String _title;
    String _link;
    String _atom_link;
    String _description;

    public WebSite() {

    }

    public WebSite(String title, String link, String atom_link, String description) {
        this._title = title;
        this._link = link;
        this._atom_link = atom_link;
        this._description = description;
    }

    //all methods to set
    public void setTitle(String title) {
        this._title = title;
    }

    public void setId(Integer id) {
        this._id = id;
    }

    public void setLink(String link) {
        this._link = link;
    }

    public void setAtomLink(String atom_link) {
        this._atom_link = atom_link;
    }

    public void setDescription(String description) {
        this._description = description;
    }


    //all methods to get values
    public String getLink() {
        return this._link;
    }

    public String getDescription() {
        return this._description;
    }

    public String getTitle() {
       return this._title;
    }

    public String getAtomLink() {
        return this._atom_link;
    }

    public Integer getId() {
        return this._id;
    }
}
