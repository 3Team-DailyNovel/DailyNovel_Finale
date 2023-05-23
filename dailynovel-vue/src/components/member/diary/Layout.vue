<script setup>
import Header from './Header.vue'
import List from './List.vue'
import Editor from './editor.vue'
import { reactive, onBeforeMount, ref, watchEffect } from 'vue';
import { useUserDetailsStore } from "../../store/useUserDetailsStore.js";
import Filter from './filter.js'
import { useRoute } from 'vue-router'
let userDetails = useUserDetailsStore(); //피impo니아를 사용하는 방법
//router 정보
const route = useRoute();
//메뉴 관리
const diaryFilter = reactive({
        feeling:new Filter(0,["기분","화남","불편","평온","실망","불안","행복","슬픔","감동","신남"]),
        weather:new Filter(0,["날씨","천둥번개", "이슬비",  "비",  "눈", "미세먼지", "맑음", "흐림"]),
        date:null,
        sort:new Filter(0,["정렬기준","최신순","오래된순"]),
        collection:new Filter(0,["컬렉션","나만의 일기","배부른 일기","더망"]),
        keyword: ""
})
const diary = reactive({
    list : null
})


onBeforeMount (()=> {
    getCollectionList();
    getListwithFiltering();
})
// 필터 목록 클릭시 동작 이벤트핸들러
function filterClickHandler(selected){
    // console.log(selected);
    let backupMenu = {
        name:null,
        value : null
    }
    switch(selected.menuname){
        case "feeling":
            backupMenu.name = "feeling";
            backupMenu.value=diaryFilter.feeling.idx;
            diaryFilter.feeling.idx = selected.menuvalue;
            break;
        case "weather":
            backupMenu.name = "weather";
            backupMenu.value=diaryFilter.weather.idx;
            diaryFilter.weather.idx = selected.menuvalue;
            break;
        case "date":
            backupMenu.name = "date";
            backupMenu.value=diaryFilter.date;
            diaryFilter.date = selected.menuvalue;
            break;
        case "sort":
            backupMenu.name = "sort";
            backupMenu.value=diaryFilter.sort.idx;
            diaryFilter.sort.idx = selected.menuvalue;
            break;
        case "collection":
            backupMenu.name = "collection";
            backupMenu.value=diaryFilter.collection.idx;
            diaryFilter.collection.idx = selected.menuvalue;
            break;
        case "keyword":
            backupMenu.name = "keyword";
            backupMenu.value=diaryFilter.keyword;
            diaryFilter.keyword = selected.menuvalue;
            break;
        }


        // 정렬 값의 변화가 있기 때문에 리스트를 다시 불러야함
        getListwithFiltering(backupMenu)
}

// 필터 목록 클릭시 Diary List 불러오는 함수
function getListwithFiltering(backup) {
    //backup ==> 만약 리스트가 없다면 이전 필터상태를 유지시킨다.
    // 필터링 리스트에 따라 쿼리 뽑기
    const feeling = diaryFilter.feeling;
    const weather = diaryFilter.weather;
    const date = diaryFilter.date;
    const sort = diaryFilter.sort;
    const collection = diaryFilter.collection;
    const keyword = diaryFilter.keyword;

    // 필터링을 위한 쿼리 생성구문
    let query = "?";
    // 유저 정보 (필수)
    query += `memberId=${userDetails.id}`;
    if(feeling.idx != 0){
        query += `&feeling=${feeling.menu[feeling.idx]}`;
    }
    if(weather.idx != 0){
        query += `&weather=${weather.menu[weather.idx]}`;
    }
    if(date!= null){
        let offset = date.getTimezoneOffset() * 60000; 
        let dateOffset = new Date(date.getTime() - offset);
        query += `&date=${dateOffset.toISOString()}`;
    }
    if(sort.idx != 0){
        query += `&order=${sort.menu[sort.idx]}`;
    }
    if(keyword != ""){
        query += `&query=${keyword}`;
    }
    if(collection != "" && collection.idx !=0){
        query += `&collection=${collection.id[collection.idx]}`;
    }

    // 뽑힌 쿼리에 따라 Fetch 실행
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(`http://localhost:8080/diary/filter${query}`, requestOptions)
        .then(response => response.json())
        .then(result => { 
            //받아온 데이터가 아무것도 없다면
            
            console.log();
            
            if(Object.keys(result).length == 0){
                if(!query.includes("&")){
                    // 유저의 다이어리가 아무것도 없을 때
                    return
                }
                alert("일기가 없습니다.");
                
                if(backup && backup.name != null){
                    switch(backup.name){
                    case "feeling":
                        diaryFilter.feeling.idx = backup.value;
                        break;
                    case "weather":
                        diaryFilter.weather.idx = backup.value;
                        break;
                    case "date":
                        console.log(backup)
                        console.log("데이트터짐" + backup.value)
                        diaryFilter.date = backup.value;
                        console.log("최종적으로 데이트 바뀐거 확인" + diaryFilter.date)
                        break;
                    case "sort":
                        diaryFilter.sort.idx = backup.value;
                        break;
                    case "collection":
                        diaryFilter.collection.idx = backup.value;
                        break;
                    case "keyword":
                        diaryFilter.keyword = "";
                        break;
                    }
                    return;
                }
            }
            //받아온 데이터가 있다면
            //diary reactive 객체의 list 속성에 넣고
            diary.list=result
            // console.log(diary.list["2021년 5월"][0].diaryId);

            // 모든 데이터의 Date형식을 Javascript의 Date 객체로 변경
            for(let key in diary.list){
                for(let value in diary.list[key]){
                    // 공유 만료일이 적힌 데이터라면
                    if(diary.list[key][value].sharedEndDate)
                        diary.list[key][value].sharedEndDate = new Date(diary.list[key][value].sharedEndDate);
                    // 일기 생성일자
                    diary.list[key][value].regDate = new Date(diary.list[key][value].regDate);
                }
            }
            console.log(result);
            console.log(diary.list)
            console.log(diary.list[(Object.keys(diary.list)[0])][0].diaryId)
            newestDiaryId.value = diary.list[(Object.keys(diary.list)[0])][0].diaryId;
        })
        .catch(error => console.log('error', error));
    }
    // 리스트 재요청시 가장 최신 일기 꺼내기(코드 개판이니 조심하시기 바랍니다. (165번째줄에서 넣어주는중)
    let newestDiaryId = ref()
// 컬렉션 데이터 불러와서 데이터 삽입
function getCollectionList() {
    let requestOptions = {
    method: 'GET',
    redirect: 'follow'
    };

    fetch(`http://localhost:8080/collection?memberId=${userDetails.id}`, requestOptions)
    .then(response => response.json())
    .then(collections => {
        //배열 생성
        let tmpnameList = [];
        let tmpIdList = [];
        tmpnameList.push("컬렉션")
        tmpIdList.push(null)
        for(let index in collections){
            let col=collections[index];
            tmpnameList.push(collections[index].name);
            tmpIdList.push(collections[index].id)
            // tmpIdList
        }
        diaryFilter.collection.menu = tmpnameList;
        diaryFilter.collection.id = tmpIdList;
    })
    .catch(error => console.log('error', error));
}

// 현재 클릭된 다이어리의 DiaryId값(DB에서 Diary Table의 Id 값)
let curDiaryId = ref(null);
function diaryClickHandler(diaryId){
    curDiaryId.value= diaryId
    // console.log("Layout에서 다이어리 클릭을 알립니다!")
    console.log(`이건 DiaryId ${curDiaryId.value}`)
}
// 다이어리 추가 버튼을 눌렀는지 감지
let isClickDiaryAdd= ref(false);
function diaryAddbtnClickHandler(){
    isClickDiaryAdd.value=true;
    console.log(`다이어리 만들라고? ${isClickDiaryAdd.value}`)
}
function getNewestDiary(){
    console.log(diary)
}
getNewestDiary();

//모재영 추가 완료용 emit 담는 곳
let listReload = 0;
function diaryAddHander(){
  listReload ++;
}

</script>
<template>
    <div class="diary-container">
        <Header class="no-scroll"  :filter = "diaryFilter"  @filterClickedHandler="filterClickHandler"/>
        
        <section class="diary-main">
            <List :diary = "diary" @diaryClickinList="diaryClickHandler" @addBtnClick = "diaryAddbtnClickHandler"/>

            <Editor
                :newestDiaryId = "newestDiaryId"
                :isAdd = "isClickDiaryAdd"
                :loadDiaryId = "curDiaryId"
                @DoneAddDiary = "diaryAddHander"
            />
            <!-- :current ="diary.value"  -->
        </section>
    </div>
</template>

<style scpoed>
/* 페이지 컨테이너 */
.diary-container{
    display:flex;
    flex-direction: column;
}
/* 다이어리 컨텐츠 부분 레이아웃 잡기*/
.diary-main{
display:flex;
flex-grow: 1;
}

.no-scroll {
    -webkit-user-select:none;
    -moz-user-select:none;
    -ms-user-select:none;
    user-select:none;
}
</style>