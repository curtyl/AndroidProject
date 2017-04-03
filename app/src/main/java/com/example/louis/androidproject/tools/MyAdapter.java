package com.example.louis.androidproject.tools;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.louis.androidproject.MainActivity;
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
    private final MainActivity mActivity;
    private DatabaseHandler dbHandler;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public final Button mButton;
        public final TextView global;
        public final Button share;
        public final Button refresh;
        public ViewHolder(View v) {
            super(v);
            mButton = (Button) v.findViewById(R.id.button);
            global = (TextView) v.findViewById(R.id.globalinfo);
            share = (Button) v.findViewById(R.id.share);
            refresh = (Button) v.findViewById(R.id.refresh);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)

    /**
     * Constructor
     * @param ctx
     * @param initialCities
     * @param bdd
     */
    public MyAdapter(Context ctx, ArrayList<GlobalObject> initialCities, DatabaseHandler bdd, MainActivity Activity) {
        mCtx = ctx;
        mDataset = initialCities;
        mBdd = bdd;
        mActivity = Activity;

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
    public void onBindViewHolder(ViewHolder holder,final int position) {


        final MessageObject msg = mDataset.get(position).getRxs().getObs().get(0).getMsg();

        holder.refresh.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.getDataFromUrl("https://api.waqi.info/api/feed/@" + mDataset.get(position).getRxs().getObs().get(0).getMsg().getCity().getIdx()+ "/obs.fr.json",false);
            }
        });

        holder.share.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{""});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT   , mDataset.get(position).getRxs().getObs().get(0).getMsg().getCity().getName() + mCtx.getResources().getString(R.string.bodyMail) + msg.getIaqi().get(0).getV().get(0));
                try {
                    mCtx.startActivity(Intent.createChooser(i, mCtx.getResources().getString(R.string.sendMail)));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(mCtx, mCtx.getResources().getString(R.string.mailErreur), Toast.LENGTH_SHORT).show();
                }
            }
        });

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