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
        // Reading input file
    	String path ="";
    	if(args.length==0)
    	{
    		path = "giris.txt";
    	}
    	else
    	{
    		path = args[0];
    	}
        LinkedList<Process> processes = readProcesses(path);

        // Process queue
        Map<Integer, LinkedList<Process>> processQueue = new HashMap<>();

        // Defining settings
        final Resource systemResource = new Resource(1024, 2, 1, 1, 2);
        final FCFS fcfs = new FCFS();
        final RR rr = new RR();


        // Pointer
        Process ongoing = null;
        int loopTimer = -1;
        while (loopTimer < 1000) {
            loopTimer++;
            final int timer = loopTimer;

            // Welcoming new processes @timer second.
            List<Process> arrivedProcesses = processes.stream()
                    .filter(p -> p.startedAt() == timer)
                    .collect(Collectors.toList());
            arrivedProcesses = validate(arrivedProcesses, systemResource);

            if (ongoing != null && ongoing.isTimeout(timer)) {
                System.out.println(ongoing.id() + " - HATA - Proses zaman aşımı (20 sn de tamamlanamadı).");
                processQueue.get(ongoing.priority()).remove(ongoing);
                ongoing = null;
                continue;
            }

            // prioritized process queue constructed with LinkedList.
            // FCFS is provided by adding newly arrived processes at the end of the list and reading the first process based on its priority
            fcfs.dispatch(timer, processQueue, arrivedProcesses);

            // GBG is provided by querying from the most prioritized process if exists
            Process ongoingProcessCandidate = fcfs.queryForPrioritizedProcess(timer, processQueue);

            // Round-robin is running if the least prioritized queue is left.
            ongoingProcessCandidate = rr.cycle(processQueue, ongoing, ongoingProcessCandidate);

            // Suspension or not.
            if (ongoing == null && ongoingProcessCandidate != null) {
                ongoing = ongoingProcessCandidate;
                ongoing.start(timer);
            } else if (ongoingProcessCandidate != null && !ongoing.equals(ongoingProcessCandidate)) {
                // Suspending ongoing
                ongoing.suspend(timer);
                LinkedList<Process> prioritizedQueue = processQueue.getOrDefault(ongoing.priority(), new LinkedList<>());
                prioritizedQueue.remove(ongoing);
                prioritizedQueue.addLast(ongoing);
                processQueue.put(ongoing.priority(), prioritizedQueue);

                // Starting prioritized process
                ongoing = ongoingProcessCandidate;
                ongoing.start(timer);
            }

            // Ticking ongoing process.
            if (ongoing != null) {
                ongoing.tick();
                if (ongoing.isCompleted(timer)) {
                    processQueue.get(ongoing.priority()).remove(ongoing);
                    ongoing = null;
                }
            }
        }
    }

    private static LinkedList<Process> validate(List<Process> arrivedProcesses, Resource systemResource) {
        LinkedList<Process> validatedProcesses = new LinkedList<>();
        if (arrivedProcesses != null) {
            for (Process process : arrivedProcesses) {
                if (process.priority() == 0) {
                    if (process.tooMuchMemory()) {
                        System.out.println(process.id() + " - !! HATA - Gerçek-zamanlı proses (64MB) tan daha fazla bellek talep ediyor - proses silindi.");
                        continue;
                    }
                    if (process.tooMuchResource(systemResource)) {
                        System.out.println(process.id() + " - !! HATA - Gerçek-zamanlı proses çok sayıda kaynak talep ediyor - proses silindi.");
                        continue;
                    }

                } else {
                    if (process.tooMuchMemory()) {
                        System.out.println(process.id() + " - !! HATA - Proses (960 MB) tan daha fazla bellek talep ediyor – proses silindi");
                        continue;
                    }
                    if (process.tooMuchResource(systemResource)) {
                        System.out.println(process.id() + " - !! HATA - Proses çok sayıda kaynak talep ediyor - proses silindi");
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
            throw new RuntimeException("Process input cannot be read.", e);
        }
        return processes;
    }

}
