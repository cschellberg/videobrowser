package com.eliga.videobrowser.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eliga.videobrowser.model.Channel;
import com.eliga.videobrowser.model.Result;
import com.eliga.videobrowser.model.Video;
import com.eliga.videobrowser.repository.ChannelRepository;
import com.eliga.videobrowser.service.VideoService;

@Controller
public class ChannelController {

	private static Logger logger = LoggerFactory.getLogger(ChannelController.class);
	@Autowired
	ChannelRepository channelRepository;

	@Autowired
	VideoService videoService;

	@RequestMapping(value = "/admin/channel", method = RequestMethod.POST)
	public @ResponseBody Result saveChannel(@RequestBody Channel channel) throws Exception {
		try {
			channelRepository.save(channel);
			return new Result(0, "success");
		} catch (Exception ex) {
			return new Result(1, ex.getMessage());
		}
	}

	@PostMapping(value = "/admin/channel/addVideo")
	public String addVideoToChannel(@RequestParam("channelName") String channelName, @RequestParam("name") String name,
			@RequestParam("videoFile") MultipartFile videoFile) throws Exception {
		String errorMessage = "";
		try {
			String link = videoService.saveVideoToDisk(channelName, name, videoFile.getOriginalFilename(),
					videoFile.getInputStream());
			Video video = new Video();
			video.setName(name);
			video.setLink(link);
			Channel channel = channelRepository.findByName(channelName);
			if (channel != null) {
				channel.getVideos().add(video);
				channelRepository.save(channel);
			} else {
				errorMessage = "&errorMessage=" + URLEncoder.encode(channelName + "is not found");
			}
		} catch (Exception ex) {
			errorMessage = "&errorMessage=" + URLEncoder.encode(ex.getMessage().replace(File.separator, "_"));
		}
		return "redirect:/channel.html?view=channel_list" + errorMessage;
	}

	@RequestMapping(value = "/channel/list", method = RequestMethod.GET)
	public @ResponseBody List<Channel> list() {
		List<Channel> retList = new ArrayList<Channel>();
		Iterator<Channel> it = channelRepository.findAll().iterator();
		while (it.hasNext()) {
			retList.add(it.next());
		}
		return retList;
	}

	@RequestMapping(value = "/channel/{channelName:.+}", method = RequestMethod.GET)
	public @ResponseBody Channel getChannel(@PathVariable("channelName") String channelName) {
		Channel channel = new Channel();
		channel = channelRepository.findByName(channelName);
		return channel;
	}

	@RequestMapping(value = "/admin/channel/delete/{name:.+}", method = RequestMethod.DELETE)
	public @ResponseBody Result deleteChannel(@PathVariable("name") String channelName) {
		Channel channel = channelRepository.findByName(channelName);
		if (channel != null && channel.getVideos().size() == 0) {
			channelRepository.delete(channel);
			return new Result(0, "success");
		} else {
			if (channel == null) {
				return new Result(1, "channel not found");
			} else if (channel.getVideos().size() > 0) {
				return new Result(1, "cannot delete channel that has videos.  You must remove the videos first");
			} else {
				return new Result(1, "failure");
			}
		}
	}

	@RequestMapping(value = "/admin/channel/deleteVideo/{channelName:.+}/{video:.+}", method = RequestMethod.DELETE)
	public @ResponseBody Result deleteVideo(@PathVariable("channelName") String channelName,
			@PathVariable("video") String videoName) {
		Channel channel = channelRepository.findByName(channelName);
		if (channel != null) {
			for (Video video : channel.getVideos()) {
				if (videoName.equals(video.getName())) {
					channel.getVideos().remove(video);
					channelRepository.save(channel);
					return new Result(0, "success");
				}
			}
			return new Result(1, "video not found");
		} else {
			return new Result(1, "channel not found");
		}
	}

	@RequestMapping(value = "/channel/getVideo/{channelName:.+}/{video:.+}", method = RequestMethod.GET)
	@ResponseBody
	public void getVideo(@PathVariable("channelName") String channelName, @PathVariable("video") String videoName,
			HttpServletResponse response) {
		try {
			Channel channel = channelRepository.findByName(channelName);
			if (channel != null) {
				Video video = channel.getVideo(videoName);
				if (video != null) {
					File file = new File(video.getLink());
					response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
					response.setHeader("Content-Disposition",
							"attachment; filename=" + file.getName().replace(" ", "_"));
					InputStream iStream = new FileInputStream(file);
					IOUtils.copy(iStream, response.getOutputStream());
					response.flushBuffer();
				}
			}
		} catch (NoSuchFileException e) {
			response.setStatus(404);
		} catch (Exception e) {
			response.setStatus(500);
		}
	}

}
