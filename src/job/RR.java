package job;

import java.util.LinkedList;
import java.util.Map;

public class RR {
    // RR sınıfının cycle metodu, Round Robin algoritmasının bir bölümünü temsil ediyor gibi görünüyor
    public Process cycle(Map<Integer, LinkedList<Process>> processQueue, Process ongoing, Process ongoingProcessCandidate) {
        // Eğer 3. öncelik seviyesindeki kuyruk doluysa ve diğer öncelik seviyeleri boşsa işlemleri yeniden sırala
        if (processQueue.get(3) != null && processQueue.get(3).size() > 1
                && (processQueue.get(0) == null || processQueue.get(0).isEmpty())
                && (processQueue.get(1) == null || processQueue.get(1).isEmpty())
                && (processQueue.get(2) == null || processQueue.get(2).isEmpty())) {
            // Eğer devam eden işlem (ongoing) ve devam eden işlem adayı (ongoingProcessCandidate) aynıysa
            if (ongoing != null && ongoing.equals(ongoingProcessCandidate)) {
                // Öncelikli kuyruğu al veya oluştur, devam eden işlemi bu kuyruktan çıkar ve kuyruğun sonuna koy
                LinkedList<Process> prioritizedQueue = processQueue.getOrDefault(ongoing.priority(), new LinkedList<>());
                prioritizedQueue.remove(ongoing);
                prioritizedQueue.addLast(ongoing);
                processQueue.put(ongoing.priority(), prioritizedQueue);
                // Yeniden düzenlenmiş öncelikli kuyruktan ilk işlemi al ve geri döndür
                return processQueue.get(ongoingProcessCandidate.priority()).getFirst();
            }
        }
        // Durumunun herhangi bir değişiklik olmaması durumunda devam eden işlem adayını geri döndür
        return ongoingProcessCandidate;
    }
}
