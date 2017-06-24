package com.popfu.dailynote;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;

/**
 * Created by pengfu on 24/06/2017.
 */

public class BasePresenter {


    protected DatabaseHelper databaseHelper = null;


    protected RuntimeExceptionDao<Note, Integer> mNoteDao ;


    public BasePresenter(){
        databaseHelper = getHelper() ;
        mNoteDao = databaseHelper.getNoteDataDao() ;

    }

    /**
     * You'll need this in your class to get the helper from the manager once per class.
     */
    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(DNApp.getAppContext(), DatabaseHelper.class);
        }
        return databaseHelper;
    }
}
