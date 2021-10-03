package com.example.solseekers;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.math.BigDecimal;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText longitudeText;
    private EditText latitudeText;
    private EditText startDateText;
    private EditText endDateText;
    private Button button;
    private Spinner spinner;
    private GraphView graph;
    private MainActivity mainActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // declare the views
        spinner = findViewById(R.id.spinner);
        longitudeText = findViewById(R.id.longitudeText);
        latitudeText = findViewById(R.id.latitudeText);
        startDateText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.endDateText);
        button = findViewById(R.id.button);

        // populate the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.timePeriod, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timePeriod = spinner.getSelectedItem().toString();

                if (timePeriod.equals("daily")) {

                    // get the user's input
                    String longitude = longitudeText.getText().toString();
                    String latitude = latitudeText.getText().toString();
                    String startDate = startDateText.getText().toString();
                    String endDate = endDateText.getText().toString();

                    // Send a GET request to NASA API
                    RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);

                    String url = "https://power.larc.nasa.gov/api/temporal/daily/point?parameters=ALLSKY_SFC_SW_DWN&community=RE&format=JSON&longitude="
                            + longitude + "&latitude=" + latitude + "&start=" + startDate + "&end=" + endDate;

                    // ArrayLists to hold clean data
                    ArrayList<Integer> dates = new ArrayList<Integer>();
                    ArrayList<Float> solarIrradiance = new ArrayList<Float>();

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        // get raw Data
                                        JSONObject properties = response.getJSONObject("properties");
                                        JSONObject parameter = properties.getJSONObject("parameter");
                                        JSONObject data = parameter.getJSONObject("ALLSKY_SFC_SW_DWN");

                                        // clean data
                                        String rawData = data.toString().replace("{", "").replace("}", "");
                                        // split data into arrays
                                        String splitData[] = rawData.split(",");

                                        for (int i = 0; i < splitData.length; i++) {
                                            String temp[] = splitData[i].split(":");
                                            dates.add(Integer.parseInt(temp[0].substring(1, temp[0].length() - 1)));
                                            solarIrradiance.add(Float.parseFloat(temp[1]));
                                        }

                                        float x, y;

                                        // Display the graph
                                        graph = findViewById(R.id.graph);
                                        graph.removeAllSeries();
                                        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();

                                        for (int i = 0; i < dates.size(); i++) {
                                            x = i;
                                            y = solarIrradiance.get(i);
                                            series.appendData(new DataPoint(x, y), true, dates.size());
                                        }
                                        graph.addSeries(series);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    requestQueue.add(request);
                }
                else if (timePeriod.equals("monthly")) {
                    String longitude = longitudeText.getText().toString();
                    String latitude = latitudeText.getText().toString();
                    String startDate = startDateText.getText().toString();
                    String endDate = endDateText.getText().toString();

                    // Send a GET request to NASA API
                    RequestQueue requestQueue = Volley.newRequestQueue(mainActivity);

                    String url = "https://power.larc.nasa.gov/api/temporal/monthly/point?parameters=ALLSKY_SFC_SW_DWN&community=RE&format=JSON&longitude="
                            + longitude + "&latitude=" + latitude + "&start=" + startDate + "&end=" + endDate;

                    // ArrayLists to hold clean data
                    ArrayList<Integer> dates = new ArrayList<Integer>();
                    ArrayList<Float> solarIrradiance = new ArrayList<Float>();

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        // get raw Data
                                        JSONObject properties = response.getJSONObject("properties");
                                        JSONObject parameter = properties.getJSONObject("parameter");
                                        JSONObject data = parameter.getJSONObject("ALLSKY_SFC_SW_DWN");

                                        // clean data
                                        String rawData = data.toString().replace("{", "").replace("}", "");
                                        // split data into arrays
                                        String splitData[] = rawData.split(",");

                                        for (int i = 0; i < splitData.length; i++) {
                                            String temp[] = splitData[i].split(":");
                                            dates.add(Integer.parseInt(temp[0].substring(1, temp[0].length() - 1)));
                                            solarIrradiance.add(Float.parseFloat(temp[1]));
                                        }

                                        float x, y;

                                        // Display the graph
                                        graph = findViewById(R.id.graph);
                                        graph.removeAllSeries();
                                        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();

                                        for (int i = 0; i < dates.size(); i++) {
                                            x = i;
                                            y = solarIrradiance.get(i);
                                            series.appendData(new DataPoint(x, y), true, dates.size());
                                        }
                                        graph.addSeries(series);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    requestQueue.add(request);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}