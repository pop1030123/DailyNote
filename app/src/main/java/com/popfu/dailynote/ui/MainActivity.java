package com.popfu.dailynote.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.popfu.dailynote.AddNoteEvent;
import com.popfu.dailynote.bean.Note;
import com.popfu.dailynote.presenter.MainPresenter;
import com.popfu.dailynote.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements View.OnClickListener {


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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.right_button:
                AddNoteActivity_.intent(this).start() ;
                break ;
        }
    }
}
