package com.eliga.videobrowser.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eliga.videobrowser.model.Channel;
import com.eliga.videobrowser.model.User;

@Repository
public interface ChannelRepository extends CrudRepository<Channel, Long> {
	Channel findByName(String name);
}
