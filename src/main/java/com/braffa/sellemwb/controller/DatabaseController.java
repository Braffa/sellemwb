package com.braffa.sellemwb.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.braffa.sellem.model.xml.ProductXml;
import com.braffa.sellem.model.xml.RegisteredUserXml;
import com.braffa.sellem.model.xml.UserToProductXml;
import com.braffa.sellemwb.form.DatabaseReportForm;
import com.braffa.sellemwb.xml.parser.ConvertStringAndXML;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

@Controller
@SessionAttributes("loggedin")
public class DatabaseController {

	private static final Logger logger = Logger.getLogger(DatabaseController.class);

	@RequestMapping("/adminmenu.html")
	public String adminmenu() {
		if (logger.isDebugEnabled()) {
			logger.debug("adminmenu");
		}
		return "databasemenu";
	}

	@RequestMapping("/recreateDatabase.html")
	public String recreateDatabase(Map<String, Object> model) {
		if (logger.isDebugEnabled()) {
			logger.debug("recreateDatabase");
		}
		DatabaseReportForm databaseReportForm = new com.braffa.sellemwb.form.DatabaseReportForm();
		try {
			// MySQLSetUp mySQLSetUp = new MySQLSetUp();
			// mySQLSetUp.setUpLogin();
			// mySQLSetUp.setUpRegisteredUser();
			// mySQLSetUp.setUpUserToProductTable();
			// mySQLSetUp.setUpProduct();

			databaseReportForm.setlOfRegisteredUser(getLOfRegisteredUser());
			databaseReportForm.setlOfProducts(getLOfProducts());
			databaseReportForm.setlOfUserToCatalog(getUserToProduct());

		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("databaseReportForm", databaseReportForm);
		return "databasereport";
	}

	private List<RegisteredUserXml> getLOfRegisteredUser() {
		if (logger.isDebugEnabled()) {
			logger.debug("getLOfRegisteredUser");
		}
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());

		String xml = service.path("rest").path("registeredusers").path("findall").accept(MediaType.TEXT_XML)
				.get(String.class);

		List<RegisteredUserXml> lOfRegisteredUser = ConvertStringAndXML.getLOfRegisteredUser(xml);

		return lOfRegisteredUser;
	}

	@RequestMapping("/viewregistereduser.html")
	public String viewRegisteredUser(Map<String, Object> model) {
		if (logger.isDebugEnabled()) {
			logger.debug("viewRegisteredUser");
		}
		try {
			DatabaseReportForm databaseReportForm = new DatabaseReportForm();
			databaseReportForm.setlOfRegisteredUser(getLOfRegisteredUser());
			model.put("databaseReportForm", databaseReportForm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "databasereport";
	}

	private List<ProductXml> getLOfProducts() {
		if (logger.isDebugEnabled()) {
			logger.debug("getLOfProducts");
		}
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		String xml = service.path("rest").path("product").path("findall").accept(MediaType.TEXT_XML).get(String.class);
		List<ProductXml> lOfProductXml = ConvertStringAndXML.getLOfProducts(xml);
		return lOfProductXml;
	}

	@RequestMapping("/viewcatalog.html")
	public String viewCatalog(Map<String, Object> model) {
		if (logger.isDebugEnabled()) {
			logger.debug("viewcatalog");
		}
		try {
			DatabaseReportForm databaseReportForm = new DatabaseReportForm();
			databaseReportForm.setlOfProducts(getLOfProducts());
			model.put("databaseReportForm", databaseReportForm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "databasereport";
	}

	private List<UserToProductXml> getUserToProduct() {
		if (logger.isDebugEnabled()) {
			logger.debug("getUserToProduct");
		}
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		String xmlStr = service.path("rest").path("usertoproduct").path("findall").accept(MediaType.TEXT_XML)
				.get(String.class);
		List<UserToProductXml> lOfUserToProducts = ConvertStringAndXML.getLOfUserToProducts(xmlStr);
		return lOfUserToProducts;
	}

	@RequestMapping("/viewusertocatalog.html")
	public String viewUserToCatalog(Map<String, Object> model) {
		if (logger.isDebugEnabled()) {
			logger.debug("viewUserToCatalog");
		}
		try {
			DatabaseReportForm databaseReportForm = new DatabaseReportForm();
			databaseReportForm.setlOfUserToCatalog(getUserToProduct());
			model.put("databaseReportForm", databaseReportForm);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "databasereport";
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/sellemws").build();
	}
}
