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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.braffa.productlookup.amazon.ProductLookUp;
import com.braffa.sellem.model.xml.ProductMsgXml;
import com.braffa.sellem.model.xml.ProductXml;
import com.braffa.sellem.model.xml.RegisteredUserXml;
import com.braffa.sellem.model.xml.UserToProductMsgXml;
import com.braffa.sellem.model.xml.UserToProductXml;
import com.braffa.sellemwb.form.CatalogForm;
import com.braffa.sellemwb.form.ProductForm;
import com.braffa.sellemwb.form.RegisteredUserToProduct;
import com.braffa.sellemwb.form.WhoHasThisForm;
import com.braffa.sellemwb.xml.parser.ConvertStringAndXML;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

@Controller
@SessionAttributes({ "userObject", "newProduct" })
public class ProductController {

	private static final Logger logger = Logger.getLogger(ProductController.class);

	// XML node keys
	public static final String KEY_AUTHOR = "author";
	public static final String KEY_MANUFACTURER = "manufacturer";
	public static final String KEY_PRODUCT_GROUP = "productgroup";
	public static final String KEY_PRODUCT_ID = "productid";
	public static final String KEY_PRODUCT_INDEX = "productIndex";
	public static final String KEY_PRODUCT_ID_TYPE = "productidtype";
	public static final String KEY_SOURCE = "source";
	public static final String KEY_SOURCE_ID = "sourceid";
	public static final String KEY_SOURCE_TITLE = "title";
	public static final String KEY_IMAGE_URL = "imageURL";

	public static final String KEY_IMAGE_BITMAP = "imagebitmap";

	public static final String KEY_URL = "URL";

	public static final String KEY_IMAGE_HEIGHT = "Height";
	public static final String KEY_IMAGE_WIDTH = "Width";

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/sellemws").build();
	}

	private final String getElementValue(Node elem) {
		Node child;
		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
					if (child.getNodeType() == Node.TEXT_NODE) {
						return child.getNodeValue();
					}
				}
			}
		}
		return "";
	}

	private String getValue(Element item, String key) {
		NodeList n = item.getElementsByTagName(key);
		String value = this.getElementValue(n.item(0));
		return value;
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

	@RequestMapping(value = "/getProductUserDetails.html")
	public Object getProductUserDetails(@ModelAttribute("newProduct") ProductXml productXml,
			@RequestParam("productId") String productid, Model model) {
		if (logger.isDebugEnabled()) {
			logger.debug("getProductUserDetails productid " + productid);
		}

		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		String xmlStr = service.path("rest").path("usertoproduct").path("search").path("productId").path(productid)
				.accept(MediaType.TEXT_XML).get(String.class);
		List<UserToProductXml> lOfUserToProducts = ConvertStringAndXML.getLOfUserToProducts(xmlStr);
		UserToProductMsgXml userToProductMsgXml = ConvertStringAndXML.convertStringToUserToProductToObject(xmlStr);
		ProductXml product = userToProductMsgXml.getProduct();
		WhoHasThisForm whoHasThisForm = new WhoHasThisForm();

		if (lOfUserToProducts.size() > 0) {
			List<RegisteredUserToProduct> lOfRegisteredUserToProduct = new ArrayList<RegisteredUserToProduct>();
			for (UserToProductXml userToProductXml : lOfUserToProducts) {
				RegisteredUserToProduct registeredUserToProduct = new RegisteredUserToProduct();
				registeredUserToProduct.setCrDate(userToProductXml.getCrDate());
				registeredUserToProduct.setEmail(userToProductXml.getEmail());
				registeredUserToProduct.setUserId(userToProductXml.getUserId());
				registeredUserToProduct.setProductId(userToProductXml.getProductId());
				registeredUserToProduct.setProductIndex("0");
				lOfRegisteredUserToProduct.add(registeredUserToProduct);
			}
			whoHasThisForm.setlOfRegisteredUserToProduct(lOfRegisteredUserToProduct);
		} else {
			if (product.getProductId().length() == 0) {
			Map<String, Object> modelAttributes = model.asMap();
			if (modelAttributes.containsKey("newProduct")) {
				product = (ProductXml) modelAttributes.get("newProduct");
			}
			}
		}

		whoHasThisForm.setProduct(product);
		whoHasThisForm.setShowLinks(false);
		whoHasThisForm.setHeader("Who Has This");
		whoHasThisForm.setCurrentPage("whoHasThis");
		model.addAttribute("whoHasThisForm", whoHasThisForm);
		return "whohasthis";
	}

	@RequestMapping("/addNewProduct")
	public Object addNewProduct(@ModelAttribute("userObject") RegisteredUserXml registeredUserXml, Model model) {
		if (logger.isDebugEnabled()) {
			logger.debug("addNewProduct");
		}
		if (registeredUserXml == null || registeredUserXml.getUserId() == null) {
			return new ModelAndView("redirect:homepage.html");
		}
		ProductForm productForm = new ProductForm();
		productForm.setProductidtype("EAN");
		productForm.setCurrentPage("newProduct");
		productForm.setAction("new");
		productForm.setCurrentPage("newProduct");
		model.addAttribute("productForm", productForm);
		return "product";
	}

	@RequestMapping(value = "/lookUpProduct", method = RequestMethod.POST)
	public Object lookUpProduct(@Valid ProductForm newProductForm, BindingResult result, Model model) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveProduct " + newProductForm.getProductid());
		}
		CatalogForm catalogForm = new CatalogForm();
		catalogForm.setNewProduct(false); 
		try {
			ProductXml existingProduct = getProduct(newProductForm.getProductid());
			if (existingProduct == null) {
				catalogForm.setNewProduct(true); 
				String barcodeFormat = "EAN";
				Document document = ProductLookUp.getProductsWithImage(newProductForm.getProductid(), barcodeFormat, null,
						null, "All", "ThumbnailImage");
				List<ProductXml> lOfProducts = new ArrayList<ProductXml>();
				NodeList productList = document.getElementsByTagName("product");
				for (int i = 0; i < productList.getLength(); i++) {
					Element element = (Element) productList.item(i);
					ProductXml product = new ProductXml();
					product.setAuthor(getValue(element, KEY_AUTHOR));
					product.setImageLargeURL(getValue(element, KEY_URL));
					product.setImageURL(getValue(element, KEY_URL));
					product.setProductgroup(getValue(element, KEY_PRODUCT_GROUP));
					product.setManufacturer(KEY_MANUFACTURER);
					product.setProductId(getValue(element, KEY_PRODUCT_ID));
					product.setProductIndex("0");
					product.setProductidtype(getValue(element, KEY_PRODUCT_ID_TYPE));
					product.setSource(getValue(element, KEY_SOURCE));
					product.setSourceid(getValue(element, KEY_SOURCE_ID));
					product.setTitle(getValue(element, KEY_SOURCE_TITLE));
					lOfProducts.add(product);
				}
				model.addAttribute("newProduct", lOfProducts.get(0));
				catalogForm.setmOfProducts(lOfProducts);
			} else {
				model.addAttribute("newProduct", existingProduct);
				List<ProductXml> lOfProducts = new ArrayList<ProductXml>();
				lOfProducts.add(existingProduct);
				catalogForm.setmOfProducts(lOfProducts);
			}
			catalogForm.setShowLinks(false);
			catalogForm.setHeader("Search Results");
			catalogForm.setOrigin("productLookUp");
			catalogForm.setCurrentPage("myCatalogue");
			model.addAttribute("catalogForm", catalogForm);
			return "catalog";
		} catch (Exception e) {
			ProductForm productForm = new ProductForm();
			productForm.setProductidtype("EAN");
			productForm.setCurrentPage("newProduct");
			productForm.setAction("new");
			productForm.setErrorMessage("Cannot connect to internet");
			model.addAttribute("productForm", productForm);
			return "product";
		}
	}

	@RequestMapping(value = "/saveNewProduct.html", method = RequestMethod.GET)
	public ModelAndView saveNewProduct(@ModelAttribute("userObject") RegisteredUserXml registeredUserXml,
			@RequestParam("productId") String productid, Model model) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveNewProduct " + productid);
		}
		if (registeredUserXml == null || registeredUserXml.getUserId() == null) {
			return new ModelAndView("redirect:homepage.html");
		}
		String barcodeFormat = "EAN";
		Document document = ProductLookUp.getProductsWithImage(productid, barcodeFormat, null, null, "All",
				"ThumbnailImage");
		List<ProductXml> lOfProducts = new ArrayList<ProductXml>();
		NodeList productList = document.getElementsByTagName("product");
		for (int i = 0; i < productList.getLength(); i++) {
			Element element = (Element) productList.item(i);
			ProductXml product = new ProductXml();
			product.setAuthor(getValue(element, KEY_AUTHOR));
			product.setImageLargeURL(getValue(element, KEY_URL));
			product.setImageURL(getValue(element, KEY_URL));
			product.setProductgroup(getValue(element, KEY_PRODUCT_GROUP));
			product.setManufacturer(KEY_MANUFACTURER);
			product.setProductId(getValue(element, KEY_PRODUCT_ID));
			product.setProductidtype(getValue(element, KEY_PRODUCT_ID_TYPE));
			product.setSource(getValue(element, KEY_SOURCE));
			product.setSourceid(getValue(element, KEY_SOURCE_ID));
			product.setTitle(getValue(element, KEY_SOURCE_TITLE));
			lOfProducts.add(product);
		}
		ProductMsgXml xmlProductMsg = new ProductMsgXml(lOfProducts.get(0));
		int status = addProduct(xmlProductMsg);
		if (status == 200) {
			processUserToProduct(lOfProducts.get(0).getProductId(), registeredUserXml.getUserId(), "create");
		}
		return new ModelAndView("redirect:myCatalog.html");
	}

	private int addProduct(ProductMsgXml xmlProductMsg) {
		if (logger.isDebugEnabled()) {
			logger.debug("addProduct");
		}
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		WebResource createService = service.path("rest").path("product").path("create");
		ClientResponse response = createService.accept(MediaType.APPLICATION_XML).post(ClientResponse.class,
				xmlProductMsg);
		return response.getStatus();
	}

	private int processUserToProduct(String productid, String userId, String action) {
		if (logger.isDebugEnabled()) {
			logger.debug("processUserToProduct " + productid + " " + userId + " " + action);
		}
		UserToProductXml userToProduct = new UserToProductXml();
		userToProduct.setUserId(userId);
		userToProduct.setProductId(productid);
		userToProduct.setProductIndex("0");
		UserToProductMsgXml userToProductMsg = new UserToProductMsgXml(userToProduct);
		ClientConfig userToProductconfig = new DefaultClientConfig();
		Client userToProductClient = Client.create(userToProductconfig);
		WebResource userToProductService = userToProductClient.resource(getBaseURI());
		WebResource usertoproductCreateService = userToProductService.path("rest").path("usertoproduct").path(action);
		ClientResponse usertoproductresponse = usertoproductCreateService.accept(MediaType.APPLICATION_XML)
				.post(ClientResponse.class, userToProductMsg);
		if (logger.isDebugEnabled()) {
			logger.debug(usertoproductresponse.getStatus());
		}
		return usertoproductresponse.getStatus();
	}

	@RequestMapping(value = "/addExistingProduct.html")
	public ModelAndView addExistingProduct(@ModelAttribute("userObject") RegisteredUserXml registeredUserXml,
			@RequestParam("productId") String productid, Model model) {
		if (logger.isDebugEnabled()) {
			logger.debug("addExistingProduct " + productid);
		}
		if (registeredUserXml == null || registeredUserXml.getUserId() == null) {
			return new ModelAndView("redirect:homepage.html");
		}
		processUserToProduct(productid, registeredUserXml.getUserId(), "create");
		return new ModelAndView("redirect:myCatalog.html");
	}

	@RequestMapping(value = "/deleteProduct", method = RequestMethod.GET)
	public ModelAndView deleteProduct(@ModelAttribute("userObject") RegisteredUserXml registeredUserXml, 
			@RequestParam("productId") String productid,
			@RequestParam("productIndex") String productIndex, 
			Model model) {
		if (logger.isDebugEnabled()) {
			logger.debug("deleteProduct" + productid + " " + productIndex);
		}
		deleteToProduct(productid, registeredUserXml.getUserId());
		return new ModelAndView("redirect:myCatalog.html");
	}

	private int deleteToProduct(String productid, String userId) {
		if (logger.isDebugEnabled()) {
			logger.debug("processUserToProduct " + productid + " " + userId);
		}
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource webResource = client.resource(getBaseURI());
		ClientResponse response = webResource.path("rest").path("usertoproduct").path("delete").path(userId)
				.path(productid).path("0").delete(ClientResponse.class);
		if (logger.isDebugEnabled()) {
			logger.debug(response.getStatus());
		}
		return response.getStatus();
	}

}
