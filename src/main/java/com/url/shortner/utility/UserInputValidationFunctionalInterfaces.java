package com.url.shortner.utility;

import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class UserInputValidationFunctionalInterfaces {

    //Functional Interfaces for checking the validity of Orginal URL
    public static final Predicate<String> isOrginalURLProvided = (URL) -> !(null == URL || null == URL.trim() || URL.trim().length() == 0);
    public static final Predicate<String> isOrginalURLProtocolValid = (URL) -> (URL.startsWith("https://") || URL.startsWith("http://"));
    public static final Function<String, Boolean> isOrginalURLValid = (url) -> {
        try {
            new URL(url).toURI();
            return true;
        } catch(Exception e) {
            return false;
        }
    };

    //Functional Interfaces for checking the validity of Date/Seconds
    public static final IntPredicate isUrlExpirySecondsValid = (seconds) -> (seconds > 0);
    public static final Predicate<Date> isUrlExpiryDateValid = (date) -> (null != date && !(Date.from(Instant.now()).compareTo(date) >= 0));
    public static final Predicate<String> isUrlExpiryDateProvided = (date) -> null != date;

    //Functional Interfaces for checking whether requested short url is valid
    public static final Predicate<String> isRequestedShortURLNull = (short_url) -> null == short_url;
    public static final Predicate<String> isRequestedURLHasMinLen = (short_url) -> short_url.length() >= ApplicationConstants.URLShortenConstants.REQUESTED_SHORT_URL_MIN_LENGTH;
    public static final Predicate<String> isRequestedURLPassedRegExp = (short_url) -> Pattern.matches("^[a-zA-Z0-9]*$", short_url);
    public final static Predicate<String> isRequestedShortUrlValid = isRequestedURLHasMinLen.and(isRequestedURLPassedRegExp);

}
