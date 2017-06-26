package com.popfu.dailynote.event;

import com.popfu.dailynote.bean.Note;

/**
 * Created by pengfu on 26/06/2017.
 */

public class AddNoteEvent {


    public Note note ;

    public AddNoteEvent(Note note) {
        this.note = note ;
    }
}
