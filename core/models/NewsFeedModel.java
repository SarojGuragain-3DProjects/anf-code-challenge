package com.anf.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anf.core.beans.NewsFeedBeans;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewsFeedModel {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NewsFeedModel.class);
	
    @OSGiService
    private SlingSettingsService settings;
    
    @SlingObject
    private Resource currentResource;
    
    @SlingObject
    private ResourceResolver resourceResolver;
    
    @SlingObject
    private SlingHttpServletRequest request;
    
    private static final String NEWS_FEED_PARENT_NODE = "/var/commerce/products/anf-code-challenge/newsData";
	
	private List<NewsFeedBeans> newsFeedBean = new ArrayList<>();
	
    @PostConstruct
    protected void init() {
    	try {
    		
	    	Resource resource = resourceResolver.getResource(NEWS_FEED_PARENT_NODE);		
			Node nodeData = resource.adaptTo(Node.class); 
			
			NodeIterator nodeItr = nodeData.getNodes();
		    
	        while(nodeItr.hasNext()){
	        	Node childNode = nodeItr.nextNode();
	        	newsFeedBean.add(newsFeedBeans(childNode));
	        }
	        
    	}catch(Exception e) {
    		LOGGER.error("Exception occurred...", e);
    	}
    }      
    
	public List<NewsFeedBeans> getNewsFeedBean() {
		return newsFeedBean;
	}
	
	private NewsFeedBeans newsFeedBeans(Node childNode) {
		final NewsFeedBeans newsFeedbeans = new NewsFeedBeans();
		
		String author = null;
		String content = null;
		String description = null;
		String title = null;
		String url = null;
		String urlImage = null;
		try {
			author = childNode.getProperty("author").getValue().toString();
			content = childNode.getProperty("content").getValue().toString();
			description = childNode.getProperty("description").getValue().toString();
			title = childNode.getProperty("title").getValue().toString();
			url = childNode.getProperty("url").getValue().toString();
			urlImage = childNode.getProperty("urlImage").getValue().toString();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		
		newsFeedbeans.setAuthor(author);
		newsFeedbeans.setContent(content);
		newsFeedbeans.setDescription(description);
		newsFeedbeans.setTitle(title);
		newsFeedbeans.setUrl(url);
		newsFeedbeans.setUrlImage(urlImage);
		
		return newsFeedbeans;
	}
  
}
