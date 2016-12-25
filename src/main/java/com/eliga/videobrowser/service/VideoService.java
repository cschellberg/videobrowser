package com.eliga.videobrowser.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;

@Service
@Configuration
@PropertySources(value = { @PropertySource("classpath:application.properties") })
public class VideoService {

	// @Value("#{base.video.dir}")
	private String baseVideoDir = "/videobrowser/videos";

	public String saveVideoToDisk(String channelName, String name, String fileName, InputStream inputStream)
			throws IOException {
		FileOutputStream fis = null;
		try {
			Path path = Paths.get(baseVideoDir, channelName.toLowerCase().replace(' ', '_'),
					name.toLowerCase().replace(' ', '_'), fileName.replace(' ', '_'));
			path.getParent().toFile().mkdirs();
			fis = new FileOutputStream(path.toFile());
			IOUtils.copy(inputStream, fis);
			return path.toString();
		} finally {
			inputStream.close();
			fis.close();
		}
	}

}
