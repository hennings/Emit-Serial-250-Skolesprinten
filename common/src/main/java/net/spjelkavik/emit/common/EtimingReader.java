package net.spjelkavik.emit.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class EtimingReader {

    static Logger log = Logger.getLogger(EtimingReader.class.getName());

    JdbcTemplate jdbcTemplate;
    private SeriousLogger seriousLogger;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    private String lastStatus;

    public String getLastStatus() {
        return lastStatus;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean updateResults(int startNumber, int badgeNumber) {

        renameOldEcard(badgeNumber);

        log.debug("update new set ecard="+badgeNumber+" where startno="+startNumber);

        try {
            int r2 = jdbcTemplate.update("update name set ecard=? where startno=?",
                    new Object[]{badgeNumber, startNumber});
            log.debug("database updated " + r2);
        } catch (DuplicateKeyException dke) {
            log.error("Update ecard failed", dke);
            seriousLogger.logMessageToDisk("Could not update ecard: " + dke);
            lastStatus = dke.toString();
            return false;
        }

        lastStatus = "OK";
        return true;
    }

    public boolean updateResultsEcard2(int startNumber, int badgeNumber) {

        renameOldEcard2(badgeNumber);

        log.debug("update new set ecard2="+badgeNumber+" where startno="+startNumber);

        int r2 = jdbcTemplate.update("update name set ecard2=? where startno=?",
                new Object[] {badgeNumber,startNumber} );

        log.debug("database updated " + r2);
        lastStatus = "OK";

        return true;
    }

    public boolean renameOldEcard(int ecard) {
        try {

            List<Integer> res = jdbcTemplate.queryForList("select startno as startnr from name where ecard=?", Integer.class, ecard);

            if (res.size()==0) {
                log.debug("no need to rename");
                return true;
            }

            Integer startno = res.get(0);
            if (startno!=null) {
                log.warn("Existing runner " + startno + " had ecard " + ecard + " => changed the ecard");
                int newEcard = startno + 10000000;
                jdbcTemplate.update("update name set ecard=? where ecard=?",
                        new Object[] {newEcard, ecard} );
                int r1 = jdbcTemplate.update("update ecard set ecardno=? where ecardno=?",
                        new Object[] {newEcard, ecard} );
                seriousLogger.logMessageToDisk("Renamed ecard in 'name' and 'ecard' for startno " + startno + "; old="+ecard+" -> "+newEcard);
            } else {
                int newEcard = -ecard;
                jdbcTemplate.update("update name set ecard=? where ecard=?",
                        new Object[] {newEcard, ecard} );
                jdbcTemplate.update("update name set ecard=? where ecard=?",
                        new Object[] {newEcard, ecard} );
                seriousLogger.logMessageToDisk("Renamed ecard in 'name' and 'ecard' ; old="+ecard+" -> "+newEcard);
            }
        }catch (Exception e) {
            log.error("could not rename ", e);
            seriousLogger.logMessageToDisk("Failed " + e.toString());
            lastStatus = e.toString();
            return false;
        }
        lastStatus = "OK";

        return true;
    }

    public boolean renameOldEcard2(int ecard) {
        try {
            int startno = jdbcTemplate.queryForObject("select max(startno) as startnr from name where ecard2=?",Integer.class, ecard);
            if (startno>0) {
                log.warn("Existing runner " + startno + " had ecard2 " + ecard + " => changed the ecard");
                int newEcard = startno + 10000000;
                jdbcTemplate.update("update name set ecard2=? where ecard2=?",
                        new Object[] {newEcard, ecard} );
                seriousLogger.logMessageToDisk("Renamed ecard2 in 'name' for startno " + startno + "; old="+ecard+" -> "+newEcard);

            }
        }catch (Exception e) {
            log.error("could not rename ", e);
            lastStatus = e.toString();
            return false;

        }
        lastStatus = "OK";

        return true;
    }


    public boolean updateEcardAnonymous(int startNumber, int ecard) {
        int newEcard = startNumber;
        if (newEcard<200000) newEcard=startNumber + 200000;

        log.debug("rename old");
        int r0 = jdbcTemplate.update("update name set ecard=? where ecard=?",
                new Object[] {newEcard, ecard} );
        int r1 = jdbcTemplate.update("update ecard set ecardno=? where ecardno=?",
                new Object[] {newEcard, ecard} );

        log.debug("update new set ecard="+ecard+" where startno="+startNumber);
        int r2 = jdbcTemplate.update("update name set ecard=? where startno=?",
                new Object[] {ecard,startNumber} );
        log.debug("database updated " + r0+", " + r1+", "+r2);

        jdbcTemplate.execute("select * from Name where ecard=" + ecard);
        lastStatus = "OK";

        if (r2>0) return true;
        return false;
    }

    RowMapper rowMapperRunner = new RowMapper() {

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
            final Map<String, String> result = new HashMap<String, String>();
            result.put("id", rs.getString("id"));
            result.put("name", rs.getString("name"));
            result.put("ename", rs.getString("ename"));
            result.put("ecard", rs.getString("ecard"));
            result.put("ecard2", rs.getString("ecard2"));
            result.put("startno", rs.getString("startno"));
            result.put("seed", rs.getString("seed"));
            result.put("times", rs.getString("times"));
            result.put("place", rs.getString("place"));
            result.put("team_name", rs.getString("team_name"));
            return result;
        }

    };




    @SuppressWarnings("unchecked")
    public Map<String,String> getRunner(int startNumber) {

        List<Map<String,String>> r = jdbcTemplate.query(
                "select n.id,n.ename, n.name,n.times, n.seed, n.place, n.class, n.cource, n.starttime, "+
                        " n.status, n.statusmsg, n.startno, n.ecard2, n.times, n.place, "+
                        " n.intime, n.ecard, n.changed, n.team, t.name as team_name from name n, team t "+
                        " where n.team=t.code and n.startno=? "
                ,new Object[] { new Integer(startNumber)}, rowMapperRunner);

        log.info("template returned: " + r);
        if (r.size()>0) return r.get(0);
        return null;
    }

    @SuppressWarnings("unchecked")
    public Map<String,String> getRunnerByEcard(int ecard) {

        List<Map<String,String>> r = jdbcTemplate.query(
                "select n.id,n.ename, n.name,n.times, n.seed, n.place, n.class, n.cource, n.starttime, "+
                        " n.status, n.statusmsg, n.startno, n.ecard2, n.times, n.place, "+
                        " n.intime, n.ecard, n.changed, n.team, t.name as team_name from name n, team t "+
                        " where n.team=t.code and n.ecard=? or n.ecard2=? "
                ,new Object[] { new Integer(ecard), new Integer(ecard) }, rowMapperRunner);

        log.info("template returned: " + r);
        if (r.size()>0) return r.get(0);
        return null;
    }


    public boolean updateResults(int startNumber, Frame frame) {
        throw new IllegalStateException("skolemesterskapet");
    }

    public void setSeriousLogger(SeriousLogger seriousLogger) {
        this.seriousLogger = seriousLogger;
    }

    public SeriousLogger getSeriousLogger() {
        return seriousLogger;
    }
}
