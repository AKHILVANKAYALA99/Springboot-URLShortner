package com.url.shortner.controller;

import java.net.URI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.url.shortner.exception.URLRedirectionException;
import com.url.shortner.service.URLShortenService;


@Controller
public class PublicController {
	
	@Autowired
	private URLShortenService urlShortenService;

	private static final Logger logger = LogManager.getLogger(PublicController.class);

	@GetMapping(value = "/{shortID}")
	 public ResponseEntity<Void> redirect(@PathVariable("shortID") String shortID) throws URLRedirectionException {
		logger.info("Controller :: Request Payload: " + shortID);
		String orginalURL = urlShortenService.getOrginalUrl(shortID);
		logger.info("Redirecting to:" + orginalURL);
		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(orginalURL)).build();
	 }
}
