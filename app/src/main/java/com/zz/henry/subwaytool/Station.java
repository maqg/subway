package com.zz.henry.subwaytool;

import com.zz.henry.subwaytool.utils.Shoumoche;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by henry on 14/12/19.
 */
public class Station {

    private int id;
    private String name;
    private Station nextStation = null;
    private Station preStation = null;
    private Map<Station, Integer> subStationsMap = null;
    private int preLength = 0;
    private int nextLength = 0;
    private int position;
    private List<Integer> lineIdList = new ArrayList<>();
    private List<Shoumoche> timeList = new ArrayList<>();

    public Station() {
        subStationsMap = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNextLength() {
        return nextLength;
    }

    public void setNextLength(int nextLength) {
        this.nextLength = nextLength;
    }

    public int getPreLength() {
        return preLength;
    }

    public void setPreLength(int preLength) {
        this.preLength = preLength;
    }

    public Map<Station, Integer> getSubStationsMap() {
        return subStationsMap;
    }

    public Station getPreStation() {
        return preStation;
    }

    public void setPreStation(Station preStation) {
        this.preStation = preStation;
    }

    public Station getNextStation() {
        return nextStation;
    }

    public void setNextStation(Station nextStation) {
        this.nextStation = nextStation;
    }

    public void addSubStationsMap(Station station, Integer distance) {
        subStationsMap.put(station, distance);
    }

    public void delSubStationsMap(Station station) {
        subStationsMap.remove(station);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getLineId() {
        return (id >> 16) & 0XFF;
    }

    public void addLineId(int lineId) {

        if (lineIdList.isEmpty()) {
            lineIdList.add(getLineId());
        }

        if (!lineIdList.contains(lineId)) {
            lineIdList.add(lineId);
        }
    }

    public List<Integer> getLineIdList() {
        if (lineIdList.isEmpty()) {
            lineIdList.add(getLineId());
        }
        return lineIdList;
    }

    public void setSubStationsMap(Map<Station, Integer> subStationsMap) {
        this.subStationsMap = subStationsMap;
    }

    public void setLineIdList(List<Integer> lineIdList) {
        this.lineIdList = lineIdList;
    }

    public List<Shoumoche> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<Shoumoche> timeList) {
        this.timeList = timeList;
    }

    public String getShoumoche() {
        String values = "";

        List<Shoumoche> shoumoches = getTimeList();
        for (int i = 0; i < shoumoches.size(); i++) {
            Shoumoche shoumoche = shoumoches.get(i);
            values += shoumoche.getName() + "：";
            values += "首车 " + shoumoche.getFirst();
            if (shoumoche.getLast() == null) {
                values += "\n";
            } else {
                values += "，末车 " + shoumoche.getLast() + "\n";
            }
        }

        return values;
    }
}
