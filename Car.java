
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class Car implements Runnable{

    private int carId;
    private int direction;
    private final int period = 5000;
    private Bridge bridge;
    CyclicBarrier cb;
    Car(int c, int d,CyclicBarrier cb, Bridge b){
        this.carId = c;
        this.direction = d;
        this.bridge = new Bridge();
        this.cb = cb;
        bridge = b;
    }

    void addBridge(int waitingOne, int waitingZero, int active){
      bridge.bridge_init(waitingOne, waitingZero, active);
    }

    @Override
    public void run() {    
        try{
            
            System.out.println("Thread of car "+this.carId+" started\n");
            // calling await so the current thread suspends   
            cb.await();   
            if(bridge.arriveToBridge(this))
            {
                bridge.crossBridge(this);
                bridge.exitBridge(this);
            }
            //Thread.sleep(2000);
        } catch (InterruptedException e) {
        System.out.println(e);
        } catch (BrokenBarrierException e) {
        System.out.println(e);
        }
    }

    public void setCarDirection(int c){
        this.direction = c;
    }

    public void setCarId(int c){
        this.carId = c;
    }

    public int getCarDirection(){
        return this.direction;
    }
    
    public int getCarDId(){
        return this.carId;
    }

}
