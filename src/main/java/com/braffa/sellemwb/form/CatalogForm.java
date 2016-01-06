package com.braffa.sellemwb.form;

import java.util.List;

import com.braffa.sellem.model.xml.ProductXml;

public class CatalogForm {
	
	private List<ProductXml> lOfProducts;
	
	private boolean showLinks; 
	
	private String header;
	
	private String origin;
	
	private String currentPage;
	
	private boolean newProduct = false;

	public List<ProductXml> getlOfProducts() {
		return lOfProducts;
	}

	public void setmOfProducts(List<ProductXml> lOfProducts) {
		this.lOfProducts = lOfProducts;
	}

	public boolean isShowLinks() {
		return showLinks;
	}

	public void setShowLinks(boolean showLinks) {
		this.showLinks = showLinks;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public boolean isNewProduct() {
		return newProduct;
	}

	public void setNewProduct(boolean newProduct) {
		this.newProduct = newProduct;
	}

}
