package org.zero.newsaggregator.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private ZonedDateTime publishedAt;
    private String author;
    private String urlToImage;
    private String description;
    private Source source;
    private String title;
    private String url;
    private String content;
}
