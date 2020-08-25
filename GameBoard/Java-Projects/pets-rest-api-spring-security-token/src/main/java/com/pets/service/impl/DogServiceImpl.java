package com.pets.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.pets.dao.DogDaoRepository;
import com.pets.dao.entity.DogEntity;
import com.pets.service.DogService;
import com.pets.vo.Dog;

@Service
@Transactional
public class DogServiceImpl implements DogService {
	
	@Autowired
	private DogDaoRepository daoRepository;
	
	@Override
	public List<Dog> findByColor(String color) {
		Iterable<DogEntity> iterable=daoRepository.findByColor(color);
		List<Dog> dogs=new ArrayList<Dog>();
		for(DogEntity entity:iterable) {
			Dog dog=new Dog();
			BeanUtils.copyProperties(entity, dog);
			dogs.add(dog);
		}
		return dogs;
	}

	@Override
	public String save(Dog dog) {
		boolean bsbs=TransactionSynchronizationManager.isActualTransactionActive();
		System.out.println("Transaction Status = "+bsbs);
		DogEntity entity=new DogEntity();
		BeanUtils.copyProperties(dog, entity);
		daoRepository.save(entity);
		return "success";
	}

	@Override
	public Dog findByid(int did) {
		Optional<DogEntity> optional=daoRepository.findById(did);
		DogEntity entity=optional.get();
		Dog dog=new Dog();
		BeanUtils.copyProperties(entity, dog);
		return dog;
	}

	@Override
	public String deleteById(int did) {
		daoRepository.deleteById(did);
		return "success";
	}

	@Override
	public List<Dog> findAll() {
		Iterable<DogEntity> iterable=daoRepository.findAll();
		List<Dog> dogs=new ArrayList<Dog>();
		for(DogEntity entity:iterable) {
			Dog dog=new Dog();
			BeanUtils.copyProperties(entity, dog);
			dogs.add(dog);
		}
		return dogs;
	}
	
	

}
