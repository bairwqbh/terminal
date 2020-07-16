package per.cby.terminal.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import per.cby.frame.common.util.IDUtil;
import per.cby.frame.web.controller.AbstractController;
import per.cby.terminal.vo.CommoduBindVo;
import per.cby.terminal.model.Commodu;
import per.cby.terminal.service.CommoduService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 通信模组 前端控制器
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/commodu")
@Api(value = "通信模组Controller", tags = "通信模组前端控制器接口")
public class CommoduController extends AbstractController<CommoduService, Commodu> {

    @GetMapping("/bindInfo")
    @ApiOperation("获取模组绑定信息")
    public CommoduBindVo bindInfo(@ApiParam("终端编号") @RequestParam(required = false) String terminalId) {
        return baseService.bindInfo(terminalId);
    }

    @GetMapping("/expTpl")
    @ApiOperation("导出数据模板")
    public void expTpl(HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=commodu_template.xls");
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

}
