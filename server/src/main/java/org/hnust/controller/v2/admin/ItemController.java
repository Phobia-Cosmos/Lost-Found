package org.hnust.controller.v2.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hnust.context.BaseContext;
import org.hnust.dto.ItemPageDTO;
import org.hnust.entity.Item;
import org.hnust.result.PageResult;
import org.hnust.result.Result;
import org.hnust.service.ItemService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hnust.constant.RoleConstant.ADMIN;

@Slf4j
@RestController("AdminItemControllerV2")
@RequestMapping("/admin/v2/item")
@Api(tags = "管理端失物招领相关接口")
public class ItemController {
    @Resource
    private ItemService itemService;

    @PutMapping("/validate")
    @ApiOperation("管理员审核建议")
    public Result validate(@RequestBody Map<String, Object> requestBody) {
        Long id = ((Number) requestBody.get("id")).longValue();
        Integer status = (Integer) requestBody.get("status");
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

    @GetMapping("{id}")
    @ApiOperation("管理员根据id查询失物招领")
    public Result<Item> getById(@PathVariable Long id) {
        Item item = itemService.getById(id);
        return Result.success(item);
    }

    @GetMapping("/allItem")
    @ApiOperation("管理员查询失物招领统计图")
    public Result all() {
        // 查询出所有失物广场的数量
        List<Item> itemList = itemService.getAll();

        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, String>> list = new ArrayList<>();

        int lostSize = 0;
        int foundSize = 0;
        for (Item item : itemList) {
            if (item.getIsLost() == 1) lostSize++;
            else foundSize++;
        }

        // 往list里面塞业务数据
        Map<String, String> lostMap = new HashMap<>();
        lostMap.put("name", "平台发布失物总量");
        lostMap.put("value", String.valueOf(lostSize));
        list.add(lostMap);
        Map<String, String> findMap = new HashMap<>();
        findMap.put("name", "平台发布招领总量");
        findMap.put("value", String.valueOf(foundSize));
        list.add(findMap);

        resultMap.put("text", "平台所有物品数量的统计");
        resultMap.put("subtext", "统计范围：失物广场和招领广场");
        resultMap.put("name", "总量");
        resultMap.put("data", list);
        return Result.success(resultMap);
    }

    @GetMapping("/lostItem")
    @ApiOperation("管理员查询失物统计图")
    public Result lost() {
        List<Item> itemList = itemService.getAll();
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();

        log.info("here");

        Map<String, Long> lostMap = itemList.stream()
                .filter(x -> x.getIsLost() == 1) // Ensure status is not null
                .collect(Collectors.groupingBy(
                        item -> {
                            switch (item.getStatus()) {
                                case 0:
                                    return "waitAudit";
                                case 1:
                                    return "failAudit";
                                case 2:
                                    return "waitImpl";
                                case 3:
                                    return "Implemented";
                                default:
                                    return "Unknown"; // Handle unknown status values
                            }
                        },
                        Collectors.counting() // Count occurrences of each status
                ));


        // 遍历lostMap里面的key
        for (String key : lostMap.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", key);
            map.put("value", lostMap.get(key));
            list.add(map);
        }
        resultMap.put("name", "总量");
        resultMap.put("text", "失物广场物品数量的占比数据");
        resultMap.put("subtext", "统计范围：丢失中和已找回");
        resultMap.put("data", list);

        return Result.success(resultMap);
    }

    @GetMapping("/findItem")
    @ApiOperation("管理员查询招领统计图")
    public Result find() {
        log.info("here");
        List<Item> itemList = itemService.getAll();
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Long> findMap = itemList.stream()
                .filter(x -> x.getIsLost() == 0) // Ensure status is not null
                .collect(Collectors.groupingBy(
                        item -> {
                            switch (item.getStatus()) {
                                case 0:
                                    return "waitAudit";
                                case 1:
                                    return "failAudit";
                                case 2:
                                    return "waitImpl";
                                case 3:
                                    return "Implemented";
                                default:
                                    return "Unknown"; // Handle unknown status values
                            }
                        },
                        Collectors.counting() // Count occurrences of each status
                ));

        log.info("there");
        // 遍历lostMap里面的key
        for (String key : findMap.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", key);
            map.put("value", findMap.get(key));
            list.add(map);
        }
        log.info("ok");
        resultMap.put("name", "总量");
        resultMap.put("text", "招领广场物品数量的占比数据");
        resultMap.put("subtext", "统计范围：已找到失主和未找到失主");
        resultMap.put("data", list);

        return Result.success(resultMap);
    }
}
