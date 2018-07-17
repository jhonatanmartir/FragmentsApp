package com.dev.jhonyrg.fragmentsapp.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.jhonyrg.fragmentsapp.items.ToDo;
import com.dev.jhonyrg.fragmentsapp.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final int CURRENTLY = 10;
    private static final int DONE = 11;
    private static final int CRITICAL = 12;

    private List<ToDo> toDoList;
    private int idLayout;
    private OnItemClickListener itemClickListener;
    private OnItemLogClickListener itemLongListener;
    private Context context;

    public RecyclerViewAdapter(List<ToDo> toDo, int idLayout, OnItemClickListener itemClickListener, OnItemLogClickListener itemLongListener, Context context) {
        this.toDoList = toDo;
        this.idLayout = idLayout;
        this.itemClickListener = itemClickListener;
        this.itemLongListener = itemLongListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate layout item_view
        View view = LayoutInflater.from(this.context).inflate(idLayout, parent, false);

        //Create view Holder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setLongClickable(true);
        holder.bind(this.toDoList.get(position), itemClickListener, itemLongListener);
    }

    @Override
    public int getItemCount() {
        return this.toDoList.size();
    }

    //Listeners Interfaces
    public interface OnItemClickListener
    {
        void onItemClick(ToDo itemToDo, int position, View view);
    }

    public interface OnItemLogClickListener
    {
        void onItemLongClick(ToDo itemToDo, int position, View view);
    }

    //View Holder Class
    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txtvId)  TextView id;
        @BindView(R.id.txtvTitle)  TextView title;
        @BindView(R.id.txtvDescription)  TextView description;
        @BindView(R.id.txtvDate)  TextView date;
        @BindView(R.id.ibtnDelete) ImageButton delete;
        @BindView(R.id.imgStatus) ImageView status;
        @BindView(R.id.cvItem) CardView cardView;
        @BindView(R.id.lytDelete) LinearLayout layout;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final ToDo itemToDo, final OnItemClickListener listener, final OnItemLogClickListener longListener)
        {
            this.id.setText(String.valueOf(itemToDo.getId().toString()));
            this.title.setText(itemToDo.getTitle());
            this.description.setText(itemToDo.getDescription());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date dtt = dateFormat.parse(itemToDo.getDate());
                this.date.setText(String.valueOf(dtt.getDay() +"-"+ dtt.getMonth() +"-"+ dtt.getYear()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.date.setText(CustomDate.fromServerToLocal(itemToDo.getDate()));

            switch (itemToDo.getStatus())
            {
<<<<<<< HEAD
                case CURRENTLY:
                    Picasso.get().load(R.drawable.ic_todo_currently).into(this.status);
=======
                case WAIT:
                    this.status.setImageResource(R.drawable.calendar_clock);
>>>>>>> a2b44fabf08f73efd7fa31e9914e166951d68183
                    this.cardView.setCardBackgroundColor(itemView.getResources().getColor(R.color.backgroundWaitColor));
                    this.layout.setBackgroundResource(R.color.backgroundDeleteWaitColor);
                    this.delete.setBackgroundResource(R.color.backgroundDeleteWaitColor);
                    break;

                case DONE:
<<<<<<< HEAD
                    Picasso.get().load(R.drawable.ic_todo_done).into(this.status);
=======
                    this.status.setImageResource(R.drawable.calendar_check);
>>>>>>> a2b44fabf08f73efd7fa31e9914e166951d68183
                    this.cardView.setCardBackgroundColor(itemView.getResources().getColor(R.color.backgroundDoneColor));
                    this.layout.setBackgroundResource(R.color.backgroundDeleteDoneColor);
                    this.delete.setBackgroundResource(R.color.backgroundDeleteDoneColor);
                    break;

                case CRITICAL:
<<<<<<< HEAD
                    Picasso.get().load(R.drawable.ic_todo_critical).into(this.status);
=======
                    this.status.setImageResource(R.drawable.calendar_late);
>>>>>>> a2b44fabf08f73efd7fa31e9914e166951d68183
                    this.cardView.setCardBackgroundColor(itemView.getResources().getColor(R.color.backgroundCriticalColor));
                    this.layout.setBackgroundResource(R.color.backgroundDeleteCriticalColor);
                    this.delete.setBackgroundResource(R.color.backgroundDeleteCriticalColor);
                    break;
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(itemToDo, getAdapterPosition(), itemView);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longListener.onItemLongClick(itemToDo, getAdapterPosition(), itemView);
                    return true;
                }
            });
        }
    }

    public void setData(List<ToDo> list)
    {
        this.toDoList = list;
        this.notifyDataSetChanged();
    }

    public void setData()
    {
        this.notifyDataSetChanged();
    }
}