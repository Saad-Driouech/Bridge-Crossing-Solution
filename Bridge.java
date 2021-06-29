import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Bridge{
    public Semaphore directionOne;
    public Semaphore directionZero;
    public Semaphore act;
    public ArrayList<Semaphore> directions = new ArrayList<Semaphore>();
    public Semaphore inBridge;
    public int[] waitingCars = {3,2,4};
    public int activeCars;
    public Bridge(){
        this.directionOne = new Semaphore(1,true);
        this.directionZero = new Semaphore(1,true);
        this.inBridge = new Semaphore(3,true);
        this.act = new Semaphore(1,true);
        directions.add(directionZero);
        directions.add(directionOne);
    }

    void setParameters(int w0, int w1,int a){
        this.waitingCars[0] = w0;
        this.waitingCars[1] = w1;
        this.activeCars = a;
    }

    void bridge_init(int waitingOne, int waitingZero, int active){
        waitingCars[0] = waitingZero;
        waitingCars[1] = waitingOne;
        this.activeCars = active;
    }

    boolean hasAccess(Car c){
        if(waitingCars[0] + waitingCars[1] == 0)
            return true;
        else if(activeCars <= 3 && directions.get(c.getCarDirection()).availablePermits() == 1)
            return true;
        else 
            return false;

    }
    boolean sameDirection(int d)
    {
        if(directions.get(d).availablePermits() == 1)
            return true;
        else 
            return false;
    }
    boolean canCross()
    {
        if(inBridge.availablePermits() > 0 )
            return true;
        else 
            return false;
    }


    public void crossBridge(Car car){
        try{
        
            //while(activeCars >= 3)
            activeCars++;
            //System.out.println("Before "+car.getCarDId()+" Crossing, Active Cars:"+activeCars+"\n");
            waitingCars[car.getCarDirection()]--;
            System.out.println("Car "+car.getCarDId()+" is crossing\n");
            Thread.sleep(5000);
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return;
    }



    public boolean arriveToBridge(Car car){
    try
    {
        //System.out.println("arrive inside try\n");
        System.out.println("avail: " + inBridge.availablePermits());
        System.out.println("Car "+car.getCarDId()+" arriving at direction "+car.getCarDirection()+"\n");
        waitingCars[car.getCarDirection()]++;//here
        if(directions.get(car.getCarDirection()).availablePermits() == 1 && directions.get(1-car.getCarDirection()).availablePermits() == 1)
        {
            //System.out.println("arrive if 1\n");
            //in case this is the first thread, both directions will be closed, we open the one of the thread
            directions.get(1-car.getCarDirection()).acquire(1);
            System.out.println("Direction set to "+car.getCarDirection()+"\n");
        }


        
        if(!sameDirection(car.getCarDirection()))
        {
            //System.out.println("arrive inside if 2\n");
            //waitingCars[car.getCarDirection()]++;
            while(!sameDirection(car.getCarDirection()))
            {
                //wait until car has same direction of traffic
            }
            //System.out.println("maybe here");
            if(directions.get(1-car.getCarDirection()).availablePermits() == 1)//when directions match, lock opposite direction if not yet locked
                directions.get(1-car.getCarDirection()).acquire(1);;

            if(canCross()) // if cars that crossed the birdge are less than 3
            {
                if(act.availablePermits() == 0)
                    {
                        inBridge.acquireUninterruptibly(1);
                        //System.out.println("permits after acquire= " + inBridge.availablePermits() + "\n");
                    }
                    else{
                        inBridge.acquireUninterruptibly(1);//here
                        //System.out.println("permits after acquire= " + inBridge.availablePermits() + "\n");
                        act.release(1);
                    }
            }
            else //cant cross the bridge is full
            {
                //System.out.println("error print car "+car.getCarDId()+"\n");
                while(!(waitingCars[1-car.getCarDirection()] != 0 || canCross())) 
                {
                    //System.out.println("error print car "+car.getCarDId()+" blocked\n");
                    // wait until a permit is available or there are no cars in opposite direction
                }
                if(act.availablePermits() == 0)
                    {
                        inBridge.acquireUninterruptibly(1);
                        System.out.println("permits after acquire= " + inBridge.availablePermits() + "\n");
                    }
                    else{
                        inBridge.acquireUninterruptibly(1);//here
                        System.out.println("permits after acquire= " + inBridge.availablePermits() + "\n");
                        act.release(1);
                    }
            }
        }
        else
        {
            //System.out.println("arrive else 1 / cardId: "+car.getCarDId()+"\n");
            if(directions.get(1-car.getCarDirection()).availablePermits() == 1)
            {//when directions match, lock opposite direction if not yet locked
                directions.get(1-car.getCarDirection()).acquire(1);;
                if(canCross()) // if cars that crossed the birdge are less than 3
                {
                    if(act.availablePermits() == 0)
                    {
                        inBridge.acquireUninterruptibly(1);
                        //System.out.println("permits after acquire= " + inBridge.availablePermits() + "\n");
                    }
                    else{
                        inBridge.acquireUninterruptibly(1);//here
                        //System.out.println("permits after acquire= " + inBridge.availablePermits() + "\n");
                        act.release(1);
                    }
                }
                else //cant cross the bridge is full
                {
                    while(waitingCars[1-car.getCarDirection()] != 0 || canCross()) 
                    {
                        // wait until a permit is available or there are no cars in opposite direction
                    }
                    if(act.availablePermits() == 0)
                    {
                        //System.out.println("permits before acquire= " + inBridge.availablePermits() + "\n");
                        inBridge.acquireUninterruptibly(1);
                        //System.out.println("permits after acquire= " + inBridge.availablePermits() + "\n");
                    }
                    else{
                        //System.out.println("permits before acquire= " + inBridge.availablePermits() + "\n");
                        inBridge.acquireUninterruptibly(1);//here
                        //System.out.println("permits after acquire= " + inBridge.availablePermits() + "\n");
                        act.release(1);
                    }
                }
            }
        }
    }
    catch(Exception ex)
    {
        ex.printStackTrace();
        return false;
    }
    return true;
    }

    public void exitBridge(Car car){
        System.out.println("Car "+car.getCarDId()+" exiting\n");
        activeCars--;
        //inBridge.release();
        if(activeCars == 0 || waitingCars[car.getCarDirection()] == 0)
        {
            System.out.println("error print inside if before releasing: " + inBridge.availablePermits() +"\n");
            while(inBridge.availablePermits() < 3)
            {
                //System.out.println("error print inside while releasing\n");
                inBridge.release(1);
            }
        }
        if(activeCars == 0 || waitingCars[1-car.getCarDirection()] != 0) // if it is the last car that crosses the bridge
        {
            try{
            //System.out.println("testing0");
            directions.get(car.getCarDirection()).acquire(1); //Lock current direction
            directions.get(1-car.getCarDirection()).release(1); // open opposite direction
            System.out.println("Direction switched from "+car.getCarDirection()+" to "+(1-car.getCarDirection())+"\n");
            try {
                //System.out.println("testing");
                act.acquire(1);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }}catch(Exception e){}
        }
    
        return;
    }
}