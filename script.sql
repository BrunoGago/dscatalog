-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema sakila
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema dscatalog
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema dscatalog
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `dscatalog` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `dscatalog` ;

-- -----------------------------------------------------
-- Table `dscatalog`.`tb_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dscatalog`.`tb_category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `category_name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `dscatalog`.`tb_product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dscatalog`.`tb_product` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `description` TEXT NULL DEFAULT NULL,
  `img_url` VARCHAR(255) NULL DEFAULT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `price` DOUBLE NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `dscatalog`.`tb_product_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dscatalog`.`tb_product_category` (
  `product_id` BIGINT NOT NULL,
  `category_id` BIGINT NOT NULL,
  PRIMARY KEY (`product_id`, `category_id`),
  INDEX `FK5r4sbavb4nkd9xpl0f095qs2a` (`category_id` ASC) VISIBLE,
  CONSTRAINT `FK5r4sbavb4nkd9xpl0f095qs2a`
    FOREIGN KEY (`category_id`)
    REFERENCES `dscatalog`.`tb_category` (`id`),
  CONSTRAINT `FKgbof0jclmaf8wn2alsoexxq3u`
    FOREIGN KEY (`product_id`)
    REFERENCES `dscatalog`.`tb_product` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
