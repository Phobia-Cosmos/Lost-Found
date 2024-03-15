package org.hnust.service;

import cn.hutool.core.bean.BeanUtil;
import org.hnust.constant.MessageConstant;
import org.hnust.context.BaseContext;
import org.hnust.dto.MessageDTO;
import org.hnust.dto.MessageQueryDTO;
import org.hnust.entity.Message;
import org.hnust.exception.DeletionNotAllowedException;
import org.hnust.exception.MessageNotFoundException;
import org.hnust.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class MessageService {

    private final static short MAX_PAGE_SIZE = 50;

    @Resource
    private UserService userService;

    @Resource
    private ItemService itemService;

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
        if (messageQueryDTO.getUpdateTime() == null) messageQueryDTO.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));

        List<Message> messageList = messageMapper.loadMessageList(messageQueryDTO, loadtype);

        // 3.结果封装
        return messageList;
    }


    // 按理来说用户和item不会为空，但是我们还是做了判断；为了测试使用
    public void publish(MessageDTO messageDTO) {
        userService.getById(messageDTO.getSender());
        userService.getById(messageDTO.getReceiver());
        itemService.getById(messageDTO.getItemId());

        Message message = BeanUtil.copyProperties(messageDTO, Message.class);
        message.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
        // TODO：这个为什么错误？message.setCreateTime((Timestamp) new Date());
        message.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));

        messageMapper.insert(message);
    }

    // 只能删除自己发送的消息，不可以删除对方的信息
    public void deleteByIds(List<Long> ids) {
        for (Long id : ids) {
            Long currentUserId = BaseContext.getCurrentUser().getId();
            Message message = messageMapper.getById(id);
            if(message == null){
                throw new MessageNotFoundException(MessageConstant.MESSAGE_NOT_FOUND);
            }
            // Item item = itemService.getById(message.getItemId());
            // Long receiverId = item.getUserId();
            if (message.getSender().equals(currentUserId)) {
                messageMapper.deleteByIds(ids);
            } else
                throw new DeletionNotAllowedException(MessageConstant.MESSAGE_DELETION_NOT_ALLOWED);
        }
    }
}