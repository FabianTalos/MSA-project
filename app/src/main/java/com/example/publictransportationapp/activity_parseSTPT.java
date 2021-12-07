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

            Log.d("Check_function", "Inside doInBackground");

            try{

                String url = "http://stpt.ro/info2.html";
                Document doc = Jsoup.connect(url).followRedirects(false).ignoreContentType(true).get();
                Elements data = doc.select("html"); //. lqrhlcda idc0_335

                Element iframe = doc.select("iframe").first(); //get frame that leads to STPT actual server
                String iframeSrc = iframe.attr("src");
                if(iframeSrc != null) {
                    Document document = Jsoup.connect(iframeSrc).get(); //connect to their server and start parsing

                    Elements myFrame = document.select("frameset").select("frame");

                    Element stanga = myFrame.get(1);//.attr("name");
                    Element centru = myFrame.get(2);
                    Element dreapta = myFrame.get(3);

                    Log.d("Data_print", "Stanga: " + stanga);  //iconite
                    Log.d("Data_print", "centru: " + centru);  //vehicule
                    Log.d("Data_print", "dreapta: " + dreapta); //timpi de sosire - statii

                    Elements centruCode = document.select("html > frameset > frameset > frame:nth-child(2)");// > html > body > #apDiv6 > p:nth-child(1) > a:nth-child(1)").attr("title");//.select("#apDiv6 > p:nth-child(1) > a:nth-child(1)").attr("title");
                    String centruCode2 = document.select("html")
                            .select("frameset")
                            .select("frameset")
                            .select("frame").attr("nth-child(2)");
                    Log.d("Data_print", "centruCode: " + centruCode);
                    Log.d("Data_print", "String: " + centruCode2);

                }

                //Log.d("Data_print", "data: " + data);
                //Log.d("Data_print", "body: " + body);
                //Log.d("Data_print", "divs: " + divs);
                //Log.d("Data_print", "myDiv: " + myDiv);
                //Log.d("Data_print", "content: " + content);

                int size = data.size();
                for(int i = 0; i < size; i++)
                {
                    String routeName = data.select("body.ul.table.tbody.tr.td") //html.vrmfqqq idc0_334
                            .select("b")
                            .eq(i)
                            .text();

                    Log.d("route_name", "routeName: " + routeName);
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