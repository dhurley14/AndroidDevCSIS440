package com.example.todolist;

import java.util.ArrayList;

public class Card implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private ArrayList<Double> prices;
	private ArrayList<String> dates;
	public Card(String name) {
		this.name=name;
	    prices=new ArrayList<Double>();
	    dates=new ArrayList<String>();
	}
	public String getName() {
		return name;
	}
	public void addPrice(double t) {
		prices.add(t);
	}
	public void addDate(String t) {
		dates.add(t);
	}
	public void remove(int i) {
		prices.remove(i);
		dates.remove(i);
	}
	public ArrayList<Double> getPrices() {
		return prices;
	}
	public ArrayList<String> getDates() {
		return dates;
	}
	public Double getCurrentPrice() {
		return prices.get(prices.size()-1);
	}
	public double getAverage() {
		double average=0;
		for(int i =0;i<prices.size();i++) {
			average=average+prices.get(i);
		}
		average=average/prices.size();
		return average;
	}
	public double getMin() {
		double min=prices.get(0);
		for(int i =0;i<prices.size();i++) {
			if(prices.get(i)<min) {
				min=prices.get(i);
			}
		}
		return min;
	}
	public double getMax() {
		double max=prices.get(0);
		for(int i =0;i<prices.size();i++) {
			if(prices.get(i)>max) {
				max=prices.get(i);
			}
		}
		return max;
	}
}
