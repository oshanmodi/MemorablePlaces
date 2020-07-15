package com.example.oshan.memorableplaces;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView placesListView;
    ArrayList<String> placeNames = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        add a header to the list view. this add extra space on top which refines the look of the app.
        placesListView = findViewById(R.id.placesListView);
        TextView headerTextView;
        headerTextView = new TextView(this);
        headerTextView.setText("Your saved places \n");
        headerTextView.setTextSize(30);
        headerTextView.setClickable(true);
        headerTextView.setAllCaps(true);
        placesListView.addHeaderView(headerTextView);

//        add array adapter to the list view. We only have on item at the start.
//        as user saves more location we keep adding more itemss
        placeNames.add("add a favorite place");

        Context context = getApplicationContext();
        ArrayAdapter placesAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, placeNames);

        placesListView.setAdapter(placesAdapter);

    }
}
