package com.example.android.life.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.life.databinding.PersonBinding;
import com.example.android.life.pojos.PersonField;

import java.util.List;

public class LifeAdapter extends RecyclerView.Adapter<LifeAdapter.PersonViewHolder> {

    private List<PersonField> mPeople;

    public LifeAdapter(){}

    public LifeAdapter(List<PersonField> people){
        mPeople = people;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PersonBinding binding = PersonBinding.inflate(inflater, parent, false);
        return new PersonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        PersonField person = mPeople.get(position);
        holder.bind(person);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull PersonViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }
    @Override
    public boolean onFailedToRecycleView(@NonNull PersonViewHolder holder) {
        holder.itemView.clearAnimation();
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public int getItemCount() {
        return mPeople != null ? mPeople.size() : 0;
    }

    public void setPeople(List<PersonField> people){
        mPeople = people;
        notifyDataSetChanged();
    }

    class PersonViewHolder extends RecyclerView.ViewHolder{


        private PersonBinding mBinding;

        PersonViewHolder(PersonBinding binding){
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(PersonField person){
            mBinding.setPerson(person);
            mBinding.executePendingBindings();
        }

        void unbind(){
            if(mBinding!=null){
                mBinding.unbind();
            }
        }

    }
}
