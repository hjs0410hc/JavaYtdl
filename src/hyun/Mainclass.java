package hyun;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.*;

public class Mainclass extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1528337565886526207L;

	Mainclass() {
		
		setTitle("아래 칸에 URL 복사하세요");
		setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JTextField texts = new JTextField("");
		texts.setColumns(30);
		JButton button = new JButton("정보 가져오기");
		button.addActionListener(e -> {
			if (texts.getText().equals("") || !(texts.getText().contains("youtube.com/") || (texts.getText().contains("search_query")))) {
				JOptionPane.showMessageDialog(this, "올바른 유튜브 URL을 입력해야 합니다!", "오류", JOptionPane.ERROR_MESSAGE);
			} else {

				if (texts.getText().contains("playlist?")) {
					openWindowPlayList(texts.getText());
				} else {
					openWindow(texts.getText());
				}
			}
		});
		panel.add(texts, BorderLayout.CENTER);
		panel.add(button, BorderLayout.SOUTH);
		add(panel, BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		// setSize(1300,800);
		setVisible(true);
	}

	void openWindow(String youtubeurl) {
		new OneVideo(youtubeurl);
	}

	void openWindowPlayList(String youtubeurl) {
		new PlayList(youtubeurl);
	}

	public static void main(String[] args) {
		new Mainclass();
	}

}
