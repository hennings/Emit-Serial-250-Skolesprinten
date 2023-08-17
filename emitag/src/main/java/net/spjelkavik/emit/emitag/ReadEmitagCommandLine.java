package net.spjelkavik.emit.emitag;

import net.spjelkavik.emit.common.CardNumberReader;
import net.spjelkavik.emit.common.ComPort;

import javax.comm.*;
import java.io.InputStream;
import java.util.Enumeration;

public class ReadEmitagCommandLine {

    static CommPortIdentifier portId;
    static Enumeration portList;
    InputStream inputStream;
    SerialPort serialPort;
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
        ComPort.findPort(defaultPort);
        EmitagReader re = new EmitagReader(new EmitMessageListener() {
            @Override
            public void handleCardMessage(CardNumberReader m) {
                System.out.println("Got message: " + m);
            }
        });


    }
    static String defaultPort = "COM29";

}
