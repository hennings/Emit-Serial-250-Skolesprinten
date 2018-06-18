package net.spjelkavik.emit.emitag;

import net.miginfocom.swing.MigLayout;
import net.spjelkavik.emit.common.EtimingReader;
import net.spjelkavik.emit.common.Frame;
import net.spjelkavik.emit.common.SeriousLogger;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ActionMapUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.*;
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
            config = new EmitagConfig("static event", null,null,com,"ecard1", "jdbc:odbc:etime-java", ConfigFrameMiG.sunJdbcDriver, true, Boolean.getBoolean("VERIFY"));
        } else {
            config = new AskForConfig().askForConfig(EmitagReader.findSerialPorts());
        }

        log.info("Starting with config: " + config);

        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(config.getJdbcDriver());
        ds.setUrl(config.getJdbcUrl());
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        et.setJdbcTemplate(jdbcTemplate);

        if (config.getMode().equals(EmitagConfig.Mode.VERIFY)) {
            VerifyEmitagFrame af = new VerifyEmitagFrame(config);
            af.setEtimingReader(et);
            EmitagReader.findPort(config.getComPort());
            EmitagReader reader = new EmitagReader(af);
            af.setComStatus("Port: " + reader.getPortName());

        } else {
            AnonEmitagFrame af = new AnonEmitagFrame(config);
            af.setEtimingReader(et);
            if (!"NOCOM".equals(config.getComPort())) {
                EmitagReader.findPort(config.getComPort());
                EmitagReader reader = new EmitagReader(af);
                af.setComStatus("Port: " + reader.getPortName());
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
