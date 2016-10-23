package com.example.administrator.arithmetic_master.view;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.arithmetic_master.Model.Leaderboard_RowItem;
import com.example.administrator.arithmetic_master.R;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {

    private ImageView img_doCalculate;
    private ImageView img_reStart;
    private ImageView img_order;
    private ImageView img_statistic;
    private ImageView img_muitiplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        img_doCalculate = (ImageView) findViewById(R.id.img_calculate);
        img_reStart = (ImageView) findViewById(R.id.img_restart);
        img_order = (ImageView) findViewById(R.id.img_order);
        img_statistic = (ImageView) findViewById(R.id.img_statistic);
        img_muitiplication = (ImageView) findViewById(R.id.img_multiplication);

        img_doCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalculateActivity.class);
                startActivity(intent);

            }
        });

        img_reStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DoWrongActivity.class);
                startActivity(intent);
            }
        });

        img_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserOrderActivity.class);
                startActivity(intent);
            }
        });

        img_statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StatisticActivity.class);
                startActivity(intent);
            }
        });
        img_muitiplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPageActivity.this, MuitiplicationActivity.class));
            }
        });
    }
}
