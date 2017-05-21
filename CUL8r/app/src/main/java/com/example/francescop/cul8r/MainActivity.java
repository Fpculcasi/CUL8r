package com.example.francescop.cul8r;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    int userSelection = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            userSelection = savedInstanceState.getInt("userSelection");
        }
        setContentView(R.layout.activity_main);
    }

    public void buttonClicked(View v) {
        switch (v.getId()) {
            case R.id.button1:
                userSelection = 1;
                break;
            case R.id.button2:
                userSelection = 2;
                break;
            case R.id.button3:
                userSelection = 3;
                break;
        }
        showDetails(userSelection);
    }

    private void highlightButton() {
        int hc = getResources().getColor(android.R.color.holo_orange_dark);
        int nc = getResources().getColor(android.R.color.primary_text_light);
        Button b1 = (Button) findViewById(R.id.button1);
        Button b2 = (Button) findViewById(R.id.button2);
        Button b3 = (Button) findViewById(R.id.button3);
        b1.setTextColor(nc);
        b2.setTextColor(nc);
        b3.setTextColor(nc);
        switch (userSelection) {
            case 1:
                b1.setTextColor(hc);
                break;
            case 2:
                b2.setTextColor(hc);
                break;
            case 3:
                b3.setTextColor(hc);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("userSelection", userSelection);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userSelection = savedInstanceState.getInt("userSelection");
    }

    boolean twoPanes() {
        boolean res;
        View detailsFrame = findViewById(R.id.details);
        res = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        return res;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (twoPanes()) {
            displayDetailsFragment();
        }
    }

    private void displayDetailsFragment() {
        highlightButton();
        DetailsFragment details =
                (DetailsFragment)getFragmentManager().findFragmentById(R.id.details);
        if (details == null || details.getIndexFromArguments() != userSelection) {
            details = DetailsFragment.newInstance(userSelection);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.details, details);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
    }

    private void showDetails(int i) {
        if (twoPanes()) {
            displayDetailsFragment();
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("index", i);
            startActivity(intent);
        }
    }
}