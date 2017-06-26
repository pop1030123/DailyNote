package com.popfu.dailynote.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.popfu.dailynote.presenter.MainPresenter;
import com.popfu.dailynote.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity implements View.OnClickListener {


    @ViewById(R.id.main_list)
    RecyclerView mRecyclerView ;

    @ViewById(R.id.right_button)
    View mAddNoteView ;

    LinearLayoutManager mLayoutManager ;

    private MainPresenter mPresenter ;

    private MainAdapter mMainAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new MainPresenter() ;

        mMainAdapter = new MainAdapter(this) ;
        mLayoutManager = new LinearLayoutManager(this) ;

    }


    @AfterViews
    void afterViews(){

        mMainAdapter.setNoteList(mPresenter.getNotes());

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mMainAdapter);

        mAddNoteView.setOnClickListener(this);
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
