package com.eliga.videobrowser.web;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eliga.videobrowser.model.Menu;
import com.eliga.videobrowser.model.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.eliga.videobrowser.types.ROLE;

@Controller
public class MenuController {

	private final static String MENU_FILE = "/menu/menu.json";
	private Map<ROLE, Menu> menuMap = new HashMap<ROLE, Menu>();

	public MenuController() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = IOUtils.toString(MenuController.class.getResourceAsStream(MENU_FILE),
				Charset.defaultCharset());
		Menu mainMenu = mapper.readValue(jsonStr, Menu.class);
		for (ROLE role : ROLE.values()) {
			Menu roleMenu = new Menu("main");
			setRoleMenu(role, mainMenu, roleMenu);
			menuMap.put(role, roleMenu);
		}
	}

	private void setRoleMenu(ROLE role, Menu menu, Menu roleMenu) {
		for (Menu subMenu : menu.getSubMenus()) {
			if (subMenu.getRoles().contains(role.toString())) {
				Menu roleSubmenu = new Menu(subMenu.getName(), subMenu.getLink(), role.toString());
				roleMenu.getSubMenus().add(roleSubmenu);
				if (subMenu.getSubMenus().size() > 0) {
					setRoleMenu(role, subMenu, roleSubmenu);
				}
			}
		}
	}

	@RequestMapping(value = "/menu", method = RequestMethod.GET)
	public @ResponseBody Menu getMenu() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			GrantedAuthority grant = auth.getAuthorities().iterator().next();
			ROLE role = ROLE.valueOf(grant.getAuthority());
			return menuMap.get(role);
		} else {
			return menuMap.get(ROLE.none);
		}
	}

	@RequestMapping(value = "/anonymousMenu", method = RequestMethod.GET)
	public @ResponseBody Menu getAnonymousMenu() {
		return menuMap.get(ROLE.none);
	}
}
