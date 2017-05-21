package com.example.francescop.cul8r;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DetailsFragment extends Fragment {

    // factory
    public static DetailsFragment newInstance(int index) {
        DetailsFragment f = new DetailsFragment();

        // provides index as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    public int getIndexFromArguments() {
        return getArguments().getInt("index", 0);
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        int l = 0;
        int shownIndex = getIndexFromArguments();
        switch (shownIndex) {
            case 1:
                l = R.layout.details_fragment1; break;
            case 2:
                l = R.layout.details_fragment2; break;
            case 3:
                l = R.layout.details_fragment3; break;
        }
        return inflater.inflate(l, container, false);
    }
}