package com.eliga.videobrowser.repository;

import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.eliga.videobrowser.model.User;
import com.eliga.videobrowser.types.ROLE;
import com.google.gson.Gson;

public class VideoBrowserTester {

	public static void main(String[] args) {
		try {
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			CloseableHttpClient client = httpClientBuilder.build();

				HttpPost httpPost = new HttpPost("http://localhost:8080/vb/login");
			String loginStr="username=dschellberg&password=blah";
			HttpEntity entity = new StringEntity(loginStr);
			httpPost.setEntity(entity);
			HttpResponse response=client.execute(httpPost);
			System.out.println(IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset()));
			
			httpPost = new HttpPost("http://localhost:8080/vb/user");
			User user = new User();
			user.setFirstName("Don");
			user.setLastName("Schellberg");
			user.setUsername("dschellberg");
			user.setPassword("blah");
			user.setRole(ROLE.admin.toString());
			httpPost.addHeader("accept", "application/json");
			httpPost.addHeader("content-type", "application/json");
			Gson gson = new Gson();
			String jsonStr = gson.toJson(user);
			entity = new StringEntity(jsonStr);
			httpPost.setEntity(entity);
			response=client.execute(httpPost);
			System.out.println(IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset()));

			httpPost = new HttpPost("http://localhost:8080/vb/user");
			user = new User();
			user.setFirstName("Chris");
			user.setLastName("Schellberg");
			user.setUsername("cschellberg");
			user.setPassword("blah");
			user.setRole(ROLE.user.toString());
			httpPost.addHeader("accept", "application/json");
			httpPost.addHeader("content-type", "application/json");
			gson = new Gson();
			jsonStr = gson.toJson(user);
			entity = new StringEntity(jsonStr);
			httpPost.setEntity(entity);
			response=client.execute(httpPost);
			System.out.println(IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset()));

			String url = "http://localhost:8080/vb/user/list";
			HttpGet request = new HttpGet(url);
			request.addHeader("accept", "application/json");
			request.addHeader("content-type", "application/json");
			response = client.execute(request);
			System.out.println(IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset()));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
