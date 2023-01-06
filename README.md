# OOP.ASSIGNMENT2_PART_2

The Java Virtual machine (JVM) schedules threads using a preemptive, priority-based policy.
Every thread has a priority 􏰁 Threads with higher priority are executed in preference to threads with lower priority. When code running in a thread creates a new Thread object, the new thread has its initial priority set automatically equal to the priority of the creating thread.
If a thread was created using a different ThreadGroup, the priority of the newly created thread is the smaller of priority of the thread creating it and the maximum permitted priority of the thread group.
A user may set the priority of a thread using the method: public final void setPriority(int newPriority). The setPriority method changes the priority of a thread. For platform threads, the priority is set to the smaller of the specified newPriority and the maximum permitted priority of the thread group.



