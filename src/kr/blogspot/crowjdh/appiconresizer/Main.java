package kr.blogspot.crowjdh.appiconresizer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {
	private static List<Integer> androidIconSizes;
	private static List<Integer> iOSIconSizes;
	
	private static JFileChooser mChooser;
	private static JFrame frame;
	private static JLabel mFileName;
	
	static {
		androidIconSizes = new ArrayList<Integer>();
		androidIconSizes.add(192);
		androidIconSizes.add(144);
		androidIconSizes.add(96);
		androidIconSizes.add(72);
		androidIconSizes.add(48);
		androidIconSizes.add(36);

		iOSIconSizes = new ArrayList<Integer>();
		iOSIconSizes.add(180);
		iOSIconSizes.add(167);
		iOSIconSizes.add(152);
		iOSIconSizes.add(144);
		iOSIconSizes.add(120);
		iOSIconSizes.add(114);
		iOSIconSizes.add(76);
		iOSIconSizes.add(72);
		iOSIconSizes.add(57);
	}
	
	public static void main(String args[]) {
		mChooser = new JFileChooser();
		
		frame = new JFrame("Unity App Icon Converter");
		Container container = frame.getContentPane();
		frame.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel top = new JPanel();
		top.setAlignmentX(Component.LEFT_ALIGNMENT);
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		JButton btn = new JButton("Load & Convert");
		btn.addActionListener(onLoadImageCallback);
		top.add(btn);
		mFileName = new JLabel("Status: ");
		mFileName.setBackground(Color.CYAN);
		mFileName.setPreferredSize(new Dimension(300, 50));
		top.add(mFileName);
		
		container.add(top);
		
		frame.setSize(400, 200);
		frame.setVisible(true);
	}
	
	private static ActionListener onLoadImageCallback = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			mFileName.setText("Status: Converting...");
			
			mChooser.addChoosableFileFilter(new FileNameExtensionFilter("csv", "csv"));
			mChooser.addChoosableFileFilter(new FileNameExtensionFilter("tsv", "tsv"));
			File curDir = new File(".");
			mChooser.setCurrentDirectory(curDir.getAbsoluteFile());
			int returnVal = mChooser.showOpenDialog(frame);

			if(returnVal == JFileChooser.APPROVE_OPTION) {
				File file = mChooser.getSelectedFile();
				try {
					for (int size : androidIconSizes) {
						resizeImageWithPrefix(file, size, "Android");
					}
					for (int size : iOSIconSizes) {
						resizeImageWithPrefix(file, size, "iOS");
					}
					mFileName.setText("Status: Done");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	private static void resizeImageWithPrefix(File imageFile, int size, String prefix) throws IOException {
		File directory = new File(imageFile.getParent(), prefix);
		directory.mkdir();
		BufferedImage image = ImageIO.read(imageFile);
		saveImage(resize(image, size, size), size, directory, prefix);
	}
	
	private static void saveImage(BufferedImage image, int size, File directory, String prefix) throws IOException {
		String fileName = String.format("%s_app_icon_%d.png", prefix, size);
		ImageIO.write(image, "png", new File(directory, fileName));
	}
	
	private static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}

}
