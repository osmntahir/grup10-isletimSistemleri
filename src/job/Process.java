package job;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Process {
    private final static AtomicInteger pidSequencer = new AtomicInteger(0);

    private final static int TIMEOUT = 20;

    private final static int MAX_MEMORY_REAL_TIME_PROCESS = 64;
    private final static int MAX_MEMORY_USER_PROCESS = 960;

    // İşlem zaman aşımını kontrol eden metod
    public boolean isTimeout(int timer) {
        return timer - startedAt >= TIMEOUT;
    }

    // İşlem durumunu belirleyen enum
    public enum Status {
        ACCEPTED,
        QUEUED,
        SUSPENDED,
        RUNNING,
        COMPLETED,
        FAILED
    }

    private final int id;
    private final int startedAt;
    private final int priority;
    private int elapsedTime;
    private final Resource resource;
    private Status status = Status.ACCEPTED;
    private final String color;

    // Process sınıfının yapıcı metodları
    public Process(int id, int startedAt, int priority, int elapsedTime, Resource resource) {
        this.id = id;
        this.startedAt = startedAt;
        this.priority = priority;
        this.elapsedTime = elapsedTime;
        this.resource = resource;
        this.color = Color.getRandomColor(id % 7); // Renk atanıyor
    }

    // Getter metodlar
    public int id() {
        return id;
    }

    public int startedAt() {
        return startedAt;
    }

    public int priority() {
        return priority;
    }

    public String color() {
        return color;
    }

    // İşlem durumlarını değiştiren metodlar
    public void start(int timer) {
        status = Status.RUNNING;
        System.out.println(timer + ". second. Process started. \t\t" + this);
    }

    public void suspend(int timer) {
        status = Status.SUSPENDED;
        System.out.println(timer + ". second. Process suspended. \t\t" + this);
    }

    public boolean isCompleted(int timer) {
        if (this.elapsedTime == 0) {
            this.status = Status.COMPLETED;
            System.out.println(timer + ". second. Process completed. \t\t" + this);
            return true;
        } else {
            System.out.println(timer + ". second. Process running. \t\t" + this);
            return false;
        }
    }

    public void queued(int timer) {
        this.status = Status.QUEUED;
        System.out.println(timer + ". second. Process queued. \t\t" + this);
    }

    // Bellek limitleri kontrol eden metodlar
    public boolean tooMuchMemory() {
        if (priority == 0) {
            return resource.getMemory() > MAX_MEMORY_REAL_TIME_PROCESS;
        } else {
            return resource.getMemory() > MAX_MEMORY_USER_PROCESS;
        }
    }

    public boolean tooMuchResource(Resource systemResource) {
        if (priority == 0) {
            return resource.getPrinter() + resource.getScanner() + resource.getModem() + resource.getCDDriver() != 0;
        } else {
            return resource.getMemory() > systemResource.getMemory() ||
                    resource.getPrinter() > systemResource.getPrinter() ||
                    resource.getScanner() > systemResource.getScanner() ||
                    resource.getModem() > systemResource.getModem() ||
                    resource.getCDDriver() > systemResource.getCDDriver();
        }
    }

    // String formatındaki kayıttan bir Process objesi oluşturan metod
    public static Process parse(String record) {
        String[] inputs = record.split(",");
        int startAt = Integer.parseInt(inputs[0].trim());
        int priority = Integer.parseInt(inputs[1].trim());
        int time = Integer.parseInt(inputs[2].trim());
        int memory = Integer.parseInt(inputs[3].trim());
        int printer = Integer.parseInt(inputs[4].trim());
        int scanner = Integer.parseInt(inputs[5].trim());
        int modem = Integer.parseInt(inputs[6].trim());
        int CDDriver = Integer.parseInt(inputs[7].trim());
        Resource resource = new Resource(memory, printer, scanner, modem, CDDriver);
        return new Process(pidSequencer.getAndIncrement(), startAt, priority, time, resource);
    }

    // Zamanı azaltan metod
    public void tick() {
        this.elapsedTime--;
    }

    // Equals, hashCode ve toString metotları
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Process process = (Process) o;
        return id == process.id &&
                startedAt == process.startedAt &&
                priority == process.priority &&
                elapsedTime == process.elapsedTime &&
                Objects.equals(resource, process.resource) &&
                status == process.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startedAt, priority, elapsedTime, resource, status);
    }

    @Override
    public String toString() {
        return "Process{" +
                "id=" + id +
                ", priority=" + priority +
                ", elapsedTime=" + elapsedTime +
                ", resource=" + resource +
                ", status=" + status + color +
                '}';
    }
}
