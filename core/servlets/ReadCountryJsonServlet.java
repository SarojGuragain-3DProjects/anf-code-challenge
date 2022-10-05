package com.anf.core.servlets;

import static com.anf.core.servlets.ReadCountryJsonServlet.RESOURCE_TYPE;
import static com.anf.core.servlets.ReadCountryJsonServlet.SERVICE_NAME;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.servlet.Servlet;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.iterators.TransformIterator;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.json.JSONArray;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.dam.commons.util.DamUtil;


@Component(
        service = Servlet.class,
        property = {
                Constants.SERVICE_ID + "=" + SERVICE_NAME,
                SLING_SERVLET_RESOURCE_TYPES + "=" + RESOURCE_TYPE
        }
)
public class ReadCountryJsonServlet extends SlingSafeMethodsServlet {
	
	private static final long serialVersionUID = 1L;
	protected static final String SERVICE_NAME = "Read Json Servlet";
    protected static final String RESOURCE_TYPE = "/apps/anf-code-challenge/components/form/country/dropdown";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReadCountryJsonServlet.class);
	
	 @Override
	    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {

	        try {

	            ResourceResolver resourceResolver = request.getResourceResolver();
	            
	            Resource currentResource = request.getResource();
	            
	            String dropdownSelector = Objects.requireNonNull(currentResource.getChild("datasource"))
	                    .getValueMap()
	                    .get("dropdownSelector", String.class);
	            
	            Resource jsonResource = getJsonResource(resourceResolver, Objects.requireNonNull(dropdownSelector));

	            Asset asset = DamUtil.resolveToAsset(jsonResource);

	            Rendition originalAsset = Objects.requireNonNull(asset).getOriginal();

	            InputStream content = Objects.requireNonNull(originalAsset).adaptTo(InputStream.class);

	            StringBuilder jsonContent = new StringBuilder();
	            BufferedReader jsonReader = new BufferedReader(
	                    new InputStreamReader(Objects.requireNonNull(content), StandardCharsets.UTF_8));

	            String line;
	            while ((line = jsonReader.readLine()) != null) {
	                jsonContent.append(line);
	            }
	            JSONArray jsonArray = new JSONArray(jsonContent.toString());
	            Map<String, String> data = new TreeMap<>();
	            for (int i = 0; i < jsonArray.length(); i++) {
	                data.put(jsonArray.getJSONObject(i).getString("text"),
	                        jsonArray.getJSONObject(i).getString("value"));
	            }
	            @SuppressWarnings({"unchecked", "rawtypes"})
	            DataSource ds = new SimpleDataSource(new TransformIterator<>(data.keySet().iterator(), (Transformer) o -> {
	                String dropValue = (String) o;
	                ValueMap vm = new ValueMapDecorator(new HashMap<>());
	                vm.put("text", dropValue);
	                vm.put("value", data.get(dropValue));
	                return new ValueMapResource(resourceResolver, new ResourceMetadata(), JcrConstants.NT_UNSTRUCTURED, vm);
	            }));
	            request.setAttribute(DataSource.class.getName(), ds);
	            
	        }catch(Exception e) {
	        	LOGGER.error("Eception", e);
	        	}
	        }
	 
	 
	    private Resource getJsonResource(ResourceResolver resourceResolver, String dropdownSelector) {
	        Resource jsonResource;

	                jsonResource = resourceResolver.getResource("/content/dam/anf-code-challenge/exercise-1/countries.json");

	        return jsonResource;
	    }

}
