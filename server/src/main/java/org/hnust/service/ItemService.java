package org.hnust.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.hnust.constant.MessageConstant;
import org.hnust.dto.ItemDTO;
import org.hnust.dto.ItemPageDTO;
import org.hnust.entity.Item;
import org.hnust.exception.ItemNotFoundException;
import org.hnust.exception.OperationNotAllowedException;
import org.hnust.exception.ParamInvalidException;
import org.hnust.file.service.FileStorageService;
import org.hnust.mapper.ItemMapper;
import org.hnust.result.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hnust.constant.ItemConstant.WAITING;

@Service
@Slf4j
public class ItemService {

    @Resource
    private ItemMapper itemMapper;

    @Resource
    private FileStorageService fileStorageService;

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

    // TODO:不能修改userId
    public void modify(ItemDTO itemDTO) {
        isItemExists(itemDTO.getId());
        Item item = BeanUtil.copyProperties(itemDTO, Item.class);
        Timestamp startTime = item.getStartTime();
        log.info("起始时间为{}", startTime);
        itemMapper.update(item);
    }

    // TODO：查看这个item是否存在
    public void finish(Long id, Integer status) {
        isItemExists(id);
        Item item = Item.builder()
                .id(id)
                .status(status)
                .build();

        itemMapper.update(item);
    }

    private Item isItemExists(Long id) {
        Item byId = itemMapper.getById(id);
        if (byId == null) {
            throw new ItemNotFoundException(MessageConstant.ITEM_NOT_FOUND);
        }
        return byId;
    }

    // TODO：要判断当前用户和删除的用户ID关系，只能删除自己的（mapper中指定）；不需要判断是否为Admin，只有用户可以删除
    // 要判断id是否存在，是否可以删除
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
        return isItemExists(id);
    }

    public void validate(Long id, Integer status) {
        isItemExists(id);
        if (status > 3 || status < 0) {
            throw new OperationNotAllowedException(MessageConstant.OPERATION_NOT_ALLOWED);
        }
        Item item = Item.builder()
                .id(id)
                .status(status)
                .build();
        itemMapper.update(item);
    }

    public String uploadPicture(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.getSize() == 0) {
            throw new ParamInvalidException(MessageConstant.PARAM_INVALID);
        }

        String fileName = UUID.randomUUID().toString().replace("-", "");
        String originalFilename = multipartFile.getOriginalFilename();
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileId = null;
        try {
            fileId = fileStorageService.uploadImgFile("", fileName + postfix, multipartFile.getInputStream());
            log.info("上传图片到MinIO中，fileId:{}", fileId);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("WmMaterialServiceImpl-上传文件失败");
        }

        // 3.保存到数据库中
        return fileId;
    }
}
