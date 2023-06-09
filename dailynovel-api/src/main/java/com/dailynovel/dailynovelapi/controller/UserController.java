package com.dailynovel.dailynovelapi.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailynovel.dailynovelapi.entity.Member;
import com.dailynovel.dailynovelapi.service.EmailVerificationService;
import com.dailynovel.dailynovelapi.service.MailCheckService;

import com.dailynovel.dailynovelapi.service.UserService;
@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	private UserService service;
	@Autowired
	private MailCheckService mailService;
	@Autowired
	private EmailVerificationService emailVerificationService;
	
	
	@PostMapping("signup")
	public boolean signup(@RequestBody Member member) {
	boolean result = service.signup(member);

		return result;
	}

	@GetMapping("nicknameCheck")
	public boolean nicknameCheck(String nickname) {
		boolean samenickname = service.FindSameNickname(nickname);
		return samenickname;
	}

	@GetMapping("PhoneNumberCheck")
	public String phoneNumberCheck(String phoneNumber) {
		String email = service.FindSamephoneNumber(phoneNumber);
		return email;
	}

	@GetMapping("emailCheck")
	public boolean emailCheck(String email) {
		boolean result= false;
		boolean sameemail = service.FindSameEmail(email);

		if(!sameemail){
			String authCode = mailService.randNum();
			emailVerificationService.saveVerificationCode(email, authCode);
			System.out.println(authCode);
			boolean mailCheck = mailService.mailCheck(email, authCode, "DailyNovel 인증메일 입니다.", "이 번호를 입력하세요:");
			result =mailCheck;
		}
		return result;

	}

	@GetMapping("EmailVerificationNumber")
	public boolean EmailVerificationNumber(String email) {
		boolean result= true;
		boolean sameemail = service.FindSameEmail(email);

		if(sameemail){
			String authCode = mailService.randNum();
			emailVerificationService.saveVerificationCode(email, authCode);
			System.out.println(authCode);
			boolean mailCheck = mailService.mailCheck(email, authCode, "DailyNovel 임시비밀번호 입니다.", "임시비밀번호 발급:");
			service.ChangePassword(email, authCode);
			result =mailCheck;
		}
		return result;

	}
	
	

	@GetMapping("emailCheckAuth")
	public boolean emailCheckAuth(String email) {
		boolean sameemail = service.FindSameEmail(email);
		return !sameemail;
	}

	@GetMapping("emailCheckNum")
	public boolean emailCheckNum(String email, String emailCheckNum) {

		return emailVerificationService.getVerificationCodeResult(email, emailCheckNum);

		
	}



}
