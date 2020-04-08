package controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import launcher.Launcher;
import models.Station;

public class DataController {
	
	private static String FILENAME = "/resources/estaciones.csv";
	
	private Map<String, Station> mapLines;
	private Map<String, ArrayList<String>> mapNameStation;
	private ArrayList<String> listNameStation;
	
	public DataController() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(Launcher.class.getResourceAsStream(FILENAME)))) {

			this.mapLines = new HashMap<String, Station>();
			this.mapNameStation = new HashMap<String, ArrayList<String>>();
			this.listNameStation = new ArrayList<String>();
			
			String metro_line = "-";
			Station current_station = null;
			Station prev_station = null;

			String line = br.readLine();
			while (line != null) {
				current_station = this.createStation(line);
				this.updateDataLists(current_station);
				
				if ( !metro_line.equals(current_station.getLineStation())){
					prev_station = null;
					current_station.setPrevStation(prev_station);
					current_station.setNextStation(null);
					prev_station = current_station;
					
					metro_line = current_station.getLineStation();
					this.mapLines.put(metro_line, current_station);
					
					line = br.readLine();
					current_station = this.createStation(line);
					this.updateDataLists(current_station);
				}
				prev_station.setNextStation(current_station);
				current_station.setPrevStation(prev_station);
				
				prev_station = current_station;
				
				line = br.readLine();
			}
			Collections.sort(this.listNameStation);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public Map<String, Station> getMapLines() {
		return mapLines;
	}

	public Map<String, ArrayList<String>> getMapNameStation() {
		return mapNameStation;
	}

	public ArrayList<String> getListNameStation() {
		return listNameStation;
	}
	
	private Station createStation(String line) {
		String[] line_parts = line.split(";");

		Station current_station = new Station(line_parts[0], line_parts[1]);
		current_station.setxStation(Double.parseDouble(line_parts[2]));
		current_station.setyStation(Double.parseDouble(line_parts[3]));

		if (line_parts.length > 4) {
			String[] transfers = line_parts[4].split("-");
			Map<String, Integer> transfer_station = new HashMap<String, Integer>();
			for (int i = 0; i < transfers.length; i++) {
				if (!transfers[i].equals("0")) {
					transfer_station.put(Character.toString((char) (65 + i)), Integer.parseInt(transfers[i]));
				}
			}
			current_station.setTransferStation(transfer_station);
		}
		
		return current_station;
	}
	
	private void updateDataLists(Station current_station){
		if (!this.listNameStation.contains(current_station.getNameStation())){
			ArrayList<String> station_lines = new ArrayList<String>();
			station_lines.add(current_station.getLineStation());
			this.mapNameStation.put(current_station.getNameStation(), station_lines);
			this.listNameStation.add(current_station.getNameStation());
		} else {
			ArrayList<String> station_lines = this.mapNameStation.get(current_station.getNameStation());
			station_lines.add(current_station.getLineStation());
			this.mapNameStation.put(current_station.getNameStation(), station_lines);
		}
	}
	
	public ArrayList<Station> getStationsByName(String station_name){
		ArrayList<Station> station_list = new ArrayList<Station>();
		
		ArrayList<String> station_lines = this.mapNameStation.get(station_name);
		for (String line_station : station_lines) {
			Station current_station = this.mapLines.get(line_station);
			while(!current_station.getNameStation().equals(station_name)){
				current_station = current_station.getNextStation();
			}
			station_list.add(current_station);
		}
		return station_list;
	}
	
	public Station getStation(String line, String station_name){
		Station current_station = this.mapLines.get(line);
		while(!current_station.getNameStation().equals(station_name)){
			current_station = current_station.getNextStation();
		}
		return current_station;
	}
}
