package per.cby.terminal.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import per.cby.frame.common.util.BaseUtil;
import per.cby.frame.web.annotation.RequestModel;
import per.cby.frame.web.controller.AbstractController;
import per.cby.system.model.Attach;
import per.cby.terminal.model.Fota;
import per.cby.terminal.service.FotaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 终端程序升级 前端控制器
 * </p>
 *
 * @author chenboyang
 * @since 2019-11-04
 */
@Validated
@RestController
@RequestMapping("/fota")
@Api(value = "终端程序升级Controller", tags = "终端程序升级前端控制器接口")
public class FotaController extends AbstractController<FotaService, Fota> {

    @PostMapping("/save")
    @ApiOperation("保存FOTA附件信息")
    public boolean save(@RequestModel Fota fota, @ApiParam("终端编号集") @RequestModel("terminalIds") String terminalIds,
            @ApiParam("是否立即执行升级") @RequestModel("execute") Boolean execute, MultipartFile file) {
        List<String> terminalIdList = null;
        if (StringUtils.isNotBlank(terminalIds)) {
            terminalIdList = BaseUtil.newArrayList(terminalIds.split(","));
        }
        if (file != null) {
            fota.setAttach(Attach.createAttath(file));
        }

        if (Boolean.TRUE.equals(execute)) {
            return baseService.saveAndExecute(fota, terminalIdList);
        }
        return baseService.save(fota, terminalIdList);
    }

    @PostMapping("/execute")
    @ApiOperation("执行FOTA升级")
    public boolean execute(@RequestBody Fota fota) {
        return baseService.execute(fota);
    }

    @PostMapping("/interrupt")
    @ApiOperation("中断FOTA升级")
    public boolean interrupt(@RequestBody Fota fota) {
        return baseService.interrupt(fota);
    }

    @PostMapping("/close")
    @ApiOperation("强制关闭FOTA升级")
    public boolean close(@RequestBody Fota fota) {
        return baseService.close(fota);
    }

    @GetMapping("/check")
    @ApiOperation("检查该类型的终端是否在升级中")
    public boolean check(@ApiParam("终端类型编号") @RequestParam(required = false) String typeId) {
        return baseService.check(typeId);
    }

    @DeleteMapping("/clearCache/{upgradeId}")
    @ApiOperation("清除FOTA升级缓存")
    public boolean clearCache(@ApiParam(value = "升级编号", required = true) @PathVariable("upgradeId") String upgradeId) {
        return baseService.clearCache(upgradeId);
    }

}
