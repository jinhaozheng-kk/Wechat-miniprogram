package com.example.mapper;

import com.example.entity.Inquiry;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 询价单 Mapper
 */
public interface InquiryMapper {

    /** 新建询价单（自增 id 回填到入参对象） */
    int insert(Inquiry inquiry);

    /**
     * 询价单列表（支持按状态筛选）
     *
     * @param status 可空，按状态筛选
     */
    List<Inquiry> findList(@Param("status") String status);

    /** 按 id 查询 */
    Inquiry findById(@Param("id") Long id);

    /** 更新状态 */
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    /** 总数 */
    long count();

    /** 按状态分组计数 */
    List<Map<String, Object>> countGroupByStatus();

    /**
     * 最近 N 天每天的询价数（用于折线图）
     *
     * @param days 最近多少天
     * @return [{date, count}]，按日期升序
     */
    List<Map<String, Object>> countByDay(@Param("days") int days);
}
