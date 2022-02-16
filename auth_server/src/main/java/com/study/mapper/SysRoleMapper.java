package com.study.mapper;

import com.study.domain.SysRole;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author 35612
 */
public interface SysRoleMapper  {
    @Select("SELECT id,ROLE_NAME roleName,ROLE_DESC roleDesc FROM sys_role  sr,sys_user_role sur WHERE sr.ID = sur.RID AND sur.UID = #{uId} ")
    List<SysRole> findRoleListByUid(Integer uId);
}
