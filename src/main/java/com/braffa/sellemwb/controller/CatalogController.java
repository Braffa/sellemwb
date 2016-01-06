package com.braffa.sellemwb.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.braffa.sellem.model.xml.ProductXml;
import com.braffa.sellem.model.xml.RegisteredUserXml;
import com.braffa.sellem.model.xml.UserToProductXml;
import com.braffa.sellemwb.form.CatalogForm;
import com.braffa.sellemwb.xml.parser.ConvertStringAndXML;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

@Controller
@SessionAttributes({ "userObject", "newProduct" })

public class CatalogController {

	private static final Logger logger = Logger.getLogger(CatalogController.class);

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/sellemws").build();
	}

	private ProductXml getProduct(String productId) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		String xml = service.path("rest").path("product").path("findbyproductid").path(productId)
				.accept(MediaType.TEXT_XML).get(String.class);
		List<ProductXml> lOfProductXml = ConvertStringAndXML.getLOfProducts(xml);
		if (lOfProductXml.size() > 0) {
			return lOfProductXml.get(0);
		}
		return null;
	}

	@RequestMapping("/getCatalog.html")
	public String getCatalog(@ModelAttribute("userObject") RegisteredUserXml registeredUserXml, Model model) {
		if (logger.isDebugEnabled()) {
			logger.debug("");
		}
		System.out.println(registeredUserXml.toString());

		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		String xml = service.path("rest").path("product").path("findall").accept(MediaType.TEXT_XML).get(String.class);
		List<ProductXml> lOfProductXml = ConvertStringAndXML.getLOfProducts(xml);
		CatalogForm catalogForm = new CatalogForm();
		catalogForm.setmOfProducts(lOfProductXml);
		catalogForm.setShowLinks(false);
		catalogForm.setHeader("Full Catalogue");
		catalogForm.setCurrentPage("fullCatalogue");
		model.addAttribute("catalogForm", catalogForm);
		model.addAttribute("newProduct", new ProductXml());
		return "catalog";
	}

	@RequestMapping("/myCatalog.html")
	public Object myCatalog(@ModelAttribute("userObject") RegisteredUserXml registeredUserXml, Model model) {
		if (logger.isDebugEnabled()) {
			logger.debug("");
		}
		if (registeredUserXml == null || registeredUserXml.getUserId() == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("myCatalog - no registeredUserXml in session");
			}
			return new ModelAndView("redirect:homepage.html");
		}

		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		WebResource service = client.resource(getBaseURI());

		String xmlStr = service.path("rest").path("usertoproduct").path("find").path(registeredUserXml.getUserId())
				.path(" ").path(" ").accept(MediaType.TEXT_XML).get(String.class);

		List<UserToProductXml> lOfUserToProducts = ConvertStringAndXML.getLOfUserToProducts(xmlStr);
		ArrayList<ProductXml> lOfMyProducts = new ArrayList<ProductXml>();

		for (UserToProductXml userToProductXml : lOfUserToProducts) {
			ProductXml myProduct = getProduct(userToProductXml.getProductId());
			if (myProduct != null) {
				lOfMyProducts.add(myProduct);
			}
		}

		CatalogForm catalogForm = new CatalogForm();
		catalogForm.setmOfProducts(lOfMyProducts);
		catalogForm.setShowLinks(true);
		catalogForm.setHeader("My Catalogue");
		catalogForm.setOrigin("");
		catalogForm.setCurrentPage("myCatalogue");
		model.addAttribute("catalogForm", catalogForm);
		model.addAttribute("newProduct", new ProductXml());
		return "catalog";

	}

	@RequestMapping(value = "/catalog", method = RequestMethod.POST)
	public String catalog() {
		if (logger.isDebugEnabled()) {
			logger.debug("");
		}

		return "redirect:login.html";

	}

}
