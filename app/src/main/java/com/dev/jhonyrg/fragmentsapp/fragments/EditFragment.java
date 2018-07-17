package com.dev.jhonyrg.fragmentsapp.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dev.jhonyrg.fragmentsapp.R;
import com.dev.jhonyrg.fragmentsapp.api.ApiClient;
import com.dev.jhonyrg.fragmentsapp.items.Response;
import com.dev.jhonyrg.fragmentsapp.items.ToDo;
import com.dev.jhonyrg.fragmentsapp.utils.ApiCaller;
import com.dev.jhonyrg.fragmentsapp.utils.CustomDate;
import com.dev.jhonyrg.fragmentsapp.utils.OnApiCallFinish;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;

public class EditFragment extends Fragment implements OnApiCallFinish{
    private OnItemToDoResultListener mListener;

    private static final int CREATE = 1;
    private static final int UPDATE = 2;
    private static final String REG_ID = "id";
    private static final String OPERATION = "operation";
    private static final int CURRENTLY = 10;
    private static final int DONE = 11;
    private static final int CRITICAL = 12;
    private static final String TAG = "EditActivity";

    @NotEmpty(messageId = R.string.error, order = 1)
    @MinLength(value = 3, messageId = R.string.error, order = 2)
    @BindView(R.id.etxtTitle) MaterialEditText title;

    @NotEmpty(messageId = R.string.error, order = 1)
    @MinLength(value = 4, messageId = R.string.error, order = 2)
    @BindView(R.id.etxtDescription) MaterialEditText description;

    @NotEmpty(messageId = R.string.error, order = 1)
    @MinLength(value = 4, messageId = R.string.error, order = 2)
    @BindView(R.id.etxtDate) MaterialEditText date;

    @BindView(R.id.rbWait) RadioButton statusWait;
    @BindView(R.id.rbDone) RadioButton statusDone;
    @BindView(R.id.rbCritical) RadioButton statusCritical;
    @BindView(R.id.rbgStatus) RadioGroup statusGroup;

    private SimpleDateFormat localFormat = new SimpleDateFormat("dd/MM/yyyy");
    private int action;
    private Integer registerId;
    private CustomDate formatterDate;
    private Date myDate;
    private String dateServer;
    private android.support.v7.app.ActionBar actionBar;

    public EditFragment() {
        // Required empty public constructor
    }

    public static EditFragment newInstance(int operation, Integer idToDo) {
        EditFragment editFragment = new EditFragment();
        Bundle args = new Bundle();
        args.putInt(EditFragment.OPERATION, operation);
        args.putInt(EditFragment.REG_ID, idToDo);
        editFragment.setArguments(args);
        return editFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.formatterDate = new CustomDate();

        this.action = getArguments() != null ? getArguments().getInt(OPERATION, 0) : 0;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if(actionBar != null)
        {
            this.actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(this.action == UPDATE)
        {
            this.statusGroup.setVisibility(View.VISIBLE);
            this.registerId = getArguments() != null ? getArguments().getInt(REG_ID, 0) : 0;

            //api code
            ApiCaller caller = ApiClient.getTaskItem(this, String.valueOf(this.registerId));
            caller.setContextActivity(getActivity());
            caller.execute();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemToDoResultListener) {
            mListener = (OnItemToDoResultListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnItemToDoResultListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if(actionBar != null)
        {
            this.actionBar.setDisplayHomeAsUpEnabled(false);
        }
        //((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mListener = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(Integer id, String content) {
        switch (id)
        {
            case ApiClient.TASK_ITEM:
                ToDo toDo = new Gson().fromJson(content, ToDo.class);
                this.title.setText(toDo.getTitle());
                this.description.setText(toDo.getDescription());
                this.date.setText(CustomDate.fromServerToLocal(toDo.getDate()));
                this.dateServer = toDo.getDate();
                //this.status = toDo.getStatus();
                switch (toDo.getStatus())
                {
                    case CURRENTLY:
                        this.statusWait.setChecked(true);
                        break;

                    case DONE:
                        this.statusDone.setChecked(true);
                        break;

                    case CRITICAL:
                        this.statusCritical.setChecked(true);
                        break;
                }
                break;

            case ApiClient.TASK_SAVE:
                Response responseSave = new Gson().fromJson(content, Response.class);
                Toast.makeText(getActivity(), ""+responseSave.getMessage(), Toast.LENGTH_SHORT).show();
                break;

            case ApiClient.TASK_UPDATE:
                Response response = new Gson().fromJson(content, Response.class);
                Toast.makeText(getActivity(), ""+response.getMessage(), Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onError(Integer id, Integer code) {
        switch (code)
        {
            case 408:
                Toast.makeText(getActivity(), "Retry please", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @OnClick(R.id.ibtnDate)
    public void clickPicker()
    {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int actualMonth = month +1;
                String dayFormat = (dayOfMonth < 10)? 0 + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String monthFormat = (actualMonth < 10)? 0 + String.valueOf(actualMonth):String.valueOf(actualMonth);

                String dateString = dayFormat + "/" + monthFormat + "/" + year;
                try {
                    myDate = new Date();
                    myDate = localFormat.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                date.setText(String.valueOf(formatterDate.fromDateToLocal(myDate)));
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @OnClick(R.id.btnSave)
    public void clickSave()
    {
        if(FormValidator.validate(this, new SimpleErrorPopupCallback(getContext())))
        {
            try
            {
                switch (action)
                {
                    case CREATE:
                        Insert();
                        break;

                    case UPDATE:
                        Update();
                        //close fragment
                        getActivity().getFragmentManager().popBackStack();
                        break;
                }
            }
            catch (SQLException ex)
            {
                Log.e(TAG, ex.getMessage());
            }
        }
    }

    private void Insert()
    {
        ToDo toDo = new ToDo();
        toDo.setTitle(title.getText().toString());
        toDo.setDescription(description.getText().toString());
        toDo.setDate(CustomDate.fromDateToServer(this.myDate));
        toDo.setStatus(CURRENTLY);

        //code api
        ApiCaller caller = ApiClient.setTaskSave(this, toDo);
        caller.setContextActivity(getActivity());
        caller.execute();

        title.setText("");
        description.setText("");
        date.setText("");
        title.requestFocus();

        mListener.onToDoItemResult(Activity.RESULT_OK);
    }

    private void Update()
    {
        ToDo toDo = new ToDo();

        toDo.setId(this.registerId);
        toDo.setTitle(title.getText().toString());
        toDo.setDescription(description.getText().toString());
        if(myDate != null)
        {
            toDo.setDate(CustomDate.fromDateToServer(myDate));
        }
        else
        {
            toDo.setDate(this.dateServer);
        }


        if(statusWait.isChecked())
        {
            toDo.setStatus(CURRENTLY);
        }
        else if(statusDone.isChecked())
        {
            toDo.setStatus(DONE);
        }
        else if(statusCritical.isChecked())
        {
            toDo.setStatus(CRITICAL);
        }

        //code api
        ApiCaller caller = ApiClient.setTaskUpdate(this, toDo);
        caller.setContextActivity(getActivity());
        caller.execute();

        mListener.onToDoItemResult(Activity.RESULT_OK);
    }

    public interface OnItemToDoResultListener {
        void onToDoItemResult(Integer resultCode);
    }
}
