import java.util.concurrent.CyclicBarrier;
import java.util.ArrayList;
import java.util.Random;

public class MainScenario2 {
    static Bridge br = new Bridge();
    public static void main(String[] args){

        System.out.println("Bridge is ready!\n");
        int numThreads = 0;
        int carIdSequence = 0;
        
        Random r = new Random();
        
        numThreads = 10;
       
        int scenarioCounter = 0;
        while(scenarioCounter < 2)
        {    
            final CyclicBarrier gate = new CyclicBarrier(numThreads);
            System.out.println("Number of threads: "+numThreads);
            int counter = 0;
            ArrayList<Car> cars = new ArrayList<>();
            int waitingOne = 0, waitingZero = 0;
            
            while(counter < numThreads)
            {
                Car car = new Car(carIdSequence ,r.nextInt(2),gate,br);
                cars.add(car);
                counter++;
                carIdSequence++;
                if(car.getCarDirection() == 1)
                    waitingOne++;
                else
                    waitingZero++;
            }

            br.setParameters(waitingZero, waitingOne, 0);            
            System.out.println("Cars:");
            
            for (Car c : cars) {
                Thread t1 = new Thread(c, "Thread - T1");
                t1.start();
            }
            try {
                Thread.sleep(150000);
            }catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            scenarioCounter++;
        }
       
       

    }
}
