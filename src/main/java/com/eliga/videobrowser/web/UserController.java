package com.eliga.videobrowser.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eliga.videobrowser.model.Menu;
import com.eliga.videobrowser.model.Result;
import com.eliga.videobrowser.model.User;
import com.eliga.videobrowser.repository.UserRepository;
import com.eliga.videobrowser.types.ROLE;

@Controller
public class UserController {
 @Autowired
 UserRepository userRepository;
	
	@RequestMapping(value = "/user/{username:.+}", method = RequestMethod.GET)
	public @ResponseBody User getUser(@PathVariable("username") String username) {
		User user=userRepository.findByUsername(username);
		return user;
	}
	

	@RequestMapping(value = "/admin/user", method = RequestMethod.POST)
	public @ResponseBody Result saveUser(@RequestBody User user) throws Exception {
		try
		{
		validate(user);
		userRepository.save(user);
		return new Result(0,"success");
		}catch(Exception ex){
			return new Result(1,ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/getLoggedInUser", method = RequestMethod.GET)
	public @ResponseBody User getLoggedInUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			return userRepository.findByUsername(auth.getName());
		} else {
			return new User();
		}
	}

	
	@RequestMapping(value = "/reg", method = RequestMethod.POST)
	public @ResponseBody Result registerUser(@RequestBody User user) throws Exception {
		return saveUser(user);
	}
	private void validate(User user) throws Exception {
		if ( StringUtils.isBlank(user.getFirstName())){
			throw new Exception("First Name is required");
		}
		if ( StringUtils.isBlank(user.getLastName())){
			throw new Exception("Last Name is required");
		}
		if ( StringUtils.isBlank(user.getUsername())){
			throw new Exception("Username is required");
		}
		if ( StringUtils.isBlank(user.getPassword())){
			throw new Exception("password is required");
		}
		if ( StringUtils.isBlank(user.getRole())){
			user.setRole(ROLE.user.toString());
		}

	}


	@RequestMapping(value = "/admin/user/delete/{username:.+}", method = RequestMethod.DELETE)
	public @ResponseBody Result deleteUser(@PathVariable("username") String username) {
		User user=userRepository.findByUsername(username);
		if ( user != null){
		userRepository.delete(user);
		return new Result(0,"success");
		}else {		
		return new Result(1,"No user found for "+username);
		}
	}

	@RequestMapping(value = "/admin/user/deleteAll", method = RequestMethod.GET)
	public @ResponseBody String deleteAll() {
		userRepository.deleteAll();
		long count=userRepository.count();
		return "success "+count;
	}

	@RequestMapping(value = "/admin/user/list", method = RequestMethod.GET)
	public @ResponseBody List<User> list() {
		List<User> retList=new ArrayList<User>();
		Iterator<User> it=userRepository.findAll().iterator();
		while (it.hasNext()){
			retList.add(it.next());
		}
		return retList;
	}
	


}
