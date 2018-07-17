package com.dev.jhonyrg.fragmentsapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dev.jhonyrg.fragmentsapp.R;
import com.dev.jhonyrg.fragmentsapp.api.ApiClient;
import com.dev.jhonyrg.fragmentsapp.items.ToDo;
import com.dev.jhonyrg.fragmentsapp.items.ToDoList;
import com.dev.jhonyrg.fragmentsapp.utils.ApiCaller;
import com.dev.jhonyrg.fragmentsapp.utils.OnApiCallFinish;
import com.dev.jhonyrg.fragmentsapp.utils.RecyclerViewAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class ListFragment extends Fragment implements
        RecyclerViewAdapter.OnItemClickListener,
        RecyclerViewAdapter.OnItemLogClickListener,
        OnApiCallFinish, SwipeRefreshLayout.OnRefreshListener {

    private static final int CREATE = 1;
    private static final int UPDATE = 2;

    private RecyclerViewAdapter adapter;
    private LinearLayout layoutDelete;
    private ImageButton buttonDelete;

    @BindView(R.id.swpRefresh) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.rvToDo) RecyclerView rvToDo;

    private OnItemToDoListener mListener;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<ToDo> toDoList = new ArrayList<>();
        this.adapter = new RecyclerViewAdapter(toDoList, R.layout.item_view, this, this, getActivity());
        this.rvToDo.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvToDo.setHasFixedSize(true);

        this.refreshLayout.setOnRefreshListener(this);
        this.refreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.backgroundDeleteDoneColor),
                getResources().getColor(R.color.backgroundDeleteCriticalColor),
                getResources().getColor(R.color.backgroundDeleteWaitColor));

        SlideInDownAnimator animator = new SlideInDownAnimator();
        animator.setInterpolator(new OvershootInterpolator());
        rvToDo.setItemAnimator(animator);

        ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(this.adapter);
        alphaAdapter.setDuration(1000);
        alphaAdapter.setInterpolator(new OvershootInterpolator(1f));
        alphaAdapter.setFirstOnly(false);
        rvToDo.setAdapter(alphaAdapter);

        //code api
        this.fillList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemToDoListener) {
            mListener = (OnItemToDoListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnItemToDoResultListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSuccess(Integer id, String content) {
        switch (id)
        {
            case ApiClient.TASK_LIST:
                ToDoList toDoList = new Gson().fromJson(content, ToDoList.class);
                this.adapter.setData(toDoList.getData());
                break;

            case ApiClient.TASK_DELETE:
                Toast.makeText(getActivity(), "Deleted: " + content, Toast.LENGTH_SHORT).show();
                this.adapter.setData();
                break;
        }
    }

    @Override
    public void onError(Integer id, Integer code) {
        switch (code)
        {
            case 408:
                Toast.makeText(getContext(), "Retry please", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onItemClick(ToDo itemToDo, int position, View view) {
        layoutDelete = view.findViewById(R.id.lytDelete);
        buttonDelete = view.findViewById(R.id.ibtnDelete);

        if(layoutDelete.getVisibility() == View.VISIBLE)
        {
            layoutDelete.setVisibility(View.GONE);
        }
        else
        {
            mListener.onItemToDo(UPDATE, itemToDo.getId());
        }
    }

    @Override
    public void onItemLongClick(final ToDo itemToDo, int position, View view) {
        layoutDelete = view.findViewById(R.id.lytDelete);
        buttonDelete = view.findViewById(R.id.ibtnDelete);

        if(layoutDelete.getVisibility() == View.GONE)
        {
            layoutDelete.setVisibility(View.VISIBLE);
        }
        else
        {
            layoutDelete.setVisibility(View.GONE);
        }

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code api
                ApiCaller caller = ApiClient.setTaskDelete(ListFragment.this, itemToDo.getId().toString());
                caller.setContextActivity(getActivity());
                caller.execute();

                fillList();
            }
        });
    }

    @OnClick(R.id.fabAdd)
    public void ClickAction()
    {
        mListener.onItemToDo(CREATE, 0);
    }

    @Override
    public void onRefresh() {
        fillList();
        refreshLayout.setRefreshing(false);
    }

    private void fillList() {
        //code api
        ApiCaller caller = ApiClient.getTaskList(this);
        caller.setContextActivity(getActivity());
        caller.execute();
    }

    public interface OnItemToDoListener {
        void onItemToDo(int operation, Integer idToDo);
    }
}
