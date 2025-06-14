CPU Process Scheduler with Best Fit Memory Allocation
This project simulates a CPU Process Scheduler using the Round Robin scheduling algorithm combined with Best Fit memory allocation strategy.
It models how operating systems handle process scheduling and memory management in a simplified environment.

--Features
1) Process Scheduling:
Implements the Round Robin CPU scheduling algorithm with a fixed time slice.
Each process runs in slices of up to 2 seconds.

2) Memory Allocation:
Uses the Best Fit strategy to allocate memory to processes from available memory blocks.
Processes are only scheduled if sufficient memory is available.

3) Process States:
Tracks states like NEW, RUNNING, and TERMINATED.

4)Simulated Time:
Uses TimeUnit.SECONDS.sleep to simulate real-time process execution.

--Project Structure
.
├── CPU_Process.java                # Main entry point
├── SchedulingAlgorithm.java       # Scheduling algorithm interface
├── Process.java                   # Abstract base class for processes
├── MyProcess.java                 # Concrete implementation of a process
├── ProcessScheduler.java          # Abstract scheduler class
├── RoundRobinScheduler.java      # Round Robin scheduler implementation
├── BestFitMemoryAllocation.java  # Best Fit memory management class

--How It Works
1)Memory Initialization:
A set of memory blocks is initialized (e.g., 100, 400, 200...).

2) Process Creation:
A list of processes with different priorities, arrival times, burst times, and memory requirements is created.

3)Adding to Scheduler:
Each process is added to the Round Robin scheduler if memory can be allocated.

4)Execution:
The scheduler executes each process in a time-sliced round-robin manner.
If the process still has remaining time, it's re-added to the queue.


Author
Dev Vaghani
Student at Nirma University
Pupil at Codeforces (1267), 3⭐ at CodeChef (1609)
Interested in Machine Learning & System Programming

