package com.url.shortner.service;

import com.url.shortner.exception.URLRedirectionException;

public interface URLShortenService {
	String getOrginalUrl(String shortID) throws URLRedirectionException;

}
