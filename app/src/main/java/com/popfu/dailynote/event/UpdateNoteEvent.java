package com.popfu.dailynote.event;

/**
 * Created by pengfu on 26/06/2017.
 */

public class UpdateNoteEvent {


    public int note_id ;
    public UpdateNoteEvent(int note_id){
        this.note_id = note_id ;
    }
}
