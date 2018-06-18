package net.spjelkavik.emit.emitag;

import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EmitagMessageParserTest {

     /*

Protocol 0:
D 07 07 N3342409        Y878100391      M22466  C70     E12:00:35.128   T01:38:26.781
IECU-HW1-SW5-V1.73      M1-22467        Y878100391      W12:00:39.112   C70    X0


      */

    private static final Logger LOG = Logger.getLogger(EmitagMessageParserTest.class);

    @Test
    public void testParseSingleMessage() throws Exception {
        ECBMessage m = new ECBMessage();
        EmitagMessageParser parser = new EmitagMessageParser();
        parser.parseMessage(m, "N3350998");
        assertEquals(3350998, m.getEmitagNumber());

        parser.parseMessage(m, "Y878100246");
        assertEquals("878100246", m.getSerial());

        parser.parseMessage(m, "S878100246");
        assertEquals(878100246, m.getSerialNumber());

    }

    @Test
    public void testParseAFrame() throws Exception{
        String frameEmitagDump = "D 08 09\tN3342409\tY878100391\tW13:16:06.668\tV300-2082\tS3342409\tRemiTag v5\tL0097\tX7\tQ0-0-69792681528-00:00:00.000-06:47:45.606-133\tQ1-103-69792758035-00:01:16.507-06:49:02.113-133\tQ2-160-69792763296-00:01:21.768-06:49:07.374-133\tQ3-90-69792929222-00:04:07.694-06:51:53.300-133\tQ4-250-69793359972-00:11:18.444-06:59:04.050-133\tQ5-250-69793366472-00:11:24.944-06:59:10.550-133\tQ6-250-69793372035-00:11:30.507-06:59:16.113-133\tQ7-250-69793377597-00:11:36.069-06:59:21.675-133\tQ8-250-69793383348-00:11:41.820-06:59:27.426-133\tQ9-250-69793389473-00:11:47.945-06:59:33.551-133\tQ10-250-69793395477-00:11:53.949-06:59:39.555-133\tQ11-250-69793401160-00:11:59.632-06:59:45.238-133\tQ12-250-69793418222-00:12:16.694-07:00:02.300-133\tQ13-90-69793465535-00:13:04.007-07:00:49.613-133\tQ14-90-69793534596-00:14:13.068-07:01:58.674-133\tQ15-250-69793678035-00:16:36.507-07:04:22.113-133\tQ16-250-69793714597-00:17:13.069-07:04:58.675-133\tQ17-250-69793791597-00:18:30.069-07:06:15.675-133\tQ18-250-69793904785-00:20:23.257-07:08:08.863-133\tQ19-90-69794243535-00:26:02.007-07:13:47.613-133\tQ20-250-69795165098-00:41:23.570-07:29:09.176-133\tQ21-250-69795182226-00:41:40.698-07:29:26.304-133\tQ22-250-69795188472-00:41:46.944-07:29:32.550-133\tQ23-250-69795194660-00:41:53.132-07:29:38.738-133\tQ24-250-69795200910-00:41:59.382-07:29:44.988-133\tQ25-250-69795207288-00:42:05.760-07:29:51.366-133\tQ26-250-69795213785-00:42:12.257-07:29:57.863-133\tQ27-250-69795926285-00:54:04.757-07:41:50.363-133\tQ28-250-77007947285-06:14:25.757-19:02:11.363-50\tQ29-250-77009119909-06:33:58.381-19:21:43.987-50\tQ30-250-77009179164-06:34:57.636-19:22:43.242-50\tQ31-250-77009547536-06:41:06.008-19:28:51.614-50\tQ32-250-77009577224-06:41:35.696-19:29:21.302-50\tQ33-250-77010007535-06:48:46.007-19:36:31.613-50\tQ34-250-77010016411-06:48:54.883-19:36:40.489-50\tQ35-250-77010021473-06:48:59.945-19:36:45.551-50\tQ36-250-81302615909-200:12:14.381-11:59:59.987-0\tQ37-250-81302648597-200:12:47.069-12:00:32.675-0\tQ38-250-81307182347-201:28:20.819-13:16:06.425-0\t";
        ECBMessage m = parseAFrame(frameEmitagDump);
        assertEquals("emitagnumber", 3342409, m.getEmitagNumber());
        LOG.info(m);

        frameEmitagDump = "D 07 07\tN3342409\tY878100391\tM22466\tC70\tE12:00:35.128\tT01:38:26.781";
        m = parseAFrame(frameEmitagDump);
        assertEquals("emitagnumber", 3342409, m.getEmitagNumber());


    }

    private ECBMessage parseAFrame(String frameEmitagDump) {
        EmitagMessageParser parser = new EmitagMessageParser();
        EmitagFrame frame =  new EmitagFrame();
        frame.initBytes(frameEmitagDump);
        return parser.parse(frame);
    }
}
