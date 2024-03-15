package org.hnust.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.hnust.dto.ItemDTO;
import org.hnust.dto.ItemPageDTO;
import org.hnust.entity.Item;
import org.hnust.mapper.ItemMapper;
import org.hnust.result.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.hnust.constant.ItemConstant.WAITING;

@Service
@Slf4j
public class ItemService {

    @Resource
    private ItemMapper itemMapper;

    // 如果没有设置起始和结束时间，则默认为当前时间
    // TODO:判断用户是否存在
    public void publish(ItemDTO itemDTO) {
        Item item = BeanUtil.copyProperties(itemDTO, Item.class);
        item.setStatus(WAITING);

        if (itemDTO.getStartTime() == null) {
            item.setStartTime(Timestamp.valueOf(LocalDateTime.now()));
        }
        if (itemDTO.getEndTime() == null) {
            item.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
        }

        item.setPublishTime(Timestamp.valueOf(LocalDateTime.now()));
        itemMapper.insert(item);
    }

    public void modify(ItemDTO itemDTO) {
        Item item = BeanUtil.copyProperties(itemDTO, Item.class);
        Timestamp startTime = item.getStartTime();
        log.info("起始时间为{}", startTime);
        itemMapper.update(item);
    }

    // TODO：查看这个item是否存在
    public void finish(Long id, Integer status) {
        Item item = Item.builder()
                .id(id)
                .status(status)
                .build();

        itemMapper.update(item);
    }

    // TODO：要判断当前用户和删除的用户ID关系，只能删除自己的（mapper中指定）；不需要判断是否为Admin，只有用户可以删除
    public void deleteByIds(List<Long> ids) {
        itemMapper.deleteByIds(ids);
    }

    public PageResult pageQuery(ItemPageDTO itemPageDTO, Integer role) {
        PageHelper.startPage(itemPageDTO.getPage(), itemPageDTO.getPageSize());
        Page<Item> page = itemMapper.pageQuery(itemPageDTO, role);
        return new PageResult(page.getTotal(), page.getResult());
    }
    // 以下方式会查出

    public Item getById(Long id) {
        Item item = itemMapper.getById(id);
        return item;
    }

    public void validate(Long id, Integer status) {
        Item item = Item.builder()
                .id(id)
                .status(status)
                .build();
        itemMapper.update(item);

    }
}
