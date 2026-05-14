-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主機： 127.0.0.1
-- 產生時間： 2023-12-28 20:28:06
-- 伺服器版本： 10.4.28-MariaDB
-- PHP 版本： 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `sa`
--

-- --------------------------------------------------------

--
-- 資料表結構 `administrator`
--

CREATE TABLE `administrator` (
  `administrator_email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `administrator_password` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `administrator`
--

INSERT INTO `administrator` (`administrator_email`, `administrator_password`) VALUES
('root@root.com', 'Root1234');

-- --------------------------------------------------------

--
-- 資料表結構 `daily_diet`
--

CREATE TABLE `daily_diet` (
  `daily_diet_id` int(11) NOT NULL,
  `member_id` int(11) NOT NULL,
  `daily_diet_date` datetime NOT NULL,
  `set_label_id` int(11) NOT NULL,
  `total_calories` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `daily_diet`
--

INSERT INTO `daily_diet` (`daily_diet_id`, `member_id`, `daily_diet_date`, `set_label_id`, `total_calories`) VALUES
(1, 1, '2023-12-25 09:00:00', 1, 475),
(2, 1, '2023-12-25 13:00:00', 2, 675),
(3, 1, '2023-12-25 18:30:02', 3, 620),
(4, 3, '2023-12-27 18:54:02', 1, 50);

-- --------------------------------------------------------

--
-- 資料表結構 `food`
--

CREATE TABLE `food` (
  `food_id` int(11) NOT NULL,
  `food_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `calories` float NOT NULL,
  `six_categories_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `food`
--

INSERT INTO `food` (`food_id`, `food_name`, `calories`, `six_categories_id`) VALUES
(1, '玉米', 152, 1),
(2, '白飯', 200, 48),
(3, '糙米飯', 180, 49),
(4, '燕麥', 330, 50),
(5, '米苔目', 350, 51),
(7, '粄條', 250, 52),
(8, '紫米飯', 225, 47),
(9, '小圓麵包', 250, 46),
(10, '蕎麥粥', 200, 45),
(11, '全穀餅乾', 150, 7),
(12, '油麵', 350, 44),
(13, '湯圓', 250, 43),
(14, '全麥麵包', 80, 42),
(19, '水煮蛋', 80, 59),
(20, '荷包蛋', 100, 58),
(21, '紅豆湯', 180, 57),
(22, '綠豆湯', 180, 56),
(23, '烤鮭魚', 400, 92),
(24, '魚湯', 150, 55),
(25, '煎鯖魚', 350, 13),
(26, '紅燒牛肉', 300, 54),
(27, '紅燒牛肉麵', 550, 14),
(28, '炸雞腿', 350, 15),
(29, '排骨湯', 250, 53),
(30, '炸豬排', 350, 2),
(31, '咖哩飯', 500, 41),
(32, '便當', 750, 16),
(33, '漢堡', 350, 17),
(34, '白醬義大利麵', 500, 102),
(38, '羊肉炒麵', 450, 97),
(39, '煎牛排', 400, 93),
(40, '牛肉燴飯', 500, 96),
(41, '香腸炒飯', 500, 95),
(42, '鐵板豆腐', 250, 94),
(55, '杏仁片', 160, 88),
(56, '核桃', 185, 85),
(57, '腰果', 160, 6),
(58, '夏威夷果', 200, 87),
(59, '松子', 160, 91),
(66, '花生', 160, 90),
(67, '南瓜子', 150, 89),
(70, '花生醬吐司', 250, 106),
(75, '杏仁巧克力', 200, 20),
(76, '胡麻醬', 175, 86),
(77, '全脂牛奶', 150, 5),
(78, '低酯牛奶', 100, 83),
(79, '起司蛋餅', 250, 18),
(80, '水果優格', 150, 107),
(81, '乳酪蛋糕', 300, 104),
(82, '咖啡拿鐵', 100, 84),
(83, '奶茶', 125, 82),
(84, '珍珠奶茶', 300, 105),
(85, '牛奶餅乾', 150, 103),
(86, '奶油起司焗烤飯', 450, 22),
(87, '紅蘿蔔炒蛋', 200, 109),
(88, '海鮮麵', 450, 24),
(89, '炒花椰菜', 70, 65),
(90, '炒高麗菜', 60, 66),
(91, '炒青江菜', 50, 61),
(92, '番茄炒蛋', 200, 23),
(93, '竹筍炒肉絲', 250, 110),
(94, '炒菠菜', 60, 64),
(95, '炒豆芽菜', 50, 62),
(96, '馬鈴薯蛋凱薩蔬果沙拉', 300, 26),
(97, '蔬菜春捲', 200, 25),
(98, '羅宋蛋花湯', 200, 27),
(99, '涼拌小黃瓜', 60, 3),
(100, '烤茄子', 80, 63),
(101, '胡麻醬沙拉', 225, 28),
(102, '蘋果', 52, 74),
(103, '香蕉', 89, 72),
(104, '柳橙', 43, 4),
(105, '葡萄', 69, 71),
(106, '草莓', 35, 73),
(107, '水梨', 57, 70),
(108, '水蜜桃', 39, 68),
(109, '西瓜', 30, 67),
(110, '哈密瓜', 34, 69),
(111, '芒果', 60, 78),
(112, '水果沙拉', 100, 29),
(113, '水果拼盤', 150, 30),
(114, '柳橙汁', 120, 79),
(115, '蘋果汁', 120, 75),
(116, '葡萄汁', 150, 80),
(117, '西瓜汁', 90, 76),
(118, '草莓奶昔', 200, 108),
(119, '香蕉奶昔', 200, 21),
(121, '葡萄乾', 100, 81),
(122, '草莓吐司', 120, 112),
(123, '巧克力吐司', 150, 19),
(124, '藍莓鬆餅', 200, 31),
(125, '草莓千層派', 350, 32),
(126, '橙香水果麵包', 300, 113),
(127, '叉燒拉麵', 500, 101),
(128, '石鍋拌飯', 600, 33),
(129, '烤鴨捲餅', 500, 111),
(130, '小籠包', 250, 99),
(131, '花生豆花', 250, 34),
(132, '豬肉刈包', 350, 98),
(133, '魚丸湯', 150, 60),
(134, '釋迦', 125, 77),
(135, '香雞飯糰', 350, 100);

-- --------------------------------------------------------

--
-- 資料表結構 `food_linking_daily_diet`
--

CREATE TABLE `food_linking_daily_diet` (
  `food_id` int(11) NOT NULL,
  `daily_diet_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `food_linking_daily_diet`
--

INSERT INTO `food_linking_daily_diet` (`food_id`, `daily_diet_id`) VALUES
(135, 1),
(83, 1),
(33, 2),
(118, 2),
(134, 2),
(111, 3),
(97, 3),
(55, 3),
(92, 3);

-- --------------------------------------------------------

--
-- 資料表結構 `food_linking_menu`
--

CREATE TABLE `food_linking_menu` (
  `food_id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  `set_label_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `food_linking_menu`
--

INSERT INTO `food_linking_menu` (`food_id`, `menu_id`, `set_label_id`) VALUES
(79, 1, 1),
(57, 1, 1),
(97, 1, 1),
(32, 1, 2),
(80, 1, 2),
(133, 1, 2),
(34, 1, 3),
(91, 1, 3),
(109, 1, 3),
(126, 2, 1),
(21, 2, 1),
(33, 2, 2),
(118, 2, 2),
(58, 2, 2),
(39, 2, 3),
(77, 2, 3),
(101, 2, 3),
(122, 3, 1),
(82, 3, 1),
(2, 3, 2),
(100, 3, 2),
(42, 3, 2),
(105, 3, 2),
(130, 3, 3),
(29, 3, 3),
(66, 3, 3),
(14, 4, 1),
(67, 4, 1),
(86, 4, 2),
(19, 4, 2),
(102, 4, 2),
(5, 4, 3),
(22, 4, 3),
(91, 4, 3),
(9, 5, 1),
(78, 5, 1),
(20, 5, 1),
(2, 5, 2),
(87, 5, 2),
(104, 5, 2),
(56, 5, 2),
(96, 5, 3),
(83, 5, 3),
(1, 5, 3),
(121, 5, 3);

-- --------------------------------------------------------

--
-- 資料表結構 `image`
--

CREATE TABLE `image` (
  `image_id` int(11) NOT NULL,
  `image_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `member`
--

CREATE TABLE `member` (
  `member_id` int(11) NOT NULL,
  `member_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `member_email` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `member_password` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `member`
--

INSERT INTO `member` (`member_id`, `member_name`, `member_email`, `member_password`) VALUES
(1, '珞神', 'luoshin@gmail.com', 'Aa123456'),
(3, 'test', 'test@gmail.com', 'Aa5123456');

-- --------------------------------------------------------

--
-- 資料表結構 `menu`
--

CREATE TABLE `menu` (
  `menu_id` int(11) NOT NULL,
  `six_categories_id` int(11) NOT NULL,
  `total_calories` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `menu`
--

INSERT INTO `menu` (`menu_id`, `six_categories_id`, `total_calories`) VALUES
(1, 35, 2240),
(2, 36, 2005),
(3, 37, 1254),
(4, 38, 1492),
(5, 39, 1755);

-- --------------------------------------------------------

--
-- 資料表結構 `personal_inbody`
--

CREATE TABLE `personal_inbody` (
  `member_id` int(11) NOT NULL,
  `personal_inbody_date` datetime NOT NULL,
  `age` int(11) NOT NULL,
  `gender` varchar(50) NOT NULL,
  `height` float NOT NULL,
  `personal_inbody_weight` float NOT NULL,
  `self_activity` varchar(50) NOT NULL,
  `basal_metabolic_rate` float NOT NULL,
  `total_daily_energy_expenditure` float NOT NULL,
  `six_categories_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- 傾印資料表的資料 `personal_inbody`
--

INSERT INTO `personal_inbody` (`member_id`, `personal_inbody_date`, `age`, `gender`, `height`, `personal_inbody_weight`, `self_activity`, `basal_metabolic_rate`, `total_daily_energy_expenditure`, `six_categories_id`) VALUES
(1, '2023-12-20 13:05:53', 21, '女', 151, 45, '正常', 1129, 1749.95, 40);

-- --------------------------------------------------------

--
-- 資料表結構 `set_label`
--

CREATE TABLE `set_label` (
  `set_label_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `set_label`
--

INSERT INTO `set_label` (`set_label_id`) VALUES
(1),
(2),
(3);

-- --------------------------------------------------------

--
-- 資料表結構 `six_categories`
--

CREATE TABLE `six_categories` (
  `six_categories_id` int(11) NOT NULL,
  `six_categories_label` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `grains` float NOT NULL DEFAULT 0,
  `meats_and_protein` float NOT NULL DEFAULT 0,
  `vegetables` float NOT NULL DEFAULT 0,
  `fruits` float NOT NULL DEFAULT 0,
  `milk_and_products` float NOT NULL DEFAULT 0,
  `fats` float NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `six_categories`
--

INSERT INTO `six_categories` (`six_categories_id`, `six_categories_label`, `grains`, `meats_and_protein`, `vegetables`, `fruits`, `milk_and_products`, `fats`) VALUES
(1, 'food', 1, 0, 0, 0, 0, 0),
(2, 'food', 0, 1, 0, 0, 0, 0),
(3, 'food', 0, 0, 1, 0, 0, 0),
(4, 'food', 0, 0, 0, 1, 0, 0),
(5, 'food', 0, 0, 0, 0, 1, 0),
(6, 'food', 0, 0, 0, 0, 0, 1),
(7, 'food', 0.5, 0, 0, 0, 0, 0),
(8, 'food', 0, 0.5, 0, 0, 0, 0),
(9, 'food', 0, 0, 0.5, 0, 0, 0),
(10, 'food', 0, 0, 0, 0.5, 0, 0),
(11, 'food', 0, 0, 0, 0, 0.5, 0),
(12, 'food', 0, 0, 0, 0, 0, 0.5),
(13, 'food', 0, 2, 0, 0, 0, 0),
(14, 'food', 1, 1, 0, 0, 0, 0),
(15, 'food', 0, 1, 0, 0, 0, 1),
(16, 'food', 1, 2, 2, 0, 0, 0),
(17, 'food', 1, 1, 1, 0, 0, 0),
(18, 'food', 1, 0, 0, 0, 1, 0),
(19, 'food', 1, 0, 0, 0, 0, 1),
(20, 'food', 0, 0, 0, 0, 0, 2),
(21, 'food', 0, 0, 0, 1, 1, 0),
(22, 'food', 1, 0, 0, 0, 2, 0),
(23, 'food', 0, 1, 1, 0, 0, 0),
(24, 'food', 1, 2, 0, 0, 0, 0),
(25, 'food', 1, 0, 1, 0, 0, 0),
(26, 'food', 1, 1, 1, 1, 0, 1),
(27, 'food', 0, 1, 2, 0, 0, 0),
(28, 'food', 0, 0, 1, 0, 0, 1),
(29, 'food', 0, 0, 1, 1, 0, 0),
(30, 'food', 0, 0, 0, 2, 0, 0),
(31, 'food', 1, 0, 0, 1, 0, 0),
(32, 'food', 1, 1, 0, 1, 1, 0),
(33, 'food', 1, 1, 0, 1, 0, 0),
(34, 'food', 0, 1, 0, 0, 0, 1),
(35, 'menu', 4, 3, 4, 2, 3, 1),
(36, 'menu', 2, 5, 2, 2, 2, 2),
(37, 'menu', 3, 3, 1, 2, 1, 1),
(38, 'menu', 3, 2, 1, 1, 2, 1),
(39, 'menu', 4, 3, 2, 3, 2, 2),
(40, 'member', 3, 5, 1.5, 3, 2, 4),
(41, 'food', 1, 0, 0, 0, 0, 0),
(42, 'food', 1, 0, 0, 0, 0, 0),
(43, 'food', 1, 0, 0, 0, 0, 0),
(44, 'food', 1, 0, 0, 0, 0, 0),
(45, 'food', 1, 0, 0, 0, 0, 0),
(46, 'food', 1, 0, 0, 0, 0, 0),
(47, 'food', 1, 0, 0, 0, 0, 0),
(48, 'food', 1, 0, 0, 0, 0, 0),
(49, 'food', 1, 0, 0, 0, 0, 0),
(50, 'food', 1, 0, 0, 0, 0, 0),
(51, 'food', 1, 0, 0, 0, 0, 0),
(52, 'food', 1, 0, 0, 0, 0, 0),
(53, 'food', 0, 1, 0, 0, 0, 0),
(54, 'food', 0, 1, 0, 0, 0, 0),
(55, 'food', 0, 1, 0, 0, 0, 0),
(56, 'food', 0, 1, 0, 0, 0, 0),
(57, 'food', 0, 1, 0, 0, 0, 0),
(58, 'food', 0, 1, 0, 0, 0, 0),
(59, 'food', 0, 1, 0, 0, 0, 0),
(60, 'food', 0, 1, 0, 0, 0, 0),
(61, 'food', 0, 0, 1, 0, 0, 0),
(62, 'food', 0, 0, 1, 0, 0, 0),
(63, 'food', 0, 0, 1, 0, 0, 0),
(64, 'food', 0, 0, 1, 0, 0, 0),
(65, 'food', 0, 0, 1, 0, 0, 0),
(66, 'food', 0, 0, 1, 0, 0, 0),
(67, 'food', 0, 0, 0, 1, 0, 0),
(68, 'food', 0, 0, 0, 1, 0, 0),
(69, 'food', 0, 0, 0, 1, 0, 0),
(70, 'food', 0, 0, 0, 1, 0, 0),
(71, 'food', 0, 0, 0, 1, 0, 0),
(72, 'food', 0, 0, 0, 1, 0, 0),
(73, 'food', 0, 0, 0, 1, 0, 0),
(74, 'food', 0, 0, 0, 1, 0, 0),
(75, 'food', 0, 0, 0, 1, 0, 0),
(76, 'food', 0, 0, 0, 1, 0, 0),
(77, 'food', 0, 0, 0, 1, 0, 0),
(78, 'food', 0, 0, 0, 1, 0, 0),
(79, 'food', 0, 0, 0, 1, 0, 0),
(80, 'food', 0, 0, 0, 1, 0, 0),
(81, 'food', 0, 0, 0, 1, 0, 0),
(82, 'food', 0, 0, 0, 0, 1, 0),
(83, 'food', 0, 0, 0, 0, 1, 0),
(84, 'food', 0, 0, 0, 0, 1, 0),
(85, 'food', 0, 0, 0, 0, 0, 1),
(86, 'food', 0, 0, 0, 0, 0, 1),
(87, 'food', 0, 0, 0, 0, 0, 1),
(88, 'food', 0, 0, 0, 0, 0, 1),
(89, 'food', 0, 0, 0, 0, 0, 1),
(90, 'food', 0, 0, 0, 0, 0, 1),
(91, 'food', 0, 0, 0, 0, 0, 1),
(92, 'food', 0, 2, 0, 0, 0, 0),
(93, 'food', 0, 2, 0, 0, 0, 0),
(94, 'food', 0, 2, 0, 0, 0, 0),
(95, 'food', 1, 1, 0, 0, 0, 0),
(96, 'food', 1, 1, 0, 0, 0, 0),
(97, 'food', 1, 1, 0, 0, 0, 0),
(98, 'food', 1, 1, 0, 0, 0, 0),
(99, 'food', 1, 1, 0, 0, 0, 0),
(100, 'food', 1, 1, 0, 0, 0, 0),
(101, 'food', 1, 1, 0, 0, 0, 0),
(102, 'food', 1, 0, 0, 0, 1, 0),
(103, 'food', 1, 0, 0, 0, 1, 0),
(104, 'food', 1, 0, 0, 0, 1, 0),
(105, 'food', 1, 0, 0, 0, 1, 0),
(106, 'food', 1, 0, 0, 0, 0, 1),
(107, 'food', 0, 0, 0, 1, 1, 0),
(108, 'food', 0, 0, 0, 1, 1, 0),
(109, 'food', 0, 1, 1, 0, 0, 0),
(110, 'food', 0, 1, 1, 0, 0, 0),
(111, 'food', 1, 2, 0, 0, 0, 0),
(112, 'food', 1, 0, 0, 1, 0, 0),
(113, 'food', 1, 1, 0, 1, 0, 0);

-- --------------------------------------------------------

--
-- 資料表結構 `weight_change`
--

CREATE TABLE `weight_change` (
  `weight_change_id` int(11) NOT NULL,
  `weight_change_date` datetime NOT NULL,
  `weight_change_weight` float NOT NULL,
  `member_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `weight_change`
--

INSERT INTO `weight_change` (`weight_change_id`, `weight_change_date`, `weight_change_weight`, `member_id`) VALUES
(1, '2023-12-23 19:30:00', 44, 1),
(2, '2023-12-25 19:30:00', 46, 1);

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `administrator`
--
ALTER TABLE `administrator`
  ADD PRIMARY KEY (`administrator_email`);

--
-- 資料表索引 `daily_diet`
--
ALTER TABLE `daily_diet`
  ADD PRIMARY KEY (`daily_diet_id`),
  ADD KEY `daily_diet_ibfk_1` (`member_id`),
  ADD KEY `set_label_id` (`set_label_id`);

--
-- 資料表索引 `food`
--
ALTER TABLE `food`
  ADD PRIMARY KEY (`food_id`),
  ADD KEY `six_nutrients_id` (`six_categories_id`);

--
-- 資料表索引 `food_linking_daily_diet`
--
ALTER TABLE `food_linking_daily_diet`
  ADD KEY `food_id` (`food_id`),
  ADD KEY `daily_diet _id` (`daily_diet_id`);

--
-- 資料表索引 `food_linking_menu`
--
ALTER TABLE `food_linking_menu`
  ADD KEY `food_id` (`food_id`),
  ADD KEY `menu_id` (`menu_id`),
  ADD KEY `set_label_id` (`set_label_id`);

--
-- 資料表索引 `image`
--
ALTER TABLE `image`
  ADD PRIMARY KEY (`image_id`);

--
-- 資料表索引 `member`
--
ALTER TABLE `member`
  ADD PRIMARY KEY (`member_id`);

--
-- 資料表索引 `menu`
--
ALTER TABLE `menu`
  ADD PRIMARY KEY (`menu_id`),
  ADD KEY `six_nutrients_id` (`six_categories_id`);

--
-- 資料表索引 `personal_inbody`
--
ALTER TABLE `personal_inbody`
  ADD KEY `six_nutrients_id` (`six_categories_id`),
  ADD KEY `personal_inbody_ibfk_1` (`member_id`);

--
-- 資料表索引 `set_label`
--
ALTER TABLE `set_label`
  ADD PRIMARY KEY (`set_label_id`);

--
-- 資料表索引 `six_categories`
--
ALTER TABLE `six_categories`
  ADD PRIMARY KEY (`six_categories_id`);

--
-- 資料表索引 `weight_change`
--
ALTER TABLE `weight_change`
  ADD PRIMARY KEY (`weight_change_id`),
  ADD KEY `member_id` (`member_id`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `daily_diet`
--
ALTER TABLE `daily_diet`
  MODIFY `daily_diet_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `food`
--
ALTER TABLE `food`
  MODIFY `food_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=136;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `image`
--
ALTER TABLE `image`
  MODIFY `image_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `member`
--
ALTER TABLE `member`
  MODIFY `member_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `menu`
--
ALTER TABLE `menu`
  MODIFY `menu_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `set_label`
--
ALTER TABLE `set_label`
  MODIFY `set_label_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `six_categories`
--
ALTER TABLE `six_categories`
  MODIFY `six_categories_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=114;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `weight_change`
--
ALTER TABLE `weight_change`
  MODIFY `weight_change_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- 已傾印資料表的限制式
--

--
-- 資料表的限制式 `daily_diet`
--
ALTER TABLE `daily_diet`
  ADD CONSTRAINT `daily_diet_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `daily_diet_ibfk_2` FOREIGN KEY (`set_label_id`) REFERENCES `set_label` (`set_label_id`);

--
-- 資料表的限制式 `food`
--
ALTER TABLE `food`
  ADD CONSTRAINT `food_ibfk_1` FOREIGN KEY (`six_categories_id`) REFERENCES `six_categories` (`six_categories_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 資料表的限制式 `food_linking_daily_diet`
--
ALTER TABLE `food_linking_daily_diet`
  ADD CONSTRAINT `food_linking_daily_diet_ibfk_1` FOREIGN KEY (`food_id`) REFERENCES `food` (`food_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `food_linking_daily_diet_ibfk_2` FOREIGN KEY (`daily_diet_id`) REFERENCES `daily_diet` (`daily_diet_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 資料表的限制式 `food_linking_menu`
--
ALTER TABLE `food_linking_menu`
  ADD CONSTRAINT `food_linking_menu_ibfk_1` FOREIGN KEY (`food_id`) REFERENCES `food` (`food_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `food_linking_menu_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`menu_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `food_linking_menu_ibfk_3` FOREIGN KEY (`set_label_id`) REFERENCES `set_label` (`set_label_id`);

--
-- 資料表的限制式 `menu`
--
ALTER TABLE `menu`
  ADD CONSTRAINT `menu_ibfk_1` FOREIGN KEY (`six_categories_id`) REFERENCES `six_categories` (`six_categories_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 資料表的限制式 `personal_inbody`
--
ALTER TABLE `personal_inbody`
  ADD CONSTRAINT `personal_inbody_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `personal_inbody_ibfk_2` FOREIGN KEY (`six_categories_id`) REFERENCES `six_categories` (`six_categories_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 資料表的限制式 `weight_change`
--
ALTER TABLE `weight_change`
  ADD CONSTRAINT `weight_change_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
