package pgd.irbl.business.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-24 17:40
 */
@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("pgd.irbl.business"))
                .paths(PathSelectors.any())
                .build();
    }


    /**
     * 用于定义API主界面的信息，比如可以声明所有的API的总标题、描述、版本
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("IRBL Bug Report Localization") //  可以用来自定义API的主标题
                .description("IRBL Bug Report Localization API Description") // 可以用来描述整体的API
                .termsOfServiceUrl("no~") // 用于定义服务的域名
                .version("0.01") // 可以用来定义版本。
                .build(); //
    }
}
