package org.mentalizr.backendTest.commons;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * TODO Move to commons/utils together with m7r-persistence-mongo/MongoDates
 */
public class Dates {

    public static boolean isNotOlderThanOneMinute(String iso) {
        int minutes = 1;
        ZonedDateTime reference = ZonedDateTime.parse(iso);
        ZonedDateTime now = ZonedDateTime.now();
        Duration duration = Duration.between(reference, now);
        return duration.toMinutes() < minutes;
    }

}
