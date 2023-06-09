package com.dailynovel.dailynovelapi.service;

import com.dailynovel.dailynovelapi.entity.Member;

public interface UserService {


    boolean FindSameNickname(String nickname);

    String PasswordEncoder(String password);

    boolean signup( String nickname,String password, String email, String phoneNumber);

    boolean FindSameEmail(String email);

    boolean signup(Member member);

    String FindSamephoneNumber(String phoneNumber);


    boolean ChangePassword(String email, String password);
}
