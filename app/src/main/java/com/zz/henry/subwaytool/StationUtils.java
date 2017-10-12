package com.zz.henry.subwaytool;

import java.util.List;

/**
 * Created by henry on 2015/1/6.
 */
public class StationUtils {

    public static String[] getStationStrList(String lineName) {
        Line line = LineUtils.getInstance().getLine(lineName);
        if (line == null) {
            System.out.println("get line by name error: " + lineName);
            return null;
        }

        List<Station> stationList = line.getStations();
        if (stationList == null) {
            System.out.println("no stations in this line: " + line.getName());
            return null;
        }

        String[] stationStrList = new String[stationList.size()];
        for (int i = 0; i < stationList.size(); i++) {
            Station station = stationList.get(i);
            stationStrList[i] = station.getName();
        }

        return stationStrList;
    }

    public static Station getStation(int id) {
        Line line = LineUtils.getInstance().getLine((id >> 8) & 0xFF);
        if (line == null) {
            return null;
        }

        for (Station station : line.getStations()) {
            if (station.getId() == id) {
                return station;
            }
        }

        return null;
    }

    public static Station getStation(String name) {
        List<Line> lines = LineUtils.getInstance().getLines();
        for (Line line : lines) {
            Station station = line.getStation(name);
            if (station != null) {
                return station;
            }
        }
        return null;
    }


    public static Station getStation(String name, String lineName) {
        List<Line> lines = LineUtils.getInstance().getLines();
        for (Line line : lines) {

            if (!line.getName().equals(lineName)) {
                continue;
            }

            Station station = line.getStation(name);
            if (station != null) {
                return station;
            }
        }
        return null;
    }
}
