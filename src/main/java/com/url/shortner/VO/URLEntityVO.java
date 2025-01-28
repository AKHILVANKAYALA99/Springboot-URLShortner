package com.url.shortner.VO;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.url.shortner.utility.ApplicationConstants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class URLEntityVO {
	
	@NotBlank(message = "URL should not be empty")
	@JsonProperty(ApplicationConstants.PropertyNames.PROPERTY_URL)
	private String orginalUrl;

	@JsonProperty(ApplicationConstants.PropertyNames.PROPERTY_SHORT_URL)
	private String requestedShortURL;

	@JsonProperty(ApplicationConstants.PropertyNames.PROPERTY_EXPIRY_SECONDS)
	private int shortURLExpirySeconds;

	@JsonProperty(ApplicationConstants.PropertyNames.PROPERTY_EXPIRY_DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss [AM/PM]")
	private String shortURLExpiryDate;
	
	@JsonProperty(ApplicationConstants.PropertyNames.PROPERTY_TIME_ZONE)
	private String timeZone = ApplicationConstants.DEFAULT_TIMEZONE_VALUE;
}
