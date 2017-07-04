package com.popfu.dailynote.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.Arrays;

import de.greenrobot.event.EventBus;
import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconPage;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.EmojiconsView;
import io.github.rockerhieu.emojicon.emoji.Emojicon;

/**
 * Created by pengfu on 26/06/2017.
 */

@EActivity(R.layout.act_edit_note)
public class EditNoteActivity extends FragmentActivity implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsView.OnEmojiconBackspaceClickedListener , View.OnClickListener, TextWatcher {


    public static final String KEY_EDIT = "key_edit" ;
    public static final String KEY_NOTE_ID = "key_note_id" ;

    EditNotePresenter mPresenter ;
    private boolean isEdit ;
    private int mNoteId = -1 ;
    private Note mOldNote ;

    @ViewById(R.id.edit_text)
    EmojiconEditText mEditText ;
    @ViewById(R.id.count)
    TextView mCountView ;

    @ViewById(R.id.left_button)
    TextView mLeftView ;

    @ViewById(R.id.right_button)
    TextView mRightView ;

    @ViewById(R.id.emoji_icon)
    ImageView mEmojiIcon ;
    @ViewById(R.id.emojicons_view)
    EmojiconsView mEmojiconsView ;

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
        mEmojiIcon.setOnClickListener(this);
        mRightView.setVisibility(View.INVISIBLE);
        mEditText.addTextChangedListener(this);

        mEmojiconsView.setPages(Arrays.asList(
                new EmojiconPage(Emojicon.TYPE_PEOPLE, null, false, R.drawable.ic_emoji_people_light),
                new EmojiconPage(Emojicon.TYPE_NATURE, null, false, R.drawable.ic_emoji_nature_light),
                new EmojiconPage(Emojicon.TYPE_OBJECTS, null, false, R.drawable.ic_emoji_objects_light),
                new EmojiconPage(Emojicon.TYPE_PLACES, null, false, R.drawable.ic_emoji_places_light),
                new EmojiconPage(Emojicon.TYPE_SYMBOLS, null, false, R.drawable.ic_emoji_symbols_light)
        ) ,this ,this);

        if(isEdit && mNoteId > 0){
            mOldNote = mPresenter.getNote(mNoteId) ;
            mEditText.setText(mOldNote.getContent());
        }
        mEditText.setSelection(mEditText.getText().length());
        updateCount() ;
    }

    @Override
    public void onBackPressed() {
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

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        L.d("onEmojiconClicked:"+emojicon);
        EmojiconsFragment.input(mEditText, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        L.d("onEmojiconBackspaceClicked:"+v);
        EmojiconsFragment.backspace(mEditText);
    }
}
