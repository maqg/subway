package com.zz.henry.subwaytool;

import com.zz.henry.subwaytool.utils.Shoumoche;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Line {

    private int id = 0;
    private String name = null;
    private List<Station> stations = new ArrayList<>();

    public int initStations(JSONObject lineNode) {
        try {

            Station preStation = null;
            int preLength = 0;
            JSONArray stationList = lineNode.getJSONArray("stations");

            for (int i = 0; i < stationList.length(); i++) {
                JSONObject stationNode = stationList.getJSONObject(i);
                Station station = new Station();

                station.setId(stationNode.getInt("id") + (id << 16));
                station.setName(stationNode.getString("name"));
                station.setNextLength(stationNode.getInt("length"));

                // to record preLength here.
                station.setPreLength(preLength);
                preLength = station.getNextLength();

                // to record preStation here.
                station.setPreStation(preStation);
                if (preStation != null) {
                    preStation.setNextStation(station);
                }

                if (stationNode.has("endCircle")) {
                    station.setNextStation(stations.get(0));
                    stations.get(0).setPreStation(station);
                    stations.get(0).setPreLength(station.getNextLength());
                }

                if (stationNode.has("lastTrain")) {
                    JSONArray timeList = stationNode.getJSONArray("lastTrain");
                    for (int j = 0; j < timeList.length(); j++) {

                        JSONObject timeNode = timeList.getJSONObject(j);
                        Shoumoche shoumoche = new Shoumoche();
                        shoumoche.setName(timeNode.getString("direction"));
                        shoumoche.setFirst(timeNode.getString("first"));

                        if (timeNode.has("last")) {
                            shoumoche.setLast(timeNode.getString("last"));
                        }

                        station.getTimeList().add(shoumoche);
                    }
                }

                preStation = station;

                stations.add(station);
            }
        } catch (Exception e) {
            System.out.println("handle line node error");
            return -1;
        }

        return 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Station getStation(String name) {
        for (Station station : stations) {
            if (station.getName().equals(name)) {
                return station;
            }
        }
        return null;
    }
}