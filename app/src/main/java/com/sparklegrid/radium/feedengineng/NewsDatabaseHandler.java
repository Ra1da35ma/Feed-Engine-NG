
package com.sparklegrid.radium.feedengineng;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahman on 3/28/2015.
 */
public class NewsDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "newsReader";
    private static final String TABLE_NEWS = "websites";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LINK = "link";
    private static final String KEY_ATOM_LINK = "atom_link";
    private static final String KEY_DESCRIPTION = "description";

    public NewsDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NEWS_TABLE = "CREATE TABLE " + TABLE_NEWS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," + KEY_LINK
                + " TEXT," + KEY_ATOM_LINK + " TEXT," + KEY_DESCRIPTION
                + " TEXT" + " )";
        db.execSQL(CREATE_NEWS_TABLE);

    }

    //upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);

        onCreate(db);
    }



    public  void addSite(WebSite site) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, site.getTitle());
        values.put(KEY_LINK, site.getLink());
        values.put(KEY_ATOM_LINK, site.getAtomLink());
        values.put(KEY_DESCRIPTION, site.getDescription());

        if (!isSiteExists(db, site.getAtomLink())) {
            db.insert(TABLE_NEWS, null, values);
            db.close();
        } else {
            updateSite(site);
            db.close();
        }
    }

    public List<WebSite> getAllSites () {
        List<WebSite> siteList = new ArrayList<WebSite>();

        String selectQuery = "SELECT * FROM " + TABLE_NEWS
                + " ORDER BY id DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                WebSite site = new WebSite();
                site.setId(Integer.parseInt(cursor.getString(0)));
                site.setTitle(cursor.getString(1));
                site.setLink(cursor.getString(2));
                site.setAtomLink(cursor.getString(3));
                site.setDescription(cursor.getString((4)));

                siteList.add(site);

            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();

        return siteList;
    }

    public int updateSite(WebSite site) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, site.getTitle());
        values.put(KEY_LINK, site.getLink());
        values.put(KEY_ATOM_LINK, site.getAtomLink());
        values.put(KEY_DESCRIPTION, site.getDescription());

        int update = db.update(TABLE_NEWS, values, KEY_ATOM_LINK + " = ?",
                new String[] {String.valueOf(site.getAtomLink())});
        db.close();
        return update;

    }

    public WebSite getSite(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NEWS,
                new String[] {KEY_ID, KEY_TITLE, KEY_LINK, KEY_ATOM_LINK, KEY_DESCRIPTION },
                KEY_ID + "=?",
                new String[] {String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        WebSite site = new WebSite(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

        site.setId(Integer.parseInt(cursor.getString(0)));
        site.setTitle(cursor.getString(1));
        site.setLink(cursor.getString(2));
        site.setAtomLink(cursor.getString(3));
        site.setDescription(cursor.getString(4));

        cursor.close();
        db.close();
        return site;
    }

    public void deleteSite(WebSite site) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NEWS, KEY_ID + " = ?", new String[] {String.valueOf(site.getId())});
        db.close();
    }

    public boolean isSiteExists(SQLiteDatabase db, String atom_link) {

        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_NEWS
                + " WHERE atom_link = '" + atom_link + "'", new String[] {} );
        boolean exists = (cursor.getCount() > 0);
        return exists;
    }

}

