package com.popfu.dailynote.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.popfu.dailynote.R;
import com.popfu.dailynote.bean.Note;
import com.popfu.dailynote.presenter.AddNotePresenter;
import com.popfu.dailynote.ui.toast.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by pengfu on 26/06/2017.
 */

@EActivity(R.layout.act_add_note)
public class AddNoteActivity extends Activity implements View.OnClickListener {


    AddNotePresenter mPresenter ;

    @ViewById(R.id.edit_text)
    EditText mEditText ;

    @ViewById(R.id.left_button)
    TextView mLeftView ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AddNotePresenter() ;
    }

    @AfterViews
    void afterViews(){
        mLeftView.setText("Back");
        mLeftView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_button:
                String content = mEditText.getEditableText().toString() ;
                if(TextUtils.isEmpty(content)){
                    ToastUtil.show("not content.");
                }else{
                    Note note = new Note(content) ;
                    mPresenter.addNote(note);
                    ToastUtil.show("save content");
                }
                finish();
                break ;
        }
    }
}
