package com.braffa.sellemwb.form;

import java.util.ArrayList;
import java.util.List;

import com.braffa.sellem.model.xml.ProductXml;
import com.braffa.sellem.model.xml.RegisteredUserXml;
import com.braffa.sellem.model.xml.UserToProductXml;



public class DatabaseReportForm {
	
	private List<RegisteredUserXml> lOfRegisteredUser;
	
	private List<ProductXml> lOfProducts;

	private List<UserToProductXml> lOfUserToCatalog;
	
	public DatabaseReportForm () {
		lOfRegisteredUser = new ArrayList<RegisteredUserXml>();
		lOfProducts = new ArrayList<ProductXml>();
		lOfUserToCatalog = new ArrayList<UserToProductXml>();
	}

	public List<RegisteredUserXml> getlOfRegisteredUser() {
		return lOfRegisteredUser;
	}

	public void setlOfRegisteredUser(List<RegisteredUserXml> lOfRegisteredUser) {
		this.lOfRegisteredUser = lOfRegisteredUser;
	}

	public List<ProductXml> getlOfProducts() {
		return lOfProducts;
	}

	public void setlOfProducts(List<ProductXml> lOfProducts) {
		this.lOfProducts = lOfProducts;
	}

	public List<UserToProductXml> getlOfUserToCatalog() {
		return lOfUserToCatalog;
	}

	public void setlOfUserToCatalog(List<UserToProductXml> lOfUserToCatalog) {
		this.lOfUserToCatalog = lOfUserToCatalog;
	} 
	
}