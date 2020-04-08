package models;

import java.util.Map;

public class Station {

	private static double SPEED = 0.5;

	private String lineStation;
	private double xStation;
	private double yStation;
	private String nameStation;
	private Map<String, Integer> transferStation;
	private Station prevStation;
	private Station nextStation;
	
	private Station parentStation;

	public Station(String linea, String nombre) {
		lineStation = linea;
		nameStation = nombre;
		transferStation = null;
	}

	public String getLineStation() {
		return lineStation;
	}

	public void setLineStation(String lineStation) {
		this.lineStation = lineStation;
	}

	public double getxStation() {
		return xStation;
	}

	public void setxStation(double xStation) {
		this.xStation = xStation;
	}

	public double getyStation() {
		return yStation;
	}

	public void setyStation(double yStation) {
		this.yStation = yStation;
	}

	public String getNameStation() {
		return nameStation;
	}

	public void setNameStation(String nameStation) {
		this.nameStation = nameStation;
	}

	public Map<String, Integer> getTransferStation() {
		return transferStation;
	}

	public void setTransferStation(Map<String, Integer> transferStation) {
		this.transferStation = transferStation;
	}

	public Station getPrevStation() {
		return prevStation;
	}

	public void setPrevStation(Station prevStation) {
		this.prevStation = prevStation;
	}

	public Station getNextStation() {
		return nextStation;
	}

	public void setNextStation(Station nextStation) {
		this.nextStation = nextStation;
	}

	public Station getParentStation() {
		return parentStation;
	}

	public void setParentStation(Station parentStation) {
		this.parentStation = parentStation;
	}
	
	public double time(Station dstStation) {
		double time = 0;
		if (this.transferStation != null && !this.equals(dstStation) && this.nameStation.equals(dstStation.getNameStation())){
			time = 90 * this.transferStation.get(dstStation.getLineStation());
		} else if(!this.nameStation.equals(dstStation.getNameStation())) {
			time = 6000 * (Math.sqrt(Math.pow((dstStation.getxStation() - this.getxStation()), 2) + Math.pow((dstStation.getyStation() - this.getyStation()), 2))) / SPEED;
		}
		return time;
	}

	public boolean equals(Station station) {
		return this.lineStation.equals(station.getLineStation()) && this.nameStation.equals(station.getNameStation());
	}

	@Override
	public String toString() {
		return "<" + this.lineStation + ":" + this.nameStation + ">";
	}

}
