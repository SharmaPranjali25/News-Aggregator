package org.zero.newsaggregator.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.zero.newsaggregator.document.ArticleDocument;

import java.util.Optional;

public interface ArticleDocumentRepository extends MongoRepository<ArticleDocument, String> {
    Optional<ArticleDocument> findByTitle(String title); // Custom query method
}