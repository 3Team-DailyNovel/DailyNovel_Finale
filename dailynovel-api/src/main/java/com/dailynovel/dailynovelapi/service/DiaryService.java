package com.dailynovel.dailynovelapi.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.dailynovel.dailynovelapi.entity.Diary;
import com.dailynovel.dailynovelapi.mbentity.MbDiary;
import com.dailynovel.dailynovelapi.mbentity.MbDiaryCollectionView;
import com.dailynovel.dailynovelapi.mbentity.MbDiaryDisplayed;

public interface DiaryService {
    public TreeMap<String, Integer> getByFeeling(int memberId);
    public TreeMap<String, Integer>  getByTag(int memberId);
    public TreeMap<String, Integer>  getByHonesty(int memberId);
    public List<Object> getBypino(int memberId);
    public List<Object> getByNpino(int memberId);
    public List<Object[]> getById(int memberId);

    public List<Diary> getAllByMId();
    public Map<String, List<MbDiaryCollectionView>> getListGroupingMonthly(int memberId, String feeling, String weather,
            LocalDate localDate, String order, String collection, String query);

    //모재영 : 테스트 용
    public boolean isValid(String id);

    public MbDiary readDiary(int id);
    public List<MbDiary> readListDiary();
    public int writeDiary(Diary diary);
    public int writeDiary(MbDiary diary);

    public int editDiary(MbDiary diary);

    public int removeDiary(int id);
    public List<MbDiaryDisplayed> getSharedDiary(int memberId, Integer collectionId,String sortStandard);
    public List<MbDiaryDisplayed> getListInCollection(int memberId, int collectionId);
}
 
