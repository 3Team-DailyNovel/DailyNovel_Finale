package com.dailynovel.dailynovelapi.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.dailynovel.dailynovelapi.entity.GuestBook;
import com.dailynovel.dailynovelapi.entity.GuestBookComment;
import com.dailynovel.dailynovelapi.mbentity.MbGuestBook;
import com.dailynovel.dailynovelapi.mbentity.MbGuestBookAll;
import com.dailynovel.dailynovelapi.mbentity.MbGuestBookComment;
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
  public List<MbGuestBookAll> viewGuestBookAll() {
    List<MbGuestBookAll> list = mbRepository.findAllbyId(1);
    return list;
  }

  @Override
  public List<MbGuestBookAll> viewGuestBookAll(int id) {
    List<MbGuestBookAll> list = mbRepository.findAllbyId(id);
    return list;
  }

  // 방명록 작성 서비스
  @Override  
  public void writeGuestBook(Map<String, Object> guestbook) {
    
    // Integer memberId = Integer.parseInt((String) guestbook.get("memberId")); 
    // Integer writerId = Integer.parseInt((String) guestbook.get("writerId")); 

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
  public int writeGuestBookComment(Map<String, Object> comment) {
    Integer guestbookId = (Integer) comment.get("guestbookId");
    Integer memberId = (Integer) comment.get("memberId");
    String content = comment.get("comment").toString();

    MbGuestBookComment temp = MbGuestBookComment.builder()
        .content(content)
        .guestbookId(guestbookId)
        .memberId(memberId)
        .build();
    
    mbRepository.insertGuestbookComment(temp);

    return 1;
  }

  @Override
  public int rewriteGuestBookComment(Map<String, Object> comment) {
    Integer guestbookId = (Integer) comment.get("guestbookId");
    Integer memberId = (Integer) comment.get("memberId");
    String content = comment.get("comment").toString();

    MbGuestBookComment temp = MbGuestBookComment.builder()
        .content(content)
        .guestbookId(guestbookId)
        .memberId(memberId)
        .build();
    
    mbRepository.updateGuestbookComment(temp);

    return 1;
  }

  // 방명록 답글 삭제 서비스
  @Override
  public void deleteGuestBookComment(Map<String, Object> guestbookId) {
    int id = (int) guestbookId.get("guestbookId");
    mbRepository.deleteGuestbookComment(id);
  }

  @Override
  public MbGuestBookAll getGusetbookbyId(int id) {
    
    MbGuestBookAll guestbook = mbRepository.getGuestbookAllbyGBId(id);

    return guestbook;
  }

}
