package job;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FCFS {

    GBG gbg = new GBG(); // GBG sınıfından bir nesne oluşturuluyor.

    // Process kuyruğuna gelen işlemleri sıraya yerleştirme metodunu işaret eden "dispatch" metodudur.
    public void dispatch(int timer, Map<Integer, LinkedList<Process>> processQueue, List<Process> arrivedProcesses) {
        for (Process process : arrivedProcesses) {
            LinkedList<Process> prioritizedQueue = processQueue.getOrDefault(process.priority(), new LinkedList<>());
            process.queued(timer); // İşlem zamanı (timer) ayarlanıyor.
            prioritizedQueue.addLast(process); // Öncelikli kuyruğa işlem ekleniyor.
            processQueue.put(process.priority(), prioritizedQueue); // İşlem önceliğine göre kuyruğa işlem ekleniyor.
        }
    }

    // Öncelikli işlemi sorgulamak için kullanılan metod.
    public Process queryForPrioritizedProcess(int timer, Map<Integer, LinkedList<Process>> processQueue) {
        return gbg.getPrioritizedProcess(timer, processQueue); // Öncelikli işlemi alma işlemi gerçekleştiriliyor.
    }
}
