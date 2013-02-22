<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>PDMP Lookup</title>
  <script src="http://code.jquery.com/jquery-latest.js"></script>
</head>
<body>
<table>
  <tr>
     <th>Patient Id</th>
     <th>Name</th>   
     <th>Identifier</th>
  </tr>
  <c:forEach var="patient" items="${thePatientList}">
  <tr>        
  	<td>${patient.patientId}</td>
  	<td>${patient.personName}</td>
    <td>${patient.patientIdentifier}</td>
  </tr>
  </c:forEach>
</table>
<div id="subsection">${subsection}</div>
<script>

</script>
</body>
</html>