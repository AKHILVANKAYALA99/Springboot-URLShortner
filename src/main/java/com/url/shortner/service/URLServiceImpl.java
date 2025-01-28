package com.url.shortner.service;

import java.util.*;
import java.util.stream.Collectors;

import com.url.shortner.utility.ApplicationConstants;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.url.shortner.VO.URLEntityVO;
import com.url.shortner.exception.URLValidationException;
import com.url.shortner.model.UrlEntity;
import com.url.shortner.model.UrlShortenedEntity;
import com.url.shortner.repository.URLRepository;
import com.url.shortner.repository.UrlShortenedRepository;
import com.url.shortner.utility.BuildURLEntityFromVO;

@Service
@Transactional(readOnly = true)
public class URLServiceImpl implements URLService {

	@Autowired
	private UrlShortenedRepository urlShortenedRepository;
	
	@Autowired
	private URLRepository urlRepository;
	
	@Autowired
	private BuildURLEntityFromVO buildURLEntityFromVO;

	@Value("${app.properties.shorturl.length}")
	public int shortUrlLength;

	private static final Logger logger = LogManager.getLogger(URLServiceImpl.class);

	@Override
	@Transactional
	public UrlEntity createShortenLink(URLEntityVO urlEntityVO) throws Exception {
		List<String> errors = new ArrayList<String>();
		
		//Convert VO object to Entity object
		UrlEntity urlEntity = buildURLEntityFromVO.convert(urlEntityVO, errors); 
		if(errors.size() != 0) {
			logger.error("Throwing Validation Error");
			throw new URLValidationException("Validation Error", errors);
		}
		errors.clear();
		
		Boolean isProceedToSave = isProceedToSave(urlEntity, errors);
		if(isProceedToSave) {
			try {
				urlEntity = urlRepository.saveAndFlush(urlEntity);
				urlEntity.setTimeZone(ApplicationConstants.DEFAULT_TIMEZONE_VALUE);
			} catch(Exception e) {
				logger.error("UrlEntity saving error ::", e);
				throw new Exception(ApplicationConstants.INTERNAL_ERROR_MESSAGE);
			}
		} else {
			throw new URLValidationException("Validation Error", errors);
		}
		
		return urlEntity;
	}

	@Override
	@Transactional
	public UrlEntity createQuickShortLink(URLEntityVO urlEntityVO) throws URLValidationException {
		UrlEntity urlEntity = new UrlEntity();
		urlEntity.setUrlShortenedEntity(new UrlShortenedEntity());
		urlEntity.getUrlShortenedEntity().setUrlEntity(urlEntity);

		if(buildURLEntityFromVO.isValidOrginalURL(urlEntityVO.getOrginalUrl())) {
			urlEntity.setOrginalUrl(urlEntityVO.getOrginalUrl());
			urlEntity.setShortURLExpiryseconds(ApplicationConstants.URLConstants.QUICK_SHORT_URL_VALIDITY_SECONDS);
			urlEntity.getUrlShortenedEntity().setShortenedURL(getRandomCode());
			urlEntity = urlRepository.saveAndFlush(urlEntity);
		} else {
			throw new URLValidationException(ApplicationConstants.URLConstants.INVALID_URL);
		}
		return urlEntity;
	}

	private Boolean isProceedToSave(UrlEntity entity, List<String> errors) {
		if(null != entity.getUrlShortenedEntity().getShortenedURL()) {
			Boolean isUnique = isUniqueCode(entity.getUrlShortenedEntity().getShortenedURL());
			if(!isUnique) {
				errors.add(ApplicationConstants.URLShortenConstants.DUPLICATE_REQUESTED_SHORT_URL);
				return false;
			}
			return true;
		}
		String randomString = getRandomCode();
		while(true) {
			if(isUniqueCode(randomString)) {
				break;
			} else
				randomString = getRandomCode();
		}
		entity.getUrlShortenedEntity().setShortenedURL(randomString);
		return true;
	}
	
	private Boolean isUniqueCode(String code) {
		UrlShortenedEntity temp = urlShortenedRepository.findByShortenedURL(code);
		if(null != temp) {
			return false;
		}
		return true;
	}

	private String getRandomCode() {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
				.limit(shortUrlLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
		return randomizeString(generatedString);
	}

	private String randomizeString(String string) {
		List<String> letters = Arrays.asList(string.split(""));
		Collections.shuffle(letters);
		Collections.shuffle(letters);
		return letters.stream().collect(Collectors.joining());
	}

}