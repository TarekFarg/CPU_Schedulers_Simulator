import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import static java.lang.Math.ceil;
import static java.lang.Math.max;


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
        // Priority Queue sorted by FCAI Factor
        PriorityQueue<Processe> queue = new PriorityQueue<>(Comparator.comparingInt(Processe::getFCAIfactor));
        // Calc FCAI factor for each process
        for(int i = 0 ; i < NumberOfProcesses ; i++)
        {
            int f = CalcFCAIfactor(processeList.get(i));
            processeList.get(i).setFCAIfactor(f);
            queue.add(processeList.get(i));
        }
        while(!queue.isEmpty())
        {
            Processe cur = queue.poll() ;
            cur.setBurstTime(cur.getBurstTime()-cur.getQuantumTime());
            if(cur.getBurstTime()>0)
            {
                cur.setQuantumTime(cur.getQuantumTime()+1);
                cur.setFCAIfactor(CalcFCAIfactor(cur));
                queue.add(cur) ;
            }
        }
    }
}
