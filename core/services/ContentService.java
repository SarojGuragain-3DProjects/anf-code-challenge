package com.anf.core.services;

import org.apache.sling.api.resource.ResourceResolver;

public interface ContentService {
	void commitUserDetails(String firstName, String lastName, String countryName, Integer age, ResourceResolver resourceResolver);
}
