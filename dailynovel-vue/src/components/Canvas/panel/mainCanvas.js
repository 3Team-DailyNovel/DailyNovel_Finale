import Slime from '../item/slime.js';
import Background from '../item/background.js';


// Vue와 VueRouter를 사용하기 위해 Vue.use()를 호출합니다.

export default function MainCanvas() {
  this.dom = this.$refs.canvas;
    this.slime = new Slime(100, 100); // 수정된 부분
    this.background = new Background();
    this.ctx = this.dom.getContext("2d");
    this.dom.addEventListener("click", this.clickHandler);
  }
  
  MainCanvas.prototype = {
    run: function() {
      this.update();
      this.draw();
      window.setTimeout(()=>{this.run();},17);
    },
    update: function() {
  
    },
    draw: function() {
      this.background.draw(this.ctx);
      this.slime.draw(this.ctx);
    },
    clickHandler: function() {
      this.slime.move(2);
      this.slime.draw(this.ctx);

    }
  };