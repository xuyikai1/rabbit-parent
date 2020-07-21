package cn.xuyk.rabbit.producer;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @Author: Xuyk
 * @Description: tk-mybatis MyMapper
 * @Date: 2020/6/28
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
