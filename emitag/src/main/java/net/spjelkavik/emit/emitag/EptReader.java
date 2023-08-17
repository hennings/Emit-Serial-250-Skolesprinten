package net.spjelkavik.emit.emitag;


import net.spjelkavik.emit.common.ComPort;

import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;

/**
 * Class declaration
 *
 *
 * @author
 * @version 1.8, 08/03/00
 */
public class EptReader implements Runnable, SerialPortEventListener {
	InputStream		      inputStream;
	SerialPort		      serialPort;
	Thread		      readThread;

	/**
	 * Method declaration
	 *
	 *
	 * @param args
	 *
	 * @see
	 */
	public static void main(String[] args) {
		boolean		      portFound = false;

		if (args.length > 0) {
			defaultPort = args[0];
		}

	}
	static String defaultPort = "COM2";


	/**
	 * Constructor declaration
	 *
	 *
	 * @see
	 */
	public EptReader(final EmitMessageListener af) {
		badgeListener = af;
		try {
			System.out.println("Opening " + ComPort.portId+", "+ ComPort.portId.getName());
			serialPort = (SerialPort) ComPort.portId.open("SimpleReadApp",32000);
		} catch (PortInUseException e) {
			System.err.println("ProblemS: " + e);
		}

		try {
			inputStream = serialPort.getInputStream();
		} catch (IOException e) {}

		try {
			serialPort.addEventListener(this);
		} catch (TooManyListenersException e) {}

		serialPort.notifyOnDataAvailable(true);

		try {
			serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, 
					SerialPort.STOPBITS_2, 
					SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {}

		readThread = new Thread(this);
		readThread.setDaemon(true);
		readThread.start();
	}

	/**
	 * Method declaration
	 *
	 *
	 * @see
	 */
	public void run() {
		System.err.println("run");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {}
		System.err.println("run..exit");
	} 

	int totNr = 0;
	
	List<EptFrame> EptFrames = new ArrayList<EptFrame>();
	EptFrame eptFrame = new EptFrame();
	EptFrame prevEptFrame = null;
	private EmitMessageListener badgeListener;
	
	int prev = -1;
	
	long lastEvent;
	
	/**
	 * Method declaration
	 *
	 *
	 * @param event
	 *
	 * @see
	 */
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {

		case SerialPortEvent.BI:

		case SerialPortEvent.OE:

		case SerialPortEvent.FE:

		case SerialPortEvent.PE:

		case SerialPortEvent.CD:

		case SerialPortEvent.CTS:

		case SerialPortEvent.DSR:

		case SerialPortEvent.RI:

		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;

		case SerialPortEvent.DATA_AVAILABLE:
			byte[] readBuffer = new byte[400];

			long now = System.currentTimeMillis();
			
			if ( (now-lastEvent) > 2 * 1000) {
				eptFrame = new EptFrame();
				System.out.println("-- New EptFrame - more than two seconds...");
			}
			
			try {
				int numBytes = 0;
				while (inputStream.available() > 0) {
					 int delta = inputStream.read(readBuffer);
					 numBytes +=delta;
				} 

				 //System.out.println("Read: " + numBytes);
				for (int i = 0; i < numBytes; i++) {
					byte c = readBuffer[i];
					int c2 = (int) (c&0xFF);
					int c3 = c2 ^ 223;
					
					if (c3 == 255 && prev == 255) {
						if (eptFrame !=null && eptFrame.isReady()) {
							//System.out.println("Previous was: " + EptFrame.getBadgeNo());
							this.badgeListener.handleCardMessage(eptFrame);
							prevEptFrame = eptFrame;
						}
						eptFrame = new EptFrame();
						EptFrames.add(eptFrame);
						eptFrame.add(255);
					}
					if (eptFrame.isReady() && eptFrame.curByte==10) {
//						this.badgeListener.setBadgeNumber(EptFrame.getBadgeNo());
					}
					eptFrame.add(c3);
					//System.out.println("#"+i+" :" + c3 + " ( " +( totNr )  + ") " + prev);
					prev = c3;
					totNr++;
				} 
				
				if (eptFrame!=null && eptFrame.isComplete()) {
					this.badgeListener.handleCardMessage(eptFrame);
					System.out.println(eptFrame.getBadgeNo() + " - " + eptFrame);
				}
				
				//System.out.print("Read: " + new String(readBuffer));
			} catch (IOException e) {}

			lastEvent = now;
			
			break;
		}
	}

	public void setCallback(EmitMessageListener af) {
		this.badgeListener = af;
	} 

}



