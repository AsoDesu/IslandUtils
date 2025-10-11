package net.asodev.islandutils.util;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {

    public static final Pattern TIME_REGEX = Pattern.compile("(?:\\d+d\\s*|\\d+h\\s*|\\d+m\\s*)+");
    private static final Pattern timeRegex = Pattern.compile("(\\d*)([dhms])");

    public static long getTimeSeconds(String string) {
        long timeSeconds = -1;
        Matcher matcher = timeRegex.matcher(string);

        while (matcher.find()) {
            if(timeSeconds == -1) timeSeconds = 0;
            MatchResult result = matcher.toMatchResult();
            int value;

            try { value = Integer.parseInt(result.group(1)); }
            catch (Exception e) { continue; }

            String time = result.group(2);
            switch (time) {
                case "d" -> timeSeconds += value * 86400L;
                case "h" -> timeSeconds += value * 3600L;
                case "m" -> timeSeconds += value * 60L;
                case "s" -> timeSeconds += value;
            }
        }

        return timeSeconds;
    }
}
