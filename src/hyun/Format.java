package hyun;

public class Format {
	public String fps = "";
	public String format_note = "";
	public String ext = "";
	public String acodec = "";
	public String vcodec ="";
	public String format_id = "";
	public String filesize= "";
	public String height = "";
	public String asr = "";
	public String width = "";
	public String abr = "";
	public String type = "video";
	public String filename = "";
	
	Format(String b,String c,String d,String e,String f,String g,String h,String i, String j,String k,String l){
		fps = b;
		format_note = c;
		ext = d;
		acodec = e;
		format_id = f;
		filesize = g;
		height = h;
		asr = i;
		width = j;
		vcodec = k;
		abr = l;
		
		if(i.equals("null")) {
			type = "video only";
		}else if(fps.equals("null")) {
			type = "audio only";
		}
	}
	

	
	
}
