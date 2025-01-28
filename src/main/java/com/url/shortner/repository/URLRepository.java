package com.url.shortner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.url.shortner.model.UrlEntity;

@Repository
public interface URLRepository extends JpaRepository<UrlEntity, Long> {

}
