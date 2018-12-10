package com.stt.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stt.domain.AuthVO;
import com.stt.domain.LoginDTO;
import com.stt.domain.SignupDTO;
import com.stt.service.UserService;

@Controller
public class UserController {
	private final static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Inject
	private UserService service;
	
	@GetMapping("/signup")
	public String signupGET() {
		logger.info("GET signup page");
		return "signup";
	}
	
	@PostMapping("/signup")
	public String signupPOST(SignupDTO dto, Model model) {
		logger.info("POST signup data: "+dto.toString());
		String url = "signup";
		try {
			if(service.isUsingEmail(dto.getEmail())) {
				model.addAttribute("error","��ϵ� �̸����Դϴ�");
				return url;
			}
			if(!dto.getPassword().equals(dto.getConfirm_password())) {
				model.addAttribute("error", "�н����尡 ��ġ�����ʽ��ϴ�");
				return url;
			}
			service.regist(dto);
			url = "redirect:/";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}
	
	@GetMapping("/login")
	public void loginGET() {
		logger.info("GET login page");
	}
	
	@PostMapping("/login")
	public String loginPOST(LoginDTO dto, HttpSession session, Model model) {
		logger.info("POST login data: " + dto.toString());
		try {
			AuthVO user = service.getUser(dto);
			if(user!=null) {
				session.setAttribute("user", user);
				return "redirect:/";
			} else {
				model.addAttribute("error", "�̸��� Ȥ�� �н����尡 �߸��Ǿ����ϴ�");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "login";
	}

}