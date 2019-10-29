package com.weixin.common.util.controller;

import com.weixin.common.util.util.qnyUtil.QiNiuYunUtils;
import com.womdata.common.core.model.BaseDataResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class QnyController {

    @PostMapping(value = "/saveFile")
    public BaseDataResponse saveFile(@RequestParam("file") MultipartFile file) {
        String file1Path = QiNiuYunUtils.qiniuFile(file);
        return new BaseDataResponse<>(file1Path);
    }
}
