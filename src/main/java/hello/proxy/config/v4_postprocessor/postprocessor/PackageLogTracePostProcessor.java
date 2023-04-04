package hello.proxy.config.v4_postprocessor.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
public class PackageLogTracePostProcessor implements BeanPostProcessor {
    /** 특정 패키지에 있는 빈들만 프록시로 만들 것이다 */

    private final String basePackage;
    private final Advisor advisor;      //어드바이저 안에는 포인트컷, 어드바이스가 있다.

    public PackageLogTracePostProcessor(String basePackage, Advisor advisor) {
        this.basePackage = basePackage;
        this.advisor = advisor;
    }

    /** 아래 다 작성하고 빈 후처리기 PackageLogTracePostProcessor를 스프링 빈으로 등록하자.*/
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //After로 한 이유, 빈의 초기화가 다 끝나고나서 즉, 빈 객체가 다 만들어지고 나서 프록시를 적용할 것이기 때문.
        log.info("param beanName ={} bean={}", beanName, bean.getClass());

        //프록시 적용 대상 여부 체크

        //프록시 적용 대상 아니면 원본을 그대로 진행
        String packageName = bean.getClass().getPackageName();
        //bean이 넘어오는데, bean이 있는 클래스의 패키지가 우리가 지정한 패키지가 아니면(우린 app 하위 애들만 프록시 적용할거) 원본을 그대로 스프링 빈으로 등록한다(조작 X)
        if (!packageName.startsWith(basePackage)) {
            return bean;
        }

        //프록시 대상이면 프록시 만들어서 반환
        //프록시 적용하려면 프록시 팩토리가 필요하고 파라미터는 타겟을 넣어줘야지 !
        ProxyFactory proxyFactory = new ProxyFactory(bean);
        proxyFactory.addAdvisor(advisor);       // 부가기능을 어디에?-pointcut 어떤 로직?-advice 를 적용할지 어드바이저 add
        Object proxy = proxyFactory.getProxy();
        log.info("create proxy : target={} proxy={}", bean.getClass(), proxy.getClass());
        return proxy;       //이게 스프링 빈으로 등록이 된다.
    }
}

/** 이후에 테스트 코드에서 applicationContext.getBean("beanA")를 찍어서 프록시 객체가 찍히는지 확인해 볼것 근데 아마 helloA 실제 객체가 찍힐 것이다.
 * 그래도 뭐, 부가기능이 잘 찍히도록 프록시가 동작은 하는데, 왜 helloA가 찍히는 지는 영한쌤도 모른다.*/