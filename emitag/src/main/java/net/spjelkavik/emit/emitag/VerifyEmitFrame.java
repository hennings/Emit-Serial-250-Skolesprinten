package net.spjelkavik.emit.emitag;

import net.miginfocom.swing.MigLayout;
import net.spjelkavik.emit.common.CardNumberReader;
import net.spjelkavik.emit.common.EtimingReader;
import net.spjelkavik.emit.common.Frame;
import net.spjelkavik.emit.common.SeriousLogger;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import java.util.Map;

public class VerifyEmitFrame extends JFrame implements ActionListener, EmitMessageListener {


    final static Logger log = Logger.getLogger(VerifyEmitFrame.class);

    /**
     *
     */
    private static final long serialVersionUID = 4059084342591755190L;
    private final EcardField ecardField;
    private final EmitagConfig emitagConfig;
    private final SeriousLogger seriousLogger;
    private EtimingReader etimingReader;
    private CardNumberReader ecbMessage;
    private String comStatus;

    private static final int fontBaseSize = 16;


    private JButton saveDataButton;
    private int musicNumber = 0;
    private JLabel runnerNameLabel;
    private JLabel clubNameLabel;
    private JTextField brikkeField;
    private JLabel statusLabel;

    private JLabel brikkeNrLestLabel;
    private JLabel brikkeNrLabel1;
    private JLabel brikkeNrLabelInDb;
    private JButton fetchRunnerButton;
    private JButton updateBrikkeButton;
    private Frame frame;

    private JLabel runnerTimeLabel;

    private JLabel comStatusLabel;

    private JLabel prevLabel;

    private static final String fontName ="Tahoma";

    public int getBadgeNumber() {
        return NumberUtils.toInt(brikkeField.getText(), -1);
    }



    private void clearStatus() {
        statusLabel.setText("new");

    }


    Timer timer = new Timer(5000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            cleanRunner();
        }
    });


    public VerifyEmitFrame(EmitagConfig config) {

        //give the window a name
        super("Verify emitag - " + config.getDb() + " - " + config.getEcardField());


        this.timer.setRepeats(false);
        this.emitagConfig = config;
        this.ecardField = config.getEcardField();


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String hostname = System.getenv().get("COMPUTERNAME");
        if (hostname == null) {
            hostname = "default";
        }

        String prefix = emitagConfig.getDbDir();

        File logfile = new File(prefix + "log-verify-" + hostname + ".txt");

        this.seriousLogger = new SeriousLogger(hostname + ";" + config.getTitle(), logfile, null, "http://project.spjelkavik.net/logger/");


        this.setMinimumSize(new Dimension(700, 450));

        JPanel all = new JPanel(new MigLayout("gap 10px 10px"));
        this.add(all);
        all.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel(config.getTitle());

        Font jfbig = new Font(fontName, Font.PLAIN, fontBaseSize * 3);

        title.setFont(jfbig);
        all.add(title, "grow,span, wrap");


        runnerNameLabel = new JLabel();
        runnerNameLabel.setFont(jfbig);
        clubNameLabel = new JLabel();
        clubNameLabel.setFont(jfbig);
        runnerTimeLabel = new JLabel("00:00:00");
        runnerTimeLabel.setFont(new Font(fontName, Font.BOLD, fontBaseSize));
        runnerNameLabel.setPreferredSize(new Dimension(650, 40));
        clubNameLabel.setPreferredSize(new Dimension(650, 40));

        statusLabel = new JLabel("Startup!");

        comStatusLabel = new JLabel("Port");



        brikkeNrLestLabel = new JLabel("lest brikkenr");
        brikkeNrLestLabel.setFont(new Font(fontName, Font.PLAIN, fontBaseSize + 4));

        brikkeNrLabel1 = new JLabel("db brikkenr");
        brikkeNrLabel1.setFont(new Font(fontName, Font.PLAIN, fontBaseSize + 4));

        brikkeNrLabelInDb = new JLabel("db brikkenr ekstra");
        brikkeNrLabelInDb.setFont(new Font(fontName, Font.PLAIN, fontBaseSize - 2));

        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Current Runner"));

        all.add(panel, "span, wrap");

        panel.add(runnerNameLabel, "grow,span 2");
        panel.add(runnerTimeLabel, "wrap");
        panel.add(clubNameLabel, "grow,span, wrap");
        panel.add(brikkeNrLabelInDb, "span, wrap");
        panel.add(brikkeNrLabel1, "span, wrap");
        panel.add(statusLabel, "span, wrap");



        brikkeField = new JTextField(16);

        all.add(new JLabel("Ecard:"));
        all.add(brikkeField, "wrap");
        brikkeField.addActionListener(this);


        all.add(new JLabel("Lest brikkenr:"));
        all.add(brikkeNrLestLabel, "wrap");

        JPanel prevPanel = new JPanel(new MigLayout());
        prevPanel.setBorder(BorderFactory.createTitledBorder("Log"));

        prevPanel.add(statusLabel, "wrap");
        prevPanel.add(new JLabel("Previous:"));
        all.add(comStatusLabel, "wrap");


        //display the window
        this.pack();
        this.setVisible(true);

        brikkeField.requestFocus();
    }

    public void cleanRunner() {
        runnerNameLabel.setText("...");
        clubNameLabel.setText("...");
        currentState.name = "...";
        currentState.stnr = 0;
        brikkeNrLabelInDb.setText("");
        brikkeNrLabel1.setText("");
        brikkeNrLestLabel.setText("");
        brikkeField.setText("");
    }


    public void updateRunner() {
        clearStatus();

        timer.stop();

        int ecard = getBadgeNumber();
        Map<String, String> runner = etimingReader.getRunnerByEcard(ecard);
        if (runner != null) {
//            runnerNameLabel.setText("<html><h1>"+runner.get("startno")+" " + runner.get("name")+" " + runner.get("ename")+"</h1>");
            String stnr = runner.get("startno");


            if (emitagConfig.isRelay()) {
                int startno = NumberUtils.toInt(stnr, 0) / 100;
                int leg = NumberUtils.toInt(stnr, 0) % 100;
                runnerNameLabel.setText(startno + "-" + leg + " " + runner.get("name") + " " + runner.get("ename"));
                clubNameLabel.setText(runner.get("team_name") + "; Leg " + runner.get("seed"));
            } else {
                int startno = NumberUtils.toInt(stnr, 0);
                runnerNameLabel.setText(startno + " " + runner.get("name") + " " + runner.get("ename"));
                clubNameLabel.setText(runner.get("team_name"));
            }
            if (EcardField.ECARD2.equals(ecardField)) {
                brikkeNrLabelInDb.setText("In db: ecard " + runner.get("ecard") +
                        " / ecard2 " + runner.get("ecard2"));
            } else {
                brikkeNrLabelInDb.setText("In db: ecard " + runner.get("ecard"));
            }
            currentState.name = runner.get("name") + " " + runner.get("ename") + " / " + runner.get("team_name");
            setRunningTime(StringUtils.trimToEmpty(runner.get("times")) + "  " + StringUtils.trimToEmpty(runner.get("place")));
            seriousLogger.logMessageToDisk("For ecard " + ecard + " found " + stnr +", " + currentState.name);
        } else {
            seriousLogger.logMessageToDisk("For ecard " + ecard + " did not find anything");
            runnerNameLabel.setText("Unknown...");
            clubNameLabel.setText("Unknown...");
            currentState.name = "Unknown...";
            currentState.stnr = 0;
        }
        timer.restart();

    }

    public void handleCardMessage(CardNumberReader f) {
        this.ecbMessage = f;
        if (f.getCardNumber() > 0) {
            this.setBadgeNumber(f.getCardNumber());
            //this.setRunningTime(f.getTimeSinceZero());
            log.info("read badge number");
            updateRunner();
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

    @Override

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == brikkeField) {
            updateRunner();
            brikkeField.selectAll();
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
