<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<!DOCTYPE html>
<html>
<head>
<title>Upload gambar</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="/css/w3.css">
</head>
<body>
	<h3>File Upload:</h3>
	Select a file to upload:
	<br />
	<form action="<%= blobstoreService.createUploadUrl("/upload_tampilkan") %>" method="post"
		enctype="multipart/form-data">
		<input type="file" name="myFile" accept="image/*"/> <br /> <input
			type="submit" value="Upload File" />
	</form>
</body>
</html>