-- =======================================================
-- 华信通讯科技有限公司 后端数据库初始化脚本
-- 数据库：MySQL 8.x（端口 3306）
-- 使用方式（务必带 --default-character-set=utf8mb4，否则 Windows 下中文会乱码）：
--   mysql -u root -p --default-character-set=utf8mb4 < schema.sql
-- =======================================================

-- 显式声明客户端连接编码：避免 Windows 命令行 stdin 重定向把 UTF-8 字节误当 GBK
SET NAMES utf8mb4;

-- 1. 创建数据库
DROP DATABASE IF EXISTS huaxin;
CREATE DATABASE huaxin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE huaxin;

-- =======================================================
-- 2. 表结构
-- =======================================================

-- 产品分类表
CREATE TABLE category (
    id            VARCHAR(32)  NOT NULL COMMENT '分类编码（小程序中使用的拼音 id）',
    name          VARCHAR(64)  NOT NULL COMMENT '分类名称',
    icon          VARCHAR(8)            DEFAULT NULL COMMENT '图标文字（单字）',
    color         VARCHAR(16)           DEFAULT NULL COMMENT '主色调',
    bg_color      VARCHAR(16)           DEFAULT NULL COMMENT '背景色',
    image         VARCHAR(255)          DEFAULT NULL COMMENT '封面图路径',
    description   VARCHAR(500)          DEFAULT NULL COMMENT '分类简介',
    product_count INT          NOT NULL DEFAULT 0 COMMENT '产品数（冗余字段，便于展示）',
    sort_order    INT          NOT NULL DEFAULT 0 COMMENT '排序，越小越靠前',
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB COMMENT ='产品分类表';

-- 产品表
CREATE TABLE product (
    id          VARCHAR(32)  NOT NULL COMMENT '产品编码',
    category_id VARCHAR(32)  NOT NULL COMMENT '所属分类编码',
    name        VARCHAR(255) NOT NULL COMMENT '产品名称',
    model       VARCHAR(255)          DEFAULT NULL COMMENT '产品型号',
    images      JSON                  DEFAULT NULL COMMENT '图片路径数组',
    specs       JSON                  DEFAULT NULL COMMENT '规格参数（[{key,value}]）',
    tags        JSON                  DEFAULT NULL COMMENT '标签数组',
    description TEXT                  DEFAULT NULL COMMENT '产品描述',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_category (category_id)
) ENGINE = InnoDB COMMENT ='产品表';

-- 询价单表
CREATE TABLE inquiry (
    id             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    product_ids    JSON                  DEFAULT NULL COMMENT '询价产品 id 列表',
    product_names  VARCHAR(500)          DEFAULT NULL COMMENT '产品名称（冗余，逗号分隔）',
    contact_name   VARCHAR(64)  NOT NULL COMMENT '联系人姓名',
    contact_phone  VARCHAR(32)  NOT NULL COMMENT '联系电话',
    project_name   VARCHAR(255)          DEFAULT NULL COMMENT '工程名称',
    city           VARCHAR(64)           DEFAULT NULL COMMENT '所在城市',
    requirement    TEXT                  DEFAULT NULL COMMENT '需求描述',
    remark         VARCHAR(500)          DEFAULT NULL COMMENT '备注',
    status         VARCHAR(16)  NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING待处理/CONTACTED已联系/CLOSED已成交',
    created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_status (status),
    KEY idx_created (created_at)
) ENGINE = InnoDB COMMENT ='询价单表';

-- =======================================================
-- 3. 测试数据：分类（14 个，与小程序保持一致）
-- =======================================================
INSERT INTO category (id, name, icon, color, bg_color, image, description, product_count, sort_order) VALUES
('guang-xian',    '光纤入户信息箱',    '光', '#1565C0', '#E3F2FD', '/images/guang-xian/1.png',    '适用于光纤入户终端安装，含塑面和铁面两种面板类型',                    6,  1),
('xiao-fang',     '消防端子箱',        '消', '#B71C1C', '#FFEBEE', '/images/xiao-fang/1.png',     '明装消防端子箱/模块箱，适用于消防系统配线安装',                        8,  2),
('duo-mei-ti',    '多媒体信息箱',      '媒', '#1B5E20', '#E8F5E9', '/images/duo-mei-ti/1.png',    '家庭多媒体综合布线箱，整合网络、电话、有线电视等弱电系统',              12, 3),
('deng-dian-wei', '等电位联结端子箱',  '等', '#E65100', '#FFF3E0', '/images/deng-dian-wei/1.png', 'TD-28系列等电位联结端子箱，用于建筑物防雷等电位联结',                   3,  4),
('pz30',          'PZ30回路箱',        '路', '#4A148C', '#F3E5F5', '/images/pz30/1.png',          '梅兰系列PZ30强电回路箱，暗装/明装可选，6~36回路',                       10, 5),
('dian-hua',      '电话箱',            '话', '#006064', '#E0F7FA', '/images/dian-hua/1.jpg',      '电话接线箱，暗装，10~100对容量可选',                                    4,  6),
('dian-shi',      '电视分线箱',        '视', '#33691E', '#F1F8E9', '/images/dian-shi/1.jpg',      '有线电视分配箱，明装，CATV-I/II/III 三种规格',                          3,  7),
('hu-wai-bx',     '户外不锈钢箱',      '钢', '#37474F', '#ECEFF1', '/images/hu-wai-bx/1.png',     '201/304不锈钢防水配电箱，含配电箱、挂墙箱、户外户内箱系列',             40, 8),
('fang-lei',      '防雷测试箱',        '雷', '#F57F17', '#FFFDE7', '/images/fang-lei/1.jpg',      '建筑物防雷接地测试箱，用于接地电阻测量和防雷检测',                      2,  9),
('ji-ye',         '基业箱',            '基', '#BF360C', '#FBE9E7', '/images/ji-ye/1.jpg',         '通用基业配电箱，适用于工厂、仓库等场所',                                3,  10),
('jia-ting-bx',   '不锈钢家庭信息箱',  '家', '#1A237E', '#E8EAF6', '/images/jia-ting-bx/1.jpg',   '不锈钢家庭弱电信息箱，美观耐用',                                        2,  11),
('shui-biao',     '水表箱',            '水', '#006064', '#E0F7FA', '/images/shui-biao/1.jpg',     '户外水表安装计量箱',                                                    1,  12),
('dian-biao',     '电表箱',            '电', '#263238', '#ECEFF1', '/images/dian-biao/1.jpg',     '电能计量箱，适用于居民和工商业用电计量',                                2,  13),
('an-fang',       '安防接线箱',        '安', '#880E4F', '#FCE4EC', '/images/an-fang/1.jpg',       '安防系统接线箱，用于监控、报警设备布线',                                2,  14);

-- =======================================================
-- 4. 测试数据：产品（每个分类抽 2~3 条代表型号，约 30 条）
-- =======================================================
INSERT INTO product (id, category_id, name, model, images, specs, tags) VALUES
-- 光纤入户信息箱
('gx-001', 'guang-xian', '光纤入户信息箱 250×300×120 塑面', '250×300×120mm',
 JSON_ARRAY('/images/guang-xian/1.png','/images/guang-xian/2.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','规格尺寸','value','250×300×120 mm'),JSON_OBJECT('key','面板类型','value','塑面'),JSON_OBJECT('key','板厚','value','0.8mm / 1.0mm 可选'),JSON_OBJECT('key','安装方式','value','明装 / 暗装')),
 JSON_ARRAY('塑面','光纤入户')),
('gx-002', 'guang-xian', '光纤入户信息箱 300×350×120 塑面', '300×350×120mm',
 JSON_ARRAY('/images/guang-xian/1.png','/images/guang-xian/2.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','规格尺寸','value','300×350×120 mm'),JSON_OBJECT('key','面板类型','value','塑面'),JSON_OBJECT('key','板厚','value','0.8mm / 1.0mm 可选'),JSON_OBJECT('key','安装方式','value','明装 / 暗装')),
 JSON_ARRAY('塑面','光纤入户')),
('gx-004', 'guang-xian', '光纤入户信息箱 250×300×120 铁面', '250×300×120mm',
 JSON_ARRAY('/images/guang-xian/1.png','/images/guang-xian/2.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','规格尺寸','value','250×300×120 mm'),JSON_OBJECT('key','面板类型','value','铁面'),JSON_OBJECT('key','板厚','value','0.8mm / 1.0mm'),JSON_OBJECT('key','安装方式','value','明装 / 暗装')),
 JSON_ARRAY('铁面','光纤入户')),

-- 消防端子箱
('xf-001', 'xiao-fang', '明装消防端子箱 200×200×80', '200×200×80mm',
 JSON_ARRAY('/images/xiao-fang/1.png','/images/xiao-fang/2.png'),
 JSON_ARRAY(JSON_OBJECT('key','规格尺寸','value','200×200×80 mm'),JSON_OBJECT('key','箱体/门板/底板厚','value','0.5 / 0.6 / 0.6 mm'),JSON_OBJECT('key','安装方式','value','明装')),
 JSON_ARRAY('明装','消防')),
('xf-004', 'xiao-fang', '明装消防端子箱 300×300×100', '300×300×100mm',
 JSON_ARRAY('/images/xiao-fang/1.png','/images/xiao-fang/2.png'),
 JSON_ARRAY(JSON_OBJECT('key','规格尺寸','value','300×300×100 mm'),JSON_OBJECT('key','箱体/门板/底板厚','value','0.5 / 0.6 / 0.6 mm'),JSON_OBJECT('key','安装方式','value','明装')),
 JSON_ARRAY('明装','消防')),
('xf-007', 'xiao-fang', '明装消防端子箱 600×500×100', '600×500×100mm',
 JSON_ARRAY('/images/xiao-fang/1.png','/images/xiao-fang/2.png'),
 JSON_ARRAY(JSON_OBJECT('key','规格尺寸','value','600×500×100 mm'),JSON_OBJECT('key','箱体/门板/底板厚','value','0.6 / 0.6 / 0.6 mm'),JSON_OBJECT('key','安装方式','value','明装')),
 JSON_ARRAY('明装','消防')),

-- 多媒体信息箱
('dmt-003', 'duo-mei-ti', '多媒体信息箱 200×300×100 (0.6/1.0)', '200×300×100mm',
 JSON_ARRAY('/images/duo-mei-ti/1.png','/images/duo-mei-ti/2.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','规格尺寸','value','200×300×100 mm'),JSON_OBJECT('key','箱体/面板厚','value','0.6 / 1.0 mm'),JSON_OBJECT('key','安装方式','value','明装 / 暗装'),JSON_OBJECT('key','备注','value','★ 推荐款')),
 JSON_ARRAY('多媒体','弱电','推荐')),
('dmt-006', 'duo-mei-ti', '多媒体信息箱 250×300×100 (0.8/1.0)', '250×300×100mm',
 JSON_ARRAY('/images/duo-mei-ti/1.png','/images/duo-mei-ti/2.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','规格尺寸','value','250×300×100 mm'),JSON_OBJECT('key','箱体/面板厚','value','0.8 / 1.0 mm'),JSON_OBJECT('key','安装方式','value','明装 / 暗装')),
 JSON_ARRAY('多媒体','弱电')),
('dmt-007', 'duo-mei-ti', '多媒体信息箱 280×380×90 (0.5/0.8)', '280×380×90mm',
 JSON_ARRAY('/images/duo-mei-ti/1.png','/images/duo-mei-ti/2.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','规格尺寸','value','280×380×90 mm'),JSON_OBJECT('key','箱体/面板厚','value','0.5 / 0.8 mm'),JSON_OBJECT('key','安装方式','value','明装 / 暗装')),
 JSON_ARRAY('多媒体','弱电')),

-- 等电位联结端子箱
('ddw-001', 'deng-dian-wei', 'TD-28 等电位联结端子箱 小号', 'TD-28-S',
 JSON_ARRAY('/images/deng-dian-wei/1.png'),
 JSON_ARRAY(JSON_OBJECT('key','规格尺寸','value','200×150×80 mm'),JSON_OBJECT('key','执行标准','value','GB 50057'),JSON_OBJECT('key','适用场景','value','建筑物防雷等电位联结')),
 JSON_ARRAY('等电位','防雷')),
('ddw-002', 'deng-dian-wei', 'TD-28 等电位联结端子箱 中号', 'TD-28-M',
 JSON_ARRAY('/images/deng-dian-wei/1.png'),
 JSON_ARRAY(JSON_OBJECT('key','规格尺寸','value','250×200×100 mm'),JSON_OBJECT('key','执行标准','value','GB 50057'),JSON_OBJECT('key','适用场景','value','建筑物防雷等电位联结')),
 JSON_ARRAY('等电位','防雷')),

-- PZ30 回路箱（暂用样例规格，后续可补全）
('pz30-001', 'pz30', '梅兰 PZ30 暗装 12 回路', 'PZ30-12',
 JSON_ARRAY('/images/pz30/1.png'),
 JSON_ARRAY(JSON_OBJECT('key','回路数','value','12'),JSON_OBJECT('key','安装方式','value','暗装'),JSON_OBJECT('key','适用场景','value','住宅/商铺')),
 JSON_ARRAY('PZ30','暗装')),
('pz30-002', 'pz30', '梅兰 PZ30 明装 24 回路', 'PZ30-24',
 JSON_ARRAY('/images/pz30/1.png'),
 JSON_ARRAY(JSON_OBJECT('key','回路数','value','24'),JSON_OBJECT('key','安装方式','value','明装'),JSON_OBJECT('key','适用场景','value','商业/办公')),
 JSON_ARRAY('PZ30','明装')),

-- 电话箱
('dh-001', 'dian-hua', '电话接线箱 暗装 10 对', 'DH-10',
 JSON_ARRAY('/images/dian-hua/1.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','容量','value','10 对'),JSON_OBJECT('key','安装方式','value','暗装')),
 JSON_ARRAY('电话','暗装')),
('dh-003', 'dian-hua', '电话接线箱 暗装 50 对', 'DH-50',
 JSON_ARRAY('/images/dian-hua/1.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','容量','value','50 对'),JSON_OBJECT('key','安装方式','value','暗装')),
 JSON_ARRAY('电话','暗装')),

-- 电视分线箱
('dtv-001', 'dian-shi', '有线电视分配箱 CATV-I', 'CATV-I',
 JSON_ARRAY('/images/dian-shi/1.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','规格','value','CATV-I 小型分线箱'),JSON_OBJECT('key','安装方式','value','明装')),
 JSON_ARRAY('电视','分线')),
('dtv-002', 'dian-shi', '有线电视分配箱 CATV-II', 'CATV-II',
 JSON_ARRAY('/images/dian-shi/1.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','规格','value','CATV-II 中型分线箱'),JSON_OBJECT('key','安装方式','value','明装')),
 JSON_ARRAY('电视','分线')),

-- 户外不锈钢箱
('bxpd-001', 'hu-wai-bx', '不锈钢防水配电箱 300×400×150', 'BXPD-300×400×150',
 JSON_ARRAY('/images/hu-wai-bx/1.png'),
 JSON_ARRAY(JSON_OBJECT('key','规格尺寸','value','300×400×150 mm'),JSON_OBJECT('key','材质','value','201 不锈钢'),JSON_OBJECT('key','防护等级','value','IP54')),
 JSON_ARRAY('不锈钢','户外','防水')),
('bxpd-005', 'hu-wai-bx', '不锈钢防水配电箱 400×500×200', 'BXPD-400×500×200',
 JSON_ARRAY('/images/hu-wai-bx/1.png'),
 JSON_ARRAY(JSON_OBJECT('key','规格尺寸','value','400×500×200 mm'),JSON_OBJECT('key','材质','value','304 不锈钢'),JSON_OBJECT('key','防护等级','value','IP54')),
 JSON_ARRAY('不锈钢','户外','304')),
('bxgq-001', 'hu-wai-bx', '不锈钢挂墙箱 400×500×200', 'BXGQ-400×500×200',
 JSON_ARRAY('/images/hu-wai-bx/1.png'),
 JSON_ARRAY(JSON_OBJECT('key','规格尺寸','value','400×500×200 mm'),JSON_OBJECT('key','材质','value','201 不锈钢'),JSON_OBJECT('key','安装方式','value','挂墙')),
 JSON_ARRAY('不锈钢','挂墙')),

-- 防雷测试箱
('fl-001', 'fang-lei', '防雷接地测试箱 标准型', 'FL-STD',
 JSON_ARRAY('/images/fang-lei/1.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','适用场景','value','建筑物防雷接地测量'),JSON_OBJECT('key','执行标准','value','GB 50057')),
 JSON_ARRAY('防雷','测试')),
('fl-002', 'fang-lei', '防雷接地测试箱 加大型', 'FL-LRG',
 JSON_ARRAY('/images/fang-lei/1.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','适用场景','value','大型建筑物防雷'),JSON_OBJECT('key','执行标准','value','GB 50057')),
 JSON_ARRAY('防雷','测试')),

-- 基业箱
('jy-001', 'ji-ye', '基业配电箱 300×400×150', 'JY-300×400×150',
 JSON_ARRAY('/images/ji-ye/1.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','规格尺寸','value','300×400×150 mm'),JSON_OBJECT('key','材质','value','冷轧钢板'),JSON_OBJECT('key','适用场景','value','工厂/仓库')),
 JSON_ARRAY('基业','工业')),
('jy-002', 'ji-ye', '基业配电箱 400×500×200', 'JY-400×500×200',
 JSON_ARRAY('/images/ji-ye/1.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','规格尺寸','value','400×500×200 mm'),JSON_OBJECT('key','材质','value','冷轧钢板'),JSON_OBJECT('key','适用场景','value','工厂/仓库')),
 JSON_ARRAY('基业','工业')),

-- 不锈钢家庭信息箱
('jtbx-001', 'jia-ting-bx', '不锈钢家庭信息箱 250×300×100', 'JTBX-250×300×100',
 JSON_ARRAY('/images/jia-ting-bx/1.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','规格尺寸','value','250×300×100 mm'),JSON_OBJECT('key','材质','value','201 不锈钢'),JSON_OBJECT('key','适用场景','value','家庭弱电')),
 JSON_ARRAY('不锈钢','家庭')),
('jtbx-002', 'jia-ting-bx', '不锈钢家庭信息箱 300×400×120', 'JTBX-300×400×120',
 JSON_ARRAY('/images/jia-ting-bx/1.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','规格尺寸','value','300×400×120 mm'),JSON_OBJECT('key','材质','value','201 不锈钢'),JSON_OBJECT('key','适用场景','value','家庭弱电')),
 JSON_ARRAY('不锈钢','家庭')),

-- 水表箱
('sb-001', 'shui-biao', '户外水表箱 单户', 'SB-1',
 JSON_ARRAY('/images/shui-biao/1.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','容量','value','1 户'),JSON_OBJECT('key','适用场景','value','户外水表安装')),
 JSON_ARRAY('水表','户外')),

-- 电表箱
('db-001', 'dian-biao', '居民单户电表箱', 'DB-1',
 JSON_ARRAY('/images/dian-biao/1.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','容量','value','1 户'),JSON_OBJECT('key','适用场景','value','居民用电计量')),
 JSON_ARRAY('电表','居民')),
('db-002', 'dian-biao', '工商业三户电表箱', 'DB-3',
 JSON_ARRAY('/images/dian-biao/1.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','容量','value','3 户'),JSON_OBJECT('key','适用场景','value','工商业用电计量')),
 JSON_ARRAY('电表','工商业')),

-- 安防接线箱
('af-001', 'an-fang', '安防接线箱 标准型', 'AF-STD',
 JSON_ARRAY('/images/an-fang/1.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','适用场景','value','监控/报警'),JSON_OBJECT('key','安装方式','value','明装')),
 JSON_ARRAY('安防','接线')),
('af-002', 'an-fang', '安防接线箱 加大型', 'AF-LRG',
 JSON_ARRAY('/images/an-fang/1.jpg'),
 JSON_ARRAY(JSON_OBJECT('key','适用场景','value','大型安防系统'),JSON_OBJECT('key','安装方式','value','明装')),
 JSON_ARRAY('安防','接线'));

-- =======================================================
-- 5. 测试数据：询价单（覆盖最近 7 天，含不同状态，便于图表展示）
-- =======================================================
INSERT INTO inquiry (product_ids, product_names, contact_name, contact_phone, project_name, city, requirement, remark, status, created_at) VALUES
(JSON_ARRAY('gx-001','gx-002'), '光纤入户信息箱 250×300×120 塑面,光纤入户信息箱 300×350×120 塑面',
 '张工', '13800138001', '杭州滨江某住宅项目', '杭州市',
 '需要光纤入户信息箱共 200 套，要求 1.0mm 板厚', '客户已确认型号', 'CONTACTED', DATE_SUB(NOW(), INTERVAL 6 DAY)),
(JSON_ARRAY('xf-004'), '明装消防端子箱 300×300×100',
 '李经理', '13900139002', '宁波某商业综合体', '宁波市',
 '消防端子箱 300×300×100 共 80 套', '需提供 3C 认证', 'PENDING', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(JSON_ARRAY('dmt-003','dmt-006'), '多媒体信息箱 200×300×100 (0.6/1.0),多媒体信息箱 250×300×100 (0.8/1.0)',
 '王总', '13700137003', '温州某酒店', '温州市',
 '多媒体信息箱 50 套', NULL, 'PENDING', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(JSON_ARRAY('bxpd-005'), '不锈钢防水配电箱 400×500×200',
 '陈先生', '13600136004', '上海某工厂户外配电改造', '上海市',
 '304 不锈钢防水配电箱 30 套，IP54', '本月内需到货', 'PENDING', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(JSON_ARRAY('pz30-001'), '梅兰 PZ30 暗装 12 回路',
 '赵总', '13500135005', '苏州工业园某厂房', '苏州市',
 'PZ30 暗装 12 回路 100 套', NULL, 'CLOSED', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(JSON_ARRAY('fl-001','fl-002'), '防雷接地测试箱 标准型,防雷接地测试箱 加大型',
 '孙工', '13400134006', '某高层建筑防雷工程', '南京市',
 '防雷测试箱共 12 套', '需现场调试', 'PENDING', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(JSON_ARRAY('jy-002'), '基业配电箱 400×500×200',
 '周经理', '13300133007', '某物流仓库', '合肥市',
 '基业箱 25 套', NULL, 'CONTACTED', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(JSON_ARRAY('af-001','af-002'), '安防接线箱 标准型,安防接线箱 加大型',
 '吴先生', '13200132008', '某园区监控系统升级', '杭州市',
 '安防接线箱共 18 套', '附详细布点图，已发邮件', 'PENDING', NOW());

-- =======================================================
-- 完成
-- =======================================================
-- 数据汇总：
-- - category 表：14 条
-- - product 表：约 30 条（覆盖全部 14 个分类）
-- - inquiry 表：8 条（最近 7 天，覆盖 PENDING / CONTACTED / CLOSED 三种状态）
-- =======================================================
