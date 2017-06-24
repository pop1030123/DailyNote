package com.popfu.dailynote;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by pengfu on 24/06/2017.
 */

@DatabaseTable(tableName = "note")
public class Note {


    public Note(){}

    public Note(String content){
        this.content = content ;
    }


    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField
    private String title;
    @DatabaseField
    private String content;


    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
