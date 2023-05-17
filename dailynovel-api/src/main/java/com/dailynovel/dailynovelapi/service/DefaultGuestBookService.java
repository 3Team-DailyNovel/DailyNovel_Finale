package com.dailynovel.dailynovelapi.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dailynovel.dailynovelapi.entity.GuestBook;
import com.dailynovel.dailynovelapi.entity.GuestBookComment;
import com.dailynovel.dailynovelapi.mbentity.MbGuestBook;
import com.dailynovel.dailynovelapi.mbentity.MbGuestBookAll;
import com.dailynovel.dailynovelapi.mbrepository.MbGuestBookRepository;
import com.dailynovel.dailynovelapi.repository.GuestBookRepository;

import jakarta.transaction.Transactional;

@Service
public class DefaultGuestBookService implements GuestBookService {

  @Autowired
  private GuestBookRepository repository;

  @Autowired
  private MbGuestBookRepository mbRepository;

  // 방명록 조회 서비스
  @Override
  public List<GuestBook> viewGuestBooks() {
    List<GuestBook> list = repository.findByMemberId(1);
    return list;
  }

  // 방명록 작성 서비스
  @Override
  public void writeGuestBook(Map<String, Object> guestbook) {
    Integer memberId = (Integer) guestbook.get("memberId");
    Integer writerId = (Integer) guestbook.get("writerId");
    String content = guestbook.get("content").toString();

    MbGuestBook temp = MbGuestBook.builder()
        .content(content)
        .memberId(memberId)
        .writerId(writerId)
        .build();
    mbRepository.insertGuestbook(temp);
  }

  @Override
  public GuestBookComment writeGuestBookComment() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'writeGuestBookComment'");
  }

  @Override
  public List<MbGuestBookAll> viewGuestBookAll() {
    List<MbGuestBookAll> list = mbRepository.findAllbyId(1);
    return list;
  }

}
