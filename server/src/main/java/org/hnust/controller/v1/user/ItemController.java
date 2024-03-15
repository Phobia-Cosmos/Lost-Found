package org.hnust.controller.v1.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hnust.context.BaseContext;
import org.hnust.dto.ItemDTO;
import org.hnust.dto.ItemPageDTO;
import org.hnust.entity.Item;
import org.hnust.result.PageResult;
import org.hnust.result.Result;
import org.hnust.service.ItemService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static org.hnust.constant.RoleConstant.USER;

@RestController("UserItemController")
@RequestMapping("/user/v1/item")
@Slf4j
@Api(tags = "用户端失物招领相关接口")
public class ItemController {

    @Resource
    private ItemService itemService;

    //  TODO：用户时间类型不对劲！
    @PostMapping
    @ApiOperation("发表失物招领")
    public Result publish(@RequestBody ItemDTO itemDTO) {
        log.info("{}用户发表了{}", BaseContext.getCurrentUser().getUsername(), Long.valueOf(itemDTO.getIsLost()) == 1 ? "招领" :
                "失物");
        itemService.publish(itemDTO);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改失物招领")
    public Result modify(@RequestBody ItemDTO itemDTO) {
        log.info("{}用户正在对{}进行修改", BaseContext.getCurrentUser().getUsername(), Long.valueOf(itemDTO.getIsLost()) == 1 ?
                "招领" : "失物");
        log.info("参数为{}", itemDTO);
        itemService.modify(itemDTO);
        return Result.success();
    }

    @PutMapping("/finish")
    @ApiOperation("完成一项失物招领")
    // 若使用RequestParam，则前端要使用query（URL）来进行参数的传递
    // public Result finish(
    //         @ApiParam(value = "ID of the item", required = true) @RequestParam Long id,
    //         @ApiParam(value = "Status of the item", required = true) @RequestParam Integer status) {
    public Result finish(@RequestBody Map<String, Object> requestBody) {
        Long id = ((Number) requestBody.get("id")).longValue();
        Integer status = (Integer) requestBody.get("status");
        log.info("{}用户将{}号失物招领标记为完成...", id);
        itemService.finish(id, status);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除失物招领")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("批量删除失物招领：{}", ids);
        itemService.deleteByIds(ids);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("用户分页查询失物招领")
    // 默认查询所有类型的数据，非审核失败
    // TODO：为什么分页查询不需要@RequestBody？
    // TODO：这里有奇怪的问题！！！
    public Result<PageResult> page(ItemPageDTO itemPageDTO) {
        log.info("用户分页查询失物招领，参数为: {}", itemPageDTO);
        PageResult pageResult = itemService.pageQuery(itemPageDTO, USER);
        return Result.success(pageResult);
    }

    @GetMapping("{id}")
    @ApiOperation("根据id查询失物招领")
    public Result<Item> getById(@PathVariable Long id) {
        Item item = itemService.getById(id);
        return Result.success(item);
    }

//     下方是测试接口参数与注解关系的注解， 来自于V2
    // @PutMapping("/modify1")
    // @ApiOperation("修改失物招领1")
    // // 如果不使用@RequestBody，则我们需要一个一个的标识
    // public Result modify1(ItemDTO itemDTO) {
    //     // log.info("{}用户正在对{}进行修改", BaseContext.getCurrentUser().getUsername(), Long.valueOf(itemDTO.getIsLost()) == 1 ?
    //     //         "招领" : "失物");
    //     log.info("参数为{}", itemDTO);
    //     itemService.modify(itemDTO);
    //     return Result.success();
    // }

    // @PutMapping("/modify1")
    // @ApiOperation("修改失物招领1")
    // public Result modify1(Long id, String name, String description, String img, Integer tag, Long userId, Integer isLost, Timestamp startTime, Timestamp endTime) {
    //     ItemDTO itemDTO = new ItemDTO();
    //     itemDTO.setId(id);
    //     itemDTO.setName(name);
    //     itemDTO.setDescription(description);
    //     itemDTO.setImg(img);
    //     itemDTO.setTag(tag);
    //     itemDTO.setUserId(userId);
    //     itemDTO.setIsLost(isLost);
    //     itemDTO.setStartTime(startTime);
    //     itemDTO.setEndTime(endTime);
    //
    //     log.info("参数为{}", itemDTO);
    //     itemService.modify(itemDTO);
    //     return Result.success();
    // }

    // @PutMapping("/finish2")
    // @ApiOperation("完成一项失物招领2")
    // // 若使用RequestParam，则前端要使用query（URL）来进行参数的传递
    // // public Result finish(
    // //         @ApiParam(value = "ID of the item", required = true) @RequestParam Long id,
    // //         @ApiParam(value = "Status of the item", required = true) @RequestParam Integer status) {
    // public Result finish2(@RequestParam("id") Long id, @RequestParam("status") Integer status) {
    //     // Long id = ((Number) requestBody.get("id")).longValue();
    //     // Integer status = (Integer) requestBody.get("status");
    //     log.info("{}用户将{}号失物招领标记为完成...", id);
    //     itemService.finish(id, status);
    //     return Result.success();
    // }
    //
    // @PutMapping("/finish4")
    // @ApiOperation("完成一项失物招领4")
    // public Result finish4(Long id, Integer status) {
    //     log.info("{}用户将{}号失物招领标记为完成...", id);
    //     itemService.finish(id, status);
    //     return Result.success();
    // }
    //
    // @PutMapping("/finish3/{id}/{status}")
    // @ApiOperation("完成一项失物招领2")
    // // 若使用RequestParam，则前端要使用query（URL）来进行参数的传递
    // // public Result finish(
    // //         @ApiParam(value = "ID of the item", required = true) @RequestParam Long id,
    // //         @ApiParam(value = "Status of the item", required = true) @RequestParam Integer status) {
    // public Result finish3(@PathVariable Long id, @PathVariable Integer status) {
    //     // Long id = ((Number) requestBody.get("id")).longValue();
    //     // Integer status = (Integer) requestBody.get("status");
    //     log.info("{}用户将{}号失物招领标记为完成...", id);
    //     itemService.finish(id, status);
    //     return Result.success();
    // }

    // @DeleteMapping("{ids}")
    // @ApiOperation("批量删除失物招领1")
    // public Result delete1(@PathVariable List<Long> ids) {
    //     log.info("批量删除失物招领：{}", ids);
    //     itemService.deleteByIds(ids);
    //     return Result.success();
    // }
    //
    // @DeleteMapping("/id")
    // @ApiOperation("批量删除失物招领2")
    // public Result delete2(@RequestBody List<Long> ids) {
    //     log.info("批量删除失物招领：{}", ids);
    //     itemService.deleteByIds(ids);
    //     return Result.success();
    // }

     // @GetMapping("/id")
    // @ApiOperation("根据id查询失物招领2")
    // public Result<Item> getById2(Long id) {
    //     Item item = itemService.getById(id);
    //     return Result.success(item);
    // }
    //
    // @GetMapping("/id1")
    // @ApiOperation("根据id查询失物招领3")
    // public Result<Item> getById3(@RequestParam Long id) {
    //     Item item = itemService.getById(id);
    //     return Result.success(item);
    // }
}
