package hello.proxy.app.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping
@ResponseBody
public interface OrderControllerV1 {

    /**
     * 컨트롤러를 인터페이스로 만드는 경우는 자주 없다.
     * @RestController쓰면 되는데 왜 저렇게 하지?  // 스프링은 @Controller 또는 @RequestMapping 애노테이션이 타입에 있어야 스프링이 컨트롤러로 인식한다.
     * 여기서 타입이란 interface나 class 다.
     *
     * @Controller나 @RestController는 내부에 @Component 가 있다 그래서 자동 컴포넌트 스캔의 대상이 되기 떄문에, 수동 등록하기 위해서 스프링 컨트롤러로 인식할 수 있는 @RequestMapping 을 썼다.
     *
     * 인터페이스에는 자바 버전에 따라서 자바가 컴파일하면서 제대로 인식 못할 떄가 있어서 @RequestParam("itemId")를 넣어줬다.
     */

    @GetMapping("/v1/request")
    String request(@RequestParam("itemId") String itemId);

    @GetMapping("/v1/no-log")
    String noLog();
}
