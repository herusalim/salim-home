<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page
	import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService"%>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="common/header.jsp" />
<script src="/js/uploadImage.js"></script>

<title>Tambah buku</title>
</head>
<body>

	<c:if test="${(empty imgSource)}">

		<form
			action="<%=blobstoreService.createUploadUrl("/buku/tambah/upload")%>"
			method="post" enctype="multipart/form-data">
			<input type="file" name="myFile" accept="image/*" /> <br /> <input
				type="submit" value="Upload File" />
		</form>
	</c:if>

	<c:if test="${not empty imgSource}">
		<img class="my-image" src="/imgstore?img=<c:out value="${imgSource}"/>" />
		<script>
			$('.my-image').croppie();
		</script>
		
		<div class="demo"></div>
<script>
$('.demo').croppie({
    url: 'demo/demo-1.jpg',
});
</script>
<!-- or even simpler -->
<img class="my-image2" src="/imgstore?img=<c:out value="${imgSource}"/>" />
<script>
$('.my-image2').croppie();
</script>
	</c:if>

	<div id="progress"></div>
</body>
</html>