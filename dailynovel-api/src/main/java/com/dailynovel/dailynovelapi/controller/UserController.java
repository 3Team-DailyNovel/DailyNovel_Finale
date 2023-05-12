package com.dailynovel.dailynovelapi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailynovel.dailynovelapi.entity.Member;
import com.dailynovel.dailynovelapi.service.MemberService;
import com.dailynovel.dailynovelapi.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("users")
public class UserController {
	@Autowired
	private MemberService MemberService;
	@Autowired
	private UserService service;

	@PostMapping("signup")
	public boolean signup(int id,String nickname, String password, String email,
			String phoneNumber) {
				password = service.PasswordEncoder(password);
				boolean result= service.signup(id,nickname,password, email, phoneNumber);
		return result;
	}

	@RequestMapping("mailCheck")
	public boolean mailCheck(String email, HttpSession session) throws Exception {

		String authCode = service.randNum();
		boolean mailCheck = service.mailCheck(email, authCode, "회원가입 인증메일 입니다.", "이메일 인증번호:");
		session.setAttribute("authCode", authCode);

		return mailCheck;

	}

	@RequestMapping("nicknameCheck")
	public boolean nicknameCheck(String nickname) {

		int samenicknameNumber = service.FindSameNickname(nickname);
		if (samenicknameNumber == 0) {
			return true;
		}
		return false;
	}

	@RequestMapping("emailCheckNum")
	public boolean emailCheckNum(int emailCheckNum, HttpSession session) {
		int passwordChangeAuthCode = (int) session.getAttribute("authCode");
		if (emailCheckNum == passwordChangeAuthCode) {
			String checkNum = service.randNum();
			session.setAttribute("passwordChangeauthCode", checkNum);
			return true; //처리
		}

		return false;
	};

}
