package org.hnust.controller.v2.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hnust.constant.MessageConstant;
import org.hnust.dto.MessageDTO;
import org.hnust.dto.MessageQueryDTO;
import org.hnust.entity.Message;
import org.hnust.result.Result;
import org.hnust.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController("MessageControllerV2")
@RequestMapping("/user/v2/message")
@Slf4j
@Api(tags = "用户端留言相关接口")
public class MessageController {

    @Resource
    private MessageService messageService;

    @PostMapping
    @ApiOperation("用户留言")
    public Result publish(@RequestBody MessageDTO messageDTO) {
        log.info("{}用户向用户{}留言...", messageDTO.getLostUserId(), messageDTO.getFoundUserId());
        messageService.publish(messageDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除留言")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("批量删除留言：{}", ids);
        messageService.deleteByIds(ids);
        return Result.success();
    }

    @GetMapping("/old")
    @ApiOperation("用户加载历史留言")
    public Result<List<Message>> loadOld(MessageQueryDTO messageQueryDTO) {
        log.info("用户加载历史留言，参数为: {}", messageQueryDTO);
        List<Message> messageList = messageService.load(MessageConstant.LOADTYPE_LOAD_OLD, messageQueryDTO);
        return Result.success(messageList);
    }

    @GetMapping("/new")
    @ApiOperation("用户查询最新留言")
    public Result<List<Message>> loadNew(MessageQueryDTO messageQueryDTO) {
        log.info("用户查询最新留言，参数为: {}", messageQueryDTO);
        List<Message> messageList = messageService.load(MessageConstant.LOADTYPE_LOAD_NEW, messageQueryDTO);
        return Result.success(messageList);
    }

}
