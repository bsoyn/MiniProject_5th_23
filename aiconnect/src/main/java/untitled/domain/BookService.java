package untitled.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import untitled.domain.BookCover;
import untitled.domain.SummaryCreated;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Autowired
    private BookCoverRepository bookCoverRepository;

    @Autowired
    private BookSummaryRepository bookSummaryRepository;

    public void generateCover(SummaryCreated summaryCreated) {
        String prompt = String.format(
            "다음 책 내용을 바탕으로 어울리는 책 표지 일러스트를 생성해주세요. " +
            "책의 장르는 '%s'이고, 요약은 다음과 같습니다: \"%s\". " +
            "책 표지는 감성적이거나 상징적인 그림으로, 내용의 분위기와 주제를 잘 전달할 수 있도록 해주세요. " +
            "텍스트는 포함하지 말고 책 표지 이미지만 제공하세요.",
            summaryCreated.getCategory(),
            summaryCreated.getSummary()
        );

        try {
            String imageUrl = callImageGenerationAPI(apiKey, prompt);

            BookCover cover = new BookCover();
            cover.setManuscriptId(summaryCreated.getManuscriptId());
            cover.setImageUrl(imageUrl);
            BookCover savedCover =  bookCoverRepository.save(cover);

            CoverCreated coverCreated = new CoverCreated(savedCover);
            coverCreated.publishAfterCommit();

        } catch (Exception e) {
            throw new RuntimeException("커버 이미지 생성 실패", e);
        }
    }

    private String callImageGenerationAPI(String apiKey, String prompt) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                                    .connectTimeout(15, TimeUnit.SECONDS)
                                    .readTimeout(30, TimeUnit.SECONDS)
                                    .writeTimeout(15, TimeUnit.SECONDS)
                                    .build();

        MediaType mediaType = MediaType.parse("application/json");
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode requestBody = mapper.createObjectNode();
        requestBody.put("model", "dall-e-3");  
        requestBody.put("prompt", prompt);
        requestBody.put("size", "1024x1024");

        Request request = new Request.Builder()
            .url("https://api.openai.com/v1/images/generations")
            .post(RequestBody.create(mediaType, mapper.writeValueAsString(requestBody)))
            .addHeader("Authorization", "Bearer " + apiKey)
            .addHeader("Content-Type", "application/json")
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            JsonNode root = mapper.readTree(response.body().string());
            return root.get("data").get(0).get("url").asText();
        }
    }
    
    public void summaryBook(PublicationRequested event) {
        String manuscriptContent = event.getContent();

        String prompt =
            "다음은 사용자의 도서 원고입니다. 이 내용을 기반으로 도서 요약, 카테고리, 가격, 키워드를 생성하세요.\n" +
            "---\n" + manuscriptContent + "\n---\n" +
            "요약은 3문단 이내로 제공하고, 카테고리는 한글로 하나만, 가격은 숫자로(1000~20000 범위, 단위는 포함하지 말고 수치만), 키워드는 반드시 쉼표로 구분된 문자열로 주세요 (키워드 예: 우주,별,관측,탐사,신호)\n" +
            "이때 JSON을 매우 엄격하게 반환해야 하며, 절대로 마크다운 코드 블럭(```json`)을 포함하지 마세요. 반드시 순수한 JSON 텍스트만 응답하세요."  + " {\"summary\":\"...\", \"category\":\"...\", \"price\":10000, \"keywords\":\"키워드1,키워드2,...\"}";

        try {
            String response = callGPT(apiKey, prompt);
            // 응답 전처리 -> 백틱 부분 제거 
            if (response.startsWith("```")) {
                response = response.replaceAll("(?s)```json\\s*", "").replaceAll("```", "").trim();
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response);

            BookSummary bookSummary = new BookSummary();
            bookSummary.setManuscriptId(event.getId());
            bookSummary.setBookId(null); // 추후 도서 최종 등록 후에 업뎃 
            bookSummary.setSummary(node.get("summary").asText());
            bookSummary.setCategory(node.get("category").asText());
            bookSummary.setPrice(node.get("price").asInt());
            bookSummary.setKeywords(node.get("keywords").asText());

            bookSummaryRepository.save(bookSummary);

            SummaryCreated summaryCreated = new SummaryCreated(bookSummary);
            summaryCreated.publishAfterCommit();

        } catch (Exception e) {
            throw new RuntimeException("AI 요약 실패", e);
        }
    }


    private String callGPT(String apiKey, String prompt) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

        MediaType mediaType = MediaType.parse("application/json");
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode requestBody = mapper.createObjectNode();
        requestBody.put("model", "gpt-4o");

        ArrayNode messages = mapper.createArrayNode();

        ObjectNode systemMsg = mapper.createObjectNode();
        systemMsg.put("role", "system");
        systemMsg.put("content", "당신은 도서 편집 및 출판 전문가이며, 반드시 순수한 JSON 객체 형식만 응답해야 합니다. 불필요한 설명은 포함하지 마세요.");

        ObjectNode userMsg = mapper.createObjectNode();
        userMsg.put("role", "user");
        userMsg.put("content", prompt);

        messages.add(systemMsg);
        messages.add(userMsg);

        requestBody.set("messages", messages);

        String bodyJson = mapper.writeValueAsString(requestBody);

        Request request = new Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .post(RequestBody.create(mediaType, bodyJson))
            .addHeader("Authorization", "Bearer " + apiKey)
            .addHeader("Content-Type", "application/json")
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            JsonNode root = mapper.readTree(response.body().string());

            return root.get("choices").get(0).get("message").get("content").asText();
        }
    }




}
