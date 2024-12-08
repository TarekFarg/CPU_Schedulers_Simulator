import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import static java.lang.Math.*;


public class FCAI {

    private List<Processe> processeList ;
    private int NumberOfProcesses ;
    private double v1 , v2 ;

    // Calc v1 and v2
    private void CalcV()
    {
        int max_b = 0 , lastA = 0 ;
        for(int i = 0 ; i < NumberOfProcesses ; i++)
        {
            max_b = max(max_b, processeList.get(i).getBurstTime()) ;
            lastA = max(lastA,processeList.get(i).getArrivalTime()) ;
        }
        v1 = (max_b*1.0)/10.0 ;
        v2 = (lastA*1.0)/10.0 ;
    }


    // Calc FCAI factor
    private int CalcFCAIfactor(Processe p)
    {
        //FCAI Factor = (10−Priority) + (Arrival Time/V1) + (Remaining Burst Time/V2)
        return  (int) ((10 - p.getPriorityNumber()) + ceil((double)p.getArrivalTime()/v1) + ceil((double)p.getBurstTime()/v2));
    }

    // constructor
    FCAI(List<Processe> list , int n)
    {
        this.processeList = list ;
        NumberOfProcesses = n ;
        CalcV();
        start();
    }


    void start()
    {
        // sort processes by arrival time
        processeList.sort(Comparator.comparingInt(Processe::getArrivalTime));

        // Priority Queue sorted by FCAI Factor
        PriorityQueue<Processe> queue = new PriorityQueue<>(Comparator.comparingInt(Processe::getFCAIfactor));

        int curTime = 0 , index = 0 , curQuantum = 0 ;
        Processe curProcess = null ;
        while(!queue.isEmpty() || index < NumberOfProcesses)
        {
            // add any process its arrival time >= curTime
            while(index < NumberOfProcesses && processeList.get(index).getArrivalTime() >= curTime)
            {
                // Calc FCAI factor for each process
                int f = CalcFCAIfactor(processeList.get(index));
                processeList.get(index).setFCAIfactor(f);

                // add to queue
                queue.add(processeList.get(index));
                index++ ;
            }

            // no process to execute
            if(queue.isEmpty() && curProcess == null)
            {
                // update the cur time to equal the arrival time of the next process
                curTime = processeList.get(index).getArrivalTime();
                continue;
            }


            // first 40% of the Quantum
            curProcess = queue.poll() ;
            curQuantum = curProcess.getQuantumTime() ;
            int executeTime = (int) ceil(0.4*curQuantum); // 40%
            executeTime = min(executeTime,curProcess.getBurstTime()) ; // check if the execute time > burst time
            curTime += executeTime ; // update cur time
            curQuantum -= executeTime ; //update the quantum
            curProcess.setBurstTime(curProcess.getBurstTime()-executeTime);


            // the remaining Quantum
            while (curQuantum>0)
            {

                //the process is preempted
                if(!queue.isEmpty() && queue.peek().getFCAIfactor() < curProcess.getFCAIfactor())
                {
                    // update the quantum -> quantum + unused quantum
                    curProcess.setQuantumTime(curProcess.getQuantumTime()+curQuantum);

                    //calc FCAI factor
                    curProcess.setFCAIfactor(CalcFCAIfactor(curProcess));

                    // add the process to the queue
                    queue.add(curProcess) ;
                }

                // add any process its arrival time >= curTime
                while (index < NumberOfProcesses && processeList.get(index).getArrivalTime() >= curTime)
                {
                    // Calc FCAI factor for each process will be added
                    int f = CalcFCAIfactor(processeList.get(index));
                    processeList.get(index).setFCAIfactor(f);

                    // add to queue
                    queue.add(processeList.get(index));
                    index++ ;
                }

                curProcess.setBurstTime(curProcess.getBurstTime()-1);
                curQuantum-- ;
                curTime++;

                // process is finished
                if(curProcess.getBurstTime()==0)
                {
                    curProcess = null ;
                    break;
                }

            }

            //process completes its quantum and still has remaining work
            if(curQuantum==0 && curProcess != null)
            {
                // update Quantum -> quantum + 2
                curProcess.setQuantumTime(curProcess.getQuantumTime()+2);
                //calc FCAI factor
                curProcess.setFCAIfactor(CalcFCAIfactor(curProcess));
                // add the process to the queue
                queue.add(curProcess) ;
            }

        }
    }
}
