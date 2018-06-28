package com.dev.jhonyrg.fragmentsapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ListFragment extends Fragment implements
        RecyclerViewAdapter.OnItemClickListener,
        RecyclerViewAdapter.OnItemLogClickListener,
        OnApiCallFinish {

    private static final String TAG = "MainActivity";
    static final int TODO_REQUEST = 11;
    private RecyclerViewAdapter adapter;
    LinearLayout layoutDelete;
    ImageButton buttonDelete;

    @BindView(R.id.rvToDo)RecyclerView rvToDo;
    @BindView(R.id.fabAdd)FloatingActionButton fabAdd;

    private OnFragmentInteractionListener mListener;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
        this.rvToDo.setAdapter(this.adapter);
        this.rvToDo.setItemAnimator(new DefaultItemAnimator());

        //code api
        this.fillList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
            //second fragment, coming soon
        }
    }

    @Override
    public void onItemLongClick(final ToDo itemToDo, int position, View view) {
        
    }

    @OnClick(R.id.fabAdd)
    public void ClickAction()
    {
        //second fragment, coming soon
    }

    private void fillList() {
        //code api
        ApiCaller caller = ApiClient.getTaskList(this);
        caller.setContextActivity(getActivity());
        caller.execute();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
