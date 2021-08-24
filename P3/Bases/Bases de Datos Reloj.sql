-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`HoraCentral`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`HoraCentral` (
  `ID` INT NOT NULL,
  `hPrev` VARCHAR(45) NOT NULL,
  `hRef` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Equipos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Equipos` (
  `ID` INT NOT NULL,
  `IP` VARCHAR(45) NULL,
  `Nombre` VARCHAR(45) NULL,
  `Latencia` VARCHAR(45) NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`HoraEquipos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`HoraEquipos` (
  `ID` INT NOT NULL,
  `IDhSincr` INT NOT NULL,
  `IDEquipo` INT NOT NULL,
  `hEquipo` VARCHAR(45) NULL,
  `aEquipo` VARCHAR(45) NULL,
  `ralentizar` VARCHAR(45) NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_HoraEquipos_HoraCentral_idx` (`IDhSincr` ASC) VISIBLE,
  INDEX `fk_HoraEquipos_Equipos1_idx` (`IDEquipo` ASC) VISIBLE,
  CONSTRAINT `fk_HoraEquipos_HoraCentral`
    FOREIGN KEY (`IDhSincr`)
    REFERENCES `mydb`.`HoraCentral` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_HoraEquipos_Equipos1`
    FOREIGN KEY (`IDEquipo`)
    REFERENCES `mydb`.`Equipos` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
