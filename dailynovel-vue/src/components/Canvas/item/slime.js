export default class Slime {
    constructor() {
      this.img = document.querySelector("#slime");
      this.x = 0;
      this.y = 0;
    }
  

    move (dir){
        switch(dir){
            case 1:
                this.y -= 10;
                break;
            case 2:
                this.x += 10;
                break;
            case 3:
                this.y += 10;
                break;
            case 4:
                this.x -= 10;
                break;
        }
    }
    
    update() {}
  
    draw(ctx) {
      ctx.drawImage(this.img, this.x, this.y, 100, 100);
    }

    
}
  