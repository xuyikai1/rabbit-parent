package cn.xuyk.rabbit.common.serializer.impl;

import cn.xuyk.rabbit.api.pojo.Message;
import cn.xuyk.rabbit.common.serializer.Serializer;
import cn.xuyk.rabbit.common.serializer.SerializerFactory;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: 2020/6/27
 */
public class JacksonSerializerFactory implements SerializerFactory {

    /**
     * 简单的单例模式
     */
    public static final SerializerFactory INSTANCE = new JacksonSerializerFactory();

    @Override
    public Serializer create() {
        return JacksonSerializer.createParametricType(Message.class);
    }

}
