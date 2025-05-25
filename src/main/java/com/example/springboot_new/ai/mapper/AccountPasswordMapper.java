package com.example.springboot_new.ai.mapper;

import com.example.springboot_new.ai.pojo.AccountPassword;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountPasswordMapper {
    
    @Select("SELECT * FROM accountPassword WHERE account = #{account}")
    AccountPassword findByAccount(String account);
    
    @Select("SELECT COUNT(*) FROM accountPassword WHERE account = #{account}")
    int countByAccount(String account);
    
    @Insert("INSERT INTO accountPassword(accountName, account, password, accountIdentity) VALUES(#{accountName}, #{account}, #{password}, #{accountIdentity})")
    @Options(useGeneratedKeys = true, keyProperty = "accountId")
    int insert(AccountPassword accountPassword);
    
    @Update("UPDATE accountPassword SET password = #{password} WHERE accountId = #{accountId}")
    int updatePassword(AccountPassword accountPassword);
    
    @Delete("DELETE FROM accountPassword WHERE accountId = #{accountId}")
    int deleteById(Integer accountId);
}