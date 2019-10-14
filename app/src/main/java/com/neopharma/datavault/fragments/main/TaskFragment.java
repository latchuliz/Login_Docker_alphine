package com.neopharma.datavault.fragments.main;

import android.app.Dialog;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.neopharma.datavault.R;
import com.neopharma.datavault.adapter.TaskListAdapter;
import com.neopharma.datavault.databinding.FragmentTaskBinding;
import com.neopharma.datavault.fragments.viewmodel.DonorHistoryViewModel;
import com.neopharma.datavault.fragments.viewmodel.TaskViewModel;
import com.neopharma.datavault.model.Constants;
import com.neopharma.datavault.model.Constants.FragmentType;
import com.neopharma.datavault.model.response.TaskResponse;
import com.neopharma.datavault.model.task.Task;
import com.neopharma.datavault.utility.Store;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TaskFragment extends MainFragment<FragmentTaskBinding> {

    @Inject
    public TaskViewModel taskViewModel;

    @Inject
    public DonorHistoryViewModel viewModel;

    private List<Task> tasks = new ArrayList<>();
    private TaskListAdapter taskListAdapter;

    @Override
    public int layoutId() {
        return R.layout.fragment_task;
    }

    @Override
    protected String getToolbarTitle() {
        return FragmentType.TASKS;
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.menuSelected.post(FragmentType.TASKS);
        showBackIcon();
        pendingDonorLive();
        taskList();
        fetchAdminTasks();
        fetchSupervisorTasks();
        radioTaskListener();
        if (Constants.ENFORCEMENT_OFFICER.equals(Store.userRole)) {
            getViewDataBinding().getDonorsEnf.setVisibility(View.VISIBLE);
            getViewDataBinding().getDonorsEnf.setOnClickListener(v -> {
                activity.action_TaskFragment_to_DonorListFragment(null, false);
            });
        }
    }

    private void radioTaskListener() {
        getViewDataBinding().radioTask.setOnCheckedChangeListener((group, checkedId) -> {
            fetchAdminTasks();
            fetchSupervisorTasks();
        });
    }

    private void pendingDonorLive() {
        viewModel.getPendingDonorsLive().observe(this, res -> {
            if (res == null || res.isEmpty()) {
                hideToolBarItem();
            } else {
                showToolbarSync();
            }
        });
    }

    private void fetchSupervisorTasks() {
        if (getViewDataBinding().radioSupervisor.isChecked()) {
            activity.showProgressBar();
            taskViewModel.getSupervisorTasks().observe(this, res -> {
                if (activity.validResponse(res)) {
                    notifyAdapterChange(res);
                }
                activity.hideProgressBar();
            });
        }
    }

    private void fetchAdminTasks() {
        if (getViewDataBinding().radioAdmin.isChecked()) {
            activity.showProgressBar();
            taskViewModel.getAdminTasks().observe(this, res -> {
                if (activity.validResponse(res)) {
                    notifyAdapterChange(res);
                }
                activity.hideProgressBar();
            });
        }
    }

    private void notifyAdapterChange(TaskResponse res) {
        if (res.data.tasks.isEmpty()) {
            getViewDataBinding().noRecord.setVisibility(View.VISIBLE);
            getViewDataBinding().taskList.setVisibility(View.GONE);
            getViewDataBinding().titleLyt.setVisibility(View.GONE);
            getViewDataBinding().radioTask.setVisibility(Constants.SUPERVISOR.equals(Store.userRole) ? View.VISIBLE : View.GONE);
        } else {
            getViewDataBinding().noRecord.setVisibility(View.GONE);
            getViewDataBinding().taskList.setVisibility(View.VISIBLE);
            getViewDataBinding().titleLyt.setVisibility(View.VISIBLE);
            getViewDataBinding().radioTask.setVisibility(Constants.SUPERVISOR.equals(Store.userRole) ? View.VISIBLE : View.GONE);
            tasks.clear();
            tasks.addAll(res.data.tasks);
            taskListAdapter.notifyDataSetChanged();
        }
    }

    private void taskList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        getViewDataBinding().taskList.setLayoutManager(layoutManager);

        taskListAdapter = new TaskListAdapter(tasks);
        getViewDataBinding().taskList.setAdapter(taskListAdapter);

        taskListAdapter.setOnClickListener((view, position, task, hasDonor) -> {
            if (hasDonor) {
                Bundle b = new Bundle();
                b.putString(Constants.TASK, task.id);
                b.putString(Constants.TASK_NAME, task.name);
                activity.action_TaskFragment_to_DonorListFragment(b, false);
            } else {
                taskDialog(task);
            }
        });
    }

    public void taskDialog(Task task) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_task);
        dialog.setCancelable(false);
        TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
        TextView dialogDescription = dialog.findViewById(R.id.dialog_description);
        ImageView dialogClose = dialog.findViewById(R.id.dialog_close);
        dialogTitle.setText(task.name);
        dialogDescription.setText(task.description);
        dialogClose.setOnClickListener(view -> dialog.cancel());
        dialog.show();
    }

}
