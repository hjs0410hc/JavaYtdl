package hyun;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Video {
	public ArrayList<Format> formatarr = new ArrayList<>();
	public int duration;
	public double average_rating;
	public String thumbnailurl;
	public String fulltitle;
	public String webpage_url;
	public String uploader;
	public String description;
	public String filename;
	
	Video(String jsonString){
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(jsonString);
		JsonArray formatsArray = jsonElement.getAsJsonObject().get("formats").getAsJsonArray();
		JsonObject target;
		for(int i=0;i<formatsArray.size();i++) {
			target = formatsArray.get(i).getAsJsonObject();
			try {
			formatarr.add(new Format(
					target.get("fps").toString(),
					target.get("format_note").toString(),
					target.get("ext").toString(),
					target.get("acodec").toString(),
					target.get("format_id").toString(),
					target.get("filesize").toString(),
					target.get("height").toString(),
					target.get("asr").toString(),
					target.get("width").toString(),
					target.get("vcodec").toString(),
					target.get("abr").toString()));
			}catch(Exception e) {
				formatarr.add(new Format(
						target.get("fps").toString(),
						target.get("format_note").toString(),
						target.get("ext").toString(),
						target.get("acodec").toString(),
						target.get("format_id").toString(),
						target.get("filesize").toString(),
						target.get("height").toString(),
						target.get("asr").toString(),
						target.get("width").toString(),
						target.get("vcodec").toString(),
						"null"));
			}
		}
		duration = Integer.parseInt(jsonElement.getAsJsonObject().get("duration").toString());
		average_rating = Double.parseDouble(jsonElement.getAsJsonObject().get("average_rating").toString());
		thumbnailurl = jsonElement.getAsJsonObject().get("thumbnail").toString();
		fulltitle = jsonElement.getAsJsonObject().get("fulltitle").toString();
		webpage_url = jsonElement.getAsJsonObject().get("webpage_url").toString();
		uploader = jsonElement.getAsJsonObject().get("uploader").toString();
		description = jsonElement.getAsJsonObject().get("description").toString();
		filename = jsonElement.getAsJsonObject().get("_filename").toString();
		
		
		
		
		
		
	}

	@Override
	public String toString() {
		return "Video [duration=" + duration + ", average_rating=" + average_rating + ", thumbnailurl=" + thumbnailurl
				+ ", fulltitle=" + fulltitle + ", webpage_url=" + webpage_url + ", uploader=" + uploader
				+ ", description=" + description + ", filename=" + filename + "]";
	}
}
