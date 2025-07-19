package com.challenge.salasia.article.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(OutputCaptureExtension.class)
class ArticleSchedulerTest {

  @Mock ArticleService articleService;
  @InjectMocks ArticleScheduler scheduler;

  @Test
  void scheduledFetchAndSaveArticles_shouldLogErrorWhenServiceFails(CapturedOutput output) {
    doThrow(new RuntimeException("Boom")).when(articleService).fetchAndSaveArticles();

    scheduler.scheduledFetchAndSaveArticles();

    assertThat(output.getOut() + output.getErr()).contains("Scheduled job failed");
  }
}
