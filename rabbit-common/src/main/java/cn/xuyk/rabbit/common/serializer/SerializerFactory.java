package cn.xuyk.rabbit.common.serializer;

/**
 * @Author: Xuyk
 * @Description: 序列化工厂
 * @Date: 2020/6/27
 */
public interface SerializerFactory {

    /**
     * 创建序列化器
     * @return
     */
    Serializer create();

}
