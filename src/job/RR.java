package job;

import java.util.LinkedList;
import java.util.Map;

public class RR {
    public Process cycle(Map<Integer, LinkedList<Process>> processQueue, Process ongoing, Process ongoingProcessCandidate) {
        if (processQueue.get(3)!=null && processQueue.get(3).size() > 1
                && (processQueue.get(0) == null || processQueue.get(0).isEmpty())
                && (processQueue.get(1) == null || processQueue.get(1).isEmpty())
                && (processQueue.get(2) == null || processQueue.get(2).isEmpty())) {
            if (ongoing != null && ongoing.equals(ongoingProcessCandidate)) {
                LinkedList<Process> prioritizedQueue = processQueue.getOrDefault(ongoing.priority(), new LinkedList<>());
                prioritizedQueue.remove(ongoing);
                prioritizedQueue.addLast(ongoing);
                processQueue.put(ongoing.priority(), prioritizedQueue);
                return processQueue.get(ongoingProcessCandidate.priority()).getFirst();
            }
        }
        return ongoingProcessCandidate;
    }
}
