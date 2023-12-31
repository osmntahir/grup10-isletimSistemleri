package job;

import java.util.Objects;

public class Resource {
    private final int memory;
    private final int printer;
    private final int scanner;
    private final int modem;
    private final int CDDriver;

    public Resource(int memory, int printer, int scanner, int modem, int CDDriver) {
        this.memory = memory;
        this.printer = printer;
        this.scanner = scanner;
        this.modem = modem;
        this.CDDriver = CDDriver;
    }

    public int getPrinter() {
        return printer;
    }

    public int getScanner() {
        return scanner;
    }

    public int getModem() {
        return modem;
    }

    public int getCDDriver() {
        return CDDriver;
    }

    public int getMemory() {
        return memory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource that = (Resource) o;
        return printer == that.printer
                && scanner == that.scanner
                && modem == that.modem
                && CDDriver == that.CDDriver
                && memory == that.memory;
    }

    @Override
    public int hashCode() {
        return Objects.hash(printer, scanner, modem, CDDriver, memory);
    }

    @Override
    public String toString() {
        return "Resource{" +
                "memory=" + memory +
                ", printer=" + printer +
                ", scanner=" + scanner +
                ", modem=" + modem +
                ", CDDriver=" + CDDriver +
                '}';
    }
}
