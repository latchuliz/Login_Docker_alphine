package com.neopharma.datavault.model.task;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TaskData {
    public Object lastKey;
    public Integer count;

    @SerializedName("results")
    public List<Task> tasks;

    public List<String> getTaskNames() {
        List<String> taskNames = new ArrayList<>();
        if (tasks != null)
            for (Task task : tasks) {
                taskNames.add(task.name);
            }
        return taskNames;
    }
}
