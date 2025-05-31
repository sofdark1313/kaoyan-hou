package com.example.springboot_new.ai.mapper;

import com.example.springboot_new.ai.pojo.AccountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AccountMapper {
    @Select("SELECT accountId, accountName, account, phoneNumber, accountIdentity FROM accountPassword WHERE accountId = #{accountId}")
    AccountInfo getAccountById(Integer accountId);
}