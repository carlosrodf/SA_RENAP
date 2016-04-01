SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`estado`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`estado` ;

CREATE TABLE IF NOT EXISTS `mydb`.`estado` (
  `idestado` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idestado`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`tipo_certificado`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`tipo_certificado` ;

CREATE TABLE IF NOT EXISTS `mydb`.`tipo_certificado` (
  `idtipo_certificado` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idtipo_certificado`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`certificado`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`certificado` ;

CREATE TABLE IF NOT EXISTS `mydb`.`certificado` (
  `idcertificado` INT NOT NULL AUTO_INCREMENT,
  `correlativo` VARCHAR(32) NOT NULL,
  `verificador` VARCHAR(32) NOT NULL,
  `tipo` INT NULL,
  `valido` TINYINT(1) NOT NULL,
  `contenido` VARCHAR(10000) NULL,
  PRIMARY KEY (`idcertificado`, `correlativo`, `verificador`),
  INDEX `fk_certificado_1_idx` (`tipo` ASC),
  CONSTRAINT `fk_certificado_1`
    FOREIGN KEY (`tipo`)
    REFERENCES `mydb`.`tipo_certificado` (`idtipo_certificado`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`ciudadano`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`ciudadano` ;

CREATE TABLE IF NOT EXISTS `mydb`.`ciudadano` (
  `idciudadano` INT NOT NULL AUTO_INCREMENT,
  `predpi` INT NOT NULL,
  `DPI` VARCHAR(20) NULL,
  `primer_nombre` VARCHAR(45) NULL,
  `segundo_nombre` VARCHAR(45) NULL,
  `primer_apellido` VARCHAR(45) NULL,
  `segundo_apellido` VARCHAR(45) NULL,
  `fecha_nac` DATE NULL,
  `genero` VARCHAR(1) NULL,
  `pais` VARCHAR(45) NULL,
  `departamento` VARCHAR(45) NULL,
  `municipio` VARCHAR(45) NULL,
  `estado` INT NULL,
  `padre` INT NULL,
  `madre` INT NULL,
  PRIMARY KEY (`idciudadano`),
  UNIQUE INDEX `DPI_UNIQUE` (`DPI` ASC),
  INDEX `fk_ciudadano_1_idx` (`estado` ASC),
  UNIQUE INDEX `pre-dpi_UNIQUE` (`predpi` ASC),
  INDEX `fk_ciudadano_2_idx` (`padre` ASC),
  INDEX `fk_ciudadano_3_idx` (`madre` ASC),
  CONSTRAINT `fk_ciudadano_1`
    FOREIGN KEY (`estado`)
    REFERENCES `mydb`.`estado` (`idestado`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ciudadano_4`
    FOREIGN KEY (`predpi`)
    REFERENCES `mydb`.`certificado` (`idcertificado`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ciudadano_2`
    FOREIGN KEY (`padre`)
    REFERENCES `mydb`.`ciudadano` (`idciudadano`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ciudadano_3`
    FOREIGN KEY (`madre`)
    REFERENCES `mydb`.`ciudadano` (`idciudadano`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`matrimonio`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`matrimonio` ;

CREATE TABLE IF NOT EXISTS `mydb`.`matrimonio` (
  `esposo` INT NOT NULL,
  `esposa` INT NOT NULL,
  `certificado` INT NOT NULL,
  PRIMARY KEY (`esposo`, `esposa`),
  INDEX `fk_matrimonio_2_idx` (`esposa` ASC),
  INDEX `fk_matrimonio_3_idx` (`certificado` ASC),
  CONSTRAINT `fk_matrimonio_1`
    FOREIGN KEY (`esposo`)
    REFERENCES `mydb`.`ciudadano` (`idciudadano`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_matrimonio_2`
    FOREIGN KEY (`esposa`)
    REFERENCES `mydb`.`ciudadano` (`idciudadano`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_matrimonio_3`
    FOREIGN KEY (`certificado`)
    REFERENCES `mydb`.`certificado` (`idcertificado`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`DPI1`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`DPI1` ;

CREATE TABLE IF NOT EXISTS `mydb`.`DPI1` (
  `inc` INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`inc`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`DPI2`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`DPI2` ;

CREATE TABLE IF NOT EXISTS `mydb`.`DPI2` (
  `inc` INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`inc`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`DPI3`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`DPI3` ;

CREATE TABLE IF NOT EXISTS `mydb`.`DPI3` (
  `inc` INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`inc`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`verificadores`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`verificadores` ;

CREATE TABLE IF NOT EXISTS `mydb`.`verificadores` (
  `inc` INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`inc`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`correlativos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`correlativos` ;

CREATE TABLE IF NOT EXISTS `mydb`.`correlativos` (
  `inc` INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`inc`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `mydb`.`estado`
-- -----------------------------------------------------
START TRANSACTION;
USE `mydb`;
INSERT INTO `mydb`.`estado` (`idestado`, `nombre`) VALUES (1, 'DEFAULT');
INSERT INTO `mydb`.`estado` (`idestado`, `nombre`) VALUES (2, 'EN_QUIEBRA');
INSERT INTO `mydb`.`estado` (`idestado`, `nombre`) VALUES (3, 'MUERTO');

COMMIT;


-- -----------------------------------------------------
-- Data for table `mydb`.`tipo_certificado`
-- -----------------------------------------------------
START TRANSACTION;
USE `mydb`;
INSERT INTO `mydb`.`tipo_certificado` (`idtipo_certificado`, `nombre`) VALUES (NULL, 'NACIMIENTO');
INSERT INTO `mydb`.`tipo_certificado` (`idtipo_certificado`, `nombre`) VALUES (NULL, 'MATRIMONIO');
INSERT INTO `mydb`.`tipo_certificado` (`idtipo_certificado`, `nombre`) VALUES (NULL, 'DEFUNCION');

COMMIT;


-- -----------------------------------------------------
-- Data for table `mydb`.`DPI1`
-- -----------------------------------------------------
START TRANSACTION;
USE `mydb`;
INSERT INTO `mydb`.`DPI1` (`inc`) VALUES (null);
INSERT INTO `mydb`.`DPI1` (`inc`) VALUES (null);
INSERT INTO `mydb`.`DPI1` (`inc`) VALUES (null);
INSERT INTO `mydb`.`DPI1` (`inc`) VALUES (null);
INSERT INTO `mydb`.`DPI1` (`inc`) VALUES (null);
INSERT INTO `mydb`.`DPI1` (`inc`) VALUES (null);
INSERT INTO `mydb`.`DPI1` (`inc`) VALUES (null);

COMMIT;


-- -----------------------------------------------------
-- Data for table `mydb`.`DPI2`
-- -----------------------------------------------------
START TRANSACTION;
USE `mydb`;
INSERT INTO `mydb`.`DPI2` (`inc`) VALUES (null);
INSERT INTO `mydb`.`DPI2` (`inc`) VALUES (null);
INSERT INTO `mydb`.`DPI2` (`inc`) VALUES (null);
INSERT INTO `mydb`.`DPI2` (`inc`) VALUES (null);

COMMIT;


-- -----------------------------------------------------
-- Data for table `mydb`.`DPI3`
-- -----------------------------------------------------
START TRANSACTION;
USE `mydb`;
INSERT INTO `mydb`.`DPI3` (`inc`) VALUES (null);

COMMIT;


-- -----------------------------------------------------
-- Data for table `mydb`.`verificadores`
-- -----------------------------------------------------
START TRANSACTION;
USE `mydb`;
INSERT INTO `mydb`.`verificadores` (`inc`) VALUES (null);
INSERT INTO `mydb`.`verificadores` (`inc`) VALUES (null);
INSERT INTO `mydb`.`verificadores` (`inc`) VALUES (null);
INSERT INTO `mydb`.`verificadores` (`inc`) VALUES (null);

COMMIT;
