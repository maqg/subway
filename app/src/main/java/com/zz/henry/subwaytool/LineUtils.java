package com.zz.henry.subwaytool;

import com.zz.henry.subwaytool.common.Constants;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by henry on 2015/1/6.
 */
public class LineUtils {

    private static LineUtils ourInstance = new LineUtils();

    public static LineUtils getInstance() {
        return ourInstance;
    }

    private LineUtils() {
    }

    private List<Line> lines = new ArrayList<Line>();

    public String[] getLineStrList() {
        String[] linesStr = new String[lines.size()];

        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            linesStr[i] = line.getName();
        }

        return linesStr;
    }

    public int initLines(InputStream is) {

        if (lines.size() != 0) {
            return 0;
        }

        lines = new ArrayList<Line>();
        byte[] buffer = null;

        try {
            buffer = new byte[is.available()];
            is.read(buffer);
        } catch (Exception e) {
            System.out.println("loadExampleData resources error");
            return -1;
        }

        try {
            String json = new String(buffer, "utf-8");
            JSONArray lineArray = new JSONArray(json);

            for (int i = 0; i < lineArray.length(); i++) {
                JSONObject lineNode = lineArray.getJSONObject(i);

                if (lineNode.getInt("id") == Constants.LINE_ID_JICHANGE) {
                    continue;
                }

                Line line = new Line();
                line.setName(lineNode.getString("name"));
                line.setId(lineNode.getInt("id"));

                if (line.initStations(lineNode) != 0) {
                    System.out.println("init stations error");
                    return -1;
                }

                lines.add(line);
            }

        } catch (Exception e) {
            System.out.println("buffer to string error");
            return -1;
        }

        return 0;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public Line getLine(int lineId) {
        for (Line line : lines) {
            if (line.getId() == lineId) {
                return line;
            }
        }
        return null;
    }

    public Line getLine(String lineName) {
        for (Line line : lines) {
            if (line.getName().equals(lineName)) {
                return line;
            }
        }
        return null;
    }
}
