package hello.core.singleton;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        //ThreadA : A사용자 10000원 주문
        int userAPrice = statefulService1.order("userA", 10000);

        //ThreadB : B사용자 10000원 주문
        int userBPrice = statefulService2.order("userB", 20000);

        //ThreadA : A사용자 주문 금액 조회
        //int price = statefulService1.getPrice();
        System.out.println("price = " + userAPrice);

        //assertThat(statefulService1.getPrice()).isEqualTo(20000);
        //statefulService1, 2 는 같은 인스턴스 영역을 사용하므로 1의 금액을 조회하려해도 순차적으로 2가 가장 최근에 객체를 사용하여서 2의 금액을 조회하게 되는 문제가 발생한다.

        //해결
        //싱글톤 방식은 여러 클라이언트가 하나의 같은 객체 인스턴스를 공유하므로 상태를 유지(stateful)하게 설계하면 안된다. (무상태 stateless로 설계)
        //특정 클라이언트에 의존적인 필드, 또는 값을 변경할 수 있는 필드가 있으면 안도니다.
        //필드 대신에 자바에서 공유되지 않는, 지역변수, 파라미터, ThreadLocal 등을 사용해야 한다.
    }

    static class TestConfig {

        @Bean
        public StatefulService statefulService(){
            return new StatefulService();
        }
    }

}