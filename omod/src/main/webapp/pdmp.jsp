<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="openmrs" uri="/WEB-INF/taglibs/openmrs.tld" %>

<c:choose>
  <c:when test="${prescriptions == null}">
    <div style="margin-left: 1em">
      <p><b>No PDMP Data Found</b></p>
    </div>
  </c:when>
  <c:otherwise>
    <c:forEach items="${prescriptions}" var="rx">
      <div class="boxHeader"><b><openmrs:formatDate date="${rx.writtenOn}" type="medium" /> - ${rx.drug}</b></div>
      <div class="box" style="margin-bottom: 0.5em">
        <p><b>Prescriber:</b>${rx.prescriber}</p>
        <table>
          <tr><th>Rx Number</th><th>When Filled</th><th>Pharmacy</th><th>Pharmacist</th><th>Quantity</th><th>Status</th></tr>
          <tr><td>${rx.rxNumber}</td><td><openmrs:formatDate date="${rx.filledOn}" type="medium" /></td><td>${rx.pharmacy}</td><td>${rx.pharmacist}</td><td>${rx.quantityFilled}</td><td>${rx.status}</td></tr>
        </table>
      </div>
    </c:forEach>
  </c:otherwise>
</c:choose>
