package cn.xuyk.rabbit.producer.broker.container;

import cn.hutool.core.util.StrUtil;
import cn.xuyk.rabbit.api.common.MessageType;
import cn.xuyk.rabbit.api.exception.MessageRunTimeException;
import cn.xuyk.rabbit.api.pojo.Message;
import cn.xuyk.rabbit.common.convert.GenericMessageConverter;
import cn.xuyk.rabbit.common.convert.RabbitMessageConverter;
import cn.xuyk.rabbit.common.serializer.Serializer;
import cn.xuyk.rabbit.common.serializer.SerializerFactory;
import cn.xuyk.rabbit.common.serializer.impl.JacksonSerializerFactory;
import cn.xuyk.rabbit.producer.constant.BrokerMessageStatus;
import cn.xuyk.rabbit.producer.dao.BrokerMessageDao;
import cn.xuyk.rabbit.producer.pojo.BrokerMessage;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Xuyk
 * @Description:  池化封装/缓存
 *  * 	每一个topic 对应一个RabbitTemplate
 *  *	1.	提高发送消息的效率
 *  * 	2. 	可以根据不同的需求制定化不同的RabbitTemplate, 比如每一个topic 都有自己的routingKey规则
 * @Date: 2020/6/27
 */
@Slf4j
@Component
public class RabbitTemplateContainer implements RabbitTemplate.ConfirmCallback {

	@Autowired
	private BrokerMessageDao brokerMessageDao;

    /**
     * 缓存 map<topic,template>
     */
	private Map<String, RabbitTemplate> rabbitMap = Maps.newConcurrentMap();

	/**
	 * guava splitter：按#分割
	 */
	private Splitter splitter = Splitter.on("#");

	private SerializerFactory serializerFactory = JacksonSerializerFactory.INSTANCE;

//	@Autowired
//	private MessageStoreService messageStoreService;
	
	@Autowired
	private ConnectionFactory connectionFactory;
	
    /**
     * 根据不同消息获取相应的template
     * @param message
     * @return
     * @throws MessageRunTimeException
     */
	public RabbitTemplate getTemplate(Message message) throws MessageRunTimeException {
		Preconditions.checkNotNull(message);
		String topic = message.getTopic();
		RabbitTemplate rabbitTemplate = rabbitMap.get(topic);
		// 1.如果template池中存在则直接取出返回
		if(rabbitTemplate != null) {
			return rabbitTemplate;
		}
		log.info("#RabbitTemplateContainer.getTemplate# topic: {} is not exists, create one", topic);

		// 2.池中不存在对应的template则新增
		RabbitTemplate newTemplate = new RabbitTemplate(connectionFactory);
		newTemplate.setExchange(topic);
		newTemplate.setRoutingKey(message.getRoutingKey());
		newTemplate.setRetryTemplate(new RetryTemplate());
		
		// 3.添加序列化反序列化和converter对象
		Serializer serializer = serializerFactory.create();
		GenericMessageConverter gmc = new GenericMessageConverter(serializer);
		// 装饰者模式
		RabbitMessageConverter rmc = new RabbitMessageConverter(gmc);
		newTemplate.setMessageConverter(rmc);

		// 4.如果消息类型不是迅速消息，都需要设置确认回调
		String messageType = message.getMessageType();
		if(!MessageType.RAPID.equals(messageType)) {
			newTemplate.setConfirmCallback(this);
		}
		
		rabbitMap.putIfAbsent(topic, newTemplate);
		
		return rabbitMap.get(topic);
	}

    /**
     * 无论是 confirm 消息 还是 reliant 消息 ，发送消息以后 broker都会去回调confirm
     * @param correlationData
     * @param ack
     * @param cause
     */
	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		String id = correlationData.getId();
		if(StrUtil.isBlank(id)){
			log.error("send message content is empty");
			return ;
		}
		// 	具体的消息应答
		List<String> strings = splitter.splitToList(id);
		String messageId = strings.get(0);
		long sendTime = Long.parseLong(strings.get(1));
		String messageType = strings.get(2);
		if(ack) {
			//	当Broker 返回ACK成功时, 就是更新一下日志表里对应的消息发送状态为 SEND_OK
			
			// 	如果当前消息类型为reliant 我们就去数据库查找并进行更新
			if(MessageType.RELIANT.endsWith(messageType)) {
				BrokerMessage bm = BrokerMessage.builder()
						.messageId(messageId)
						.status(BrokerMessageStatus.SEND_OK.getStatus())
						.updateTime(new Date()).build();
				brokerMessageDao.updateByPrimaryKeySelective(bm);
			}
			log.info("send message is OK, confirm messageId: {}, sendTime: {}", messageId, sendTime);
		} else {
			log.error("send message is Fail, confirm messageId: {}, sendTime: {}", messageId, sendTime);
			// 队列满了/
		}
	}

}
