SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`sede`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`sede` (
  `id_sede` INT NOT NULL,
  `nombre` VARCHAR(45) NULL,
  `estado` VARCHAR(45) NULL,
  PRIMARY KEY (`id_sede`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`usuario` (
  `id_usuario` INT NOT NULL,
  `nombre_usuario` VARCHAR(45) NULL,
  `estado` VARCHAR(45) NULL,
  `fecha_creacion` VARCHAR(45) NULL,
  `contrasena` VARCHAR(45) NULL,
  `tipo_usuario_id_tipo_usuario` INT NOT NULL,
  `sede_id_sede` INT NOT NULL,
  `foto` BLOB NULL,
  `nombres` VARCHAR(45) NULL,
  `apellidos` VARCHAR(45) NULL,
  `dni` VARCHAR(8) NULL,
  `correo` VARCHAR(45) NULL,
  `distrito` VARCHAR(45) NULL,
  PRIMARY KEY (`id_usuario`),
  INDEX `fk_usuario_sede1_idx` (`sede_id_sede` ASC) VISIBLE,
  CONSTRAINT `fk_usuario_sede1`
    FOREIGN KEY (`sede_id_sede`)
    REFERENCES `mydb`.`sede` (`id_sede`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`medicamentos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`medicamentos` (
  `id_medicamentos` INT NOT NULL,
  `descripcion` VARCHAR(250) NULL,
  `nombre` VARCHAR(45) NULL,
  `foto` BLOB NULL,
  `inventario` VARCHAR(45) NULL,
  `precio_unidad` VARCHAR(45) NULL,
  `fecha_ingreso` DATETIME NULL,
  PRIMARY KEY (`id_medicamentos`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`proveedor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`proveedor` (
  `id_proveedor` INT NOT NULL,
  `nombre` VARCHAR(45) NULL,
  `descripcion` VARCHAR(250) NULL,
  `proveedorcol` VARCHAR(45) NULL,
  PRIMARY KEY (`id_proveedor`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`envios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`envios` (
  `id_envios` INT NOT NULL,
  `descripcion` VARCHAR(250) NULL,
  `fecha_registro` DATETIME NULL,
  `fecha_entrega` DATETIME NULL,
  `estado` VARCHAR(45) NULL,
  `proveedor_id_proveedor` INT NOT NULL,
  PRIMARY KEY (`id_envios`),
  INDEX `fk_envios_proveedor1_idx` (`proveedor_id_proveedor` ASC) VISIBLE,
  CONSTRAINT `fk_envios_proveedor1`
    FOREIGN KEY (`proveedor_id_proveedor`)
    REFERENCES `mydb`.`proveedor` (`id_proveedor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`medicamentos_has_envios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`medicamentos_has_envios` (
  `medicamentos_id_medicamentos` INT NOT NULL,
  `pedidos_id_pedidos` INT NOT NULL,
  PRIMARY KEY (`medicamentos_id_medicamentos`, `pedidos_id_pedidos`),
  INDEX `fk_medicamentos_has_pedidos_pedidos1_idx` (`pedidos_id_pedidos` ASC) VISIBLE,
  INDEX `fk_medicamentos_has_pedidos_medicamentos1_idx` (`medicamentos_id_medicamentos` ASC) VISIBLE,
  CONSTRAINT `fk_medicamentos_has_pedidos_medicamentos1`
    FOREIGN KEY (`medicamentos_id_medicamentos`)
    REFERENCES `mydb`.`medicamentos` (`id_medicamentos`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_medicamentos_has_pedidos_pedidos1`
    FOREIGN KEY (`pedidos_id_pedidos`)
    REFERENCES `mydb`.`envios` (`id_envios`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`medico`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`medico` (
  `id_medico` INT NOT NULL,
  `nombre` VARCHAR(45) NULL,
  `apellido` VARCHAR(45) NULL,
  `sede_id_sede` INT NOT NULL,
  `colegiatura_medico` VARCHAR(45) NULL,
  PRIMARY KEY (`id_medico`),
  INDEX `fk_medico_sede1_idx` (`sede_id_sede` ASC) VISIBLE,
  CONSTRAINT `fk_medico_sede1`
    FOREIGN KEY (`sede_id_sede`)
    REFERENCES `mydb`.`sede` (`id_sede`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`ventas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`ventas` (
  `id_ventas` INT NOT NULL,
  `fecha_solicitud` DATETIME NULL,
  `usuario_id_usuario` INT NOT NULL,
  `costo_total` VARCHAR(45) NULL,
  `estado` VARCHAR(45) NULL,
  `tipo` VARCHAR(45) NULL,
  `receta` BLOB NULL,
  `fecha_entrega` DATETIME NULL,
  `comentario` VARCHAR(45) NULL,
  `indicador_receta` VARCHAR(45) NULL,
  `metodo_pago` VARCHAR(45) NULL,
  `fecha_aprobacion` DATETIME NULL,
  `tracking` VARCHAR(45) NULL,
  `medico_id_medico` INT NOT NULL,
  PRIMARY KEY (`id_ventas`, `usuario_id_usuario`, `medico_id_medico`),
  INDEX `fk_ventas_usuario1_idx` (`usuario_id_usuario` ASC) VISIBLE,
  INDEX `fk_ventas_medico1_idx` (`medico_id_medico` ASC) VISIBLE,
  CONSTRAINT `fk_ventas_usuario1`
    FOREIGN KEY (`usuario_id_usuario`)
    REFERENCES `mydb`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ventas_medico1`
    FOREIGN KEY (`medico_id_medico`)
    REFERENCES `mydb`.`medico` (`id_medico`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`ventas_has_medicamentos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`ventas_has_medicamentos` (
  `ventas_id_ventas` INT NOT NULL,
  `medicamentos_id_medicamentos` INT NOT NULL,
  `cantidad` VARCHAR(45) NULL,
  `costo_por_medicamento` VARCHAR(45) NULL,
  PRIMARY KEY (`ventas_id_ventas`, `medicamentos_id_medicamentos`),
  INDEX `fk_ventas_has_medicamentos_medicamentos1_idx` (`medicamentos_id_medicamentos` ASC) VISIBLE,
  INDEX `fk_ventas_has_medicamentos_ventas1_idx` (`ventas_id_ventas` ASC) VISIBLE,
  CONSTRAINT `fk_ventas_has_medicamentos_ventas1`
    FOREIGN KEY (`ventas_id_ventas`)
    REFERENCES `mydb`.`ventas` (`id_ventas`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ventas_has_medicamentos_medicamentos1`
    FOREIGN KEY (`medicamentos_id_medicamentos`)
    REFERENCES `mydb`.`medicamentos` (`id_medicamentos`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`solicitudes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`solicitudes` (
  `id_solicitudes` INT NOT NULL,
  `fecha_solicitud` DATETIME NULL,
  `estado` VARCHAR(45) NULL,
  `descripcion` VARCHAR(250) NULL,
  `usuario_id_usuario` INT NOT NULL,
  PRIMARY KEY (`id_solicitudes`, `usuario_id_usuario`),
  INDEX `fk_solicitudes_usuario1_idx` (`usuario_id_usuario` ASC) VISIBLE,
  CONSTRAINT `fk_solicitudes_usuario1`
    FOREIGN KEY (`usuario_id_usuario`)
    REFERENCES `mydb`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`paciente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`paciente` (
  `id_paciente` INT NOT NULL,
  `seguro` VARCHAR(45) NULL,
  `edad` VARCHAR(45) NULL,
  `usuario_id_usuario` INT NOT NULL,
  `telefono` VARCHAR(45) NULL,
  PRIMARY KEY (`id_paciente`, `usuario_id_usuario`),
  INDEX `fk_paciente_usuario1_idx` (`usuario_id_usuario` ASC) VISIBLE,
  CONSTRAINT `fk_paciente_usuario1`
    FOREIGN KEY (`usuario_id_usuario`)
    REFERENCES `mydb`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`tarjeta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`tarjeta` (
  `id_tarjeta` INT NOT NULL,
  `numero` VARCHAR(45) NULL,
  `fecha_caduca` VARCHAR(45) NULL,
  `cci` VARCHAR(45) NULL,
  PRIMARY KEY (`id_tarjeta`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`estado_de_envio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`estado_de_envio` (
  `id_estado` INT NOT NULL,
  `estado` VARCHAR(45) NULL,
  `envios_id_envios` INT NOT NULL,
  PRIMARY KEY (`id_estado`, `envios_id_envios`),
  INDEX `fk_estado_de_envio_envios1_idx` (`envios_id_envios` ASC) VISIBLE,
  CONSTRAINT `fk_estado_de_envio_envios1`
    FOREIGN KEY (`envios_id_envios`)
    REFERENCES `mydb`.`envios` (`id_envios`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`roles` (
  `id_roles` INT NOT NULL,
  `paciente` INT NOT NULL,
  `admin_sede` INT NOT NULL,
  `super_admin` INT NOT NULL,
  `farmacista` INT NOT NULL,
  PRIMARY KEY (`id_roles`, `paciente`, `admin_sede`, `super_admin`, `farmacista`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`roles_has_usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`roles_has_usuario` (
  `roles_id_roles` INT NOT NULL,
  `roles_paciente` INT NOT NULL,
  `roles_admin` INT NOT NULL,
  `roles_super_admin` INT NOT NULL,
  `roles_farmacista` INT NOT NULL,
  `usuario_id_usuario` INT NOT NULL,
  PRIMARY KEY (`roles_id_roles`, `roles_paciente`, `roles_admin`, `roles_super_admin`, `roles_farmacista`, `usuario_id_usuario`),
  INDEX `fk_roles_has_usuario_usuario1_idx` (`usuario_id_usuario` ASC) VISIBLE,
  INDEX `fk_roles_has_usuario_roles1_idx` (`roles_id_roles` ASC, `roles_paciente` ASC, `roles_admin` ASC, `roles_super_admin` ASC, `roles_farmacista` ASC) VISIBLE,
  CONSTRAINT `fk_roles_has_usuario_roles1`
    FOREIGN KEY (`roles_id_roles`, `roles_paciente`, `roles_admin`, `roles_super_admin`, `roles_farmacista`)
    REFERENCES `mydb`.`roles` (`id_roles`, `paciente`, `admin_sede`, `super_admin`, `farmacista`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_roles_has_usuario_usuario1`
    FOREIGN KEY (`usuario_id_usuario`)
    REFERENCES `mydb`.`usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
