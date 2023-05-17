package com.dailynovel.dailynovelapi.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.dailynovel.dailynovelapi.entity.Diary;

public interface DiaryService {
    public TreeMap<String, Integer> getByFeeling();
    public TreeMap<String, Integer>  getByTag();
    public TreeMap<String, Integer>  getByHonesty();
    public Map<String, List<MbDiaryCollectionView>> getListGroupingMonthly(int memberId, String feeling, String weather,
            LocalDate localDate, String order, String collection, String query);

    //모재영 : 테스트 용
    public boolean isValid(String id);

    public int writeDiary(Diary diary);
}
 
