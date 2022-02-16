package com.study.mapper;


import com.study.domain.SysUser;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 35612
 */
public interface SysUserMapper  {
    @Select("SELECT id,username,`password`,`status` FROM sys_user WHERE username = #{username} ")
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(property = "roleList",column = "id",javaType = List.class,many = @Many(select = "com.study.mapper.SysRoleMapper.findRoleListByUid"))
    })
    SysUser findByUsername(String username);
}
