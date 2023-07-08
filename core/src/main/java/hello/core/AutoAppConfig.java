package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
//@ComponentScan.Filter -> @Configuration이 붙은 설정 정보를 컴포넌트 스캔 대상에서 제외
public class AutoAppConfig {
    //@ComponentScan은 @Component가 붙은 모든 클래스를 스프링 빈으로 등록한다.
    //이 때 스프링 빈의 기본 이름은 클래스명을 사용하되 맨 앞글자만 소문자를 사용한다.

    //생성자에 @Autowired를 지정하면, 스프링 컨테이너가 자동으로 해당 스프링 빈을 찾아서 주입한다.

    //이전 AppConfig에서는 @Bean으로 직접 설정 정보를 작성하고, 의존관계도 직접 명시했으나, 이제는 의존관계 주입도 구현체 클래스 안에서 해결해야 한다.(@Autowired를 사용)
    //@Autowired는 의존관계를 자동으로 주입해준다.
}
