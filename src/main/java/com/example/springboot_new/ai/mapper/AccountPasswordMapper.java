package com.example.springboot_new.ai.mapper;

import com.example.springboot_new.ai.pojo.AccountPassword;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountPasswordMapper {
    
    @Select("SELECT * FROM accountPassword WHERE account = #{account}")
    AccountPassword findByAccount(String account);
    
    @Select("SELECT COUNT(*) FROM accountPassword WHERE account = #{account}")
    int countByAccount(String account);
    
    @Insert("INSERT INTO accountPassword(accountName, account, password, phoneNumber, accountIdentity, messageContent) VALUES(#{accountName}, #{account}, #{password}, #{phoneNumber}, #{accountIdentity}, #{messageContent})")
    @Options(useGeneratedKeys = true, keyProperty = "accountId")
    int insert(AccountPassword accountPassword);
    
    @Update("UPDATE accountPassword SET password = #{password} WHERE accountId = #{accountId}")
    int updatePassword(AccountPassword accountPassword);
    
    @Delete("DELETE FROM accountPassword WHERE accountId = #{accountId}")
    int deleteById(Integer accountId);
    
    // 新增方法：根据手机号查找账户
    @Select("SELECT * FROM accountPassword WHERE phoneNumber = #{phoneNumber}")
    AccountPassword findByPhoneNumber(String phoneNumber);
    
    // 新增方法：更新账户身份
    @Update("UPDATE accountPassword SET accountIdentity = #{accountIdentity} WHERE accountId = #{accountId}")
    int updateAccountIdentity(AccountPassword accountPassword);
}