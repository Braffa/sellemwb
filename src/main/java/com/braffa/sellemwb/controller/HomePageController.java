package com.braffa.sellemwb.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.braffa.sellem.model.sql.RegisteredUser;
import com.braffa.sellem.model.xml.RegisteredUserXml;
import com.braffa.sellemwb.form.LoginForm;
import com.braffa.sellemwb.xml.parser.ConvertStringAndXML;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

@Controller
@SessionAttributes("userObject")
public class HomePageController {

	private static final Logger logger = Logger.getLogger(HomePageController.class);
	
	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/sellemws").build();
	}
	
	@RequestMapping("/homepage.html")
	public String showHomePage(Model model) {
		if (logger.isDebugEnabled()) {
			logger.debug("showHomePage");
		}
		LoginForm loginForm = new LoginForm();
		loginForm.setCurrentPage("home");
		model.addAttribute("loginForm", loginForm);
		RegisteredUserXml registeredUserXml = new RegisteredUserXml();
		model.addAttribute("userObject", registeredUserXml);
		return "home";
	}

	@RequestMapping("/gotoPage.html")
	public ModelAndView gotoPage(@RequestParam String gotoPage, Model model) {
		if (logger.isDebugEnabled()) {
			logger.debug(gotoPage);
		}
		if (!model.containsAttribute("userObject")) {
			RegisteredUserXml registeredUserXml = new RegisteredUserXml();
			model.addAttribute("userObject", registeredUserXml);
		}
		if (gotoPage.equals("home")) {
			return new ModelAndView("redirect:homepage.html");
		}
		if (gotoPage.equals("catalogue")) {
			return new ModelAndView("redirect:getCatalog.html");
		}
		if (gotoPage.equals("searchCatalogue")) {
			return new ModelAndView("redirect:searchCatalogStart.html");
		}
		if (gotoPage.equals("addProduct")) {
			return new ModelAndView("redirect:addNewProduct.html");
		}
		if (gotoPage.equals("myCatalog")) {
			return new ModelAndView("redirect:myCatalog.html");
		}
		if (gotoPage.equals("login")) {
			return new ModelAndView("redirect:homepage.html");
		}
		if (gotoPage.equals("register")) {
			return new ModelAndView("redirect:registerStart.html");
		}
		if (gotoPage.equals("showRegisteredUsers")) {
			return new ModelAndView("redirect:showRegisteredUsers.html");
		}
		if (gotoPage.equals("showAdminMenu")) {
			return new ModelAndView("redirect:adminmenu.html");
		}
		if (gotoPage.equals("signOut")) {
			RegisteredUser RegisteredUser = new RegisteredUser();
			model.addAttribute("loggedin", RegisteredUser);
			return new ModelAndView("redirect:homepage.html");
		}
		if (gotoPage.equals("productToUsers")) {
			return new ModelAndView("redirect:getProductUserDetails.html");
		}
		return null;
	}
	
	
	@RequestMapping(value = "/attemptlogin.html", method = RequestMethod.POST)
	public Object processForm(@Valid LoginForm loginForm, BindingResult result, Model model) {
		if (logger.isDebugEnabled()) {
			logger.debug("processForm");
		}
		if (result.hasErrors()) {
			return "home";
		}

		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		
		String xml = service.path("rest").path("registeredusers").path("findbyuserid").path(loginForm.getUserName())
				.accept(MediaType.TEXT_XML).get(String.class);
		
		List<RegisteredUserXml> lOfRegisteredUser = ConvertStringAndXML.getLOfRegisteredUser(xml);
		RegisteredUserXml registeredUserXml = lOfRegisteredUser.get(0);
		ModelAndView mv = new ModelAndView("redirect:homepage.html");

		if (registeredUserXml.getUserId().equals("9999")) {
			result.addError(new FieldError("loginForm", "userName",
					"Invalid userName" + " ( " + loginForm.getUserName() + " does not exist" + " )"));
			return "home";
		} else {
			if (registeredUserXml.getPassword().equals(loginForm.getPassword())) {	
				model.addAttribute("userObject", registeredUserXml);
				mv = new ModelAndView("redirect:getCatalog.html");
			} else {
				result.addError(new FieldError("loginForm", "password",
						"Invalid " + "password" + " ( " + "Invalid password" + " )"));
				return "home";
			}
		}
		return mv;
	}

}
