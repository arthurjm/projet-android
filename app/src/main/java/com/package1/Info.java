package com.package1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class Info extends AppCompatActivity {

    public TextView text;
    public ArrayList<Button> buttonList;

    protected void onCreate(Bundle savedUnstanceState) {
        super.onCreate(savedUnstanceState);
        setContentView(R.layout.info);

        initiate();
        addListener();

    }

    public void initiate() {

        text = findViewById(R.id.textInfo);

        buttonList = new ArrayList<>();
        Button tb;

        tb = findViewById(R.id.infoBut);
        buttonList.add(tb);
        tb = findViewById(R.id.infoBut2);
        buttonList.add(tb);
        tb = findViewById(R.id.infoBut3);
        buttonList.add(tb);


    }

    public void addListener() {

        if (buttonList != null && buttonList.isEmpty() != true) {
            buttonList.get(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonList.get(1).setVisibility(View.GONE);
                    buttonList.get(0).setVisibility(View.GONE);
                    text.setText("Nous sommes 4 étudiants en licence 3 d'informatique de Bordeaux. \n Mathieu DUBAN \n Elias KHATI-LEFRANCOIS \n Arthur MONDON  \n LiangLiang PAN" +
                            "\n\n Ce projet a été réalisé dans le but de l'UE Projet Technologique.");
                }
            });
            buttonList.get(1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
            buttonList.get(2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonList.get(1).setVisibility(View.VISIBLE);
                    buttonList.get(0).setVisibility(View.VISIBLE);
                    text.setText("");
                }
            });
        }
    }

}
