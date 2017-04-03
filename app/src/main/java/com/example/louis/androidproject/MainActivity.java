package com.example.louis.androidproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.louis.androidproject.database.DatabaseHandler;
import com.example.louis.androidproject.model.CityObject;
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
    private final ArrayList<GlobalObject> initialCities = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText mEditText = (EditText) findViewById(R.id.idCity);
        final Button valider = (Button) findViewById(R.id.valider);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        dbHandler = new DatabaseHandler(this);
        final List<CityObject> tmpCities = dbHandler.selectAll();

        /**
         * Initialize the database
         */
        for(int i=0; i<tmpCities.size();i++) {
            getDataFromUrl("https://api.waqi.info/api/feed/@"+tmpCities.get(i).getIdx()+"/obs.fr.json?token=af073d16e3707f6d085660cfcd0137a61b961365", false);
        }
        /**
         * Add event on editText
         */
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    valider.setEnabled(false);
                } else {
                    valider.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /**
         * Add event on the on click button
         */
        valider.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (tmpCities.isEmpty()){
                    getDataFromUrl("https://api.waqi.info/api/feed/@"+mEditText.getText().toString()+"/obs.fr.json?token=af073d16e3707f6d085660cfcd0137a61b961365", true);

                }
                boolean continuer = true;
                for(int i=0; i<initialCities.size() && continuer;i++) {
                    Integer check = Integer.parseInt(mEditText.getText().toString());
                    if(!check.equals((tmpCities.get(i).getIdx()))){
                         continuer = true;
                    }else {
                        Toast.makeText(MainActivity.this, "Vous avez déjà rentré cette ville", Toast.LENGTH_SHORT).show();
                        continuer = false;
                    }
                }
                if (continuer){
                    getDataFromUrl("https://api.waqi.info/api/feed/@"+mEditText.getText().toString()+"/obs.fr.json?token=af073d16e3707f6d085660cfcd0137a61b961365", true);
                }

            }
        });

        mAdapter = new MyAdapter(this, initialCities, dbHandler, this);
        mRecyclerView.setAdapter(mAdapter);
        dbHandler.close();

        ItemTouchHelper.Callback callback = new TouchHelperItem((MyAdapter) mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * This function get data from the API
     * @param url string de l'url
     * @param add boolean
     */
    public void getDataFromUrl(String url, final Boolean add) {
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

                        if(initialCities.indexOf(obj) == -1){
                            initialCities.add(obj);
                        }
                        if(add){
                            dbHandler.insert(obj.getRxs().getObs().get(0).getMsg().getCity());
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
