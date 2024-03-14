package org.hnust.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hnust.dto.SuggestionDTO;
import org.hnust.dto.SuggestionPageQueryDTO;
import org.hnust.result.PageResult;
import org.hnust.result.Result;
import org.hnust.service.SuggestionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static org.hnust.constant.RoleConstant.MINE;
import static org.hnust.constant.RoleConstant.USER;

@RestController("UserSuggestionController")
@RequestMapping("/user/suggest")
@Slf4j
@Api(tags = "用户端建议相关接口")
public class SuggestionController {

    @Resource
    private SuggestionService suggestionService;

    @PostMapping
    @ApiOperation("发表建议")
    public Result publish(@RequestBody SuggestionDTO suggestionDTO) {
        log.info("发表建议: {}", suggestionDTO);
        suggestionService.publish(suggestionDTO);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改建议")
    public Result register(@RequestBody SuggestionDTO suggestionDTO) {
        log.info("修改建议: {}", suggestionDTO);
        suggestionService.modify(suggestionDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除建议")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("批量删除建议：{}", ids);
        suggestionService.deleteByIds(ids);
        return Result.success();
    }

    @PutMapping("/voteUp")
    @ApiOperation("点赞")
    public Result voteUp(@RequestParam Long id, @RequestParam Long userId) {
        log.info("{}用户为{}号建议点赞...", userId, id);
        suggestionService.voteUp(id, userId);
        return Result.success();
    }

    @PutMapping("/voteDown")
    @ApiOperation("取消点赞")
    public Result voteDown(@RequestParam Long id, @RequestParam Long userId) {
        log.info("{}用户取消对{}号建议的点赞...", userId, id);
        suggestionService.voteDown(userId, id);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("用户分页查询建议")
    public Result<PageResult> page(@RequestBody SuggestionPageQueryDTO suggestionPageQueryDTO) {
        log.info("用户分页查询建议，参数为: {}", suggestionPageQueryDTO);
        PageResult pageResult = suggestionService.pageQuery(suggestionPageQueryDTO, USER);
        return Result.success(pageResult);
    }

    // TODO:这里的逻辑和代码要修改
    @GetMapping("/list")
    @ApiOperation("用户分页查询自己的建议")
    public Result<PageResult> list(@RequestBody SuggestionPageQueryDTO suggestionPageQueryDTO) {
        log.info("用户分页查询建议，参数为: {}", suggestionPageQueryDTO);
        PageResult pageResult = suggestionService.pageQuery(suggestionPageQueryDTO, MINE);
        return Result.success(pageResult);
    }

}
