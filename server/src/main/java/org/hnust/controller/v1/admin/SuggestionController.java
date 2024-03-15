package org.hnust.controller.v1.admin;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hnust.context.BaseContext;
import org.hnust.dto.SuggestionPageQueryDTO;
import org.hnust.result.PageResult;
import org.hnust.result.Result;
import org.hnust.service.SuggestionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static org.hnust.constant.RoleConstant.ADMIN;

@RestController("AdminSuggestionController")
@RequestMapping("/admin/v1/suggest")
@Slf4j
@Api(tags = "管理端建议相关接口")
public class SuggestionController {
    @Resource
    private SuggestionService suggestionService;

    @GetMapping("/page")
    @ApiOperation("用户分页查询建议")
    public Result<PageResult> page(SuggestionPageQueryDTO suggestionPageQueryDTO) {
        log.info("用户分页查询建议，参数为: {}", suggestionPageQueryDTO);
        PageResult pageResult = suggestionService.pageQuery(suggestionPageQueryDTO, ADMIN);
        return Result.success(pageResult);
    }

    @PutMapping("/validate")
    @ApiOperation("管理员审核建议")
    // TODO：要不要加一个审核时间字段？
    public Result validate(@RequestParam Long id, @RequestParam Integer status) {
        log.info("管理员{}正在审核{}号建议...", BaseContext.getCurrentUser().getUsername(), id);
        suggestionService.validate(id, status);
        return Result.success();
    }

}
