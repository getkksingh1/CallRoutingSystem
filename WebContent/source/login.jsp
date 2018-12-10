
<!doctype html>
<!--[if IE 8]>         <html lang="en" class="no-js ie ie8 lt-ie9"> <![endif]-->
<!--[if IE 9]>         <html lang="en" class="no-js ie ie9">        <![endif]-->
<!--[if gt IE 9]><!--> <html lang="en" class="no-js">               <!--<![endif]-->

<head>

<meta charset="utf-8">



<title>Login</title>





<meta name="description" content="">
<meta name="author" content="">
<style type"text/css">
  .container-solo {
    max-width: 460px !important;
  }
.alert {
padding: 8px 35px 8px 14px;
margin-bottom: 20px;
text-shadow: 0 1px 0 rgba(255, 255, 255, 0.5);
background-color: #fcf8e3;
border: 1px solid #fbeed5;
-webkit-border-radius: 4px;
-moz-border-radius: 4px;
border-radius: 4px;
}

.alert,
.alert h4 {
color: #c09853;
}

.alert h4 {
margin: 0;
}

.alert-danger,
.alert-error {
background-color: #f2dede;
border-color: #eed3d7;
color: #b94a48;
}

.alert-danger h4,
.alert-error h4 {
color: #b94a48;
}

</style>



<link rel="shortcut icon" href="https://plivostatic.s3.amazonaws.com/images/favicon-white-png.png">

<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

<link rel="stylesheet" href="https://new-ui-cms-plivo.s3.amazonaws.com/prod/assets/dist/css/style.min.css">
<link href="https://plivostatic.s3.amazonaws.com/css/new-theme.css" rel="stylesheet">

<style>
  #header { position: static !important; box-shadow: none; border-bottom: none; }
  #content { padding-top: 30px; padding-bottom: 30px; }
  body { position: relative; background: #fff; }
  .center-me { width: 80%; margin: 20px auto; }
  .logo { position: static !important; width: 140px !important; }
  #nav { float: right; clear: none !important; text-align: right !important; }
  .nav-user { margin-top: 5px !important; }
  .m-t-30 { margin-top: 30px; }
  .m-t-20 { margin-top: 20px; }
  .m-t-50 { margin-top: 50px; }
  .copyright { font-size: 14px; }
  .container-solo { min-height: 550px; }
  .email-container {  margin-right: 5px; }
  @media (max-width: 767px) {
    #nav { float: none !improtant; clear: both !important; }
    .email-container { display: block; }
  }
  @media (max-width: 979px) {
    #nav, .nav-user { float: right !important; }
  }
</style>



<script src="//use.typekit.net/faz8tkt.js"></script>
<script>
try{Typekit.load();}catch(e){}
</script>

<script src="https://new-ui-cms-plivo.s3.amazonaws.com/js/lib/modernizr-2.6.2.min.js"></script>


</head>

<body 

class="_login"

>
  <div class="center-me">
    <header id="header" class="header">
      <div class="container-fluid">

        <button id="nav-btn" class="btn-nav">Nav</button>
        <nav id="nav" class="nav-user">
            
        
            
        </nav>
      </div>
    </header>

    <div id="content">

      
<div class="container-solo">
    
    <form action="/CallTracker/LoginServlet" id="login" class="form-simple" method="POST"><div style='display:none;'><input type='hidden' id='csrfmiddlewaretoken' name='csrfmiddlewaretoken' value='341a8877e7a06a7b3aa2a819b015b94b' /></div>
    
        <header class="form-header">
            <h1>Login</h1>
        </header>

  
        
        <div style='display:none'><input type='hidden' name='csrfmiddlewaretoken' value='341a8877e7a06a7b3aa2a819b015b94b' /></div>
        
    
        <div class="field">
        <label for="login-user" class="hidden-vis" id="label_id_username">Email</label>

        <input type="text" name="user"  value=""  id="id_username" placeholder="Email" required>
        
        </div>
        
        <div class="field">
        <label for="login-user" class="hidden-vis" id="label_id_password">Password</label>

        <input type="password" name="pwd"  id="id_password" placeholder="Password" required>
        
        </div>
    
        <div class="text-center">
          <button type="submit" id="login-sub" class="btn-primary- btn-full">Login</button>
        </div>
        <div class="text-center m-t-20">
	
	        
  <input type="checkbox" name="remember" value="true" />
  <label> Remember Me!</label>

        </div>
    </form>
    <footer class="form-footer">
        
    </footer>
</div>



      <div class="copyright text-center m-t-50">© 2017 Triyasoft Inc.</div>
    </div>
  </div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script src="https://new-ui-cms-plivo.s3.amazonaws.com/js/script.min.js"></script>

<!-- Google analytics -->
<script type="text/javascript">
    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', 'UA-23474761-2']);
    _gaq.push(['_setDomainName', 'plivo.com']);
    _gaq.push(['_trackPageview']);
    (function() {
        var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
    })();
</script>
<!-- end google analytics -->

<script type="text/javascript">var _kmq = _kmq || [];
    var _kmk = _kmk || 'd120fc047f6354d46e364788e2aea1ed87fd794b';
    function _kms(u){
        setTimeout(function(){
                var d = document, f = d.getElementsByTagName('script')[0],
                s = d.createElement('script');
                s.type = 'text/javascript'; s.async = true; s.src = u;
                f.parentNode.insertBefore(s, f);
                }, 1);
    }
_kms('//i.kissmetrics.com/i.js');
_kms('//doug1izaerwt3.cloudfront.net/' + _kmk + '.1.js');
var KS_KISSMETRICS_DATA_TRACKING_ENABLED =  false ;

</script>

<script src="https://plivostatic.s3.amazonaws.com/js/kissmetrics.js"></script>

 



</body>
</html>
