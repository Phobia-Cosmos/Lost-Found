package org.hnust.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.hnust.dto.SuggestionDTO;
import org.hnust.dto.SuggestionPageQueryDTO;
import org.hnust.entity.Suggestion;
import org.hnust.mapper.SuggestionMapper;
import org.hnust.mapper.VoterMapper;
import org.hnust.result.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.hnust.constant.RoleConstant.VOTEDOWN;
import static org.hnust.constant.RoleConstant.VOTEUP;

@Service
public class SuggestionService {

    @Resource
    private SuggestionMapper suggestionMapper;

    @Resource
    private VoterMapper voterMapper;

    public void publish(SuggestionDTO suggestionDTO) {
        Suggestion suggestion = BeanUtil.copyProperties(suggestionDTO, Suggestion.class);
        suggestion.setPollCount(0);
        suggestion.setStatus(0);
        suggestion.setPublishTime((Timestamp) new Date());
        suggestionMapper.insert(suggestion);
    }

    public void modify(SuggestionDTO suggestionDTO) {
        Suggestion suggestion = BeanUtil.copyProperties(suggestionDTO, Suggestion.class);
        suggestionMapper.update(suggestion);
    }

    public void deleteByIds(List<Long> ids) {
        suggestionMapper.deleteByIds(ids);
    }

    public void voteUp(Long id, Long userId) {
        Suggestion suggestion = updateUser(id, userId, VOTEUP);
        suggestionMapper.update(suggestion);
    }


    public void voteDown(Long userId, Long id) {
        Suggestion suggestion = updateUser(id, userId, VOTEDOWN);
        suggestionMapper.update(suggestion);
    }

    @Transactional
    public Suggestion updateUser(Long id, Long userId, Long operation) {
        Suggestion suggestion = suggestionMapper.getById(id);
        Integer pollCount = suggestion.getPollCount();
        if (operation.equals(VOTEUP)) {
            // suggestion.getVotedUser().add(userId);
            voterMapper.insert(id, userId);
            suggestion.setPollCount(pollCount + 1);
        } else {
            // suggestion.getVotedUser().remove(userId);
            voterMapper.deleteById(id);
            suggestion.setPollCount(pollCount - 1);
        }
        return suggestion;
    }

    public PageResult pageQuery(SuggestionPageQueryDTO suggestionPageQueryDTO, Integer role) {
        PageHelper.startPage(suggestionPageQueryDTO.getPage(), suggestionPageQueryDTO.getPageSize());
        Page<Suggestion> page = suggestionMapper.pageQuery(suggestionPageQueryDTO, role);
        return new PageResult(page.getTotal(), page.getResult());
    }

    public void validate(Long id, Integer status) {
        Suggestion suggestion = Suggestion.builder()
                .id(id)
                .status(status)
                .build();

        suggestionMapper.update(suggestion);
    }
}