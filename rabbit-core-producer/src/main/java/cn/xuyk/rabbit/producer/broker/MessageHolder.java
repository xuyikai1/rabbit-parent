package cn.xuyk.rabbit.producer.broker;

import cn.xuyk.rabbit.api.pojo.Message;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author: Xuyk
 * @Description: 使用threadLocal暂存线程中传输的消息列表
 * @Date: 2020/7/2
 */
public class MessageHolder {

    private List<Message> messages = Lists.newArrayList();

    private static final ThreadLocal<MessageHolder> HOLDER = ThreadLocal.withInitial(MessageHolder::new);

    public static void add(Message message) {
        HOLDER.get().messages.add(message);
    }

    public static List<Message> getAndClear() {
        List<Message> tmp = Lists.newArrayList(HOLDER.get().messages);
        HOLDER.remove();
        return tmp;
    }

}
