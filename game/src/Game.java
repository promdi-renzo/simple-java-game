
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Random;

public class Game extends Canvas implements Runnable{


    private static final long serialVersionUID = -4322120347108108478L;

    public static final int WIDTH = 800, HEIGHT = 600;

    private Thread thread;
    private boolean running = true;


    private Handler handler;
    private Random r;


    public Game() {


        handler = new Handler();
        r = new Random();
        this.addKeyListener(new KeyInput(handler));
        new Window(WIDTH, HEIGHT, "Game", this);





        handler.addObject(new Player((WIDTH/2)-32, (HEIGHT/2)-32, ID.Player, this.handler));

        handler.addObject(new FallingBlock(r.nextInt(Game.WIDTH-51)+1, 0, ID.Enemy, this.handler));



    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        try{
            thread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void run() {

        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1){
                tick();
                delta--;
            }
            if(running){
                render();
            }
            frames++;

            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();

    }


    private void tick(){

        handler.tick();
    }

    private void render(){

        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){

            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        handler.render(g);

        g.dispose();
        bs.show();

    }


    public static int clamp(int var, int min, int max){

        if(var >= max){
            return var = max;
        }else if(var <= min){
            return var = min;
        }else{
            return var;
        }
    }

    public static void main(String[] args) {
        System.out.println("asdasd");
        new Game();

    }



}
