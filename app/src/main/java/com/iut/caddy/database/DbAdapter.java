/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.iut.caddy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class DbAdapter {

    private static final String TAG = "DbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    public static final String KEY_NAME = "name";
    public static final String KEY_LABEL = "label";

    private static final String DATABASE_NAME = "caddy";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("create table products (_id integer primary key autoincrement, label text);");
            db.execSQL("create table lists (_id integer primary key autoincrement, name text);");
            db.execSQL("create table productList (productId integer, listId integer, foreign key (productId) references products(_id), foreign key (listId) references lists(_id))");
            db.execSQL("insert into lists(name) values (\"Liste 1\");");
            db.execSQL("insert into lists(name) values (\"Liste 2\");");
            db.execSQL("insert into products(label) values ('Bananes')");
            db.execSQL("insert into products(label) values ('Fraises')");
            db.execSQL("insert into products(label) values ('Melons')");
            db.execSQL("insert into products(label) values ('Salades')");
            db.execSQL("insert into productList(productId, listId) values (1,1)");
            db.execSQL("insert into productList(productId, listId) values (2,1)");
            db.execSQL("insert into productList(productId, listId) values (3,1)");
            db.execSQL("insert into productList(productId, listId) values (4,2)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS products");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public DbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public Cursor fetchAllLists() {
        return mDb.query("lists", new String[] {"_id", "name"}, null, null, null, null, null);
    }

    public void newList(String _name) {
        mDb.execSQL("insert into lists (name) values ('"+_name+"');");
    }

    public Cursor findProductsByListId(int listId) {
        return mDb.rawQuery("select _id, label from products as p inner join productList as pl on pl.productId = p._id where pl.listId = ?;", new String[]{String.valueOf(listId)});
    }

    public Cursor fetchAllProducts() {
        return mDb.query("products", new String[] {"_id", "label"}, null, null, null, null, null);
    }

    public void addItemToList(int productId, int listId) {
        mDb.execSQL("insert into productList(productId, listId) values ('"+productId+"', '"+listId+"')");
    }

    public Cursor getListIdFromName(String listName) {
        return mDb.rawQuery("select _id from lists where name like ?", new String[]{String.valueOf(listName)});
    }
}
