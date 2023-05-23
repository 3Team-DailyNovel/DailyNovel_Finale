<script setup>
import quill from './quill.vue';
import quill2 from './quill2.vue';
import quillCopy from './quill copy.vue';

import { ref,onMounted, onUpdated,  defineProps , defineEmits} from 'vue';
import MapBox from './MapBox.vue';

// "기분","화남","불편","평온","실망","불안","행복","슬픔","감동","신남"
// "자유"

const props = defineProps({
    'isAdd' : '',
    'loadDiaryId' : '',
    'newestDiaryId' : ''
});

const emit = defineEmits(
    ["DoneAddDiary"]);

const defineRef = ref({
  'loading' : '',
  'isInit' : '',
  // 'isShared' : 'false'
});

const isSharedref = ref();
isSharedref.value = false


// defineRef.value.isInit = false;

const initHander = function (firstValue){
    if(firstValue != null) {
      // console.log(defineRef.value.isInit);
      defineRef.value.isInit = false;
    }
};


let weatherDone = false;
let ControllerAdd = props.isAdd;
let myLocation = { lat: null, lng: null };

onMounted(() => {
  console.log(props.newestDiaryId);
  geoFindMe();
});


// loadDiary() 무한 반복 막기위한 선언
let previousLoad = -1;
let newest = false;

//id 값 비동기 처리를 위한 promise 선언
function newestPromise(diaryId){
  return new Promise(function (resolve){
    resolve(diaryId);
  })
}
let isOn = false;
onUpdated(() => {
  initHander(props.newestDiaryId);
  //최초 데이터 받을 때
  if(!newest){
    // defineRef.value.isInit = false; //배경화면 끄기
    // defineRef.value.loading = true; //로딩창 소환
    let promise = newestPromise(props.newestDiaryId);
    promise
    .then( result => loadDiary(result))
    .then( sucess => {
      // defineRef.value.isInit = true;
      // defineRef.value.loading = false;
      newest = sucess;
    });
    // newest = true;
  }

  ControllerAdd = props.isAdd
  // console.log(ControllerAdd);
    //update 이중 방지용
  // console.log(defineRef.value.loading , defineRef.value.isInit);
  if( ControllerAdd == true && weatherDone == false
    && isOn == false){
    let promise = geoFindMe();
    isOn = true;

    defineRef.value.loading = true; //로딩창 소환
    defineRef.value.isInit = true; //배경화면 끄기

    promise
    .then(response => weather(response[0],response[1]))
    .then(result => {

      // console.log(defineRef.value.loading , defineRef.value.isInit);
      addDiary(result)
    })
    .then(success => {
      defineRef.value.loading = false;
      defineRef.value.isInit = false;
      // console.log(defineRef.value.loading , defineRef.value.isInit);
      console.log(success)
    });
  }
  // console.log("load: "+defineRef.value.loading ,"init: "+ defineRef.value.isInit);

  // console.log(previousLoad, props.loadDiaryId);
  if(previousLoad != props.loadDiaryId){
    loadDiary(props.loadDiaryId);
    previousLoad = props.loadDiaryId
  }

})

function getDate(gotdate){

  //프로토 타입
  Date.prototype.amPm = function() {
      let h = this.getHours() < 12 ? "오전" : "오후";
      return h;
  }
  Date.prototype.getHoursAmPm = function() {
      let h = this.getHours() < 12 ? this.getHours() : this.getHours() - 12;
      return h;
  }

  // console.log(gotdate);
  // let date = new Date();
  let date = gotdate;
  // console.log(date.getFullYear());
  titleDate = `${date.getMonth()+1}월 ${date.getDate()}일`;
  return `${date.getFullYear()}년 ${date.getMonth()+1}월 ${date.getDate()}일 ${date.amPm()} ${date.getHoursAmPm()}시 ${date.getMinutes()}분`;
}

  let titleDate = "";
  const mapToggle = ref(false);
  const imageToggle = ref(false);


//현재시간 받아오기
//   const time = getDate(new Date);


//날씨관련 객체
  const weatherData = ref("");

  weatherData.value = {
      'city' : '평양',
      'weatherCode': '0',
      'weatherDes': '날씨'
  };


 //현재 위치 받아오기 API

// 지도 설정한 좌표값 얻어오기
function coor(coor) {

  console.log(coor)
}

 function geoFindMe() {

  return new Promise(function (resolve){
    function success(position) {
        const latitude = position.coords.latitude;
        const longitude = position.coords.longitude;
        //날씨 호출
        // weather(latitude, longitude);
        resolve([latitude,longitude]);
        myLocate(latitude,longitude)
    }
    function error() {
      console.log("Unable to retrieve your location");
    }

    if (!navigator.geolocation) {
      console.log("Geolocation is not supported by your browser");
    } else {
      console.log("Locating…");
      navigator.geolocation.getCurrentPosition(success, error);
    }
  });
  }
  function myLocate(latitude,longitude){
      myLocation.lat = latitude;
      myLocation.lng = longitude;

      return { lat: myLocation.lat, lng: myLocation.lng };

    }
  function weather(latitude, longitude){

    return new Promise(function (resolve){

    let requestOptions = {
      method: 'GET',
      redirect: 'follow'
      };

      fetch(`http://api.openweathermap.org/data/2.5/forecast?lat=${latitude}&lon=${longitude}&exclude=current&appid=fdbbc96af009ea8344414a270ad5386e&units=metric&lang=kr`, requestOptions)
      .then(response => response.json())
      .then(result => {
          weatherData.value.city = result.city.name;
          weatherData.value.weatherCode = result.list[1].weather[0].id;

          // console.log( result.list[1].weather[0].id);
          weatherDone = true;
          return result.list[1].weather[0].id;
          // weatherCodeToDes(result.list[1].weather[0].id);
      })

        //날씨 코드에서 명칭으로 변경 7가지 경우의 수
      .then(code =>{
        let weatherCodeConvert = parseInt(code /= 100);

        if(weatherCodeConvert == 2)
          weatherData.value.weatherDes = "천둥번개";
        else if(weatherCodeConvert == 3)
          weatherData.value.weatherDes = "천둥번개";
        else if(weatherCodeConvert == 5)
        weatherData.value.weatherDes = "비";
        else if(weatherCodeConvert == 6)
        weatherData.value.weatherDes = "눈";
        else if(weatherCodeConvert == 7)
        weatherData.value.weatherDes = "미세먼지";
        else if(weatherCodeConvert == 8){
          if(code / 800 == 1)
            weatherData.value.weatherDes = "맑음";
          else
            weatherData.value.weatherDes = "흐림";
        }
        resolve(weatherData.value.weatherDes);
        return true;
      })
      .catch(error => console.log('error', error));
    })
  };


const diaryRef = ref("");

let diaryObj = {
  "id": null ,
  "member_id": null,
  "title": null,
  "content": "",
  "weather": null,
  "feeling": "",
  "honesty": "",
  "tag": "",
  "regDate": getDate(new Date),
  "lat": 38,
  "lng": 128
};
// console.log(diaryObj);
diaryRef.value = diaryObj;
// console.log(diaryRef.value);


//값 기본 입력해주기
function objRef
(Did, Mid, DTitle ,DContent,
 DWeather,DFeeling,DHonesy, DTag,
 DDate , DLat, Dlng){

  diaryRef.value.id = Did;
  diaryRef.value.member_id = Mid;
  diaryRef.value.title = titleDate+'의 일기';
  diaryRef.value.content = DContent;

  diaryRef.value.weather = weatherData.value.weatherDes;
  diaryRef.value.feeling = DFeeling;
  diaryRef.value.honesy = DHonesy;
  diaryRef.value.tag = DTag;

  diaryRef.value.regDate = DDate;
  diaryRef.value.lat = DLat;
  diaryRef.value.lng = Dlng;

}

console.log(diaryObj);

const addDiary = function(isAdd){
  return new Promise(function(resolve, reject){

  // if(isAdd == true){
    let myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");


    //ref 기본값 담기
    objRef(null,1,null,"당신마음입력"
    ,null,"기분",100,"태그"
    ,null,38,128);

    // console.log(diaryRef.value);
    diaryObj = diaryRef.value;
    // diaryObj.regDate = null;
    // console.log(diaryObj);
    let raw = JSON.stringify(diaryObj);

    let requestOptions = {
      method: 'POST',
      headers: myHeaders,
      body: raw,
      redirect: 'follow'
    };

    fetch("http://localhost:8080/diary", requestOptions)
      .then(response => response.text())
      .then(result => {
        emit("DoneAddDiary", true)
        diaryRef.value.regDate = getDate(new Date);
        // console.log(diaryObj.regDate);
        // console.log(diaryRef.value.regDate);
      })
      .catch(error => console.log('error', error));
    console.log("완료");
  // }
    resolve("success");
})};

const loadDiary = function(diaryId){
  return new Promise(function(resolve){

  let requestOptions = {
    method: 'GET',
    redirect: 'follow'
  };

  fetch(`http://localhost:8080/diary/${diaryId}`, requestOptions)
    .then(response => response.json())
    .then(result => {
      diaryRef.value = result;
      diaryRef.value.regDate = getDate(new Date(result.regDate));
      resolve(true);
    })
    .catch(error => {
      console.log('error', error);
      loadDiary(diaryId);
    });
  })
}

const EditDiary = function(diaryId){
  return new Promise(function(resolve, reject){

    let myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    //ref 기본값 담기
    objRef(diaryId,1,null,"당신마음입력"
    ,null,"기분",100,"태그"
    ,null,38,128);

    diaryObj = diaryRef.value;
    let raw = JSON.stringify(diaryObj);

    let requestOptions = {
      method: 'PUT',
      headers: myHeaders,
      body: raw,
      redirect: 'follow'
    };

    fetch("http://localhost:8080/diary", requestOptions)
      .then(response => response.text())
      .then(result => console.log(result))
      .catch(error => console.log('error', error));




    resolve();
  })

}


const toggleClickHandler = (e) =>{
  isSharedref.value = (isSharedref.value == true) ? (false) : (true);
  // console.log(isSharedref.value);
};

// let previousValueFeeling = diaryRef.value.feeling;
// let previousValueTag = diaryRef.value.tag;
const feelingDropdownHandler = (e) =>{

  console.log(e.target);
  console.log(e.target.className == "feel");

  if(e.target.className == "feel")
    diaryRef.value.feeling = e.target.innerText;
  else if(e.target.className == "tag")
    diaryRef.value.tag = e.target.innerText;

};


</script>

<template>

  <div
      v-if="defineRef.loading"
      class="dot-overtaking"></div>

  <div
      v-if="!defineRef.isInit"
      class="editor-wrap"
      @click="onclickedHandler"
      >


    <div class="editor-attribue-box">
      <!-- <div class="editor-data">2023년 4월 10일 오후 02시 01분 </div> -->
      <div class="editor-data">{{diaryRef.regDate}}</div>
      <div class="editor-attribue">{{diaryRef.weather}}</div>
      <!-- <div class="editor-attribue">{{'#'+diaryRef.tag}}</div> -->
      <div class="editor-attribue dropdown">
        <button class="dropbtn">{{'#'+diaryRef.tag}}</button>
        <div 
          :checkTag ="diaryTag"
          @click="feelingDropdownHandler"          
          class="dropdown-content">

          <div class="tag" href="#">자유</div>
          <div class="tag" href="#">여행</div>
          <div class="tag" href="#">영화</div>
        </div>
      </div>


      <!-- <div class="editor-attribue">{{diaryRef.feeling}}</div> -->
      <div class="editor-attribue dropdown">
        <button class="dropbtn">{{diaryRef.feeling}}</button>
        <div 
          :checkTag ="diaryFeel"
          @click="feelingDropdownHandler"          
          class="dropdown-content">

          <div class="feel" href="#">화남</div>
          <div class="feel" href="#">불편</div>
          <div class="feel" href="#">평온</div>
          <div class="feel" href="#">실망</div>
          <div class="feel" href="#">불안</div>
          <div class="feel" href="#">행복</div>
          <div class="feel" href="#">슬픔</div>
          <div class="feel" href="#">감동</div>
          <div class="feel" href="#">신남</div>
        </div>
      </div>
      
      <div class="add-btn">-</div>
    </div>

    <div class="editor-title">
        <header contenteditable="true"
                class="editor-title-content editor-header"
                @click="editHandler"
                >
          <!-- {{titleDate+'의 일기'}} -->
          {{ diaryRef.title }}
        </header>
        <div
            v-if="isSharedref"
            class="editor-share">공유 중</div>
        <div
            v-if="!isSharedref"
            class="editor-share .active">공유 여부</div>

        <input type="checkbox" id="toggle" class="toggle2" hidden>
        <label 
          @click="toggleClickHandler"
          for="toggle" 
          class="toggleSwitch">
          
          <span class="toggleButton"></span>
        </label>

    </div>

    <hr class="editor-title-hr">

    <div class="editor-main-quill">
      <main
      class="editor-main"
      @click="editHandler"
      >
        <!-- <quill2/> -->
        <quill
          class="editor-quill"
          :content = "diaryRef.content"
        />
          <!-- <quillCopy/> -->

      </main>

      <div
        class="editor-sub-button editor-sub">

        <!-- @click.prevent="mapToggleHandler"> -->
        <button
          @click="imageToggle = !imageToggle"
          >
          사진추가
        </button>
      <button
          @click="mapToggle = !mapToggle"
          >
          지도추가
      </button>

      </div>

      <div class="img-map-container">
        <div
          v-if="imageToggle"
          class="editor-image editor-sub">
          이미지를 넣어넣어 놀자놀자
        </div>


        <!-- 맵 삽입칸 -->
        <div
          v-if="mapToggle"
          class="mapToggle-map editor-sub">
          <MapBox :myLocation="myLocation" @coor="coor"/>
        </div>

      </div>

    </div>
  </div>


</template>

<style scoped>
.editor-wrap{
  width: 70%;
  height: 95%;
  
  /* overflow: scroll; */
  /* overflow: scroll; */
  margin: 15px 40px;
}


.editor-main-quill{
    height: 80%;
    overflow-y: scroll;

    display: grid;
    grid-template-columns: 1fr 1fr;
    grid-template-rows: 40px 1fr 1fr;
}

.editor-main{

  box-sizing: border-box;

  width: 100%;
  height: 95%;
  /* overflow-y: scroll; */

  /* height: auto; */

  /* display: grid; */
  grid-row: 1/4; 
  grid-column: 1/2;

  margin: 10px 10px 0 0;
}

.editor-sub{
  margin: 10px 0 0 10px;
  border: 1px solid #E3E6E2;
  border-radius: 5px;
}

.editor-sub-button{
  height: 40px;

  grid-row: 1/2;
  grid-column: 2/3;

  display: flex;
  justify-content: start;
  align-items: center;

  border: none;
}

  .editor-sub-button button{
    /* border: 1px solid black; */
    border-radius: 5px;
    /* padding: 5px 20px; */
    background-color: #F9F4F4;
    font-size: 17px;

    width: 100px;
    height: 30px;

    margin-left: 10px;
    
  }

.editor-image{
  /* grid-row: 2/3; */
  grid-column: 2/3;

  /* margin-top: 10px; */
  height: 200px;
}
.editor-map{
  /* grid-row: 3 / 4; */
  grid-column: 2/3;

  /* margin-top: 10px; */

  height: 200px;
}


/* placeholader 관련 설정 */

/* .editor.empty:before {
  content: attr(placeholder);
  margin-top: 10px;
  color: #9A9A97;
  font-size: 20px;
} */

/* p#editor::placeholder{
    color: #9A9A97;
    font-size: 15px;
    padding: 0px;
} */



.editor-attribue-box{
    color: #636363;
    font-size: 15px;

    display: flex;
    align-items: center;
}

 .editor-attribue{
    background-color: #F9F4F4;
    color: #3F3F3F;
    margin-left: 20px;
    padding: 6px 16px;
    border-radius: 5px;
 } 




  .editor-template{
    background-color: #F9F4F4;
    
    color: #636363;
    /* font-size: 20px; */
    /* font-weight: bold; */

    display:flex;
    justify-content: center;
    align-items: center;

    margin-left: 30px;
    /* padding: 3px 20px; */
    border-radius: 5px;

    width: 90px;
    height: 30px;

    /* flex-grow: 1; */
    /* grid-row: 1 / 2; */
    /* justify-self: start; */
  }

.editor-title{

    margin-top: 45px;
    color: #252728;
    font-weight: bold;
    display: flex;
    /* grid-template-columns: 1fr 0.5fr 0.5fr; */
}

  .editor-title-content{
        /* flex-grow: 1; */
        
    font-size: 32px;
    flex-grow: 1000;
      /* line-height: 1.2; */
      /* font-size: 32px; */

      /* grid-row: 1 / 2; */
    }
    .editor-title-content:focus{
        outline: none;
    }


  .editor-share{
      /* box-sizing: border-box; */
      /* width: 60px;
      height: 24px; */

      /* background-color: #191F78;
      
      padding: 4.5px 13px;
      border: 1px solid #191F78;
      border-radius: 2px;

      color: #FCFCFC;
      font-size : 12px;
      text-align: center; */

      justify-self: center;
      align-self: center;
      width:60px;
      height:24px;

      display:flex;
      justify-content: center;
      align-items: center;
      color:#fcfcfc;
      font-size:12px;
      border-radius: 5px;
      background-color: #191F78;

      flex-grow: 1;


      /* grid-row: 1 / 2; */
  }

    .editor-share.active{
      background-color: transparent;
        border: 1px solid #8391FC;
        color: #8391FC;
    }


.editor-title-hr{

  background-color: black;
  height: 1px;
}


.img-map-container{
  overflow-y: scroll;
}


/**
 * ==============================================
 * Experimental: Gooey Effect
 * Dot Overtaking
 * ==============================================
 */
.dot-overtaking {
  position: absolute;
  top : 50%;
  left : 50%;

  width: 12px;
  height: 12px;
  border-radius: 6px;
  background-color: transparent;
  color: hsl(0deg, 100%, 0%);
  margin: -1px 0;
  box-shadow: 0 -20px 0 0;
  filter: blur(2px);
  animation: dot-overtaking 2s infinite cubic-bezier(0.2, 0.6, 0.8, 0.2);
}
.dot-overtaking::before, .dot-overtaking::after {
  content: "";
  display: inline-block;
  position: absolute;
  top: 0;
  left: 0;
  width: 12px;
  height: 12px;
  border-radius: 6px;
  background-color: transparent;
  color: hsl(0deg, 100%, 0%);
  box-shadow: 0 -20px 0 0;
  filter: blur(2px);
}
.dot-overtaking::before {
  animation: dot-overtaking 2s infinite cubic-bezier(0.2, 0.6, 0.8, 0.2);
  animation-delay: 0.3s;
}
.dot-overtaking::after {
  animation: dot-overtaking 1.5s infinite cubic-bezier(0.2, 0.6, 0.8, 0.2);
  animation-delay: 0.6s;
}

@keyframes dot-overtaking {
  0% {
    transform: rotateZ(0deg);
  }
  100% {
    transform: rotateZ(360deg);
  }
}


/*
  toggle
*/

.toggleSwitch {
  width: 60px;
  height: 30px;
  display: block;
  position: relative;
  border-radius: 30px;
  background-color: #fff;
  box-shadow: 0 0 16px 3px rgba(0 0 0 / 15%);
  cursor: pointer;
  margin-left: 30px;
}

.toggleSwitch .toggleButton {
  width: 24px;
  height: 24px;
  position: absolute;
  top: 50%;
  left: 4px;
  transform: translateY(-50%);
  border-radius: 50%;
  background: #191F78;
}

#toggle:checked ~ .toggleSwitch {
  background: #191F78;
}

#toggle:checked ~ .toggleSwitch .toggleButton {
  left: calc(100% - 26.4px);
  background: #fff;
}

.toggleSwitch, .toggleButton {
  transition: all 0.2s ease-in;
}

.add-btn{
    font-size: 40px;
    font-weight: bold; 
    /* position:fixed; */
    margin-left: auto;
    margin-right: 10px;

    /* border: 3px solid; */
    /* border-radius: 50%; */
    
    /* right: 3%; */
    -webkit-user-select:none;
    -moz-user-select:none;
    -ms-user-select:none;
    user-select:none;  
    /* top:50%; */
    /* transform: translateY(-50%); */

}
.add-btn:hover{
    cursor:pointer;
}


/* 드롭다운 관련 */
.dropbtn {
  /* background-color: #ea2129;
  color: white;
  padding: 16px;
  font-size: 16px;
  border: none; */

  border: none;
  padding: 0px;
  background-color: #F9F4F4;
  

}

.dropdown {
  position: relative;
  display: inline-block;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: #f1f1f1;
  min-width: 130px;
  height: 250px;
  box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
  z-index: 1;

  left: -3%;

  overflow-y: scroll;
}

.dropdown-content {

}

.feel {
  color: black;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
}

.dropdown-content .feel:hover {
  background-color: #ddd;
}

.tag {
  color: black;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
}

.dropdown-content .tag:hover {
  background-color: #ddd;
}

.dropdown:hover .dropdown-content {
  display: block;
}
/* .dropdown-content div :focus {display: none;} */

.dropdown:hover .dropbtn{
  /* background-color: #3e8e41; */
}



</style>