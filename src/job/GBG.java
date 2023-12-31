package job;

import java.util.LinkedList;
import java.util.Map;

public class GBG {
    // Öncelikli işlemi belirleme metodunu gerçekleştiren "getPrioritizedProcess" metodudur.
    public Process getPrioritizedProcess(int timer, Map<Integer, LinkedList<Process>> processQueue) {
        Process ongoingProcessCandidate = null; // Şu anki işlem adayı, başlangıçta null olarak ayarlanır.
        int priority = 0;
        // Önceliklere göre işlem adayını belirlemek için bir döngü kullanılır.
        for (; priority < 4; priority++) {
            // Öncelik sırasında boş olmayan kuyruğa sahip işlemler kontrol edilir.
            if (processQueue.get(priority) != null && !processQueue.get(priority).isEmpty()) {
                ongoingProcessCandidate = processQueue.get(priority).getFirst(); // Öncelikli işlem adayı alınır.
                // İşlem zaman aşımını kontrol eden bir döngü başlatılır.
                while (!processQueue.isEmpty()) {
                    // Eğer işlem zaman aşımına uğradıysa bildirim yapılır ve işlem kuyruktan çıkarılır.
                    if (ongoingProcessCandidate.isTimeout(timer)) {
                        System.out.println("P:" + ongoingProcessCandidate.id() + " - HATA - Proses zaman aşımı (20 sn de tamamlanamadı).");
                        processQueue.get(ongoingProcessCandidate.priority()).remove(ongoingProcessCandidate);

                        // Eğer öncelikli işlem kuyruğu boşsa döngüden çıkılır.
                        if (processQueue.get(ongoingProcessCandidate.priority()).isEmpty()) break;

                        // Kuyrukta hala işlem varsa bir sonraki öncelikli işlem adayı belirlenir.
                        ongoingProcessCandidate = processQueue.get(ongoingProcessCandidate.priority()).getFirst();
                    } else {
                        break; // İşlem zaman aşımına uğramadıysa döngüden çıkılır.
                    }
                }
            }
            if (ongoingProcessCandidate != null) {
                break; // İşlem adayı varsa döngüden çıkılır.
            }
        }
        return ongoingProcessCandidate; // Öncelikli işlem adayı döndürülür.
    }
}
