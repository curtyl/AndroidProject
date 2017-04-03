package com.example.louis.androidproject.touchHelper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.louis.androidproject.tools.MyAdapter;

/**
 * Created by louis on 27/03/2017 for AndroidProject.
 */

public class TouchHelperItem extends ItemTouchHelper.SimpleCallback {
    private final MyAdapter myAdapter;

    public TouchHelperItem(MyAdapter myAdapter){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.myAdapter = myAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * Call the method remove from myAdapter to remove the item swiped at the "Adapter Position"
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        myAdapter.remove(viewHolder.getAdapterPosition());
    }
}
