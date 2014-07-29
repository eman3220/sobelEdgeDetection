package main;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * input: image file output: image file with edge detection highlights
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
			BufferedImage bi = ImageIO.read(new File(imagepath));
			Raster ras = bi.getData();

			// create 2d array images
			img = new int[ras.getWidth()][ras.getHeight()];
			output = new int[ras.getWidth()][ras.getHeight()];

			tb = new int[ras.getWidth()][ras.getHeight()];
			lr = new int[ras.getWidth()][ras.getHeight()];

			for (int i = 0; i < ras.getHeight(); i++) {
				for (int j = 0; j < ras.getWidth(); j++) {

					int rgb = bi.getRGB(j, i);
					int r = (rgb >> 16) & 0xFF;
					int g = (rgb >> 8) & 0xFF;
					int b = (rgb & 0xFF);

					int gray = (r + g + b) / 3;

					img[j][i] = gray;

					// System.out.print(gray+" ");
				}
				// System.out.println();
			}

			/*
			 * // read image from array for(int i=0; i<ras.getHeight();i++){
			 * for(int j=0; j<ras.getWidth();j++){
			 * System.out.print(img[j][i]+" "); } System.out.println(); }
			 */

			// top to bottom sweep
			for (int i = 0; i < ras.getHeight() - 1; i++) {
				for (int j = 0; j < ras.getWidth() - 1; j++) {
					// get current pixel
					int current = img[j][i];
					// check bottom pixel
					int bottom = img[j][i + 1];
					// get difference
					int diff = Math.abs(bottom - current);

					// put into output array at the bottom pixel
					tb[j][i] = diff;
				}
			}
			printImage(tb);

			// left to right sweep
			for (int i = 0; i < ras.getHeight() - 1; i++) {
				for (int j = 0; j < ras.getWidth() - 1; j++) {
					// get current pixel
					int current = img[j][i];
					// check right pixel
					int right = img[j+1][i];
					// get difference
					int diff = Math.abs(right - current);

					// put into output array at the bottom pixel
					lr[j][i] = diff;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printImage(int[][] array){
		for(int i=0;i<array.length;i++){
			for(int j=0;j<array[i].length;j++){
				System.out.print(array[i][j]+" ");
			}
			System.out.println();
		}
	}

}
