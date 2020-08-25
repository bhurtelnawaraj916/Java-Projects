package com.pets.service;

import java.util.List;

import com.pets.vo.Dog;

public interface DogService {
	public String save(Dog dog);
	public Dog findByid(int did);
	public String deleteById(int did);
	public List<Dog> findAll();
	List<Dog> findByColor(String color);
	
}
