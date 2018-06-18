package net.spjelkavik.emit.emitag;

import net.miginfocom.swing.MigLayout;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.prefs.Preferences;


import static net.spjelkavik.emit.emitag.CommonConstants.fontBaseSize;
import static net.spjelkavik.emit.emitag.CommonConstants.fontName;

/**
 * User: hennings
 * Date: 22.08.2014
 * Time: 18:24
 */
public class ConfigFrameMiG extends JFrame {

    public final static String sunJdbcDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
    public final static String sqlServerJdbcDriver = "com.microsoft.sqlserver.jdbc.SQLServerConnectionPoolDataSource";

    private String globalJdbc;
    private String globalJdbcUrl;
    private String globalTitle;

    Logger log = Logger.getLogger(ConfigFrameMiG.class);

    JTextField dbFileTxt = new JTextField();
    JTextField sysFileTxt = new JTextField();
    JButton butOk = new JButton("Start");
    //JButton butVerify = new JButton("Verify tag only");
    private JTextField emitSql = new JTextField();
    private JComboBox emitSqlDb = new JComboBox();


    private JTextField title = new JTextField();


    private JButton butExit = new JButton("Avslutt");

    private JComboBox listOfComPorts;
    private final JLabel configStatus = new JLabel("Not tested");
    private EmitagConfig emitagConfig;

    String ecardField;

    public String getGlobalJdbc() {
        return globalJdbc;
    }

    public String getGlobalJdbcUrl() {
        return globalJdbcUrl;
    }

    public void init(List<String> serialPorts, final ActionListener callback) {

        final JFrame thisFrame = this;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel all = new JPanel(new MigLayout("gap 5px 5px"));

        all.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.add(all);
        final RegistryPreferences prefs = new RegistryPreferences();

        String dbFileName = prefs.getDbFile();
        String sysFileName = prefs.getSysFile();
        String comPort = prefs.getComPort();
        String mode64 = prefs.getMode64();
        ecardField = prefs.getEcardField();

        dbFileTxt.setText(dbFileName);
        sysFileTxt.setText(sysFileName);

        JLabel titleLabel = new JLabel("Anonyme Emitags " + (Boolean.getBoolean("VERIFY") ? " - Verify" : ""));
        titleLabel.setFont(new Font(fontName, Font.BOLD, fontBaseSize * 2));

        all.add(titleLabel, "span,wrap");


        String[] dbTypes;
        dbTypes = new String[]{"Emit", "EmitSQL"};

        addButtonLine(all, "Database Type", dbTypes, prefs.getDbType(), prefs, new DoIt() {
            @Override
            public void doit(String value) {
                prefs.setDbType(value);
            }
        });


        // SQL Server

        all.add(new JLabel("Emit SQL Server"));
        emitSql.setText(prefs.getEmitSql());
        emitSql.setPreferredSize(new Dimension(250, 20));
        if (StringUtils.stripToNull(emitSql.getText()) == null) {
            emitSql.setText("jdbc:sqlserver://falketind:1433");
        }
        emitSqlDb = new JComboBox();
        emitSqlDb.addItem("********");
        all.add(emitSql, "growx 200");
        JButton emitSqlSeek = new JButton();
        emitSqlSeek.setText("Connect");
        emitSqlSeek.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FetchEmitSqlDbEventsWorker(emitSql.getText(), emitSqlDb).execute();
            }
        });

        all.add(emitSqlSeek, "growx 200");
        all.add(emitSqlDb, "growx 200, wrap");


        // ACcess


        addFileChooser(all, "eTime.mdb file", dbFileTxt, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileTypeFilter ftf = new FileTypeFilter("mdb", "etime.mdb Files", "Select", thisFrame);
                File dbFile = ftf.pickFile(dbFileTxt.getText());
                if (dbFile != null) {
                    dbFileTxt.setText(dbFile.getAbsolutePath());
                }
            }
        });

        addFileChooser(all, "system.mdw file", sysFileTxt, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileTypeFilter ftf = new FileTypeFilter("system.mdw", "system.mdw file", "Select", thisFrame);
                File sysFile = ftf.pickFile(sysFileTxt.getText());
                if (sysFile != null) {
                    sysFileTxt.setText(sysFile.getAbsolutePath());
                }
            }
        });

        String winMode = "Windows 32/64 bits? (" + System.getProperty("os.arch") + ")";
        String[] buttonsMode = new String[]{"32", "64"};
        addButtonLine(all, winMode, buttonsMode, mode64, prefs, new DoIt() {
            @Override
            public void doit(String value) {
                prefs.setMode64(value);
            }
        });


        String[] comPorts = serialPorts.toArray(new String[0]);
        listOfComPorts = new JComboBox(comPorts);
        for (int i = 0; i < listOfComPorts.getItemCount(); i++) {
            if (listOfComPorts.getItemAt(i).equals(comPort)) {
                listOfComPorts.setSelectedIndex(i);
            }
        }
        JLabel comportLabel = new JLabel("COM-port");
        if (listOfComPorts.getItemCount() == 0) {
            comportLabel.setText("COM-port (MISSING)");
            comportLabel.setForeground(Color.red);
        }
        all.add(comportLabel);
        all.add(listOfComPorts, "wrap");


        String ecardLabel = "ecard1 or ecard2?";
        String[] ecardAlternatives = new String[]{"ecard1", "ecard2"};
        addButtonLine(all, ecardLabel, ecardAlternatives, ecardField, prefs, new DoIt() {
            @Override
            public void doit(String value) {
                prefs.setEcardField(value);
                ecardField = value;
            }
        });


        final JTextField furl = new JTextField();
        all.add(furl, "span,width 100%");

        butOk.setEnabled(false);
        butOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setupConfiguration(prefs, furl);

                callback.actionPerformed(null);
                setVisible(false);
            }
        });

        /*
        butVerify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setupConfiguration(prefs, furl);

                callback.actionPerformed(null);
                setVisible(false);
            }
        });
        */

        JButton butTest = new JButton("Test config");
        all.add(butTest);
        all.add(new JLabel(""), "wrap");
        butTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateConfig(prefs);
                verifyDataSource(prefs, furl);
                emitagConfig = new EmitagConfig(globalTitle, dbFileTxt.getText(), sysFileTxt.getText(),
                        (String) listOfComPorts.getSelectedItem(), ecardField, globalJdbcUrl, globalJdbc, Boolean.getBoolean("RELAY"), Boolean.getBoolean("VERIFY"));

            }
        });

        configStatus.setForeground(Color.RED);
        all.add(configStatus, "span");
        all.add(new JLabel(""));
        all.add(new JLabel(""), "wrap");

        all.add(butTest);


        all.add(butOk);
        //all.add(butVerify);
        all.add(butExit, "wrap");

        butExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(-1);
            }
        });

        pack();
        setLocationRelativeTo(null);  // Center window.
        setVisible(true);


    }

    private void setupConfiguration(RegistryPreferences prefs, JTextField furl) {
        updateConfig(prefs);
        calculateJdbcUrl(prefs, furl);
        //verifyDataSource(prefs, furl);

        emitagConfig = new EmitagConfig(globalTitle, dbFileTxt.getText(), sysFileTxt.getText(),
                (String) listOfComPorts.getSelectedItem(), ecardField, globalJdbcUrl, globalJdbc,
                Boolean.getBoolean("RELAY"), Boolean.getBoolean("VERIFY"))
        ;
    }


    void addButtonLine(JPanel all, String lineText, String[] buttons, String currentValue,
                       final RegistryPreferences prefs, final DoIt l) {
        JLabel l0 = new JLabel(lineText);
        all.add(l0);
        java.util.List<JRadioButton> rb = new ArrayList<JRadioButton>();
        ButtonGroup rg = new ButtonGroup();
        int nbuttons = buttons.length;
        all.add(new JLabel(), "split " + (1 + nbuttons));
        for (final String bn : buttons) {
            JRadioButton theButton = new JRadioButton(bn);
            if (bn.equals(currentValue)) {
                theButton.setSelected(true);
            }

            theButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    l.doit(bn);
                }

            });

            rb.add(theButton);
            rg.add(theButton);
            all.add(theButton);
        }

        all.add(new JLabel(), "wrap");
    }


    private JPanel addFileChooser(JPanel cf, String theLabel, JTextField resultTextField, ActionListener al) {
        //BorderLayout bl = new BorderLayout(5,5);
        //JPanel cf = new JPanel();
        //cf.setLayout(bl);
        JLabel label = new JLabel(theLabel);
        JTextField tf = resultTextField;
        JButton selectButton = new JButton("Select file");
        selectButton.addActionListener(al);

        cf.add(label);
        cf.add(tf, "growx 200");
        cf.add(selectButton, "wrap");

        //cf.add(configFileStatus, BorderLayout.PAGE_END);
        return null;
    }

    public EmitagConfig getConfig() {
        return emitagConfig;
    }

    interface DoIt {
        void doit(String value);
    }

    private void updateConfig(RegistryPreferences prefs) {
        prefs.setDbFile(dbFileTxt.getText());
        prefs.setSysFile(sysFileTxt.getText());
        prefs.setComPort((String) listOfComPorts.getSelectedItem());
        prefs.setEmitSql(emitSql.getText());

    }

    private static class RegistryPreferences {
        final Preferences preferences;
        final static Logger log = Logger.getLogger(RegistryPreferences.class);

        RegistryPreferences() {
            preferences = Preferences.userNodeForPackage(ConfigFrameMiG.class);
            log.info("Registry preferences: " + preferences);
            preferences.put("testwrite", "42");
        }

        public void setSysFile(String sysFile) {
            preferences.put("/db/sysFile", sysFile);
        }

        public void setDbFile(String dbFile) {
            preferences.put("/db/etimeFile", dbFile);
        }

        public String getSysFile() {
            return preferences.get("/db/sysFile", "c:\\system.mdb");
        }

        public String getDbFile() {
            return preferences.get("/db/etimeFile", "c:\\etime.mdb");
        }

        public void setMode64(String m) {
            preferences.put("/mode64", m);
        }

        public String getMode64() {
            return preferences.get("/mode64", "64");
        }


        public String getComPort() {
            return preferences.get("/comport", "COM1");
        }

        public void setComPort(String comPort) {
            preferences.put("/comport", comPort);
        }

        public String getEcardField() {
            return preferences.get("/ecardfield", "ecard1");
        }

        public void setEcardField(String s) {
            preferences.put("/ecardfield", s);

        }

        public String getDbType() {
            return preferences.get("/dbType", "Emit");
        }

        public void setDbType(String value) {
            preferences.put("/dbType", value);
        }

        public String getEmitSql() {
            return preferences.get("/emitSql", "");
        }

        public void setEmitSql(String s) {
            preferences.put("/emitSql", s);
        }


    }

    public static class FileTypeFilter {
        final String type;
        final String descr;
        final String buttonTxt;
        final JFrame jframe;

        FileTypeFilter(String type, String descr, String buttonTxt, JFrame jframe) {
            this.type = type;
            this.descr = descr;
            this.buttonTxt = buttonTxt;
            this.jframe = jframe;
        }

        private File pickFile(String cur) {
            JFileChooser dbFile = new JFileChooser(cur);
            dbFile.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (pathname.isDirectory()) return true;
                    if (pathname.getName().toLowerCase().endsWith(type)) {
                        return true;
                    }
                    return false;
                }

                @Override
                public String getDescription() {
                    return descr;
                }
            });
            dbFile.setApproveButtonText(buttonTxt);
            dbFile.showDialog(jframe, null);
            return dbFile.getSelectedFile();

        }

    }

    private String addDefaultUserAndPasswordOnSQLServer(final String inurl) {
        String url = inurl;
        if (!url.contains("user")) {
            url = url + ";user=emit";
        }
        if (!url.contains("password")) {
            url = url + ";password=time";
        }
        return url;

    }


    private void verifyDataSource(RegistryPreferences prefs, JTextField furl) {
        String url;
        if ("Emit".equals(prefs.getDbType())) {

            url = calculateJdbcUrl(prefs, furl);
            globalJdbc = sunJdbcDriver;
            globalJdbcUrl = url;
            int c = 0;
            configStatus.setText("Not yet successful");
            configStatus.setForeground(Color.RED);
            try {
                BasicDataSource ds = new BasicDataSource();
                ds.setDriverClassName(sunJdbcDriver);
                ds.setUrl(url);
                JdbcTemplate sjt = new JdbcTemplate(ds);
                c = sjt.queryForObject("select count(*) from arr", Integer.class);
                if (c > 0) {
                    String name = findName(sjt);
                    configStatus.setText("OK! " + name);
                    configStatus.setForeground(Color.GREEN);
                    globalTitle = name;

                } else {
                    configStatus.setForeground(Color.RED);

                }


                butOk.setEnabled(true);
            } catch (Exception ec) {
                log.error("Couldn't run: " + ec);
            }
        } else if ("EmitSQL".equals(prefs.getDbType())) {

            //url="jdbc:sqlserver://steffensberget:51757;databaseName=VMTest2016Test1_2016;user=emit;password=time";
            String dbName = (String) emitSqlDb.getSelectedItem();
            url = emitSql.getText() + ";databaseName=" + dbName;
            url = addDefaultUserAndPasswordOnSQLServer(url);

            globalJdbc = sqlServerJdbcDriver;
            globalJdbcUrl = url;

            furl.setText(url);
            log.info("DSN: " + url);
            int c = 0;
            configStatus.setText("Not yet successful");
            configStatus.setForeground(Color.RED);
            try {
                BasicDataSource ds = new BasicDataSource();
                ds.setDriverClassName(sqlServerJdbcDriver);
                ds.setUrl(url);
                JdbcTemplate sjt = new JdbcTemplate(ds);
                c = sjt.queryForObject("select count(*) from arr", Integer.class);
                if (c > 0) {
                    String name = findName(sjt);
                    configStatus.setText("OK! " + name);
                    if (title.getText().length() == 0) {
                        title.setText(name);
                    }
                    configStatus.setForeground(Color.GREEN);
                    globalTitle = title.getText();
                    butOk.setEnabled(true);
                    return;
                } else {
                    log.warn("Something strange during startup.");
                    if (c == 0) {
                        log.fatal("The database does not contain any competition");
                    }
                }

            } catch (Exception ec) {
                log.error("Couldn't run: " + ec);
            }
        }


    }

    private String calculateJdbcUrl(RegistryPreferences prefs, JTextField furl) {
        String driver;

        if ("64".equals(prefs.getMode64())) {
            driver = "Microsoft Access Driver (*.mdb, *.accdb)";
        } else {
            driver = "Microsoft Access Driver (*.mdb)";
        }
        String url = String.format("jdbc:odbc:Driver={%s};dbq=%s;SystemDB=%s;UID=admin",
                driver, prefs.getDbFile(), prefs.getSysFile()).replaceAll("\\\\", "/");
        furl.setText(url);
        return url;
    }

    private String findName(JdbcTemplate sjt) {
        return sjt.queryForObject("select name from arr", String.class);
    }


    private class ArrInfo {
        public final String name;
        public final boolean tenths;

        public ArrInfo(String name, int tenths) {
            this.name = name;
            this.tenths = tenths == 1 ? true : false;
        }
    }


    private class FetchEmitSqlDbEventsWorker extends SwingWorker<List<String>, Void> {

        String dsn;
        JComboBox dblist;

        public FetchEmitSqlDbEventsWorker(String emitSql, JComboBox emitSqlDb) {
            dsn = emitSql;
            dblist = emitSqlDb;
            log.info("fetch emit sql db");
        }

        @Override
        protected void done() {
            List<String> result = null;
            try {
                result = get();
            } catch (InterruptedException | ExecutionException e) {
                log.warn("couldn't connect to SQL Server", e);
            }
            emitSqlDb.removeAllItems();
            for (String s : result) {
                emitSqlDb.addItem(s);
            }
        }

        @Override
        protected List<String> doInBackground() throws Exception {
            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName(sqlServerJdbcDriver);
            ds.setUrl(addDefaultUserAndPasswordOnSQLServer(dsn));
            log.info("fetch emit sql db, try to connect to " + dsn);
            JdbcTemplate sjt = new JdbcTemplate(ds);
            List<Map<String, Object>> res = sjt.queryForList("SELECT name, database_id, create_date FROM sys.databases");
            log.info("got results " + res);
            List<String> result = new ArrayList<>();
            for (Map<String, Object> e : res) {
                result.add((String) e.get("name"));
            }
            return result;
        }


    }

}
