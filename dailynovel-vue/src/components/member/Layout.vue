<script setup>

import Header from './Header.vue';

import Canvas from './Canvas.vue';

import ModalHeader from './ModalHeader.vue'
import { useUserDetailsStore } from '../store/useUserDetailsStore.js';
import Footer from './Footer.vue';
import { ref  } from 'vue';
let userDetails = useUserDetailsStore(); //피impo니아를 사용하는 방법

//=============모달 관련 속성들===================
const canvasRef = ref(null);
let closeEffect = ref(false); //애니메이션 동작 유무
let isModalOpen = ref(false); //모달창 제어

function modalOpenHandler() {
  //모달 창이 뜰지 말지 결정하는 이벤트
  closeEffect.value = true; //모달창 키기
  isModalOpen.value = true; //올라오기
}

function modalCloseHandler() {
  //모달 창이 뜰지 말지 결정하는 이벤트
  console.log("click!")
  isModalOpen.value = true;  //모달창 키기(상태유지)
  closeEffect.value = false; //내려오기 (애니메이션 작동)

  
  const canvasElement = canvasRef.value;
    if (canvasElement instanceof HTMLElement) {
      canvasElement.focus();
    }
}
function animationedHandler() {
  //모달창의 애니메이션이 발생 했는지 안했는지 감지하는 이벤트
  // closeEffect = true;
  // console.log(isModalOpen)
  if (closeEffect.value == false) {
    isModalOpen.value = false;
    
  }
}

//==============모달 기본 동작 정의 =====================
</script>

<template>
  <Header />
  <section class="member-room-canvas ">
    <Canvas v-on:modalOpenHandler="modalOpenHandler" tabindex="0" ref="canvasRef" />
  </section>
  <!-- <section class="member-room-temp-linkes">
    <router-link to="/member/room/collection/main" @click="modalOpenHandler"><button>컬렉션</button></router-link>
    <router-link to="/member/room/diary" @click="modalOpenHandler"><button>셋팅</button></router-link>
    <router-link to="/member/room/achievement" @click="modalOpenHandler"><button>업적</button></router-link>
    <router-link to="/member/room/community" @click="modalOpenHandler"><button>커뮤니티</button></router-link>
    <router-link to="/member/room/guestbook/list" @click="modalOpenHandler"><button>방명록</button></router-link>
    <router-link to="/member/room/profile" @click="modalOpenHandler"><button>내 정보</button></router-link>
    <router-link :to="'/member/room/follow/'+userDetails.id" @click="modalOpenHandler"><button>구독페이지</button></router-link>
    <router-link to="/member/room"><button>메인</button></router-link>
  </section> -->
  <Footer />


  
  <!-- 모달창 -->
  <div class="modal-bg" :class="{ 'd-none': !isModalOpen }">
    <section class="modal-screen" :class="{ 'active': !closeEffect }" @animationend="animationedHandler" @click.stop>

      <div class="modal-container">
        <ModalHeader @modalCloseEventHandler="modalCloseHandler" class="header" />
        <Router-view class="content" />
      </div>
    </section><!--modal FIN-->
  </div>
</template>


<style scoped>

/* 모달 화면 조정 css */
@keyframes show-effect {
  from {
    transform: scale(0%);
    /* transform: rotate(360deg); */
  }

  to {
    transform: scale(100%);
    /* transform: rotate(100000deg); */
  }
}

@keyframes close-effect {
  from {
    transform: scale(100%);
  }

  to {
    transform: scale(0);
  }
}

.member-room-canvas {
  width: 1920px;
  display: flex;
  justify-content: center;

  margin-top: 3rem;
  margin-bottom: 3rem;

  
}

.member-room-temp-linkes {
  width: 1920px;
  display: flex;
  justify-content: center;
}

.modal-bg {
  position: fixed;
  top: 0;

  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;

  background-color: #00000080
}

.modal-bg>.modal-screen {
  background-color: #FAFFF9;

  position: fixed;
  max-width: 1920px;
  max-width: 1280px;
  max-height: 720px;
  width: 90%;
  height: 80%;

  border-radius: 8px;
  overflow: hidden;
  animation: show-effect 0.5s ease-in-out;
  /* animation-fill-mode: forwards; */
}

.modal-screen.active {
  animation: close-effect 0.5s ease-in-out !important;
  /* animation-direction:reverse !important; */
  animation-fill-mode: forwards;

}

.modal-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;



}

.modal-container .header {
  width: 100%;
  height: 72px;
}

.modal-container .content {
  width: 100%;
  flex-grow: 1;
}

.modal-container .content {
  width: 100%;
  height: 648px;
  flex-grow: 1;
}

.modal.active {
  animation: close-effect 0.5s ease-in-out !important;
  /* animation-direction:reverse !important; */
  animation-fill-mode: forwards;
}

/*============================================== */
/* style */
</style>

