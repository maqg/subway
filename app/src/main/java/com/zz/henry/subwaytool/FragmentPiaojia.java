package com.zz.henry.subwaytool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import com.zz.henry.subwaytool.findway.QueryResult;
import java.io.InputStream;

/**
 * Created by henry on 15/4/22.
 */
public class FragmentPiaojia extends Fragment {

    private Spinner spinnerLine;
    private Spinner spinnerStation;
    private Spinner spinnerLineEnd;
    private Spinner spinnerStationEnd;
    private QueryResult queryResult;

    private static View rootView = null;
    private Activity myactivity = null;

    private void initSpinners() {
        spinnerLine = (Spinner) rootView.findViewById(R.id.line_select_spinner);
        spinnerStation = (Spinner) rootView.findViewById(R.id.station_select_spinner);
        spinnerLineEnd = (Spinner) rootView.findViewById(R.id.end_line_select_spinner);
        spinnerStationEnd = (Spinner) rootView.findViewById(R.id.end_station_select_spinner);

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

        spinnerLineEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Selected " + spinnerLineEnd.getSelectedItem().toString());
                loadEndStation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("Nonthing selected");
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

    private int initStartInfo() {

        String[] lines = LineUtils.getInstance().getLineStrList();
        ArrayAdapter<String> lineAdapter = new ArrayAdapter<String>(myactivity, android.R.layout.simple_spinner_item, lines);
        lineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLine.setAdapter(lineAdapter);

        loadStartStation();

        return 0;
    }

    private void loadEndStation() {
        String lineName = spinnerLineEnd.getSelectedItem().toString();
        String[] stations = StationUtils.getStationStrList(lineName);

        ArrayAdapter<String> stationAdapter = new ArrayAdapter<String>(myactivity, android.R.layout.simple_spinner_item, stations);
        stationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStationEnd.setAdapter(stationAdapter);
    }

    private int initEndInfo() {

        String[] lines = LineUtils.getInstance().getLineStrList();

        ArrayAdapter<String> lineAdapter = new ArrayAdapter<String>(myactivity, android.R.layout.simple_spinner_item, lines);
        lineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLineEnd.setAdapter(lineAdapter);

        loadEndStation();

        return 0;
    }

    public int setSpinnerInfo() {
        // loadExampleData Spinners
        initSpinners();

        if (initStartInfo() != 0 || initEndInfo() != 0) {
            return -1;
        }

        return 0;
    }

    private void alertExit(String msg) {

        AlertDialog.Builder build = new AlertDialog.Builder(myactivity);

        build.setTitle("异常退出");
        build.setPositiveButton("确定", null);
        build.setMessage(msg);

        AlertDialog alertDialog = build.create();
        alertDialog.show();

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                System.out.println("系统异常退出！");
                System.exit(1);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // get activity
        myactivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (rootView == null){
            rootView = inflater.inflate(R.layout.piaojia, null);

            InputStream is = this.getResources().openRawResource(R.raw.subway); // resources
            if (LineUtils.getInstance().initLines(is) != 0) {
                alertExit("初始化原始数据失败，系统退出。。");
            }

            if (setSpinnerInfo() != 0) {
                alertExit("初始化Spinner信息失败，系统退出。。");
            }

            queryResult = new QueryResult();

            buttonOnClick();
        }

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    private void buttonOnClick() {
        Button button = (Button) rootView.findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView textView = (TextView) rootView.findViewById(R.id.output);

                queryResult.dijkstraQuery(spinnerStation.getSelectedItem().toString(),
                        spinnerStationEnd.getSelectedItem().toString());

                String outText = "\n注意：以下结果给出的是最短距离的计价方案，并不一定是最优的换乘方案";
                outText += "\n总距离：" + queryResult.getDistance() + " 米";
                outText += "\n消耗时间：约 " + queryResult.getMinutes() + " 分钟";
                outText += "\n单次费用：" + queryResult.getPrice() + " 元";
                outText += "\n每月花费：约 " + queryResult.getPayMonth() + " 元";
                outText += "\n每年花费：约 " + queryResult.getPayYear() + " 元";
                outText += "\n经停站：（共计 " + (queryResult.getPreStationList().size() - 1) + " 站）";
                outText += "\n" + queryResult.getStationListStr();

                textView.setText(outText);

                textView.setMovementMethod(ScrollingMovementMethod.getInstance());
            }
        });
    }

}
