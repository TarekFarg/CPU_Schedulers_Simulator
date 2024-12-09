import java.util.ArrayList;
import java.util.List;

public class Processe {
    private String Name ;
    private int Color ;
    private int ArrivalTime ;
    private int BurstTime ;
    private int PriorityNumber ;
    private int WaitingTime ;
    private int ExecutionOrder;
    private int TurnaroundTime;
    private int QuantumTime ;
    private int FCAIfactor;
    private int finishedTime;
    private int initialBurst;
    List<Integer> UpdateOfQuantum ;
    Processe(String Name , int Color , int ArrivalTime , int BurstTime , int PriorityNumber , int QuantumTime){
        this.Name = Name ;
        this.Color = Color;
        this.ArrivalTime = ArrivalTime ;
        this.BurstTime = BurstTime ;
        this.PriorityNumber = PriorityNumber ;
        this.QuantumTime = QuantumTime ;
    }

    Processe(){
        ExecutionOrder = -1 ;
        UpdateOfQuantum = new ArrayList<>();
    }

    public void setWaitingTime(int t)
    {
        WaitingTime = t ;
    }
    public void setTurnaroundTime(int t)
    {
        TurnaroundTime = t ;
    }
    public void setName(String name)
    {
        Name = name ;
    }
    public void setArrivalTime(int t)
    {
        ArrivalTime = t ;
    }
    public void setColor(int c)
    {
        Color = c ;
    }
    public void setBurstTime(int t)
    {
        BurstTime = t ;
    }
    public void setPriorityNumber(int p)
    {
        PriorityNumber = p ;
    }
    public void setQuantumTime(int t)
    {
        QuantumTime = t ;
    }
    public void setExecutionOrder(int t)
    {
        ExecutionOrder = t ;
    }
    //for FCAI Schedule
    public void setFCAIfactor(int f)
    {
        FCAIfactor = f ;
    }
    public void setFinishedTime(int t)
    {
        finishedTime = t ;
    }
    public void setInitialBurst(int t)
    {
        initialBurst = t ;
    }


    public int getColor()
    {
        return Color;
    }
    public String getName()
    {
        return Name;
    }
    public int getArrivalTime()
    {
        return ArrivalTime;
    }
    public int getPriorityNumber()
    {
        return PriorityNumber ;
    }
    public int getBurstTime()
    {
        return BurstTime;
    }
    public int getWaitingTime()
    {
        return WaitingTime;
    }
    public int getExecutionOrder()
    {
        return ExecutionOrder;
    }
    public int getTurnaroundTime()
    {
        return TurnaroundTime;
    }
    public int getQuantumTime()
    {
        return QuantumTime;
    }
    //for FCAI Schedule
    int getFCAIfactor()
    {
        return FCAIfactor;
    }
    int getFinishedTime()
    {
        return finishedTime;
    }
    int getInitialBurst()
    {
        return initialBurst;
    }
}
