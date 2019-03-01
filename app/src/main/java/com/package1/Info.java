package com.package1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class Info extends AppCompatActivity {
    /**
     * A text to set some explications
     */
    public TextView text;
    /**
     * An arraylist of buttons
     */
    public ArrayList<Button> buttonList;

    /**
     * @param savedUnstanceState
     */
    protected void onCreate(Bundle savedUnstanceState) {
        super.onCreate(savedUnstanceState);
        setContentView(R.layout.info);
        initiate();
        addListener();
    }

    /**
     * To set each button on the screen
     */
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

    /**
     * To link the corresponding action with each button
     */
    public void addListener() {

        if (buttonList != null && buttonList.isEmpty() != true) {
            buttonList.get(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    text.setText("Nous sommes 4 étudiants en licence 3 d'informatique de Bordeaux. \n Mathieu DUBAN \n Elias KHATI-LEFRANCOIS \n Arthur MONDON  \n LiangLiang PAN" +
                            "\n\n Ce projet a été réalisé dans le but de l'UE Projet Technologique.");
                }
            });
            buttonList.get(1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    text.setText("ToGrey -> met une image en gris \nColorize -> permet de colorizer une image avec une couleur choisie\nKeepcolor -> permet de choisir une couleur à garder et son niveau d'intensite\n" +
                            "Contrast -> permet d'augmenter le contraste de l'image");
                }
            });
            buttonList.get(2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    text.setText("");
                }
            });
        }
    }

}
