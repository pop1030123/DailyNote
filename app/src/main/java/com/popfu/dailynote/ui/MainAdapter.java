package com.popfu.dailynote.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.popfu.dailynote.R;
import com.popfu.dailynote.bean.Note;

import java.util.List;

/**
 * Created by pengfu on 26/06/2017.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ItemHolder> {


    Context mContext ;

    List<Note> mNoteList ;

    public MainAdapter(Context context){
        mContext = context ;
    }

    public void setNoteList(List<Note> data){
        mNoteList = data ;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(mContext , R.layout.main_item ,null) ;
        ItemHolder holder = new ItemHolder(rootView) ;
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.titleView.setText(mNoteList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mNoteList.size() ;
    }


    class ItemHolder extends RecyclerView.ViewHolder{

        TextView titleView ;

        public ItemHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.text) ;
        }
    }
}
