package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor //final이 붙은 필드를 모아서 생성자를 자동으로 만들어준다(Ctrl + F12로 확인가능)
public class OrderServiceImpl implements OrderService{
    //롬복이 자바의 어노테이션 프로세서라는 기능을 이용해서 컴파일 시점에 생성자 코들르 자동으로 생성해준다.
    //실제 'class'를 열어보면 생성자를 추가하는 코드가 작성되어 있는 것을 확인할 수 있다.
    //최근에는 생성자를 1개만 두고, @Autowired를 생략하는 방법을 주로 사용한다.
    //이에 더하여 Lombok라이브러리의 @RequiredArgsConstructor를 함께 사용하면 기능은 전부 제공하며, 코드는 더 간결하다.

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);

        //할인에 대한 것은 discoutnPolicy가 해결하도록 두었다. => 단일 체계 원칙
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);

    }

    //테스트 용도
    public MemberRepository getMemberRepository(){
        return memberRepository;
    }
}
