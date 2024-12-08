public class Processe {
    String Name ;
    int Color ;
    int ArrivalTime ;
    int BurstTime ;
    int PriorityNumber ;
    int WaitingTime ;
    int ExecutionOrder;
    int TurnaroundTime;
    int QuantumTime ;
    Processe(String Name , int Color , int ArrivalTime , int BurstTime , int PriorityNumber , int QuantumTime){
        this.Name = Name ;
        this.Color = Color;
        this.ArrivalTime = ArrivalTime ;
        this.BurstTime = BurstTime ;
        this.PriorityNumber = PriorityNumber ;
        this.QuantumTime = QuantumTime ;
    }
    public void setWaitingTime(int t)
    {
        WaitingTime = t ;
    }
    public void setTurnaroundTime(int t)
    {
        TurnaroundTime = t ;
    }
    public void setExecutionOrder(int t)
    {
        ExecutionOrder = t ;
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
}
