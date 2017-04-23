package br.edu.ufam.icomp.lopespimentel.restbdmap.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufam.icomp.lopespimentel.restbdmap.model.Country;

/**
 * Created by micael on 4/16/17.
 */

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "RestBdMap.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "COUNTRIES";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    Country.NAME + " TEXT PRIMARY KEY," +
                    Country.CAPITAL + " TEXT," +
                    Country.LATLNG + "0" + " DOUBLE," +
                    Country.LATLNG + "1" + " DOUBLE" +  " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public boolean hasCountry(String name) {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Country.NAME,
                Country.CAPITAL,
                Country.LATLNG+"0",
                Country.LATLNG+"1"
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = Country.NAME + " = ?";
        String[] selectionArgs = {name};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                Country.NAME + " DESC";

        Cursor c = db.query(
                TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return c.getCount() > 0;
    }

    public List<Country> getCountries() {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Country.NAME,
                Country.CAPITAL,
                Country.LATLNG+"0",
                Country.LATLNG+"1"
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                Country.NAME + " ASC";

        Cursor c = db.query(
                TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        ArrayList<Country> list = new ArrayList<>();
        if(c.getCount() > 0) {
            c.moveToFirst();
            do {
                String name = c.getString(c.getColumnIndex(Country.NAME));
                String capital = c.getString(c.getColumnIndex(Country.CAPITAL));
                List<Double> latlng = new ArrayList<>();
                latlng.add(c.getDouble(c.getColumnIndex(Country.LATLNG + "0")));
                latlng.add(c.getDouble(c.getColumnIndex(Country.LATLNG + "1")));

                Country country = new Country(name, capital, latlng );

                list.add(country);

            } while (c.moveToNext());
        }

        return list;
    }

    public void insertCountry(Country country) {
        // Checa se o pais ja existe para evitar dados duplicados
        if(hasCountry(country.getName()))
            return;

        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Country.NAME, country.getName());
        values.put(Country.CAPITAL, country.getCapital());
        try {
            values.put(Country.LATLNG + "0", country.getLatlng().get(0));
            values.put(Country.LATLNG + "1", country.getLatlng().get(1));
        } catch (IndexOutOfBoundsException e) {
            System.err.println("IndexOutOfBoundsException: " + e.getMessage());
        }

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME, null, values);
    }
}
