package job;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FCFS {

    GBG gbg = new GBG();

    public void dispatch(int timer, Map<Integer, LinkedList<Process>> processQueue, List<Process> arrivedProcesses) {
        for (Process process : arrivedProcesses) {
            LinkedList<Process> prioritizedQueue = processQueue.getOrDefault(process.priority(), new LinkedList<>());
            process.queued(timer);
            prioritizedQueue.addLast(process);
            processQueue.put(process.priority(), prioritizedQueue);
        }
    }

    public Process queryForPrioritizedProcess(int timer, Map<Integer, LinkedList<Process>> processQueue) {
        return gbg.getPrioritizedProcess(timer, processQueue);
    }
}
