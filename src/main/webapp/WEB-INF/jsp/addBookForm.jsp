<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="common/header.jsp" />
<title>Tambah buku</title>
</head>
<body>

<div class="page-header">
<h1>Tambah Buku Baru</h1>
</div>

  <!-- This is the image we're attaching Jcrop to -->
  <table>
  	<tr>
  		<td>Cover depan:</td>
  		<td><img width="100" src="/imgstore?img=<c:out value="${frontCoverId}"/>" id="target" alt="[Jcrop Example]" /></td>
  	</tr>
  	<tr>
  		<td>Cover belakang:</td>
  		<td><img width="100" src="/imgstore?img=<c:out value="${backCoverId}"/>" id="target" alt="[Jcrop Example]" /></td>
  	</tr>
  	<tr>
  		<td>Judul Buku:</td>
  		<td><input type="text" name="title" value="<c:out value="${title}"/>" /> <a href="<c:out value="/buku/tambah/judul?id=${param.id}"/>">Dari cover depan</a></td>
  	</tr>
  </table>

</body>
</html>