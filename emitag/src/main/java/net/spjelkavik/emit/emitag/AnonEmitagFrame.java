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
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;


import static net.spjelkavik.emit.emitag.CommonConstants.fontBaseSize;
import static net.spjelkavik.emit.emitag.CommonConstants.fontName;

public class AnonEmitagFrame extends JFrame implements ActionListener, EmitagMessageListener {



    final static Logger log = Logger.getLogger(AnonEmitagFrame.class);

    /**
     *
     */
    private static final long serialVersionUID = 4059084342591755190L;
    private final EcardField ecardField;
    private final EmitagConfig emitagConfig;
    private final SeriousLogger seriousLogger;
    private EtimingReader etimingReader;
    private ECBMessage ecbMessage;
    private String comStatus;


    private JButton saveDataButton;
    private int musicNumber = 0;
    private JLabel runnerNameLabel;
    private JLabel clubNameLabel;
    private JTextField startNumberField;
    private JTextField brikkeField;
    private JLabel statusLabel;

    private JLabel brikkeNrLestLabel;
    private JLabel brikkeNrLabel1;
    private JLabel brikkeNrLabelInDb;
    private JButton fetchRunnerButton;
    private JButton updateBrikkeButton;
    private Frame frame;

    public void actionPerformed(ActionEvent ae){
        if (ae.getSource() == saveDataButton) {
            updateDatabase();

        }
        if (ae.getSource() == startNumberField) {
            System.out.println("Startnumber action!");
            updateRunner();
            startNumberField.selectAll();
        }
        if (ae.getSource() == fetchRunnerButton) {
            System.out.println("Pushed");
            //update the counter of button presses
            musicNumber++;
            System.out.println("The button has been pressed " +
                    musicNumber + " times");
            updateRunner();
        }

    }



    private JLabel runnerTimeLabel;

    private JLabel comStatusLabel;

    private JLabel prevLabel;

    private void updateDatabase() {
        System.err.println("update database");
        if (getBadgeNumber()<1) {
            log.error("No tag number!");
            statusLabel.setText("no tag number");
            return;
        }
        if (getStartNumber()<1) {
            log.error("No start number!");
            statusLabel.setText("no start number");
            return;
        }

        String logMessage = "Update: stnr " + getStartNumber() + " = emitag " + getBadgeNumber() + " = " +
                new Date().toString();
        seriousLogger.logMessageToDisk(logMessage);



        boolean ok;
        if (EcardField.ECARD2.equals(emitagConfig.getEcardField())) {
            ok = etimingReader.updateResultsEcard2(getStartNumber(), getBadgeNumber());
        } else {
            ok = etimingReader.updateResults(getStartNumber(), getBadgeNumber());
        }
        statusLabel.setText(etimingReader.getLastStatus());

        if (ok) {

            currentState.ecard = getBadgeNumber();
            currentState.stnr = getStartNumber();

            logArea.append( currentState.toString() +   "\n");
            prevLabel.setText("previous: " + brikkeNrLestLabel.getText()+", " + startNumberField.getText());
            statusLabel.setText("Stored!");
            brikkeNrLestLabel.setText("");
            brikkeNrLabel1.setText("");
            brikkeField.setText("");
            runnerNameLabel.setText(".");
            runnerTimeLabel.setText("");
            clubNameLabel.setText(".");
            clubNameLabel.setText("");
            brikkeNrLabelInDb.setText("");
            frame = null;

        } else {
            if (StringUtils.isEmpty(statusLabel.getText())) {
                statusLabel.setText("Not found");
            }
        }
    }


    public int getBadgeNumber() {
        return NumberUtils.toInt(brikkeField.getText(), -1);
    }


    public int getStartNumber() {
        String text = startNumberField.getText();
        int nr;
        if (text.length()==8) {
            nr = NumberUtils.toInt(text.substring(0,6),0);
        } else if (text.length()==6) {
            nr = NumberUtils.toInt(text.substring(0,5),0);
        } else {
            nr = NumberUtils.toInt(text,0);
        }
        return nr;
    }


    public void updateRunner() {
        clearStatus();


        Map<String, String> runner = etimingReader.getRunner(getStartNumber());
        if (runner!=null) {
//            runnerNameLabel.setText("<html><h1>"+runner.get("startno")+" " + runner.get("name")+" " + runner.get("ename")+"</h1>");
            String stnr = runner.get("startno");


            if  (emitagConfig.isRelay()) {
                int startno = NumberUtils.toInt(stnr,0)/100;
                int leg = NumberUtils.toInt(stnr,0)%100;
                runnerNameLabel.setText(startno+"-" + leg +" " + runner.get("name")+" " + runner.get("ename"));
                clubNameLabel.setText(runner.get("team_name")+"; Leg " + runner.get("seed"));
            } else {
                int startno = NumberUtils.toInt(stnr,0);
                runnerNameLabel.setText(startno+" " + runner.get("name")+" " + runner.get("ename"));
                clubNameLabel.setText(runner.get("team_name"));
            }
            if (EcardField.ECARD2.equals(ecardField)) {
                brikkeNrLabelInDb.setText("In db: ecard " + runner.get("ecard") +
                        " / ecard2 " + runner.get("ecard2"));
            } else {
                brikkeNrLabelInDb.setText("In db: ecard " + runner.get("ecard"));
            }
            currentState.name = runner.get("name")+" " + runner.get("ename") + " / " + runner.get("team_name");
        } else {
            runnerNameLabel.setText("Unknown...");
            clubNameLabel.setText("Unknown...");
            currentState.name="Unknown...";
            currentState.stnr = getStartNumber();
        }

    }
    private void clearStatus() {
        statusLabel.setText("new");

    }

    JTextArea logArea = new JTextArea();
    JScrollPane sp;

    public AnonEmitagFrame(EmitagConfig config) {

        //give the window a name
        super("Anonyme emitag - " + config.getDb() + " - " + config.getEcardField());


        this.emitagConfig = config;
        this.ecardField = config.getEcardField();


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String hostname = System.getenv().get("COMPUTERNAME");
        if (hostname==null) { hostname="default";   }

        String prefix = emitagConfig.getDbDir();

        File logfile = new File(prefix + "log-brikkenr-"+hostname+".txt");
        File logfile2 = new File("log-brikkenr-2-"+hostname+".txt");

        this.seriousLogger = new SeriousLogger(hostname+";"+config.getTitle(), logfile, logfile2, "http://project.spjelkavik.net/logger/");


        JPanel all = new JPanel(new MigLayout("gap 5px 5px"));
        this.add(all);
        all.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel(config.getTitle());

        Font jfbig = new Font(fontName, Font.PLAIN, fontBaseSize+8);

        title.setFont(jfbig);
        all.add(title, "grow,span, wrap");


        runnerNameLabel = new JLabel();
        runnerNameLabel.setFont(jfbig);
        clubNameLabel= new JLabel();
        clubNameLabel.setFont(jfbig);
        runnerTimeLabel = new JLabel("00:00:00");
        runnerTimeLabel.setFont(new Font(fontName, Font.BOLD, fontBaseSize));
        runnerNameLabel.setPreferredSize(new Dimension(650,40));
        clubNameLabel.setPreferredSize(new Dimension(650,40));

        //add the button
        saveDataButton = new JButton("Oppdater - F4");
        saveDataButton.setSize(new Dimension(120,60));
        saveDataButton.setBackground(Color.GREEN);
        saveDataButton.addActionListener(this);

        statusLabel = new JLabel("Startup!");

        comStatusLabel= new JLabel("Port");


//		logArea.setPreferredSize(new Dimension(500,150));
        logArea.setFocusable(false);
        logArea.setFont(new Font("Courier New", Font.PLAIN, fontBaseSize-4));
        logArea.setLineWrap(false);

        sp = new JScrollPane(logArea);
        sp.setPreferredSize(new Dimension(640,150));


        brikkeNrLestLabel = new JLabel("lest brikkenr");
        brikkeNrLestLabel.setFont(new Font(fontName, Font.PLAIN, fontBaseSize+4));

        brikkeNrLabel1 = new JLabel("db brikkenr");
        brikkeNrLabel1.setFont(new Font(fontName, Font.PLAIN, fontBaseSize+4));

        brikkeNrLabelInDb = new JLabel("db brikkenr ekstra");
        brikkeNrLabelInDb.setFont(new Font(fontName, Font.PLAIN, fontBaseSize-2));

        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Current Runner"));

        all.add(panel, "span, wrap");

        panel.add(runnerNameLabel, "grow,span 2");
        panel.add(runnerTimeLabel, "wrap");
        panel.add(clubNameLabel, "grow,span, wrap");
        panel.add(brikkeNrLabelInDb, "span, wrap");
        panel.add(brikkeNrLabel1, "span, wrap");
        panel.add(statusLabel, "span, wrap");


        fetchRunnerButton = new JButton("Hent loper");
        fetchRunnerButton.addActionListener(this);
        all.add(fetchRunnerButton, "wrap");

        all.add(saveDataButton, "wrap");

        prevLabel = new JLabel("previous, previous");
        prevLabel.setFont(new Font(fontName, Font.PLAIN, fontBaseSize-4));


        startNumberField = new JTextField(16);
        brikkeField = new JTextField("brikkedefault", 16);

        all.add(new JLabel("Startnummer:"));
        all.add(startNumberField, "wrap");
        startNumberField.addActionListener(this);


        all.add(new JLabel("Lest brikkenr:"));
        all.add(brikkeNrLestLabel, "wrap");

        all.add(new JLabel("Manuell brikke:"));
        all.add(brikkeField,"wrap");

        updateBrikkeButton = new JButton("Manuell brikke");
        updateBrikkeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int brikkeNr = NumberUtils.toInt(brikkeField.getText(), 0);
                setBadgeNumber(brikkeNr);
                startNumberField.requestFocus();
                startNumberField.selectAll();
            }
        });
        all.add(updateBrikkeButton,"wrap");

        JPanel prevPanel = new JPanel(new MigLayout());
        prevPanel.setBorder(BorderFactory.createTitledBorder("Log"));

        prevPanel.add(statusLabel, "wrap");
        prevPanel.add(new JLabel("Previous:"));
        prevPanel.add(prevLabel, "wrap");
        prevPanel.add(sp, "span, grow, wrap");
        all.add(prevPanel, "span, wrap");
        all.add(comStatusLabel, "wrap");


        InputMap keyMap = new ComponentInputMap(saveDataButton);
        keyMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), "action");

        ActionMap actionMap = new ActionMapUIResource();
        actionMap.put("action", new UpdateAction("updater"));



        SwingUtilities.replaceUIActionMap(saveDataButton, actionMap);
        SwingUtilities.replaceUIInputMap(saveDataButton, JComponent.WHEN_IN_FOCUSED_WINDOW, keyMap);


        //display the window
        this.pack();
        this.setVisible(true);

        startNumberField.requestFocus();
    }

    @Override
    public void handleECBMessage(ECBMessage f) {
        this.ecbMessage = f;
        if (f.getEmitagNumber()>0) {
            this.setBadgeNumber(f.getEmitagNumber());
            this.setRunningTime(f.getTimeSinceZero());
            log.info("read badge number");
        } else {
            log.info("irrelevant frame " + f);
        }
    }

    private void setRunningTime(String runningTime) {
        this.runnerTimeLabel.setText(runningTime);
    }

    public void setBadgeNumber(int badge) {
        clearStatus();
        brikkeNrLestLabel.setText("emitag " + badge);
        brikkeNrLabel1.setText("emitag " + badge);
        brikkeField.setText("" + badge);
    }

    public void setEtimingReader(EtimingReader etimingReader) {
        this.etimingReader = etimingReader;
        etimingReader.setSeriousLogger(seriousLogger);
    }

    public void setComStatus(String comStatus) {
        this.comStatusLabel.setText(comStatus);
    }


    class UpdateAction extends AbstractAction {
        public UpdateAction(String text) {
            super(text);
        }
        public void actionPerformed(ActionEvent e) {
            System.err.println("Action for first button/menu item: "+ e);
            updateDatabase();
        }
    }

    final CurrentState currentState = new CurrentState();

    class CurrentState {
        String name;
        String time;
        int ecard;
        int stnr;

        public String toString() {
            return String.format("%-20s ecard %-8d  stnr %6d %s", name, ecard, stnr, new Date().toString());
        }

    }

}
