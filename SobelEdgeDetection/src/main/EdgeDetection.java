package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Author: Emmanuel Godinez 300251168
 *
 * input: image file. Only use .png files. (note: tiff images don't seem to like
 * java's BufferedImage class) output: image file with edge detection highlights
 *
 */
public class EdgeDetection {

	public static void main(String[] args) {
		if (args.length != 1) {
			JOptionPane.showMessageDialog(null, "One argument please");
			return;
		}
		if (!args[0].endsWith(".png")) {
			JOptionPane.showMessageDialog(null, "PNG file please");
			return;
		}

		new EdgeDetection(args[0]);
	}

	public EdgeDetection(String imagepath) {
		int[][] img = ImageHandler.readImage(imagepath);
		ImageHandler.displayImage("original", img);

		// create convolution masks

		// Vertical Mask
		double[][] rowMask = { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };

		// Horizontal Mask
		double[][] columnMask = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };

		// Apply convolution masks
		int[][] rowOutput = ImageHandler.applyConvolutionMask(img, rowMask);
		ImageHandler.brightenImage(rowOutput);
		ImageHandler.displayImage("Row Output", rowOutput);

		int[][] columnOutput = ImageHandler.applyConvolutionMask(img,
				columnMask);
		ImageHandler.brightenImage(columnOutput);
		ImageHandler.displayImage("Column Output", columnOutput);

		// combine images
		int[][] finalOutput = new int[columnOutput.length][columnOutput[0].length];
		int maxVal = Integer.MIN_VALUE;
		int minVal = Integer.MAX_VALUE;
		for (int y = 0; y < columnOutput[0].length; y++) {
			for (int x = 0; x < columnOutput.length; x++) {
				finalOutput[x][y] = (rowOutput[x][y]+columnOutput[x][y])/2;
				
				// finalOutput[x][y] =
				// (int)Math.tanh(rowOutput[x][y]/columnOutput[x][y]);

				// finalOutput[x][y] = (int) Math.sqrt(Math
				// .pow(rowOutput[x][y], 2)
				// * Math.pow(columnOutput[x][y], 2));
				
				if (finalOutput[x][y] < minVal) {
					minVal = finalOutput[x][y];
				}
				if (finalOutput[x][y] > maxVal) {
					maxVal = finalOutput[x][y];
				}
			}
		}

		// fix image
		int range = maxVal - minVal;
		for (int y = 0; y < finalOutput[0].length; y++) {
			for (int x = 0; x < finalOutput.length; x++) {
				double temporary1 = (double) Math.abs(finalOutput[x][y])
						/ (double) range;
				double temporary2 = temporary1 * 255;
				finalOutput[x][y] = (int) temporary2;
			}
		}
		ImageHandler.brightenImage(finalOutput);
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImageHandler.displayImage("Final", finalOutput);
	}
}
