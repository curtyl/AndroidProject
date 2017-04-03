package com.example.louis.androidproject.touchHelper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.louis.androidproject.tools.MyAdapter;

/**
 * Created by louis on 27/03/2017.
 */

public class TouchHelperItem extends ItemTouchHelper.SimpleCallback {
    private MyAdapter myAdapter;

    public TouchHelperItem(MyAdapter myAdapter){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.myAdapter = myAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        myAdapter.remove(viewHolder.getAdapterPosition());
    }
}
