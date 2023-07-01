package hello.core.member;

public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    //의존관계가 인터페이스 뿐만 아니라 구현까지 모두 의존하는 문제점이 있다.

    //MemberServiceImpl은 MemoryMemberRepository를 의존하지 않고, MemberRepository 인터페이스만 의존한다.
    //MemberServiceImpl 생성자를 통해 어떤 구현 객체를 주입하지는 오직 외부(AppConfig)에서 결정된다.(DI)
    //즉 MemberServiceImpl은 의존관계 설정에서 벗어나 실행만 하게된다.
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
