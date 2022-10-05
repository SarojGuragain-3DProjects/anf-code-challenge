package com.anf.core.servlets;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

@Component(service = Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION + "=Servlet to execute Query and get results",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/execute/query" })
public class ExcerciseThreeServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 4438376868274173005L;

	private static final Logger log = LoggerFactory.getLogger(ExcerciseThreeServlet.class);
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {

		ResourceResolver resourceResolver = request.getResourceResolver();
		try {

			log.info("Servlet called");
			SearchResult result = null;
			Map<String, String> map = new HashMap<>();
			Session session = resourceResolver.adaptTo(Session.class);
			QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
			
			map.put("path", "/content/anf-code-challenge/us/en");
			map.put("type", "cq:Page");
			map.put("1_property","anfCodeChallenge");
			map.put("1_property.value", "true");
			
			map.put("p.limit", "10");
			
			Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
			result = query.getResult();
			
			response.getWriter().println("Resulted list of 10 Page::::::::::::: \n" );
			
			for (Hit hit : result.getHits()) {
				String path = hit.getPath();				
				response.getWriter().println(path);
			}			

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
