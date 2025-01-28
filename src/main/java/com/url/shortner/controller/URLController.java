package com.url.shortner.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.url.shortner.VO.URLEntityVO;
import com.url.shortner.model.UrlEntity;
import com.url.shortner.service.URLService;

@RestController
@RequestMapping("/api/v1")
public class URLController {
	
	@Autowired
	private URLService urlService;

	public static Logger logger = LogManager.getLogger(URLController.class);
	
	@PostMapping("/urls/shorten")
	public ResponseEntity<Object> createShortLink(@Valid @RequestBody URLEntityVO urlEntityVO, HttpServletRequest request) throws Exception {
		logger.info("Controller Class :: Request Payload: " + urlEntityVO.toString());

		String applicationURL = applicationUrl(request);
		
		logger.info("Controller Class :: calling createShortenLink service");
		UrlEntity entity = urlService.createShortenLink(urlEntityVO);
		entity.getUrlShortenedEntity().setShortenedURL(applicationURL + "/" + entity.getUrlShortenedEntity().getShortenedURL());
		
		return new ResponseEntity<>(entity.getUrlShortenedEntity(), HttpStatus.CREATED);
	}

	@PostMapping("/urls/quickshorturl")
	public ResponseEntity<Object> createQuickShortLink(@Valid @RequestBody URLEntityVO urlEntityVO, HttpServletRequest request) throws Exception {
		String applicationURL = applicationUrl(request);
		UrlEntity entity = urlService.createQuickShortLink(urlEntityVO);
		entity.getUrlShortenedEntity().setShortenedURL(applicationURL + "/" + entity.getUrlShortenedEntity().getShortenedURL());
		return new ResponseEntity<>(entity.getUrlShortenedEntity(), HttpStatus.CREATED);
	}

	private String applicationUrl(HttpServletRequest httpServletRequest) {
		String protocol = httpServletRequest.getProtocol().toLowerCase().startsWith("http") ? "http://" : "https://";
		return protocol + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort() + httpServletRequest.getContextPath();
	}

}
