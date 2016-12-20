package com.eliga.videobrowser.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eliga.videobrowser.model.Channel;
import com.eliga.videobrowser.model.Result;
import com.eliga.videobrowser.model.User;
import com.eliga.videobrowser.repository.ChannelRepository;
import com.eliga.videobrowser.repository.UserRepository;

@Controller
public class ChannelController {

	@Autowired
	 ChannelRepository channelRepository;

	@RequestMapping(value = "/admin/channel", method = RequestMethod.POST)
	public @ResponseBody Result saveChannel(@RequestBody Channel channel) throws Exception {
		try
		{
		channelRepository.save(channel);
		return new Result(0,"success");
		}catch(Exception ex){
			return new Result(1,ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/channel/list", method = RequestMethod.GET)
	public @ResponseBody List<Channel> list() {
		List<Channel> retList=new ArrayList<Channel>();
		Iterator<Channel> it=channelRepository.findAll().iterator();
		while (it.hasNext()){
			retList.add(it.next());
		}
		return retList;
	}
}
