package com.example.publictransportationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class activity_parseSTPT extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ParseAdapter adapter;
    private ArrayList<ParseItem> parseItems = new ArrayList<>();
    private ProgressBar progressBar;

    public activity_parseSTPT()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_stpt);

        //progressBar = progressBar.findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParseAdapter(parseItems, this);
        recyclerView.setAdapter(adapter);


        Content content = new Content();
        content.execute();
    }

    private class Content extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
            //progressBar.setAnimation(AnimationUtils.loadAnimation(activity_parseSTPT.this, android.R.anim.fade_in)); //Main_Activity maybe?
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            //progressBar.setVisibility(View.GONE);
            //progressBar.setAnimation(AnimationUtils.loadAnimation(activity_parseSTPT.this, android.R.anim.fade_out)); //Main_Activity maybe?
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled(Void unused) {
            super.onCancelled(unused);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                String url = "http://stpt.ro/info2.html";
                Document doc = Jsoup.connect(url).get();
                Log.d("items", "connected");
                Elements data = doc.select("html.vrmfqqq idc0_334");
                int size = data.size();
                for(int i = 0; i < size; i++)
                {
                    String routeName = data.select("body.ul.table.tbody.tr.td") //html.vrmfqqq idc0_334
                            .select("b")
                            .eq(i)
                            .text();
                    String stationName = data.select("body.ul.table.tbody.tr.td") //html.vrmfqqq idc0_334
                            .select("b")
                            .eq(i)
                            .attr("D8D8D8");

                    String transportName = routeName;

                    String arrivalTime = data.select("body.ul.table.tbody.tr.td") //html.vrmfqqq idc0_334
                            .select("b")
                            .eq(i)
                            .text();

                    parseItems.add(new ParseItem(routeName, transportName, stationName, arrivalTime));

                    Log.d("items", "routeName: " + routeName + " transportName: " + transportName +
                            " stationName: " + stationName + " arrivalTime: " + arrivalTime);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}