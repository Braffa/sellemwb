package com.braffa.sellemwb.xml.parser;

import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.braffa.sellem.model.xml.ProductMsgXml;
import com.braffa.sellem.model.xml.ProductXml;
import com.braffa.sellem.model.xml.RegisteredUserMsgXml;
import com.braffa.sellem.model.xml.RegisteredUserXml;
import com.braffa.sellem.model.xml.UserToProductMsgXml;
import com.braffa.sellem.model.xml.UserToProductXml;

public class ConvertStringAndXML {
	
	private static ProductMsgXml convertStringToProductMsgXml(String xmlStr) {
		try {
			StringReader reader = new StringReader(xmlStr);
			JAXBContext jaxbContext = JAXBContext.newInstance(ProductMsgXml.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (ProductMsgXml) jaxbUnmarshaller.unmarshal(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<ProductXml> getLOfProducts(String xmlStr) {
		ProductMsgXml productMsgXml = convertStringToProductMsgXml(xmlStr);
		return productMsgXml.getLOfProducts();
	}
	
	public static UserToProductMsgXml convertStringToUserToProductToObject(String xmlStr) {
		try {
			StringReader reader = new StringReader(xmlStr);
			JAXBContext jaxbContext = JAXBContext.newInstance(UserToProductMsgXml.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (UserToProductMsgXml) jaxbUnmarshaller.unmarshal(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<UserToProductXml> getLOfUserToProducts(String xmlStr) {
		UserToProductMsgXml userToProductMsgXml = convertStringToUserToProductToObject(xmlStr);
		return userToProductMsgXml.getLOfUserToProduct();
	}
	
	private static RegisteredUserMsgXml convertStringToRegisteredUserMsgXml(String xmlStr) {
		try {
			StringReader reader = new StringReader(xmlStr);
			JAXBContext jaxbContext = JAXBContext
					.newInstance(RegisteredUserMsgXml.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (RegisteredUserMsgXml) jaxbUnmarshaller.unmarshal(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<RegisteredUserXml> getLOfRegisteredUser(String xmlStr) {
		RegisteredUserMsgXml registeredUserMsg = convertStringToRegisteredUserMsgXml(xmlStr);
		return registeredUserMsg.getLOfRegisteredUsers();
	}

}
