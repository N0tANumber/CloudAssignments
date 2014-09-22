/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.magnum.mobilecloud.video;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
public class AnEmptyController {

	/**
	 * You will need to create one or more Spring controllers to fulfill the
	 * requirements of the assignment. If you use this file, please rename it to
	 * something other than "AnEmptyController"
	 * 
	 * 
	 * ________ ________ ________ ________ ___ ___ ___ ________ ___ __ |\
	 * ____\|\ __ \|\ __ \|\ ___ \ |\ \ |\ \|\ \|\ ____\|\ \|\ \ \ \ \___|\ \
	 * \|\ \ \ \|\ \ \ \_|\ \ \ \ \ \ \ \\\ \ \ \___|\ \ \/ /|_ \ \ \ __\ \ \\\
	 * \ \ \\\ \ \ \ \\ \ \ \ \ \ \ \\\ \ \ \ \ \ ___ \ \ \ \|\ \ \ \\\ \ \ \\\
	 * \ \ \_\\ \ \ \ \____\ \ \\\ \ \ \____\ \ \\ \ \ \ \_______\ \_______\
	 * \_______\ \_______\ \ \_______\ \_______\ \_______\ \__\\ \__\
	 * \|_______|\|_______|\|_______|\|_______|
	 * \|_______|\|_______|\|_______|\|__| \|__|
	 * 
	 * 
	 */

	@RequestMapping(value = "/go", method = RequestMethod.GET)
	public @ResponseBody
	String goodLuck() {
		return "Good Luck!";
	}

	// Using Spring @Autowired to implement VideoRepository.java
	@Autowired
	private VideoRepository videos;

	// Use the save method of VideoRepository class to save video object into
	// the Repository
	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.POST)
	public @ResponseBody
	Video addVideo(@RequestBody Video v) {
		// Initiate likes to 0 using the setter method of Video.java
		v.setLikes(0);
		videos.save(v);
		return v;
	}

	// Receives GET requests to /video and returns the current
	// list of videos in memory. Spring automatically converts
	// the list of videos to JSON because of the @ResponseBody
	// annotation.
	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody
	Collection<Video> getVideoList() {
		return Lists.newArrayList(videos.findAll());
	}

	// Receives GET request to /video + id and returns the video associated with
	// that id
	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Video getVideoById(@PathVariable("id") long id, HttpServletResponse response) {

		if (videos.findOne(id) != null) {
			return videos.findOne(id);
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}

		return null;
	}

	// Return video by Name
	@RequestMapping(value = VideoSvcApi.VIDEO_TITLE_SEARCH_PATH, method = RequestMethod.GET)
	public @ResponseBody
	Collection<Video> findByTitle(
	// Tell Spring to use the "title" parameter in the HTTP request's query
	// string as the value for the title method parameter
			@RequestParam(VideoSvcApi.TITLE_PARAMETER) String title) {
		return videos.findByName(title);
	}

	// Return video by duration less than var duration
	@RequestMapping(value = VideoSvcApi.VIDEO_DURATION_SEARCH_PATH, method = RequestMethod.GET)
	public @ResponseBody
	Collection<Video> findByDurationLessThan(
			@RequestParam(VideoSvcApi.DURATION_PARAMETER) long duration) {
		return videos.findByDurationLessThan(duration);
	}

	// Post video likes
	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/like", method = RequestMethod.POST)
	public @ResponseBody
	void likeVideo(@PathVariable("id") long id, Principal p,
			HttpServletResponse response) {

		if (videos.findOne(id) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}else{
			
			Video v = videos.findOne(id);
			Set<String> likesUsernames = v.getLikesUsernames();

			if (likesUsernames.contains(p.getName())) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else {
				likesUsernames.add(p.getName());
				v.setLikesUsernames(likesUsernames);
				v.setLikes(likesUsernames.size());
				videos.save(v);
				response.setStatus(HttpServletResponse.SC_OK);
			}
		}
	}

	// POST video unlikes
	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/unlike", method = RequestMethod.POST)
	public @ResponseBody
	void unlikeVideo(@PathVariable("id") long id, Principal p,
			HttpServletResponse response) {

		if (videos.findOne(id) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}

		Video v = videos.findOne(id);
		Set<String> likesUsernames = v.getLikesUsernames();

		if (!likesUsernames.contains(p.getName())) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			likesUsernames.remove(p.getName());
			v.setLikesUsernames(likesUsernames);
			v.setLikes(likesUsernames.size());
			videos.save(v);
			response.setStatus(HttpServletResponse.SC_OK);
		}

	}

	// GET users who like video
	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/likedby", method = RequestMethod.GET)
	public @ResponseBody
	Collection<String> getUsersWhoLikedVideo(@PathVariable("id") long id,
			HttpServletResponse response) {
		if (videos.findOne(id) != null) {
			return videos.findOne(id).getLikesUsernames();
		}
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return null;
	}

}
