<script setup>
    import Header from './Header.vue';
    import Footer from './Footer.vue';

    import Canvas from './Canvas.vue';
    import {ref} from 'vue';

//=============모달 관련 속성들===================
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
}
function animationedHandler() {
  //모달창의 애니메이션이 발생 했는지 안했는지 감지하는 이벤트
  // closeEffect = true;
  console.log(isModalOpen)
  if(closeEffect.value == false){
    isModalOpen.value = false;
  }
}

//==============모달 기본 동작 정의 =====================
</script>

<template>
    <h1>Main Page</h1>
    <Header />
    <Canvas v-on:modalOpenHandler="modalOpenHandler" />
    <Footer />

    <router-link to="/main/modal/collection" @click="modalOpenHandler"><button>컬렉션</button></router-link>
    <router-link to="/main/modal/diary" @click="modalOpenHandler"><button>다이어리</button></router-link>
    <router-link to="/main/modal/setting" @click="modalOpenHandler"><button>셋팅</button></router-link>
    <router-link to="/main/modal/Community" @click="modalOpenHandler"><button>커뮤니티</button></router-link>
    <router-link to="/main"><button>메인</button></router-link>
    

    <!-- 모달창 -->
        <div class="modal-bg"  :class="{'d-none':!isModalOpen}">
            <section class="modal-screen" :class="{'active': !closeEffect}" @animationend="animationedHandler" @click.stop>
                <!-- <Modal  @closeModal="modalCloseHandler"/> -->
                <Router-view @closeModal="modalCloseHandler"></Router-view>
            </section><!--modal FIN-->
        </div>

</template>


<style scoped>
/* 모달 화면 조정 css */
@keyframes show-effect{
     from{
       transform: scale(0%);
       /* transform: rotate(360deg); */
      }
      to{
        transform: scale(100%);
        /* transform: rotate(100000deg); */
      }  
    }

    @keyframes close-effect{
     from{
       transform: scale(100%);
    }
    to{
      transform: scale(0);
     }  
    }


.modal-bg{
  position:fixed;
  top:0;

  width:100vw;
  height: 100vh;
  display:flex;
  justify-content: center;
  align-items: center;

  background-color:  #00000080
}
.modal-bg > .modal-screen{
  background-color: #FAFFF9;

  position: fixed;
  /* max-width:1920px; */
  max-width:1280px;
  max-height: 720px;
  width:90%;
  height:80%;


  animation: show-effect 0.5s ease-in-out ;
  /* animation-fill-mode: forwards; */

  
}
.modal-screen.active{
  animation: close-effect 0.5s ease-in-out !important;
  /* animation-direction:reverse !important; */
  animation-fill-mode: forwards;
  
}


/* util */
.d-none{
  display : none;
}

.flex-v{
  display:flex;
  flex-direction: column;
}
.flex-h{
  display: flex;
  flex-direction: row;
}
.flex-center{
  display:flex;
  justify-content: center;
  align-items: center;
}

.inline-block{
  display: inline-block;
}

</style>