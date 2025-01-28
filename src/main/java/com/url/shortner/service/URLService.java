package com.url.shortner.service;

import com.url.shortner.VO.URLEntityVO;
import com.url.shortner.exception.URLValidationException;
import com.url.shortner.model.UrlEntity;
import com.url.shortner.model.UrlShortenedEntity;

public interface URLService {
	
	UrlEntity createShortenLink(URLEntityVO urlEntityVO) throws Exception;

    UrlEntity createQuickShortLink(URLEntityVO urlEntityVO) throws URLValidationException;
}
