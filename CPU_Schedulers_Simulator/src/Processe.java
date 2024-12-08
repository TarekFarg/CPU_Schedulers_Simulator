public class Processe {
    String Name ;
    int Color ;
    int ArrivalTime ;
    int BurstTime ;
    int PriorityNumber ;
    int WaitingTime ;
    Processe(String Name , int Color , int ArrivalTime , int BurstTime , int PriorityNumber){
        this.Name = Name ;
        this.Color = Color;
        this.ArrivalTime = ArrivalTime ;
        this.BurstTime = BurstTime ;
        this.PriorityNumber = PriorityNumber ;
        WaitingTime = 0 ;
    }
}
