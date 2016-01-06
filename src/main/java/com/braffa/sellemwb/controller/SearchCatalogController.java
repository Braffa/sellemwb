package com.braffa.sellemwb.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.braffa.sellem.model.xml.ProductXml;
import com.braffa.sellemwb.form.CatalogForm;
import com.braffa.sellemwb.form.SearchCatalogForm;
import com.braffa.sellemwb.xml.parser.ConvertStringAndXML;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

@Controller
@SessionAttributes
public class SearchCatalogController {

	private static final Logger logger = Logger
			.getLogger(SearchCatalogController.class);
	
	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/sellemws").build();
	}

	@RequestMapping("/searchCatalogStart")
	public String searchCatalogStart(Map<String, SearchCatalogForm> model) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchCatalog");
		}
		SearchCatalogForm searchCatalogForm = new SearchCatalogForm();
		searchCatalogForm.setCurrentPage("searchCatalogue");
		model.put("searchCatalogForm", searchCatalogForm);
		return "searchCatalogue";
	}

	@RequestMapping(value = "/searchCatalog", method = RequestMethod.POST)
	public String search(@Valid SearchCatalogForm searchForm,
			BindingResult result, Map<String, Object> model) {
		if (logger.isDebugEnabled()) {
			logger.debug("");
		}
		if (result.hasErrors()) {
			return "searchCatalogue";
		}
		if (searchForm.getAuthor().length() == 0
				&& searchForm.getManufacturer().length() == 0
				&& searchForm.getProductid().length() == 0
				&& searchForm.getTitle().length() == 0) {
			result.addError(new FieldError("SearchCatalogForm", "author",
					"At least one field must have a value"));
			return "searchCatalogue";
		}
		String searchField = "";
		String searchValue = "";
		
		if (searchForm.getAuthor().length() > 0) {
			searchField = "author";
			searchValue = searchForm.getAuthor();
		}
		if (searchForm.getManufacturer().length() > 0) {
			searchField = "manufacturer";
			searchValue = searchForm.getManufacturer();
		}
		if (searchForm.getProductid().length() > 0) {
			searchField = "productid";
			searchValue = searchForm.getProductid();
		}
		if (searchForm.getTitle().length() > 0) {
			searchField = "title";
			searchValue = searchForm.getTitle();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("searchField " + searchField + " searchValue " + searchValue);
		}
		
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		
		String xml = service.path("rest").path("product").path("searchProduct").path(searchField).path(searchValue)
				.accept(MediaType.TEXT_XML).get(String.class);

		List<ProductXml> lOfProductXml = ConvertStringAndXML.getLOfProducts(xml);
		
		CatalogForm catalogForm = new CatalogForm();
		catalogForm.setmOfProducts(lOfProductXml);
		catalogForm.setShowLinks(false);
		catalogForm.setHeader("Search Results");
		catalogForm.setCurrentPage("searchResults");
		model.put("catalogForm", catalogForm);
		return "catalog";
	}

}
