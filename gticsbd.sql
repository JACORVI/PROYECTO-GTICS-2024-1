-- MySQL dump 10.13  Distrib 8.0.24, for Win64 (x86_64)
--
-- Host: localhost    Database: gticsbd
-- ------------------------------------------------------
-- Server version	8.0.24

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `carrito`
--

DROP TABLE IF EXISTS `carrito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carrito` (
  `medicamentos_id_medicamentos` int NOT NULL,
  `usuario_id_usuario` int NOT NULL,
  `cantidad` int DEFAULT '1',
  `numero_pedido` varchar(45) DEFAULT NULL,
  `estado_de_compra` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`medicamentos_id_medicamentos`,`usuario_id_usuario`),
  KEY `fk_usuario_has_medicamentos_medicamentos1_idx` (`medicamentos_id_medicamentos`),
  KEY `fk_carrito_usuario1_idx` (`usuario_id_usuario`),
  CONSTRAINT `fk_carrito_usuario1` FOREIGN KEY (`usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `fk_usuario_has_medicamentos_medicamentos1` FOREIGN KEY (`medicamentos_id_medicamentos`) REFERENCES `medicamentos` (`id_medicamentos`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carrito`
--

LOCK TABLES `carrito` WRITE;
/*!40000 ALTER TABLE `carrito` DISABLE KEYS */;
/*!40000 ALTER TABLE `carrito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicamentos`
--

DROP TABLE IF EXISTS `medicamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicamentos` (
  `id_medicamentos` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(400) DEFAULT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  `foto` longblob,
  `inventario` int DEFAULT NULL,
  `precio_unidad` double DEFAULT NULL,
  `fecha_ingreso` date DEFAULT NULL,
  `categoria` varchar(45) DEFAULT NULL,
  `dosis` varchar(45) DEFAULT NULL,
  `borrado_logico` int DEFAULT NULL,
  PRIMARY KEY (`id_medicamentos`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicamentos`
--

LOCK TABLES `medicamentos` WRITE;
/*!40000 ALTER TABLE `medicamentos` DISABLE KEYS */;
/*!40000 ALTER TABLE `medicamentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicamentos_del_pedido`
--

DROP TABLE IF EXISTS `medicamentos_del_pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicamentos_del_pedido` (
  `id_medicamentos_del_pedido` int NOT NULL AUTO_INCREMENT,
  `nombre_medicamento` varchar(45) DEFAULT NULL,
  `costo_medicamento` double DEFAULT NULL,
  `cantidad` int DEFAULT NULL,
  `pedidos_paciente_idpedidos_paciente` int NOT NULL,
  `pedidos_paciente_usuario_id_usuario` int NOT NULL,
  PRIMARY KEY (`id_medicamentos_del_pedido`,`pedidos_paciente_idpedidos_paciente`,`pedidos_paciente_usuario_id_usuario`),
  KEY `fk_medicamentos_del_pedido_pedidos_paciente1_idx` (`pedidos_paciente_idpedidos_paciente`,`pedidos_paciente_usuario_id_usuario`),
  KEY `FK6254fi91xksw2lfvh52jdsgg2` (`pedidos_paciente_usuario_id_usuario`),
  CONSTRAINT `FK6254fi91xksw2lfvh52jdsgg2` FOREIGN KEY (`pedidos_paciente_usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `fk_medicamentos_del_pedido_pedidos_paciente1` FOREIGN KEY (`pedidos_paciente_idpedidos_paciente`, `pedidos_paciente_usuario_id_usuario`) REFERENCES `pedidos_paciente` (`idpedidos_paciente`, `usuario_id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicamentos_del_pedido`
--

LOCK TABLES `medicamentos_del_pedido` WRITE;
/*!40000 ALTER TABLE `medicamentos_del_pedido` DISABLE KEYS */;
/*!40000 ALTER TABLE `medicamentos_del_pedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicamentos_recojo`
--

DROP TABLE IF EXISTS `medicamentos_recojo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicamentos_recojo` (
  `idmedicamentos_recojo` int NOT NULL AUTO_INCREMENT,
  `nombre_medicamento` varchar(45) DEFAULT NULL,
  `costo_medicamento` double DEFAULT NULL,
  `cantidad` int DEFAULT NULL,
  `pedidos_paciente_recojo_idpedidos_paciente_recojo` int NOT NULL,
  `pedidos_paciente_recojo_usuario_id_usuario` int NOT NULL,
  PRIMARY KEY (`idmedicamentos_recojo`,`pedidos_paciente_recojo_idpedidos_paciente_recojo`,`pedidos_paciente_recojo_usuario_id_usuario`),
  KEY `fk_medicamentos_recojo_pedidos_paciente_recojo1_idx` (`pedidos_paciente_recojo_idpedidos_paciente_recojo`,`pedidos_paciente_recojo_usuario_id_usuario`),
  KEY `FKhnvcm2y6it02wnfudgv7brqk8` (`pedidos_paciente_recojo_usuario_id_usuario`),
  CONSTRAINT `FK42s1trfymav7oqclaaccfasnk` FOREIGN KEY (`pedidos_paciente_recojo_idpedidos_paciente_recojo`) REFERENCES `pedidos_paciente` (`idpedidos_paciente`),
  CONSTRAINT `fk_medicamentos_recojo_pedidos_paciente_recojo1` FOREIGN KEY (`pedidos_paciente_recojo_idpedidos_paciente_recojo`, `pedidos_paciente_recojo_usuario_id_usuario`) REFERENCES `pedidos_paciente_recojo` (`idpedidos_paciente_recojo`, `usuario_id_usuario`),
  CONSTRAINT `FKhnvcm2y6it02wnfudgv7brqk8` FOREIGN KEY (`pedidos_paciente_recojo_usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicamentos_recojo`
--

LOCK TABLES `medicamentos_recojo` WRITE;
/*!40000 ALTER TABLE `medicamentos_recojo` DISABLE KEYS */;
/*!40000 ALTER TABLE `medicamentos_recojo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos_paciente`
--

DROP TABLE IF EXISTS `pedidos_paciente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos_paciente` (
  `idpedidos_paciente` int NOT NULL AUTO_INCREMENT,
  `nombre_paciente` varchar(45) DEFAULT NULL,
  `apellido_paciente` varchar(45) DEFAULT NULL,
  `medico_que_atiende` varchar(45) DEFAULT NULL,
  `seguro` varchar(45) DEFAULT NULL,
  `direccion` varchar(90) DEFAULT NULL,
  `distrito` varchar(45) DEFAULT NULL,
  `telefono` int DEFAULT NULL,
  `dni` int DEFAULT NULL,
  `hora_de_entrega` varchar(45) DEFAULT NULL,
  `receta` blob,
  `costo_total` double DEFAULT NULL,
  `tipo_de_pedido` varchar(45) DEFAULT NULL,
  `fecha_solicitud` varchar(45) DEFAULT NULL,
  `fecha_entrega` varchar(45) DEFAULT NULL,
  `validacion_del_pedido` varchar(45) DEFAULT NULL,
  `comentario` varchar(180) DEFAULT NULL,
  `fecha_validacion` varchar(45) DEFAULT NULL,
  `estado_del_pedido` varchar(45) DEFAULT NULL,
  `numero_tracking` varchar(45) DEFAULT NULL,
  `aviso_vencimiento` varchar(45) DEFAULT NULL,
  `metodo_pago` varchar(45) DEFAULT NULL,
  `usuario_id_usuario` int NOT NULL,
  PRIMARY KEY (`idpedidos_paciente`,`usuario_id_usuario`),
  KEY `fk_pedidos_paciente_usuario1_idx` (`usuario_id_usuario`),
  CONSTRAINT `fk_pedidos_paciente_usuario1` FOREIGN KEY (`usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos_paciente`
--

LOCK TABLES `pedidos_paciente` WRITE;
/*!40000 ALTER TABLE `pedidos_paciente` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedidos_paciente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos_paciente_recojo`
--

DROP TABLE IF EXISTS `pedidos_paciente_recojo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos_paciente_recojo` (
  `idpedidos_paciente_recojo` int NOT NULL AUTO_INCREMENT,
  `nombre_paciente` varchar(45) DEFAULT NULL,
  `apellido_paciente` varchar(45) DEFAULT NULL,
  `medico_que_atiende` varchar(45) DEFAULT NULL,
  `seguro` varchar(45) DEFAULT NULL,
  `telefono` int DEFAULT NULL,
  `dni` int DEFAULT NULL,
  `receta` varchar(45) DEFAULT NULL,
  `costo_total` double DEFAULT NULL,
  `tipo_de_pedido` varchar(45) DEFAULT NULL,
  `fecha_solicitud` varchar(45) DEFAULT NULL,
  `fecha_entrega` varchar(45) DEFAULT NULL,
  `validacion_del_pedido` varchar(45) DEFAULT NULL,
  `comentario` varchar(180) DEFAULT NULL,
  `fecha_validacion` varchar(45) DEFAULT NULL,
  `estado_del_pedido` varchar(45) DEFAULT NULL,
  `numero_tracking` varchar(45) DEFAULT NULL,
  `aviso_vencimiento` varchar(45) DEFAULT NULL,
  `sede_de_recojo` varchar(45) DEFAULT NULL,
  `usuario_id_usuario` int NOT NULL,
  PRIMARY KEY (`idpedidos_paciente_recojo`,`usuario_id_usuario`),
  KEY `fk_pedidos_paciente_recojo_usuario1_idx` (`usuario_id_usuario`),
  CONSTRAINT `fk_pedidos_paciente_recojo_usuario1` FOREIGN KEY (`usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos_paciente_recojo`
--

LOCK TABLES `pedidos_paciente_recojo` WRITE;
/*!40000 ALTER TABLE `pedidos_paciente_recojo` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedidos_paciente_recojo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos_paciente_recojo_o_presencial`
--

DROP TABLE IF EXISTS `pedidos_paciente_recojo_o_presencial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos_paciente_recojo_o_presencial` (
  `idpedidos_paciente_recojo_o_presencial` int NOT NULL AUTO_INCREMENT,
  `nombre_paciente` varchar(45) DEFAULT NULL,
  `apellido_paciente` varchar(45) DEFAULT NULL,
  `medico_que_atiende` varchar(45) DEFAULT NULL,
  `seguro` varchar(45) DEFAULT NULL,
  `telefono` int DEFAULT NULL,
  `dni` int DEFAULT NULL,
  `receta` varchar(45) DEFAULT NULL,
  `costo_total` double DEFAULT NULL,
  `tipo_de_pedido` varchar(45) DEFAULT NULL,
  `fecha_solicitud` varchar(45) DEFAULT NULL,
  `fecha_entrega` varchar(45) DEFAULT NULL,
  `validacion_del_pedido` varchar(45) DEFAULT NULL,
  `comentario` varchar(180) DEFAULT NULL,
  `fecha_validacion` varchar(45) DEFAULT NULL,
  `estado_del_pedido` varchar(45) DEFAULT NULL,
  `numero_tracking` varchar(45) DEFAULT NULL,
  `aviso_vencimiento` varchar(45) DEFAULT NULL,
  `sede_de_recojo` varchar(45) DEFAULT NULL,
  `usuario_id_usuario` int NOT NULL,
  PRIMARY KEY (`idpedidos_paciente_recojo_o_presencial`,`usuario_id_usuario`),
  KEY `fk_pedidos_paciente_recojo_o_presencial_usuario1_idx` (`usuario_id_usuario`),
  CONSTRAINT `fk_pedidos_paciente_recojo_o_presencial_usuario1` FOREIGN KEY (`usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos_paciente_recojo_o_presencial`
--

LOCK TABLES `pedidos_paciente_recojo_o_presencial` WRITE;
/*!40000 ALTER TABLE `pedidos_paciente_recojo_o_presencial` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedidos_paciente_recojo_o_presencial` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos_reposicion`
--

DROP TABLE IF EXISTS `pedidos_reposicion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos_reposicion` (
  `id_pedidos_reposicion` int NOT NULL AUTO_INCREMENT,
  `usuario_id_usuario` int NOT NULL,
  `fecha_solicitud` date DEFAULT NULL,
  `costo_total` double DEFAULT NULL,
  `fecha_entrega` date DEFAULT NULL,
  `estado_de_reposicion` varchar(45) DEFAULT NULL,
  `proveedor_id_proveedor` int NOT NULL,
  PRIMARY KEY (`id_pedidos_reposicion`,`usuario_id_usuario`,`proveedor_id_proveedor`),
  KEY `fk_ventas_usuario1_idx` (`usuario_id_usuario`),
  KEY `fk_pedidos_reposicion_proveedor1_idx` (`proveedor_id_proveedor`),
  CONSTRAINT `fk_pedidos_reposicion_proveedor1` FOREIGN KEY (`proveedor_id_proveedor`) REFERENCES `proveedor` (`id_proveedor`),
  CONSTRAINT `fk_ventas_usuario1` FOREIGN KEY (`usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos_reposicion`
--

LOCK TABLES `pedidos_reposicion` WRITE;
/*!40000 ALTER TABLE `pedidos_reposicion` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedidos_reposicion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos_reposicion_has_medicamentos`
--

DROP TABLE IF EXISTS `pedidos_reposicion_has_medicamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos_reposicion_has_medicamentos` (
  `pedidos_reposicion_id_pedidos_reposicion` int NOT NULL,
  `pedidos_reposicion_usuario_id_usuario` int NOT NULL,
  `pedidos_reposicion_proveedor_id_proveedor` int NOT NULL,
  `medicamentos_id_medicamentos` int NOT NULL,
  `cantidad` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`pedidos_reposicion_id_pedidos_reposicion`,`pedidos_reposicion_usuario_id_usuario`,`pedidos_reposicion_proveedor_id_proveedor`,`medicamentos_id_medicamentos`),
  KEY `fk_pedidos_reposicion_has_medicamentos_medicamentos1_idx` (`medicamentos_id_medicamentos`),
  KEY `fk_pedidos_reposicion_has_medicamentos_pedidos_reposicion1_idx` (`pedidos_reposicion_id_pedidos_reposicion`,`pedidos_reposicion_usuario_id_usuario`,`pedidos_reposicion_proveedor_id_proveedor`),
  CONSTRAINT `fk_pedidos_reposicion_has_medicamentos_medicamentos1` FOREIGN KEY (`medicamentos_id_medicamentos`) REFERENCES `medicamentos` (`id_medicamentos`),
  CONSTRAINT `fk_pedidos_reposicion_has_medicamentos_pedidos_reposicion1` FOREIGN KEY (`pedidos_reposicion_id_pedidos_reposicion`, `pedidos_reposicion_usuario_id_usuario`, `pedidos_reposicion_proveedor_id_proveedor`) REFERENCES `pedidos_reposicion` (`id_pedidos_reposicion`, `usuario_id_usuario`, `proveedor_id_proveedor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos_reposicion_has_medicamentos`
--

LOCK TABLES `pedidos_reposicion_has_medicamentos` WRITE;
/*!40000 ALTER TABLE `pedidos_reposicion_has_medicamentos` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedidos_reposicion_has_medicamentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proveedor`
--

DROP TABLE IF EXISTS `proveedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proveedor` (
  `id_proveedor` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) DEFAULT NULL,
  `descripcion` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id_proveedor`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proveedor`
--

LOCK TABLES `proveedor` WRITE;
/*!40000 ALTER TABLE `proveedor` DISABLE KEYS */;
/*!40000 ALTER TABLE `proveedor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id_roles` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_roles`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'Superadmin'),(2,'Admin'),(3,'Farmacista'),(4,'Paciente'),(5,'Doctor');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sede`
--

DROP TABLE IF EXISTS `sede`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sede` (
  `id_sede` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) DEFAULT NULL,
  `estado` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_sede`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sede`
--

LOCK TABLES `sede` WRITE;
/*!40000 ALTER TABLE `sede` DISABLE KEYS */;
/*!40000 ALTER TABLE `sede` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sede_has_medicamentos`
--

DROP TABLE IF EXISTS `sede_has_medicamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sede_has_medicamentos` (
  `sede_id_sede` int NOT NULL,
  `medicamentos_id_medicamentos` int NOT NULL,
  PRIMARY KEY (`sede_id_sede`,`medicamentos_id_medicamentos`),
  KEY `fk_sede_has_medicamentos_medicamentos1_idx` (`medicamentos_id_medicamentos`),
  KEY `fk_sede_has_medicamentos_sede1_idx` (`sede_id_sede`),
  CONSTRAINT `fk_sede_has_medicamentos_medicamentos1` FOREIGN KEY (`medicamentos_id_medicamentos`) REFERENCES `medicamentos` (`id_medicamentos`),
  CONSTRAINT `fk_sede_has_medicamentos_sede1` FOREIGN KEY (`sede_id_sede`) REFERENCES `sede` (`id_sede`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sede_has_medicamentos`
--

LOCK TABLES `sede_has_medicamentos` WRITE;
/*!40000 ALTER TABLE `sede_has_medicamentos` DISABLE KEYS */;
/*!40000 ALTER TABLE `sede_has_medicamentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spring_session`
--

DROP TABLE IF EXISTS `spring_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spring_session` (
  `PRIMARY_ID` char(36) NOT NULL,
  `SESSION_ID` char(36) NOT NULL,
  `CREATION_TIME` bigint NOT NULL,
  `LAST_ACCESS_TIME` bigint NOT NULL,
  `MAX_INACTIVE_INTERVAL` int NOT NULL,
  `EXPIRY_TIME` bigint NOT NULL,
  `PRINCIPAL_NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`PRIMARY_ID`),
  UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`),
  KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`),
  KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spring_session`
--

LOCK TABLES `spring_session` WRITE;
/*!40000 ALTER TABLE `spring_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `spring_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spring_session_attributes`
--

DROP TABLE IF EXISTS `spring_session_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spring_session_attributes` (
  `SESSION_PRIMARY_ID` char(36) NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`),
  CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `spring_session` (`PRIMARY_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spring_session_attributes`
--

LOCK TABLES `spring_session_attributes` WRITE;
/*!40000 ALTER TABLE `spring_session_attributes` DISABLE KEYS */;
/*!40000 ALTER TABLE `spring_session_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tarjeta`
--

DROP TABLE IF EXISTS `tarjeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tarjeta` (
  `id_tarjeta` int NOT NULL,
  `numero` int DEFAULT NULL,
  `fecha_caduca` date DEFAULT NULL,
  `cci` int DEFAULT NULL,
  PRIMARY KEY (`id_tarjeta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tarjeta`
--

LOCK TABLES `tarjeta` WRITE;
/*!40000 ALTER TABLE `tarjeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `tarjeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nombres` varchar(45) NOT NULL,
  `apellidos` varchar(45) NOT NULL,
  `correo` varchar(45) NOT NULL,
  `dni` int NOT NULL,
  `codigo_colegiatura` varchar(45) DEFAULT NULL,
  `distrito` varchar(45) DEFAULT NULL,
  `seguro` varchar(45) DEFAULT NULL,
  `estado` int DEFAULT NULL,
  `contrasena` varchar(100) NOT NULL,
  `foto` longblob,
  `fecha_creacion` date DEFAULT NULL,
  `estado_solicitud` varchar(45) DEFAULT NULL,
  `motivo_rechazo` varchar(250) DEFAULT NULL,
  `borrado_logico` int DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `imagen` varchar(255) DEFAULT NULL,
  `referencia` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `id_roles` int NOT NULL,
  `cuenta_activada` int DEFAULT NULL,
  `fecha_recuperacion` datetime DEFAULT NULL,
  `token_recuperacion` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id_usuario`,`id_roles`),
  KEY `fk_usuario_roles1_idx` (`id_roles`),
  CONSTRAINT `fk_usuario_roles1` FOREIGN KEY (`id_roles`) REFERENCES `roles` (`id_roles`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'Andres','Lujan','rodlu@gmail.com',1234567,'No tiene','San Juan de Lurigancho','Rimac',1,'$2a$10$u828b590H.p5.N5UZnLRxeVZrIiC/f/9/3AEREiwFeg9k9MLSt37W',NULL,'2024-04-29','Aceptado',NULL,0,NULL,NULL,NULL,NULL,4,1,NULL,NULL),(2,'Pedro','Perez','pedro@gmail.com',12345678,'No tiene','Lima','Rima',1,'$2a$12$eYN9rTOB.pVGwqLuD7At3.RFgHFWpEwsrzfN9SW7RjVVy0F7lQ73K',NULL,'2024-04-29','Aceptado',NULL,0,NULL,NULL,NULL,NULL,1,1,'2024-06-04 18:55:52','LQOGqcuÑgTV5HktH0yRw8Y9h3XEE5Ja2ahSbO3yJ'),(43,'elvis','perez','pepe@gmail.com',78945612,'Sin-Codigo','Lurigancho','',1,'$2a$12$TAYg.j0p1KwoCfyJadqw9ud54mi/ao2fyarFEVWyL1uNBhfM7OH0e',NULL,'2024-06-02','','',0,NULL,NULL,NULL,NULL,1,1,NULL,NULL),(44,'juan','pedro','hhh@gmail.com',78945661,'Sin-Codigo','peru ','rimac',0,'',NULL,'2024-06-04',NULL,NULL,0,'peru',NULL,NULL,NULL,4,0,NULL,'QNZTVq2nXrZ4omw3SpRZasbSuHgCVfQgOcH2c5Wy'),(45,'pedro','hola','jjjj@gmail.com',78945623,'Sin-Codigo','av peru ','Rimac',1,'$2a$10$hdQ4pC6zpwWe5s.1cBXdL.Z0birVcBQ5V8jjQzmblxeS.DTJq56vq',NULL,'2024-06-04',NULL,NULL,0,'Av bolivar',NULL,NULL,NULL,4,1,NULL,NULL),(46,'juan pedro','poll','po@gmail.com',45632178,'DOD008','Sin-Distrito','',1,'$2a$10$aHBzf1J67QCqkx5YmIluH.pwqrtXNW7jIO49NTOQqVLlaAZMCIlim',NULL,'2024-06-04','','',0,NULL,NULL,NULL,NULL,5,1,NULL,NULL),(47,'Juan Pablo','Perez Rodriguez','lll@gmail.com',96385274,'Sin-Codigo','Lurin','',1,'$2a$10$y7Mb0sVaokUk0iPF3IMRVOfQ2hx2SDShaxkwYSi5LuG/UYn5OXgGO',NULL,'2024-06-04','','',0,NULL,NULL,NULL,NULL,2,1,NULL,NULL),(49,'juancito','perez','coreldrain@gmail.com',45612323,'Sin-Codigo','rimac rim','rimac',1,'',NULL,'2024-06-04',NULL,NULL,0,'rimac',NULL,NULL,NULL,4,1,'2024-06-05 16:43:22','uGbwleVdedpMps3SnVhf4lk6MI2j8L5LBSdMTAX6');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_has_sede`
--

DROP TABLE IF EXISTS `usuario_has_sede`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario_has_sede` (
  `usuario_id_usuario` int NOT NULL,
  `sede_id_sede` int NOT NULL,
  PRIMARY KEY (`usuario_id_usuario`,`sede_id_sede`),
  KEY `fk_usuario_has_sede_sede1_idx` (`sede_id_sede`),
  KEY `fk_usuario_has_sede_usuario1_idx` (`usuario_id_usuario`),
  CONSTRAINT `fk_usuario_has_sede_sede1` FOREIGN KEY (`sede_id_sede`) REFERENCES `sede` (`id_sede`),
  CONSTRAINT `fk_usuario_has_sede_usuario1` FOREIGN KEY (`usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_has_sede`
--

LOCK TABLES `usuario_has_sede` WRITE;
/*!40000 ALTER TABLE `usuario_has_sede` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuario_has_sede` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-05 16:35:18
