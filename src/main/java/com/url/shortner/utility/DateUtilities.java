package com.url.shortner.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.url.shortner.exception.URLValidationException;

@Component
public class DateUtilities {
	
	private static final Logger logger = LogManager.getLogger(DateUtilities.class);

	public Date toUTCDateFromDateInStringFormat(String inputDate, String timeZone) throws ParseException, URLValidationException {
		Date date = convertStringToDateObject(inputDate);

		if(timeZone.equalsIgnoreCase(ApplicationConstants.DEFAULT_TIMEZONE_VALUE)) {
			ZoneId fromTimeZoneId = ZoneId.of("UTC");
			ZonedDateTime fromZonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).withZoneSameLocal(fromTimeZoneId);
			return Date.from(fromZonedDateTime.toInstant());
		}

		Date UTCDate = convertDateFromCustomTimeZoneToUTCTimeZone(date, timeZone);
		return UTCDate;
	}

	public Date convertDateFromCustomTimeZoneToUTCTimeZone(Date date, String timeZone) throws URLValidationException {
		Date UTCDate = null;
		if(ZoneId.getAvailableZoneIds().contains(timeZone)) {
			ZoneId fromTimeZoneId = ZoneId.of(timeZone);
			ZoneId toTimeZoneId = ZoneId.of(ApplicationConstants.DEFAULT_TIMEZONE_VALUE);

			ZonedDateTime fromZonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).withZoneSameLocal(fromTimeZoneId);
			ZonedDateTime toZonedDateTime = fromZonedDateTime.withZoneSameInstant(toTimeZoneId);
			UTCDate = Date.from(toZonedDateTime.toInstant());
		} else {
			throw new URLValidationException("Invalid timezone specified: " + timeZone);
		}

		return UTCDate;
	}
	
//	public Date convertDateToUTCDate(Date date, String timeZone) throws URLValidationException {
//
//		if(timeZone.equalsIgnoreCase(ApplicationConstants.DEFAULT_TIMEZONE_VALUE)) {
//			ZoneId fromTimeZoneId = ZoneId.of("UTC");
//			ZonedDateTime fromZonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).withZoneSameLocal(fromTimeZoneId);
//			return Date.from(fromZonedDateTime.toInstant());
//		}
//
//		Date UTCDate = null;
//		if(ZoneId.getAvailableZoneIds().contains(timeZone)) {
//			ZoneId fromTimeZoneId = ZoneId.of(timeZone);
//	        ZoneId toTimeZoneId = ZoneId.of(ApplicationConstants.DEFAULT_TIMEZONE_VALUE);
//
//	        ZonedDateTime fromZonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).withZoneSameLocal(fromTimeZoneId);
//	        ZonedDateTime toZonedDateTime = fromZonedDateTime.withZoneSameInstant(toTimeZoneId);
//	        UTCDate = Date.from(toZonedDateTime.toInstant());
//		} else {
//			throw new URLValidationException("Invalid timezone specified: " + timeZone);
//		}
//
//        return UTCDate;
//	}
	
	public Date getUTCTimeNow() {
		Instant i = Instant.now();
		return Date.from(i);
	}

	public Date convertStringToDateObject(String date) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
		Date parsedDate = formatter.parse(date);
		return parsedDate;
	}

}
