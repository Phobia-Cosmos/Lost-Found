package org.hnust.controller.v2.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hnust.context.BaseContext;
import org.hnust.dto.SuggestionDTO;
import org.hnust.dto.SuggestionPageQueryDTO;
import org.hnust.result.PageResult;
import org.hnust.result.Result;
import org.hnust.service.SuggestionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static org.hnust.constant.RoleConstant.MINE;
import static org.hnust.constant.RoleConstant.USER;

@RestController("UserSuggestionControllerV2")
@RequestMapping("/user/v2/suggest")
@Slf4j
@Api(tags = "用户端建议相关接口")
public class SuggestionController {

    // 初始化时赋值了就是null，因此我们要使用postConstruct；（都没有用）
    // private UserDTO userDTO;
    //
    // @PostConstruct
    // public void init() {
    //     this.userDTO = BaseContext.getCurrentUser();
    // }

    @Resource
    private SuggestionService suggestionService;

    @PostMapping
    @ApiOperation("发表建议")
    // 要先判断用户是否存在；但是这个仅存在于测试阶段，因为登陆后用户一定存在
    public Result publish(@RequestBody SuggestionDTO suggestionDTO) {
        // log.info("用户{}发表建议: {}", userDTO.getUsername(), suggestionDTO);
        log.info("用户发表建议: {}", suggestionDTO);
        suggestionService.publish(suggestionDTO);
        return Result.success("发布建议成功！");
    }

    @PutMapping
    @ApiOperation("修改建议")
    // 要先判断这个建议是不是自己的；要判断这个建议是否存在（测试阶段问题）
    public Result modify(@RequestBody SuggestionDTO suggestionDTO) {
        // log.info("用户{}修改建议: {}", userDTO.getUsername(), suggestionDTO);
        log.info("用户{}修改建议: {}", BaseContext.getCurrentUser().getUsername(), suggestionDTO);
        suggestionService.modify(suggestionDTO, USER);
        return Result.success("修改建议成功！");
    }

    @DeleteMapping
    @ApiOperation("批量删除建议")
    // 要先判断这个建议是不是自己的
    public Result delete(@RequestParam List<Long> ids) {
        log.info("用户{}批量删除{}号建议：", BaseContext.getCurrentUser().getUsername(), ids);
        suggestionService.deleteByIds(ids);
        return Result.success();
    }

    @PutMapping("/voteUp")
    @ApiOperation("点赞")
    // TODO:这里接收逻辑到底是怎么样的？
    // TODO：封装为DTO！
    // TODO:可以修改mapper返回的数据为String，因为我们不同的点赞结合点赞的状态可能会有不同的信息！！！
    public Result voteUp(@RequestBody Map<String, Object> requestBody) {
        Long id = ((Number) requestBody.get("id")).longValue();
        Long userId = ((Number) requestBody.get("userId")).longValue();
        log.info("{}用户为{}号建议点赞...", userId, id);
        suggestionService.voteUp(id, userId);
        return Result.success(userId + "号用户对" + id + "号建议点赞...");
    }

    @PutMapping("/voteDown")
    @ApiOperation("取消点赞")
    public Result voteDown(@RequestBody Map<String, Object> requestBody) {
        Long id = ((Number) requestBody.get("id")).longValue();
        Long userId = ((Number) requestBody.get("userId")).longValue();
        log.info("{}用户取消对{}号建议的点赞...", userId, id);
        suggestionService.voteDown(userId, id);
        return Result.success(userId + "号用户取消对" + id + "号建议的点赞...");
    }

    @GetMapping("/page")
    @ApiOperation("用户分页查询建议")
    // 用户查询不到审核失败的建议
    // TODO：前端要限制可以展示的status！
    // TODO：审核那一栏的数据查询有点逻辑问题！
    public Result<PageResult> page(SuggestionPageQueryDTO suggestionPageQueryDTO) {
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
