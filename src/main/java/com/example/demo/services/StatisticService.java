package com.example.demo.services;

import com.example.demo.DbService;
import com.example.demo.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StatisticService {
    private final AnswersDataService answersDataService;

    @Autowired
    public StatisticService(AnswersDataService answersDataService) {
        this.answersDataService = answersDataService;
    }

    public StatisticOnQuestionDto[] getStatisticOfSurvey(UUID surveyId) {
        var surveyData = answersDataService.getSurveyAndAnswers(surveyId);
        var questions = surveyData.surveyInfo();
        var answers = surveyData.answers();
        var statisticOfSurvey = new ArrayList<StatisticOnQuestionDto>();
        for (var indexQuestionAndAnswer = 0; indexQuestionAndAnswer < questions.survey.size(); indexQuestionAndAnswer++) {
            var question = questions.survey.get(indexQuestionAndAnswer);
            var answersOnQuestion = getAnswersOnCurrQuestion(answers, indexQuestionAndAnswer);
            var amountSelectedByVariant =  buildStatisticOnQuestion(answersOnQuestion).GetAnswerStatistic();
            var viewStatisticOnQuestion = getViewStatisticOnQuestion(amountSelectedByVariant, question);
            statisticOfSurvey.add(viewStatisticOnQuestion);
        }
        return statisticOfSurvey.toArray(new StatisticOnQuestionDto[0]);
    }

    public StatisticOnQuestionDto getViewStatisticOnQuestion(Map<String, Integer> amountSelectedByVariant,
                                                             QuestionsDto question) {
        var statisticsVariantDto = new ArrayList<StatisticVariantDto>();
        for (var variant : amountSelectedByVariant.keySet()) {
            statisticsVariantDto.add(new StatisticVariantDto(variant, amountSelectedByVariant.get(variant)));
        }
        return new StatisticOnQuestionDto(question.questionId, question.question, statisticsVariantDto);
    }

    public ArrayList<String> getAnswersOnCurrQuestion(List<AnswersDto> answers, Integer indexQuestion) {
        var answersOnCurrQuestion = new ArrayList<String>();
        for (AnswersDto answerPerson : answers) {
            answersOnCurrQuestion.add(answerPerson.answers.get(indexQuestion));
        }
        return answersOnCurrQuestion;
    }

    public StatisticOnQuestionBuilder buildStatisticOnQuestion(List<String> answers) {
        var statisticOnQuestion = new StatisticOnQuestionBuilder();
        for (var answer : answers) {
            var variants = answer.split("\n");
            for (var variant : variants)
                statisticOnQuestion.CountAnswer(variant);
        }
        return statisticOnQuestion;
    }
}
