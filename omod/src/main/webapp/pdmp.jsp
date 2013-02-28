<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="subsection">${subsection}</div>

<c:choose>
  <c:when test="${meds == null}">
    <p>No PDMP Data Found</p>
  </c:when>
  <c:otherwise>
    <c:forEach items="${meds}" var="rx">
      <p><b>Prescriber:</b>${rx.prescriber}</p>
      <p><b>Drug:</b>${rx.drug}</p>
      <p><b>When Written:</b>${rx.writtenOn}</p>
      <table border='1'>
        <tr><th>Rx Number</th><th>When Filled</th><th>Pharmacy</th><th>Pharmacist</th><th>Quantity</th><th>Status</th></tr>
        <tr><td>${rx.rxNumber}</td><td>${rx.filledOn}</td><td>${rx.pharmacy}</td><td>${rx.pharmacist}</td><td>${rx.quantityFilled}</td><td>${rx.status}</td></tr>
      </table>
      <hr/>
    </c:forEach>
  </c:otherwise>
</c:choose>
