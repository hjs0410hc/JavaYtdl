package hyun;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.swing.*;

public class PlayList extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4303000807554642741L;
	private static final String filepath = "lib/youtube-dl.exe";
	JFileChooser jfc = new JFileChooser();
	JPanel southpanel = new JPanel();
	JButton dlButton = new JButton("Download ALL");
	JPanel centerpanel = new JPanel();
	JLabel status = new JLabel("Waiting for your order...");
	
	PlayList(String youtubeurl){
		setTitle("Download playlist");
		setLayout(new BorderLayout());
		setJfcSetting();
		ArrayList<Video> videoarr = getVideo(youtubeurl);
		centerpanel.setLayout(new GridLayout(videoarr.size(),1));
		for(Video item : videoarr) {
			centerpanel.add(new JLabel(item.fulltitle));
		}
		add(centerpanel,BorderLayout.CENTER);
		dlButton.addActionListener(e->{
			dlVideos(youtubeurl);
		});
		southpanel.setLayout(new GridLayout(2,1));
		southpanel.add(dlButton);
		southpanel.add(status);
		add(southpanel,BorderLayout.SOUTH);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	void dlVideos(String youtubeurl) {
		String folder = "";
		if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			folder = jfc.getSelectedFile().toString();		
			try {
				status.setText("downloading... 기다려");
				File WorkingDir = new File(folder);
				ProcessBuilder pb = new ProcessBuilder(filepath, youtubeurl);
				pb.directory(WorkingDir);
				Process p = pb.start();
				String line ="";
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(),"EUC-KR"));
				while((line = br.readLine())!=null) {
					System.out.println(line);
				}
				status.setText("download completed.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			
		}


	}

	void setJfcSetting() {
		jfc.setCurrentDirectory(new java.io.File("."));
		jfc.setDialogTitle("폴더를 선택하세요");
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.setMultiSelectionEnabled(false);
	}
	
	
	ArrayList<Video> getVideo(String youtubeurl) {
		String jsonstring = "";
		ArrayList<Video> videoarr = new ArrayList<>();
		try {
			Process p = new ProcessBuilder(filepath, "-j", youtubeurl).start();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

			while((jsonstring = br.readLine()) != null) {
				videoarr.add(new Video(jsonstring));
				
			}
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(videoarr.size());
		return videoarr;
	}
}
