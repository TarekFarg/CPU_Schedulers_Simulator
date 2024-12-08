import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        System.out.print("Enter the Number of processes: ");
        int NumberOfProcesses = Integer.parseInt(scn.nextLine()) ;
        System.out.print("Enter the Round Robin Time Quantum: ");
        int RoundRobinTimeQuantum = Integer.parseInt(scn.nextLine());
        System.out.print("Enter the context switching: ");
        int contextSwitching = Integer.parseInt(scn.nextLine());

        List<Processe> processeList = new ArrayList<>();
        for(int i = 1 ; i <= NumberOfProcesses ; i++)
        {
            Processe p = new Processe();
            System.out.print("Enter the " + i + "th Process name: ");
            p.setName(scn.nextLine());
            System.out.print("Enter the " + i + "th Process color: ");
            p.setColor(Integer.parseInt(scn.nextLine()));
            System.out.print("Enter the " + i + "th Process Arrival Time : ");
            p.setArrivalTime(Integer.parseInt(scn.nextLine()));
            System.out.print("Enter the " + i + "th Process Burst Time : ");
            p.setBurstTime(Integer.parseInt(scn.nextLine()));
            System.out.print("Enter the " + i + "th Process Priority Number : ");
            p.setPriorityNumber(Integer.parseInt(scn.nextLine()));
            System.out.print("Enter the " + i + "th Process Quantum Time if needed : ");
            p.setQuantumTime(Integer.parseInt(scn.nextLine()));
            processeList.add(p) ;
        }

        System.out.println("Choose the type of Scheduling: ");
        System.out.println("SJF -> 1");
        System.out.println("SRTF -> 2");
        System.out.println("Priority -> 3");
        System.out.println("FCAI -> 4");
        int c = scn.nextInt() ;
        switch (c)
        {
            case 1:
                System.out.println("SJF");
                //
                break;
            case 2:
                System.out.println("SRTF");
                //
                break;
            case 3:
                System.out.println("Priority");
                //
                break;
            case 4:
                System.out.println("FCAI");
                FCAI f = new FCAI(processeList , NumberOfProcesses) ;
                break;
            default:
                System.out.println("Invalid choice");
        }
    }
}