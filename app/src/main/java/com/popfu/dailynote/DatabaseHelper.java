package com.popfu.dailynote;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by pengfu on 24/06/2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "dailyNote.db";

    private static final int DATABASE_VERSION = 1;

    private Dao<Note, Integer> noteDao = null;
    private RuntimeExceptionDao<Note, Integer> noteRuntimeDao = null;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            L.i(DatabaseHelper.class.getName()+" onCreate");
            TableUtils.createTable(connectionSource, Note.class);
        } catch (SQLException e) {
            L.e(DatabaseHelper.class.getName() + " Can't create database", e);
            throw new RuntimeException(e);
        }

        // here we try inserting data in the on-create as a test
        RuntimeExceptionDao<Note, Integer> dao = getNoteDataDao();
        long millis = System.currentTimeMillis();
        // create some entries in the onCreate
        Note simple = new Note("c1");
        dao.create(simple);
        simple = new Note("c2");
        dao.create(simple);
        L.i(DatabaseHelper.class.getName()+" created new entries in onCreate: " + millis);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public Dao<Note, Integer> getDao() throws SQLException {
        if (noteDao == null) {
            noteDao = getDao(Note.class);
        }
        return noteDao;
    }

    public RuntimeExceptionDao<Note, Integer> getNoteDataDao() {
        if (noteRuntimeDao == null) {
            noteRuntimeDao = getRuntimeExceptionDao(Note.class);
        }
        return noteRuntimeDao;
    }


    @Override
    public void close() {
        super.close();
        noteDao = null;
        noteRuntimeDao = null;
    }
}
