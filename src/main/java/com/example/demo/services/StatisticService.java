package com.example.demo.services;

import com.example.demo.DbService;
import com.example.demo.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.UUID;

@Service
public class StatisticService {
    private final AnswersDataService answersDataService;

    @Autowired
    public StatisticService(AnswersDataService answersDataService) {
        this.answersDataService = answersDataService;
    }

    public StatisticOnQuestionDto[] GetStatisticOfSurvey(UUID surveyId) {
        var surveyData = answersDataService.getSurveyAndAnswers(surveyId);
        var questions = surveyData.surveyInfo();
        var answers = surveyData.answers();
        var statisticOfSurvey = new ArrayList<StatisticOnQuestionDto>();
        for (var indexQuestionAndAnswer = 0; indexQuestionAndAnswer < questions.survey.size(); indexQuestionAndAnswer++) {
            var question = questions.survey.get(indexQuestionAndAnswer);
            var answersOnQuestion = GetAnswersOnCurrQuestion(answers, indexQuestionAndAnswer);
            var amountSelectedByVariant =  BuildStatisticOnQuestion(answersOnQuestion).GetAnswerStatistic();
            var viewStatisticOnQuestion = GetViewStatisticOnQuestion(amountSelectedByVariant, question);
            statisticOfSurvey.add(viewStatisticOnQuestion);
        }
        return statisticOfSurvey.toArray(new StatisticOnQuestionDto[0]);
    }

    public StatisticOnQuestionDto GetViewStatisticOnQuestion(Dictionary<String, Integer> amountSelectedByVariant,
                                                             QuestionsDto question) {
        var statisticsVariantDto = new ArrayList<StatisticVariantDto>();
        for (var iteratorAnswers = amountSelectedByVariant.keys().asIterator(); iteratorAnswers.hasNext(); ) {
            var answer = iteratorAnswers.next();
            statisticsVariantDto.add(new StatisticVariantDto(answer, amountSelectedByVariant.get(answer)));
        }
        return new StatisticOnQuestionDto(question.questionId, question.question, statisticsVariantDto);
    }

    public ArrayList<String> GetAnswersOnCurrQuestion(List<AnswersDto> answers, Integer indexQuestion) {
        var answersOnCurrQuestion = new ArrayList<String>();
        for (AnswersDto answerPerson : answers) {
            answersOnCurrQuestion.add(answerPerson.answers.get(indexQuestion));
        }
        return answersOnCurrQuestion;
    }

    public StatisticOnQuestionBuilder BuildStatisticOnQuestion(List<String> answers) {
        var statisticOnQuestion = new StatisticOnQuestionBuilder();
        for (var answer : answers) {
            var variants = answer.split("\n");
            for (var variant : variants)
                statisticOnQuestion.CountAnswer(variant);
        }
        return statisticOnQuestion;
    }
}
