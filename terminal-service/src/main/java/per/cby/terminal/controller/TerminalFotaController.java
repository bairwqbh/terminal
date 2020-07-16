package per.cby.terminal.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import per.cby.frame.web.annotation.MultiRequestBody;
import per.cby.frame.web.controller.AbstractController;
import per.cby.terminal.model.TerminalFota;
import per.cby.terminal.service.TerminalFotaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 终端程序升级关系 前端控制器
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
@Validated
@RestController
@RequestMapping("/terminalFota")
@Api(value = "终端程序升级关系Controller", tags = "终端程序升级关系前端控制器接口")
public class TerminalFotaController extends AbstractController<TerminalFotaService, TerminalFota> {

    @PostMapping("/bind")
    @ApiOperation("绑定FOTA升级终端关系")
    public boolean bind(@ApiParam(value = "升级编号", required = true) @MultiRequestBody String upgradeId,
            @ApiParam("终端编号列表") @MultiRequestBody(required = false) List<String> terminalIdList) {
        return baseService.bind(upgradeId, terminalIdList);
    }

}
