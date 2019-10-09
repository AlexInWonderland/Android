package com.wistron.recyclerviewexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> implements Filterable {

    private ArrayList<ExampleItem> mExampleList;
    private ArrayList<ExampleItem> mExampleListFull;

    public class ExampleViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
        }
    }

    public ExampleAdapter(ArrayList<ExampleItem> arrayList){
        mExampleList = arrayList;
        mExampleListFull = new ArrayList<>(mExampleList);
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getmText1());
        holder.mTextView2.setText(currentItem.getText2());

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ExampleItem> filterList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filterList.addAll(mExampleListFull);
            }
            String pattern = charSequence.toString().toLowerCase().trim();
            for(ExampleItem item : mExampleListFull){
                if(item.getmText1().toLowerCase().contains(pattern)){
                    filterList.add(item);
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mExampleList.clear();
            mExampleList.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };
}
