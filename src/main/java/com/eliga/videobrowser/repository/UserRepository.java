package com.eliga.videobrowser.repository;


import org.springframework.data.repository.CrudRepository;import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.eliga.videobrowser.model.User;

@Repository
public interface UserRepository  extends CrudRepository<User, Long>{

	User findByUsernameAndPassword(String username, String password);
	
	User findByUsername(String username);

}
