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
import per.cby.terminal.dto.RawdataDto;
import per.cby.terminal.model.Rawdata;
import per.cby.terminal.service.RawdataService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 终端报文数据 前端控制器
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-14
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/rawdata")
@Api(value = "终端报文数据Controller", tags = "终端报文数据前端控制器接口")
public class RawdataController {

    @Autowired
    private RawdataService baseService;

    @GetMapping("/list")
    @ApiOperation("查询报文数据列表")
    public List<Rawdata> list(@RequestModel RawdataDto dto) {
        return baseService.list(dto);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询报文数据")
    public Page<Rawdata> page(Page<Rawdata> page, @RequestModel RawdataDto dto) {
        return baseService.page(page, dto);
    }

    @GetMapping("/expData")
    @ApiOperation("导出数据")
    public void expData(@RequestModel RawdataDto dto, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + IDUtil.createUniqueTimeId() + ".xls");
        try {
            baseService.expData(dto, response.getOutputStream());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
