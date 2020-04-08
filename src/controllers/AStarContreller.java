package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import models.Station;
import net.datastructures.Entry;
import net.datastructures.PriorityQueue;
import net.datastructures.SortedListPriorityQueue;


public class AStarContreller {
	
	private DataController dataMap;
	private PriorityQueue<Double, Station> openList;
	private ArrayList<Station> closeList;
	private Station currentStation;
	
	public AStarContreller(DataController map){
		this.dataMap = map;
	}
	
	public ArrayList<String[]> calculate(String src_station, String dst_station){
		
		ArrayList<Station> src_stations = this.dataMap.getStationsByName(src_station);
		ArrayList<Station> dst_stations = this.dataMap.getStationsByName(dst_station);
		
		AStartResult final_a_star_result = new AStartResult(Math.pow(10.0, 6), null);
		for (Station source_station : src_stations) {
			for (Station destination_station : dst_stations) {
				AStartResult a_star_result = this.executeAStar(source_station, destination_station);
				if(a_star_result.time < final_a_star_result.time){
					final_a_star_result = a_star_result;
				}
			}
		}
		
		ArrayList<String[]> route = new ArrayList<String[]>();
		
		Integer total_time = 0;
		for (Station station : final_a_star_result.route) {
			Double time = 0.0, min = 0.0, seg = 0.0;
			if (station.getParentStation() != null){
				time = station.time(station.getParentStation());
			}
			min = time/60; seg = time%60; total_time += min.intValue()*60 + seg.intValue(); 
			String estacion = station.getLineStation();
			switch (estacion) {
			case "A":
				estacion = "1";
				break;
			case "B":
				estacion = "2";
				break;
			case "C":
				estacion = "3";
				break;
			case "D":
				estacion = "4";
				break;
			case "E":
				estacion = "5";
				break;
			case "F":
				estacion = "6";
				break;
			case "G":
				estacion = "7";
				break;
			case "H":
				estacion = "8";
				break;
			case "I":
				estacion = "9";
				break;
			case "J":
				estacion = "10";
				break;
			case "K":
				estacion = "11";
				break;
			case "L":
				estacion = "12";
				break;
			default:
				estacion = "-1";
				break;
			}
			route.add(new String[]{estacion, station.getNameStation(), String.format("%02d min %02d seg", min.intValue(), seg.intValue())});
			station.setParentStation(null);
		}
		
		route.add(new String[]{String.format("%02d horas %02d minutos %02d segundos", total_time/3600, (total_time%3600)/60, (total_time%3600)%60)});
		
		return route;
	}
	
	private AStartResult executeAStar(Station src_station, Station dst_station){
		src_station.setParentStation(null);
		dst_station.setParentStation(null);
		
		this.openList = new SortedListPriorityQueue<Double, Station>();
		this.closeList = new ArrayList<Station>();
		this.currentStation = src_station;
		
		Station last_station = null;
		double current_f = 0;
		
		double timeGx = 0;
		double timeG = this.currentStation.time(src_station);
		double timeH = this.currentStation.time(dst_station);
		
		this.openList.insert(timeG+timeH, this.currentStation);
		
		while (!this.openList.isEmpty()) {
			last_station = this.currentStation;
			
			Entry<Double, Station> f_station = this.openList.removeMin();
			this.currentStation = f_station.getValue();
			current_f = f_station.getKey();
			
			while(this.closeList.contains(this.currentStation)){
				f_station = this.openList.removeMin();
				this.currentStation = f_station.getValue();
				current_f = f_station.getKey();
			}
			
			this.closeList.add(this.currentStation);
			if (this.currentStation.getNameStation().equals(dst_station.getNameStation())){
				break;
			}
			
			timeG = current_f - this.currentStation.time(dst_station);
			
			Station prev_station = this.currentStation.getPrevStation();
			if (prev_station != null && !prev_station.equals(last_station) && !this.closeList.contains(prev_station)) {
				timeGx = timeG + this.currentStation.time(prev_station);
				timeH = prev_station.time(dst_station);
				this.updateOpenList(timeGx + timeH, prev_station);
			}
			
			Station next_station = this.currentStation.getNextStation();
			if (next_station != null && !next_station.equals(last_station) && !this.closeList.contains(next_station)) {
				timeGx = timeG + this.currentStation.time(next_station);
				timeH = next_station.time(dst_station);
				this.updateOpenList(timeGx + timeH, next_station);
			} 
			
			Map<String, Integer> current_transfer_station = this.currentStation.getTransferStation();
			if(current_transfer_station != null){
				for (String line : this.dataMap.getMapNameStation().get(this.currentStation.getNameStation())) {
					Station transfer_station = this.dataMap.getStation(line, this.currentStation.getNameStation());
					if (!this.currentStation.equals(transfer_station) && !last_station.equals(transfer_station) && !this.closeList.contains(transfer_station)){
						timeGx = timeG + this.currentStation.time(transfer_station);
						timeH = this.currentStation.time(dst_station);
						this.updateOpenList(timeGx + timeH, transfer_station);
					}
				}
			}
		}
		
		ArrayList<Station> route = new ArrayList<Station>();
		do {
			route.add(this.currentStation);
			this.currentStation = this.currentStation.getParentStation();
		} while (this.currentStation != null);
		Collections.reverse(route);
		
		return new AStartResult(timeG, route);
	}

	private void updateOpenList(Double f, Station station){
		PriorityQueue<Double, Station> aux_open_list = new SortedListPriorityQueue<Double, Station>();
		boolean inserted = false;
		
		while(!this.openList.isEmpty()){
			Entry<Double, Station> f_station = this.openList.removeMin();
			if(f_station.getValue().equals(station)){
				inserted = true;
				if (f_station.getKey() > f){
					station.setParentStation(this.currentStation);
					aux_open_list.insert(f, station);
				} else{
					aux_open_list.insert(f_station.getKey(), f_station.getValue());
				}
			}else{
				aux_open_list.insert(f_station.getKey(), f_station.getValue());
			}
		}
		
		if (!inserted){
			station.setParentStation(this.currentStation);
			aux_open_list.insert(f, station);
		}
		
		this.openList = aux_open_list;
	}
}


class AStartResult {
	
	public Double time;
	public ArrayList<Station> route;
	
	public AStartResult(Double time, ArrayList<Station> route){
		this.time = time;
		this.route = route;
	}
}
