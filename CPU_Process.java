import java.util.*;
import java.util.concurrent.TimeUnit;

interface SchedulingAlgorithm {
    void addProcess(Process p, BestFitMemoryAllocation memoryManager);
    void executeProcesses();
}

abstract class Process implements Runnable {
    private static final Random random = new Random();
    private static final Set<Integer> usedIds = new HashSet<>();
    private final int processId;
    private final int priority;
    private final int arrivalTime;
    private final int memory;
    private String state;
    private int remainingTime;

    Process(int priority, int arrivalTime, int burstTime, int memory) {
        this.priority = priority;
        this.processId = generateId();
        this.arrivalTime = arrivalTime;
        this.remainingTime = burstTime;
        this.memory = memory;
        this.state = "NEW";
    }

    private int generateId() {
        int id;
        do {
            id = 10000 + random.nextInt(90000);
        } while (usedIds.contains(id));
        usedIds.add(id);
        return id;
    }

    public abstract void start();

    @Override
    public void run() {
        start();
        while (remainingTime > 0) {
            executeTimeSlice();
        }
        terminate();
    }

    private void executeTimeSlice() {
        int timeSlice = Math.min(remainingTime, 2);
        remainingTime -= timeSlice;

        System.out.println("Running Process ID: " + processId + ", Remaining Time: " + remainingTime);

        try {
            TimeUnit.SECONDS.sleep(timeSlice);
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public abstract void terminate();

    public int getArrivalTime() {
        return arrivalTime;
    }

    public String getState() {
        return this.state;
    }

    public int getMemory() {
        return this.memory;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getProcessId() {
        return processId;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public int getPriority() {
        return priority;
    }
}

class MyProcess extends Process {
    MyProcess(int priority, int arrivalTime, int burstTime, int memory) {
        super(priority, arrivalTime, burstTime, memory);
    }

    @Override
    public void start() {
        setState("RUNNING");
        System.out.println("Process ID: " + getProcessId() + " is starting.");
    }

    @Override
    public void terminate() {
        setState("TERMINATED");
        System.out.println("Process ID: " + getProcessId() + " has terminated.");
    }
}

abstract class ProcessScheduler implements SchedulingAlgorithm {
    protected Queue<Process> readyQueue;

    ProcessScheduler() {
        this.readyQueue = new LinkedList<>();
    }

    public boolean isReadyQueueEmpty() {
        return readyQueue.isEmpty();
    }
}

class RoundRobinScheduler extends ProcessScheduler {
    RoundRobinScheduler() {
        super();
    }

    @Override
    public void addProcess(Process p, BestFitMemoryAllocation memoryManager) {
        if (memoryManager.allocateMemory(p.getMemory())) {
            readyQueue.add(p);
            System.out.println("Process ID: " + p.getProcessId() + " added to the queue.");
        } else {
            System.out.println("Process ID: " + p.getProcessId() + " not added due to insufficient memory.");
        }
    }

    @Override
    public void executeProcesses() {
        int currentTime = 0;

        while (!isReadyQueueEmpty()) {
            Process process = readyQueue.poll();

            if (process != null && process.getArrivalTime() <= currentTime) {
                process.run();

                if (process.getRemainingTime() > 0) {
                    readyQueue.add(process);
                }
            } else if (process != null) {
                readyQueue.add(process);
                currentTime++;
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }
}

class BestFitMemoryAllocation {
    private LinkedList<Integer> memoryBlocks = new LinkedList<>();

    BestFitMemoryAllocation() {
        int[] blocks = {100, 400, 200, 500, 250, 450, 150, 1000, 150, 550};
        for (int block : blocks) {
            memoryBlocks.add(block);
        }
    }

    public boolean allocateMemory(int memorySize) {
        Integer bestBlock = null;
        int bestIndex = -1;

        for (int i = 0; i < memoryBlocks.size(); i++) {
            int blockSize = memoryBlocks.get(i);
            if (blockSize >= memorySize) {
                if (bestBlock == null || blockSize < bestBlock) {
                    bestBlock = blockSize;
                    bestIndex = i;
                }
            }
        }

        if (bestBlock != null) {
            memoryBlocks.set(bestIndex, bestBlock - memorySize);
            return true;
        } else {
            return false;
        }
    }

    public void displayMemoryStatus() {
        System.out.println("Memory Block Status:");
        for (int i = 0; i < memoryBlocks.size() - 1; i++) {
            System.out.print(memoryBlocks.get(i) + "->");
        }
        System.out.println(memoryBlocks.get(memoryBlocks.size() - 1));
        System.out.println("");
    }
}

public class CPU_Process {
    public static void main(String[] args) {
        BestFitMemoryAllocation memoryManager = new BestFitMemoryAllocation();
        System.out.println("");
        memoryManager.displayMemoryStatus();
        System.out.println("");
        
        List<Process> processes = new ArrayList<>();
        processes.add(new MyProcess(2, 0, 5, 512));
        processes.add(new MyProcess(1, 2, 3, 256));
        processes.add(new MyProcess(3, 1, 4, 128));
        
        ProcessScheduler roundRobinScheduler = new RoundRobinScheduler();
        for (Process p : processes) {
            roundRobinScheduler.addProcess(p, memoryManager);
        }
        
        roundRobinScheduler.executeProcesses();
        System.out.println("");
        memoryManager.displayMemoryStatus();
    }
}
