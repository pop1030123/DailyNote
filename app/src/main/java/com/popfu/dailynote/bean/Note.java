package com.popfu.dailynote.bean;

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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
