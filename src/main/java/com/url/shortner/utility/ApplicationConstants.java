package com.url.shortner.utility;

public class ApplicationConstants {

    public static final String DEFAULT_TIMEZONE_VALUE = "UTC";

    public static final String INTERNAL_ERROR_MESSAGE = "Something went wrong from our side. Please try after sometime";

    public class PropertyNames {
        public static final String PROPERTY_EXPIRY_SECONDS = "expiry_seconds";

        public static final String PROPERTY_EXPIRY_DATE = "expiry_date";

        public static final String PROPERTY_URL = "url";

        public static final String PROPERTY_TIME_ZONE = "time_zone";

        public static final String PROPERTY_SHORT_URL = "short_url";
    }

    public class URLConstants {

        public static final String INVALID_URL = "Invalid URL provided.";

        public static final String INVALID_EXPIRY_DATA = "Either Seconds should be greater than 0 or Expiry Date should be mentioned";

        public static final String INVALID_EXPIRY_DATE = "Expiry Date should be future date";

        public static final String INVALID_EXPIRY_DATE_FORMAT = "Invalid expiry date format. Please specify date in dd-MM-yyyy hh:mm:ss [AM/PM]";

        public static final int QUICK_SHORT_URL_VALIDITY_SECONDS = 600;
    }

    public class URLShortenConstants {
        public static final String INVALID_SHORT_URL = "Invalid short URL";

        public static final String SHORT_URL_NOT_FOUND = "Provided short url is not found";

        public static final String INVALID_REQUESTED_SHORT_URL = "Requested short url should be minimum 3 characters length with alphanumeric characters";

        public static final String SHORT_URL_EXPIRED = "Short URL Expired!";

        public static final int REQUESTED_SHORT_URL_MIN_LENGTH = 3;

        public static final String DUPLICATE_REQUESTED_SHORT_URL = "Requested short url is already taken. Please request another";
    }
}
