package com.popfu.dailynote.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Slide;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.popfu.dailynote.event.AddNoteEvent;
import com.popfu.dailynote.R;
import com.popfu.dailynote.bean.Note;
import com.popfu.dailynote.event.DeleteNoteEvent;
import com.popfu.dailynote.event.UpdateNoteEvent;
import com.popfu.dailynote.presenter.EditNotePresenter;
import com.popfu.dailynote.ui.toast.ToastUtil;
import com.popfu.dailynote.util.L;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

/**
 * Created by pengfu on 26/06/2017.
 */

@EActivity(R.layout.act_edit_note)
public class EditNoteActivity extends Activity implements View.OnClickListener, TextWatcher {


    public static final String KEY_EDIT = "key_edit" ;
    public static final String KEY_NOTE_ID = "key_note_id" ;

    EditNotePresenter mPresenter ;
    private boolean isEdit ;
    private int mNoteId = -1 ;
    private Note mOldNote ;

    @ViewById(R.id.edit_text)
    EditText mEditText ;
    @ViewById(R.id.count)
    TextView mCountView ;

    @ViewById(R.id.left_button)
    TextView mLeftView ;

    @ViewById(R.id.right_button)
    TextView mRightView ;

    SlideBackLayout mSlideBackLayout ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSlideBackLayout = new SlideBackLayout(this) ;
        mSlideBackLayout.bind();
        mPresenter = new EditNotePresenter() ;
        isEdit = getIntent().getBooleanExtra(KEY_EDIT ,false) ;
        if(isEdit)
        {
            mNoteId = getIntent().getIntExtra(KEY_NOTE_ID , -1) ;
        }
    }

    @AfterViews
    void afterViews(){
        mLeftView.setText("Back");
        mLeftView.setOnClickListener(this);
        mRightView.setVisibility(View.INVISIBLE);
        mEditText.addTextChangedListener(this);
        if(isEdit && mNoteId > 0){
            mOldNote = mPresenter.getNote(mNoteId) ;
            mEditText.setText(mOldNote.getContent());
        }
        mEditText.setSelection(mEditText.getText().length());
        updateCount() ;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        onPageFinished() ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_button:
                onPageFinished() ;
                break ;
        }
    }

    private void onPageFinished(){
        String content = mEditText.getEditableText().toString() ;
        if(TextUtils.isEmpty(content)){
            if(isEdit){
                int count = mPresenter.deleteNote(mNoteId) ;
                EventBus.getDefault().post(new DeleteNoteEvent(mNoteId));
                L.d("删除note:"+mNoteId+"/"+count);
            }else{
                ToastUtil.show("No content.");
            }
        }else{
            if(isEdit){
                // 修改内容
                if(content.equals(mOldNote.getContent())){
                    // 无内容修改
                    ToastUtil.show("No content changed.");
                }else{
                    // 内容已经修改，存库，提示
                    mOldNote.setContent(content);
                    mPresenter.updateNote(mOldNote) ;
                    EventBus.getDefault().post(new UpdateNoteEvent(mOldNote.getId()));
                }
            }else{
                Note note = new Note(content) ;
                mPresenter.addNote(note);
                EventBus.getDefault().post(new AddNoteEvent(note));
                ToastUtil.show("Content saved.");
            }
        }
        finish();
        overridePendingTransition(R.anim.slide_in_left,0) ;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        updateCount() ;
    }


    private void updateCount(){
        mCountView.setText(""+mEditText.getText().length());
    }
}
