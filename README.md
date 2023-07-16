# Spring_Core_Basic

좋은 객체 지향 설계의 5가지 원칙(SOLID)

1. SRP : 단일 책임 원칙(single responsibility principle)
   - 한 클래스는 하나의 책임만 가져야 한다.
   - 하나의 책임이라는 것은 모호하므로 중요한 기준은 변경이다.
   - 변경이 발생할 시 파급 효과가 적으면 단일 책임 원칙을 잘 따른 것(ex : UI변경, 객체의 생성과 사용을 분리)
2. OCP : 개방 - 폐쇄 원칙(open / closed principle)
   - 소프트웨어의 요소는 확장에는 열려있으나 변경에는 닫혀 있어야 한다.
   - 확장을 하기 위해선느 당연히 기존 코드를 변경하여야 할 것 같지만 다형성을 통해 해결할 수 있다.
   - 인터페이스를 구현한 새로운 클래스를 만들어서 새로운 기능을 구현(역할과 구현의 분리)
   - 즉 객체를 생성하고, 연관관계를 맺어주는 별도의 조립, 설정자가 필요하다.(Spring Container)
4. LSP : 리스코프 치환 원칙(Liskov substitution principle)
   - 프로그램의 객체는 프로그램의 정확성을 깨뜨리지 않으면서 하위 타입의 인스턴스로 바꿀수 있어야 한다.
   - 다형성에서 하위 클래스는 인터페이스 규약을 다 지켜야 한다는 것, 다형성을 지원하기 위한 원칙
6. ISP : 인터페이스 분리 원칙(interface segregation principle)
   - 특정 클라이언트를 위한 인터페이스 여러 개가 범용 인터페이스 하나보다 낫다.
   - 자동차 인터페이스 -> 운전 인터페이스, 정비 인터페이스로 분리
   - 사용자 클라이언트 -> 운전자 클라이언트, 정비사 클라이언트로 분리
   - 분리하면 정비 인터페이스 자체가 변해도 운전자 클라이언트에 영향을 주지 않는다.
   - 즉 인터페이스가 명확해지고, 대체 가능성이 높아진다.
8. DIP : 의존관계 역전 원칙(dependency inversion principle)
   - 구현 클래스에 의존하지 않고, 인터페이스에 의존해야 한다.
   - 즉 역할에 의존하게 해야 한다. 구현체에 의존하게 될 시, 변경이 어려워진다.

===========================================================================

비즈니스 요구사항과 설계

1. 회원
   - 회원 가입, 조회 기능
   - 회원은 일반과 VIP 등급 구분
   - 회원 데이터는 자체 DB를 구축할 수 있고, 외부 시스템과 연동 가능(미확정)
2. 주문과 할인 정책
   - 회원 상품 주문 기능
   - 회원 등급에 따른 할인 정책
   - 할인 정책은 모든 VIP는 1천원 할인 고정 금액 할인 적용(추후 변경 가능)
   - 할인 정책은 변경 가능성이 높다. 회사의 기본 할인 정책을 아직 정하지 못했고, 오픈 직전까지 고민을 미루고 싶어한다.
  
=> 요구사항을 보면 회원 데이터, 할인 정책과 같은 부분은 지금 결정하기 어려운 부분이다. 그렇다고 개발을 무기한 기다릴 수도 없다.

=> 객체 지향 설계 방법으로 해결해보자.

===========================================================================

주문과 할인 도메인 설계

1. 주문 생성 : 클라이언트는 주문 서비스에 주문 생성을 요청한다.
2. 회원 조회 : 할인을 위해서는 회원 등급이 필요하다. -> 주문 서비스는 회원 저장소에서 회원을 조회한다.
3. 할인 적용 : 주문 서비스는 회원 등급에 따른 할인 여부를 할인 정책에 위임한다.
4. 주문 결과 반환 : 주문 서비스는 할인 결과를 포함한 주문 결과를 반환한다.

=> 실제로는 주문 데이터를 DB에 저장하지만, 예제로서 편의를 위해 단순히 주문 결과를 반환한다.

===========================================================================

새로운 할인 정책 적용과 '문제점'

이전까지는 역할과 구현을 분리하였고, 다형성 구현과 인터페이스, 구현 객체를 분리하였다.
OCP, DIP 같은 객체지향 설계 원칙을 충실히 준수한 것 같지만 클래스 의존 관계에서 문제점이 있다.
클래스 의존관계를 확인해보자.
추상 인터페이스 의존 : DiscountPolicy
구현 클래스 : FixDiscountPolicy, RateDiscountPolicy
-> 추상 인터페이스 뿐만 아니라 구현 클래스에도 의존하고 있다. 
-> 기능을 확장해서 변경하게 되면, 클라이언트 코드(OrderServiceImpl)에 영향을 주게된다.
-> 따라서 OCP, DIP를 위반한다.

::DIP를 위반하지 않고 인터페이스만 의존하게 되어야한다. -> OrderServiceImpl에 DiscountPolicy의 구현 객체를 대신 생성하고 주입하는 무언가가 필요하다.

===========================================================================

관심사의 분리

애플리케이션의 전체 동작 방식을 구성(Config)하기 위해, '구현 객체'를 생성하고, '연결'하는 책임을 가지는 별도의 설정 클래스를 만들자.

1. AppConfig는 애플리케이션의 실제 동작에 필요한 구현 객체를 생성한다.
2. AppConfig는 생성한 객체 인스턴스의 참조(레퍼런스)를 생성자를 통해서 주입(연결)해준다.

===========================================================================

AppConfig 리팩토링

1. new MemoryMemberRepository 의 부분이 중복이 되어 제거하였다. MemoryMemberRepository를 다른 구현체로 변경할 때 한 부분만 변경하면 된다.
2. AppConfig의 역할과 구현 클래스를 구분하였다. 애플리케이션 전체 구성을 파악하기 용이해졌다.

===========================================================================

IOC, DI, 그리고 컨테이너

제어의 역전(IOC : Inversion Of Control)
1. 기존 프로그램은 클라이언트 구현 객체가 스스로 필요한 서버 구현 객체를 생성하고, 연결, 실행했다. 즉, 구현 객체가 프로그램의 제어 흐름을 스스로 조종했다.
2. AppConfig 사용 후 부터는 구현 객체는 자신의 로직을 실행하는 역할만 담당한다. 프로그램의 제어 흐름은 AppConfig가 가져간다.
3. OrderServiceImpl도 AppConfig가 생성하고, OrderService 인터페이스의 다른 구현객체를 생성하고 실행할 수 도 있다.
4. 이렇듯 프로그램의 제어흐름을 직접 제어하는 것이 아닌, 외부에서 관리하는 것을 제어의 역전(IOC)이라 한다.

프레임워크 vs 라이브러리
1. 프레임워크가 내가 작성한 코드를 제어하고, 대신 실행하면 그것은 프레임워크이다.(JUnit)
2. 반면 내가 작성한 코드가 직접 제어의 흐름을 담당한다면 라이브러리이다.

의존관계 주입(DI : Dependency Injection)
1. OrderServiceImpl은 DiscountPolicy인터페이스에 의존한다. 실제로 어떤 구현 객체가 사용될지는 알 수 없다.
2. 의존관계는 '정적인 클래스 의존 관계와 실행 시점에 결정되는 동적인 객체(인스턴스) 의존 관계' 둘을 분리해서 생각해야 한다.

 정적인 클래스 의존관계
 - 클래스가 사용하는 import 코드만 보고 의존관계를 쉽게 판단할 수 있다.
 - OrderServiceImpl은 MemberRepository , DiscountPolicy 에 의존한다는 것을 알 수 있다.
 - 이러한 클래스 의존관계 만으로는 실제 어떤 객체가 OrderServiceImpl 에 주입 될지 알 수 없다.

 동적인 객체 인스턴스 의존 관계
 - 애플리케이션 실행 시점(런타임)에 외부에서 실제 구현 객체를 생성하고 클라이언트에 전달해서 클라이언트와 서버의 실제 의존관계가 연결 되는 것을 의존관계 주입이라 한다.
 - 객체 인스턴스를 생성하고, 그 참조값을 전달해서 연결된다. 의존관계 주입을 사용하면 클라이언트 코드를 변경하지 않고, 클라이언트가 호출하는 대상의 타입 인스턴스를 변경할 수 있다.
 - 의존관계 주입을 사용하면 정적인 클래스 의존관계를 변경하지 않고, 동적인 객체 인스턴스 의존관계를 쉽게 변경할 수 있다.

IOC 컨테이너, DI 컨테이너
- AppConfig 처럼 객체를 생성하고 관리하면서 의존관계를 연결해 주는 것을 IoC 컨테이너 또는 DI 컨테이너라 한다. 
- 의존관계 주입에 초점을 맞추어 최근에는 주로 DI 컨테이너라 한다.

===========================================================================
***************************************************************************
===========================================================================

스프링으로 전환하기

1. ApplicationContext를 스프링 컨테이너라고 하며, 스프링 컨테이너는 @Configuration 이 붙은 AppConfig를 설정(구성)정보로 사용한다.
2. @Bean 메서드를 모두 호출하여 반환된 객체를 스프링 컨테이너에 등록한다.
3. 스프링 컨테이너를 통해 필요한 스프링 빈(객체)를 applicationContext.getBean() 메서드를 사용하여 조회한다.

===========================================================================

스프링 컨테이너 생성

1. ApplicationContext는 인터페이스이며, 스프링 컨테이너라고 한다.
2. 스프링 컨테이너는 XML과 어노테이션 기반의 자바 설정 클래스로 만들 수 있다.
3. AppConfig를 사용했던 방식이 어노테이션 기반의 자바 설정 클래스로 스프링 컨테이너를 만든 것이다.
4. 스프링 컨테이너 생성
   - new AnnotationCofigApplicationContext(AppConfig.class) -> 스프링 컨테이너를 생성할 때는 구성정보를 지정해주어야 한다.
5. 스프링 빈 등록
   - @Bean을 사용하여 등록하고 (name = '')을 사용하여 이름 부여도 가능하다. 그러나 빈 이름은 항상 다른 이름을 부여해야한다.
6. 스프링 빈 의존관계 설정
   - 스프링 컨테이너는 설정 정보를 참고해서 의존관계를 주입(DI)한다.
   - *AppConfig의 orderService 참고
   - 자바 코드를 호출하는 것과 차이는 싱글톤 컨테이너 부분 확인

===========================================================================

빈팩토리와 어플리케이션컨텍스트

1. BeanFactory
   - 스프링 컨테이너의 최상위 인터페이스로 스프링 빈을 관리, 조회하는 역할을 한다.(ex : getBean())
2. ApplicationContext
   - BeanFactory 기능을 모두 상속받아 제공하며, 많은 부가기능을 제공한다.
   - 로컬, 개발, 운영 등과 같은 환경변수를 구분하여 처리한다.
   - 파일, 클래스패스, 외부 등에서 리소스를 편리하게 지원해준다.

*BeanFactory나 ApplicationContext를 스프링 컨테이너라고 한다.

===========================================================================

싱글톤 패턴
1. 싱글톤 패턴의 등장
   - 스프링 어플리케이션의 대부분은 웹 어플리케이션으로, 여러 고객의 동시 요청을 처리해야한다.
   - 트래픽이 초당 100이 나오면 초당 100개 객체가 생성되고 소멸되는 메모리 낭비가 발생한다.
   - 이를 해결하기 위해 객체를 1개만 생성하여 공유되도록 설계하는 싱글톤 패턴이 등장한다.
2. 사용방법
   1) static 영역에 객체 instance를 미리 하나 생성하여 올려둔다.
   2) 이 객체 인스턴스는 getInstance() 메소드를 통해서만 조회가능하다. 이 메소드를 호출하면 항상 같은 인스턴스를 반환한다.
   3) 1개의 객체 인스턴스만 존재하야 하므로, 생성자를 private으로 선언하여 외부에서 new 키워드를 사용한는 것을 방지한다.   
3. 문제점
   - 싱글톤 패턴을 구현하는 코드 자체가 많이 들어간다.
   - 의존관계상 클라이언트가 구체 클래스를 의존한다.(DIP위반)
   - 클라이언트가 구체 클래스에 의존하기 때문에 OCP도 위반할 가능성이 높다.
   - 테스트하기 용이하지 않다.
   - private 생성자로 자식 클래스를 만들기 어렵고, 내부 속성 변경이나 초기화가 어려워, 유연성이 떨어진다.
   - 안티 패턴이라고도 불린다.

===========================================================================

싱글톤 컨테이너
- 스프링 컨테이너는 싱글톤 패턴의 문제점을 해결하면서, 객체 인스턴스를 싱글톤으로 관리한다.
- 스프링 빈이 바로 싱글톤으로 관리되는 빈이다.
- 스프링 컨테이너는 싱글톤 컨테이너 역할을 하며, 싱글톤 객체를 생성하고, 관리하는 기능을 싱글톤 레지스트리라고 한다.
- DIP, OCP, 테스트, private 생성자로 부터 자유롭게 싱글톤을 사용가능하다.

===========================================================================

컴포넌트 스캔과 의존관계 자동 주입(AutoAppConfig.java 참고)

- 설정정보는 @Configuration과 @ComponentScan을 사용한다.
- @ComponentScan은 @Component가 붙은 모든 클래스를 스프링 빈으로 등록한다. (스프링 빈의 기본 이름은 클래스명이지만, 첫 글자만 소문자 사용)
- @Autowired를 생성자에 지정하여 의존 관계를 자동으로 스프링 빈을 찾아 주입한다.
- 이전 AppConfig에서는 @Bean으로 직접 설정 정보를 작성하고, 의존관계도 직접 명시했으나, 이제는 의존관계 주입도 구현체 클래스 안에서 해결해야 한다.(@Autowired를 사용)

===========================================================================

탐색 위치와 기본 스캔 대상

- 모든 자바 클래스를 컴포넌트 스캔하기에는 시간과 비용소모가 비효율적이므로, 필요한 위치부터 탐색 시작 위치를 지정할 수 있다.
- basePackages = "hello.core.member" 으로 탐색할 위치 지정
- basePackageClasses = AutoAppConfig.class 지정할 클래스의 패키지를 탐색 시작 위치로 지정
- 지정하지 않은 default는 @ComponentScan이 붙은 설정 정보 클래스의 패키지가 시작 위치가 된다.
- 권장 : 설정 정보의 클래스 위치를 프로젝트 최상단으로 두어, 패키지 위치를 지정하지 않는 방법

컴포넌트 스캔 기본 대상과 부가 기능
1. @Component : 컴포넌트 스캔에 사용한다.
2. @Controller : 스프링 MVC 컨트롤러에서 사용하며, MVC 컨트롤러로 인식한다.
3. @Service : 스프링 비즈니스 로직에서 사용하며, 특별한 처리보다는 핵심 비즈니스 로직의 위치인 비즈니스 계층을 인식하는데 도움이 된다.
4. @Repository : 스프링 데이터 접근 계층에서 사용하며, 스프링 데이터 접근 계층으로 인식하고, 데이터 계층의 예외를 스프링 예외로 반환해준다.
5. @Configuration : 스프링 설정 정보에서 사용하며, 스프링 빈이 싱글톤을 유지하도록 추가 처리를 한다.

===========================================================================

다양한 의존관계 주입 방법

1. 생성자 주입
   - 생성자를 통해 의존 관계를 주입 받는 방법
   - 생성자 호출 시점에 딱 한번만 호출되는 것이 보장된다.
   - *불변, 필수인 의존관계에 사용한다. (setter 메소드를 사용하면 안됨)
   - *생성자가 1개만 있으면 @Autowired를 생략해도 자동 주입 된다.(스프링 빈에만 해당)

2. 수정자 주입(setter 주입)
   - setter라 불리는 필의 값을 변경하는 수정자 메소드를 통해서 의존관계를 주입하는 방법
   - *선택, 변경의 가능성이 있는 의존관계에 사용한다.
   - *@Autowired의 기본 동작은 주입할 대상이 없으면 오류가 발생하므로, 없어도 동작하게 하기 위해서는 '@Autowired(required = false)'로 지정하면 된다.
   - 자바빈 프로퍼티 검색 참고

3. 필드 주입
   - 필드에 바로 주입하는 방법이다.
   - 코드가 간결하다는 장점이 있지만 외부에서 변경이 불가능하기 때문에 테스트를 하기 어려운 치명적인 단점이 있다.
   - DI프레임워크가 없으면 아무것도 할 수 없다.
   - 애플리케이션의 실제 코드와 관계 없는 테스트 코드 또는, 스프링 설정을 목적으로 하는 @Configuration 같은 곳에서만 특별한 용도로 사용하자.

4. 일반 메소드 주입
   - 일반 메소드를 통해서 주입 받을 수 있다.
   - 한번에 여러 필드를 주입 받을 수 있다는 장점이 있으나, 생성자, 수정자 주입으로 해결 할 수 있기 떄문에 일반적으로 잘 사용하지 않는다.

===========================================================================

생성자 주입을 선택

- 과거에는 수정자 주입과 필드 주입을 주로 사용했지만, 최근에는 스프링을 포함한 DI프레임워크 대부분이 생성자 주입을 권장하는데 그 이유에 대해 알아보자
- (전제) 대부분의 의존관계는 애플리케이션 종료 전까지 변한면 안된다.
- 수정자 주입을 사용 시, setXxx 메소드를 public으로 열어두어야 한다.
- 실수로 변경할 수도 있고, 변경하면 안되는 메소드를 열어두는 것은 좋은 설계 방법이 아니다.
- 생성자 주입은 객체를 생성할 때에 1번만 호출되어 이후에는 호출되는 일이 없으므로, 불변하게 설계할 수 있다.
- 컴파일 시점에 객체를 주입받아 테스트 코드를 작성할 수 있으며, 주입하는 객체가 '누락'된 경우 컴파일 시점에 오류를 발견할 수 있다.
- 생성자에서 혹시라도 값이 설정되지 않는 오류를 필드에 final 키워드를 사용하여 컴파일 시점에 막을 수 있다.

** 컴파일 오류가 가장 빠르고 좋은 오류이다.
** 수정자 주입을 포함한 나머지 주입 방식은 생성자 이후에 호출되므로, 필드에 final 키워드를 사용할 수 없다. 오직 생성자 주입 방식만 사용가능하다.

===========================================================================

롬복과 최신 트렌드

막상 개발을 해보면 대부분이 다 불변이기 때문에 필드에 final키워드를 사용하게 된다.
final 키워드를 사용하면 생성자도 만들어야 하고, 중비 받은 값을 대입하는 코드도 작성해야 한다.
이전에 학습했던 필드 주입처럼 간편하게 사용하는 롬복에 대해 알아보자.

1. build.gradle에 롬복 라이브러리를 적용
2. @RequiredArgsConstructor 기능을 통해 final이 붙은 필드를 모아 생성자를 자동 생성

롬복이 자바의 어노테이션 프로세서라는 기능을 이용해서 컴파일 시점에 생성자 코드르 자동으로 생성해준다.
실제 'class'를 열어보면 생성자를 추가하는 코드가 작성되어 있는 것을 확인할 수 있다. (Ctrl + F12로 확인가능)
최근에는 생성자를 1개만 두고, @Autowired를 생략하는 방법을 주로 사용한다.
이에 더하여 Lombok라이브러리의 @RequiredArgsConstructor를 함께 사용하면 기능은 전부 제공하며, 코드는 더 간결하다.
