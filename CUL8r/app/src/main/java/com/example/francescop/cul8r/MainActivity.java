package com.example.francescop.cul8r;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    int userSelection = 1;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            userSelection = savedInstanceState.getInt("userSelection");
        }
        setContentView(R.layout.activity_main);
        username = getIntent().getStringExtra("username");
        TextView tw = (TextView) findViewById(R.id.textViewUname);
        String txt = tw.getText().toString() + " " + username;
        ((TextView) findViewById(R.id.textViewUname)).setText(txt);
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
            case R.id.button4:
                finish();
                return;
        }
        showDetails(userSelection);
    }

    private void highlightButton() {
        int hc = ContextCompat.getColor(this, R.color.colorAccent);
        int nc = ContextCompat.getColor(this, R.color.colorPrimary);
        Button b[] = new Button[3];
        b[0] = (Button) findViewById(R.id.button1);
        b[1] = (Button) findViewById(R.id.button2);
        b[2] = (Button) findViewById(R.id.button3);

        for(Button i:b){//normal color for each button
            i.setBackgroundColor(nc);
        }
        //highlight only the selected one
        b[userSelection-1].setBackgroundColor(hc);
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

        // if the details fragment was empty or different from the currently selected
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