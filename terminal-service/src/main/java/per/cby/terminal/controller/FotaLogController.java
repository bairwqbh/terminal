package per.cby.terminal.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import per.cby.frame.common.model.Page;
import per.cby.frame.common.util.IDUtil;
import per.cby.frame.web.annotation.RequestModel;
import per.cby.terminal.dto.FotaLogDto;
import per.cby.terminal.model.FotaLog;
import per.cby.terminal.service.FotaLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * FOTA升级日志 前端控制器
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-14
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/fotaLog")
@Api(value = "FOTA升级日志Controller", tags = "FOTA升级日志前端控制器接口")
public class FotaLogController {

    @Autowired
    private FotaLogService baseService;

    @GetMapping("/list")
    @ApiOperation("查询FOTA升级日志列表")
    public List<FotaLog> list(@RequestModel FotaLogDto dto) {
        return baseService.list(dto);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询FOTA日志记录")
    public Page<FotaLog> page(Page<FotaLog> page, @RequestModel FotaLogDto dto) {
        return baseService.page(page, dto);
    }

    @GetMapping("/exp")
    @ApiOperation("导出FOTA升级日志")
    public void exp(@RequestModel FotaLogDto dto, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + IDUtil.createUniqueTimeId() + ".xls");
        try {
            baseService.exp(dto, response.getOutputStream());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
