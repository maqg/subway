package com.zz.henry.subwaytool.findway;

import com.zz.henry.subwaytool.Line;
import com.zz.henry.subwaytool.LineUtils;
import com.zz.henry.subwaytool.Station;
import com.zz.henry.subwaytool.common.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by henry on 2015/1/12.
 */
public class QueryResult {

    private int price = 0;
    private int distance = 1;
    private int extraDistance = 0; // Plan Line Distance
    private int minutes = 0;
    private int payMonth = 0;
    private int payYear = 0;

    private List<Station> preStationList;
    private List<Integer> exchangeList = new ArrayList<>();

    private Station start;
    private Station end;
    private Subway subway;

    List<Integer> specialStationList = new ArrayList<>();

    public QueryResult() {

        subway = new Subway();

        specialStationList.add(Constants.STATION_ID_SIHUI_LINE1);
        specialStationList.add(Constants.STATION_ID_SIHUI_LINE8TONG);
        specialStationList.add(Constants.STATION_ID_SIHUIDONG_LINE1);
        specialStationList.add(Constants.STATION_ID_SIHUIDONG_LINE8TONG);
    }

    boolean isSpecialStation(Integer stationId) {
        return specialStationList.contains(stationId);
    }

    public int getMinutes() {
        int seconds = ((distance + 500 - 1) / 1000) * 90;
        seconds += (preStationList.size() - 1) * 32;
        return (seconds + 30 - 1) / 60 + exchangeList.size() * 5;
    }

    public boolean isInTheSameLine(int stationId) {

        Station stationStart = preStationList.get(stationId - 1);
        Station stationEnd = preStationList.get(stationId + 1);
        Station thisStation = preStationList.get(stationId);

        List<Integer> list1 = stationStart.getLineIdList();
        List<Integer> list2 = stationEnd.getLineIdList();
        boolean inSameLine = false;

        for (Integer lineId : list1) {
            if (list2.contains(lineId)) {
                inSameLine = true;

                // to process special case
                if (isSpecialStation(thisStation.getId())) {
                    if (stationId != 1 && stationId != preStationList.size() - 2 && !isExchangeStation(stationId - 1)) {
                        inSameLine = false;
                    }
                }
                break;
            }
        }

        return inSameLine;
    }

    public void calculatePayMore() {

        payMonth = 100;

        // suppose 24 days for every month.
        int total = price * 24 * 2;

        if (total - 150 > 0) {
            payMonth += (total - 150) * 0.5;
            total = (total - (total - 150));// 150
        }

        if (total - 100 > 0) {
            payMonth += (total - 100) * 0.8;
        }

        payYear = 12 * payMonth;
    }

    public void calculateFee() {

        if (distance > 32000) {
            price = 6 + (distance - 32000 + 20000 - 1) / 20000;
        } else if (distance > 12000) {
            price = 4 + (distance - 12000 + 10000 - 1) / 10000;
        } else if (distance == 0) {
            price = 3;
        } else {
            price = 2 + (distance + 6000 - 1) / 6000;
        }

        calculatePayMore();
    }

    public int getExtraDistance() {
        return extraDistance;
    }

    public int getPrice() {
        return price;
    }

    public int getDistance() {
        return distance;
    }

    public List<Station> getPreStationList() {
        return preStationList;
    }

    public void initExchangeList() {
        exchangeList.clear();
        for (int i = 0; i < preStationList.size(); i++) {
            if (i == 0) {
                continue;
            } else {
                if (i < preStationList.size() - 1 && !isInTheSameLine(i)) {
                    exchangeList.add(i);
                }
            }
        }
    }

    public boolean isExchangeStation(Integer stationId) {
        if (stationId == 0) {
            return false;
        }
        return exchangeList.contains(stationId);
    }

    public String getStationListStr() {
        String nameList = "";

        for (int i = 0; i < preStationList.size(); i++) {
            if (i == 0) {
                nameList += preStationList.get(i).getName();
            } else {
                nameList += "->" + preStationList.get(i).getName();
                if (isExchangeStation(i)) {
                    Line line = getExchangeLine(i);
                    nameList += "（换乘" + line.getName() + "）";
                }
            }
        }

        return nameList;
    }

    public Line getExchangeLine(int stationId) {
        List<Integer> list1 = preStationList.get(stationId - 1).getLineIdList();
        List<Integer> list2 = preStationList.get(stationId + 1).getLineIdList();
        List<Integer> listBetween = preStationList.get(stationId).getLineIdList();

        for (Integer lineId : listBetween) {
            if (!list1.contains(lineId) && list2.contains(lineId)) {
                return LineUtils.getInstance().getLine(lineId);
            }
        }

        return LineUtils.getInstance().getLine(stationId);
    }

    public int getPayMonth() {
        return payMonth;
    }

    public void setExtraDistance() {
        extraDistance = 0;
    }

    public int getPayYear() {
        return payYear;
    }

    public int dijkstraQuery(String startName, String endName) {
        start = subway.stationListMap.get(startName);
        end = subway.stationListMap.get(endName);

        subway.dijkstra(start.getPosition());

        distance = subway.getDistance(end);
        preStationList = subway.getPathStationList(end);
        initExchangeList();

        setExtraDistance();

        calculateFee();

        return 0;
    }
}
