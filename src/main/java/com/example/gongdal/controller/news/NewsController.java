package com.example.gongdal.controller.news;

import com.example.gongdal.config.exception.response.ResponseService;
import com.example.gongdal.config.exception.response.SingleResult;
import com.example.gongdal.config.swagger.ErrorCodeExamGenerator;
import com.example.gongdal.controller.news.response.GetNewsResponse;
import com.example.gongdal.controller.news.response.NewsListResponse;
import com.example.gongdal.service.news.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.example.gongdal.config.exception.code.ErrorResponseCode.NOT_FOUND_NEWS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/news")
@Slf4j
@Tag(name = "6.1 공지사항")
public class NewsController {
    private final ResponseService responseService;
    private final NewsService newsService;

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {})
    @Operation(summary = "공지사항 리스트 조회")
    public SingleResult<Page<NewsListResponse>> getNewsList(@PageableDefault(page = 0, size = 10)
                                        @Parameter(hidden = true) Pageable pageable) {
        log.info("[NewsController] getNewsList");

        return responseService.getSingleResult(newsService.getList(pageable).map(NewsListResponse::toRes));
    }

    @GetMapping("/detail/{newsId}")
    @ResponseStatus(HttpStatus.OK)
    @ErrorCodeExamGenerator(value = {NOT_FOUND_NEWS})
    @Operation(summary = "공지사항 세부정보 조회")
    public SingleResult<GetNewsResponse> getNewsDetail(@PathVariable("newsId") long newsId) {
        log.info("[NewsController] getNewsDetail - newsId: {}", newsId);

        return responseService.getSingleResult(GetNewsResponse.toRes(newsService.getDetail(newsId)));
    }
}
