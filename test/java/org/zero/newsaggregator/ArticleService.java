package org.zero.newsaggregator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zero.newsaggregator.bo.Article;
import org.zero.newsaggregator.bo.Source;
import org.zero.newsaggregator.document.ArticleDocument;
import org.zero.newsaggregator.repository.ArticleDocumentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    @Autowired
    private ArticleDocumentRepository articleRepository;

    public ArticleDocument saveOrUpdateArticle(Article article) {
        // Find existing article by title
        Optional<ArticleDocument> existingArticle = articleRepository.findByTitle(article.getTitle());
        ArticleDocument articleDocument = existingArticle.orElseGet(ArticleDocument::new);
        if (article.getPublishedAt() != null) {
            long milliseconds = article.getPublishedAt().toInstant().toEpochMilli();
            articleDocument.setPublishedAt(milliseconds);
        } else {
            articleDocument.setPublishedAt(null);
        }

        articleDocument.setAuthor(article.getAuthor());
        articleDocument.setUrlToImage(article.getUrlToImage());
        articleDocument.setDescription(article.getDescription());
        Source source = article.getSource();
        articleDocument.setSource(new ArticleDocument.Source(source.getId(), source.getName()));
        articleDocument.setUrl(article.getUrl());
        articleDocument.setContent(cleanArticleContent(article.getContent()));
        return articleRepository.save(articleDocument); // Save Or Update
    }

    public List<ArticleDocument> getArticles(Long startDate, Long endDate, String source, int page, int size) {
        List<ArticleDocument> articles = articleRepository.findAll();  // You can add more filtering logic here
        return articles.stream()
                .filter(article -> (startDate == null || article.getPublishedAt() >= startDate) &&
                        (endDate == null || article.getPublishedAt() <= endDate) &&
                        (source == null || (article.getSource() !=null && article.getSource().getName().equalsIgnoreCase(source))))
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    // Retrieve a specific article by ID
    public Optional<ArticleDocument> getArticleById(String id) {
        return articleRepository.findById(id);
    }

    // Fetch articles ready for sentiment analysis (example: filter based on some condition)
    public List<ArticleDocument> getArticlesReadyForSentimentAnalysis() {
        return articleRepository.findAll().stream()
                .filter(article -> article.getContent() != null && !article.getContent().isEmpty()) // Example condition
                .map(article -> {
                    article.setContent(cleanArticleContent(article.getContent()));
                    return article;
                })
                .collect(Collectors.toList());
    }

    public static String cleanArticleContent(String content) {
        // Step 1: Remove HTML tags
        String withoutHtmlTags = content.replaceAll("<[^>]*>", "");

        // Step 2: Remove special characters (except spaces, letters, and numbers)
        String withoutSpecialChars = withoutHtmlTags.replaceAll("[^a-zA-Z0-9\\s]", "");

        // Step 3: Trim whitespace and replace multiple spaces with a single space
        String cleanedContent = withoutSpecialChars.replaceAll("\\s+", " ").trim();

        // Step 4: Convert text to lowercase
        cleanedContent = cleanedContent.toLowerCase();

        return cleanedContent;
    }
}
