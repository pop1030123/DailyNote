package com.popfu.dailynote.ui;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.lang.reflect.Field;

import de.greenrobot.event.EventBus;
import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.emoji.Emojicon;

import static android.view.View.GONE;

/**
 * Created by pengfu on 26/06/2017.
 */

@EActivity(R.layout.act_edit_note)
public class EditNoteActivity extends FragmentActivity implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener , View.OnClickListener, TextWatcher {


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

    @ViewById(R.id.emojicons_view)
    FrameLayout mEmojiconsView ;

    @ViewById(R.id.toggle_keyboard_emoji)
    ImageView mToggleKeyBoardEmojiIcon ;

    @ViewById(R.id.delete_icon)
    View mDeleteIcon ;

    @ViewById(R.id.rootView)
    View mRootView ;

    SlideBackLayout mSlideBackLayout ;
    private int mKeyboardHeight ;

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
        mEditText.setUseSystemDefault(false);
        mToggleKeyBoardEmojiIcon.setOnClickListener(this);
        mDeleteIcon.setOnClickListener(this);

        if(isEdit && mNoteId > 0){
            mOldNote = mPresenter.getNote(mNoteId) ;
            mEditText.setText(mOldNote.getContent());
        }
        mEditText.setSelection(mEditText.getText().length());
        updateCount() ;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons_view, EmojiconsFragment.newInstance(false))
                .commit();
        mEmojiconsView.setVisibility(GONE);

        mRootView.getViewTreeObserver(). addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                // r will be populated with the coordinates of your view that area still visible.
                mRootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = mRootView.getRootView().getHeight();
                int heightDiff = screenHeight - (r.bottom - r.top);
                int statusBarHeight = 0;
                if (heightDiff > 100)
                    // if more than 100 pixels, its probably a keyboard
                    // get status bar height

                try {
                    Class<?> c = Class.forName("com.android.internal.R$dimen");
                    Object obj = c.newInstance();
                    Field field = c.getField("status_bar_height");
                    int x = Integer.parseInt(field.get(obj).toString());
                    statusBarHeight = getResources().getDimensionPixelSize(x);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int realKeyboardHeight = heightDiff - statusBarHeight;
                L.i("keyboard height = " + realKeyboardHeight);
                if(mKeyboardHeight < realKeyboardHeight){
                    mKeyboardHeight = realKeyboardHeight ;
                    // 设置emoji view的高度为keyboard的高度；
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)mEmojiconsView.getLayoutParams() ;
                    layoutParams.height = realKeyboardHeight ;
                }
            }
        }) ;
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
            case R.id.toggle_keyboard_emoji:
                onToggleKeyBoardEmojiClicked() ;
                break;
            case R.id.delete_icon:
                EmojiconsFragment.backspace(mEditText);
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

    private void onToggleKeyBoardEmojiClicked() {
        // 处理软键盘的弹出和关闭
        if(mEmojiconsView.getVisibility() == GONE){
            // 关闭软键盘
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            // 弹出emoji
            mEmojiconsView.setVisibility(View.VISIBLE);
            mToggleKeyBoardEmojiIcon.setImageResource(R.drawable.ic_keyboard);
        }else{
            // 打开软键盘
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//得到InputMethodManager的实例
            if (imm.isActive()) {
//如果开启
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
            }
            // 关闭emoji
            mEmojiconsView.setVisibility(GONE);
            mToggleKeyBoardEmojiIcon.setImageResource(R.drawable.ic_emoji);
        }
    }
}
