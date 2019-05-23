package todolist.model.task;

public enum TaskPriority {

    HIGH ("HIGH") {
        @Override
        public String toString() {
            return "HIGH";
        }
    },
    MEDIUM ("MEDIUM"){
        @Override
        public String toString() {
            return "MEDIUM";
        }
    },
    LOW ("LOW") {
        @Override
        public String toString() {
            return "LOW";
        }
    };

    TaskPriority(String priority) {

    }


}
