export default class Background {
    constructor() {
      this.img = document.querySelector("#background");
      this.x = 0;
      this.y = 0;
    }
  
    update() {}
  
    draw(ctx) {
      ctx.drawImage(this.img, this.x, this.y);
    }

    
}
  