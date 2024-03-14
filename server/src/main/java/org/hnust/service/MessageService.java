package org.hnust.service;

import cn.hutool.core.bean.BeanUtil;
import org.hnust.constant.MessageConstant;
import org.hnust.dto.MessageDTO;
import org.hnust.dto.MessageQueryDTO;
import org.hnust.entity.Message;
import org.hnust.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class MessageService {

    private final static short MAX_PAGE_SIZE = 50;

    @Resource
    private MessageMapper messageMapper;

    public List<Message> load(Short loadtype, MessageQueryDTO messageQueryDTO) {
        Integer size = messageQueryDTO.getSize();
        if (size == null || size == 0) {
            size = 10;
        }
        size = Math.min(size, MAX_PAGE_SIZE);
        messageQueryDTO.setSize(size);

        // 类型参数检验
        if (!loadtype.equals(MessageConstant.LOADTYPE_LOAD_OLD) && !loadtype.equals(MessageConstant.LOADTYPE_LOAD_NEW)) {
            loadtype = MessageConstant.LOADTYPE_LOAD_OLD;
        }
        // 时间校验
        if (messageQueryDTO.getUpdateTime() == null) messageQueryDTO.setUpdateTime((Timestamp) new Date());

        List<Message> messageList = messageMapper.loadMessageList(messageQueryDTO, loadtype);

        // 3.结果封装
        return messageList;
    }

    public void publish(MessageDTO messageDTO) {
        Message message = BeanUtil.copyProperties(messageDTO, Message.class);
        message.setUpdateTime((Timestamp) new Date());
        message.setCreateTime((Timestamp) new Date());

        messageMapper.insert(message);
    }

    public void deleteByIds(List<Long> ids) {
        messageMapper.deleteByIds(ids);
    }
}