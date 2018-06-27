package com.dev.jhonyrg.fragmentsapp.api;

import android.app.Activity;

import com.dev.jhonyrg.fragmentsapp.items.ToDo;
import com.dev.jhonyrg.fragmentsapp.utils.ApiCaller;
import com.dev.jhonyrg.fragmentsapp.utils.OnApiCallFinish;

import java.util.HashMap;

public class ApiClient {
    private static final String URL = "http://192.168.43.247:3000/api/task/{id}";
    public static final int TASK_LIST = 100;
    public static final int TASK_ITEM = 101;
    public static final int TASK_SAVE = 102;
    public static final int TASK_UPDATE = 103;
    public static final int TASK_DELETE = 104;
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String DATE = "date";
    public static final String STATUS = "status";

    private static Activity activity;

    public static ApiCaller getTaskList(OnApiCallFinish listener){
        ApiCaller caller = new ApiCaller();

        caller.setUrl(URL.replace("{id}", ""));
        caller.setRequestId(TASK_LIST);
        caller.setOnApiCallFinish(listener);
        caller.setContextActivity(activity);
        return caller;
    }

    public static ApiCaller getTaskItem(OnApiCallFinish listener, String id){
        ApiCaller caller = new ApiCaller();

        caller.setUrl(URL.replace("{id}", id));
        caller.setRequestId(TASK_ITEM);
        caller.setOnApiCallFinish(listener);
        return caller;
    }

    public static ApiCaller setTaskDelete(OnApiCallFinish listener, String id) {
        ApiCaller caller = new ApiCaller();

        caller.setUrl(URL.replace("{id}", id));
        caller.setPost(true);
        caller.setRequestId(TASK_DELETE);
        caller.setOnApiCallFinish(listener);
        return caller;
    }

    public static ApiCaller setTaskSave(OnApiCallFinish listener, ToDo item) {
        ApiCaller caller = new ApiCaller();
        HashMap<String, String> value = new HashMap<>();

        value.put(TITLE, item.getTitle());
        value.put(DESCRIPTION, item.getDescription());
        value.put(DATE, item.getDate());
        value.put(STATUS, item.getStatus().toString());

        caller.setUrl(URL.replace("{id}", ""));
        caller.setPost(true);
        caller.setValues(value);
        caller.setRequestId(TASK_SAVE);
        caller.setOnApiCallFinish(listener);
        return caller;
    }

    public static ApiCaller setTaskUpdate(OnApiCallFinish listener, ToDo item) {
        ApiCaller caller = new ApiCaller();
        HashMap<String, String> value = new HashMap<>();

        value.put(TITLE, item.getTitle());
        value.put(DESCRIPTION, item.getDescription());
        value.put(DATE, item.getDate());
        value.put(STATUS, item.getStatus().toString());

        caller.setUrl(URL.replace("{id}", String.valueOf(item.getId())));
        caller.setPost(true);
        caller.setValues(value);
        caller.setRequestId(TASK_UPDATE);
        caller.setOnApiCallFinish(listener);
        return caller;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
