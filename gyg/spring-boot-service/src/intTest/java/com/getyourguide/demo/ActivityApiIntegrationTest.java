package com.getyourguide.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

public class ActivityApiIntegrationTest extends IntegrationTest {
    @Autowired
    private WebTestClient webClient;

    @Test
    public void testActivitySearchWithEmptyQuery() {
        webClient.get().uri(
                        it -> it.path("/activity/search")
                                .queryParam("query", "")
                                .queryParam("page", 1)
                                .queryParam("size", 10)
                                .build()
                )
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.content").isNotEmpty()
                .jsonPath("$.content.size()").isEqualTo(10)
                .jsonPath("$.totalHits").isEqualTo(14)
                .jsonPath("$.totalPages").isEqualTo(2)
                .jsonPath("$.currentPage").isEqualTo(1)
                .jsonPath("$.pageSize").isEqualTo(10)
                .jsonPath("$.hasPrevious").isEqualTo(false)
                .jsonPath("$.hasNext").isEqualTo(true);
    }

    @Test
    public void testActivitySearchWithQuery() {
        webClient.get().uri(
                        it -> it.path("/activity/search")
                                .queryParam("query", "berlin")
                                .queryParam("page", 1)
                                .queryParam("size", 10)
                                .build()
                )
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.content").isNotEmpty()
                .jsonPath("$.content.size()").isEqualTo(8)
                .jsonPath("$.totalHits").isEqualTo(8)
                .jsonPath("$.totalPages").isEqualTo(1)
                .jsonPath("$.currentPage").isEqualTo(1)
                .jsonPath("$.pageSize").isEqualTo(8)
                .jsonPath("$.hasPrevious").isEqualTo(false)
                .jsonPath("$.hasNext").isEqualTo(false)
        ;
    }

    @Test
    public void testActivitySearchWithPagination() {
        webClient.get().uri(
                        it -> it.path("/activity/search")
                                .queryParam("query", "berlin")
                                .queryParam("page", 1)
                                .queryParam("size", 5)
                                .build()
                )
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.content").isNotEmpty()
                .jsonPath("$.content.size()").isEqualTo(5)
                .jsonPath("$.totalHits").isEqualTo(8)
                .jsonPath("$.totalPages").isEqualTo(2)
                .jsonPath("$.currentPage").isEqualTo(1)
                .jsonPath("$.pageSize").isEqualTo(5)
                .jsonPath("$.hasPrevious").isEqualTo(false)
                .jsonPath("$.hasNext").isEqualTo(true)
        ;
    }

    @Test
    public void testActivitySearchWithNextPage() {
        webClient.get().uri(
                        it -> it.path("/activity/search")
                                .queryParam("query", "berlin")
                                .queryParam("page", 2)
                                .queryParam("size", 5)
                                .build()
                )
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.content").isNotEmpty()
                .jsonPath("$.content.size()").isEqualTo(3)
                .jsonPath("$.totalHits").isEqualTo(8)
                .jsonPath("$.totalPages").isEqualTo(2)
                .jsonPath("$.currentPage").isEqualTo(2)
                .jsonPath("$.pageSize").isEqualTo(3)
                .jsonPath("$.hasPrevious").isEqualTo(true)
                .jsonPath("$.hasNext").isEqualTo(false)
        ;
    }

    @Test
    public void testActivitySearchWithBadInput() {
        webClient.get().uri(
                        it -> it.path("/activity/search")
                                .queryParam("query", "berlin")
                                .queryParam("page", -1)
                                .queryParam("size", 5)
                                .build()
                )
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("$.page").isEqualTo("Page number should not be less than 1")
        ;
    }


}
