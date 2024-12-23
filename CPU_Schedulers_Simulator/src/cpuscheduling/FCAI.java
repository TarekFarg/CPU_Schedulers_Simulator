package cpuscheduling;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import static java.lang.Math.*;


public class FCAI {

    private List<Processe> processeList , finishedList;
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

            // set initial burst time
            processeList.get(i).setInitialBurst(processeList.get(i).getBurstTime());
        }
        v1 = (lastA*1.0)/10.0 ;
        v2 = (max_b*1.0)/10.0 ;
    }


    // Calc cpuscheduling.FCAI factor
    private int CalcFCAIfactor(Processe p)
    {
        //cpuscheduling.FCAI Factor = (10−cpuscheduling.Priority) + (Arrival Time/V1) + (Remaining Burst Time/V2)
        return  (int) ((10 - p.getPriorityNumber()) + ceil((double)p.getArrivalTime()/v1) + ceil((double)p.getBurstTime()/v2));
    }

    // constructor
    public FCAI(List<Processe> list, int n)
    {
        this.processeList = list ;
        finishedList = new ArrayList<>();
        NumberOfProcesses = n ;
        CalcV();
        start();
        CalcResults();
        showResultsInGUI();
    }


    void start()
    {
        // sort processes by arrival time
        processeList.sort(Comparator.comparingInt(Processe::getArrivalTime));

        // cpuscheduling.Priority Queue sorted by cpuscheduling.FCAI Factor
        PriorityQueue<Processe> queue = new PriorityQueue<>(Comparator.comparingInt(Processe::getFCAIfactor));

        int curTime = 0 , index = 0 , curQuantum = 0 , numOfExecuted = 0 ;
        Processe curProcess = null ;
        while(!queue.isEmpty() || index < NumberOfProcesses)
        {
            // add any process its arrival time <= curTime
            while(index < NumberOfProcesses && processeList.get(index).getArrivalTime() <= curTime)
            {
                // Calc cpuscheduling.FCAI factor for each process
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
            if(curProcess==null)
                curProcess = queue.poll() ;

            if(curProcess.getExecutionOrder()==-1) // set Execution order & add the initial quantum
            {
                numOfExecuted++;
                curProcess.setExecutionOrder(numOfExecuted);
                curProcess.UpdateOfQuantum.add(curProcess.getQuantumTime()); //
            }

            curQuantum = curProcess.getQuantumTime() ;
            int executeTime = (int) ceil(0.4*curQuantum); // 40%
            executeTime = min(executeTime,curProcess.getBurstTime()) ; // check if the execute time > burst time
            curTime += executeTime ; // update cur time
            curQuantum -= executeTime ; //update the quantum
            curProcess.setBurstTime(curProcess.getBurstTime()-executeTime);


            // check if the process is finished
            if(curProcess.getBurstTime()==0)
            {
                curProcess.setFinishedTime(curTime); // set the finished time
                finishedList.add(curProcess) ; // add to the finished list
                curProcess = null ;
                continue;
            }

            // the remaining Quantum
            while (curQuantum>0)
            {

                // add any process its arrival time <= curTime
                while (index < NumberOfProcesses && processeList.get(index).getArrivalTime() <= curTime)
                {
                    // Calc cpuscheduling.FCAI factor for each process will be added
                    int f = CalcFCAIfactor(processeList.get(index));
                    processeList.get(index).setFCAIfactor(f);

                    // add to queue
                    queue.add(processeList.get(index));
                    index++ ;
                }


                //the process is preempted
                if(!queue.isEmpty() && queue.peek().getFCAIfactor() < curProcess.getFCAIfactor())
                {
                    // update the quantum -> quantum + unused quantum
                    curProcess.setQuantumTime(curProcess.getQuantumTime()+curQuantum);
                    curProcess.UpdateOfQuantum.add(curProcess.getQuantumTime()); // add the updated quantum

                    //calc cpuscheduling.FCAI factor
                    curProcess.setFCAIfactor(CalcFCAIfactor(curProcess));

                    // add the process to the queue
                    queue.add(curProcess) ;
                    curProcess = null;
                    break;
                }


                curProcess.setBurstTime(curProcess.getBurstTime()-1);
                curQuantum-- ;
                curTime++;

                // process is finished
                if(curProcess.getBurstTime()==0)
                {
                    curProcess.setFinishedTime(curTime); // set the finished time
                    finishedList.add(curProcess) ; // add to the finished list
                    curProcess = null ;
                    break;
                }

            }

            //process completes its quantum and still has remaining work
            if(curQuantum==0 && curProcess != null)
            {
                // update Quantum -> quantum + 2
                curProcess.setQuantumTime(curProcess.getQuantumTime()+2);
                curProcess.UpdateOfQuantum.add(curProcess.getQuantumTime()); // add the updated quantum

                //calc cpuscheduling.FCAI factor
                curProcess.setFCAIfactor(CalcFCAIfactor(curProcess));
                // add the process to the queue
                Processe next = null ;
                if(!queue.isEmpty()) next = queue.poll();
                queue.add(curProcess) ;
                curProcess = next;
            }

        }
    }

    void CalcResults()
    {
        // sort finished list by arrival time
        finishedList.sort(Comparator.comparingInt(Processe::getArrivalTime));

        // burst time in the finished process now is equal to zero
        // we want to set the initial burst time
        for(Processe p : finishedList)
        {
            // make the burst time equal to the initial value
            p.setBurstTime(p.getInitialBurst());
        }


        // calc waiting time & Turnaround Time
        for(Processe p : finishedList)
        {
            int w = p.getFinishedTime()-p.getArrivalTime(); // Turnaround Time -> finished - Arrival
            p.setTurnaroundTime(w); // set Turnaround Time

            w -= p.getBurstTime() ; // waiting time -> finished - arrival - burst
            p.setWaitingTime(w); // set waiting time
        }
    }

    private void showResultsInGUI() {
        JFrame resultFrame = new JFrame("FCAI Scheduling Results");
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultFrame.setSize(800, 600);

        String[] columnNames = {"Process", "Arrival", "Burst", "Waiting", "Turnaround", "ExecutionOrder"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable resultTable = new JTable(tableModel);

        for (Processe p : finishedList) {
            tableModel.addRow(new Object[]{
                    p.getName(),
                    p.getArrivalTime(),
                    p.getBurstTime(),
                    p.getWaitingTime(),
                    p.getTurnaroundTime(),
                    p.getExecutionOrder()
            });
        }

        double totalWaitingTime = finishedList.stream().mapToInt(Processe::getWaitingTime).sum();
        double totalTurnaroundTime = finishedList.stream().mapToInt(Processe::getTurnaroundTime).sum();
        double avgWaitingTime = totalWaitingTime / NumberOfProcesses;
        double avgTurnaroundTime = totalTurnaroundTime / NumberOfProcesses;

        JLabel avgWaitingTimeLabel = new JLabel("Average Waiting Time: " + avgWaitingTime);
        JLabel avgTurnaroundTimeLabel = new JLabel("Average Turnaround Time: " + avgTurnaroundTime);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JScrollPane tableScrollPane = new JScrollPane(resultTable);

        mainPanel.add(tableScrollPane);
        mainPanel.add(avgWaitingTimeLabel);
        mainPanel.add(avgTurnaroundTimeLabel);

        resultFrame.add(mainPanel);
        resultFrame.setVisible(true);
    }

    public List<Processe> getFinishedList() {
        return finishedList;
    }
}
