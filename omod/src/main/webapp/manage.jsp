<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>
<script type="text/javascript">
function updateRecord()
{
	document.PDMPManage.action="manage.form";
	document.PDMPManage.method="post";
	document.PDMPManage.submit();
}
</script>
<p>Hello ${user.systemId}!</p>
<form id="thisPDMPManageForm" name="PDMPManage" method="post" action="manage.form">
<table>
  <tr>
     <th width="40%">PDMP URL</th>
     <th width="30%">UserID</th>   
     <th width="30%">Password</th>
  </tr>
  <tr>        
  	<td><input type="text" name="pdmpURL" size="40px" value="${pdmpURL}"></input></td>
  	<td><input type="text" name="pdmpUID" size="30px" value="${pdmpUID}"></input></td>
    <td><input type="password" name="pdmpPWD" size="30px"></input></td>
  </tr>
</table>

<input type="button" name="button" value="Save" onclick="updateRecord()">
</form>
<%@ include file="/WEB-INF/template/footer.jsp"%>