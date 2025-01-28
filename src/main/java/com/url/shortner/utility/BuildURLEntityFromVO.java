package com.url.shortner.utility;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.url.shortner.VO.URLEntityVO;
import com.url.shortner.exception.URLValidationException;
import com.url.shortner.model.UrlEntity;
import com.url.shortner.model.UrlShortenedEntity;

@Component
public class BuildURLEntityFromVO {
	
	@Autowired
	private DateUtilities dateConversionUtilities;

	private Logger logger = LogManager.getLogger(BuildURLEntityFromVO.class);

	public UrlEntity convert(URLEntityVO urlEntityVO, List<String> errors) {
		//Validating User Input
		boolean isValidUserInput = isValidUserInput(urlEntityVO, errors);
		if(!isValidUserInput) {
			return null;
		}

		UrlEntity urlEntity = new UrlEntity();
		urlEntity.setUrlShortenedEntity(new UrlShortenedEntity());
		urlEntity.getUrlShortenedEntity().setUrlEntity(urlEntity);

		urlEntity.setOrginalUrl(urlEntityVO.getOrginalUrl().trim());
		urlEntity.setShortURLExpiryseconds(urlEntityVO.getShortURLExpirySeconds());
		
		if(isValidUserInput && urlEntityVO.getShortURLExpiryDate() != null) {
			try {
				urlEntity.setShortURLExpiryDate(dateConversionUtilities.toUTCDateFromDateInStringFormat(urlEntityVO.getShortURLExpiryDate(), urlEntityVO.getTimeZone()));
			} catch (URLValidationException e) {
				errors.add(e.getMessage());
			} catch (ParseException e) {
				errors.add(ApplicationConstants.URLConstants.INVALID_EXPIRY_DATE_FORMAT);
			}
		}

		urlEntity.setTimeZone(urlEntityVO.getTimeZone());
		urlEntity.getUrlShortenedEntity().setShortenedURL(urlEntityVO.getRequestedShortURL());

		return urlEntity;
	}

	public boolean isValidUserInput(URLEntityVO urlEntityVO, List<String> errors) {
		boolean isValidOrginalURL = isValidOrginalURL(urlEntityVO.getOrginalUrl().trim(), errors);
		boolean isValidRequestedShortURL = isValidShortURL(urlEntityVO.getRequestedShortURL(), errors);
		boolean isValidExpiryData = isValidExpiryDataProvided(urlEntityVO.getShortURLExpirySeconds(), urlEntityVO.getShortURLExpiryDate(), errors);

		if(!isValidOrginalURL || !isValidRequestedShortURL || !isValidExpiryData) {
			return false;
		}

		return true;
	}

	public boolean isValidOrginalURL(String URL, List<String> errors) {
		boolean isOrginalURLProvided = UserInputValidationFunctionalInterfaces.isOrginalURLProvided.test(URL);
		boolean isOrginalURLProtocolValid = UserInputValidationFunctionalInterfaces.isOrginalURLProtocolValid.test(URL);
		boolean isOrginalURLValid = UserInputValidationFunctionalInterfaces.isOrginalURLValid.apply(URL);

		if(!isOrginalURLProvided || !isOrginalURLProtocolValid || !isOrginalURLValid) {
			errors.add(ApplicationConstants.URLConstants.INVALID_URL);
			return false;
		}

		return true;
	}

	public boolean isValidOrginalURL(String URL) {
		boolean isOrginalURLProvided = UserInputValidationFunctionalInterfaces.isOrginalURLProvided.test(URL);
		boolean isOrginalURLProtocolValid = UserInputValidationFunctionalInterfaces.isOrginalURLProtocolValid.test(URL);
		boolean isOrginalURLValid = UserInputValidationFunctionalInterfaces.isOrginalURLValid.apply(URL);

		if(!isOrginalURLProvided || !isOrginalURLProtocolValid || !isOrginalURLValid) {
			return false;
		}

		return true;
	}

	public boolean isValidShortURL(String URL, List<String> errors) {
		boolean isRequestedShortURLNull = UserInputValidationFunctionalInterfaces.isRequestedShortURLNull.test(URL);
		if(!isRequestedShortURLNull) {
			boolean isRequestedShortURLValid = UserInputValidationFunctionalInterfaces.isRequestedShortUrlValid.test(URL.trim());
			if(!isRequestedShortURLValid) {
				errors.add(ApplicationConstants.URLShortenConstants.INVALID_REQUESTED_SHORT_URL);
				return false;
			}
		}
		return true;
	}

	public boolean isValidExpiryDataProvided(int seconds, String date, List<String> errors) {
		Date expiryDate = null;
		boolean isValidSecondsProvided = UserInputValidationFunctionalInterfaces.isUrlExpirySecondsValid.test(seconds); //true, when seconds is <= 0
		boolean isExpiryDateProvided = UserInputValidationFunctionalInterfaces.isUrlExpiryDateProvided.test(date);

		try {
			if(isExpiryDateProvided)
				expiryDate = dateConversionUtilities.convertStringToDateObject(date);
		} catch (ParseException ex) {
			errors.add(ApplicationConstants.URLConstants.INVALID_EXPIRY_DATE_FORMAT);
			return false;
		}

		boolean isExpiryDateValid = UserInputValidationFunctionalInterfaces.isUrlExpiryDateValid.test(expiryDate); //will get true, if we give past date
		if(!isValidSecondsProvided && !isExpiryDateProvided) {
			errors.add(ApplicationConstants.URLConstants.INVALID_EXPIRY_DATA);
			return false;
		} else if(isExpiryDateProvided && !isExpiryDateValid) {
			errors.add(ApplicationConstants.URLConstants.INVALID_EXPIRY_DATE);
			return false;
		}

		return true;
	}
}


