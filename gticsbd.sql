CREATE DATABASE  IF NOT EXISTS `gticsbd` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `gticsbd`;
-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: gticsbd
-- ------------------------------------------------------
-- Server version	8.0.30

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
  `carrito_id` int NOT NULL,
  `usuario_id_usuario` int NOT NULL,
  `medicamentos_id_medicamentos` int NOT NULL,
  `cantidad` int DEFAULT NULL,
  PRIMARY KEY (`carrito_id`,`usuario_id_usuario`,`medicamentos_id_medicamentos`),
  KEY `fk_usuario_has_medicamentos_medicamentos1_idx` (`medicamentos_id_medicamentos`),
  KEY `fk_usuario_has_medicamentos_usuario1_idx` (`usuario_id_usuario`),
  CONSTRAINT `fk_usuario_has_medicamentos_medicamentos1` FOREIGN KEY (`medicamentos_id_medicamentos`) REFERENCES `medicamentos` (`id_medicamentos`),
  CONSTRAINT `fk_usuario_has_medicamentos_usuario1` FOREIGN KEY (`usuario_id_usuario`) REFERENCES `usuario` (`id_usuario`)
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
  `id_medicamentos` int NOT NULL,
  `descripcion` varchar(400) DEFAULT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  `foto` blob,
  `inventario` int DEFAULT NULL,
  `precio_unidad` double DEFAULT NULL,
  `fecha_ingreso` date DEFAULT NULL,
  `categoria` varchar(45) DEFAULT NULL,
  `dosis` varchar(45) DEFAULT NULL,
  `borrado_logico` int DEFAULT NULL,
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
-- Table structure for table `pedidos_paciente`
--

DROP TABLE IF EXISTS `pedidos_paciente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos_paciente` (
  `idpedidos_paciente` int NOT NULL,
  `medico_que_atiende` varchar(45) DEFAULT NULL,
  `seguro` varchar(45) DEFAULT NULL,
  `direccion` varchar(45) DEFAULT NULL,
  `distrito` varchar(45) DEFAULT NULL,
  `telefono` int DEFAULT NULL,
  `dni` int DEFAULT NULL,
  `hora_de_entrega` time DEFAULT NULL,
  `receta` blob,
  `costo_total` double DEFAULT NULL,
  `tipo_de_pedido` varchar(45) DEFAULT NULL,
  `fecha_solicitud` date DEFAULT NULL,
  `fecha_entrega` date DEFAULT NULL,
  `validacion_del_pedido` varchar(45) DEFAULT NULL,
  `comentario` varchar(45) DEFAULT NULL,
  `fecha_validacion` date DEFAULT NULL,
  `estado_del_pedido` varchar(45) DEFAULT NULL,
  `numero_tracking` varchar(45) DEFAULT NULL,
  `carrito_carrito_id` int NOT NULL,
  `carrito_usuario_id_usuario` int NOT NULL,
  `carrito_medicamentos_id_medicamentos` int NOT NULL,
  PRIMARY KEY (`idpedidos_paciente`,`carrito_carrito_id`,`carrito_usuario_id_usuario`,`carrito_medicamentos_id_medicamentos`),
  KEY `fk_pedidos_paciente_carrito1_idx` (`carrito_carrito_id`,`carrito_usuario_id_usuario`,`carrito_medicamentos_id_medicamentos`),
  CONSTRAINT `fk_pedidos_paciente_carrito1` FOREIGN KEY (`carrito_carrito_id`, `carrito_usuario_id_usuario`, `carrito_medicamentos_id_medicamentos`) REFERENCES `carrito` (`carrito_id`, `usuario_id_usuario`, `medicamentos_id_medicamentos`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos_paciente`
--

LOCK TABLES `pedidos_paciente` WRITE;
/*!40000 ALTER TABLE `pedidos_paciente` DISABLE KEYS */;
/*!40000 ALTER TABLE `pedidos_paciente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos_reposicion`
--

DROP TABLE IF EXISTS `pedidos_reposicion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos_reposicion` (
  `id_pedidos_reposicion` int NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
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
  `pedidos_reposicion_envios_id_envios` int NOT NULL,
  `medicamentos_id_medicamentos` int NOT NULL,
  `cantidad` int DEFAULT NULL,
  PRIMARY KEY (`pedidos_reposicion_id_pedidos_reposicion`,`pedidos_reposicion_usuario_id_usuario`,`pedidos_reposicion_envios_id_envios`,`medicamentos_id_medicamentos`),
  KEY `fk_pedidos_reposicion_has_medicamentos_medicamentos1_idx` (`medicamentos_id_medicamentos`),
  KEY `fk_pedidos_reposicion_has_medicamentos_pedidos_reposicion1_idx` (`pedidos_reposicion_id_pedidos_reposicion`,`pedidos_reposicion_usuario_id_usuario`,`pedidos_reposicion_envios_id_envios`),
  CONSTRAINT `fk_pedidos_reposicion_has_medicamentos_medicamentos1` FOREIGN KEY (`medicamentos_id_medicamentos`) REFERENCES `medicamentos` (`id_medicamentos`),
  CONSTRAINT `fk_pedidos_reposicion_has_medicamentos_pedidos_reposicion1` FOREIGN KEY (`pedidos_reposicion_id_pedidos_reposicion`, `pedidos_reposicion_usuario_id_usuario`) REFERENCES `pedidos_reposicion` (`id_pedidos_reposicion`, `usuario_id_usuario`)
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
  `id_proveedor` int NOT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  `descripcion` varchar(250) DEFAULT NULL,
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
  `fecha_creacion` date DEFAULT NULL,
  `estado_solicitud` varchar(45) DEFAULT NULL,
  `motivo_rechazo` varchar(250) DEFAULT NULL,
  `borrado_logico` int DEFAULT NULL,
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

-- Dump completed on 2024-04-27 21:00:53
