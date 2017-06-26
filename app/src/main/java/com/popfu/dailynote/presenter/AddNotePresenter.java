package com.popfu.dailynote.presenter;

import com.popfu.dailynote.bean.Note;
import com.popfu.dailynote.util.L;

/**
 * Created by pengfu on 26/06/2017.
 */

public class AddNotePresenter extends BasePresenter {





    public void addNote(Note note){
        int count = mNoteDao.create(note) ;
        L.d("add note: count:"+count);
    }

}
