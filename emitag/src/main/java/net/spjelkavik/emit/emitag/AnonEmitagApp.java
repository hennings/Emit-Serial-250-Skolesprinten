package net.spjelkavik.emit.emitag;

import net.spjelkavik.emit.common.ComPort;
import net.spjelkavik.emit.common.EtimingReader;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static net.spjelkavik.emit.emitag.CommonConstants.fontBaseSize;
import static net.spjelkavik.emit.emitag.CommonConstants.fontName;

public class AnonEmitagApp  {



/*
  Skolesprinten: update ecard set ecard=startno

  Procedure:
    scan number bib - name and team is shown
    read emitag - emitag number is shown prominently
    press f4 to save
    the stored combo is moved to the "previous" part
    the system is ready for the next runner



 */


    final static Logger log = Logger.getLogger(AnonEmitagApp.class);

    /**
     *
     */



    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        //BeanFactory f = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});

//       UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        UIManager.getLookAndFeelDefaults()
                .put("defaultFont", new Font("Serif", Font.PLAIN, fontBaseSize));


        setUIFont (new javax.swing.plaf.FontUIResource(fontName,Font.PLAIN,fontBaseSize));

        // -Dswing.aatext=true -Dswing.plaf.metal.controlFont=Tahoma -Dswing.plaf.metal.userFont=Tahoma



        EtimingReader et = new EtimingReader();

        EmitagConfig config;
        if (args.length > 0) {
            String com = args[0];
            System.out.println("Using port " + com);
            config = new EmitagConfig("static event", null,null,com,"ecard1", "jdbc:odbc:etime-java", ConfigFrameMiG.sunJdbcDriver, true, Boolean.getBoolean("VERIFY"), EmitagConfig.CardType.EMITAG);
        } else {
            config = new AskForConfig().askForConfig(ComPort.findSerialPorts());
        }

        log.info("Starting with config: " + config);

        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(config.getJdbcDriver());
        ds.setUrl(config.getJdbcUrl());
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        et.setJdbcTemplate(jdbcTemplate);

        if (config.getMode().equals(EmitagConfig.Mode.VERIFY)) {
            VerifyEmitFrame af = new VerifyEmitFrame(config);
            af.setEtimingReader(et);
            ComPort.findPort(config.getComPort());
            if (config.getCardType().equals(EmitagConfig.CardType.EMITAG)) {
                EmitagReader reader = new EmitagReader(af);
            } else {
                EptReader reader250 = new EptReader(af);
            }
            af.setComStatus("Port: " + ComPort.portId.getName());

        } else {
            AnonEmitFrame af = new AnonEmitFrame(config);
            af.setEtimingReader(et);
            if (!"NOCOM".equals(config.getComPort())) {
                ComPort.findPort(config.getComPort());

                if (config.getCardType().equals(EmitagConfig.CardType.EMITAG)) {
                    EmitagReader reader = new EmitagReader(af);
                } else {
                    EptReader reader250 = new EptReader(af);
                }
                af.setComStatus("Port: " + ComPort.portId.getName());
                //reader.setCallback(af);
            } else {
                log.info("Skipping COM-port.");
            }



        }
    }

    public static void setUIFont (javax.swing.plaf.FontUIResource f){
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (key, f);
        }
    }

    static class AskForConfig {
         EmitagConfig askForConfig(List<String> serialPorts) {
            ConfigFrameMiG cf = new ConfigFrameMiG();
            cf.init(serialPorts, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    configFinished();
                }
            });
            waitUntilConfigFinished();
            return cf.getConfig();
        }


        private boolean configFinished = false;

        private synchronized void waitUntilConfigFinished() {
            while (!configFinished) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

        private synchronized void configFinished() {
            configFinished = true;
            notify();
        }
    }


}
