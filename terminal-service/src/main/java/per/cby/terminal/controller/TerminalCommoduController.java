package per.cby.terminal.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import per.cby.frame.web.annotation.MultiRequestBody;
import per.cby.frame.web.controller.AbstractController;
import per.cby.terminal.model.TerminalCommodu;
import per.cby.terminal.service.TerminalCommoduService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 终端通信模组关系 前端控制器
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
@Validated
@RestController
@RequestMapping("/terminalCommodu")
@Api(value = "终端通信模组关系Controller", tags = "终端通信模组关系前端控制器接口")
public class TerminalCommoduController extends AbstractController<TerminalCommoduService, TerminalCommodu> {

    @PostMapping("/bindCommodu")
    @ApiOperation("绑定终端模组关系")
    public boolean bindCommodu(@ApiParam(value = "终端编号", required = true) @MultiRequestBody String terminalId,
            @ApiParam("模组编号列表") @MultiRequestBody(required = false) List<String> imeiList) {
        return baseService.bindCommodu(terminalId, imeiList);
    }

    @PostMapping("/bindTerminal")
    @ApiOperation("绑定模组终端关系")
    public boolean bindTerminal(@ApiParam(value = "模组编号", required = true) @MultiRequestBody String imei,
            @ApiParam("终端编号") @MultiRequestBody(required = false) String terminalId) {
        return baseService.bindTerminal(imei, terminalId);
    }

}
