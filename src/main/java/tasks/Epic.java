package main.java.tasks;

import main.java.service.Status;
import main.java.service.TaskType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    /*
Всем привет! а у вас у всех классы Task, Subtask и Epic не имеют ссылок друг на друга? Вчера жестко потратил 2ч на то что gson пытался сделать бесконечный паровозик из моих классов Subtask и Epic, так как у Epic есть массив Subtask'ов и у Subtask есть ссылка на Epic. Как я понял, gson пытался сериализовать Epic, в котором есть Subtask'и, а у него в свою очередь ссылка Epic, и тут начинается бесконечность ) только слово transient разорвало паровоз
 */
    private transient List<String> subtasksList;

    public Epic( TaskType taskType, String name, Status status, String description, List<String> subtasksList) {
        super(taskType, name, status, description);
        this.subtasksList = subtasksList;
    }

    public Epic (String id,  TaskType taskType, String name, Status status, String description) {
        super(id, taskType, name, status, description);
    }

    public List<String> getSubtasksList() {
        return subtasksList;
    }

    public void cleanSubtaskIds() {
        subtasksList.clear();
    }
    public void removeSubtask(String id) {
        subtasksList.remove(subtasksList.indexOf(id));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksList, epic.subtasksList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksList);
    }

    @Override
    public String toString() {
        return "Epic{" + "id=" + getId() + ", subtasksList=" + subtasksList + ", name='" + getName()
                + '\'' + ", description='" + getDescription() + '\'' + ", status='" + getStatus() + '\'' + '}';
    }

//    @Override
//    public String toCsvFormat() {
//        String result;
//        result = getId() + "," + getTaskType() + "," + getName() + "," + getStatus() + "," + getDescription();
//        return result;
//    }
}
