package com.zz.henry.subwaytool.findway;

import com.zz.henry.subwaytool.Line;
import com.zz.henry.subwaytool.LineUtils;
import com.zz.henry.subwaytool.Station;
import com.zz.henry.subwaytool.common.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by henry on 2015/1/19.
 */
public class Subway {

    private static final int INF = Constants.MAX_DISTANCE;
    private static List<Station> stationList = new ArrayList<Station>();
    private static int paths[];
    private static int distances[];
    private static int distanceMap[][];
    private static boolean[] visited;
    private static int total;

    Map<String, Station> stationListMap;

    public Subway() {

        stationListMap = new HashMap<>();

        for (Line line : LineUtils.getInstance().getLines()) {
            for (Station station : line.getStations()) {

                Station oldStation = stationListMap.get(station.getName());
                Station nextStation = station.getNextStation();
                Station preStation = station.getPreStation();

                if (oldStation != null) { // station of this name already exist
                    // to append lineId
                    oldStation.addLineId(station.getLineId());

                    if (nextStation != null) {
                        Station oldNextStation = stationListMap.get(nextStation.getName());
                        if (oldNextStation == null) {
                            oldStation.addSubStationsMap(nextStation, station.getNextLength());
                        } else {
                            oldStation.addSubStationsMap(oldNextStation, station.getNextLength());
                        }
                    }

                    if (preStation != null) {
                        Station oldPreStation = stationListMap.get(preStation.getName());
                        if (oldPreStation == preStation) { // this is a simple case.
                            oldStation.addSubStationsMap(preStation, station.getPreLength());
                        } else { // preStation in a complicated case.
                            oldStation.addSubStationsMap(oldPreStation, preStation.getNextLength());
                        }
                    }

                } else {

                    stationList.add(station);
                    station.setPosition(stationList.size() - 1);

                    stationListMap.put(station.getName(), station);
                    if (nextStation != null) {
                        Station oldNextStation = stationListMap.get(nextStation.getName());
                        if (oldNextStation != null) {
                            station.addSubStationsMap(oldNextStation, station.getNextLength());
                        } else {
                            station.addSubStationsMap(nextStation, station.getNextLength());
                        }
                    }
                    if (preStation != null) {
                        Station oldPreStation = stationListMap.get(preStation.getName());
                        if (oldPreStation != null) {
                            station.addSubStationsMap(oldPreStation, station.getPreLength());
                        } else {
                            // this means start of circle.
                            station.addSubStationsMap(preStation, station.getPreLength());
                        }
                    }
                }
            }
        }

        distanceMap = new int[stationList.size()][stationList.size()];
        distances = new int[stationList.size()];
        paths = new int[stationList.size()];
        visited = new boolean[stationList.size()];
        total = stationList.size();

        initDistanceMap();
    }

    public void initDistanceMap() {

        // to init distance Map
        for (int i = 0; i < stationList.size(); i++) {
            for (int j = 0; j < stationList.size(); j++) {
                if (i == j)
                    distanceMap[i][j] = 0;
                else
                    distanceMap[i][j] = INF;
            }
        }

        // to load distances from subway
        Iterator<String> it = stationListMap.keySet().iterator();
        while (it.hasNext()) {
            Station station = stationListMap.get(it.next());
            Iterator<Station> subIt = station.getSubStationsMap().keySet().iterator();

            while (subIt.hasNext()) {
                Station subStation = subIt.next();
                Integer distance = station.getSubStationsMap().get(subStation);
                distanceMap[station.getPosition()][subStation.getPosition()] = distance;
            }
        }
    }

    public static int getShortest() {
        int min = INF;
        int shortest = -1;

        for (int i = 0; i < total; i++) {
            if (!visited[i]) {
                if(distances[i] < min) {
                    min = distances[i];
                    shortest = i;
                }
            }
        }

        return shortest;
    }

    int dijkstra(int start) {

        Arrays.fill(visited, false);
        Arrays.fill(distances, INF);
        visited[start] = true;

        for (int i = 0; i < total; i++) {
            distances[i] = distanceMap[start][i];
            if (i != start && distances[i] < INF)
                paths[i] = start;
            else
                paths[i] = -1;
        }

        while (true) {

            int latest = getShortest();
            if (latest == -1) {
                // no next left.
                break;
            }

            visited[latest] = true;
            for (int i = 0; i < total; i++) {
                if (!visited[i] && distanceMap[latest][i] != INF &&
                        distances[latest] + distanceMap[latest][i] < distances[i]) {
                    // update path here.
                    distances[i] = distances[latest] + distanceMap[latest][i];
                    paths[i] = latest;
                }
            }
        }

        return 0;
    }

    public int getDistance(Station station) {
        return distances[station.getPosition()];
    }

    public List<Station> getPathStationList(Station station) {
        return getPathStationList(station.getPosition());
    }

    public List<Station> getPathStationList(int stationIndex) {
        List<Station> pathList = new ArrayList<Station>();
        int preStation = paths[stationIndex];

        while (preStation != -1) {
            pathList.add(0, stationList.get(preStation));
            preStation = paths[preStation];
        }
        pathList.add(stationList.get(stationIndex));

        return pathList;
    }

    public void dumpDijkstra() {

        for (int i = 1; i < total; i++) {
            List<Station> pathList = getPathStationList(i);
            System.out.printf("\nTo: %s(%d), via ", stationList.get(i).getName(), distances[i]);

            for (Station station : pathList) {
                System.out.printf("%s->", station.getName());
            }
        }
    }
}
