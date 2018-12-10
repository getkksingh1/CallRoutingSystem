
 <%@page import="com.triyasoft.model.UserModel"%>
<%@page import="com.triyasoft.utils.ProjectUtils"%>
  <%
    
    boolean checkIfSessionValid = ProjectUtils.checkSessionValidity(request);
    if(!checkIfSessionValid) {
		response.getWriter().println("<font color=red>Session has Expired.</font>");
		String baseURL = ProjectUtils.getBaseURL(request);
		response.sendRedirect(baseURL+"/login.jsp");
		return;
    }
    
    %>

<html>
<head>
    <meta charset="utf-8" />
                    <script src="http://www.jtable.org/Scripts/jquery-1.9.1.min.js" type="text/javascript"></script>
    			<script src="http://www.jtable.org/Scripts/jquery-ui-1.10.0.min.js" type="text/javascript"></script>
            <link href="js/themes/metro/brown/jtable.css" rel="stylesheet" type="text/css" />
        <link href="css/jquery-ui.css" rel="stylesheet" type="text/css" />
<style>
body {
    font-family: 'Roboto', sans-serif;
    color: #222;
}
body {
    font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
    font-size: 14px;
    line-height: 1.42857143;
    color: #333;
}
html {
    font-size: 10px;
}
html {
    font-family: sans-serif;
    -webkit-text-size-adjust: 100%;
}

ul {
    list-style-type: none;
    margin: 0;
    padding: 0;
    overflow: hidden;
    background-color: #468bc0; 
}

li {
    float: left;
}

li a, .dropbtn {
    display: inline-block;
    color: white;
    text-align: center;
    padding: 14px 16px;
    text-decoration: none;
}

li a:hover, .dropdown:hover .dropbtn {
      background-color: white;
      color: #7f2021;
}

li.dropdown {
    display: inline-block;
}

.dropdown-content {
    display: none;
    position: absolute;
    background-color: #f9f9f9;
    min-width: 160px;
    box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
    z-index: 1;
}

.dropdown-content a {
    color: black;
    padding: 12px 16px;
    text-decoration: none;
    display: block;
    text-align: left;
}

.dropdown-content a:hover {background-color: #f1f1f1}

.dropdown:hover .dropdown-content {
    display: block;
}
</style>

        <script src="js/jquery.jtable.min.js" type="text/javascript"></script>

        

    </head>
    <body>
    
    <ul>
  <li><a href="dashboard.jsp">Dashboard</a></li>
  
    <li><a href="live_calls.jsp">Calls</a></li>


<li class="dropdown">
    <a href="traffic_sources.jsp" class="dropbtn">Traffic Sources</a>
  
  </li>
 

  <li class="dropdown">
    <a href="buyers.jsp" class="dropbtn">Buyers</a>
  </li>
  
  <li class="dropdown">
    <a href="filters.jsp" class="dropbtn">Filters</a>
   
  </li>
  
  <li class="dropdown">
    <a href="numbers.jsp"" class="dropbtn">Numbers</a>
   
  </li>
  
       <li><a href="users.jsp">Users</a></li>
  

 <li class="dropdown">
    <a href="reports.jsp">Reports</a>
    
    <div class="dropdown-content">
  <a href="trafficsourcereport.jsp">Traffic Sources Summary</a>
      <a href="sourcesummary.jsp">Traffic Source Summary(Date-wise)</a>
  
    </div>
  </li>


 <li class="dropdown" style="float:right">
<%
 UserModel  user  = (UserModel)request.getSession().getAttribute("user");
if( user ==null || user.getUserroleid() != 1)
{
	response.sendRedirect(ProjectUtils.getBaseURL(request)+"/login.jsp");
	return;
	}

 %>
    <a href="javascript:void(0)" class="dropbtn"><%out.print(user.getFirstname()+"  "+user.getLastname()); %> </a>
    <div class="dropdown-content">
      <a href="#">Profile</a>
      <a href="LogoutServlet">Logout</a>
    </div>
  </li>
   

  
</ul>
    

    <div id="StudentTableContainer">
</div>
<script type="text/javascript">

var filterMessages = {
    serverCommunicationError: 'An error occured while communicating to the server.',
    loadingMessage: 'Loading Filters...',
    noDataAvailable: 'No data available!',
    addNewRecord: 'Add new Filter',
    editRecord: 'Edit Filter',
    areYouSure: 'Are you sure?',
    deleteConfirmation: 'This record will be deleted. Are you sure?',
    save: 'Save',
    saving: 'Saving',
    cancel: 'Cancel',
    deleteText: 'Delete',
    deleting: 'Deleting',
    error: 'Error',
    close: 'Close',
    cannotLoadOptionsFor: 'Can not load options for field {0}',
    pagingInfo: 'Showing {0}-{1} of {2}',
    pageSizeChangeLabel: 'Row count',
    gotoPageLabel: 'Go to page',
    canNotDeletedRecords: 'Can not deleted {0} of {1} records!',
    deleteProggress: 'Deleted {0} of {1} records, processing...'
};
    var cachedCityOptions = null;

    $(document).ready(function () {

        $('#StudentTableContainer').jtable({
        	messages:filterMessages,
            title: 'Filter List',
            paging: false,
            sorting: true,
            multiSorting: true,
            defaultSorting: 'Name ASC',
            deleteConfirmation: function(data) {
            	    data.deleteConfirmMessage = 'Are you sure to delete Filter ?';
            	},
            actions: {
                listAction: function (postData, jtParams) {
                    console.log("Loading from custom function...");
                    return $.Deferred(function ($dfd) {
                        $.ajax({
                            url: 'filters?requestType=loadfilters&abac=' + jtParams.jtStartIndex + '&jtPageSize=' + jtParams.jtPageSize + '&jtSorting=' + jtParams.jtSorting,
                            type: 'POST',
                            dataType: 'json',
                            data: postData,
                            success: function (data) {
                                $dfd.resolve(data);
                            },
                            error: function () {
                                $dfd.reject();
                            }
                        });
                    });
                },
               
                
                deleteAction: function (postData) {
                    console.log("deleting from custom function...");
                    return $.Deferred(function ($dfd) {
                        $.ajax({
                            url: 'filters?requestType=deletefilter',
                            type: 'POST',
                            dataType: 'json',
                            data: postData,
                            success: function (data) {
                                $dfd.resolve(data);
                            },
                            error: function () {
                                $dfd.reject();
                            }
                        });
                    });
                },
                createAction: function (postData) {
                    console.log("creating from custom function...");
                    return $.Deferred(function ($dfd) {
                        $.ajax({
                            url: 'filters?requestType=addfilter',
                            type: 'POST',
                            dataType: 'json',
                            data: postData,
                            success: function (data) {
                                $dfd.resolve(data);
                            },
                            error: function () {
                                $dfd.reject();
                            }
                        });
                    });
                },
                updateAction: function (postData) {
                    console.log("updating from custom function...");
                    return $.Deferred(function ($dfd) {
                        $.ajax({
                            url: 'filters?requestType=updatefilter',
                            type: 'POST',
                            dataType: 'json',
                            data: postData,
                            success: function (data) {
                                $dfd.resolve(data);
                            },
                            error: function () {
                                $dfd.reject();
                            }
                        });
                    });
                }
            },
            fields: {
                  id: {
                    key: true,
                    create: false,
                    edit: false,
                    list: false
                },
               
                buyer_id: {
                    title: 'Buyer',
                    options: 'buyers?requestType=loadBuyersOption'
                },
                
              
                
                operator: {
                    title: 'Operator',
                    options: { 'Eq': 'Wants Call From', 'NotEq': 'Dont Need Calls from' }
                },
                
                
                source_id: {
                    title: 'Traffic Source',
                    options: 'sources?requestType=loadSourcesOption'
                },
              
                
                
                
                is_active: { 
                    title: 'Status',
                    options: { '0': 'InActive', '1': 'Active' },
                    defaultValue: '1'
                }
                
            }
        });

        //Load student list from server
        $('#StudentTableContainer').jtable('load');
    });

</script>

    
</body>
</html>



