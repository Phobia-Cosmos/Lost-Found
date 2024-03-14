package org.hnust.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
public class ItemService {

    @Resource
    private ItemMapper itemMapper;

    public void publish(ItemDTO itemDTO) {
        Item item = BeanUtil.copyProperties(itemDTO, Item.class);
        item.setStatus(WAITING);
        item.setPublishTime(Timestamp.valueOf(LocalDateTime.now()));

        itemMapper.insert(item);
    }


    public void modify(ItemDTO itemDTO) {
        Item item = BeanUtil.copyProperties(itemDTO, Item.class);
        itemMapper.update(item);
    }

    public void finish(Long id, Integer status) {
        Item item = Item.builder()
                .id(id)
                .status(status)
                .build();

        itemMapper.update(item);
    }

    public void deleteByIds(List<Long> ids) {
        itemMapper.deleteByIds(ids);
    }

    public PageResult pageQuery(ItemPageDTO itemPageDTO, Integer role) {
        PageHelper.startPage(itemPageDTO.getPage(), itemPageDTO.getPageSize());
        Page<Item> page = itemMapper.pageQuery(itemPageDTO, role);
        return new PageResult(page.getTotal(), page.getResult());
    }

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
