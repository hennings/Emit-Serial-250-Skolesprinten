package net.spjelkavik.emit.emitag;

import org.apache.log4j.Logger;

public class EmitagConfig {
    private final String db;
    private final String system;
    private final String ecardField;
    private final String comPort;
    private final String jdbcUrl;
    private final String jdbcDriver;
    private final String title;
    private final boolean isRelay;
    private final Mode mode;
    private final RaceType raceType;
    private final CardType cardType;

    enum RaceType { INDIVIDUAL, RELAY }
    enum Mode { ASSIGN, VERIFY }
    enum CardType { EMITAG, EPT }

    private final Logger LOG = Logger.getLogger(EmitagConfig.class);
    private String dbDir;

    public EmitagConfig(String globalTitle, String db, String system, String comPort, String ecardField,
                        String jdbcUrl, String jdbcDriver, boolean isRelay, boolean verify, CardType cardType) {
        this.db = db;
        this.system = system;
        this.comPort = comPort;
        this.ecardField = ecardField;
        this.jdbcUrl = jdbcUrl;
        this.jdbcDriver = jdbcDriver;
        this.title = globalTitle;
        this.isRelay = isRelay;
        this.raceType = isRelay ? RaceType.RELAY : RaceType.INDIVIDUAL;
        this.mode = verify ? Mode.VERIFY : Mode.ASSIGN;
        this.cardType = cardType;
    }

    public Mode getMode() {
        return mode;
    }

    public RaceType getRaceType() {
        return raceType;
    }

    public boolean isRelay() {
        return isRelay;
    }

    public String getDb() {
        return db;
    }

    public String getSystem() {
        return system;
    }

    public EcardField getEcardField() {
        if ("ecard1".equals(ecardField)) {
            return EcardField.ECARD1 ;
        } else if ("ecard2".equals(ecardField)) {
            return EcardField.ECARD2;
        }
        LOG.error("Not a valid ecardfield: " + ecardField);
        return EcardField.ECARD1;
    }

    public String getTitle() {
        return title;
    }

    public String getComPort() {
        return comPort;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public CardType getCardType() {
        return cardType;
    }

    @Override
    public String toString() {
        return "EmitagConfig{" +
                "db='" + db + '\'' +
                ", system='" + system + '\'' +
                ", ecardField='" + ecardField + '\'' +
                ", comPort='" + comPort + '\'' +
                ", jdbcUrl='" + jdbcUrl + '\'' +
                '}';
    }

    public String getDbDir() {
        if (db.contains("etime.mdb")) {
            return db.replace("etime.mdb","");
        } else {
            return "c:/temp";
        }
    }
}
