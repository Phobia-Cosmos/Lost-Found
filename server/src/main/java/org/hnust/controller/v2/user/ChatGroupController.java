package org.hnust.controller.v2.user;

import org.hnust.entity.ChatGroup;
import org.hnust.result.Result;
import org.hnust.service.ChatGroupService;
import org.hnust.vo.ChatGroupVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/chatGroup")
public class ChatGroupController {

    @Resource
    private ChatGroupService chatGroupService;

    @PostMapping("/add")
    public Result add(@RequestBody ChatGroup chatGroup) {
        chatGroupService.add(chatGroup);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        chatGroupService.deleteById(id);
        return Result.success();
    }

    // @GetMapping("/selectById/{id}")
    // public Result selectById(@PathVariable Integer id) {
    //     ChatGroup chatGroup = chatGroupService.selectById(id);
    //     return Result.success(chatGroup);
    // }

    @GetMapping("/selectUserGroup")
    public Result selectUserGroup() {
        List<ChatGroupVO> chatGroupList = chatGroupService.selectUserGroup();
        return Result.success(chatGroupList);
    }

}