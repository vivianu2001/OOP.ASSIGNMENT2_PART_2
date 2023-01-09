/**
 * An enumeration of task types. Each type has a priority value that determines the priority of the task.
 * The priority is represented by an integer value, ranging from 1 to 10.
 */
    public enum TaskType {
        /**
         * A computational task.
         */
        COMPUTATIONAL(1) {
            @Override
            public String toString() {
                return "Computational Task";
            }
        },
        /**
        * An IO-bound task.
         */
        IO(2) {
            @Override
            public String toString() {
                return "IO-Bound Task";
            }
        },
         /**
          ** An unknown task.
          */
        OTHER(3) {
            @Override
            public String toString() {
                return "Unknown Task";
            }
        };
        private int typePriority;

        /**
        * Creates a new task type with the given priority.
         *
         * @param priority the priority of the task
         * @throws IllegalArgumentException if the priority is not an integer between 1 and 10
         */
        private TaskType(int priority)
        {
            if(validatePriority(priority))
            {
                this.typePriority=priority;
            }
            else
                throw new IllegalArgumentException("Priority is not an integer");

        }
        /**
         * Sets the priority of the task.
         *
        * @param priority the new priority of the task
        * @throws IllegalArgumentException if the priority is not an integer between 1 and 10
        */
        public void setPriority(int priority){
            if(validatePriority(priority))
            {
                this.typePriority=priority;
            }
            else
                throw new IllegalArgumentException("Priority is not an integer");
        }
        /**
        * Gets the priority value of the task.
         *
        * @return the priority value of the task
         */
        public int getPriorityValue()
        {
            return typePriority;
        }
        /**
         * Gets the type of the task.
         *
         * @return the type of the task
         */
        public TaskType getType()
        {
            return this;
        }

        /**
         * priority is represented by an integer value, ranging from 1 to 10
         * @param priority
         * @return whether the priority is valid or not
         */
        private static boolean validatePriority(int priority){
            if (priority < 1 || priority >10) return false;
            return true;
        }
    }




