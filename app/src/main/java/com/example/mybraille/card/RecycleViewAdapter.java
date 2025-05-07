package com.example.mybraille.card;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybraille.R;
import com.example.mybraille.ReadActivity;
import com.example.mybraille.dot.DotController;
import com.example.mybraille.sentence.Sentences;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    public char[] characterArray = Sentences.chooseRandomSentenceArray();

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView textView;

        public ViewHolder(View view)
        {
            super(view);
            textView = (TextView) view.findViewById(R.id.character);
        }

        public TextView getTextView()
        {
            return textView;
        }
    }

    @NonNull
    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);

        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter.ViewHolder holder, int position)
    {
        holder.getTextView().setText(characterArray[position] + "");


    }

    @Override
    public int getItemCount()
    {
        return characterArray.length;
    }

    public char[] getCharacterArray()
    {
        return characterArray;
    }

    public void setCharacterArray(char[] characters)
    {
        this.characterArray = characters;
    }
}
