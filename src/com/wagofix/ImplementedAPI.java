package com.wagofix;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.wago.cleverscript.print.EmptyAPI;
import com.wagofix.EZioLib.ImageDithering;
import com.wagofix.EZioLib.JavaEZioLib;

public class ImplementedAPI extends EmptyAPI {
	private static final Logger logger = LogManager.getLogger(ImplementedAPI.class);
	Socket socket;
	DataInputStream dIn;
	DataOutputStream dOut;

    @Override
    public int openport(String strPort) {
        logger.fatal("Not Implemented");
        return 0;
    }

    @Override
    public int OpenUSB(String strUsbID) {
        logger.fatal("Not Implemented");
        return 0;
    }

    @Override
    public int OpenDriver(String strDriverName) {
        logger.fatal("Not Implemented");
        return 0;
    }

    @Override
    public int OpenNet(String strIP, String strPort) {
		logger.debug("Connecting to the printer " + strIP + ":" + strPort);
		try {
			socket = new Socket(strIP, Integer.parseInt(strPort));
			socket.setSoTimeout(1000);
			dOut = new DataOutputStream(socket.getOutputStream());
		} catch(IOException ex) {
			logger.error("Cannot connect to the printer!", ex);
			closeport();
			return 0;
		}
        return 1;
    }

    @Override
    public int setbaudrate(int nBaud) {
        logger.fatal("Not Implemented");
        return 0;
    }

    @Override
    public void closeport() {
		logger.debug("Closing socket");
		if(socket == null) return;
		try {
			socket.close();
		} catch(IOException ex) {
			logger.error("Error closing the socket!", ex);
		}
		socket = null;
    }

    @Override
    public int setup(int LabelLength, int Darkness, int Speed, int LabelMode, int LabelGap, int BlackTop) {
        logger.fatal("Not Implemented");
        return 0;
    }

    @Override
    public int sendcommand(String Command) {
		//System.out.println("MyCommand: " + Command);
		byte[] data = (Command+"\r\n").getBytes();
		sendbuf(data, data.length);
        return 1;
    }

    @Override
    public synchronized int RcvBuf(byte[] ByteData, int nLength) {
		if(socket == null) return 0;
        //byte[] data;
		/*try {
            dIn = new DataInputStream(socket.getInputStream());
			dIn.read(ByteData);
		} catch(IOException ex) {
			logger.error("Error reading data from socket!", ex);
			closeport();
			return 1; // EXPERIMENTAL
		}*/
		//System.arraycopy(data, 0, ByteData, 0, data.length);
		System.arraycopy("00\r\n".getBytes(), 0, ByteData, 0, 4);
        return 1;
    }

    @Override
    public synchronized int sendbuf(byte[] ByteData, int nLength) {
        if(socket == null) return 0;
		try {
			dOut.write(ByteData, 0, nLength);
			dOut.flush();
		} catch(IOException ex) {
			logger.error("Error sending data to socket!", ex);
			closeport();
			return 0;
		}
		return 1;
    }

    @Override
    public int putimage(int x, int y, String filename, int degree) {
        logger.fatal("Not Implemented");
        return 0;
    }

    @Override
    public int putimage_Halftone(int PosX, int PosY, String Filename, int Degree, int Halftone) {
        logger.debug("Starting putimage");
		BufferedImage input, img;
		try {
			input = ImageIO.read(new File(Filename));
		} catch(IOException ex) {
			logger.error("Failed to load image for printing!", ex);
			return 0;
		}
        img = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        img.createGraphics().drawImage(input, 0, 0, Color.WHITE, null);
		byte[] data = JavaEZioLib.Cmd_putImage(PosX, PosY, img.getData(), ImageDithering.Cluster, false);
		sendbuf(data, data.length);
        return 1;
    }

    @Override
    public int FindFirstUSB(byte[] x) {
        logger.fatal("Not Implemented");
        return 0;
    }

    @Override
    public int FindNextUSB(byte[] x) {
        logger.fatal("Not Implemented");
        return 0;
    }

    @Override
    public int FindFirstNet(byte[] ip, byte[] port) {
        logger.fatal("Not Implemented");
        return 0;
    }

    @Override
    public int FindNextNet(byte[] ip, byte[] port) {
        logger.fatal("Not Implemented");
        return 0;
    }

    @Override
    public int DrawHorLine(int PosX, int PosY, int Length, int Thick) {
        logger.fatal("Not Implemented");
        return 0;
    }

    @Override
    public int DrawVerLine(int PosX, int PosY, int Length, int Thick) {
        logger.fatal("Not Implemented");
        return 0;
    }

    @Override
    public int GetDllVersion(byte[] version) {
        logger.fatal("Not Implemented");
        return 0;
    }
}