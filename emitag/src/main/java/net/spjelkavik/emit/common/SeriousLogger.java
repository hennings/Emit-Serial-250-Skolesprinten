package net.spjelkavik.emit.common;

import okhttp3.*;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * User: hennings
 * Date: 13.09.2014
 * Time: 18:20
 */
public final class SeriousLogger {

    final private OkHttpClient client = new OkHttpClient();


    final private Logger log = Logger.getLogger(SeriousLogger.class);

    final File logfile;
    final File logfile2;
    final String url;
    private final String hostname;

    public SeriousLogger(String hostname, File logfile, File logfile2, String url) {
        this.logfile = logfile;
        this.hostname = hostname;
        this.logfile2 = logfile2;
        this.url = url;
        log.debug(logfile.getAbsolutePath());
        log.debug(logfile2.getAbsolutePath());
        log.debug("Log to " + url);
    }

    public void logMessageToDisk(String logMessage) {
        FileWriter logfw;

        post(logMessage);

        try {
            logfw = new FileWriter(logfile, true);
            IOUtils.write(logMessage + "\r\n", logfw);
            logfw.close();
        } catch (IOException e) {
            log.error("Could not write log: ", e);
        }

        try {
            logfw = new FileWriter(logfile2, true);
            IOUtils.write(logMessage + "\r\n", logfw);
            logfw.close();
        } catch (IOException e) {
            log.error("Could not write log: ", e);
        }
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    Callback status = new OkCallback();

    void post(String json)  {
        if (url == null) {
            return;
        }
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url+"?host="+ hostname)
                .post(body)
                .build();
        client.newCall(request).enqueue(status);
    }

    private class OkCallback implements Callback {

        @Override
        public void onFailure(Call call, IOException e) {
            log.debug("Log posting failed; " + call.toString(), e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            log.debug("Log posting ok; " + response.body());
        }
    }

}
