package com.anf.core.listeners;

import java.util.Iterator;

import javax.jcr.Node;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anf.core.filters.ResolverUtil;
import com.day.cq.wcm.api.PageEvent;
import com.day.cq.wcm.api.PageModification;

@Component(service = EventHandler.class,
        immediate = true,
        property = {
                EventConstants.EVENT_TOPIC + "=" + PageEvent.EVENT_TOPIC
                //EventConstants.EVENT_FILTER + "=(paths=/content/anf-code-challenge/us/en/**)"
        })
public class ExcerciseFourEventHandler implements EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ExcerciseFourEventHandler.class);
    
    private static final String PARENT_PAGE= "/content/anf-code-challenge/us/en/jcr:content";
    
	@Reference
	private ResourceResolverFactory resourceResolverFactory;

    public void handleEvent(final Event event)  {
    
        try {
            Iterator<PageModification> pageInfo=PageEvent.fromEvent(event).getModifications();
            while (pageInfo.hasNext()){
                PageModification pageModification=pageInfo.next();
                LOG.info("\n Type :  {},  Page : {}",pageModification.getType(),pageModification.getPath());
                
                if(pageModification.getType().CREATED != null) {
                	
	                ResourceResolver resourceResolver= ResolverUtil.newResolver(resourceResolverFactory);
	                Resource resource=resourceResolver.getResource(PARENT_PAGE);
	                Node node=resource.adaptTo(Node.class);
	                node.setProperty("pageCreated", true);
	                resourceResolver.commit();  
                
              }

            }

        }catch (Exception e){
            LOG.info("\n Error while Activating/Deactivating - {} " , e.getMessage());
        }
    }
}