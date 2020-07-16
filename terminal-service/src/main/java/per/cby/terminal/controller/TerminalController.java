package per.cby.terminal.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import per.cby.frame.common.util.IDUtil;
import per.cby.frame.web.controller.AbstractController;
import per.cby.terminal.vo.TerminalBindVo;
import per.cby.terminal.bo.MessageIssued;
import per.cby.terminal.bo.TerminalConfig;
import per.cby.terminal.model.Terminal;
import per.cby.terminal.service.TerminalService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 终端 前端控制器
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/terminal")
@Api(value = "终端Controller", tags = "终端前端控制器接口")
public class TerminalController extends AbstractController<TerminalService, Terminal> {

    @GetMapping("/bindInfo")
    @ApiOperation("获取终端绑定信息")
    public TerminalBindVo bindInfo(@ApiParam("模组编号") @RequestParam(required = false) String imei) {
        return baseService.bindInfo(imei);
    }

    @GetMapping("/fotaBindInfo")
    @ApiOperation("获取终端FOTA绑定信息")
    public TerminalBindVo fotaBindInfo(@ApiParam("终端类型编号") @RequestParam(required = false) String typeId,
            @ApiParam("升级编号") @RequestParam(required = false) String upgradeId) {
        return baseService.fotaBindInfo(typeId, upgradeId);
    }

    @GetMapping("/expTpl")
    @ApiOperation("导出数据模板")
    public void expTpl(HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=terminal_template.xls");
        try {
            baseService.expTpl(response.getOutputStream());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @PostMapping("/impData")
    @ApiOperation("导入数据")
    public boolean impData(MultipartFile file) {
        return baseService.impData(file);
    }

    @GetMapping("/expData")
    @ApiOperation("导出数据")
    public void expData(@RequestParam Map<String, Object> param, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + IDUtil.createUniqueTimeId() + ".xls");
        try {
            baseService.expData(param, response.getOutputStream());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @PostMapping("/messageIssued")
    @ApiOperation("报文下发")
    public boolean messageIssued(@RequestBody @Valid MessageIssued messageIssued) {
        return baseService.messageIssued(messageIssued);
    }

    @PostMapping("/config")
    @ApiOperation("终端配置")
    public boolean config(@RequestBody @Valid TerminalConfig config) {
        return baseService.config(config);
    }

}
