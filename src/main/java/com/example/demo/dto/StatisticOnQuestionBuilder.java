package com.example.demo.dto;

import java.util.*;

public class StatisticOnQuestionBuilder {
    private Map<String, Integer> amountSelectedByVariant;

    public StatisticOnQuestionBuilder(){
        amountSelectedByVariant = new HashMap<>();
    }

    public void CountAnswer(String answer){
        if (amountSelectedByVariant.containsKey(answer)){
            amountSelectedByVariant.put(answer, amountSelectedByVariant.get(answer)+1);
        } else {
            amountSelectedByVariant.put(answer, 1);
        }
    }

    public Map<String, Integer> GetAnswerStatistic(){ return amountSelectedByVariant; }
}
