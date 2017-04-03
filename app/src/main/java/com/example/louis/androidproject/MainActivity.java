package com.example.louis.androidproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.louis.androidproject.database.DatabaseHandler;
import com.example.louis.androidproject.model.GlobalObject;
import com.example.louis.androidproject.tools.MyAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import com.example.louis.androidproject.touchHelper.TouchHelperItem;
public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseHandler dbHandler;
    private ArrayList<Integer> myDataset = new ArrayList<>(); //{"@3067", "@3071"};
    private ArrayList<GlobalObject> initialCities = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText mEditText = (EditText) findViewById(R.id.idCity);
        Button valider = (Button) findViewById(R.id.valider);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        dbHandler = new DatabaseHandler(this);
        List<GlobalObject> tmpCities = dbHandler.selectAll();
        mAdapter = new MyAdapter(this, initialCities, dbHandler);
        mRecyclerView.setAdapter(mAdapter);
        dbHandler.close();

        for(int i=0; i<tmpCities.size();i++) {
            initialCities.add(tmpCities.get(i));
            /*if(initialCities.isEmpty()){
                initialCities.add(tmpCities.get(i));
                i = i + 1;}

            if(!initialCities.get(i-1).equals(tmpCities.get(i))){
                initialCities.add(tmpCities.get(i));}*/
        }

        ItemTouchHelper.Callback callback = new TouchHelperItem((MyAdapter) mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        for(int i=0; i<myDataset.size();i++) {
            if(!myDataset.get(i).equals(tmpCities.get(i).getRxs().getObs().get(0).getMsg().getCity().getIdx())){
            getDataFromUrl("https://api.waqi.info/api/feed/@"+myDataset.get(i)+"/obs.fr.json?token=af073d16e3707f6d085660cfcd0137a61b961365");
            }
        }

        valider.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                for(int i=0; i<myDataset.size();i++) {
                    if(!myDataset.get(i).equals(mEditText.getText()))
                    getDataFromUrl("https://api.waqi.info/api/feed/@"+mEditText.getText().toString()+"/obs.fr.json?token=af073d16e3707f6d085660cfcd0137a61b961365");
                }
            }
        });
    }

    private void getDataFromUrl(String url) {
        final TextView mTextView = (TextView) findViewById(R.id.info);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        GlobalObject obj = gson.fromJson(response, GlobalObject.class);
                        initialCities.add(obj);
                        dbHandler.insert(obj.getRxs().getObs().get(0).getMsg().getCity());
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
