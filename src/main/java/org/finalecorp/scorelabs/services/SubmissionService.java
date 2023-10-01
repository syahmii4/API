package org.finalecorp.scorelabs.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.finalecorp.scorelabs.models.Assignment;
import org.finalecorp.scorelabs.models.Submission;
import org.finalecorp.scorelabs.repositories.AssignmentRepository;
import org.finalecorp.scorelabs.repositories.SubmissionRepository;
import org.finalecorp.scorelabs.requestObjects.NLPAnswerChecking;
import org.finalecorp.scorelabs.responseObjects.NLPAnswerGrade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SubmissionService {
    public SubmissionRepository submissionRepository;
    public AssignmentRepository assignmentRepository;

    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository, AssignmentRepository assignmentRepository){
        this.submissionRepository=submissionRepository;
        this.assignmentRepository=assignmentRepository;
    }

    public Map<Object, Object> checkAnswers(Map<Object, Object> submission, int studentId){
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode requestNode = objectMapper.valueToTree(submission);

        ArrayNode studentAnswers = objectMapper.createObjectNode().putArray("answers");

        Assignment assignment =  assignmentRepository.findAssignmentByAssignmentId(requestNode.get("assignmentId").asInt());
        Map<Object, Object> questions = assignment.getQuestion();

        JsonNode node = objectMapper.valueToTree(questions);
        List<List<String>> ansExpectedList = new ArrayList<>();

        ArrayNode nodeArray = objectMapper.createObjectNode().putArray("questions");
        node.get("questions").forEach((obj) -> {
            List<String> tempSchArray = new ArrayList<>();
            obj.get("schemeAnswer").forEach((n) -> {
                tempSchArray.add(n.asText());
            });
            ansExpectedList.add(tempSchArray);
        });

        List<NLPAnswerChecking> answerCheckRequest = new ArrayList<>();
        AtomicInteger index = new AtomicInteger();
        requestNode.get("answers").forEach((obj)->{
            NLPAnswerChecking n = new NLPAnswerChecking();
            List<String> ansList = new ArrayList<>();
            for(JsonNode answersGivenObj : obj.get("answersGiven")){
                ansList.add(answersGivenObj.textValue());
            }
            n.setAnswersGiven(ansList);
            System.out.println("index 0 : " + ansExpectedList.get(0));
            n.setAnswersAccepted(ansExpectedList.get(index.get()));
            n.setAcceptedThreshold(8);
            answerCheckRequest.add(n);
            index.getAndIncrement();
        });

        List<List<NLPAnswerGrade>> gradeList = new ArrayList<>();

        answerCheckRequest.forEach((obj) -> {
            try {
                List<NLPAnswerGrade> grade = gradeAnswer(obj);
                gradeList.add(grade);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Map<Object, Object> resMap = new HashMap<>();
        resMap.put("answers", gradeList);

        Submission submissionObj = new Submission();

        submissionObj.setAssignmentId(assignment.getAssignmentId());
        submissionObj.setAnswer(resMap);
        submissionObj.setPoints(0);
        submissionObj.setStudentId(studentId);
        submissionRepository.save(submissionObj);

        return resMap;
    }

    private List<NLPAnswerGrade> gradeAnswer(NLPAnswerChecking checking) throws IOException {
        URL url = new URL("http://127.0.0.1:8000/nlp/api/v1/check");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept", "application/json");

        httpURLConnection.setDoOutput(true);

        ObjectMapper objectMapper = new ObjectMapper();

        String body = objectMapper.writeValueAsString(checking);

        System.out.println(body);
        String resString;

        try(OutputStream os = httpURLConnection.getOutputStream()) {
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            resString = response.toString();
        }

        JsonNode node = objectMapper.readTree(resString);

        return objectMapper.readValue(node.get("response").traverse(),  new TypeReference<List<NLPAnswerGrade>>(){});
    }
}
