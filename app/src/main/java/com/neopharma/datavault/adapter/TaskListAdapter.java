package com.neopharma.datavault.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neopharma.datavault.BR;
import com.neopharma.datavault.R;
import com.neopharma.datavault.databinding.AdapterTaskListBinding;
import com.neopharma.datavault.model.task.Task;

import java.util.List;


public class TaskListAdapter extends RecyclerView.Adapter {

    private List<Task> tasks;
    private ViewTaskClick viewTaskClick;

    public TaskListAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setOnClickListener(ViewTaskClick viewTaskClick) {
        this.viewTaskClick = viewTaskClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_task_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        ViewHolder holder = ((ViewHolder) viewHolder);
        holder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public interface ViewTaskClick {
        void onDonorClickListener(View view, int position, Task task, boolean b);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AdapterTaskListBinding binding;

        public ViewHolder(AdapterTaskListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Task task) {
            binding.donorLyt.setOnClickListener(view -> viewTaskClick.onDonorClickListener(view, getAdapterPosition(), task, false));
            binding.taskIcon.setOnClickListener(view -> viewTaskClick.onDonorClickListener(view, getAdapterPosition(), task, true));
            binding.setVariable(BR.taskData, task);
            binding.executePendingBindings();
        }
    }
}
