package com.example.testokhttp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.*;




class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public MyViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<String> myDataset) {
        mDataset = myDataset.toArray(new String[0]);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mDataset[position]);

    }

    private final int limit = 4;
    // Return the size of your dataset (invoked by the layout manager)

    @Override
    public int getItemCount() {
        //if( mDataset.length > limit){
        //    return limit;
        //}
        //else
        //{
            return mDataset.length;
        //}

    }

}


public class MainActivity extends AppCompatActivity {

    private TextView mTextViewResult;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);


        //public void setHeight(final View convertView) {
        //    final int height = recyclerView.getMeasuredHeight() / 3;
        //    convertView.setLayoutParams(newAbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        //}


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(false);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)

        ////////////////////////////////////////////////////////////////////////////
        // tests

        String welcomeText = "coucou";
        //final TextView helloTextView = (TextView) findViewById(R.id.welcomeText);
        //helloTextView.setText( welcomeText);

        // tuto

        //mTextViewResult = findViewById( R.id.welcomeText );

        OkHttpClient client = new OkHttpClient();
        String url = "https://reqres.in/api/user?page=2";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if( response.isSuccessful() ){


                    final String res = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            JSONObject testParseJson = null;
                            //parsing du res de la req
                            try {
                                testParseJson = new JSONObject(res);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Integer page = null;
                            try {
                                page = testParseJson.getInt("page");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Integer per_page = null;
                            try {
                                per_page = testParseJson.getInt("per_page");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            JSONArray datas = null;
                            try {
                                datas = testParseJson.getJSONArray("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            System.out.println( page );
                            System.out.println(per_page);
                            System.out.println( datas );

                            List<String> list = new ArrayList<String>();
                            for(int i = 0; i < datas.length(); i++){
                                try {
                                    list.add(
                                            datas.getJSONObject(i).getString("id")
                                            + "\n\n" +
                                            datas.getJSONObject(i).getString("name")
                                            + "\n\n" +
                                            datas.getJSONObject(i).getString("year")

                                    );
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                            mAdapter = new MyAdapter( list );
                            recyclerView.setAdapter(mAdapter);
                            //mTextViewResult.setText( res );
                        }
                    });
                }
            }
        });
    }
}