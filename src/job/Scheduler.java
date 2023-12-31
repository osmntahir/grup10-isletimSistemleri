package job;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Scheduler {

    public static void main(String[] args) {
        // Input dosyasının okunması
    	String path ="";
    	if(args.length==0)
    	{
    		path = "giris.txt";
    	}
    	else
    	{
    		path = args[0];
    	}
        LinkedList<Process> processes = readProcesses(path); // Okunan işlemlerin listeye alınması

        // İşlem kuyruğu
        Map<Integer, LinkedList<Process>> processQueue = new HashMap<>();

        // Kaynak tanımlaması ve algoritmalar
        final Resource systemResource = new Resource(1024, 2, 1, 1, 2);
        final FCFS fcfs = new FCFS();
        final RR rr = new RR();
       
        // İşaretçi
        Process ongoing = null;
        int loopTimer = -1;
        while (loopTimer < 1000) {
            loopTimer++;
            final int timer = loopTimer;

            // Yeni gelen işlemlerin zamanlayıcıya eklenmesi
            List<Process> arrivedProcesses = processes.stream()
                    .filter(p -> p.startedAt() == timer)
                    .collect(Collectors.toList());
            arrivedProcesses = validate(arrivedProcesses, systemResource);

            if (ongoing != null && ongoing.isTimeout(timer)) {
                System.out.println("P:"+ongoing.id() + " - HATA - İşlem zaman aşımına uğradı (20 sn içinde tamamlanamadı).");
                processQueue.get(ongoing.priority()).remove(ongoing);
                ongoing = null;
                continue;
            }

            // Öncelikli işlem kuyruğu, FCFS algoritmasıyla oluşturuluyor
            fcfs.dispatch(timer, processQueue, arrivedProcesses);

            // GBG algoritması, en öncelikli işlem varsa onu sorguluyor
            Process ongoingProcessCandidate = fcfs.queryForPrioritizedProcess(timer, processQueue);

            // Round-robin, en düşük öncelikli kuyruk varsa çalıştırılıyor
            ongoingProcessCandidate = rr.cycle(processQueue, ongoing, ongoingProcessCandidate);

            // Askıya alma veya devam etme
            if (ongoing == null && ongoingProcessCandidate != null) {
                ongoing = ongoingProcessCandidate;
                ongoing.start(timer);
            } else if (ongoingProcessCandidate != null && !ongoing.equals(ongoingProcessCandidate)) {
                // Devam eden işlemi askıya alma
                ongoing.suspend(timer);
                LinkedList<Process> prioritizedQueue = processQueue.getOrDefault(ongoing.priority(), new LinkedList<>());
                prioritizedQueue.remove(ongoing);
                prioritizedQueue.addLast(ongoing);
                processQueue.put(ongoing.priority(), prioritizedQueue);

                // Öncelikli işlemi başlatma
                ongoing = ongoingProcessCandidate;
                ongoing.start(timer);
            }

            // Devam eden işlemin ilerlemesi
            if (ongoing != null) {
                ongoing.tick();
                if (ongoing.isCompleted(timer)) {
                    processQueue.get(ongoing.priority()).remove(ongoing);
                    ongoing = null;
                }
            }
            
            // Yavaşça yazdırmak için Thread.sleep
            try {
                Thread.sleep(200); // 100 milisaniye bekle
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static LinkedList<Process> validate(List<Process> arrivedProcesses, Resource systemResource) {
        LinkedList<Process> validatedProcesses = new LinkedList<>();
        if (arrivedProcesses != null) {
            for (Process process : arrivedProcesses) {
                if (process.priority() == 0) {
                    if (process.tooMuchMemory()) {
                        System.out.println("P:" + process.id() + " - !! HATA - Gerçek zamanlı işlem (64MB) için fazla bellek talep ediyor - işlem silindi.");
                        continue;
                    }
                    if (process.tooMuchResource(systemResource)) {
                        System.out.println("P:" + process.id() + " - !! HATA - Gerçek zamanlı işlem çok fazla kaynak talep ediyor - işlem silindi.");
                        continue;
                    }

                } else {
                    if (process.tooMuchMemory()) {
                        System.out.println("P:" + process.id() + " - !! HATA - İşlem (960 MB) için fazla bellek talep ediyor – işlem silindi");
                        continue;
                    }
                    if (process.tooMuchResource(systemResource)) {
                        System.out.println("P:" + process.id() + " - !! HATA - İşlem çok fazla kaynak talep ediyor - işlem silindi");
                        continue;
                    }
                }
                validatedProcesses.add(process);
            }
        }
        return validatedProcesses;
    }

    private static LinkedList<Process> readProcesses(String path) {
        String processInputResource = path;
        LinkedList<Process> processes = new LinkedList<>();
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(processInputResource))) {
            bufferedReader.lines().forEach(processInput -> processes.add(Process.parse(processInput)));
        } catch (IOException e) {
            throw new RuntimeException("İşlem girişi okunamadı.", e);
        }
        return processes;
    }

}
