package per.cby.terminal.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import per.cby.frame.common.base.AbstractService;
import per.cby.frame.common.base.BaseMapper;
import per.cby.frame.common.base.BaseModel;
import per.cby.frame.common.base.BaseService;
import per.cby.frame.common.util.BaseUtil;
import per.cby.frame.web.controller.AbstractController;

/**
 * <p>
 * 代码生成器演示
 * </p>
 */
public class MPGenerator {

    /** 表集合 */
    private static final String[] TABLES = { "ter_rawdata", "ter_dataflow" };

    /** 表前缀 */
    private static final String TABLE_PREFIX = "ter_";

    /** 驱动名 */
    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

    /** 连接地址 */
    private static final String URL = "jdbc:mysql://mysql:3306/ecsat_test?useSSL=false";

    /** 用户名 */
    private static final String USERNAME = "ecsat";

    /** 密码 */
    private static final String PASSWORD = "eternal_2019";

    /** 上级目录 */
    private static final String PARENT_PATH = "per.cby.terminal";

    /** WEB代码目录 */
    private static final String WEB_PATH = "D://code//web";

    /** WEB模块目录 */
    private static final String WEB_MODULE = "terminal";

    public static void main(String[] args) {
        generate();
    }

    public static void generate() {
        AutoGenerator ag = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
//        gc.setOutputDir("src\\main\\java");
         gc.setOutputDir("E:\\code");
        gc.setFileOverride(true);
        gc.setOpen(false);
        // gc.setEnableCache(true);
        gc.setAuthor("chenboyang");
        gc.setSwagger2(true);
        gc.setActiveRecord(true);
//        gc.setBaseColumnList(true);
//        gc.setBaseResultMap(true);
        gc.setServiceName("%sService");

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        // gc.setMapperName("%sDao");
        // gc.setXmlName("%sDao");
        // gc.setServiceName("MP%sService");
        // gc.setServiceImplName("%sServiceDiy");
        // gc.setControllerName("%sAction");
        ag.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDriverName(DRIVER_NAME);
        dsc.setUrl(URL);
        dsc.setUsername(USERNAME);
        dsc.setPassword(PASSWORD);
        ag.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();

        // 是否大写命名
        // strategy.setCapitalMode(true);
        // 表名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 设置实体类为lombok模型
        strategy.setEntityLombokModel(true);
        // 此处可以修改为您的表前缀
        strategy.setTablePrefix(TABLE_PREFIX);
        // 需要生成的表
        strategy.setInclude(TABLES);
        strategy.setRestControllerStyle(true);

        strategy.setSuperEntityClass(BaseModel.class.getName());
        strategy.setSuperEntityColumns("id", "create_time", "update_time");
        strategy.setSuperMapperClass(BaseMapper.class.getName());
        strategy.setSuperServiceClass(BaseService.class.getName());
        strategy.setSuperServiceImplClass(AbstractService.class.getName());
        strategy.setSuperControllerClass(AbstractController.class.getName());

        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        // 自定义实体父类
        // strategy.setSuperEntityClass("com.baomidou.mybatisplus.activerecord.Model");
        // 自定义实体，公共字段
        // strategy.setSuperEntityColumns(new String[] { "test_id", "age" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.baomidou.demo.TestService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl");
        // 自定义 controller 父类
        // strategy.setSuperControllerClass("com.baomidou.demo.TestController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        strategy.setEntityBuilderModel(true);

        // strategy.setLogicDeleteFieldName("del_flag");

        ag.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(PARENT_PATH);
        // pc.setModuleName(module);
        pc.setEntity("model");
        // pc.setXml(pc.getMapper());
        // pc.setServiceImpl(pc.getService());
        // pc.setController("controller");
        ag.setPackageInfo(pc);

        // 前端代码生成
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = BaseUtil.newHashMap("module", WEB_MODULE);
                this.setMap(map);
            }
        };
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        focList.add(new FileOutConfig("/per/cby/frame/common/db/mybatis/plus/generator/tpl/web/api.js.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) { // 自定义输入文件名称 return
                return WEB_PATH + "//api//" + WEB_MODULE + "//" + tableInfo.getEntityName() + "Api.js";
            }
        });
        focList.add(new FileOutConfig("/per/cby/frame/common/db/mybatis/plus/generator/tpl/web/view.vue.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) { // 自定义输入文件名称 return
                return WEB_PATH + "//wiews//" + WEB_MODULE + "//" + tableInfo.getEntityPath() + "//index.vue";
            }
        });
        cfg.setFileOutConfigList(focList);
        ag.setCfg(cfg);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
//        InjectionConfig cfg = new InjectionConfig() {
//            @Override
//            public void initMap() {
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
//                this.setMap(map);
//            }
//        };

        // 自定义 xxList.jsp 生成
//        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
//        focList.add(new FileOutConfig("/template/list.jsp.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) { // 自定义输入文件名称 return
//                return "D://my_" + tableInfo.getEntityName() + ".jsp";
//            }
//        });
//        cfg.setFileOutConfigList(focList);
//        ag.setCfg(cfg);

        // 调整 xml 生成目录演示
//        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return "/develop/code/xml/" + tableInfo.getEntityName() + ".xml";
//            }
//        });
//        cfg.setFileOutConfigList(focList);
//        ag.setCfg(cfg);

        // 关闭默认 xml 生成，调整生成 至 根目录
//        TemplateConfig tc = new TemplateConfig();
//        tc.setXml(null);
//        ag.setTemplate(tc);

        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        TemplateConfig tc = new TemplateConfig();
        tc.setEntity("/per/cby/frame/common/db/mybatis/plus/generator/tpl/entity.java");
        tc.setService("/per/cby/frame/common/db/mybatis/plus/generator/tpl/service.java");
        tc.setServiceImpl("/per/cby/frame/common/db/mybatis/plus/generator/tpl/serviceImpl.java");
        tc.setMapper("/per/cby/frame/common/db/mybatis/plus/generator/tpl/mapper.java");
        tc.setXml("/per/cby/frame/common/db/mybatis/plus/generator/tpl/mapper.xml");
        tc.setController("/per/cby/frame/common/db/mybatis/plus/generator/tpl/controller.java");
        ag.setTemplate(tc);

        // 执行生成
        ag.execute();
    }

}
