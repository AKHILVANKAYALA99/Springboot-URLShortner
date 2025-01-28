package com.url.shortner.service;

import java.util.Calendar;
import java.util.Date;

import com.url.shortner.utility.ApplicationConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.url.shortner.exception.URLRedirectionException;
import com.url.shortner.model.UrlShortenedEntity;
import com.url.shortner.repository.UrlShortenedRepository;
import com.url.shortner.utility.DateUtilities;

@Service
public class URLShortenServiceImpl implements URLShortenService {
	private static final Logger logger = LogManager.getLogger(URLShortenServiceImpl.class);
	
	@Autowired
	private UrlShortenedRepository urlShortenedRepository;
	
	@Autowired
	private DateUtilities dateUtilities;

	@Override
	public String getOrginalUrl(String shortID) throws URLRedirectionException {
		UrlShortenedEntity urlShorten = urlShortenedRepository.findByShortenedURL(shortID.trim());
		if(null == urlShorten) {
			logger.error("Redirection Exception: " + ApplicationConstants.URLShortenConstants.SHORT_URL_NOT_FOUND);
			throw new URLRedirectionException(ApplicationConstants.URLShortenConstants.SHORT_URL_NOT_FOUND);
		}
		
		if(null != urlShorten.getUrlEntity().getShortURLExpiryDate() && !(urlShorten.getUrlEntity().getShortURLExpiryDate().compareTo(dateUtilities.getUTCTimeNow()) >= 0)) {
			logger.error("Redirection Exception: " + ApplicationConstants.URLShortenConstants.SHORT_URL_EXPIRED);
			throw new URLRedirectionException(ApplicationConstants.URLShortenConstants.SHORT_URL_EXPIRED);
		}
		
		if(urlShorten.getUrlEntity().getShortURLExpiryseconds() > 0) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(urlShorten.getUrlEntity().getCreatedDate());
			cal.add(Calendar.SECOND, urlShorten.getUrlEntity().getShortURLExpiryseconds());
			Date newDate = cal.getTime();
			logger.info("Validity date for " + shortID + " is " + newDate.toString());
			
			if(!(newDate.compareTo(dateUtilities.getUTCTimeNow()) >= 0)) {
				logger.error("Redirection Exception: " + ApplicationConstants.URLShortenConstants.SHORT_URL_EXPIRED);
				throw new URLRedirectionException(ApplicationConstants.URLShortenConstants.SHORT_URL_EXPIRED);
			}
		}
		
		String orginalURL = urlShorten.getUrlEntity().getOrginalUrl();
		return orginalURL;
	}

}
