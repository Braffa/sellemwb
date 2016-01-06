<!-- home.jsp -->

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h2>Home Page</h2>
    
<div class="center_left">
	<div class="title_welcome"><span class="red">FREE</span> Hosting Control Panel</div>
	<div class="welcome_box">
		<p class="welcome">
			<span class="orange">Lorem ipsum dolor sit amet, consectetur adipisicing elit </span><br />
			Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. 
		</p>
        <a href="#" class="read_more">read more</a>          
	</div>
	
	<div class="features">   
		<div class="title">Main features</div>
		<ul class="list">
			<li><span>1</span><a href="#">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod</a></li>
			<li><span>2</span><a href="#">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod</a></li>
			<li><span>3</span><a href="#">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod</a></li>
			<li><span>4</span><a href="#">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod</a></li>
		</ul> 
	</div> 
	<div class="features">   
		<div class="title">Latest News</div>
		<div class="news_box">
			<div class="news_icon"></div>
			<div class="news_content">
				Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. 
			</div>   
		</div>
		<div class="news_box">
			<div class="news_icon"></div>
			<div class="news_content">
				Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. 
			</div>   
		</div>       
	</div> 
</div> 
<div class="center_right">
	<div class="software_box"><img src="images/3dbox.gif" alt="" title="" /></div>
	<form:form action="attemptlogin.html" commandName="loginForm">
		<div class="text_box">
			<div class="title">Clients login</div>
			<div class="login_form_row">					
				<label class="login_label">Username:</label>
				<form:input type="text" name="name" path="userName" class="login_input" />
				</br><FONT color=red><form:errors path="userName" /></FONT>
			</div>
			<div class="login_form_row">					
				<label class="login_label">Password:</label>
				<form:password name="pass" path="password" class="login_input" />
				</br><FONT color=red><form:errors path="password" /></FONT>
			</div>  
				<input type="image" src="images/login.gif" class="login" />                              
		</div>
	</form:form>
	<div class="testimonials">
		<div class="title">Testimonials</div>
		<div class="text_box">
			<p class="testimonial">
				Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Ut enim ad minim veniam<br />
				<strong>Edward Caloryd</strong>
			</p>
		</div>                    
	</div>
</div>  
<div class="clear"></div> 
