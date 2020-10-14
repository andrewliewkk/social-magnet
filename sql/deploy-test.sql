-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Apr 08, 2020 at 06:38 AM
-- Server version: 5.7.23
-- PHP Version: 7.2.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `magnet`
--
DROP DATABASE IF EXISTS `magnet`;
CREATE DATABASE IF NOT EXISTS `magnet` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `magnet`;

-- --------------------------------------------------------

--
-- Table structure for table `crop`
--

DROP TABLE IF EXISTS `crop`;
CREATE TABLE IF NOT EXISTS `crop` (
  `Name` varchar(50) NOT NULL,
  `Cost` int(11) NOT NULL,
  `Time` int(11) NOT NULL,
  `Xp` int(11) NOT NULL,
  `MinYield` int(11) NOT NULL,
  `MaxYield` int(11) NOT NULL,
  `SalePrice` int(11) NOT NULL,
  PRIMARY KEY (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `crop`
--

INSERT INTO `crop` (`Name`, `Cost`, `Time`, `Xp`, `MinYield`, `MaxYield`, `SalePrice`) VALUES
('Papaya', 20, 30, 8, 75, 100, 15),
('Pumpkin', 30, 60, 5, 5, 200, 20),
('Sunflower', 40, 120, 20, 15, 20, 40),
('Watermelon', 50, 240, 1, 5, 800, 10);

-- --------------------------------------------------------

--
-- Table structure for table `friend`
--

DROP TABLE IF EXISTS `friend`;
CREATE TABLE IF NOT EXISTS `friend` (
  `UserID` varchar(120) NOT NULL,
  `FriendID` varchar(120) NOT NULL,
  PRIMARY KEY (`UserID`,`FriendID`),
  KEY `friend_fk2` (`FriendID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `friend`
--

INSERT INTO `friend` (`UserID`, `FriendID`) VALUES
('david', 'apple'),
('kenny', 'bob'),
('apple', 'david'),
('bob', 'kenny'),
('apple', 'james'),
('david', 'james');

-- --------------------------------------------------------

--
-- Table structure for table `friend_request`
--

DROP TABLE IF EXISTS `friend_request`;
CREATE TABLE IF NOT EXISTS `friend_request` (
  `SenderID` varchar(120) NOT NULL,
  `ReceiverID` varchar(120) NOT NULL,
  PRIMARY KEY (`SenderID`,`ReceiverID`),
  KEY `friend_request_fk2` (`ReceiverID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `friend_request`
--

INSERT INTO `friend_request` (`SenderID`, `ReceiverID`) VALUES
('bob', 'apple'),
('kenny', 'apple'),
('david', 'kenny');

-- --------------------------------------------------------

--
-- Table structure for table `gift`
--

DROP TABLE IF EXISTS `gift`;
CREATE TABLE IF NOT EXISTS `gift` (
  `GiftID` int(11) NOT NULL AUTO_INCREMENT,
  `SenderID` varchar(120) NOT NULL,
  `ReceiverID` varchar(120) NOT NULL,
  `DateSent` varchar(50) NOT NULL,
  `CropName` varchar(50) NOT NULL,
  `isAccepted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`GiftID`),
  KEY `gift_fk1` (`SenderID`),
  KEY `gift_fk2` (`ReceiverID`),
  KEY `gift_fk3` (`CropName`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `gift`
--

INSERT INTO `gift` (`GiftID`, `SenderID`, `ReceiverID`, `DateSent`, `CropName`, `isAccepted`) VALUES
(1, 'bob', 'kenny', '08/04/2020 14:32:43', 'Sunflower', 0),
(2, 'david', 'apple', '08/04/2020 14:34:50', 'Watermelon', 0);

-- --------------------------------------------------------

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
CREATE TABLE IF NOT EXISTS `inventory` (
  `UserID` varchar(120) NOT NULL,
  `CropName` varchar(50) NOT NULL,
  `Quantity` int(11) NOT NULL,
  PRIMARY KEY (`UserID`,`CropName`),
  KEY `inventory_fk1` (`CropName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `inventory`
--

INSERT INTO `inventory` (`UserID`, `CropName`, `Quantity`) VALUES
('apple', 'Papaya', 5),
('apple', 'Pumpkin', 5),
('apple', 'Sunflower', 4),
('apple', 'Watermelon', 3),
('bob', 'Papaya', 1);

-- --------------------------------------------------------

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
CREATE TABLE IF NOT EXISTS `member` (
  `UserID` varchar(120) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `Fullname` varchar(255) NOT NULL,
  `Xp` int(11) NOT NULL DEFAULT '0',
  `Gold` int(11) NOT NULL DEFAULT '50',
  PRIMARY KEY (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `member`
--

INSERT INTO `member` (`UserID`, `Password`, `Fullname`, `Xp`, `Gold`) VALUES
('apple', 'apple123', 'Apple TAN', 3000, 3000),
('bob', 'bob123', 'Bob TAN', 0, 30),
('david', 'david123', 'David KOH', 0, 50),
('kenny', 'kenny123', 'Kenny LEE', 0, 50),
('james', 'james123', 'James CHIA', 0, 50);

-- --------------------------------------------------------

--
-- Table structure for table `plot`
--

DROP TABLE IF EXISTS `plot`;
CREATE TABLE IF NOT EXISTS `plot` (
  `PlotID` int(11) NOT NULL,
  `UserID` varchar(120) NOT NULL,
  PRIMARY KEY (`PlotID`,`UserID`),
  KEY `plot_fk` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `plot`
--

INSERT INTO `plot` (`PlotID`, `UserID`) VALUES
(1, 'apple'),
(2, 'apple'),
(3, 'apple'),
(4, 'apple'),
(5, 'apple'),
(6, 'apple'),
(7, 'apple'),
(1, 'bob'),
(2, 'bob'),
(3, 'bob'),
(4, 'bob'),
(5, 'bob'),
(1, 'david'),
(2, 'david'),
(3, 'david'),
(4, 'david'),
(5, 'david'),
(1, 'kenny'),
(2, 'kenny'),
(3, 'kenny'),
(4, 'kenny'),
(5, 'kenny');

-- --------------------------------------------------------

--
-- Table structure for table `plot_crop`
--

DROP TABLE IF EXISTS `plot_crop`;
CREATE TABLE IF NOT EXISTS `plot_crop` (
  `UserID` varchar(120) NOT NULL,
  `PlotID` int(11) NOT NULL,
  `CropName` varchar(50) NOT NULL,
  `TimePlanted` varchar(50) NOT NULL,
  `Yield` int(11) NOT NULL,
  PRIMARY KEY (`UserID`,`PlotID`),
  KEY `plot_crop_fk2` (`CropName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `plot_crop`
--

INSERT INTO `plot_crop` (`UserID`, `PlotID`, `CropName`, `TimePlanted`, `Yield`) VALUES
('apple', 1, 'Watermelon', '08/04/2020 14:30:51', 71),
('apple', 2, 'Sunflower', '08/04/2020 12:30:55', 15),
('apple', 5, 'Watermelon', '08/04/2020 14:31:00', 795);

-- --------------------------------------------------------

--
-- Table structure for table `plot_crop_steal`
--

DROP TABLE IF EXISTS `plot_crop_steal`;
CREATE TABLE IF NOT EXISTS `plot_crop_steal` (
  `StealerID` varchar(120) NOT NULL,
  `UserID` varchar(120) NOT NULL,
  `PlotID` int(11) NOT NULL,
  `AmountStolen` int(10) NOT NULL,
  `CumPercStolen` int(10) NOT NULL,
  PRIMARY KEY (`StealerID`,`UserID`,`PlotID`),
  KEY `plot_crop_steal_fk1` (`UserID`,`PlotID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `plot_crop_steal`
--

INSERT INTO `plot_crop_steal` (`StealerID`, `UserID`, `PlotID`, `AmountStolen`, `CumPercStolen`) VALUES
('david', 'apple', 2, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
CREATE TABLE IF NOT EXISTS `post` (
  `PostID` int(11) NOT NULL AUTO_INCREMENT,
  `PosterID` varchar(120) NOT NULL,
  `Content` varchar(5000) NOT NULL,
  `Wall_UserID` varchar(120) NOT NULL,
  `GiftID` int(11) DEFAULT NULL,
  PRIMARY KEY (`PostID`),
  KEY `post_fk1` (`PosterID`),
  KEY `post_fk2` (`Wall_UserID`),
  KEY `posk_fk3` (`GiftID`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `post`
--

INSERT INTO `post` (`PostID`, `PosterID`, `Content`, `Wall_UserID`, `GiftID`) VALUES
(9, 'kenny', 'Hello World! @apple ', 'kenny', NULL),
(10, 'bob', ': Here is a bag Sunflower seeds for you. - City Farmers', 'kenny', 1),
(11, 'david', ': Here is a bag Watermelon seeds for you. - City Farmers', 'apple', 2),
(12, 'apple', 'Hi @david it is a nice day today!', 'apple', NULL),
(13, 'apple', 'feeling tired', 'apple', NULL),
(14, 'apple', 'Welcome to my wall!', 'apple', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `post_like`
--

DROP TABLE IF EXISTS `post_like`;
CREATE TABLE IF NOT EXISTS `post_like` (
  `PostID` int(11) NOT NULL,
  `UserID` varchar(120) NOT NULL,
  `isLike` tinyint(1) NOT NULL,
  PRIMARY KEY (`PostID`,`UserID`),
  KEY `post_like_fk2` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `post_like`
--

INSERT INTO `post_like` (`PostID`, `UserID`, `isLike`) VALUES
(11, 'apple', 1);

-- --------------------------------------------------------

--
-- Table structure for table `rank`
--

DROP TABLE IF EXISTS `rank`;
CREATE TABLE IF NOT EXISTS `rank` (
  `Rank` varchar(20) NOT NULL,
  `Xp` int(11) NOT NULL,
  `Plots` int(11) NOT NULL,
  PRIMARY KEY (`Rank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rank`
--

INSERT INTO `rank` (`Rank`, `Xp`, `Plots`) VALUES
('Apprentice', 1000, 6),
('Grandmaster', 5000, 8),
('Journeyman', 2500, 7),
('Legendary', 12000, 9),
('Novice', 0, 5);

-- --------------------------------------------------------

--
-- Table structure for table `reply`
--

DROP TABLE IF EXISTS `reply`;
CREATE TABLE IF NOT EXISTS `reply` (
  `ReplyID` int(11) NOT NULL AUTO_INCREMENT,
  `PostID` int(11) NOT NULL,
  `UserID` varchar(120) NOT NULL,
  `Content` varchar(5000) NOT NULL,
  PRIMARY KEY (`ReplyID`,`PostID`),
  KEY `reply_fk1` (`PostID`),
  KEY `reply_fk2` (`UserID`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `reply`
--

INSERT INTO `reply` (`ReplyID`, `PostID`, `UserID`, `Content`) VALUES
(16, 12, 'david', 'Hello apple looks like we have to stay at home now'),
(17, 12, 'apple', 'Yeah this circuit breaker is killing me');

-- --------------------------------------------------------

--
-- Table structure for table `wallpost`
--

DROP TABLE IF EXISTS `wallpost`;
CREATE TABLE IF NOT EXISTS `wallpost` (
  `Wall_UserID` varchar(120) NOT NULL,
  `PostID` int(120) NOT NULL,
  PRIMARY KEY (`Wall_UserID`,`PostID`),
  KEY `wallpost_fk1` (`PostID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `wallpost`
--

INSERT INTO `wallpost` (`Wall_UserID`, `PostID`) VALUES
('kenny', 9),
('kenny', 10),
('apple', 9),
('apple', 11),
('apple', 12),
('apple', 13),
('apple', 14),
('david', 12);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `friend`
--
ALTER TABLE `friend`
  ADD CONSTRAINT `friend_fk1` FOREIGN KEY (`UserID`) REFERENCES `member` (`UserID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `friend_fk2` FOREIGN KEY (`FriendID`) REFERENCES `member` (`UserID`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `friend_request`
--
ALTER TABLE `friend_request`
  ADD CONSTRAINT `friend_request_fk1` FOREIGN KEY (`SenderID`) REFERENCES `member` (`UserID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `friend_request_fk2` FOREIGN KEY (`ReceiverID`) REFERENCES `member` (`UserID`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `gift`
--
ALTER TABLE `gift`
  ADD CONSTRAINT `gift_fk1` FOREIGN KEY (`SenderID`) REFERENCES `member` (`UserID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `gift_fk2` FOREIGN KEY (`ReceiverID`) REFERENCES `member` (`UserID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `gift_fk3` FOREIGN KEY (`CropName`) REFERENCES `crop` (`Name`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `inventory`
--
ALTER TABLE `inventory`
  ADD CONSTRAINT `inventory_fk1` FOREIGN KEY (`CropName`) REFERENCES `crop` (`Name`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `inventory_fk2` FOREIGN KEY (`UserID`) REFERENCES `member` (`UserID`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `plot`
--
ALTER TABLE `plot`
  ADD CONSTRAINT `plot_fk` FOREIGN KEY (`UserID`) REFERENCES `member` (`UserID`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `plot_crop`
--
ALTER TABLE `plot_crop`
  ADD CONSTRAINT `plot_crop_fk1` FOREIGN KEY (`UserID`,`PlotID`) REFERENCES `plot` (`UserID`, `PlotID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `plot_crop_fk2` FOREIGN KEY (`CropName`) REFERENCES `crop` (`Name`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `plot_crop_steal`
--
ALTER TABLE `plot_crop_steal`
  ADD CONSTRAINT `plot_crop_steal_fk1` FOREIGN KEY (`UserID`,`PlotID`) REFERENCES `plot_crop` (`UserID`, `PlotID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `plot_crop_steal_fk2` FOREIGN KEY (`StealerID`) REFERENCES `member` (`UserID`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `post`
--
ALTER TABLE `post`
  ADD CONSTRAINT `posk_fk3` FOREIGN KEY (`GiftID`) REFERENCES `gift` (`GiftID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `post_fk1` FOREIGN KEY (`PosterID`) REFERENCES `member` (`UserID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `post_fk2` FOREIGN KEY (`Wall_UserID`) REFERENCES `member` (`UserID`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `post_like`
--
ALTER TABLE `post_like`
  ADD CONSTRAINT `post_like_fk1` FOREIGN KEY (`PostID`) REFERENCES `post` (`PostID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `post_like_fk2` FOREIGN KEY (`UserID`) REFERENCES `member` (`UserID`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `reply`
--
ALTER TABLE `reply`
  ADD CONSTRAINT `reply_fk1` FOREIGN KEY (`PostID`) REFERENCES `post` (`PostID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `reply_fk2` FOREIGN KEY (`UserID`) REFERENCES `member` (`UserID`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `wallpost`
--
ALTER TABLE `wallpost`
  ADD CONSTRAINT `wallpost_fk1` FOREIGN KEY (`PostID`) REFERENCES `post` (`PostID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `wallpost_fk2` FOREIGN KEY (`Wall_UserID`) REFERENCES `member` (`UserID`) ON DELETE CASCADE ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
