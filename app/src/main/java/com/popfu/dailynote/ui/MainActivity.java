package com.popfu.dailynote.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.popfu.dailynote.event.AddNoteEvent;
import com.popfu.dailynote.bean.Note;
import com.popfu.dailynote.event.DeleteNoteEvent;
import com.popfu.dailynote.event.UpdateNoteEvent;
import com.popfu.dailynote.presenter.MainPresenter;
import com.popfu.dailynote.R;
import com.popfu.dailynote.ui.toast.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import static com.popfu.dailynote.ui.EditNoteActivity.KEY_EDIT;
import static com.popfu.dailynote.ui.EditNoteActivity.KEY_NOTE_ID;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements View.OnClickListener, MainAdapter.Callback {


    @ViewById(R.id.main_list)
    RecyclerView mRecyclerView ;

    @ViewById(R.id.right_button)
    View mAddNoteView ;

    LinearLayoutManager mLayoutManager ;

    private MainPresenter mPresenter ;

    private MainAdapter mMainAdapter ;

    private List<Note> mNoteList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MainPresenter() ;

        mMainAdapter = new MainAdapter(this) ;
        mMainAdapter.setCallback(this);
        mLayoutManager = new LinearLayoutManager(this) ;

    }


    @AfterViews
    void afterViews(){

        mNoteList = mPresenter.getNotes() ;

        mMainAdapter.setNoteList(mNoteList);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mMainAdapter);

        mAddNoteView.setOnClickListener(this);
    }


    public void onEvent(AddNoteEvent addNoteEvent){
        mNoteList.add(mNoteList.size() ,addNoteEvent.note);
        mMainAdapter.notifyItemInserted(mNoteList.size()-1);
    }

    /**
     * note has update
     * @param updateNoteEvent
     */
    public void onEvent(UpdateNoteEvent updateNoteEvent){
        Note tempNote = null ;
        for(Note note: mNoteList){
            if(note.getId() == updateNoteEvent.note_id){
                tempNote = note ;
                break ;
            }
        }
        if(tempNote != null){
            int index = mNoteList.indexOf(tempNote) ;
            Note updateNote = mPresenter.getNote(tempNote.getId()) ;
            // 先移除，再添加
            mNoteList.remove(index) ;
            mNoteList.add(index ,updateNote);
            // 更新UI
            mMainAdapter.notifyItemChanged(index);
        }
    }

    public void onEvent(DeleteNoteEvent deleteNoteEvent){
        Note tempNote = null ;
        for(Note note: mNoteList){
            if(note.getId() == deleteNoteEvent.note_id){
                tempNote = note ;
                break ;
            }
        }
        if(tempNote != null){
            int index = mNoteList.indexOf(tempNote) ;
            // 移除元素
            mNoteList.remove(tempNote) ;
            // 更新UI
            mMainAdapter.notifyItemRemoved(index);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.right_button:
                EditNoteActivity_.intent(this)
                        .extra(KEY_EDIT ,false)
                        .start() ;
                break ;
        }
    }

    @Override
    public void onItemClick(int position) {
        // to edit note
        EditNoteActivity_.intent(this)
                .extra(KEY_EDIT ,true)
                .extra(KEY_NOTE_ID ,mNoteList.get(position).getId())
                .start() ;
    }
}
