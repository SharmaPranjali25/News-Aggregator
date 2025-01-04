package org.zero.newsaggregator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zero.newsaggregator.document.ArticleDocument;
import org.zero.newsaggregator.service.ArticleService;
import org.zero.newsaggregator.service.NewsService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    private NewsService newsService;
    @Autowired
    private ArticleService articleService;

    @PostMapping ("/fetch")
    public String fetchNews() {
        return newsService.fetchAndProcessNews("technology");
    }
    @GetMapping
    public List<ArticleDocument> getArticles(
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate,
            @RequestParam(required = false) String source,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return articleService.getArticles(startDate, endDate, source, page, size);
    }

    // Retrieve a specific article by ID
    @GetMapping("/{id}")
    public Optional<ArticleDocument> getArticleById(@PathVariable String id) {
        return articleService.getArticleById(id);
    }

    // Fetch articles ready for sentiment analysis
    @GetMapping("/ready")
    public List<ArticleDocument> getArticlesReadyForSentimentAnalysis() {
        return articleService.getArticlesReadyForSentimentAnalysis();
    }
}
