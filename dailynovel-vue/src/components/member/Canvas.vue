<script>
import Slime from "../Canvas/item/slime.js";
import Background from "../Canvas/item/background.js";

export default {
  mounted() {
    this.dom = this.$refs.canvas;
    this.slime = new Slime(100, 100); // 수정된 부분
    this.background = new Background();
    this.ctx = this.dom.getContext("2d");
    this.run();
  },
  
  methods: {
    run() {
      this.update();
      this.draw();
      window.setTimeout(() => {
        this.run();
      }, 17);
    },
    update() {
      // update 로직
    },
    draw() {
      // draw 로직
      this.background.draw(this.ctx);
      this.slime.draw(this.ctx);
    },
    canvasClickHandler(event) {
      this.slime.move(2);
      this.slime.draw(this.ctx);
      this.x=(event.clientX);
      this.y=(event.clientY);
      console.log(this.x);
      console.log(this.y);
      if(this.x>=300)
       this.$router.push('/member/guestbook');
    
    }
  }
};
</script>

<style scoped>
.canvas {
  border: 1px solid #5a4d4d;
}
</style>

<template>
  <img src="../../assets/img/background.png" id="background" style="display: none" />
  <img src="../../assets/img/silme.png" id="slime" style="display: none" />
  <canvas
    class="canvas"
    width="1280"
    height="720"
    tabindex="0"
    ref="canvas"
    @click="canvasClickHandler($event)"
  >
  </canvas>
</template>
