<!-- catalog.jsp -->

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<link REL="StyleSheet" TYPE="text/css" HREF="css/styles.css">
<style>
</style>
<script src='javascript/generic.js'></script>
<script>
	
</script>
<h2 id="heading">${catalogForm.header}</h2>
<div class="features">
<c:forEach items="${catalogForm.lOfProducts}" var="product" varStatus="prod">
	<div class="product_box">
		<div class="product_icon"><img  src="${product.imageURL}"></div>
		<div class="product_content">
			<div class="product_details">
				<p title="${product.author}"><c:out value="${product.author}"/></p>
				<p title="${product.title}"><c:out value="${product.title}"/></p>
				<p title="${product.productId}"><c:out value="${product.productId}"/></p>
			</div>
			<div class="product_options">		
			<c:choose>
				<c:when test="${catalogForm.showLinks == true && userObject.authorityLevel != null}">
					<p><a href="deleteProduct.html?productId=${product.productId}&productIndex=${product.productIndex}">Delete</a></p>
				</c:when>
				<c:when test="${catalogForm.isNewProduct()}">
					<p><a href="saveNewProduct.html?productId=${product.productId}&productIndex=${product.productIndex}">Add New Product</a></p>
				</c:when>
			</c:choose>
			<c:if test="${!catalogForm.isNewProduct()}">
				<p><a href="addExistingProduct.html?productId=${product.productId}">Add Existing Product</a></p>
			</c:if>
			<p><a href="getProductUserDetails.html?productId=${product.productId}">Who Has This</a></p>
			</div>
		</div>
	</div>
</c:forEach>	
</div>
<div class="clear"></div>
