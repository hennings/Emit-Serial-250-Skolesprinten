package net.spjelkavik.emit.common;

import org.apache.log4j.Logger;

import javax.comm.CommPortIdentifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ComPort {

    final static private Logger log = Logger.getLogger(ComPort.class);

    public static CommPortIdentifier portId;
    public static Enumeration portList;
    public static boolean findPort(String defaultPort) {
        portList = CommPortIdentifier.getPortIdentifiers();
        log.info("Enumerator: " + portList);
        if (!portList.hasMoreElements()) { log.warn("No COM-port found."); }
        int n = 0;
        boolean portFound = false;
        while (portList.hasMoreElements()) {
            n++;
            portId = (CommPortIdentifier) portList.nextElement();
            log.info("Port: " + portId + " - " + portId.getName());
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals(defaultPort)) {
                    portFound  = true;
                    return portFound;
                }
            }
        }
        if (!portFound) {
            System.out.println("port " + defaultPort + " not found.");
            System.exit(-1);
        }
        return portFound;
    }


    public static List<String> findSerialPorts() {
        Enumeration portList;
        portList = CommPortIdentifier.getPortIdentifiers();
        List<String> ports = new ArrayList<String>();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                ports.add(portId.getName());
            }
        }
        return ports;
    }
}
