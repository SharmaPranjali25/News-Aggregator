package org.zero.newsaggregator.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Indexed;
import org.zero.newsaggregator.bo.Source;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "articles")
@CompoundIndex(def = "{'title': 1}", name = "title_unique", unique = true)  // Enforcing unique constraint on title
public class ArticleDocument {
    @Id
    private String id;
    private String title;
    private Long publishedAt;
    private String author;
    private String urlToImage;
    private String description;
    private Source source;
    private String url;
    private String content;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Source {
        private String id;
        private String name;
    }
}
