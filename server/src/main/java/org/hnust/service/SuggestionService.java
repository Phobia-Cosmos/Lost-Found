package org.hnust.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.hnust.constant.MessageConstant;
import org.hnust.context.BaseContext;
import org.hnust.dto.SuggestionDTO;
import org.hnust.dto.SuggestionPageQueryDTO;
import org.hnust.entity.Suggestion;
import org.hnust.exception.DeletionNotAllowedException;
import org.hnust.exception.ModificationNotAllowedException;
import org.hnust.exception.SuggestionNotFoundException;
import org.hnust.mapper.SuggestionMapper;
import org.hnust.mapper.VoterMapper;
import org.hnust.result.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.hnust.constant.RoleConstant.*;

@Service
@Slf4j
public class SuggestionService {

    @Resource
    private SuggestionMapper suggestionMapper;

    @Resource
    private VoterMapper voterMapper;

    @Resource
    private UserService userService;

    public void publish(SuggestionDTO suggestionDTO) {
        userService.getById(suggestionDTO.getUserId());

        Suggestion suggestion = BeanUtil.copyProperties(suggestionDTO, Suggestion.class);
        suggestion.setPollCount(0);
        suggestion.setStatus(0);
        suggestion.setPublishTime(new Timestamp(new Date().getTime()));
        suggestionMapper.insert(suggestion);
    }

    // 建议是否存在、用户是否存在、修改用户是否有权利
    // userId只用来做逻辑判断，不参与更新操作
    public void modify(SuggestionDTO suggestionDTO, int role) {
        userService.getById(suggestionDTO.getUserId());
        Suggestion byId = isSuggestExists(suggestionDTO.getId());

        if (role == USER && byId.getUserId() != suggestionDTO.getUserId()) {
            throw new ModificationNotAllowedException(MessageConstant.MODIFICATION_NOT_ALLOWED);
        }

        Suggestion suggestion = BeanUtil.copyProperties(suggestionDTO, Suggestion.class);
        log.info("建议为{}", suggestion);
        suggestionMapper.update(suggestion);
    }

    private Suggestion isSuggestExists(Long id) {
        Suggestion byId = suggestionMapper.getById(id);
        if (byId == null) {
            throw new SuggestionNotFoundException(MessageConstant.SUGGEST_NOT_FOUND);
        }
        return byId;
    }

    // 要判断是不是自己的建议，前端也可以进行规范，就是非自己的建议不可以有删除按钮
    public void deleteByIds(List<Long> ids, int role) {
        for (Long id : ids) {
            Suggestion suggestion = isSuggestExists(id);
            if (role == USER && suggestion.getUserId() != BaseContext.getCurrentUser().getId()) {
                throw new DeletionNotAllowedException(MessageConstant.DELETION_NOT_ALLOWED + ":" + id + "号建议");
            }
        }
        suggestionMapper.deleteByIds(ids);
    }

    public void voteUp(Long id, Long userId) {
        // 判断用户是否存在
        userService.getById(userId);
        Suggestion suggestion = updateUser(id, userId, VOTEUP);
        suggestionMapper.update(suggestion);
    }


    public void voteDown(Long userId, Long id) {
        userService.getById(userId);
        Suggestion suggestion = updateUser(id, userId, VOTEDOWN);
        suggestionMapper.update(suggestion);
    }

    @Transactional
    // 投票关系和用户的关系维护在另一表中
    // 还要维护这个用户是点赞还是取消点赞（）；我们要先看这个用户有没有投过票或者差评过
    public Suggestion updateUser(Long id, Long userId, Long operation) {
        Suggestion suggestion = isSuggestExists(id);
        Integer pollCount = suggestion.getPollCount();

        // 仅仅传递这一个参数userId不能唯一确定我们的投票，因为一个user可以对多个建议投票
        Long voteType = voterMapper.getOpByUserId(id, userId);
        // 注意：我们只可能会出现账号不存在，如果点赞不存在，我们直接新建即可
        // if (voteType == null) {
        //     throw new SuggestionNotFoundException(MessageConstant.VOTE_NOT_FOUND);
        // }

        // 建议表的点赞数单独操作，但是也需要判断是否点过（不可以放到外部单独判断）；1-点赞；2-取消点赞
        // 当我们点过赞后，无论如何操作，点赞数都应该减
        // TODO:现在的逻辑仅仅是修改点赞的状态，而不删除；后续可以换成删除
        if (voteType == null || voteType == 0) {
            log.info("null || 0");
            if (voteType == null)
                voterMapper.insert(id, userId, operation);
            if (operation.equals(VOTEUP)) {
                suggestion.setPollCount(pollCount + 1);
                voterMapper.modify(id, userId, VOTEUP);
            } else {
                suggestion.setPollCount(pollCount - 1);
                voterMapper.modify(id, userId, VOTEDOWN);
            }
        } else if (voteType == 1) {
            log.info("1");

            // 用户之前是点过赞的状态，因此再次点击就是取消点赞或者差评
            suggestion.setPollCount(pollCount - 1);
            // 更新操作一定要ID传入，否则会导致所有数据被更新
            if (operation.equals(VOTEUP)) {
                // 如果是再次点赞，则状态更新为未操作
                voterMapper.modify(id, userId, NONE);
            } else {
                voterMapper.modify(id, userId, VOTEDOWN);
            }
        } else {
            log.info("2");

            // 用户之前是差评的状态，因此再次点击就是取消差评或者点赞
            suggestion.setPollCount(pollCount + 1);
            if (operation.equals(VOTEUP)) {
                voterMapper.modify(id, userId, VOTEUP);
            } else {
                voterMapper.modify(id, userId, NONE);
            }
        }
        log.info("票数为：" + suggestion.getPollCount() + "");
        return suggestion;
    }

    public PageResult pageQuery(SuggestionPageQueryDTO suggestionPageQueryDTO, Integer role) {
        PageHelper.startPage(suggestionPageQueryDTO.getPage(), suggestionPageQueryDTO.getPageSize());
        log.info("分页查询参数为{}", suggestionPageQueryDTO);
        Page<Suggestion> page = suggestionMapper.pageQuery(suggestionPageQueryDTO, role);
        return new PageResult(page.getTotal(), page.getResult());
    }

    public void validate(Long id, Integer status, String msg) {
        isSuggestExists(id);
        Suggestion suggestion = Suggestion.builder()
                .id(id)
                .status(status)
                .validateMsg(msg)
                .validatorId(BaseContext.getCurrentUser().getId())
                .build();

        suggestionMapper.update(suggestion);
    }

    public Suggestion getById(Long id) {
        return isSuggestExists(id);
    }
}