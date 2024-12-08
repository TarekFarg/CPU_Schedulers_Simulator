import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import static java.lang.Math.*;


public class FCAI {
    private List<Processe> processeList ;
    private int NumberOfProcesses ;
    private int RoundRobinTimeQuantum;
    private int contextSwitching;
    private double v1 , v2 ;
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
    private int CalcFCAIfactor(Processe p)
    {
        //FCAI Factor = (10−Priority) + (Arrival Time/V1) + (Remaining Burst Time/V2)
        return  (int) ((10 - p.getPriorityNumber()) + ceil((double)p.getArrivalTime()/v1) + ceil((double)p.getBurstTime()/v2));
    }
    FCAI(List<Processe> list , int n , int r , int c)
    {
        this.processeList = list ;
        NumberOfProcesses = n ;
        RoundRobinTimeQuantum = r ;
        contextSwitching = c ;
        CalcV();
    }

    void start()
    {
        // sort processes by arrival time
        processeList.sort(Comparator.comparingInt(Processe::getArrivalTime));

        // Priority Queue sorted by FCAI Factor
        PriorityQueue<Processe> queue = new PriorityQueue<>(Comparator.comparingInt(Processe::getFCAIfactor));

        int curTime = 0 , index = 0 ;
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
                curTime++;
                continue;
            }

            
            curProcess = queue.poll() ;
            int executeTime = (int) ceil(0.4*curProcess.getQuantumTime()); // 40%
            executeTime = min(executeTime,curProcess.getBurstTime()) ; // check if the execute time > burst time
            curTime += executeTime ;
            curProcess.setBurstTime(curProcess.getBurstTime()-executeTime);


            if(curProcess.getBurstTime() > 0)
            {
                curProcess.setFCAIfactor(CalcFCAIfactor(curProcess)); // update FCAI factor
                queue.add(curProcess) ;
            }
            else
                curProcess = null ;

            curTime++;
        }
    }
}
