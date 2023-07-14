package hello.core.autowired;

import hello.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class AutowiredTest {

    //옵션 처리
    //주입할 스프링 빈이 없어도 동작해야 할 때가 있는데, @Autowired만 사용하면 'required'옵션의 기본값이
    //'true'기 때문에 자동 주입 대상이 없으면 오류가 발생한다. 아래에서 자동 주입 대상의 옵션 처리 방법을 알아보자

    //1. @Autowired(require=false) : 자동 주입할 대상이 없으면 수정자 메소드 자체가 호출 안됨
    //2. org.springframework.lang.@Nullable : 자동 주입할 대상이 없을 시 null이 입력
    //3. Optional<> : 자동 주입할 대상이 없으면 Optional.empty가 입력

    @Test
    void AutowiredOption(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);

    }

    static class TestBean {

        @Autowired(required = false) //result : 메소드 자체를 호출 하지 않음
        public void setBean1(Member noBean1){
            System.out.println("noBean1 = " + noBean1);
        }

        @Autowired //result : noBean2 = null
        public void setBean2(@Nullable Member noBean2){
            System.out.println("noBean2 = " + noBean2);
        }

        @Autowired //result : noBean3 = Optional.empty
        public void setBean3(Optional<Member> noBean3){
            System.out.println("noBean3 = " + noBean3);
        }

        //** @Nullable, Optional은 스프링 전반에 걸쳐서 지원된다. 생성자 자동 주입에서 특정 필드에만 사용해도 된다.

    }
}
