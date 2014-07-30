package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * Author: Emmanuel Godinez 300251168
 *
 * input: image file. Only use .png files.
 * (note: tiff images don't seem to like java's BufferedImage class)
 * output: image file with edge detection highlights
 *
 */
public class Sobel {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.exit(0);
		}
		String imagepath = args[0];

		new Sobel(imagepath);
	}

	private int[][] img;
	private int[][] tb;
	private int[][] lr;
	private int[][] output;

	public Sobel(String imagepath) {

		try {
			BufferedImage bi = null;
			while(bi==null){
				bi = ImageIO.read(new File(imagepath));
			}

			// create 2d array images
			img = new int[bi.getHeight()][bi.getWidth()];
			output = new int[bi.getWidth()][bi.getHeight()];
			tb = new int[bi.getWidth()][bi.getHeight()];
			lr = new int[bi.getWidth()][bi.getHeight()];

			// read in image pixels
			for (int i = 0; i < bi.getWidth(); i++) {
				for (int j = 0; j < bi.getHeight(); j++) {

					int rgb = bi.getRGB(i, j);
					int r = (rgb >> 16) & 0xFF;
					int g = (rgb >> 8) & 0xFF;
					int b = (rgb & 0xFF);
					int gray = (r + g + b) / 3;

					img[j][i] = gray*2;
				}
			}

			//display original image
			JFrame frame1 = new JFrame();
			frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame1.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame1.setSize((bi.getHeight() + 80), (bi.getWidth() + 80));
			frame1.setVisible(true);
			frame1.add(new mypanel(this.img));

			// sweep image to find edges
			for (int i = 0; i < bi.getWidth() - 1; i++) {
				for (int j = 0; j < bi.getHeight() - 1; j++) {
					int current = img[j][i];
					int bottom = img[j][i + 1];
					int bot_diff = Math.abs(bottom - current);
					tb[j][i] = bot_diff;

					int right = img[j + 1][i];
					int right_diff = Math.abs(right - current);
					lr[j][i] = right_diff;
				}
			}

			// merge tb and lr
			for (int i = 0; i < bi.getHeight() - 1; i++) {
				for (int j = 0; j < bi.getWidth() - 1; j++) {
					int avg = (tb[j][i] + lr[j][i]) / 2;
					output[j][i] = avg;
				}
			}
			// NOTE: we could just skip this entirely. just find a way to put everything
			// into the final 2d array

			// draw to frame
			JFrame frame2 = new JFrame();
			frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame2.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame2.setSize((bi.getHeight() + 80), (bi.getWidth() + 80));
			frame2.setVisible(true);
			frame2.add(new mypanel(this.output));

			printImage(output);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printImage(int[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				System.out.print(array[i][j] + " ");
			}
			System.out.println();
		}
	}

}
