package hello.proxy.common.service;

public interface ServiceInterface {
    /**
     * CGLIB는 너무 깊게 알려고 하지 말자 나중에 ProxyFactory나 깊게 알아라.
     */
    void save();
    void find();
}
