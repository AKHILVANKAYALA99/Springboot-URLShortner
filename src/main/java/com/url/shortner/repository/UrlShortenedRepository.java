package com.url.shortner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.url.shortner.model.UrlShortenedEntity;

@Repository
public interface UrlShortenedRepository extends JpaRepository<UrlShortenedEntity, Long> {
	
	UrlShortenedEntity findByShortenedURL(String shortID);

}
