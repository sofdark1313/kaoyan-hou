package com.example.springboot_new.ai.mapper;

import com.example.springboot_new.ai.pojo.ChatHistory;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ChatHistoryMapper {
    
    @Select("SELECT * FROM chatHistory WHERE accountId = #{accountId} AND dialogId = #{dialogId}")
    @Results({
        @Result(property = "conversation", column = "conversation", 
                typeHandler = com.example.springboot_new.ai.handler.JsonTypeHandler.class)
    })
    ChatHistory findByAccountIdAndDialogId(@Param("accountId") Integer accountId, @Param("dialogId") Integer dialogId);
    
    @Insert("INSERT INTO chatHistory(accountId, dialogId, conversation) VALUES(#{accountId}, #{dialogId}, #{conversation,typeHandler=com.example.springboot_new.ai.handler.JsonTypeHandler})")
    int insert(ChatHistory chatHistory);
    
    @Update("UPDATE chatHistory SET conversation = #{conversation,typeHandler=com.example.springboot_new.ai.handler.JsonTypeHandler} WHERE accountId = #{accountId} AND dialogId = #{dialogId}")
    int update(ChatHistory chatHistory);
    
    @Delete("DELETE FROM chatHistory WHERE accountId = #{accountId} AND dialogId = #{dialogId}")
    int delete(@Param("accountId") Integer accountId, @Param("dialogId") Integer dialogId);
}