package com.pets.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pets.controller.vo.ApplicationMessageVO;
import com.pets.service.DogService;
import com.pets.vo.Dog;

@RestController // @Controller +@ResponseBody
public class DogController {
	
	@Autowired
	private DogService dogService;

	@GetMapping("/dogs/{did}")
  	public Dog findDog(@PathVariable int did) {
		Dog dog=dogService.findByid(did);
		return dog;
	}
	
	
	//dogs?sort=asc
	//dogs?sort=desc
	@GetMapping(value="/dogs")
  	public List<Dog> findSortedDogs(@RequestParam(value="sort",required=false) String sort) {
		List<Dog> dogs=dogService.findAll();
		if(sort!=null) {
			if("asc".equalsIgnoreCase(sort)) {
				Collections.sort(dogs,(Dog o1, Dog o2)-> {
						return o1.getPrice()-o2.getPrice();
					});
				
			}else {
				Collections.sort(dogs,(Dog o1, Dog o2)-> {
					return o2.getPrice()-o1.getPrice();
				});
			}
		}
		return dogs;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/dogs")
  	public ApplicationMessageVO uploadDog(@RequestBody Dog dog) {
		dogService.save(dog);
		ApplicationMessageVO applicationMessageVO=new ApplicationMessageVO();
		applicationMessageVO.setCode(12);
		applicationMessageVO.setMessage("Hey dog is uploaded successfully...............");
		return applicationMessageVO;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/dogs")
  	public ApplicationMessageVO updateDog(@RequestBody Dog dog) {
		dogService.save(dog);
		ApplicationMessageVO applicationMessageVO=new ApplicationMessageVO();
		applicationMessageVO.setCode(12);
		applicationMessageVO.setMessage("Hey dog is updated successfully...............");
		return applicationMessageVO;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/dogs/{did}")
  	public ApplicationMessageVO deleteDog(@PathVariable int did) {
		dogService.deleteById(did);
		ApplicationMessageVO applicationMessageVO=new ApplicationMessageVO();
		applicationMessageVO.setCode(did);
		applicationMessageVO.setMessage("Hey dog is deleted successfully...............");
		return applicationMessageVO;
	}

}
