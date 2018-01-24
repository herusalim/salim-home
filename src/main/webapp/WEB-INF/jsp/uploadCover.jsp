<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page
	import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService"%>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<!DOCTYPE html>
<html>
<head>
<jsp:include page="common/header.jsp" />
<script src="/js/uploadImage.js"></script>

<title>Tambah buku</title>
</head>
<body>

	<form
		action="<%=blobstoreService.createUploadUrl("/buku/tambah/upload?id=" + request.getAttribute("entityId"))%>"
		method="post" enctype="multipart/form-data">
		<table>
			<tr>
				<td>Cover Depan:</td>
				<td><input type="file" name="frontCoverImage" accept="image/*" /></td>
			</tr>
			<tr>
				<td>Cover Belakang:</td>
				<td><input type="file" name="backCoverImage" accept="image/*" /></td>
			</tr>
			<tr>
				<td><input type="submit" value="Upload File" /></td>
			</tr>
		</table>
	</form>
</body>
</html>