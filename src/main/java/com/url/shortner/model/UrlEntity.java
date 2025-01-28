package com.url.shortner.model;


import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.url.shortner.utility.ApplicationConstants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "url")
@JsonInclude(value = Include.NON_DEFAULT)
public class UrlEntity extends Parent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "url_id")
	@JsonIgnore
	private Long id;
	
	@Column(name = "orginal_url", nullable = false)
	@JsonProperty(ApplicationConstants.PropertyNames.PROPERTY_URL)
	private String orginalUrl;
	
	@Column(name = "valid_seconds")
	@JsonProperty(ApplicationConstants.PropertyNames.PROPERTY_EXPIRY_SECONDS)
	private int shortURLExpiryseconds;
	
	@Column(name = "valid_date")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss a")
	@JsonProperty(ApplicationConstants.PropertyNames.PROPERTY_EXPIRY_DATE)
	private Date shortURLExpiryDate;
	
	@Column(name = "time_zone")
	@JsonInclude(value = Include.NON_NULL)
	@JsonProperty(ApplicationConstants.PropertyNames.PROPERTY_TIME_ZONE)
	private String timeZone = ApplicationConstants.DEFAULT_TIMEZONE_VALUE;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "urlEntity")
	@JsonIgnore
	private UrlShortenedEntity urlShortenedEntity;
}
