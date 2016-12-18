package com.eliga.videobrowser.model;

import java.util.ArrayList;
import java.util.List;

import com.eliga.videobrowser.types.ROLE;

public class Menu {
	
	private String name;
	private String link;
	private List<Menu> subMenus=new ArrayList<Menu>();
	private List<String> roles = new ArrayList<String>();
	
	public Menu(){
		
	}
	
	public Menu(String name){
		this.name = name;
	}
	
	public Menu(String name, String link, String role) {
		this.name=name;
		this.link=link;
		roles.add(role);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public List<Menu> getSubMenus() {
		return subMenus;
	}
	public void setSubMenus(List<Menu> subMenus) {
		this.subMenus = subMenus;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	

}
