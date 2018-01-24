<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="common/header.jsp" />

<script type="text/javascript">
	jQuery(function($) {

		var jcrop_api;

		$('#target').Jcrop({
			onChange : showCoords,
			onSelect : showCoords,
			onRelease : clearCoords
		}, function() {
			jcrop_api = this;
		});

		$('#coords').on(
				'change',
				'input',
				function(e) {
					var x1 = $('#x1').val(), x2 = $('#x2').val(), y1 = $('#y1')
							.val(), y2 = $('#y2').val();
					jcrop_api.setSelect([ x1, y1, x2, y2 ]);
				});

	});

	// Simple event handler, called from onChange and onSelect
	// event handlers, as per the Jcrop invocation above
	function showCoords(c) {
		$('#x1').val(c.x);
		$('#y1').val(c.y);
		$('#x2').val(c.x2);
		$('#y2').val(c.y2);
		$('#w').val(c.w);
		$('#h').val(c.h);
	};

	function clearCoords() {
		$('#coords input').val('');
	};
</script>

<title>Pilih Judul</title>
</head>
<body>
	<div class="page-header">
		<h1>Pilih Judul Buku</h1>
	</div>
<!-- <script type="text/javascript">document.getElementById("target").setAttribute("width", (screen.width * 0.2))</script> -->
	<!-- This is the image we're attaching Jcrop to -->
	<form method ="POST" action="<c:out value="/tambah/buku/judul/pilih?id=${param.id}"/>">
		<input type="text" size="4" id="x1" name="x1" /> 
		<input type="text" size="4" id="y1" name="y1" /> 
		<input type="text" size="4" id="x2" name="x2" /> 
		<input type="text" size="4" id="y2" name="y2" />
		<input type="text" size="4" id="w" name="w" />
		<input type="text" size="4" id="h" name="h" />
		<input type="text" id="imageWidth" name="imageWidth" value="300"/>

		<table width="400">
			<tr>
				<td>Cover depan:</td>
			</tr>
			<tr>
				<td><img
					src="/imgstore?img=<c:out value="${frontCoverId}"/>" id="target" width="300"/></td>
			</tr>
			<tr>
				<td><input type="submit" value="Pilih" /></td>
			</tr>
		</table>
	</form>

</body>
</html>