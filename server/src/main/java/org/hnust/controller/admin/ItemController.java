package org.hnust.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hnust.context.BaseContext;
import org.hnust.dto.ItemPageDTO;
import org.hnust.dto.SuggestionPageQueryDTO;
import org.hnust.result.PageResult;
import org.hnust.result.Result;
import org.hnust.service.ItemService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static org.hnust.constant.RoleConstant.ADMIN;

@Slf4j
@RestController("AdminItemController")
@RequestMapping("/admin/item")
@Api(tags = "管理端失物招领相关接口")
public class ItemController {
    @Resource
    private ItemService itemService;

    @PutMapping("/validate")
    @ApiOperation("管理员审核建议")
    public Result validate(@RequestParam Long id, @RequestParam Integer status) {
        log.info("管理员{}正在审核{}号物品...", BaseContext.getCurrentUser().getUsername(), id);
        itemService.validate(id, status);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("管理员分页查询建议")
    public Result<PageResult> page(ItemPageDTO itemPageDTO) {
        log.info("管理员分页查询建议，参数为: {}", itemPageDTO);
        PageResult pageResult = itemService.pageQuery(itemPageDTO, ADMIN);
        return Result.success(pageResult);
    }
}
