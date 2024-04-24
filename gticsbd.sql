-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: gitcsbd
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `envios`
--

DROP TABLE IF EXISTS `envios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `envios` (
  `id_envios` int NOT NULL,
  `proveedor_id_proveedor` int NOT NULL,
  `descripcion` varchar(250) DEFAULT NULL,
  `fecha_registro` datetime DEFAULT NULL,
  `fecha_entrega` datetime DEFAULT NULL,
  `estado` varchar(45) DEFAULT NULL,
  `estado_de_envio` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_envios`),
  KEY `fk_envios_proveedor1_idx` (`proveedor_id_proveedor`),
  CONSTRAINT `fk_envios_proveedor1` FOREIGN KEY (`proveedor_id_proveedor`) REFERENCES `proveedor` (`id_proveedor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `envios`
--

LOCK TABLES `envios` WRITE;
/*!40000 ALTER TABLE `envios` DISABLE KEYS */;
/*!40000 ALTER TABLE `envios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicamentos`
--

DROP TABLE IF EXISTS `medicamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicamentos` (
  `id_medicamentos` int NOT NULL,
  `descripcion` varchar(250) DEFAULT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  `foto` blob,
  `inventario` int DEFAULT NULL,
  `precio_unidad` double DEFAULT NULL,
  `fecha_ingreso` datetime DEFAULT NULL,
  PRIMARY KEY (`id_medicamentos`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicamentos`
--

LOCK TABLES `medicamentos` WRITE;
/*!40000 ALTER TABLE `medicamentos` DISABLE KEYS */;
/*!40000 ALTER TABLE `medicamentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicamentos_has_pedidos`
--

DROP TABLE IF EXISTS `medicamentos_has_pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicamentos_has_pedidos` (
  `medicamentos_id_medicamentos` int NOT NULL,
  `pedidos_id_ventas` int NOT NULL,
  `pedidos_usuario_id_usuario` int NOT NULL,
  `cantidad` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`medicamentos_id_medicamentos`,`pedidos_id_ventas`,`pedidos_usuario_id_usuario`),
  KEY `fk_medicamentos_has_pedidos_pedidos2_idx` (`pedidos_id_ventas`,`pedidos_usuario_id_usuario`),
  KEY `fk_medicamentos_has_pedidos_medicamentos2_idx` (`medicamentos_id_medicamentos`),
  CONSTRAINT `fk_medicamentos_has_pedidos_medicamentos2` FOREIGN KEY (`medicamentos_id_medicamentos`) REFERENCES `medicamentos` (`id_medicamentos`),
  CONSTRAINT `fk_medicamentos_has_pedidos_pedidos2` FOREIGN KEY (`pedidos_id_ventas`, `pedidos_usuario_id_usuario`) REFERENCES `pedidos` (`id_ventas`, `usuario_id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicamentos_has_pedidos`
--

LOCK TABLES `medicamentos_has_pedidos` WRITE;
/*!40000 ALTER TABLE `medicamentos_has_pedidos` DISABLE KEYS */;
/*!40000 ALTER TABLE `medicamentos_has_pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos`
--

DROP TABLE IF EXISTS `pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos` (
  `id_ventas` int NOT NULL,
  `usuario_id_usuario` int NOT NULL,
  `fecha_solicitud` datetime DEFAULT NULL,
  `costo_total` double DEFAULT NULL,
  `estado_del_pedido` varchar(45) DEFAULT NULL,
  `tipo` varchar(45) DEFAULT NULL,
  `receta` blob,
  `fecha_entrega` datetime DEFAULT NULL,
  `comentario` varchar(45) DEFAULT NULL,
  `fecha_aprobacion` datetime DEFAULT NULL,
  `medico_que_atiende` varchar(45) DEFAULT NULL,
  `envios_id_envios` int NOT NULL,
  PRIMARY KEY (`id_ventas`,`usuario_id_usuario`,`envios_id_envios`),
  KEY `fk_ventas_usuario1_idx` (`usuario_id_usuario`),
  KEY `fk_pedidos_envios1_idx` (`envios_id_envios`),
  CONSTRAINT `fk_pedidos_envios1` FOREIGN KEY (`envios_id_envios`) REFERENCES `envios` (`id_envios`),
  CONSTRAINT `fk_ventas_usuario1` FOREIGN KEY (`usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos`
--

LOCK TABLES `pedidos` WRITE;
/*!40000 ALTER TABLE `pedidos` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proveedor`
--

DROP TABLE IF EXISTS `proveedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proveedor` (
  `id_proveedor` int NOT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  `descripcion` varchar(250) DEFAULT NULL,
  `proveedorcol` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_proveedor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proveedor`
--

LOCK TABLES `proveedor` WRITE;
/*!40000 ALTER TABLE `proveedor` DISABLE KEYS */;
/*!40000 ALTER TABLE `proveedor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sede`
--

DROP TABLE IF EXISTS `sede`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sede` (
  `id_sede` int NOT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  `estado` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_sede`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sede`
--

LOCK TABLES `sede` WRITE;
/*!40000 ALTER TABLE `sede` DISABLE KEYS */;
/*!40000 ALTER TABLE `sede` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `solicitudes`
--

DROP TABLE IF EXISTS `solicitudes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `solicitudes` (
  `id_solicitudes` int NOT NULL,
  `fecha_solicitud` datetime DEFAULT NULL,
  `estado` varchar(45) DEFAULT NULL,
  `descripcion` varchar(250) DEFAULT NULL,
  `usuario_id_usuario` int NOT NULL,
  PRIMARY KEY (`id_solicitudes`,`usuario_id_usuario`),
  KEY `fk_solicitudes_usuario1_idx` (`usuario_id_usuario`),
  CONSTRAINT `fk_solicitudes_usuario1` FOREIGN KEY (`usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `solicitudes`
--

LOCK TABLES `solicitudes` WRITE;
/*!40000 ALTER TABLE `solicitudes` DISABLE KEYS */;
/*!40000 ALTER TABLE `solicitudes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tarjeta`
--

DROP TABLE IF EXISTS `tarjeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tarjeta` (
  `id_tarjeta` int NOT NULL,
  `numero` varchar(45) DEFAULT NULL,
  `fecha_caduca` varchar(45) DEFAULT NULL,
  `cci` varchar(45) DEFAULT NULL,
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
  `id_usuario` int NOT NULL,
  `nombres` varchar(45) NOT NULL,
  `apellidos` varchar(45) NOT NULL,
  `correo` varchar(45) NOT NULL,
  `dni` int NOT NULL,
  `codigo_colegiatura` varchar(45) DEFAULT NULL,
  `distrito` varchar(45) DEFAULT NULL,
  `seguro` varchar(45) DEFAULT NULL,
  `estado` varchar(45) DEFAULT NULL,
  `rol` varchar(45) NOT NULL,
  `contrasena` varchar(45) NOT NULL,
  `foto` blob,
  `fecha_creacion` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_has_medicamentos`
--

DROP TABLE IF EXISTS `usuario_has_medicamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario_has_medicamentos` (
  `usuario_id_usuario` int NOT NULL,
  `medicamentos_id_medicamentos` int NOT NULL,
  PRIMARY KEY (`usuario_id_usuario`,`medicamentos_id_medicamentos`),
  KEY `fk_usuario_has_medicamentos_medicamentos1_idx` (`medicamentos_id_medicamentos`),
  KEY `fk_usuario_has_medicamentos_usuario1_idx` (`usuario_id_usuario`),
  CONSTRAINT `fk_usuario_has_medicamentos_medicamentos1` FOREIGN KEY (`medicamentos_id_medicamentos`) REFERENCES `medicamentos` (`id_medicamentos`),
  CONSTRAINT `fk_usuario_has_medicamentos_usuario1` FOREIGN KEY (`usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_has_medicamentos`
--

LOCK TABLES `usuario_has_medicamentos` WRITE;
/*!40000 ALTER TABLE `usuario_has_medicamentos` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuario_has_medicamentos` ENABLE KEYS */;
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

-- Dump completed on 2024-04-24 12:03:21
