package job;

import java.util.LinkedList;
import java.util.Map;

public class GBG {
    public Process getPrioritizedProcess(int timer, Map<Integer, LinkedList<Process>> processQueue) {

        Process ongoingProcessCandidate = null;
        int priority = 0;
        for (; priority < 4; priority++) {
            if (processQueue.get(priority) != null && !processQueue.get(priority).isEmpty()) {
                ongoingProcessCandidate = processQueue.get(priority).getFirst();
                while (!processQueue.isEmpty()) {
                    if (ongoingProcessCandidate.isTimeout(timer)) {
                        System.out.println(ongoingProcessCandidate.id() + " - HATA - Proses zaman aşımı (20 sn de tamamlanamadı).");
                        processQueue.get(ongoingProcessCandidate.priority()).remove(ongoingProcessCandidate);

                        if (processQueue.get(ongoingProcessCandidate.priority()).isEmpty()) break;

                        ongoingProcessCandidate = processQueue.get(ongoingProcessCandidate.priority()).getFirst();
                    } else {
                        break;
                    }
                }
            }
            if (ongoingProcessCandidate != null) {
                break;
            }
        }
        return ongoingProcessCandidate;
    }
}
