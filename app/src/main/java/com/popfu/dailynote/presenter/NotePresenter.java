package com.popfu.dailynote.presenter;

import com.popfu.dailynote.bean.Note;
import com.popfu.dailynote.util.L;

/**
 * Created by pengfu on 26/06/2017.
 */

public class NotePresenter extends BasePresenter {


    public void addNote(Note note){
        int count = mNoteDao.create(note) ;
        L.d("添加Note: count:"+count+note);
    }


    public Note getNote(int note_id){
        return mNoteDao.queryForId(note_id) ;
    }

    public int deleteNote(int note_id){
        int count = mNoteDao.deleteById(note_id) ;
        L.d("删除Note:"+count+":id:"+note_id);
        return count ;
    }

    public int updateNote(Note note){
        int count =  mNoteDao.update(note) ;
        L.d("修改Note:"+note);
        return count ;
    }

}
