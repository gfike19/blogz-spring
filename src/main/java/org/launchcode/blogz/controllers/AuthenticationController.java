package org.launchcode.blogz.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.User;
import org.launchcode.blogz.models.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController extends AbstractController {
	@Autowired
	private UserDao userDao;
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm() {
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(HttpServletRequest request, Model model) {
		//  - implement signup
		String uname = request.getParameter("username"); //when using with static vars use class not instance, User not u
		String pwd = request.getParameter("password");
		String vpwd = request.getParameter("verify");
		String username_error = "";
		String password_error = "";
		String verify_error = "";
		
		User u = new User (uname,pwd);
		
		if (User.isValidUsername(uname) && User.isValidPassword(pwd) &&pwd.equals(vpwd)) {
			HttpSession s = request.getSession();
			model.addAttribute("username", uname);
			model.addAttribute("password", pwd);
			userDao.save(u);
			setUserInSession(s, u);
		} else if (!User.isValidUsername(uname)) {
			username_error = "Invalid username. Select another one";
			model.addAttribute("username_error", username_error);
			return "signup";
		} else if (!User.isValidPassword(pwd)) {
			password_error = "Invalid password. Enter another one.";
			model.addAttribute("password_error", password_error);
			return "signup";
		} else if (!pwd.equals(vpwd)) {
			verify_error = "Passwords do match. Reenter them.";
			model.addAttribute("verify_error", verify_error);
			return "signup";
		} 
		
		return "redirect:blog/newpost";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {
		
		//  - implement login
		HttpSession s = request.getSession();
		String uname = request.getParameter("username");
		String pwd = request.getParameter("password");
		User u = new User(uname,pwd);
		User db_u = userDao.findByUid(u.getUid());
		if (u.getPwHash().equals(db_u.getPwHash())) {
		setUserInSession(s, u);
		}
		else {
			String verify_error = "Incorrect password";
			model.addAttribute("verify_error", verify_error);
		}
		return "redirect:blog/newpost";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
        request.getSession().invalidate();
		return "redirect:/";
	}
	
	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String error (HttpServletRequest request) {
		return "error";
	}
	
	@RequestMapping(value = "/error", method = RequestMethod.POST)
	public String error (HttpServletRequest request, Model model) {
		return "error";
	}
}
