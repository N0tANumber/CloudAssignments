package org.magnum.dataup;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import org.magnum.dataup.model.Video;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SpringController implements VideoSvcApi {
	
	private Collection<Video> videos= new CopyOnWriteArrayList<Video>();
	
	@RequestMapping(value=VIDEO_SVC_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Video> getVideoList(){
		return videos;
	}
	
	

}
