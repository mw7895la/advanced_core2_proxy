package hello.proxy.jdkdynamic.code;

public interface AInterface {

    //JDK 동적 프록시는 인터페이스를 기반으로 프록시를 동적으로 만들어준다. 따라서 인터페이스가 필수이다.
    /**
     * 동적 프록시 기술 없이 직접 프록시를 만들면 A용,B용 프록시를 만들어야 하는데 ,  동적 프록시를 가지고 A와B를 한방에 해결하자.
     */
    String call();
}
