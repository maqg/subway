package com.zz.henry.subwaytool;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by henry on 15/4/22.
 */
public class FragmentShoumoche extends Fragment {

    private Spinner spinnerLine;
    private Spinner spinnerStation;

    private static View rootView = null;
    private Activity myactivity = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        myactivity = getActivity();
    }


    private String getShoumoche(String stationName, String lineName) {


        Station station = StationUtils.getStation(stationName, lineName);
        if (station != null) {
            return station.getShoumoche();
        } else {
            return "No such station";
        }
    }


    private void buttonOnClick() {
        Button button = (Button) rootView.findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lineName = spinnerLine.getSelectedItem().toString();
                String stationName = spinnerStation.getSelectedItem().toString();

                String result = "获得首末车时间表如下：（" + stationName + "）\n";

                TextView textView = (TextView) rootView.findViewById(R.id.output);

                result += getShoumoche(stationName, lineName);

                textView.setText(result);
            }
        });
    }

    private void loadStartStation() {
        String lineName = spinnerLine.getSelectedItem().toString();
        String[] stations = StationUtils.getStationStrList(lineName);

        ArrayAdapter<String> stationAdapter = new ArrayAdapter<String>(myactivity, android.R.layout.simple_spinner_item, stations);
        stationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStation.setAdapter(stationAdapter);
    }

    private int initLineInfo() {

        spinnerLine = (Spinner) rootView.findViewById(R.id.line_select_spinner);
        spinnerStation = (Spinner) rootView.findViewById(R.id.station_select_spinner);

        spinnerLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Selected " + spinnerLine.getSelectedItem().toString());
                loadStartStation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("Nonthing selected");
            }
        });

        String[] lines = LineUtils.getInstance().getLineStrList();
        ArrayAdapter<String> lineAdapter = new ArrayAdapter<String>(myactivity, android.R.layout.simple_spinner_item, lines);
        lineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLine.setAdapter(lineAdapter);

        loadStartStation();

        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.shoumoche, null);

            if (initLineInfo() != 0) {
                System.exit(1);
            }

            buttonOnClick();
        }

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }
}
