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


-- -----------------------------------------------------
-- Table `mydb`.`libro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`libro` (
  `idlibro` INT NOT NULL,
  `Nombre` VARCHAR(45) NULL,
  `Autor` VARCHAR(45) NULL,
  `Editorial` VARCHAR(45) NULL,
  `Precio` VARCHAR(45) NULL,
  `Portada` LONGBLOB NULL,
  PRIMARY KEY (`idlibro`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Pedido`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Pedido` (
  `idPedido` INT NOT NULL,
  `Fecha` VARCHAR(45) NULL,
  `hora_inicio` VARCHAR(45) NULL,
  `hora_fin` VARCHAR(45) NULL,
  PRIMARY KEY (`idPedido`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Sesion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Sesion` (
  `idSesion` INT NOT NULL,
  `libro_idlibro` INT NOT NULL,
  `Pedido_idPedido` INT NOT NULL,
  PRIMARY KEY (`idSesion`),
  INDEX `fk_Sesion_libro1_idx` (`libro_idlibro` ASC) VISIBLE,
  INDEX `fk_Sesion_Pedido1_idx` (`Pedido_idPedido` ASC) VISIBLE,
  CONSTRAINT `fk_Sesion_libro1`
    FOREIGN KEY (`libro_idlibro`)
    REFERENCES `mydb`.`libro` (`idlibro`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Sesion_Pedido1`
    FOREIGN KEY (`Pedido_idPedido`)
    REFERENCES `mydb`.`Pedido` (`idPedido`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Usuario` (
  `idUsuario` INT NOT NULL,
  `IP` VARCHAR(45) NULL,
  `Nombre` VARCHAR(45) NULL,
  PRIMARY KEY (`idUsuario`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`UsuarioSesion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`UsuarioSesion` (
  `IDUS` VARCHAR(45) NOT NULL,
  `Sesion_idSesion` INT NOT NULL,
  `Usuario_idUsuario` INT NOT NULL,
  `Tiempo_Usuario` VARCHAR(45) NULL,
  `Lugar_Jugador` VARCHAR(45) NULL,
  PRIMARY KEY (`Sesion_idSesion`, `Usuario_idUsuario`, `IDUS`),
  INDEX `fk_Sesion_has_Usuario_Usuario1_idx` (`Usuario_idUsuario` ASC) VISIBLE,
  INDEX `fk_Sesion_has_Usuario_Sesion1_idx` (`Sesion_idSesion` ASC) VISIBLE,
  CONSTRAINT `fk_Sesion_has_Usuario_Sesion1`
    FOREIGN KEY (`Sesion_idSesion`)
    REFERENCES `mydb`.`Sesion` (`idSesion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Sesion_has_Usuario_Usuario1`
    FOREIGN KEY (`Usuario_idUsuario`)
    REFERENCES `mydb`.`Usuario` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
