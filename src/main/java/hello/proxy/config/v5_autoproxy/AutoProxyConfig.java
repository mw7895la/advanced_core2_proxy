package hello.proxy.config.v5_autoproxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AutoProxyConfig {
    //@Bean
    public Advisor advisor1(LogTrace logTrace) {
        // pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        // advice 설정 (부가기능로직 )
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    /**
     * 어드바이저만 빈으로 등록하면 끝난다. 자동프록시 생성기라는 빈포스트 프로세서는 이미 스프링이 자동으로 등록해놓는다. (AnnotationAwareAspectJAutoProxyCreator)
     * 빈 후처리기는 이미 자동으로 등록되어있으니  어드바이저만 찾아온다.
     */


    // AspectJ expression 표현식 사용하여 포인트컷 설정
    //@Bean
    public Advisor advisor2(LogTrace logTrace) {
        //pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..))");
        //app.. app이랑 app하위의 모든 패키지  *(..) 파라미터에 대해선 상관없다.
        //다만, no-log도 프록시 대상이 되어버린다.. 그래서 빼줘야 한다.

        // advice 설정 (부가기능로직 )
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    @Bean
    public Advisor advisor3(LogTrace logTrace) {
        //pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app..noLog(..))");
        //app.. app이랑 app하위의 모든 패키지  , *(..) 파라미터에 대해선 상관없다.
        //다만, no-log도 프록시 대상이 되어버린다.. 그래서 빼줘야 한다.

        // advice 설정 (부가기능로직 )
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
