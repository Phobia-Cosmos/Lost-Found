package org.hnust.controller.v2.user;

import lombok.extern.slf4j.Slf4j;
import org.hnust.entity.ChatInfo;
import org.hnust.result.Result;
import org.hnust.service.ChatInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chatInfo")
public class ChatInfoController {

    @Resource
    private ChatInfoService chatInfoService;

    @PostMapping("/add")
    public Result add(@RequestBody ChatInfo chatInfo) {
        chatInfoService.add(chatInfo);
        return Result.success();
    }

    @PutMapping("/updateRead/{chatUserId}")
    public Result updateRead(@PathVariable Long chatUserId) {
        chatInfoService.updateRead(chatUserId);
        log.info("更新和{}号用户的聊天记录", chatUserId);
        return Result.success();
    }

    @GetMapping("/selectUserChat/{chatUserId}")
    public Result selectUserChat(@PathVariable Long chatUserId) {
        log.info("查询和{}号用户的聊天记录：", chatUserId);
        List<ChatInfo> chatInfoList = chatInfoService.selectUserChat(chatUserId);
        log.info("聊天记录为：" + chatInfoList);
        return Result.success(chatInfoList);
    }

}