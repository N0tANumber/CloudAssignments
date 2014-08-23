package org.magnum.dataup;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Collection;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.springframework.stereotype.Controller;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;

@Controller
public class SpringController implements VideoSvcApi{

	private Collection<Video> videos = new CopyOnWriteArrayList<Video>();

	@Override
	@GET("/video")
	public Collection<org.magnum.dataup.model.Video> getVideoList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@POST("/video")
	public org.magnum.dataup.model.Video addVideo(
			@Body org.magnum.dataup.model.Video v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Multipart
	@POST("/video/{id}/data")
	public VideoStatus setVideoData(@Path("id") long id,
			@Part("data") TypedFile videoData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Streaming
	@GET("/video/{id}/data")
	Response getData(@Path("id") long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
