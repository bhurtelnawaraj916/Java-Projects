package com.pets.vo;

public class Dog {

	private int did;
	private String color;
	private int price;
	private String breed;
	public int getDid() {
		return did;
	}
	public void setDid(int did) {
		this.did = did;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getBreed() {
		return breed;
	}
	public void setBreed(String breed) {
		this.breed = breed;
	}
	@Override
	public String toString() {
		return "Dog [did=" + did + ", color=" + color + ", price=" + price + ", breed=" + breed + "]";
	}
	
	
	
}
