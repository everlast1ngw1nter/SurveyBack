package com.example.demo.dto;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;

public class StatisticOnQuestionBuilder {
    private Dictionary<String, Integer> amountSelectedByVariant;

    public StatisticOnQuestionBuilder(){
        amountSelectedByVariant = new Hashtable<>();
    }

    public void CountAnswer(String answer){
        if (amountSelectedByVariant.get(answer) != null){
            amountSelectedByVariant.put(answer, amountSelectedByVariant.get(answer)+1);
        } else {
            amountSelectedByVariant.put(answer, 1);
        }
    }


    public Dictionary<String, Integer> GetAnswerStatistic(){ return amountSelectedByVariant; }


}
