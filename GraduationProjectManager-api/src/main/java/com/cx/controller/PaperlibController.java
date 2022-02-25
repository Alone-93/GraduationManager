package com.cx.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.cx.common.Result;
import com.cx.common.ResultCode;
import com.cx.fluentmybatis.entity.PaperlibEntity;
import com.cx.model.PageReq;
import com.cx.service.PaperlibService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "PaperlibController",description = "文库模块")
@RequestMapping("paperlib")
@RestController
public class PaperlibController {
    @Autowired
    PaperlibService paperlibService;

    @SaCheckLogin
    @ApiOperation(value = "分页查询获取文库列表")
    @PostMapping("/getpaperliblist")
    public Result getPaperlibList(@RequestBody @Validated PageReq pageReq){
        return Result.success(paperlibService.getPaperlibList(pageReq));
    }

    @ApiOperation(value = "插入新的论文")
    @SaCheckRole("teacher")
    @PostMapping("insertpaper")
    public Result insertPaper(@RequestBody PaperlibEntity paperlibEntity){
        int res=paperlibService.insert(paperlibEntity);
        if (res>0){
            return Result.success();
        }else {
            return Result.failure(ResultCode.INSERT_ERROR);
        }
    }
}


