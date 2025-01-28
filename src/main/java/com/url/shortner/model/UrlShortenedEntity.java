package com.url.shortner.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.url.shortner.utility.ApplicationConstants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "url_short")
@JsonInclude(value = Include.NON_NULL)
public class UrlShortenedEntity extends Parent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ush_id")
	@JsonIgnore
	private Long id;
	
	@Column(name = "shortened_url", nullable = false, updatable = false, unique = true)
	@JsonProperty(ApplicationConstants.PropertyNames.PROPERTY_SHORT_URL)
	private String shortenedURL;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "url_id", referencedColumnName = "url_id")
	private UrlEntity urlEntity;
}
