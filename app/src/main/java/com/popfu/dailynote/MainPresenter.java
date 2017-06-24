package com.popfu.dailynote;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by pengfu on 24/06/2017.
 */

public class MainPresenter extends BasePresenter {


    public void getNotes() {
        List<Note> notes = mNoteDao.queryForAll();
        L.d("所有的notes:" + notes);
    }
}
