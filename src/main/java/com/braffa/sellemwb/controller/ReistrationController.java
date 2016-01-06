package com.braffa.sellemwb.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.braffa.sellem.model.xml.RegisteredUserMsgXml;
import com.braffa.sellem.model.xml.RegisteredUserXml;
import com.braffa.sellem.model.xml.RegisteredUserXml.RegisteredUserXmlBuilder;
import com.braffa.sellemwb.form.RegisterForm;
import com.braffa.sellemwb.form.RegistrationForm;
import com.braffa.sellemwb.validaters.EmailValidator;
import com.braffa.sellemwb.xml.parser.ConvertStringAndXML;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

@Controller
@SessionAttributes("userObject")
public class ReistrationController {

	private static final Logger logger = Logger.getLogger(ReistrationController.class);

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/sellemws").build();
	}

	private boolean validateEmailAddress(String email) {
		EmailValidator emailValidator = new EmailValidator();
		boolean valid = emailValidator.validate(email);
		return valid;
	}

	@RequestMapping("/registerStart")
	public String registerStart(Map<String, RegisterForm> model) {
		if (logger.isDebugEnabled()) {
			logger.debug("register");
		}
		RegisterForm registerForm = new RegisterForm();
		registerForm.setCurrentPage("register");
		model.put("registerForm", registerForm);
		return "register";
	}

	@RequestMapping(value = "/attemptoregister", method = RequestMethod.POST)
	public String registered(@Valid RegisterForm registeredDetails, BindingResult result,
			Map<String, RegisterForm> model, Model myModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("");
		}
		if (result.hasErrors()) {
			return "register";
		}
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());

		String xml = service.path("rest").path("registeredusers").path("findbyuserid")
				.path(registeredDetails.getUserId()).accept(MediaType.TEXT_XML).get(String.class);

		List<RegisteredUserXml> lOfRegisteredUser = ConvertStringAndXML.getLOfRegisteredUser(xml);
		if (lOfRegisteredUser.size() > 0) {
			RegisteredUserXml registeredUserXml = lOfRegisteredUser.get(0);
			System.out.println(registeredUserXml.toString());
			result.addError(new FieldError("registeredDetails", "userId",
					"userName" + " ( " + registeredDetails.getUserId() + " already exists" + " )"));
			return "register";
		}
		// is email valid
		if (!validateEmailAddress(registeredDetails.getEmail())) {
			result.addError(new FieldError("registeredDetails", "email",
					"Invalid email" + " ( " + registeredDetails.getEmail() + " please correct it" + " )"));
			return "register";
		}
		RegisteredUserXml registeredUser = new RegisteredUserXmlBuilder().authorityLevel("00")
				.userId(registeredDetails.getUserId()).email(registeredDetails.getEmail())
				.firstname(registeredDetails.getFirstname()).lastname(registeredDetails.getLastname())
				.password(registeredDetails.getPassword()).telephone(registeredDetails.getTelephone()).build();

		RegisteredUserMsgXml registeredUserMsgXml = new RegisteredUserMsgXml(registeredUser);

		service = client.resource(getBaseURI());
		WebResource createService = service.path("rest").path("registeredusers").path("create");
		ClientResponse response = createService.accept(MediaType.APPLICATION_XML).post(ClientResponse.class,
				registeredUserMsgXml);

		if (logger.isDebugEnabled()) {
			logger.debug(response.getStatus());
		}

		return "redirect:homepage.html";
	}

	@RequestMapping(value = "/showRegisteredUsers")
	public Object showRegisteredUsers(@ModelAttribute("userObject") RegisteredUserXml registeredUserXml, Model model) {
		if (logger.isDebugEnabled()) {
			logger.debug("showRegisteredUsers");
		}

		if (registeredUserXml == null || registeredUserXml.getUserId() == null) {
			return new ModelAndView("redirect:homepage.html");
		}
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());

		String xml = service.path("rest").path("registeredusers").path("findall").accept(MediaType.TEXT_XML)
				.get(String.class);

		List<RegisteredUserXml> lOfRegisteredUser = ConvertStringAndXML.getLOfRegisteredUser(xml);

		List<RegisterForm> lOfRegisteredDetails = new ArrayList<RegisterForm>();
		for (RegisteredUserXml registeredUser : lOfRegisteredUser) {
			RegisterForm registerForm = new RegisterForm(registeredUser.getEmail(), registeredUser.getFirstname(),
					registeredUser.getLastname(), registeredUser.getPassword(),
					registeredUser.getTelephone(), registeredUser.getUserId());
			lOfRegisteredDetails.add(registerForm);
		}

		RegistrationForm registrationForm = new RegistrationForm();
		registrationForm.setRegisteredDetails(lOfRegisteredDetails);
		registrationForm.setCurrentPage("registeredUsers");
		model.addAttribute("registrationForm", registrationForm);
		return "registeredUsers";
	}

}
