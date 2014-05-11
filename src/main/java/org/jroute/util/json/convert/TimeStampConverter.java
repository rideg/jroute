package org.jroute.util.json.convert;

import java.sql.Timestamp;

import org.joda.time.DateTime;

public class TimeStampConverter extends JsonConverter<Timestamp> {

    @Override
    protected Object convertIntance(final Timestamp time) {
        DateTime d = new DateTime(time.getTime());
        return d.toString("YYYY-MM-dd HH:mm:ss.SSS ZZ");
    }

}
