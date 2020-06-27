package cn.xuyk.rabbit.common.serializer;

/**
 * @Author: Xuyk
 * @Description: 序列化和反序列化的接口
 * @Date: 2020/6/27
 */
public interface Serializer {

    /**
     * 对象序列化成字节
     * @param data
     * @return
     */
    byte[] serializeRaw(Object data);

    /**
     * 序列化对象成String
     * @param data
     * @return
     */
    String serialize(Object data);

    /**
     * 反序列化
     * @param content
     * @param <T>
     * @return
     */
    <T> T deserialize(String content);

    /**
     * 字节反序列化成对象
     * @param content
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] content);

}
