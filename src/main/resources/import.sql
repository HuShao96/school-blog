INSERT INTO user (user_id, username, password, nickname, email) VALUES (1, 'administrator', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '胡少Administrator', 'i@hushao.com');
INSERT INTO user (user_id, username, password, nickname, email)  VALUES (2, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '胡少Admin', 'hushao@111.com');
INSERT INTO user (user_id, username, password, nickname, email)  VALUES (3, 'hushao', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '胡少', '111@hushao.com');

INSERT INTO authority (id, name) VALUES (1, 'ROLE_ADMINistrator');
INSERT INTO authority (id, name) VALUES (2, 'ROLE_ADMIN');
INSERT INTO authority (id, name) VALUES (3, 'ROLE_USER');

 INSERT INTO user_authority (user_id, authority_id) VALUES (1, 1);
 INSERT INTO user_authority (user_id, authority_id) VALUES (2, 2);
 INSERT INTO user_authority (user_id, authority_id) VALUES (3, 3);

/*省份*/
insert into Province (province_id,province_name) values(1,'北京市');
insert into Province (province_id,province_name) values(2,'天津市');
insert into Province (province_id,province_name) values(3,'上海市');
insert into Province (province_id,province_name) values(4,'重庆市');
insert into Province (province_id,province_name) values(5,'河北省');
insert into Province (province_id,province_name) values(6,'山西省');
insert into Province (province_id,province_name) values(7,'台湾省');
insert into Province (province_id,province_name) values(8,'辽宁省');
insert into Province (province_id,province_name) values(9,'吉林省');
insert into Province (province_id,province_name) values(10,'黑龙江省');
insert into Province (province_id,province_name) values(11,'江苏省');
insert into Province (province_id,province_name) values(12,'浙江省');
insert into Province (province_id,province_name) values(13,'安徽省');
insert into Province (province_id,province_name) values(14,'福建省');
insert into Province (province_id,province_name) values(15,'江西省');
insert into Province (province_id,province_name) values(16,'山东省');
insert into Province (province_id,province_name) values(17,'河南省');
insert into Province (province_id,province_name) values(18,'湖北省');
insert into Province (province_id,province_name) values(19,'湖南省');
insert into Province (province_id,province_name) values(20,'广东省');
insert into Province (province_id,province_name) values(21,'甘肃省');
insert into Province (province_id,province_name) values(22,'四川省');
insert into Province (province_id,province_name) values(23,'贵州省');
insert into Province (province_id,province_name) values(24,'海南省');
insert into Province (province_id,province_name) values(25,'云南省');
insert into Province (province_id,province_name) values(26,'青海省');
insert into Province (province_id,province_name) values(27,'陕西省');
insert into Province (province_id,province_name) values(28,'广西壮族自治区');
insert into Province (province_id,province_name) values(29,'西藏自治区');
insert into Province (province_id,province_name) values(30,'宁夏回族自治区');
insert into Province (province_id,province_name) values(31,'新疆维吾尔自治区');
insert into Province (province_id,province_name) values(32,'内蒙古自治区');
insert into Province (province_id,province_name) values(33,'澳门特别行政区');
insert into Province (province_id,province_name) values(34,'香港特别行政区');

-- 分类表
INSERT INTO catalog VALUES ('1', '0', '九江职业大学', null, '2017-11-06 11:41:51', '15');
INSERT INTO catalog VALUES ('2', '0', '上海大学', null, '2017-11-06 11:41:51', '3');
INSERT INTO catalog VALUES ('3', '0', '九江学院', null, '2017-11-06 11:41:51', '15');
INSERT INTO catalog VALUES ('4', '0', '北京大学', null, '2017-11-06 11:41:51', '1');
INSERT INTO catalog VALUES ('5', '0', '九江财经大学', null, '2017-11-06 11:41:51', '15');

-- 关注表
INSERT INTO attention VALUES ('1', '2017-10-31 20:48:39', '1', '3');
INSERT INTO attention VALUES ('2', '2017-10-31 14:35:58', '1', '1');
INSERT INTO attention VALUES ('3', '2017-10-31 14:37:59', '3', '1');
INSERT INTO attention VALUES ('4', '2017-10-31 14:39:02', '4', '3');

-- 博客表
INSERT INTO blog VALUES ('1', '0', '我创建了这个博客，牛逼不！！', '2017-10-31 14:35:38', '<p>我创建了这个博客，牛逼不！！<br></p>', '1', '我是九江职业大学的学生！', '0', '1', '1');
INSERT INTO blog VALUES ('2', '0', '但是我也创建了这个博客，也好牛逼！', '2017-10-31 14:38:29', '<p>但是我也创建了这个博客，也好牛逼！<br></p>', '1', '我不是九江学院的学生！', '0', '3', '1');
INSERT INTO blog VALUES ('3', '0', '南昌大学世界第一！', '2017-10-31 14:39:32', '<p>南昌大学世界第一！<br></p>', '1', '南昌大学好牛逼！', '0', '4', '3');
INSERT INTO blog VALUES ('4', '1', '牛逼的管理，别删我博客！', '2017-10-31 14:40:09', '<p>牛逼的管理，别删我博客！<br></p>', '11', '我刚来的！', '2', '1', '3');
