package com.example.gongdal.config.swagger;


import com.example.gongdal.config.exception.code.ErrorResponseCode;
import com.example.gongdal.config.exception.response.ResponseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final ResponseService responseService;
    private final String securityKey = "Authorization";

    @Bean
    public OpenAPI openAPI() {
        Server local = new Server();
        local.setUrl("http://localhost:80");
        Server dev = new Server();
        dev.setUrl("http://43.203.238.18:80");

        SecurityRequirement addSecurityItem = new SecurityRequirement();
        addSecurityItem.addList(this.securityKey);

        return
            new OpenAPI()
                .servers(List.of(local, dev))
                // Security 인증 컴포넌트 설정
                .components(authSetting())
                // API 마다 Security 인증 컴포넌트 설정
                .addSecurityItem(addSecurityItem)
                .info(swaggerInfo());
    }

    private Info swaggerInfo() {
        License license = new License();
        license.setName("Gonngdal");

        return new Info()
            .version("v0.0.1")
            .title("Gonngdal")
            .description("Gonngdal API문서")
            .license(license);
    }

    private Components authSetting() {
        SecurityScheme bearerAuth =
            new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("Authorization")
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION);

        return
            new Components().addSecuritySchemes(this.securityKey, bearerAuth);
    }


    @Bean
    public OperationCustomizer customize() {
        return
            (Operation operation, HandlerMethod handlerMethod) -> {
                DisableSwaggerSecurity methodAnnotation =
                    handlerMethod.getMethodAnnotation(DisableSwaggerSecurity.class);
                ErrorCodeExamGenerator errorCodeExamGenerators =
                    handlerMethod.getMethodAnnotation(ErrorCodeExamGenerator.class);

                List<String> tags = getTags(handlerMethod);
                // DisableSecurity 어노테이션있을시 스웨거 시큐리티 설정 삭제
                if (methodAnnotation != null) {
                    operation.setSecurity(Collections.emptyList());
                }
                // 태그 중복 설정시 제일 구체적인 값만 태그로 설정
                if (!tags.isEmpty()) {
                    operation.setTags(Collections.singletonList(tags.get(0)));
                }
                if (errorCodeExamGenerators != null) {
                    generateErrorCodeExam(operation, errorCodeExamGenerators);
                }
                return operation;
            };
    }

    private void generateErrorCodeExam(
        Operation operation
        , ErrorCodeExamGenerator errorCodeExamGenerators
    ) {
        Arrays
            .stream(errorCodeExamGenerators.value())
            .collect(Collectors.groupingBy(ErrorResponseCode::getStatus))
            .forEach((k, v) -> {
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.setDescription(k.name());

                Content content = new Content();
                MediaType mediaType = new MediaType();
                Map<String, Example> examples = new HashMap<>();

                v.forEach(it -> {
                    Example example = new Example();
                    example.setValue(responseService.getFailResult(it).getBody());
                    example.setSummary(it.getCode());
                    examples.put(it.msg, example);
                });
                mediaType.setExamples(examples);
                content.addMediaType("application/json", mediaType);
                apiResponse.setContent(content);

                operation.getResponses().addApiResponse(String.valueOf(k.value()), apiResponse);
            });
    }

    private static List<String> getTags(HandlerMethod handlerMethod) {
        List<String> tags = new ArrayList<>();

        Tag[] methodTags = handlerMethod.getMethod().getAnnotationsByType(Tag.class);
        List<String> methodTagStrings =
            Arrays.stream(methodTags).map(Tag::name).collect(Collectors.toList());

        Tag[] classTags = handlerMethod.getClass().getAnnotationsByType(Tag.class);
        List<String> classTagStrings =
            Arrays.stream(classTags).map(Tag::name).collect(Collectors.toList());
        tags.addAll(methodTagStrings);
        tags.addAll(classTagStrings);
        return tags;
    }

}
