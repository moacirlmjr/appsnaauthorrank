package br.com.ufpb.appsnaauthorrank.beans;

public class MyLink {

	static int edgeCount = 0;

	private int weight;
	private int id;

	public MyLink(int weight) {
		this.id = edgeCount++;
		this.weight = weight;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String toString() {
		return this.weight + "";
	}

}