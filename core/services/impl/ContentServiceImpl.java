package com.anf.core.services.impl;


import com.anf.core.services.ContentService;
import com.anf.core.servlets.UserServlet;

import javax.jcr.Node;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, service = ContentService.class)
public class ContentServiceImpl implements ContentService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ContentServiceImpl.class);
	
	private static final String PARENT_NODE_TO_CREATE_USER_NODE = "/var/anf-code-challenge";

    @Override
    public void commitUserDetails(String firstName, String lastName, String countryName, Integer age, ResourceResolver resourceResolver) {
    	
    	try {
    		Resource resource = resourceResolver.getResource(PARENT_NODE_TO_CREATE_USER_NODE);
    		
    		Node nodeData = resource.adaptTo(Node.class);    		
    		Node userNode = nodeData.addNode(firstName + "_" + lastName,"nt:unstructured");
    		
    		userNode.setProperty("firstName", firstName);
    		userNode.setProperty("lastName", lastName);
    		userNode.setProperty("countrytName", countryName);
    		userNode.setProperty("age", age);
    		
    		resourceResolver.commit();
    		
    	}catch(Exception e) {
    		LOG.error("Exception occurred...", e);
    	}
    	
    }
}
