/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.anf.core.servlets;

import java.io.IOException;

import javax.jcr.Node;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anf.core.services.ContentService;

@Component(service = { Servlet.class })
@SlingServletPaths(
        value = "/bin/saveUserDetails"
)
public class UserServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;
    
    private static final Logger LOG = LoggerFactory.getLogger(UserServlet.class);

    @Reference
    private ContentService contentService;
	
	private static final String AGE_NODE = "/etc/age";

    @Override
    protected void doGet(final SlingHttpServletRequest request,
            final SlingHttpServletResponse response) throws ServletException, IOException {
       ResourceResolver resourceResolver = request.getResourceResolver();
		
		try {
			Resource resource = resourceResolver.getResource(AGE_NODE);
			
			Integer age = Integer.parseInt(request.getParameter("age"));
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String countryName = request.getParameter("countryName");
			
			Node ageNode = resource.adaptTo(Node.class);
			
			Integer minAge = Integer.parseInt(ageNode.getProperty("minAge").getValue().toString());
			Integer maxAge = Integer.parseInt(ageNode.getProperty("maxAge").getValue().toString());
			
			if(age >= minAge && age <= maxAge) {
				contentService.commitUserDetails(firstName, lastName, countryName, age, resourceResolver);
				response.setStatus(200);
			}else {
				response.setStatus(400);
			}			

		} catch (Exception e) {
			LOG.error("error while reading the pcc list {}", e);
		}

	}
}