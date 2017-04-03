package com.example.louis.androidproject.tools;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.louis.androidproject.R;
import com.example.louis.androidproject.database.DatabaseHandler;
import com.example.louis.androidproject.model.GlobalObject;
import com.example.louis.androidproject.model.MessageObject;

import java.util.ArrayList;

/**
 * Created by louis on 30/01/2017 for AndroidProject.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private final ArrayList<GlobalObject> mDataset;
    private final Context mCtx;
    private final DatabaseHandler mBdd;
    private DatabaseHandler dbHandler;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public final Button mButton;
        public final TextView global;
        public ViewHolder(View v) {
            super(v);
            mButton = (Button) v.findViewById(R.id.button);
            global = (TextView) v.findViewById(R.id.globalinfo);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)

    /**
     * Constructor
     * @param ctx
     * @param initialCities
     * @param bdd
     */
    public MyAdapter(Context ctx, ArrayList<GlobalObject> initialCities, DatabaseHandler bdd) {
        mCtx = ctx;
        mDataset = initialCities;
        mBdd = bdd;

    }

    // Create new views (invoked by the layout manager)

    /**
     * Create a new view and set the view parameters
     * @param parent
     * @param viewType
     * @return ViewHolder
     */
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    /**
     * Function that remove a city and notify
     * @param position
     */
    public void remove(int position) {
        mBdd.removeObj(mDataset.get(position));
        mDataset.remove(position);
        notifyItemRemoved(position);

    }

    // Replace the contents of a view (invoked by the layout manager)

    /**
     * get element from your dataset at the position and replace the contents fo the view with that element
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MessageObject msg = mDataset.get(position).getRxs().getObs().get(0).getMsg();

        for(int i=0;i<msg.getIaqi().size();i++) {
            if(msg.getIaqi().get(i).getP().contains("pm10")) {
                holder.mButton.setText(msg.getIaqi().get(i).getV().get(0).toString());
                if(msg.getIaqi().get(i).getV().get(0)<50) {
                    holder.mButton.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.green));
                    holder.global.setHighlightColor(ContextCompat.getColor(mCtx, R.color.green));
                } else if(msg.getIaqi().get(i).getV().get(0)<100) {
                    holder.mButton.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.orange));
                    holder.global.setHighlightColor(ContextCompat.getColor(mCtx, R.color.orange));
                } else {
                    holder.mButton.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.red));
                    holder.global.setHighlightColor(ContextCompat.getColor(mCtx, R.color.red));
                }
            }
            if(msg.getIaqi().get(i).getP().contains("t")) {
                String city = msg.getCity().getName();
                if(msg.getCity().getName().length()>20) {
                    city = msg.getCity().getName().substring(0,20)+"...";
                }
                holder.global.setText(city+" "+msg.getIaqi().get(i).getV().get(0)+"°C");
            }
        }
    }


    /**
     * Return the size of your dataset
     * @return int
     */
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}