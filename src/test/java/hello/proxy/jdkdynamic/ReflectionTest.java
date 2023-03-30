package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {

    @Test
    void reflection0() {
        Hello target = new Hello();

        //공통 로직 1 시작
        log.info("start");
        String result1 = target.callA();        //호출하는 메서드만 다르고 로직 2와 같다. 여긴 정적인 정보다. 자바가 실행하면 바꿀 수 없다.
        log.info("result={}", result1);
        //공통 로직 1 종료

        //공통 로직 2 시작
        log.info("start");
        String result2 = target.callB();
        log.info("result={}", result2);
        //공통 로직 2 종료
    }

    @Test
    void reflection1()throws Exception {
        //클래스 메타 정보 획득, 뭔가 정보를 획득해서 동적으로 바꿀 수 있다.
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");//내부 클래스는 구분을 위해 $ 를 사용한다.

        Hello target = new Hello();

        //callA 메서드 정보를 얻어보자. 문자로 얻었다. 그러면 얼마든지 다른것으로도 바꿀 수 있다는 말.
        //Method -> 메서드 자체를 가리키는 메타 데이터이다.
        Method methodCallA = classHello.getMethod("callA");

        //아래는 동적으로 호출이 가능하다. 변수 methodCallA가 가지고 있는 메서드를 호출하는데, 어떤 인스턴스에 있는것을 호출하냐? -> target이라는 인스턴스.
        Object result1 = methodCallA.invoke(target);
        log.info("result1={}", result1);

        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result2={}", result2);
        /**
         * 위에 reflection0 TEST 에서 target.callA()와 target.callB()는 소스코드로 박혀있었다.
         * 근데 그 부분을 여기서는 문자로 바꿨다. 즉, 파라미터로도 넘길 수 있고 다른 문자로 쓸 수 있다는 것이다.
         */
    }

    /** 이번엔 메서드를 넘긴다 */
    @Test
    void reflection2() throws Exception {
        Class<?> classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();
        //박혀있는 소스코드를 (맨 처음에 한 target.callA(),callB() ) 자바 리플렉션을 사용해서 메타정보로 바꿔치기 하는 것 ( Method )
        Method methodCallA =classHello.getMethod("callA");       // callZZZ 라고 하면  런타임 때 예외가 발생한다. 이게 리플렉션을 무조건 사용하면 안되는 이유다.
        dynamicCall(methodCallA, target);

        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB,target);
    }

    private void dynamicCall(Method method, Object target)throws Exception {
        log.info("start");
        //String result1 = target.callA();       //이 부분을 해결해야 한다. 코드가 박혀있어서 callB에선  dynamicCall() 메서드를 쓸 수가 없다.
        Object result = method.invoke(target);  //바로 위 부분을 추상화 했다.
        log.info("result={}", result);
    }

    static class Hello {
        public String callA() {
            log.info("callA");
            return "A";
        }

        public String callB() {
            log.info("callB");
            return "B";

        }
    }
}
