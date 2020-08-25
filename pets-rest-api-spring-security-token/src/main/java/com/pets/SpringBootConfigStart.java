package com.pets;

import java.util.ArrayList;
import java.util.Arrays;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.pets.dao.entity.Role;
import com.pets.dao.entity.User;
import com.pets.service.impl.UserService;

@SpringBootApplication
public class SpringBootConfigStart implements CommandLineRunner{
	

    @Autowired
    private UserService userService;
    
    
    //This is used to create bean with java based configuration
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
     
	public static void main(String[] args) {
		System.out.println("Starting Spring project as Boot!!!!!!!!!!!!!!");
		SpringApplication.run(SpringBootConfigStart.class, args);
	}
	
	 @Override
	    public void run(String... args) throws Exception {
	        try {
	            User admin = new User();
	            admin.setUsername("admin");
	            admin.setPassword("admin");
	            admin.setEmail("admin@email.com");
	            admin.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_ADMIN)));

	            userService.signup(admin);

	            User client = new User();
	            client.setUsername("client");
	            client.setPassword("client");
	            client.setEmail("client@email.com");
	            client.setRoles(new ArrayList<>(Arrays.asList(Role.ROLE_CLIENT)));
	            userService.signup(client);
	        }catch (Exception ex){
	            ex.printStackTrace();
	        }
	    }
	
}
