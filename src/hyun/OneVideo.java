package hyun;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.*;

public class OneVideo extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3241864805755620137L;
	private static final String filepath = "lib/youtube-dl.exe";
	JFileChooser jfc = new JFileChooser();
	JLabel status = new JLabel("waiting...");
	Video videoObj;

	OneVideo(String youtubeurl) {
		setTitle("Download one video");
		setJfcSetting();
		
		videoObj = getVideo(youtubeurl);
		setLayout(new BorderLayout(2,2));
		JPanel northpanel = new JPanel();
		northpanel.setLayout(new GridLayout(5, 1));
		//JLabel description = new JLabel("영상설명: " + videoObj.description.translateEscapes());
		JLabel duration = new JLabel("길이: " + formatDuration(videoObj.duration));
		JLabel fulltitle = new JLabel("제목: " + videoObj.fulltitle);
		JLabel webpage_url = new JLabel("영상URL: " + videoObj.webpage_url);
		JLabel uploader = new JLabel("업로더: " + videoObj.uploader);
		JPanel centerpanel = new JPanel();


		// webm 확장자는 없애기.........
		int count = 0;
		for (int i = 0; i < videoObj.formatarr.size(); i++) {
			if (videoObj.formatarr.get(i).ext.equals("\"webm\"")) {
				count++;
			}
		}
		for (int i = 0; i < videoObj.formatarr.size();) {
			if (count == 0) {
				break;
			}
			if (videoObj.formatarr.get(i).ext.equals("\"webm\"")) {
				videoObj.formatarr.remove(i);
				count--;
			} else {
				i++;
			}
		}
		centerpanel.setLayout(new GridLayout(videoObj.formatarr.size(), 1));

		JCheckBox[] chkboxs = new JCheckBox[videoObj.formatarr.size()];
		for (int i = 0; i < videoObj.formatarr.size(); i++) {
			Format target = videoObj.formatarr.get(i);
			if (target.fps.equals("null")) {
				chkboxs[i] = new JCheckBox(target.format_id.replaceAll("\"", "") + " " + target.ext + " audio only "
						+ target.asr + "Hz " + target.abr + "Kbps" + formatFilesize(target.filesize));
			} else if (target.abr.equals("null")) {
				chkboxs[i] = new JCheckBox(
						target.format_id.replaceAll("\"", "") + " " + target.ext + " video only " + target.width + "x"
								+ target.height + " " + target.fps + "fps "+formatFilesize(target.filesize));

			} else {
				chkboxs[i] = new JCheckBox(target.format_id.replaceAll("\"", "") + " " + target.ext + " " + target.width
						+ "x" + target.height + " " + target.fps + "fps " + target.abr + "Kbps "
						+ formatFilesize(target.filesize));
			}
		}
		for (JCheckBox items : chkboxs) {
			centerpanel.add(items);
		}
		add(centerpanel, BorderLayout.CENTER);
		northpanel.add(fulltitle);
		northpanel.add(uploader);
		northpanel.add(duration);
		//northpanel.add(description);
		northpanel.add(webpage_url);
		JPanel southpanel = new JPanel();
		southpanel.setLayout(new GridLayout(3, 1));

		southpanel.add(status);
		JButton dlButton = new JButton("DOWNLOAD.");
		dlButton.addActionListener(e -> {
			String[] temp = determineChecked(chkboxs);
			if (temp[1] == null) {
				dlVideo(temp[0], videoObj.webpage_url);
			} else {
				boolean audio = false;
				boolean video = false;
				for (JCheckBox items : chkboxs) {
					if (items.getText().contains("audio only")) {
						audio = true;

					} else if (items.getText().contains("video only")) {
						video = true;
					}
				}

				if (audio && video) {
					newmethod(temp,youtubeurl);

				} else {
					JOptionPane.showMessageDialog(this, "동시에 2개의 영상 또는 음성을 다운로드할 수 없습니다!", "오류",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		JButton mp3Button = new JButton("MP3 DOWNLOAD");
		mp3Button.addActionListener(e->{
			dlmp3(youtubeurl);
			
			
		});
		southpanel.add(dlButton);
		southpanel.add(mp3Button);
		add(southpanel, BorderLayout.SOUTH);

		add(northpanel, BorderLayout.NORTH);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();

		setLocationRelativeTo(null);
		setVisible(true);
	}

	String formatFilesize(String filesize) {
		if (filesize.equals("null")) {
			return "";
		} else {
			double parsed = Double.parseDouble(filesize);
			return (parsed / 1024 >= 1024) ? String.format("%.2f", (parsed / 1024 / 1024)) + "MiB"
					: String.format("%.2f", (parsed / 1024)) + "KiB";

		}

	}
	
	void dlmp3(String youtubeurl) {
		String folder = "";
		if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			folder = jfc.getSelectedFile().toString();
		}
		try {
			ProcessBuilder pb = new ProcessBuilder(filepath, "--no-playlist","-x","--audio-format","mp3","--audio-quality", "0", youtubeurl);
			if(!folder.equals("")) {
				File WorkingDir = new File(folder);
				pb.directory(WorkingDir);
			}
			
			Process p = pb.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "EUC-KR"));
			String line = "";
			while ((line = br.readLine()) != null) {
				System.out.println(line);

			}
			
			p.waitFor();
			status.setText("download completed.");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	

	void newmethod(String[] target,String youtubeurl) {
		String folder = "";
		if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			folder = jfc.getSelectedFile().toString();
		}
		try {
			ProcessBuilder pb = new ProcessBuilder(filepath, "--no-playlist", "-f", target[0], youtubeurl);
			if(!folder.equals("")) {
				File WorkingDir = new File(folder);
				pb.directory(WorkingDir);
			}
			Process p = pb.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "EUC-KR"));
			String line = "";
			while ((line = br.readLine()) != null) {
				System.out.println(line);

			}
			
			p.waitFor();
			p=null;
			pb=null;
			br=null;
			pb = new ProcessBuilder(filepath, "--no-playlist", "-f", target[1], youtubeurl);
			if(!folder.equals("")) {
				File WorkingDir = new File(folder);
				pb.directory(WorkingDir);
			}
			p = pb.start();
			br = new BufferedReader(new InputStreamReader(p.getInputStream(), "EUC-KR"));
			while ((line = br.readLine()) != null) {
				System.out.println(line);

			}
			status.setText("download completed.");
		}catch(Exception e) {
			e.printStackTrace();
		}

		
		String fname = videoObj.filename.replace(".webm\"", "").replace(".mp4\"", "");
		//ffmpeg -i video.mp4 -i audio.wav -c copy output.mkv
		String[] commands = {"lib/ffmpeg.exe","-y","-i",fname+".mp4\"","-i",fname+".m4a\"","-c","copy",fname+"-mixed.mp4\""};
		for(String str : commands) {
			System.out.println(str);
		}
		try {
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(commands);
		String lines = null;
		BufferedReader brs = new BufferedReader(new InputStreamReader(pr.getInputStream(),"EUC-KR"));
		
		while ((lines = brs.readLine()) != null) {
			System.out.println(lines);
		}
		String errorlines = null;
		BufferedReader errors = new BufferedReader(new InputStreamReader(pr.getErrorStream(),"EUC-KR"));
		
		while((errorlines = errors.readLine()) != null) {
			System.out.println(errorlines);
		}
		
		pr.waitFor();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	void setJfcSetting() {
		jfc.setCurrentDirectory(new java.io.File("."));
		jfc.setDialogTitle("폴더를 선택하세요");
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.setMultiSelectionEnabled(false);
	}

	String[] determineChecked(JCheckBox[] arr) {
		String[] result = new String[2];
		for (JCheckBox item : arr) {
			if (item.isSelected() && (result[0] == null)) {
				result[0] = item.getText().split(" ")[0];
			} else if (item.isSelected()) {
				result[1] = item.getText().split(" ")[0];
			}
		}
		return result;
	}

	void dlVideo(String target, String youtubeurl) {
		String folder = "";
		if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			folder = jfc.getSelectedFile().toString();
		}
		status.setText("downloading... please do not quit");
		try {
			ProcessBuilder pb = new ProcessBuilder(filepath, "--no-playlist", "-f", target, youtubeurl);
			if(!folder.equals("")) {
				File WorkingDir = new File(folder);
				pb.directory(WorkingDir);
			}
			Process p = pb.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "EUC-KR"));
			String line = "";
			while ((line = br.readLine()) != null) {
				System.out.println(line);

			}
			status.setText("download completed.");
			if (Integer.parseInt(target) >= 139 && Integer.parseInt(target) <= 141) {
				if (JOptionPane.showConfirmDialog(this, "DO YOU WANT MP3?", "Mp3 Convert",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					try {
						
						String fname = videoObj.filename.replace(".webm\"", "").replace(".mp4\"", "");
						String[] commands = {"lib/ffmpeg.exe","-y","-i",fname+".m4a\"", "-c:a", "libmp3lame", "-q:a","0",fname+".mp3\""};
						for(String str : commands) {
							System.out.println(str);
						}
						Runtime rt = Runtime.getRuntime();
						Process pr = rt.exec(commands);
						String lines = null;
						BufferedReader brs = new BufferedReader(new InputStreamReader(pr.getInputStream(),"EUC-KR"));
						
						while ((lines = brs.readLine()) != null) {
							System.out.println(lines);
						}
						String errorlines = null;
						BufferedReader errors = new BufferedReader(new InputStreamReader(pr.getErrorStream(),"EUC-KR"));
						
						while((errorlines = errors.readLine()) != null) {
							System.out.println(errorlines);
						}
						
						pr.waitFor();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	String formatDuration(int time) { // time은 초다.
		int hour = time / 60 / 60;
		int minute = (((time / 60) == 60) ? 0 : (time / 60));
		int second = time % 60;
		return String.format("%02d:%02d:%02d", hour, minute, second);

	}

	Video getVideo(String youtubeurl) {
		String jsonstring = "";

		try {
			Process p = new ProcessBuilder(filepath, "--no-playlist", "-j", youtubeurl).start();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

			jsonstring = br.readLine();
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		Video videoObj = new Video(jsonstring);
		return videoObj;
	}

}
