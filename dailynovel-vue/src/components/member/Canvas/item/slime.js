
export default class slime{
    // #vx; (private)
    speed;
    constructor(x,y){

        this.x = x || 100;
        this.y = y || 100;

        this.sw = (318/3);
        this.sh = (284/4);
        this.ix = 1;
        this.iy = 2;       
        
        this.dx = 0;
        this.dy = 0;
        this.vx = 0;
        this.vy = 0;

        this.speed = 5;

        this.walkDelay = 0;

        this.moveLeft = false;
        this.moveRight = false;
        this.moveUp = false;
        this.moveDown = false;    
             

        this.img = document.querySelector("#slime");
    }

    set speed(value){
        this.speed = value;
    }

    get speed(){
        return this.speed;
    }

    draw(ctx){

        this.sx = this.sw*this.ix;
        this.sy = this.sh*this.iy;
        ctx.drawImage(this.img,
            this.sx,
            this.sy,
            this.sw,
            this.sh,
            this.x-this.sw/2,
            this.y-this.sh/2,
            this.sw,
            this.sh
              );           
    } 

    update(){
        // 이동을 위한 코드
        if(this.moveUp)            
            this.y -= this.speed;
        if(this.moveDown)
            this.y += this.speed;    
        if(this.moveLeft)
            this.x -= this.speed;
        if(this.moveRight)
            this.x += this.speed;

        // -------------------------------------------------

        if(!(this.moveLeft || this.moveRight || this.moveUp || this.moveDown || false))
            if(this.vx==0 && this.vy==0){
                this.ix = 1;
                return;                        
            }            
        
        if (this.walkDelay > 10) {

            if (this.ix === 1) {
                this.ix = 0;
            }
            else
                this.ix = this.ix === 0 ? 2 : 0;
            this.walkDelay = 0;
        }
        this.walkDelay++;

        if ((this.dx - 0.5 <= this.x && this.x <= this.dx + 0.5) ||
            (this.dy - 0.5 <= this.y && this.y <= this.dy + 0.5)) {
            this.vx = 0;
            this.vy = 0;
        }
        
        this.x += this.vx;
        this.y += this.vy;
        
    }
    
    moveTo(dx,dy){
        
        let w = dx -this.x;
        let h = dy -this.y;
        
        let d = Math.sqrt(w*w+h*h);
        this.vx = w/d;
        this.vy = h/d;
        
        this.dx = dx;
        this.dy = dy;
        
    }
    
    move(dir){                  
        switch(dir){
            case "ArrowUp":
                this.moveUp = true;
                this.iy = 0;            
                break;
            case "ArrowDown":
                this.moveDown = true;
                this.iy = 2;
                break;
            case "ArrowRight":
                this.moveRight = true;
                this.iy = 1;
                break;
            case "ArrowLeft":
                this.moveLeft = true;
                this.iy = 3;
                break;
        }              
    }

    stop(dir){
        switch(dir){
            case "ArrowUp":
                this.moveUp = false;
                this.iy = 2;          
                break;
            case "ArrowDown":
                this.moveDown = false;
                this.iy = 2;
                break;
            case "ArrowRight":
                this.moveRight = false;
                this.iy = 2;
                break;
            case "ArrowLeft":
                this.moveLeft = false;
                this.iy = 2;
                break;
        }     

    }
    
    // moveUpdate(){
    //     if(this.cnt>1.5){
    //         if(this.ix == 1){
    //             this.ix = 0;
    //         }
    //         else 
    //             this.ix = this.ix == 0 ? 2 : 0;
            
    //         this.cnt = 0;            
    //     }

    //     this.cnt += 0.5;
    //     this.sx = this.sw*this.ix;
    //     this.sy = this.sh*this.iy;
    // }

    // kupdate(){
    //     this.sx=this.sw*this.ix;
    //     this.sy=this.sh*this.iy;
    // }

    // kreset(){
    //     this.ix = 1;
    //     this.iy = 2;
    // }


    
}

