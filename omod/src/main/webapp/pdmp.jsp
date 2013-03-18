<%@ page import="java.util.Collections,java.util.Comparator,java.util.List,org.openmrs.module.pdmp_query.Prescription" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="openmrs" uri="/WEB-INF/taglibs/openmrs.tld" %>

<c:choose>
  <c:when test="${prescriptions == null}">
    <div style="margin-left: 1em">
      <p><b>No PDMP Data Found</b></p>
    </div>
  </c:when>
  <c:otherwise>
    <% Collections.sort((List<Prescription>)request.getAttribute("prescriptions"), new Comparator<Prescription>() {
                                public int compare(Prescription o1, Prescription o2) {
                                  return o2.getWrittenOn().compareTo(o1.getWrittenOn());
                                }
                                public boolean equals(Object obj) {
                                  return (obj == this);
                                }
                                });
    %>
    <c:forEach items="${prescriptions}" var="rx">
      <div class="boxHeader"><b><openmrs:formatDate date="${rx.writtenOn}" type="medium" /> - ${rx.drug}</b></div>
      <div class="pdmp box">
        <table>
          <tr><th>Prescriber</th><th>Rx Number</th><th>When Filled</th><th>Pharmacy</th><th>Pharmacist</th><th>Quantity</th><th>Status</th></tr>
          <tr><td>${rx.prescriber}</td><td>${rx.rxNumber}</td><td><openmrs:formatDate date="${rx.filledOn}" type="medium" /></td><td>${rx.pharmacy}</td><td>${rx.pharmacist}</td><td>${rx.quantityFilled}</td><td>${rx.status}</td></tr>
        </table>
      </div>
    </c:forEach>
  </c:otherwise>
</c:choose>
