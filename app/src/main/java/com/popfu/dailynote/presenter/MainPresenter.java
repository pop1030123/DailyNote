package com.popfu.dailynote.presenter;

import com.popfu.dailynote.bean.Note;
import com.popfu.dailynote.presenter.BasePresenter;
import com.popfu.dailynote.util.L;

import java.util.List;

/**
 * Created by pengfu on 24/06/2017.
 */

public class MainPresenter extends NotePresenter {


    public List<Note> getNotes() {
        List<Note> notes = mNoteDao.queryForAll();
        L.d("所有的notes:" + notes);
        return notes;
    }




}
