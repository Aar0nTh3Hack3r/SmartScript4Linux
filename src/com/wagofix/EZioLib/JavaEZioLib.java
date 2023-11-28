package com.wagofix.EZioLib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.Raster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.zip.Deflater;

public class JavaEZioLib {
	private static final Logger logger = LogManager.getLogger(JavaEZioLib.class);
	static float[][] ClusterMask = {new float[]{115.0f, 144.0f, 129.0f, 107.0f, 79.0f, 93.0f}, new float[]{159.0f, 239.0f, 222.0f, 66.0f, 8.0f, 18.0f}, new float[]{175.0f, 190.0f, 206.0f, 53.0f, 41.0f, 29.0f}, new float[]{100.0f, 73.0f, 86.0f, 122.0f, 152.0f, 137.0f}, new float[]{59.0f, 3.0f, 13.0f, 167.0f, 247.0f, 230.0f}, new float[]{47.0f, 35.0f, 23.0f, 183.0f, 198.0f, 214.0f}};
	static float[][] DispersMask = {new float[]{3.0f, 231.0f, 44.0f, 212.0f, 5.0f, 216.0f, 34.0f, 198.0f}, new float[]{113.0f, 51.0f, 175.0f, 105.0f, 118.0f, 55.0f, 161.0f, 93.0f}, new float[]{25.0f, 184.0f, 13.0f, 250.0f, 28.0f, 188.0f, 16.0f, 236.0f}, new float[]{148.0f, 81.0f, 130.0f, 66.0f, 152.0f, 85.0f, 135.0f, 70.0f}, new float[]{8.0f, 221.0f, 37.0f, 202.0f, 1.0f, 226.0f, 41.0f, 207.0f}, new float[]{122.0f, 58.0f, 166.0f, 97.0f, 109.0f, 48.0f, 170.0f, 101.0f}, new float[]{31.0f, 193.0f, 19.0f, 240.0f, 22.0f, 179.0f, 10.0f, 245.0f}, new float[]{157.0f, 89.0f, 139.0f, 73.0f, 143.0f, 77.0f, 126.0f, 62.0f}};

    public static byte[] Cmd_putImage(int px, int py, Raster bitmap, ImageDithering dithering, boolean bCompress) {
        byte[] headbytes;
        ByteBuffer byteBuffer;
        if (dithering == ImageDithering.None || dithering == ImageDithering.Cluster || dithering == ImageDithering.Dipersed) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[height * width];
            //bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            bitmap.getPixels(0, 0, width, height, pixels);
            for (int h = 0; h < height; h++) {
                for (int w = 0; w < width; w++) {
                    pixels[(h * width) + w] = GetGrayDither(pixels[(h * width) + w], w, h, dithering);
                }
            }
            ArrayList<Byte> data = new ArrayList<>();
            int mWidth = width / 8;
            if (width % 8 != 0) {
                mWidth++;
            }
            if (bCompress) {
                headbytes = ("QA" + px + "," + py + "," + mWidth + "," + height + "\n").getBytes();
            } else {
                headbytes = ("Q" + px + "," + py + "," + mWidth + "," + height + "\n").getBytes();
            }
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < mWidth; x++) {
                    byte constructedByte = 0;
                    for (int j = 0; j < 8; j++) {
                        constructedByte = (byte) (constructedByte << 1);
                        int widthPixel = (x * 8) + j;
                        if (widthPixel < width) {
                            int pixel = pixels[(y * width) + widthPixel];
                            if (pixelBrightness(pixel) < 127) {
                                constructedByte = (byte) (constructedByte | 1);
                            }
                        }
                    }
                    data.add(Byte.valueOf(constructedByte));
                }
            }
            byte[] rawDatabytes = new byte[data.size()];
            for (int i = 0; i < rawDatabytes.length; i++) {
                rawDatabytes[i] = data.get(i).byteValue();
            }
            if (bCompress) {
                byte[] c_rawData = compress(rawDatabytes);
                byteBuffer = ByteBuffer.allocate(headbytes.length + c_rawData.length);
                byteBuffer.put(headbytes);
                byteBuffer.put(c_rawData);
            } else {
                byteBuffer = ByteBuffer.allocate(headbytes.length + rawDatabytes.length);
                byteBuffer.put(headbytes);
                byteBuffer.put(rawDatabytes);
            }
            return byteBuffer.array();
        }
        logger.fatal("Dithering not implemented! " + dithering);
        return null;
    }

	public static int GetGrayDither(int pixel, int width, int height, ImageDithering dither) {
        if ((pixel & (-1)) == 0) {
            return 0;
        }
        /*float r = (pixel & 16711680) >> 16;
        float g = (pixel & 65280) >> 8;
        float b = pixel & 255;
        float graycolor = (float) ((0.114d * r) + (0.587d * g) + (0.299d * b) + 0.5d);*/
        float graycolor = pixel;
        if(dither == ImageDithering.None) {
            if (graycolor >= 127.5d) {
                graycolor = 255.0f;
            } else {
                graycolor = 0.0f;
            }
        }
        else if(dither == ImageDithering.Cluster) {
            if (graycolor >= ClusterMask[height % 6][width % 6]) {
                graycolor = 255.0f;
            } else {
                graycolor = 0.0f;
            }
        }
        else if(dither == ImageDithering.Dipersed) {
            if (graycolor >= DispersMask[height % 8][width % 8]) {
                graycolor = 255.0f;
            } else {
                graycolor = 0.0f;
            }
        }
        return (-16777216) | (((int) graycolor) << 16) | (((int) graycolor) << 8) | ((int) graycolor);
    }

	public static int GetGrayLevel(int pixel) {
        if ((pixel & (-1)) == 0) {
            return 255;
        }
        int red = (pixel & 16711680) >> 16;
        int green = (pixel & 65280) >> 8;
        int blue = pixel & 255;
        int gray = ((red + green) + blue) / 3;
        if (gray > 255) {
            gray = 255;
        }
        return gray;
    }
	public static int pixelBrightness(int red, int green, int blue) {
        int level = ((red + green) + blue) / 3;
        return level;
    }
	public static int pixelBrightness(int pixel) {
        int r = (pixel & 16711680) >> 16;
        int g = (pixel & 65280) >> 8;
        int b = pixel & 255;
        return pixelBrightness(r, g, b);
    }
	public static byte[] compress(byte[] data) {
        byte[] output;
        Deflater compressor = new Deflater(5);
        compressor.reset();
        compressor.setInput(data);
        compressor.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        try {
            try {
                byte[] buf2 = new byte[1024];
                while (!compressor.finished()) {
                    int i = compressor.deflate(buf2);
                    bos.write(buf2, 0, i);
                }
                output = bos.toByteArray();
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e2) {
                output = data;
                e2.printStackTrace();
                try {
                    bos.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            compressor.end();
            return output;
        } catch (Throwable th) {
            try {
                bos.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            throw th;
        }
    }
}