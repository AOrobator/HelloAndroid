package com.example.hello.rvdata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<DataItem> dataItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataItems = new ArrayList<>();
        for (int i=0; i<100; i++) {
            DataItem di = new DataItem("Name" + i, "image" + i + ".jpg");
            dataItems.add(di);
        }

        RecyclerView rv = findViewById(R.id.recyclerView);
        DataItemAdapter dia = new DataItemAdapter(this, dataItems);
        rv.setAdapter(dia);
    }
}
