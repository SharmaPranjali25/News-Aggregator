package org.zero.newsaggregator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.zero.newsaggregator.bo.Article;


@Service
public class NewsService {

    private static final String API_KEY = "69be7ef058ea42929a2293513dbf9de5"; // Your API key
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Autowired
    private ArticleService articleService;

    public NewsService(ObjectMapper objectMapper) {
        this.webClient = WebClient.builder().baseUrl("https://newsapi.org/v2").build();
        this.objectMapper = objectMapper;
    }

    public String fetchAndProcessNews(String topic) {
        try {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/everything")
                            .queryParam("q", topic)
                            .queryParam("apiKey", API_KEY)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode articlesNode = rootNode.path("articles");

            if (articlesNode.isArray()) {
                for (JsonNode node : articlesNode) {
                    Article article = objectMapper.treeToValue(node, Article.class);
                    articleService.saveOrUpdateArticle(article);
                }
            }

            return "Saved successfully";

        } catch (Exception e) {
            throw new RuntimeException("Error fetching news: " + e.getMessage(), e);
        }
    }


}
